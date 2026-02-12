package tests;

import drivers.DriverFactory;
import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;

import pages.MainPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Конструктор: переходы по разделам")
class ConstructorSectionsTest {

    private WebDriver driver;
    private final DriverFactory driverFactory = new DriverFactory();

    private MainPage mainPage;

    private void init(String browser) {
        driver = driverFactory.createDriver(browser);
        mainPage = new MainPage(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @ParameterizedTest(name = "Переход к 'Соусы' ({0})")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Проверка перехода к разделу 'Соусы' в конструкторе.")
    void shouldSwitchToSaucesSection(String browser) {
        init(browser);

        mainPage.open();
        mainPage.goToSauces();

        assertTrue(mainPage.isSaucesActive(), "Должен быть активен таб 'Соусы'");
    }

    @ParameterizedTest(name = "Переход к 'Начинки' ({0})")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Проверка перехода к разделу 'Начинки' в конструкторе.")
    void shouldSwitchToFillingsSection(String browser) {
        init(browser);

        mainPage.open();
        mainPage.goToFillings();

        assertTrue(mainPage.isFillingsActive(), "Должен быть активен таб 'Начинки'");
    }

    @ParameterizedTest(name = "Переход к 'Булки' ({0})")
    @ValueSource(strings = {"chrome", "yandex"})
    @Description("Проверка перехода к разделу 'Булки' в конструкторе.")
    void shouldSwitchToBunsSection(String browser) {
        init(browser);

        mainPage.open();

        mainPage.goToSauces();
        assertTrue(mainPage.isSaucesActive(), "Подготовка: таб 'Соусы' должен стать активным");

        mainPage.goToBuns();
        assertTrue(mainPage.isBunsActive(), "Должен быть активен таб 'Булки'");
    }
}
