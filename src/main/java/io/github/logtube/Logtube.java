package io.github.logtube;

import io.github.logtube.logger.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Logtube {

    private static final Logger DEFAULT_LOGGER;

    static {
        DEFAULT_LOGGER = new Logger("", "", "");
    }

    public static boolean isTopicEnabled(@NotNull String topic) {
        return DEFAULT_LOGGER.isTopicEnabled(topic);
    }

    @NotNull
    public static IEventCommitter topic(@Nullable String topic) {
        return DEFAULT_LOGGER.topic(topic);
    }

    @NotNull
    public static IEventCommitter trace() {
        return DEFAULT_LOGGER.trace();
    }

    @NotNull
    public static IEventCommitter debug() {
        return DEFAULT_LOGGER.debug();
    }

    @NotNull
    public static IEventCommitter info() {
        return DEFAULT_LOGGER.info();
    }

    @NotNull
    public static IEventCommitter warn() {
        return DEFAULT_LOGGER.warn();
    }

    @NotNull
    public static IEventCommitter error() {
        return DEFAULT_LOGGER.error();
    }

}
