package io.github.logtube.core.loggers;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IEventMiddleware;
import io.github.logtube.core.IMutableEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 从普通日志器派生出来的日志器，用于附加过滤器，比如统一增加 keyword 等
 */
public class DerivedLogger implements IEventLogger {

    @NotNull
    private final IEventMiddleware middleware;

    @NotNull
    private final IEventLogger parent;

    /**
     * 派生日志器
     *
     * @param parent     父日志器
     * @param middleware 过滤器
     */
    public DerivedLogger(@NotNull IEventLogger parent, @NotNull IEventMiddleware middleware) {
        this.middleware = middleware;
        this.parent = parent;
    }

    @NotNull
    @Override
    public String getName() {
        return this.parent.getName();
    }

    @Override
    @NotNull
    public IEventLogger derive(@NotNull IEventMiddleware middleware) {
        return new DerivedLogger(this, middleware);
    }

    @Override
    public @NotNull IMutableEvent topic(@NotNull String topic) {
        return this.middleware.handle(this.parent.topic(topic));
    }

    @Override
    public boolean isTopicEnabled(@NotNull String topic) {
        return this.parent.isTopicEnabled(topic);
    }

    @Override
    public boolean isTraceEnabled() {
        return this.parent.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return this.parent.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return this.parent.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return this.parent.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return this.parent.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return this.parent.isFatalEnabled();
    }

}
