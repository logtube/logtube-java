package io.github.logtube.logger;

import io.github.logtube.ICommittableEvent;
import io.github.logtube.ICompatibleLogger;
import io.github.logtube.IEventFilter;
import io.github.logtube.IEventLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DerivedEventLogger implements ICompatibleLogger {

    @Nullable
    private final IEventFilter filter;

    @NotNull
    private final IEventLogger parent;

    @NotNull
    private final String name;

    public DerivedEventLogger(@NotNull IEventLogger parent, @NotNull String name, @Nullable IEventFilter filter) {
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
        return new DerivedEventLogger(this, name, filter);
    }

    @Override
    public @NotNull ICommittableEvent event() {
        ICommittableEvent event = parent.event();
        if (this.filter != null) {
            filter.handle(event);
        }
        return event;
    }

    @Override
    public boolean isTopicEnabled(@NotNull String topic) {
        return parent.isTopicEnabled(topic);
    }

}
