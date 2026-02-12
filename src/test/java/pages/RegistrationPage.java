package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static config.EvnConfig.REGISTER_PAGE;

public class RegistrationPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By nameInput = By.xpath("//label[normalize-space()='Имя']/..//input");
    private final By emailInput = By.xpath("//label[normalize-space()='Email']/..//input");
    private final By passwordInput = By.xpath("//label[normalize-space()='Пароль']/..//input");

    private final By registerButton = By.xpath("//button[normalize-space()='Зарегистрироваться']");
    private final By loginLink = By.xpath("//a[normalize-space()='Войти']");


    private final By passwordError = By.xpath(
            "//*[contains(@class,'input__error') and normalize-space()='Некорректный пароль']"
    );

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Открываем страницу регистрации")
    public void open() {
        driver.get(REGISTER_PAGE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(registerButton));
    }

    // Регистрация

    @Step("Заполняем форму регистрации (Имя, Email, Пароль)")
    public void fillForm(String name, String email, String password) {
        type(nameInput, name);
        type(emailInput, email);
        type(passwordInput, password);
    }

    @Step("Нажимаем кнопку 'Зарегистрироваться'")
    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();
    }

    @Step("Уводим фокус с поля пароля (триггер валидации)")
    public void blurPasswordField() {
        WebElement pass = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        pass.sendKeys(Keys.TAB);
    }

    @Step("Проверяем: ошибка 'Некорректный пароль' отображается")
    public boolean isPasswordErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordError)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Читаем текст ошибки пароля")
    public String getPasswordErrorText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordError)).getText();
    }

    //Вход через форму регистрации

    @Step("На форме регистрации кликаем 'Войти'")
    public void clickLoginLink() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }

    private void type(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(value);
    }
}
