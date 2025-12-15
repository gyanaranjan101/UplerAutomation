package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getExtent() {

        if (extent == null) {

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String reportPath = System.getProperty("user.dir")
                    + "/target/extent-report/ExtentReport_" + timestamp + ".html";

            new File(reportPath).getParentFile().mkdirs();

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

            spark.config().setReportName("OrangeHRM Automation Report");
            spark.config().setDocumentTitle("UI + API Automation");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Project", "OrangeHRM");
            extent.setSystemInfo("Browser", "Chrome");
            extent.setSystemInfo("Tester", "Gyanaranjan");

        }
        return extent;
    }
}
