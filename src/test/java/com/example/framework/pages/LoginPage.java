package com.example.framework.pages;

import com.example.framework.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Page Object: all locators and actions for the login page live here, not in tests. */
public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Prefer data-testid > id > css > xpath. Update these to match YOUR app.
    private final By username = By.id("username");
    private final By password = By.id("password");
    private final By submit   = By.cssSelector("button[type='submit']");
    private final By flash    = By.id("flash");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds", 10)));
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(username));
        return this;
    }

    public SecureAreaPage loginAs(String user, String pass) {
        enterCredentials(user, pass);
        return new SecureAreaPage(driver);
    }

    public LoginPage loginExpectingFailure(String user, String pass) {
        enterCredentials(user, pass);
        return this;
    }

    private void enterCredentials(String user, String pass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(username)).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(submit).click();
    }

    public String flashMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(flash)).getText();
    }

    public WebElement submitButton() { return driver.findElement(submit); }

    public WebElement usernameField() { return driver.findElement(username); }
}
