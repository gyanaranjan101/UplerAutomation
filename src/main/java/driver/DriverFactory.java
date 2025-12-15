package driver;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {
        WebDriver webDriver = new ChromeDriver();

        webDriver.manage()
                 .timeouts()
                 .implicitlyWait(Duration.ofSeconds(10));

        webDriver.manage().window().maximize();
        driver.set(webDriver);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {   // ✅ CRITICAL FIX
            driver.get().quit();
            driver.remove();
        }
    }
}
