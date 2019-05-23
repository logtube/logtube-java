package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {

    public static void flattenMap(@NotNull Properties properties, @NotNull Map<String, Object> map) {
        flattenMapPrefix(properties, map, "");
    }

    private static void flattenMapPrefix(@NotNull Properties properties, @NotNull Map<String, Object> map, @NotNull String prefix) {
        map.forEach((k, v) -> {
            if (v instanceof Map) {
                flattenMapPrefix(properties, (Map<String, Object>) v, prefix + k + ".");
            } else if (v instanceof List) {
                properties.setProperty(prefix + k, String.join(",", (List) v));
            } else {
                properties.setProperty(prefix + k, v.toString());
            }
        });
    }

}
