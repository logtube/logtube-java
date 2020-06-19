package io.github.logtube.dubbo27;

import io.github.logtube.Logtube;
import io.github.logtube.core.IMutableEvent;

public class DubboAccessEventCommitter {

    private final IMutableEvent event = Logtube.getLogger(DubboAccessEventCommitter.class).topic("x-access");

    private final long startAt = System.currentTimeMillis();

    public void commit() {
        this.event.xDuration(System.currentTimeMillis() - this.startAt).commit();
    }

}
