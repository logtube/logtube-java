package io.github.logtube;

import io.github.logtube.logger.Logger;
import io.github.logtube.outputs.EventConsoleOutput;
import io.github.logtube.utils.PropertiesUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Properties;

public class Logtube {

    private static final Logger DEFAULT_LOGGER;

    private static final String[] EMPTY_TOPICS = new String[]{"debug", "info", "error"};

    static {
        Properties props = new Properties();
        try (final InputStream stream = Logtube.class.getResourceAsStream("logtube.properties")) {
            props.load(stream);
        } catch (Exception ignored) {
        }

        LogtubeOptions options = new LogtubeOptions(props);

        DEFAULT_LOGGER = new Logger(LogtubeOptions.getHostname(), options.getProject(), options.getEnv());

        if (PropertiesUtil.getBoolean(props, "logtube.console.enabled", false)) {
            EventConsoleOutput output = new EventConsoleOutput();
            DEFAULT_LOGGER.addOutput(output);
            String[] topics = PropertiesUtil.getStringArray(props, "logtube.console.topics", EMPTY_TOPICS);
        }
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
