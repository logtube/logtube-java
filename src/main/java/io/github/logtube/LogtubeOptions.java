package io.github.logtube;

import io.github.logtube.core.utils.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class LogtubeOptions {

    public static @NotNull String getHostname() {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception ignored) {
        }
        if (hostname == null) {
            hostname = "localhost";
        }
        return hostname;
    }

    private static @NotNull Set<String> quickStringSet(@NotNull String... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    public static @NotNull LogtubeOptions fromClasspath() {
        Properties properties = new Properties();
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("logtube.properties")) {
            if (stream != null) {
                properties.load(stream);
            } else {
                System.err.println("logtube.properties not found in class path, using default options");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("failed to load logtube.properties, using default options.");
        }
        return new LogtubeOptions(properties);
    }

    @NotNull
    private final Properties properties;

    public LogtubeOptions(@NotNull Properties properties) {
        this.properties = properties;
    }

    @Nullable
    @Contract("_, !null -> !null")
    private String safeStringValue(@NotNull String field, @Nullable String defaultValue) {
        return StringUtil.safeString(properties.getProperty(field), defaultValue);
    }

    @Nullable
    @Contract("_, !null -> !null")
    private String stringValue(@NotNull String field, @Nullable String defaultValue) {
        return properties.getProperty(field, defaultValue);
    }

    private boolean booleanValue(String field, boolean defaultValue) {
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

    @Contract("_, !null -> !null")
    private @Nullable Set<String> stringSetValue(String field, @Nullable Set<String> defaultValue) {
        String value = properties.getProperty(field);
        if (value == null) {
            return defaultValue;
        }
        String[] components = value.split(",");
        if (components.length == 0) {
            return defaultValue;
        }
        HashSet<String> result = new HashSet<>();
        for (String component : components) {
            component = component.trim();
            if (component.length() == 0) {
                continue;
            }
            result.add(component);
        }
        if (result.size() == 0) {
            return defaultValue;
        }
        return result;
    }


    @NotNull
    public String getProject() {
        return safeStringValue("logtube.project", "unknown-project");
    }

    @NotNull
    public String getEnv() {
        return safeStringValue("logtube.env", "unknown-env");
    }

    @Nullable
    public Set<String> getTopics() {
        return stringSetValue("logtube.topics", quickStringSet("*", "-trace", "-debug"));
    }

    public boolean getConsoleEnabled() {
        return booleanValue("logtube.console.enabled", true);
    }

    @Nullable
    public Set<String> getConsoleTopics() {
        return stringSetValue("logtube.console.topics", null);
    }

    public boolean getFilePlainEnabled() {
        return booleanValue("logtube.file-plain.enabled", true);
    }

    @Nullable
    public Set<String> getFilePlainTopics() {
        return stringSetValue("logtube.file-plain.topics", quickStringSet("trace", "debug", "info", "warn", "error"));
    }

    @NotNull
    public String getFilePlainDir() {
        return stringValue("logtube.file-plain.dir", "logs");
    }

    @NotNull
    public String getFilePlainSignal() {
        return stringValue("logtube.file-plain.signal", "/tmp/logtube.reopen.txt");
    }

    public boolean getFileJSONEnabled() {
        return booleanValue("logtube.file-json.enabled", false);
    }

    @Nullable
    public Set<String> getFileJSONTopics() {
        return stringSetValue("logtube.file-json.topics", quickStringSet("*", "-trace", "-debug", "-info", "-warn", "-error"));
    }

    @NotNull
    public String getFileJSONDir() {
        return stringValue("logtube.file-json.dir", "logs");
    }

    @NotNull
    public String getFileJSONSignal() {
        return stringValue("logtube.file-json.signal", "/tmp/logtube.reopen.txt");
    }

    public boolean getRemoteEnabled() {
        return booleanValue("logtube.remote.enabled", false);
    }

    @Nullable
    public Set<String> getRemoteTopics() {
        return stringSetValue("logtube.remote.topics", quickStringSet("*", "-trace", "-debug"));
    }

    @NotNull
    public String[] getRemoteHosts() {
        Set<String> set = stringSetValue("logtube.remote.hosts", null);
        if (set == null) {
            return new String[]{"127.0.0.1:9921"};
        }
        return (String[]) set.toArray();
    }

}
