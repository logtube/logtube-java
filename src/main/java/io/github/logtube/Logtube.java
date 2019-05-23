package io.github.logtube;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IEventProcessor;
import org.jetbrains.annotations.NotNull;

public class Logtube {

    @NotNull
    public static IEventProcessor getProcessor() {
        return LogtubeLoggerFactory.getSingleton().getProcessor();
    }

    @NotNull
    public static IEventLogger getLogger(@NotNull String name) {
        return LogtubeLoggerFactory.getSingleton().getEventLogger(name);
    }

    @NotNull
    public static IEventLogger getLogger(@NotNull Class clazz) {
        return getLogger(clazz.getName());
    }

}
