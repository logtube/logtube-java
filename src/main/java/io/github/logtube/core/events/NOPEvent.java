package io.github.logtube.core.events;

import io.github.logtube.core.IMutableEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static io.github.logtube.core.events.Event.*;

/**
 * 无操作的日志事件，通常使用单例，用于静默事件
 */
public class NOPEvent implements IMutableEvent {

    private static final NOPEvent SINGLETON = new NOPEvent();

    private NOPEvent() {
    }

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static NOPEvent getSingleton() {
        return SINGLETON;
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
    public void setCrsrc(@Nullable String crsrc) {
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
    public @NotNull String getCrsrc() {
        return UNKNOWN_CRSRC;
    }

    @Override
    public @Nullable String getMessage() {
        return "";
    }

    @Override
    public @Nullable String getKeyword() {
        return "";
    }

    @Override
    public @Nullable Map<String, Object> getExtra() {
        return new HashMap<>();
    }

    /**
     * 提交事件，但是不会做任何事情
     */
    @Override
    public void commit() {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
