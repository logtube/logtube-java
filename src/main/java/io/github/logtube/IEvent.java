package io.github.logtube;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IEvent {

    long getTimestamp();

    @NotNull
    String getHostname();

    @NotNull
    String getEnv();

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
