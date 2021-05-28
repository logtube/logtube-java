package io.github.logtube.core.events;

import io.github.logtube.core.IMutableEvent;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志事件，commit 方法留空，需要让日志器复写该方法
 */
public class Event implements IMutableEvent {

    /**
     * 默认主机名
     */
    public static final String UNKNOWN_HOSTNAME = "unknown-host";

    /**
     * 默认项目名
     */
    public static final String UNKNOWN_PROJECT = "unknown-project";

    /**
     * 默认环境名
     */
    public static final String UNKNOWN_ENV = "unknown-env";

    /**
     * 默认主题
     */
    public static final String UNKNOWN_TOPIC = "unknown-topic";

    /**
     * 默认 CRID
     */
    public static final String UNKNOWN_CRID = "-";

    public static final String UNKNOWN_CRSRC = "-";

    private long timestamp = 0;

    private @Nullable String hostname = null;

    private @Nullable String env = null;

    private @Nullable String project = null;

    private @Nullable String topic = null;

    private @Nullable String crid = null;

    private @Nullable String crsrc = null;

    private @Nullable String message = null;

    private @Nullable String keyword = null;

    private @Nullable Map<String, Object> extra;

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public @NotNull String getHostname() {
        return hostname == null ? UNKNOWN_HOSTNAME : hostname;
    }

    @Override
    public void setHostname(@Nullable String hostname) {
        this.hostname = hostname;
    }

    @Override
    public @NotNull String getEnv() {
        return env == null ? UNKNOWN_ENV : env;
    }

    @Override
    public void setEnv(@Nullable String env) {
        this.env = env;
    }

    @NotNull
    public String getProject() {
        return project == null ? UNKNOWN_PROJECT : project;
    }

    @Override
    public void setProject(@Nullable String project) {
        this.project = project;
    }

    @Override
    public @NotNull String getTopic() {
        return topic == null ? UNKNOWN_TOPIC : topic;
    }

    @Override
    public void setTopic(@Nullable String topic) {
        this.topic = topic;
    }

    @Override
    public @NotNull String getCrid() {
        return crid == null ? UNKNOWN_CRID : crid;
    }

    @Override
    public void setCrid(@Nullable String crid) {
        this.crid = crid;
    }

    @Override
    public @NotNull String getCrsrc() {
        return crsrc == null ? UNKNOWN_CRSRC : crsrc;
    }

    @Override
    public void setCrsrc(@Nullable String crsrc) {
        this.crsrc = crsrc;
    }

    @Override
    public @Nullable String getMessage() {
        return message;
    }

    @Override
    public void setMessage(@Nullable String message) {
        this.message = Strings.normalize(message);
    }

    @Override
    public @Nullable String getKeyword() {
        return keyword;
    }

    @Override
    public void setKeyword(@Nullable String keyword) {
        this.keyword = Strings.normalize(keyword);
    }

    @Override
    public @Nullable Map<String, Object> getExtra() {
        return extra;
    }

    @Override
    public void setExtra(@Nullable Map<String, Object> extra) {
        this.extra = extra;
    }

    /**
     * 禁止使用 Event 的 commit 方法，因为没有意义
     *
     * @throws RuntimeException 任何情况下抛出该异常
     */
    @Override
    public void commit() {
        throw new RuntimeException("never call Event#commit");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Event e = (Event) super.clone();
        if (this.extra != null) {
            e.extra = new HashMap<>(this.extra);
        }
        return e;
    }

}
