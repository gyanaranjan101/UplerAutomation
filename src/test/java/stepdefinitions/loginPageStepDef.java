package stepdefinitions;

import driver.DriverFactory;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.LoginPage;
import utils.UtilsMethods;

public class loginPageStepDef {

    private LoginPage loginPage;

    @Given("user is on OrangeHRM login page")
    public void user_is_on_orange_hrm_login_page() {
        loginPage = new LoginPage(DriverFactory.getDriver());
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page is not displayed");
    }

    @When("user enters username and password and click login")
    public void user_enters_username_and_password() {
        loginPage.performLogin();
        String cookieValue =
        DriverFactory.getDriver()
            .manage()
            .getCookieNamed("orangehrm")
            .getValue();

    UtilsMethods.setRuntimeProperty("orangehrmCookie", cookieValue);
    }

    @Then("dashboard should be displayed")
    public void dashboard_should_be_displayed() {
        Assert.assertEquals(
                loginPage.getPageTitle(),
                "Dashboard",
                "Dashboard title mismatch"
        );
    }
}
