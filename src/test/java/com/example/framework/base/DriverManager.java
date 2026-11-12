package com.example.framework.base;

import org.openqa.selenium.WebDriver;

/** Holds one WebDriver per thread so tests can run in parallel safely. */
public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver get() { return DRIVER.get(); }

    public static void set(WebDriver driver) { DRIVER.set(driver); }

    public static void quit() {
        WebDriver d = DRIVER.get();
        if (d != null) {
            d.quit();
            DRIVER.remove();
        }
    }
}
