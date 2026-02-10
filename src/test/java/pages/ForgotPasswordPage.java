package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static config.EvnConfig.FORGOT_PASSWORD_PAGE;

public class ForgotPasswordPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By restoreButton = By.xpath("//button[contains(.,'Восстановить')]");
    private final By loginLink = By.xpath("//a[normalize-space()='Войти']");

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Открываем страницу восстановления пароля")
    public void open() {
        driver.get(FORGOT_PASSWORD_PAGE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(restoreButton));
    }

    @Step("На форме восстановления кликаем 'Войти'")
    public void clickLoginLink() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }
}
