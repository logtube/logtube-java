package io.github.logtube.core.loggers;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IEventMiddleware;
import io.github.logtube.core.IEventProcessorFactory;
import io.github.logtube.core.IMutableEvent;
import io.github.logtube.core.events.NOPEvent;
import io.github.logtube.utils.ITopicAware;
import io.github.logtube.utils.Reflections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 普通日志器，具有一个 名字和一套主题规则，使用 共有的处理器生成日志事件，并交给用户
 */
public class EventLogger implements IEventLogger {

    @NotNull
    private final IEventProcessorFactory processorFactory;

    @NotNull
    private final String name;

    @Nullable
    private final ITopicAware topicAware;

    /**
     * 创建一个新的子日志器
     *
     * @param processorFactory 父
     * @param name             名字
     * @param topicAware       主题过滤逻辑
     */
    public EventLogger(@NotNull IEventProcessorFactory processorFactory, @NotNull String name, @Nullable ITopicAware topicAware) {
        this.processorFactory = processorFactory;
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
        IMutableEvent event = this.processorFactory.getProcessor().event()
                .topic(topic)
                .extra("thread_name", Thread.currentThread().getName());
        StackTraceElement element = Reflections.getStackTraceElement();
        if (element != null) {
            event.extras(
                    "class_name", element.getClassName(),
                    "class_line", element.getLineNumber(),
                    "method_name", element.getMethodName()
            );
        }
        return event;
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
