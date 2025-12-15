package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.UtilsMethods;

public class LoginPage {

    private WebDriver driver;

    // ✅ ONLY locators (NO findElement here)
    private By username = By.name("username");
    private By password = By.name("password");
    private By loginBtn = By.xpath("//button[@type='submit']");
    private By form     = By.xpath("//form");
    private By pageTitle=By.xpath("//div[contains(@class,'topbar-header-title')]//h6");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isLoginPageDisplayed() {
        return driver.findElement(form).isDisplayed();
    }

    public void performLogin() {
        String user=UtilsMethods.getProperty("userName");
        String pass =UtilsMethods.getProperty("password");
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(loginBtn).click();
    }

    public String getPageTitle() {
        return driver.findElement(pageTitle).getText();
    }

    public boolean isLoginPageDisplayedAfterLogout() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    wait.until(ExpectedConditions.urlContains("/auth/login"));

    return driver.findElement(By.name("username")).isDisplayed();
}

}
