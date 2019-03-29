package io.github.logtube.event;

import io.github.logtube.ICommittableEvent;
import io.github.logtube.utils.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Event logtube log event in compact format
 */
public class Event implements ICommittableEvent {

    public static final String UNKNOWN_HOSTNAME = "unknown-host";

    public static final String UNKNOWN_PROJECT = "unknown-project";

    public static final String UNKNOWN_ENV = "unknown-env";

    public static final String UNKNOWN_TOPIC = "unknown-topic";

    public static final String UNKNOWN_CRID = "-";

    private long timestamp;

    @Nullable
    private String hostname = null;

    @Nullable
    private String env = null;

    @Nullable
    private String project = null;

    @Nullable
    private String topic = null;

    @Nullable
    private String crid = null;

    @Nullable
    private String message = null;

    @Nullable
    private String keyword = null;

    @Nullable
    private Map<String, Object> extra;

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    @NotNull
    public String getHostname() {
        return hostname == null ? UNKNOWN_HOSTNAME : hostname;
    }

    @Override
    public void setHostname(@Nullable String hostname) {
        this.hostname = hostname;
    }

    @Override
    @NotNull
    public String getEnv() {
        return env == null ? UNKNOWN_ENV : env;
    }

    @Override
    public void setEnv(@Nullable String env) {
        this.env = StringUtil.safeString(env, null);
    }

    @NotNull
    public String getProject() {
        return project == null ? UNKNOWN_PROJECT : project;
    }

    @Override
    public void setProject(@Nullable String project) {
        this.project = StringUtil.safeString(project, null);
    }

    @Override
    @NotNull
    public String getTopic() {
        return topic == null ? UNKNOWN_TOPIC : topic;
    }

    @Override
    public void setTopic(@Nullable String topic) {
        this.topic = StringUtil.safeString(topic, null);
    }


    @Override
    @NotNull
    public String getCrid() {
        return crid == null ? UNKNOWN_CRID : crid;
    }

    @Override
    public void setCrid(@Nullable String crid) {
        this.crid = StringUtil.safeString(crid, null);
    }

    @Override
    @Nullable
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Override
    @Nullable
    public String getKeyword() {
        return keyword;
    }

    @Override
    public void setKeyword(@Nullable String keyword) {
        this.keyword = keyword;
    }

    @Override
    @Nullable
    public Map<String, Object> getExtra() {
        return extra;
    }

    @Override
    public void setExtra(@Nullable Map<String, Object> extra) {
        this.extra = extra;
    }

    @Override
    public void commit() {
        throw new RuntimeException("never call Event#commit");
    }

}
