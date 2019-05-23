package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Maps {

    public static void flattenProperties(@NotNull Properties properties, @NotNull Map map) {
        flattenProperties(properties, map, "");
    }

    private static List<String> safeList(List list) {
        List<String> ret = new ArrayList<>();
        for (Object o : list) {
            ret.add(o.toString());
        }
        return ret;
    }

    private static void flattenProperties(@NotNull Properties properties, @NotNull Map<?, ?> map, @NotNull String prefix) {
        for (Map.Entry entry : map.entrySet()) {
            Object k = entry.getKey();
            Object v = entry.getValue();
            if (v instanceof Map) {
                flattenProperties(properties, (Map) v, prefix + k.toString() + ".");
            } else if (v instanceof List) {
                properties.setProperty(prefix + k.toString(), String.join(",", safeList((List) v)));
            } else {
                properties.setProperty(prefix + k.toString(), v.toString());
            }
        }
    }

}
