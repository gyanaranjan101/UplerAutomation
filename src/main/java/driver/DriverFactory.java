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
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            webDriver.quit();
            driver.remove();
            System.out.println("✅ Browser closed successfully");
        } else {
            System.out.println("ℹ️ Driver already null");
        }
    }

}
