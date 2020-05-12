package io.github.logtube.perf;

import io.github.logtube.Logtube;
import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IMutableEvent;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class XPerfCommitter {

    private final static IEventLogger defaultLogger = Logtube.getLogger(XPerfCommitter.class);

    private final IMutableEvent event;

    private final long startTime = System.currentTimeMillis();

    public XPerfCommitter(@Nullable IEventLogger logger, @Nullable String className, @Nullable String methodName) {
        if (logger == null) {
            logger = defaultLogger;
        }
        this.event = logger.topic("x-perf")
                .extra("logger_name", logger.getName())
                .extra("class_name", className)
                .extra("method_name", methodName);
    }

    @Deprecated
    public XPerfCommitter(@Nullable String className, @Nullable String methodName) {
        this(null, className, methodName);
    }

    @NotNull
    @Contract("_,_->this")
    public XPerfCommitter setCommand(String action, Object... arguments) {
        this.event.extra("action", action);
        this.event.extra("arguments", Arrays.stream(arguments).map(Strings::safeNormalize).collect(Collectors.toList()));
        return this;
    }

    public void commit() {
        this.event.extra("duration", System.currentTimeMillis() - this.startTime);
        this.event.commit();
    }

}
