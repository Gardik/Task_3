package drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class DriverFactory {

    public WebDriver createDriver(String browser) {

        String b = (browser == null || browser.isBlank())
                ? System.getProperty("browser", "chrome")
                : browser;


        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        WebDriver driver;
        switch (b.toLowerCase()) {
            case "firefox":
                driver = createFirefox(headless);
                break;

            case "yandex":
            case "ya":
            case "yandexbrowser":
                driver = createYandex(headless);
                break;

            case "chrome":
            default:
                driver = createChrome(headless);
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ZERO); // НЕ используем implicit, только явные ожидания
        driver.manage().window().maximize();
        return driver;
    }

    private WebDriver createChrome(boolean headless) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");

        return new ChromeDriver(options);
    }

    private WebDriver createFirefox(boolean headless) {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }

        return new FirefoxDriver(options);
    }

    private WebDriver createYandex(boolean headless) {

        String yandexBinary = resolveYandexBinary();

        String driverVersion = System.getProperty("driver.version");
        if (driverVersion != null && !driverVersion.isBlank()) {
            WebDriverManager.chromedriver().driverVersion(driverVersion).setup();
        } else {
            WebDriverManager.chromedriver().setup();
        }

        ChromeOptions options = new ChromeOptions();
        options.setBinary(yandexBinary);

        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");

        return new ChromeDriver(options);

    }


    private String resolveYandexBinary() {
        String bin = firstNonBlank(
                System.getProperty("webdriver.yandex.bin"),
                System.getProperty("yandex.bin"),
                System.getProperty("yandexBinary")
        );
        bin = stripQuotes(bin);
        if (isExistingFile(bin)) return bin;

        bin = firstNonBlank(
                System.getenv("YANDEX_BROWSER_BIN"),
                System.getenv("YANDEX_BIN")
        );
        bin = stripQuotes(bin);
        if (isExistingFile(bin)) return bin;

        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            return resolveYandexBinaryWindows();
        } else if (os.contains("mac")) {
            return resolveYandexBinaryMac();
        } else {
            return resolveYandexBinaryLinux();
        }
    }

    private String resolveYandexBinaryWindows() {
        String userHome = System.getProperty("user.home");

        String[] candidates = new String[] {
                userHome + "\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe",
                "C:\\Program Files\\Yandex\\YandexBrowser\\Application\\browser.exe",
                "C:\\Program Files (x86)\\Yandex\\YandexBrowser\\Application\\browser.exe"
        };

        for (String c : candidates) {
            if (isExistingFile(c)) return c;
        }

        throw new IllegalStateException(buildYandexNotFoundMessage());
    }

    private String resolveYandexBinaryMac() {
        String[] candidates = new String[] {
                "/Applications/Yandex.app/Contents/MacOS/Yandex",
                "/Applications/Yandex Browser.app/Contents/MacOS/Yandex Browser"
        };

        for (String c : candidates) {
            if (isExistingFile(c)) return c;
        }

        throw new IllegalStateException(buildYandexNotFoundMessage());
    }

    private String resolveYandexBinaryLinux() {
        String[] candidates = new String[] {
                "/usr/bin/yandex-browser",
                "/usr/bin/yandex-browser-stable",
                "/opt/yandex/browser/yandex_browser"
        };

        for (String c : candidates) {
            if (isExistingFile(c)) return c;
        }

        throw new IllegalStateException(buildYandexNotFoundMessage());
    }

    private String buildYandexNotFoundMessage() {
        return "Не найден бинарник Yandex Browser.\n" +
                "Задай один из вариантов:\n" +
                "1) VM options / Maven:\n" +
                "   -Dwebdriver.yandex.bin=\"C:\\\\...\\\\YandexBrowser\\\\Application\\\\browser.exe\"\n" +
                "2) Переменная окружения:\n" +
                "   YANDEX_BROWSER_BIN=C:\\\\...\\\\YandexBrowser\\\\Application\\\\browser.exe\n" +
                "Также можно использовать свойства: yandex.bin или yandexBinary.";
    }

    private boolean isExistingFile(String path) {
        if (path == null || path.isBlank()) return false;
        try {
            Path p = Paths.get(path);
            return Files.exists(p) && Files.isRegularFile(p);
        } catch (Exception e) {
            return false;
        }
    }

    private String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private String stripQuotes(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.length() >= 2 && ((t.startsWith("\"") && t.endsWith("\"")) || (t.startsWith("'") && t.endsWith("'")))) {
            return t.substring(1, t.length() - 1);
        }
        return t;
    }
}

