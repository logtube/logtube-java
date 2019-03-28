package io.github.logtube.event;

import io.github.logtube.IEventCommitter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NOPEventCommitter implements IEventCommitter {

    private static final NOPEventCommitter SINGLETON = new NOPEventCommitter();

    public static NOPEventCommitter getSingleton() {
        return SINGLETON;
    }

    @Override
    @NotNull
    public IEventCommitter timestamp(long timestamp) {
        return this;
    }

    @Override
    @NotNull
    public IEventCommitter topic(@Nullable String topic) {
        return this;
    }

    @Override
    @NotNull
    public IEventCommitter crid(@Nullable String crid) {
        return this;
    }

    @Override
    @NotNull
    public IEventCommitter message(@NotNull String message) {
        return this;
    }

    @Override
    @NotNull
    public IEventCommitter keyword(@NotNull String... keywords) {
        return this;
    }

    @Override
    @NotNull
    public IEventCommitter extra(@NotNull Object... kvs) {
        return this;
    }

    @Override
    public void commit() {
    }

}
