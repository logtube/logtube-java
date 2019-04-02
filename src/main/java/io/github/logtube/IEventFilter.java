package io.github.logtube;

import org.jetbrains.annotations.NotNull;

public interface IEventFilter {

    @NotNull
    IMutableEvent filter(@NotNull IMutableEvent event);

}
