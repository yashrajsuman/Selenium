package com.example.framework.pages;

import com.example.framework.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SecureAreaPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By heading = By.tagName("h2");
    private final By logout  = By.cssSelector("a.button.secondary");
    private final By flash   = By.id("flash");

    public SecureAreaPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds", 10)));
    }

    public String headingText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(heading)).getText();
    }

    public String flashMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(flash)).getText();
    }

    public LoginPage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logout)).click();
        return new LoginPage(driver);
    }
}
