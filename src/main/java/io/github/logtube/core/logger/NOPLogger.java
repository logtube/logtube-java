package io.github.logtube.core.logger;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IEventMiddleware;
import io.github.logtube.core.IMutableEvent;
import io.github.logtube.core.IRootEventLogger;
import io.github.logtube.core.event.NOPEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class NOPLogger implements IRootEventLogger {

    private static final NOPLogger SINGLETON = new NOPLogger();

    public static NOPLogger getSingleton() {
        return SINGLETON;
    }

    private NOPLogger() {
    }

    @Override
    public void clearCrid() {
    }

    @Override
    public void setCrid(@Nullable String crid) {
    }

    @Override
    public @NotNull String getCrid() {
        return "";
    }

    @Override
    public void clearPath() {
    }

    @Override
    public void setPath(@Nullable String path) {
    }

    @Override
    public @Nullable String getPath() {
        return null;
    }

    @Override
    public @Nullable String getPathDigest() {
        return null;
    }

    @Override
    public @NotNull IEventLogger derive(@Nullable String name, @Nullable IEventMiddleware middleware) {
        return this;
    }

    @Override
    public @NotNull IMutableEvent topic(@NotNull String topic) {
        return NOPEvent.getSingleton();
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void setTopics(@Nullable Set<String> topics) {
    }

    @Override
    public boolean isTopicEnabled(@NotNull String topic) {
        return false;
    }

    @Override
    public String getName() {
        return "";
    }

}
