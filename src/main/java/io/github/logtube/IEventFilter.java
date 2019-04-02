package io.github.logtube;

import org.jetbrains.annotations.NotNull;

public interface IEventFilter {

    void handle(@NotNull IMutableEvent event);

}
