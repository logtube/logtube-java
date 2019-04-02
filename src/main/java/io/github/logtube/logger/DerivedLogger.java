package io.github.logtube.logger;

import io.github.logtube.IEventFilter;
import io.github.logtube.IEventLogger;
import io.github.logtube.IMutableEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DerivedLogger implements IEventLogger {

    @Nullable
    private final IEventFilter filter;

    @NotNull
    private final IEventLogger parent;

    @NotNull
    private final String name;

    public DerivedLogger(@NotNull IEventLogger parent, @NotNull String name, @Nullable IEventFilter filter) {
        this.filter = filter;
        this.parent = parent;
        this.name = name;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    @NotNull
    public IEventLogger derive(@NotNull String name, @NotNull IEventFilter filter) {
        return new DerivedLogger(this, name, filter);
    }

    @Override
    public @NotNull IMutableEvent event() {
        IMutableEvent event = parent.event();
        if (this.filter != null) {
            filter.filter(event);
        }
        return event;
    }

    @Override
    public boolean isTopicEnabled(@NotNull String topic) {
        return parent.isTopicEnabled(topic);
    }

}
