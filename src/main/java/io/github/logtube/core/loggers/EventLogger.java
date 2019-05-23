package io.github.logtube.core.loggers;

import io.github.logtube.core.*;
import io.github.logtube.core.events.NOPEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventLogger implements IEventLogger {

    @NotNull
    private final IEventProcessor processor;

    @NotNull
    private final String name;

    @Nullable
    private final ITopicAware topicAware;

    /**
     * 创建一个新的子日志器
     *
     * @param processor 父
     * @param name      名字
     */
    public EventLogger(@NotNull IEventProcessor processor, @NotNull String name, @Nullable ITopicAware topicAware) {
        this.processor = processor;
        this.name = name;
        this.topicAware = topicAware;
    }

    @Override
    public @NotNull IEventLogger derive(@NotNull IEventMiddleware middleware) {
        return new DerivedLogger(this, middleware);
    }

    @Override
    public @NotNull IMutableEvent topic(@NotNull String topic) {
        if (!isTopicEnabled(topic)) {
            return NOPEvent.getSingleton();
        }
        return this.processor.event().topic(topic);
    }

    @Override
    public boolean isTopicEnabled(@NotNull String topic) {
        if (this.topicAware != null) {
            return this.topicAware.isTopicEnabled(topic);
        }
        return true;
    }

    @Override
    @NotNull
    public String getName() {
        return this.name;
    }

}
