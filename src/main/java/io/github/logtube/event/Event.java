package io.github.logtube.event;

import io.github.logtube.IEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Event logtube log event in compact format
 */
public class Event implements IEvent {

    private long timestamp;

    @NotNull
    private String hostname = "";

    @NotNull
    private String env = "";

    @NotNull
    private String project = "";

    @NotNull
    private String topic = EMPTY_TOPIC;

    @NotNull
    private String crid = EMPTY_CRID;

    @Nullable
    private String message = null;

    @Nullable
    private String keyword = null;

    @Nullable
    private HashMap<String, Object> extra;

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    @NotNull
    public String getHostname() {
        return hostname;
    }

    public void setHostname(@NotNull String hostname) {
        this.hostname = hostname;
    }

    @Override
    @NotNull
    public String getEnv() {
        return env;
    }

    public void setEnv(@NotNull String env) {
        this.env = env;
    }

    @NotNull
    public String getProject() {
        return project;
    }

    public void setProject(@NotNull String project) {
        this.project = project;
    }

    @Override
    @NotNull
    public String getTopic() {
        return topic;
    }

    public void setTopic(@NotNull String topic) {
        this.topic = topic;
    }


    @Override
    @NotNull
    public String getCrid() {
        return crid;
    }

    public void setCrid(@NotNull String crid) {
        this.crid = crid;
    }

    @Override
    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Override
    @Nullable
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(@Nullable String keyword) {
        this.keyword = keyword;
    }

    @Override
    @Nullable
    public HashMap<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(@Nullable HashMap<String, Object> extra) {
        this.extra = extra;
    }

}
