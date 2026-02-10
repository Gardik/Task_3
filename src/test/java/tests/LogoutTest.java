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

@DisplayName("Выход из аккаунта")
class LogoutTest {

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

        user = apiUser.createTestUser();
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

    @ParameterizedTest(name = "Выход по кнопке 'Выход' в ЛК ({0})")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Проверь выход по кнопке «Выйти» (Выход) в личном кабинете.")
    void shouldLogoutFromAccount(String browser) {
        init(browser);
        loginViaUi();

        // Переходим в ЛК
        mainPage.open();
        mainPage.clickPersonalAccountButton();
        assertTrue(accountPage.isLoaded(), "Личный кабинет должен открыться перед выходом");

        // Выходим
        accountPage.clickLogout();

        // Проверяем, что мы вернулись на страницу входа
        assertTrue(loginPage.isPageLoaded(), "После выхода должна открыться страница входа");
    }
}
