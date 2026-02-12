package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By logoutButton = By.xpath("//button[normalize-space()='Выход']");

    private final By constructorLink = By.xpath(
            "//a[.//p[normalize-space()='Конструктор']] | " +
                    "//a[normalize-space()='Конструктор'] | " +
                    "//p[normalize-space()='Конструктор']/ancestor::a"
    );

    private final By logoLink = By.cssSelector("a[href='/']");

    public AccountPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Проверяем: личный кабинет открыт (видна кнопка 'Выход')")
    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Переходим в конструктор по клику 'Конструктор'")
    public void clickConstructor() {
        wait.until(ExpectedConditions.elementToBeClickable(constructorLink)).click();
    }

    @Step("Переходим в конструктор по клику на логотип")
    public void clickLogo() {
        wait.until(ExpectedConditions.elementToBeClickable(logoLink)).click();
    }

    @Step("Выходим из аккаунта (клик по кнопке 'Выход')")
    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }
}

