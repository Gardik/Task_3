package tests;

import config.ApiUser;
import drivers.DriverFactory;
import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;

import pages.*;
import UserAuthorization.RegisterUser;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Вход пользователя — разные пути")
class LoginFlowTest {

    private WebDriver driver;

    private final DriverFactory driverFactory = new DriverFactory();
    private final ApiUser apiUser = new ApiUser();

    private RegisterUser user;

    private MainPage mainPage;
    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private ForgotPasswordPage forgotPasswordPage;
    private AccountPage accountPage;

    private void init(String browser) {

        if ("yandex".equalsIgnoreCase(browser)) {
            String yandexBin = System.getProperty("webdriver.yandex.bin");
            Assumptions.assumeTrue(yandexBin != null && !yandexBin.isBlank(),
                    "Пропуск: не задано -Dwebdriver.yandex.bin для Yandex Browser");
        }

        driver = driverFactory.createDriver(browser);

        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        registrationPage = new RegistrationPage(driver);
        forgotPasswordPage = new ForgotPasswordPage(driver);
        accountPage = new AccountPage(driver);

        user = apiUser.createTestUser();
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

    private void assertLoggedIn() {
        boolean loggedIn = mainPage.isOrderButtonVisible() || accountPage.isLoaded();
        assertTrue(loggedIn,
                "Ожидали успешный вход (главная с 'Оформить заказ' или профиль с 'Выход')");
    }

    @ParameterizedTest(name = "Вход по кнопке 'Войти в аккаунт' на главной ({0})")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Проверка: вход по кнопке «Войти в аккаунт» на главной")
    void loginFromMainLoginButton(String browser) {
        init(browser);

        mainPage.open();
        mainPage.clickAccountButton();

        assertTrue(loginPage.isPageLoaded(), "Должна открыться форма входа");

        loginPage.login(user.getEmail(), user.getPassword());
        assertLoggedIn();
    }

    @ParameterizedTest(name = "Вход через 'Личный кабинет' ({0})")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Проверка: вход через кнопку «Личный кабинет»")
    void loginFromPersonalAccountButton(String browser) {
        init(browser);

        mainPage.open();
        mainPage.clickPersonalAccountButton();

        assertTrue(loginPage.isPageLoaded(), "Должна открыться форма входа");

        loginPage.login(user.getEmail(), user.getPassword());
        assertLoggedIn();
    }

    @ParameterizedTest(name = "Вход через кнопку на форме регистрации ({0})")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Проверка: вход через кнопку в форме регистрации")
    void loginFromRegistrationForm(String browser) {
        init(browser);

        registrationPage.open();
        registrationPage.clickLoginLink();

        assertTrue(loginPage.isPageLoaded(), "Должна открыться форма входа");

        loginPage.login(user.getEmail(), user.getPassword());
        assertLoggedIn();
    }

    @ParameterizedTest(name = "Вход через кнопку на форме восстановления пароля ({0})")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Проверка: вход через кнопку в форме восстановления пароля")
    void loginFromForgotPasswordForm(String browser) {
        init(browser);

        forgotPasswordPage.open();
        forgotPasswordPage.clickLoginLink();

        assertTrue(loginPage.isPageLoaded(), "Должна открыться форма входа");

        loginPage.login(user.getEmail(), user.getPassword());
        assertLoggedIn();
    }
}
