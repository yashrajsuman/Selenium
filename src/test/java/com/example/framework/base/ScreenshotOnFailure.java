package com.example.framework.base;

import com.example.framework.utils.ScreenshotUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.WebDriver;

/** JUnit 5 extension: saves a screenshot to target/screenshots when a test fails. */
public class ScreenshotOnFailure implements TestWatcher {
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        WebDriver driver = DriverManager.get();
        if (driver != null) {
            String name = context.getRequiredTestClass().getSimpleName() + "_" + context.getRequiredTestMethod().getName();
            ScreenshotUtil.capture(driver, name);
        }
    }
}
