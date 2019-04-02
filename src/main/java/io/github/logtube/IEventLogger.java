package io.github.logtube;

import io.github.logtube.event.NOPEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public interface IEventLogger extends ITopicAware, Logger {

    @NotNull
    IEventLogger derive(@NotNull String name, @Nullable IEventFilter filter);

    @NotNull
    ICommittableEvent event();

    @NotNull
    default ICommittableEvent topic(@NotNull String topic) {
        if (isTopicEnabled(topic)) {
            return event().topic(topic);
        }
        return NOPEvent.getSingleton();
    }

    @NotNull
    default IEventLogger keyword(@NotNull String... keywords) {
        return derive(getName(), e -> e.keyword(keywords));
    }

    default ICommittableEvent trace() {
        return topic("trace");
    }

    default ICommittableEvent debug() {
        return topic("debug");
    }

    default ICommittableEvent info() {
        return topic("info");
    }

    default ICommittableEvent warn() {
        return topic("warn");
    }

    default ICommittableEvent error() {
        return topic("error");
    }

}
