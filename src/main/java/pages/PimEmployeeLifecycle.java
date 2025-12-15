package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dev.failsafe.internal.util.Assert;
import driver.DriverFactory;
import utils.UtilsMethods;

public class PimEmployeeLifecycle extends UtilsMethods {

    private WebDriver driver;

    // ✅ ONLY locators (NO findElement here)
    private By pimModule = By.xpath("//span[text()='PIM']");
    private By pagepimTitle=By.xpath("//div[contains(@class,'topbar-header-title')]//h6");
    private By addEmployeeButton=By.xpath("//button[normalize-space()='Add']");
    private By addEmployeeform=By.xpath("//form[contains(@class,'oxd-form')]");
    private By employeeFirstName=By.name("firstName");
    private By employeeMiddleName=By.name("middleName");
    private By employeeLastName=By.name("lastName");
    private By employeeId=By.xpath("//label[text()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By saveButton=By.xpath("//button[@type='submit']");
    private By uploadProfilePic=By.xpath("//input[@type='file' and contains(@class,'oxd-file-input')]");
    private By employeeList=By.xpath("//a[text()='Employee List']");
    private By empIdFilter=By.xpath("//label[text()='Employee Id']/parent::div/following-sibling::div//input");
    private By editEmpDetails=By.xpath("//i[contains(@class,'bi-pencil-fill')]");
    private By empPersonalDetails=By.xpath("//h6[text()='Personal Details']");
    private By jobDetails=By.xpath("//a[text()='Job']");
    private By jobTitleDropdown=By.xpath("//label[text()='Job Title']/following::div[contains(@class,'oxd-select-text')][1]");
    private By employmentStatusDropdown=By.xpath("//label[text()='Employment Status']/following::div[contains(@class,'oxd-select-text')][1]");
    private By userDropdown = By.cssSelector("span.oxd-userdropdown-tab");
    private By logoutBtn = By.xpath("//a[text()='Logout']");


    public PimEmployeeLifecycle(WebDriver driver) {
        this.driver = driver;
    }

    public void clickOnPimModule() {
        driver.findElement(pimModule).click();
    }

    public String getPimPageTitle() {
        return driver.findElement(pagepimTitle).getText();
    }

    public void clickOnaddButton() {
        driver.findElement(addEmployeeButton).click();
    }

     public boolean isAddEmployeeFormDisplayed() {
        return driver.findElement(addEmployeeform).isDisplayed();
    }

    public void enterEmployeeDetails() {
        driver.findElement(employeeFirstName)
      .sendKeys(UtilsMethods.getJsonValue("employee.firstName"));

driver.findElement(employeeMiddleName)
      .sendKeys(UtilsMethods.getJsonValue("employee.middleName"));

driver.findElement(employeeLastName)
      .sendKeys(UtilsMethods.getJsonValue("employee.lastName"));
driver.findElement(employeeId)
      .click();
      driver.findElement(employeeId).sendKeys(Keys.CONTROL + "a");
driver.findElement(employeeId).sendKeys(Keys.DELETE);
// driver.findElement(employeeId)
//       .clear();
driver.findElement(employeeId)
      .sendKeys(UtilsMethods.getJsonValue("employee.employeeId"));

    }

    public void clickOnSaveButton() {
        driver.findElement(saveButton).click();
        
    }

    public void uploadProfilePicture() {
    String imagePath = System.getProperty("user.dir")
            + "/src/test/resources/testdata/profilepic.jpg";

    driver.findElement(uploadProfilePic).sendKeys(imagePath);
}

public void clickOnEmployeeList() {
        driver.findElement(employeeList).click();
    }

    public void filterUserWithEmpId() {
        driver.findElement(empIdFilter).clear();
        driver.findElement(empIdFilter)
      .sendKeys(UtilsMethods.getJsonValue("employee.employeeId"));
      driver.findElement(saveButton).click();
    }

    public void clickOnEditEmployeeList() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    // Wait for table to load
    wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[@class='oxd-table-body']")
    ));

    // Re-locate edit icon freshly
    By editIcon = By.xpath(
        "//div[@class='oxd-table-body']//i[contains(@class,'bi-pencil-fill')]"
    );

    WebElement editButton = wait.until(
        ExpectedConditions.elementToBeClickable(editIcon)
    );

    editButton.click();
}


    public boolean isEmpDetailsDisplayed() {
        return driver.findElement(empPersonalDetails).isDisplayed();
    }

    public void clickOnJob() {
        driver.findElement(jobDetails).click();
    }

    public void selectJobDetailsAndSave() {

    String jobTitle =
        UtilsMethods.getJsonValue("employee.jobTitle");

    String empStatus =
        UtilsMethods.getJsonValue("employee.employmentStatus");

    UtilsMethods.selectDropdownByVisibleText(
        driver, jobTitleDropdown, jobTitle);

    UtilsMethods.selectDropdownByVisibleText(
        driver, employmentStatusDropdown, empStatus);

    // Store for backend validation
    UtilsMethods.setRuntimeProperty("jobTitle", jobTitle);
    UtilsMethods.setRuntimeProperty("employmentStatus", empStatus);

    driver.findElement(saveButton).click();
}

public boolean validateJobDetailsFromUI(
        String expectedJobTitle,
        String expectedEmploymentStatus) {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    // Wait until exactly one row is shown after filter
    WebElement row = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@class='oxd-table-body']//div[contains(@class,'oxd-table-row')]")
        )
    );

    // Get all cells in that row
    List<WebElement> cells =
        row.findElements(By.xpath(".//div[@class='oxd-table-cell oxd-padding-cell']"));

    // Index based on UI order
    String actualJobTitle = cells.get(3).getText().trim();
    String actualEmploymentStatus = cells.get(4).getText().trim();

    return actualJobTitle.equals(expectedJobTitle)
        && actualEmploymentStatus.equals(expectedEmploymentStatus);
}

public void deleteEmployeeFromList() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    // 1️⃣ Click Delete (Trash) icon
    WebElement deleteIcon = wait.until(
        ExpectedConditions.elementToBeClickable(
            By.xpath("//i[contains(@class,'bi-trash')]")
        )
    );
    deleteIcon.click();

    // 2️⃣ Click "Yes, Delete" on confirmation popup
    WebElement yesDeleteBtn = wait.until(
        ExpectedConditions.elementToBeClickable(
            By.xpath("//button[normalize-space()='Yes, Delete']")
        )
    );
    yesDeleteBtn.click();

    // 3️⃣ Wait until popup disappears (important)
    wait.until(
        ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//div[contains(@class,'orangehrm-modal')]")
        )
    );

}

public boolean isUpdatedJobDetailsReflectedInUI(
        String employeeId,
        String expectedJobTitle,
        String expectedEmploymentStatus) {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    // Re-search employee
    WebElement empIdInput = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//label[text()='Employee Id']/following::input[1]")
        )
    );
    empIdInput.clear();
    empIdInput.sendKeys(employeeId);

    driver.findElement(By.xpath("//button[.='Search']")).click();

    // Wait for table row to appear
    WebElement row = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@class='oxd-table-body']//div[@role='row']")
        )
    );

    // Fetch updated values (FRESH)
    String actualJobTitle =
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@role='row']//div[@role='cell'][3]"))
        ).getText().trim();

    String actualStatus =
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@role='row']//div[@role='cell'][4]"))
        ).getText().trim();

    System.out.println("UI Job Title  : " + actualJobTitle);
    System.out.println("UI Status     : " + actualStatus);

    return expectedJobTitle.equals(actualJobTitle)
            && expectedEmploymentStatus.equals(actualStatus);
}

public boolean isEmployeeDeletedFromUI(String employeeId) {

    if (employeeId == null || employeeId.trim().isEmpty()) {
        throw new RuntimeException("❌ Employee ID is NULL or EMPTY while validating deletion");
    }

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    // Use EXISTING filter locator (correct one)
    WebElement empIdInput = wait.until(
        ExpectedConditions.visibilityOfElementLocated(empIdFilter)
    );

    // empIdInput.clear();
    // empIdInput.clear();
    // empIdInput.sendKeys(employeeId);

    // Click Search
    driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();

    // Wait for table refresh
    wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class,'orangehrm-paper-container')]")
        )
    );

    // Validate "No Records Found"
    return wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//span[normalize-space()='No Records Found']")
        )
    ).isDisplayed();
}

public void logout() {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    wait.until(ExpectedConditions.elementToBeClickable(userDropdown)).click();

    wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();

    wait.until(ExpectedConditions.urlContains("auth/login"));
}





}
