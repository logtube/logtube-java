package io.github.logtube;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IRootEventLogger;
import org.jetbrains.annotations.NotNull;

public class Logtube {

    @NotNull
    public static IRootEventLogger getRootLogger() {
        return LogtubeLoggerFactory.getSingleton().getRootLogger();
    }

    @NotNull
    public static IEventLogger getLogger() {
        return getRootLogger();
    }

    @NotNull
    public static IEventLogger getLogger(@NotNull String name) {
        return getRootLogger().derive(name);
    }

    @NotNull
    public static IEventLogger getLogger(@NotNull Class clazz) {
        return getLogger(clazz.getName());
    }

}
