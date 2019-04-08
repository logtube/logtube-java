package net.landzero.xlog;

import org.jetbrains.annotations.NotNull;

public interface XLogEventBuilder<T extends XLogEvent> extends XLogCommitter {

    @NotNull
    T build();

    @Override
    default void commit() {
        XLog.appendEvent(this.build());
    }

}
