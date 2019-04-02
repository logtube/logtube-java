package io.github.logtube;

import org.jetbrains.annotations.NotNull;

public class Logtube {

    @NotNull
    public static IEventLogger getLogger(@NotNull String name) {
        return LogtubeLoggerFactory.getSingleton().getDerivedLogger(name);
    }

    @NotNull
    public static IEventLogger getLogger(@NotNull Class clazz) {
        return getLogger(clazz.getName());
    }

}
