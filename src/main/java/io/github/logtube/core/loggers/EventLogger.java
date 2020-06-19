package io.github.logtube.core.loggers;

import io.github.logtube.core.*;
import io.github.logtube.core.events.NOPEvent;
import io.github.logtube.utils.ITopicAware;
import io.github.logtube.utils.Reflections;
import io.github.logtube.utils.Reloadable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 普通日志器，具有一个 名字和一套主题规则，使用 共有的处理器生成日志事件，并交给用户
 */
public class EventLogger implements IEventLogger, Reloadable {

    @NotNull
    private final IEventLoggerFactory loggerFactory;

    @NotNull
    private final String name;

    @Nullable
    private ITopicAware topicAware;

    @Nullable
    private IEventProcessor processor;

    /**
     * 创建一个新的子日志器
     *
     * @param loggerFactory 父
     * @param name          名字
     */
    public EventLogger(@NotNull IEventLoggerFactory loggerFactory, @NotNull String name) {
        this.loggerFactory = loggerFactory;
        this.name = name;
        init();
    }

    public void init() {
        this.topicAware = this.loggerFactory.getTopicAware(this.name);
        this.processor = this.loggerFactory.getProcessor();
    }

    @Override
    public void reload() {
        init();
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
        IEventProcessor processor = this.processor;
        if (processor == null) {
            return NOPEvent.getSingleton();
        }
        return processor.event()
                .topic(topic)
                .xThreadName(Thread.currentThread().getName())
                .xStackTraceElement(Reflections.getStackTraceElement(), null);
    }

    @Override
    public boolean isTopicEnabled(@NotNull String topic) {
        if (this.topicAware != null) {
            return this.topicAware.isTopicEnabled(topic);
        }
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return this.topicAware != null && this.topicAware.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return this.topicAware != null && this.topicAware.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return this.topicAware != null && this.topicAware.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return this.topicAware != null && this.topicAware.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return this.topicAware != null && this.topicAware.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return this.topicAware != null && this.topicAware.isFatalEnabled();
    }

    @Override
    @NotNull
    public String getName() {
        return this.name;
    }

}
