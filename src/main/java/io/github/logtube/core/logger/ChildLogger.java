package io.github.logtube.core.logger;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IEventMiddleware;
import io.github.logtube.core.IMutableEvent;
import io.github.logtube.core.topic.TopicAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ChildLogger implements IEventLogger {

    @NotNull
    private final IEventLogger parent;

    @NotNull
    private final String name;

    @Nullable
    private final TopicAware topicAware;

    /**
     * 创建一个新的子日志器
     *
     * @param parent 父
     * @param name   名字
     */
    public ChildLogger(@NotNull IEventLogger parent, @NotNull String name, @Nullable Set<String> topics) {
        this.parent = parent;
        this.name = name;
        if (topics != null) {
            this.topicAware = new TopicAware();
            this.topicAware.setTopics(topics);
        } else {
            this.topicAware = null;
        }
    }

    @Override
    public @NotNull IEventLogger derive(@NotNull IEventMiddleware middleware) {
        return new DerivedLogger(this, middleware);
    }

    @Override
    public @NotNull IMutableEvent topic(@NotNull String topic) {
        return this.parent.topic(topic);
    }

    @Override
    public boolean isTopicEnabled(@NotNull String topic) {
        if (this.topicAware != null) {
            return this.topicAware.isTopicEnabled(topic);
        }
        return this.parent.isTopicEnabled(topic);
    }

    @Override
    @NotNull
    public String getName() {
        return this.name;
    }

}
