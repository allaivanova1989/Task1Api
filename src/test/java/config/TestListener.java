package config;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.AllureUtils;

@Log4j2
public class TestListener implements ITestListener {
    public void onTestStart(ITestResult result) {
        log.info("Test: " + result.getName() + " started.");
    }

    public void onTestSuccess(ITestResult result) {
        log.info("Test Success: " + result.getName());
    }

    public void onTestFailure(ITestResult result) {
        WebDriver driver = (WebDriver) result.getTestContext().getAttribute("driver");
        log.info("Test " + result.getName() + " is failed");
        AllureUtils.takeScreenshot(driver);
    }
}
