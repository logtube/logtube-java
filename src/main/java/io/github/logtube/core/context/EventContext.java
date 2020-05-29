package io.github.logtube.core.context;

import io.github.logtube.Logtube;
import io.github.logtube.core.IEventContext;
import org.jetbrains.annotations.Nullable;

public class EventContext implements IEventContext {

    @Nullable
    private final String crid;

    @Nullable
    private final String crsrc;

    @Nullable
    private final String path;

    public EventContext(@Nullable String crid, @Nullable String crsrc, @Nullable String path) {
        this.crid = crid;
        this.crsrc = crsrc;
        this.path = path;
    }

    @Override
    public void restore() {
        Logtube.getProcessor().setCrsrc(crsrc);
        Logtube.getProcessor().setCrid(crid);
        Logtube.getProcessor().setPath(path);
    }

}
