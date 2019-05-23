package io.github.logtube.dubbo;

import io.github.logtube.Logtube;
import io.github.logtube.core.IMutableEvent;

public class DubboAccessEventCommitter {

    private final IMutableEvent event = Logtube.getLogger(DubboAccessEventCommitter.class).topic("x-access");

    private long startAt = System.currentTimeMillis();

    public void commit() {
        this.event.extra("duration", System.currentTimeMillis() - this.startAt);
        this.event.commit();
    }

}
