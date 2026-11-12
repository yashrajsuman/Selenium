package com.example.framework.base;

import com.example.framework.utils.ConfigReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

@ExtendWith(ScreenshotOnFailure.class)
public abstract class BaseTest {
    protected WebDriver driver;
    protected final String baseUrl = ConfigReader.get("base.url", "http://localhost:8080");

    @BeforeEach
    void setUp() {
        driver = DriverFactory.create();
        DriverManager.set(driver);
    }

    @AfterEach
    void tearDown() {
        DriverManager.quit();
    }
}
