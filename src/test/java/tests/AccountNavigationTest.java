package tests;

import UserAuthorization.RegisterUser;
import config.ApiUser;
import drivers.DriverFactory;
import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;

import pages.AccountPage;
import pages.LoginPage;
import pages.MainPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Личный кабинет и переходы в конструктор")
class AccountNavigationTest {

    private WebDriver driver;

    private final DriverFactory driverFactory = new DriverFactory();
    private final ApiUser apiUser = new ApiUser();

    private RegisterUser user;

    private MainPage mainPage;
    private LoginPage loginPage;
    private AccountPage accountPage;

    private void init(String browser) {
        driver = driverFactory.createDriver(browser);

        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        accountPage = new AccountPage(driver);

        user = apiUser.createTestUser(); // создаём пользователя через API
    }

    private void loginViaUi() {
        mainPage.open();
        mainPage.clickAccountButton();

        assertTrue(loginPage.isPageLoaded(), "Должна открыться форма входа");

        loginPage.login(user.getEmail(), user.getPassword());

        assertTrue(mainPage.isOrderButtonVisible(),
                "После логина на главной должна быть кнопка 'Оформить заказ'");
    }

    @AfterEach
    void tearDown() {
        if (user != null) {
            try {
                apiUser.deleteUser(user);
            } catch (Exception e) {
                apiUser.logUserInfo(user);
            }
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @ParameterizedTest(name = "Переход в ЛК по клику 'Личный кабинет' ({0})")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Проверь переход по клику на «Личный кабинет».")
    void shouldOpenAccountByPersonalAccountClick(String browser) {
        init(browser);
        loginViaUi();

        mainPage.open();
        mainPage.clickPersonalAccountButton();

        assertTrue(accountPage.isLoaded(),
                "Ожидали открытие личного кабинета (видна кнопка 'Выход')");
    }

    @ParameterizedTest(name = "Переход из ЛК в конструктор по 'Конструктор' ({0})")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Проверь переход из личного кабинета в конструктор по клику на «Конструктор».")
    void shouldGoToConstructorFromAccountByConstructorLink(String browser) {
        init(browser);
        loginViaUi();

        mainPage.open();
        mainPage.clickPersonalAccountButton();
        assertTrue(accountPage.isLoaded(), "ЛК должен открыться перед проверкой перехода");

        accountPage.clickConstructor();

        assertTrue(mainPage.isConstructorLoaded(),
                "После клика 'Конструктор' должна открыться главная (конструктор)");
    }

    @ParameterizedTest(name = "Переход из ЛК в конструктор по логотипу ({0})")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Проверь переход из личного кабинета в конструктор по клику на логотип Stellar Burgers.")
    void shouldGoToConstructorFromAccountByLogo(String browser) {
        init(browser);
        loginViaUi();

        mainPage.open();
        mainPage.clickPersonalAccountButton();
        assertTrue(accountPage.isLoaded(), "ЛК должен открыться перед проверкой перехода");

        accountPage.clickLogo();

        assertTrue(mainPage.isConstructorLoaded(),
                "После клика по логотипу должна открыться главная (конструктор)");
    }
}
