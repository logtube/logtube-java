package io.github.logtube.event;

import io.github.logtube.ICommittableEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static io.github.logtube.event.Event.*;

public class NOPEvent implements ICommittableEvent {

    private static final NOPEvent SINGLETON = new NOPEvent();

    public static NOPEvent getSingleton() {
        return SINGLETON;
    }

    @Override
    public void commit() {
    }

    @Override
    public void setTimestamp(long timestamp) {
    }

    @Override
    public void setHostname(@Nullable String hostname) {

    }

    @Override
    public void setEnv(@Nullable String env) {

    }

    @Override
    public void setProject(@Nullable String project) {

    }

    @Override
    public void setTopic(@Nullable String topic) {

    }

    @Override
    public void setCrid(@Nullable String crid) {

    }

    @Override
    public void setMessage(@Nullable String message) {

    }

    @Override
    public void setKeyword(@Nullable String keyword) {

    }

    @Override
    public void setExtra(@Nullable Map<String, Object> extra) {

    }

    @Override
    public long getTimestamp() {
        return 0;
    }

    @Override
    public @NotNull String getHostname() {
        return UNKNOWN_HOSTNAME;
    }

    @Override
    public @NotNull String getEnv() {
        return UNKNOWN_ENV;
    }

    @Override
    public @NotNull String getProject() {
        return UNKNOWN_PROJECT;
    }

    @Override
    public @NotNull String getTopic() {
        return UNKNOWN_TOPIC;
    }

    @Override
    public @NotNull String getCrid() {
        return UNKNOWN_CRID;
    }

    @Override
    public @Nullable String getMessage() {
        return null;
    }

    @Override
    public @Nullable String getKeyword() {
        return null;
    }

    @Override
    public @Nullable Map<String, Object> getExtra() {
        return null;
    }

}
