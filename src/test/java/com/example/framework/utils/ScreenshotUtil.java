package com.example.framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ScreenshotUtil {
    private static final Path DIR = Paths.get("target", "screenshots");

    private ScreenshotUtil() {}

    public static Path capture(WebDriver driver, String name) {
        try {
            Files.createDirectories(DIR);
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path file = DIR.resolve(name.replaceAll("[^a-zA-Z0-9._-]", "_") + ".png");
            Files.write(file, bytes);
            return file;
        } catch (IOException e) {
            throw new IllegalStateException("Could not save screenshot", e);
        }
    }
}
