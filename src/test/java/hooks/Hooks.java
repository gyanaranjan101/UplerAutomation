package hooks;

import driver.DriverFactory;
import io.cucumber.java.*;
import utils.ScreenRecorderUtil;
import utils.UtilsMethods;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        DriverFactory.initDriver();
        DriverFactory.getDriver()
                .get(UtilsMethods.getProperty("base.url"));

        ScreenRecorderUtil.startRecording(scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {

        try {
            String videoPath = ScreenRecorderUtil.stopRecording();

            if (videoPath != null) {
                scenario.attach(
                        videoPath,
                        "text/plain",
                        "Execution Video"
                );
            }

        } finally {
            // 🔥 ALWAYS close browser even if recording fails
            DriverFactory.quitDriver();
        }
    }
}
