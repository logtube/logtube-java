package io.github.logtube;

import io.github.logtube.utils.PropertiesUtil;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.Properties;

public class LogtubeOptions {

    public static String getHostname() {
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

    @NotNull
    private final Properties properties;

    public LogtubeOptions(@NotNull Properties properties) {
        this.properties = properties;
    }

    @NotNull
    public String getProject() {
        return PropertiesUtil.safeString(this.properties, "logtube.project", "unknown-project");
    }

    @NotNull
    public String getEnv() {
        return PropertiesUtil.safeString(this.properties, "logtube.env", "unknown-project");
    }

}
