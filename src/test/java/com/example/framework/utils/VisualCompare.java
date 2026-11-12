package com.example.framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Minimal pixel-diff visual check (free alternative to Applitools/Percy).
 * First run creates the baseline in /screenshots. Later runs compare against it.
 * IMPORTANT: generate baselines in the same environment (Docker/CI) that compares them,
 * because fonts and rendering differ between OSes.
 */
public final class VisualCompare {
    private static final Path BASELINE_DIR = Paths.get("screenshots", "baseline");
    private static final Path DIFF_DIR = Paths.get("target", "screenshots");

    private VisualCompare() {}

    /** @return percentage (0-100) of pixels that differ; 0 when a new baseline was just created. */
    public static double compareWithBaseline(WebDriver driver, String name) {
        try {
            Files.createDirectories(BASELINE_DIR);
            Files.createDirectories(DIFF_DIR);
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            BufferedImage actual = ImageIO.read(new ByteArrayInputStream(bytes));
            Path baselineFile = BASELINE_DIR.resolve(name + ".png");

            if (Boolean.getBoolean("update.baseline") || Files.notExists(baselineFile)) {
                ImageIO.write(actual, "png", baselineFile.toFile());
                return 0.0;
            }

            BufferedImage expected = ImageIO.read(baselineFile.toFile());
            if (expected.getWidth() != actual.getWidth() || expected.getHeight() != actual.getHeight()) {
                ImageIO.write(actual, "png", DIFF_DIR.resolve(name + "-actual.png").toFile());
                return 100.0;
            }

            BufferedImage diff = new BufferedImage(actual.getWidth(), actual.getHeight(), BufferedImage.TYPE_INT_RGB);
            long different = 0;
            for (int x = 0; x < actual.getWidth(); x++) {
                for (int y = 0; y < actual.getHeight(); y++) {
                    int a = actual.getRGB(x, y);
                    int e = expected.getRGB(x, y);
                    if (a != e) {
                        different++;
                        diff.setRGB(x, y, 0xFF0000);
                    } else {
                        diff.setRGB(x, y, a);
                    }
                }
            }
            double pct = 100.0 * different / ((long) actual.getWidth() * actual.getHeight());
            if (pct > 0) {
                ImageIO.write(actual, "png", DIFF_DIR.resolve(name + "-actual.png").toFile());
                ImageIO.write(diff, "png", DIFF_DIR.resolve(name + "-diff.png").toFile());
            }
            return pct;
        } catch (IOException e) {
            throw new IllegalStateException("Visual comparison failed", e);
        }
    }
}
