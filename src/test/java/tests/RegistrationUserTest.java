package tests;

import UserAuthorization.RegisterUser;
import config.ApiUser;
import drivers.DriverFactory;
import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;

import pages.LoginPage;
import pages.MainPage;
import pages.RegistrationPage;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Регистрация пользователя")
class RegistrationUserTest {

    private WebDriver driver;

    private final DriverFactory driverFactory = new DriverFactory();
    private final ApiUser apiUser = new ApiUser();

    private RegistrationPage registrationPage;
    private LoginPage loginPage;
    private MainPage mainPage;

    // Удаляем пользователя
    private RegisterUser createdUser;

    private void init(String browser) {

        if ("yandex".equalsIgnoreCase(browser)) {
            String yandexBin = System.getProperty("webdriver.yandex.bin");
            Assumptions.assumeTrue(yandexBin != null && !yandexBin.isBlank(),
                    "Пропуск: не задано -Dwebdriver.yandex.bin для Yandex Browser");
        }

        driver = driverFactory.createDriver(browser);

        registrationPage = new RegistrationPage(driver);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);

        createdUser = null;
    }

    @AfterEach
    void tearDown() {
        if (createdUser != null) {
            try {
                apiUser.deleteUser(createdUser);
            } catch (Exception e) {
                apiUser.logUserInfo(createdUser);
            }
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @ParameterizedTest(name = "Успешная регистрация в браузере: {0}")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Успешная регистрация: пароль >= 6 символов")
    void successfulRegistrationWithValidData(String browser) {
        init(browser);

        String name = "Иван Иванов";
        String email = "test" + System.currentTimeMillis() + "@example.com";
        String password = "Password123"; // >= 6


        createdUser = new RegisterUser(email, password, name);

        registrationPage.open();
        registrationPage.fillForm(name, email, password);
        registrationPage.submit();


        assertTrue(loginPage.isPageLoaded(), "После успешной регистрации должна открыться страница входа");


        loginPage.login(email, password);
        assertTrue(mainPage.isOrderButtonVisible(), "После входа должна появиться кнопка 'Оформить заказ'");
    }

    @ParameterizedTest(name = "Ошибка короткого пароля в браузере: {0}")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Ошибка: пароль меньше 6 символов")
    void registrationWithShortPasswordShouldShowError(String browser) {
        init(browser);

        String name = "Иван Иванов";
        String email = "test" + System.currentTimeMillis() + "@example.com";
        String shortPassword = "12345"; // < 6

        registrationPage.open();
        registrationPage.fillForm(name, email, shortPassword);


        registrationPage.blurPasswordField();

        assertTrue(registrationPage.isPasswordErrorDisplayed(),
                "Должна отображаться ошибка для короткого пароля");

        assertEquals("Некорректный пароль", registrationPage.getPasswordErrorText(),
                "Ожидали точный текст ошибки: 'Некорректный пароль'");
    }
}
