package stepdefinitions;

import driver.DriverFactory;
import io.cucumber.java.en.*;
import org.testng.Assert;

import pages.LoginPage;
import pages.PimEmployeeLifecycle;
import utils.UtilsMethods;

public class PimEmployeeLifeCycleStepDef {

    private PimEmployeeLifecycle pimEmployeeLifecycle;

    @When("user clicks on PIM module")
public void user_clicks_on_pim_module() {
    pimEmployeeLifecycle = new PimEmployeeLifecycle(DriverFactory.getDriver());
    pimEmployeeLifecycle.clickOnPimModule();
}
@Then("user navigates to PIM module")
public void user_navigates_to_pim_module() {
    Assert.assertEquals(
                pimEmployeeLifecycle.getPimPageTitle(),
                "PIM",
                "Dashboard title mismatch"
        );
}
@When("user clicks on Add button")
public void user_clicks_on_add_button() {
    pimEmployeeLifecycle.clickOnaddButton();
}
@Then("Validate user navigates to add user page")
public void validate_user_navigates_to_add_user_page() {
    Assert.assertTrue(pimEmployeeLifecycle.isAddEmployeeFormDisplayed(),
                "Login page is not displayed");
}

@When("user enters employee details")
public void user_enters_employee_details() {
    pimEmployeeLifecycle.enterEmployeeDetails();
    pimEmployeeLifecycle.uploadProfilePicture();
}
@When("user clicks on Save button")
public void user_clicks_on_save_button() {
    pimEmployeeLifecycle.clickOnSaveButton();
     String successMsg = UtilsMethods.waitForSuccessToast(DriverFactory.getDriver());
Assert.assertEquals(successMsg, "Success");
}
@Then("Validate employee should be added successfully")
public void employee_should_be_added_successfully() {
//     String successMsg = UtilsMethods.waitForSuccessToast(DriverFactory.getDriver());
// Assert.assertEquals(successMsg, "Success");
}

@Then("User moves to Employeelist page")
public void user_moves_to_employeelist_page() {
   pimEmployeeLifecycle.clickOnEmployeeList();
}

@Then("Validate employee should be added successfully in Backend")
public void employee_should_be_added_successfully_in_backend() throws InterruptedException {
    Thread.sleep(6000);
    UtilsMethods.refreshSessionCookie(DriverFactory.getDriver());

    String empId = UtilsMethods.getJsonValue("employee.employeeId");

    boolean isPresent =
            UtilsMethods.isEmployeePresentInBackend(empId);

    Assert.assertTrue(isPresent,
            "Employee not found in backend for ID: " + empId);
}

@Then("User filter the employee using employeeid")
public void user_filter_the_employee_using_employeeid() {
    pimEmployeeLifecycle.filterUserWithEmpId();
}
@When("User click on the edit button to update the user details")
public void user_click_on_the_edit_button_to_update_the_user_details() {
    pimEmployeeLifecycle.clickOnEditEmployeeList();
}
@Then("Validate user moves to user details page")
public void validate_user_moves_to_user_details_page() {
    Assert.assertTrue(pimEmployeeLifecycle.isEmpDetailsDisplayed(),
                "Login page is not displayed");
}

@When("User clicks on the Job link")
public void user_clicks_on_the_job_link() {
    pimEmployeeLifecycle.clickOnJob();
}
@When("Select and update the Job Title and Employement Status")
public void select_and_update_the_job_title_and_employement_status() {
    pimEmployeeLifecycle.selectJobDetailsAndSave();
}
@Then("Validate employee should be saved successfully")
public void validate_employee_should_be_saved_successfully() {
    String successMsg = UtilsMethods.waitForSuccessToast(DriverFactory.getDriver());
Assert.assertEquals(successMsg, "Success");
}
@Then("Validate updated data reflects to the BE for the user")
public void validate_updated_data_reflects_to_the_be_for_the_user() {

    UtilsMethods.refreshSessionCookie(DriverFactory.getDriver());

    String employeeId =
        UtilsMethods.getJsonValue("employee.employeeId");

    // ✅ Use runtime values (selected from UI)
    String expectedJobTitle =
        UtilsMethods.getRuntimeProperty("jobTitle");

    String expectedEmploymentStatus =
        UtilsMethods.getRuntimeProperty("employmentStatus");

    boolean isValid =
        UtilsMethods.validateEmployeeJobDetailsFromBackendFiltered(
            employeeId,
            expectedJobTitle,
            expectedEmploymentStatus
        );

    Assert.assertTrue(
        isValid,
        "Backend mismatch for Job Title or Employment Status"
    );

}

@Then("Validate updated data reflects in UI for the user")
public void validate_updated_data_reflects_in_ui_for_the_user() {

    String empId = UtilsMethods.getRuntimeProperty("employeeId");
    String expectedJobTitle = UtilsMethods.getRuntimeProperty("updatedJobTitle");
    String expectedStatus = UtilsMethods.getRuntimeProperty("updatedEmploymentStatus");

    Assert.assertTrue(
        pimEmployeeLifecycle.isUpdatedJobDetailsReflectedInUI(
            empId,
            expectedJobTitle,
            expectedStatus
        ),
        "UI Job Title or Employment Status mismatch"
    );
}


@When("User deletes the employee")
public void user_deletes_the_employee() throws InterruptedException {
    Thread.sleep(1000);
    pimEmployeeLifecycle.deleteEmployeeFromList();
    Thread.sleep(10000);
    //pimEmployeeLifecycle.deleteEmployeeFromList();
}

@Then("Validate employee should be deleted successfully from UI")
public void validate_employee_deleted_from_ui() {

    String empId = UtilsMethods.getJsonValue("employee.employeeId");

    Assert.assertTrue(
        pimEmployeeLifecycle.isEmployeeDeletedFromUI(empId),
        "❌ Employee still present in UI after deletion"
    );
}

@Then("Validate employee is deleted from Backend")
public void validate_employee_deleted_from_backend() {

    UtilsMethods.refreshSessionCookie(DriverFactory.getDriver());
    String empId = UtilsMethods.getJsonValue("employee.employeeId");

    Assert.assertTrue(
        UtilsMethods.isEmployeeDeletedFromBackend(empId),
        "❌ Employee still exists in backend after deletion"
    );
}

@Then("User logs out from application")
public void user_logs_out_from_application() {
    pimEmployeeLifecycle.logout();
}

@Then("Validate user is redirected to login page")
public void validate_user_redirected_to_login_page() {

    LoginPage loginPage =
        new LoginPage(DriverFactory.getDriver());

    Assert.assertTrue(
        loginPage.isLoginPageDisplayedAfterLogout(),
        "User is not redirected to login page after logout"
    );
}

@Then("Validate backend session is invalidated")
public void validate_backend_session_invalidated() {

    Assert.assertTrue(
        UtilsMethods.isSessionInvalidOnBackend(),
        "Backend still allows access after logout"
    );
}



}
