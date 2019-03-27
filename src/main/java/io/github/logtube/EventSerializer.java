package io.github.logtube;

import org.jetbrains.annotations.NotNull;

public interface EventSerializer {

    @NotNull
    String serialize(@NotNull Event e);

}
