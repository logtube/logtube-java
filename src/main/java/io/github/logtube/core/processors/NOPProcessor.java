package io.github.logtube.core.processors;

import io.github.logtube.core.IEventProcessor;
import io.github.logtube.core.IMutableEvent;
import io.github.logtube.core.events.NOPEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NOPProcessor implements IEventProcessor {

    private static final NOPProcessor SINGLETON = new NOPProcessor();

    public static NOPProcessor getSingleton() {
        return SINGLETON;
    }

    private NOPProcessor() {
    }

    @Override
    public @NotNull IMutableEvent event() {
        return NOPEvent.getSingleton();
    }

    @Override
    public void clearCrid() {

    }

    @Override
    public void setCrid(@Nullable String crid) {
    }

    @Override
    public @NotNull String getCrid() {
        return "-";
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
    public void start() {

    }

    @Override
    public void stop() {
    }

}
