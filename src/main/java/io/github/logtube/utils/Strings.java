package io.github.logtube.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Strings {

    private static final Pattern PATTERN_ENVIRONMENT_VARIABLE = Pattern.compile("\\$\\{([A-Za-z0-9_-]+)\\}");

    @Contract("_, !null -> !null")
    public static @Nullable String sanitize(@Nullable String str, @Nullable String defaultValue) {
        str = normalize(str);
        if (str == null || str.length() == 0) {
            return defaultValue;
        }
        return str.toLowerCase().replaceAll("[^0-9a-zA-Z_-]", "_");
    }

    public static boolean isEmpty(@Nullable String s) {
        if (s == null) {
            return true;
        }
        if (s.length() == 0) {
            return true;
        }
        return s.trim().length() == 0;
    }

    @Nullable
    public static String normalize(@Nullable String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        if (s.length() == 0) {
            return null;
        }
        return s;
    }

    @NotNull
    public static String safeNormalize(@Nullable String s) {
        if (s == null) {
            return "";
        }
        return s.trim();
    }

    @NotNull
    public static String safeNormalize(@Nullable Object o) {
        if (o == null) {
            return "null";
        }
        return safeNormalize(o.toString());
    }

    @NotNull
    public static String safeNormalizeKeyword(@Nullable String s) {
        s = normalize(s);
        if (s == null) {
            return "null";
        }
        return s.replaceAll("[\\s,\\[\\]]+", "_");
    }

    @Contract("null -> null; !null -> !null")
    @Nullable
    public static String evaluateEnvironmentVariables(@Nullable String s) {
        if (s == null) return null;
        while (true) {
            Matcher m = PATTERN_ENVIRONMENT_VARIABLE.matcher(s);
            if (m.find()) {
                String value = System.getenv(m.group(1));
                if (value == null) {
                    value = "";
                }
                String s1 = s.substring(0, m.start());
                String s2 = s.substring(m.end());
                s = s1 + value + s2;
            } else {
                break;
            }
        }
        return s;
    }

    @NotNull
    public static String keyword(@Nullable Object... os) {
        // deny if keywords is too large
        if (os == null || os.length == 0 || os.length > 100) return "";
        return " K[" + Arrays
                .stream(os)
                // toString
                .map(String::valueOf)
                // normalize
                .map(Strings::safeNormalizeKeyword)
                // join with ','
                .collect(Collectors.joining(","))
                + "]";
    }

}
