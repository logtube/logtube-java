package io.github.logtube.core.processors;

import io.github.logtube.core.IEventContext;
import io.github.logtube.core.IEventProcessor;
import io.github.logtube.core.IMutableEvent;
import io.github.logtube.core.context.NOPEventContext;
import io.github.logtube.core.events.NOPEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 无操作的日志处理器，用于在系统未初始化的时候，保持逻辑正常
 */
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
    @NotNull
    public IEventContext captureContext() {
        return NOPEventContext.getSingleton();
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
