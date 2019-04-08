package net.landzero.xlog.perf;

import net.landzero.xlog.XLogEventBuilder;
import net.landzero.xlog.utils.Strings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class XPerfEventBuilder implements XLogEventBuilder<XPerfEvent> {

    private final XPerfEvent event = new XPerfEvent();

    private final long startTime = System.currentTimeMillis();

    public XPerfEventBuilder(String className, String methodName) {
        this.event.setClassName(className);
        this.event.setMethodName(methodName);
    }

    @NotNull
    @Contract("_,_->this")
    public XPerfEventBuilder setCommand(String action, Object... arguments) {
        this.event.setAction(action);
        this.event.setArguments(Arrays.stream(arguments).map(Strings::safeNormalize).collect(Collectors.toList()));
        return this;
    }

    @Override
    @NotNull
    public XPerfEvent build() {
        this.event.setDuration(System.currentTimeMillis() - this.startTime);
        return this.event;
    }

}
