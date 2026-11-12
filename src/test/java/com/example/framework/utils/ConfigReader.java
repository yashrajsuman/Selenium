package com.example.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Reads config.properties; -Dkey=value system properties always win. */
public final class ConfigReader {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load config.properties", e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        return System.getProperty(key, PROPS.getProperty(key));
    }

    public static String get(String key, String fallback) {
        String v = get(key);
        return v == null ? fallback : v;
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key, "false"));
    }

    public static int getInt(String key, int fallback) {
        String v = get(key);
        return v == null ? fallback : Integer.parseInt(v);
    }
}
