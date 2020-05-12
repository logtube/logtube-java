package io.github.logtube.core;

import org.jetbrains.annotations.NotNull;

public interface IEventProcessorFactory {

    @NotNull
    IEventProcessor getProcessor();

}
