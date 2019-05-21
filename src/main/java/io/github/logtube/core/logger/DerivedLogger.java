package io.github.logtube.core.logger;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IEventMiddleware;
import io.github.logtube.core.IMutableEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 从 根日志器 派生出来的 日志器
 */
public class DerivedLogger implements IEventLogger {

    @Nullable
    private final IEventMiddleware middleware;

    @NotNull
    private final IEventLogger parent;

    @NotNull
    private final String name;

    /**
     * 创建一个新的子日志器
     *
     * @param parent     父
     * @param name       名字
     * @param middleware 可选的过滤器
     */
    public DerivedLogger(@NotNull IEventLogger parent, @NotNull String name, @Nullable IEventMiddleware middleware) {
        this.middleware = middleware;
        this.parent = parent;
        this.name = name;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    @NotNull
    public IEventLogger derive(@Nullable String name, @Nullable IEventMiddleware middleware) {
        if (name == null) {
            if (middleware == null) {
                return this;
            }
            name = getName();
        }
        return new DerivedLogger(this, name, middleware);
    }

    @Override
    public @NotNull IMutableEvent topic(@NotNull String topic) {
        IMutableEvent event = this.parent.topic(topic);
        if (this.middleware != null) {
            event = middleware.handle(event);
        }
        return event;
    }

    @Override
    public boolean isTopicEnabled(@NotNull String topic) {
        return this.parent.isTopicEnabled(topic);
    }


    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

}
