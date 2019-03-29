package io.github.logtube;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IEventLogger {

    String CLASSIC_TOPIC_TRACE = "trace";

    String CLASSIC_TOPIC_DEBUG = "debug";

    String CLASSIC_TOPIC_INFO = "info";

    String CLASSIC_TOPIC_WARN = "warn";

    String CLASSIC_TOPIC_ERROR = "error";

    @NotNull
    IEventCommitter topic(@Nullable String topic);

    @NotNull
    default IEventCommitter trace() {
        return topic(CLASSIC_TOPIC_TRACE);
    }

    @NotNull
    default IEventCommitter debug() {
        return topic(CLASSIC_TOPIC_DEBUG);
    }

    @NotNull
    default IEventCommitter info() {
        return topic(CLASSIC_TOPIC_INFO);
    }

    @NotNull
    default IEventCommitter warn() {
        return topic(CLASSIC_TOPIC_WARN);
    }

    @NotNull
    default IEventCommitter error() {
        return topic(CLASSIC_TOPIC_ERROR);
    }

}
