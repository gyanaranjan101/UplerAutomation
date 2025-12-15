package utils;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import java.util.Map;
import java.util.HashMap;

public class UtilsMethods {

    // ================== PROPERTIES ==================
    private static Properties prop;
    private static JsonNode jsonNode;
    private static final Map<String, String> runtimeData = new HashMap<>();

    // ---------- Load config.properties ----------
    public static Properties initProperties() {
        prop = new Properties();
        try {
            FileInputStream ip =
                    new FileInputStream("src/test/resources/config.properties");
            prop.load(ip);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
        return prop;
    }

    public static String getProperty(String key) {
        if (prop == null) {
            initProperties();
        }
        return prop.getProperty(key);
    }

    // ================== JSON METHODS ==================
    private static void loadJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonNode = mapper.readTree(
                    new File("src/test/resources/testdata/employeeData.json"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load employeeData.json", e);
        }
    }

    // Generic JSON value reader (employee.firstName)
    public static String getJsonValue(String keyPath) {
        if (jsonNode == null) {
            loadJson();
        }

        String[] keys = keyPath.split("\\.");
        JsonNode node = jsonNode;

        for (String key : keys) {
            node = node.get(key);
        }
        return node.asText();
    }

    public static boolean getJsonBoolean(String keyPath) {
        if (jsonNode == null) {
            loadJson();
        }

        String[] keys = keyPath.split("\\.");
        JsonNode node = jsonNode;

        for (String key : keys) {
            node = node.get(key);
        }
        return node.asBoolean();
    }

    // ================== UI UTIL ==================
    // Wait & read success toast message
    public static String waitForSuccessToast(WebDriver driver) {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement toast =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class,'oxd-toast--success')]//p")
                ));

        return toast.getText();
    }


    // ================== BACKEND VALIDATION ==================
    // Validate employee presence via API

public static void setRuntimeProperty(String key, String value) {
    runtimeData.put(key, value);
}

public static String getRuntimeProperty(String key) {
    return runtimeData.get(key);
}

 public static boolean isEmployeePresentInBackend(String employeeId) {

    String sessionCookie = getRuntimeProperty("orangehrmCookie");

    Response response =
        given()
            .baseUri("https://opensource-demo.orangehrmlive.com")
            .basePath("/web/index.php/api/v2/pim/employees")
            .queryParam("limit", 50)
            .queryParam("offset", 0)
            .queryParam("model", "detailed")
            .queryParam("employeeId", employeeId) // ✅ FILTER
            .queryParam("includeEmployees", "onlyCurrent")
            .queryParam("sortField", "employee.firstName")
            .queryParam("sortOrder", "ASC")
            .cookie("orangehrm", sessionCookie)
        .when()
            .get();
    // System.out.println(response.asPrettyString());

    response.then().statusCode(200);

    List<String> empIds =
        response.jsonPath().getList("data.employeeId");

    return empIds.contains(employeeId);
}


    public static void selectDropdownByVisibleText(
        WebDriver driver,
        By dropdown,
        String value) {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    // Open dropdown
    driver.findElement(dropdown).click();

    // Select option
    WebElement option = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@role='listbox']//span[text()='" + value + "']")
        )
    );

    option.click();
}

public static boolean validateEmployeeJobDetailsFromBackendFiltered(
        String employeeId,
        String expectedJobTitle,
        String expectedEmploymentStatus) {

    String sessionCookie = getRuntimeProperty("orangehrmCookie");

    Response response =
        given()
            .baseUri("https://opensource-demo.orangehrmlive.com")
            .basePath("/web/index.php/api/v2/pim/employees")
            .queryParam("limit", 50)
            .queryParam("offset", 0)
            .queryParam("model", "detailed")
            .queryParam("employeeId", employeeId) // ✅ FILTER
            .queryParam("includeEmployees", "onlyCurrent")
            .queryParam("sortField", "employee.firstName")
            .queryParam("sortOrder", "ASC")
            .cookie("orangehrm", sessionCookie)
        .when()
            .get()
        .then()
            .statusCode(200)
            .extract()
            .response();

            // System.out.println(response.asPrettyString());

    // Since filter returns only one record
    String actualJobTitle =
        response.jsonPath().getString("data[0].jobTitle.title");

    String actualEmploymentStatus =
        response.jsonPath().getString("data[0].empStatus.name");

    return expectedJobTitle.equals(actualJobTitle)
        && expectedEmploymentStatus.equals(actualEmploymentStatus);
}

public static String refreshSessionCookie(WebDriver driver) {
    Cookie cookie =
        driver.manage().getCookieNamed("orangehrm");

    if (cookie == null) {
        throw new RuntimeException("OrangeHRM session cookie not found");
    }

    String value = cookie.getValue();
    setRuntimeProperty("orangehrmCookie", value);
    return value;
}

public static boolean isEmployeeDeletedFromBackend(String employeeId) {

    String sessionCookie = getRuntimeProperty("orangehrmCookie");

    if (sessionCookie == null) {
        throw new RuntimeException("❌ Session cookie is NULL. Login might have failed.");
    }

    Response response =
        given()
            .baseUri("https://opensource-demo.orangehrmlive.com")
            .basePath("/web/index.php/api/v2/pim/employees")
            .header("Cookie", "orangehrm=" + sessionCookie)
            .queryParam("employeeId", employeeId)
            .queryParam("model", "detailed")
            .queryParam("includeEmployees", "onlyCurrent")
        .when()
            .get()
        .then()
            .statusCode(200)
            .extract()
            .response();

    // 🔍 Backend validation
    List<?> data = response.jsonPath().getList("data");
    int total = response.jsonPath().getInt("meta.total");

    System.out.println("Backend delete validation:");
    System.out.println("Data size : " + data.size());
    System.out.println("Total     : " + total);

    return data.isEmpty() && total == 0;
}

public static boolean isSessionInvalidOnBackend() {

    String cookie =
        UtilsMethods.getRuntimeProperty("orangehrmCookie");

    Response response =
        given()
            .baseUri("https://opensource-demo.orangehrmlive.com")
            .header("Cookie", "orangehrm=" + cookie)
        .when()
            .get("/web/index.php/api/v2/pim/employees");

    return response.statusCode() == 401;
}


}
