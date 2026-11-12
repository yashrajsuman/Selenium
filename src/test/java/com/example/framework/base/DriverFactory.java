package com.example.framework.base;

import com.example.framework.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Single place deciding local vs CI vs Grid behaviour.
 *   -Dheadless=true          run without a visible browser (CI)
 *   -Dgrid.url=http://...    run on Selenium Grid / Docker
 *   -Dbrowser=chrome|firefox|edge
 */
public final class DriverFactory {
    private DriverFactory() {}

    public static WebDriver create() {
        String browser = ConfigReader.get("browser", "chrome").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");
        String grid = ConfigReader.get("grid.url");

        try {
            return switch (browser) {
                case "firefox" -> {
                    FirefoxOptions o = new FirefoxOptions();
                    if (headless) o.addArguments("-headless", "--width=1920", "--height=1080");
                    yield grid == null ? new FirefoxDriver(o) : new RemoteWebDriver(new URL(grid), o);
                }
                case "edge" -> {
                    EdgeOptions o = new EdgeOptions();
                    if (headless) o.addArguments("--headless=new", "--window-size=1920,1080");
                    yield grid == null ? new EdgeDriver(o) : new RemoteWebDriver(new URL(grid), o);
                }
                default -> {
                    ChromeOptions o = new ChromeOptions();
                    if (headless) {
                        o.addArguments("--headless=new", "--no-sandbox",
                                "--disable-dev-shm-usage", "--window-size=1920,1080");
                    }
                    yield grid == null ? new ChromeDriver(o) : new RemoteWebDriver(new URL(grid), o);
                }
            };
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid grid.url: " + grid, e);
        }
    }
}
