package io.github.logtube;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IEventCommitter {

    // hostname, project, env are not included in committer

    @NotNull
    @Contract("_ -> this")
    IEventCommitter timestamp(long timestamp);

    @NotNull
    @Contract("_ -> this")
    IEventCommitter topic(@Nullable String topic);

    @NotNull
    @Contract("_ -> this")
    IEventCommitter crid(@Nullable String crid);

    @NotNull
    @Contract("_ -> this")
    IEventCommitter message(@NotNull String message);

    @NotNull
    @Contract("_ -> this")
    IEventCommitter keyword(@NotNull String... keywords);

    @NotNull
    @Contract("_ -> this")
    IEventCommitter extra(@NotNull Object... kvs);

    void commit();

}
