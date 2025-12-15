package hooks;

import driver.DriverFactory;
import io.cucumber.java.*;
import utils.ExtentManager;
import utils.ExtentTestManager;
import utils.ScreenRecorderUtil;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class Hooks {

    private static ExtentReports extent;

    @Before
    public void setUp(Scenario scenario) {

        DriverFactory.initDriver();
        DriverFactory.getDriver()
                .get(utils.UtilsMethods.getProperty("base.url"));

        // 🎥 Start recording
        ScreenRecorderUtil.startRecording(scenario.getName());

        // 📊 Extent init
        extent = ExtentManager.getExtent();
        ExtentTest test = extent.createTest(scenario.getName());

        ExtentTestManager.setTest(test);
        test.info("Scenario started");
    }

    @After
    public void tearDown(Scenario scenario) {

        ExtentTest test = ExtentTestManager.getTest();

        try {
            if (scenario.isFailed()) {
                test.fail("Scenario failed ❌");
            } else {
                test.pass("Scenario passed ✅");
            }

            // 🎥 Stop recording
            String videoPath = ScreenRecorderUtil.stopRecording();

            if (videoPath != null) {
                test.info("Execution Video: " + videoPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DriverFactory.quitDriver();
            ExtentTestManager.unload();
            extent.flush(); // 🚨 REQUIRED
        }
    }
}
