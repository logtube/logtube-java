package io.github.logtube.utils;

import java.util.Properties;

public class PropertiesUtil {

    public static String safeString(Properties properties, String field, String defaultValue) {
        return StringUtil.safeString(properties.getProperty(field), defaultValue);
    }

    public static boolean getBoolean(Properties properties, String field, boolean defaultValue) {
        String value = properties.getProperty(field);
        if (value == null) {
            return defaultValue;
        }
        value = value.trim();
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on") || value.equalsIgnoreCase("yes")) {
            return true;
        }
        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("off") || value.equalsIgnoreCase("no")) {
            return false;
        }
        return defaultValue;
    }

    public static String[] getStringArray(Properties properties, String field, String[] defaultValue) {
        String value = properties.getProperty(field);
        if (value == null) {
            return defaultValue;
        }
        String[] components = value.split(",");
        if (components.length == 0) {
            return defaultValue;
        }
        for (int i = 0; i < components.length; i++) {
            components[i] = components[i].trim();
        }
        return components;
    }

}
