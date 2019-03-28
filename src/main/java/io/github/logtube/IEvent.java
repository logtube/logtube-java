package io.github.logtube;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IEvent {

    String EMPTY_CRID = "-";

    String EMPTY_TOPIC = ILogger.CLASSIC_TOPIC_INFO;

    long getTimestamp();

    @NotNull
    String getHostname();

    @NotNull
    String getEnvironment();

    @NotNull
    String getProject();

    @NotNull
    String getTopic();


    @NotNull
    String getCrid();

    @Nullable
    String getMessage();

    @Nullable
    String getKeyword();

    @Nullable
    Map<String, Object> getExtra();

}
