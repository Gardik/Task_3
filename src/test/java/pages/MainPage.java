package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static config.EvnConfig.MAIN_PAGE;

public class MainPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Кнопка "Войти в аккаунт" на главной
    private final By loginButton = By.xpath("//button[normalize-space()='Войти в аккаунт']");

    // "Личный кабинет" в шапке (без хэш-классов)
    private final By personalAccountButton =
            By.xpath("//*[normalize-space()='Личный Кабинет' or normalize-space()='Личный кабинет']");

    // Признак успешного логина на главной
    private final By orderButton = By.xpath("//button[normalize-space()='Оформить заказ']");

    // Признак конструктора на главной
    private final By constructorTitle = By.xpath("//h1[contains(.,'Соберите бургер')]");

    // Кликаем по табам через текст
    private final By bunsTab = By.xpath("//span[normalize-space()='Булки']/parent::*");
    private final By saucesTab = By.xpath("//span[normalize-space()='Соусы']/parent::*");
    private final By fillingsTab = By.xpath("//span[normalize-space()='Начинки']/parent::*");


    private final By bunsTabActive = By.xpath("//div[contains(@class,'tab_tab_type_current')]//span[normalize-space()='Булки']");
    private final By saucesTabActive = By.xpath("//div[contains(@class,'tab_tab_type_current')]//span[normalize-space()='Соусы']");
    private final By fillingsTabActive = By.xpath("//div[contains(@class,'tab_tab_type_current')]//span[normalize-space()='Начинки']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Открываем главную страницу")
    public void open() {
        driver.get(MAIN_PAGE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(personalAccountButton));
        wait.until(ExpectedConditions.visibilityOfElementLocated(constructorTitle));
    }

    @Step("Кликаем 'Войти в аккаунт' на главной")
    public void clickAccountButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    @Step("Кликаем 'Личный кабинет' на главной")
    public void clickPersonalAccountButton() {
        wait.until(ExpectedConditions.elementToBeClickable(personalAccountButton)).click();
    }

    @Step("Проверяем: кнопка 'Оформить заказ' видна (пользователь залогинен)")
    public boolean isOrderButtonVisible() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(orderButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверяем: открыта страница конструктора (виден заголовок 'Соберите бургер')")
    public boolean isConstructorLoaded() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(constructorTitle));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Step("Переходим к разделу 'Булки'")
    public void goToBuns() {
        wait.until(ExpectedConditions.elementToBeClickable(bunsTab)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(bunsTabActive));
    }

    @Step("Переходим к разделу 'Соусы'")
    public void goToSauces() {
        wait.until(ExpectedConditions.elementToBeClickable(saucesTab)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(saucesTabActive));
    }

    @Step("Переходим к разделу 'Начинки'")
    public void goToFillings() {
        wait.until(ExpectedConditions.elementToBeClickable(fillingsTab)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(fillingsTabActive));
    }

    @Step("Проверяем: активен таб 'Булки'")
    public boolean isBunsActive() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(bunsTabActive))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверяем: активен таб 'Соусы'")
    public boolean isSaucesActive() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(saucesTabActive))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверяем: активен таб 'Начинки'")
    public boolean isFillingsActive() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(fillingsTabActive))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
