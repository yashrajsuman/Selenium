package com.example.framework.tests;

import com.example.framework.base.BaseTest;
import com.example.framework.pages.LoginPage;
import com.example.framework.pages.SecureAreaPage;
import com.example.framework.utils.ConfigReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Functional tests. Tagged so CI can pick: smoke on every PR, regression nightly. */
class LoginTests extends BaseTest {

    @Test
    @Tag("smoke")
    @DisplayName("Valid user can log in")
    void validUserCanLogin() {
        SecureAreaPage secure = new LoginPage(driver)
                .open(baseUrl)
                .loginAs(ConfigReader.get("test.username"), ConfigReader.get("test.password"));

        assertThat(secure.headingText()).contains("Secure Area");
        assertThat(secure.flashMessage()).contains("You logged into a secure area");
    }

    @Test
    @Tag("regression")
    @DisplayName("Invalid username shows an error")
    void invalidUsernameShowsError() {
        LoginPage login = new LoginPage(driver).open(baseUrl)
                .loginExpectingFailure("wrong-user", "whatever");

        assertThat(login.flashMessage()).contains("Your username is invalid");
    }

    @Test
    @Tag("regression")
    @DisplayName("Invalid password shows an error")
    void invalidPasswordShowsError() {
        LoginPage login = new LoginPage(driver).open(baseUrl)
                .loginExpectingFailure(ConfigReader.get("test.username"), "wrong-password");

        assertThat(login.flashMessage()).contains("Your password is invalid");
    }

    @Test
    @Tag("regression")
    @DisplayName("Logged-in user can log out")
    void userCanLogOut() {
        LoginPage login = new LoginPage(driver).open(baseUrl)
                .loginAs(ConfigReader.get("test.username"), ConfigReader.get("test.password"))
                .logout();

        assertThat(login.flashMessage()).contains("You logged out");
    }
}
