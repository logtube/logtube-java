package io.github.logtube;

import org.jetbrains.annotations.NotNull;

public interface IEventOutput {

    void appendEvent(@NotNull IEvent e);

}
