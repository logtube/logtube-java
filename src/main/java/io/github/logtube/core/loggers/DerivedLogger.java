package io.github.logtube.core.loggers;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IEventMiddleware;
import io.github.logtube.core.IMutableEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 从 根日志器 派生出来的 日志器
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
     * @param middleware 可选的过滤器
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

}
