package io.github.logtube.core;

import io.github.logtube.utils.ITopicAware;
import org.jetbrains.annotations.NotNull;

public interface IEventLoggerFactory {

    @NotNull
    IEventProcessor getProcessor();

    @NotNull
    ITopicAware getTopicAware(@NotNull String name);

}
