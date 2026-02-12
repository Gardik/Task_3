package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static config.EvnConfig.LOGIN_PAGE;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By emailInput = By.xpath("//label[normalize-space()='Email']/..//input");
    private final By passwordInput = By.xpath("//label[normalize-space()='Пароль']/..//input");

    private final By loginButton = By.xpath("//button[normalize-space()='Войти']");
    private final By loginTitle = By.xpath("//h2[normalize-space()='Вход']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Открываем страницу входа")
    public void open() {
        driver.get(LOGIN_PAGE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginTitle));
    }

    @Step("Проверяем: страница входа загружена")
    public boolean isPageLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loginTitle)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Логинимся (email/password)")
    public void login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    private void type(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(value);
    }
}
