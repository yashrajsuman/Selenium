package com.example.framework.tests;

import com.example.framework.base.BaseTest;
import com.example.framework.pages.LoginPage;
import com.example.framework.utils.VisualCompare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

/** UI tests: element state, styling, responsiveness, and a pixel-diff visual check. */
class UiTests extends BaseTest {

    @Test
    @Tag("smoke")
    @DisplayName("Login page shows the expected elements")
    void loginPageElementsAreVisible() {
        LoginPage login = new LoginPage(driver).open(baseUrl);

        assertThat(driver.getTitle()).isNotBlank();
        assertThat(login.usernameField().isDisplayed()).isTrue();
        assertThat(login.submitButton().isDisplayed()).isTrue();
        assertThat(login.submitButton().isEnabled()).isTrue();
    }

    @Test
    @Tag("regression")
    @DisplayName("Submit button has a visible size and styling")
    void submitButtonStyling() {
        WebElement button = new LoginPage(driver).open(baseUrl).submitButton();

        assertThat(button.getSize().getWidth()).isGreaterThan(50);
        assertThat(button.getSize().getHeight()).isGreaterThan(20);
        // Example CSS assertions: adjust to your design system
        assertThat(button.getCssValue("cursor")).isEqualTo("pointer");
        assertThat(button.getCssValue("background-color")).isNotEqualTo("rgba(0, 0, 0, 0)");
    }

    @Test
    @Tag("regression")
    @DisplayName("Page has no horizontal scroll on a mobile-sized viewport")
    void noHorizontalScrollOnMobile() {
        driver.manage().window().setSize(new Dimension(390, 844));
        new LoginPage(driver).open(baseUrl);

        Long scrollWidth = (Long) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.documentElement.scrollWidth");
        Long clientWidth = (Long) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.documentElement.clientWidth");

        assertThat(scrollWidth).isLessThanOrEqualTo(clientWidth);
    }

    @Test
    @Tag("visual")
    @DisplayName("Login page looks the same as the baseline")
    void loginPageMatchesBaseline() {
        driver.manage().window().setSize(new Dimension(1280, 800));
        new LoginPage(driver).open(baseUrl);

        double diffPercent = VisualCompare.compareWithBaseline(driver, "login-page");

        // First run creates the baseline and passes. Later runs fail if >1% of pixels differ.
        assertThat(diffPercent).as("visual difference %").isLessThan(1.0);
    }
}
