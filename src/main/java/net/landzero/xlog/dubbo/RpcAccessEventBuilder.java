package net.landzero.xlog.dubbo;

import net.landzero.xlog.XLogEventBuilder;
import net.landzero.xlog.http.AccessEvent;
import org.jetbrains.annotations.NotNull;

public class RpcAccessEventBuilder implements XLogEventBuilder<AccessEvent> {

    private AccessEvent event = new AccessEvent();

    private long startAt = System.currentTimeMillis();

    @NotNull
    @Override
    public AccessEvent build() {
        this.event.setDuration(System.currentTimeMillis() - this.startAt);

        return this.event;
    }

}
