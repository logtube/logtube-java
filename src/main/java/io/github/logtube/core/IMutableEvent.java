package io.github.logtube.core;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 可写日志事件，这是用户最常接触的接口，用于创建日志事件，并最终提交事件到日志器，并包含一系列糖方法
 */
public interface IMutableEvent extends IEvent {

    void setTimestamp(long timestamp);

    void setHostname(@Nullable String hostname);

    void setEnv(@Nullable String env);

    void setProject(@Nullable String project);

    void setTopic(@Nullable String topic);

    void setCrid(@Nullable String crid);

    void setMessage(@Nullable String message);

    void setKeyword(@Nullable String keyword);

    void setExtra(@Nullable Map<String, Object> extra);

    void commit();

    @Contract("_ -> this")
    default @NotNull IMutableEvent timestamp(long timestamp) {
        setTimestamp(timestamp);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent hostname(@Nullable String hostname) {
        setHostname(hostname);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent project(@Nullable String project) {
        setProject(project);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent env(@Nullable String env) {
        setEnv(env);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent topic(@Nullable String topic) {
        setTopic(topic);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent crid(@Nullable String crid) {
        setCrid(crid);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent message(@Nullable String message) {
        if (message == null) {
            return this;
        }
        String current = getMessage();
        if (current == null) {
            setMessage(message);
        } else {
            setMessage(current + " " + message);
        }
        return this;
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent keyword(@NotNull String... keywords) {
        if (keywords.length == 0) {
            return this;
        }
        String current = getKeyword();
        if (current == null) {
            setKeyword(String.join(",", keywords));
        } else {
            setKeyword(current + "," + String.join(",", keywords));
        }
        return this;
    }

    @Contract("_, _ -> this")
    default @NotNull IMutableEvent extra(@NotNull String k, @Nullable Object v) {
        Map<String, Object> extra = getExtra();
        if (extra == null) {
            extra = new HashMap<>();
        }
        if (v == null) {
            extra.remove(k);
        } else {
            if (v instanceof String || v instanceof Number || v instanceof Boolean) {
                extra.put(k, v);
            } else {
                extra.put(k, v.toString());
            }
        }
        setExtra(extra);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent extras(@NotNull Object... kvs) {
        if (kvs.length == 0 || kvs.length % 2 != 0) {
            throw new IllegalArgumentException("extras key value not match");
        }
        Map<String, Object> extra = getExtra();
        if (extra == null) {
            extra = new HashMap<>();
        }
        for (int i = 0; i < kvs.length; i += 2) {
            String k = kvs[i].toString();
            Object v = kvs[i + 1];
            if (v instanceof String || v instanceof Number || v instanceof Boolean) {
                extra.put(k, v);
            } else {
                extra.put(k, v.toString());
            }
        }
        setExtra(extra);
        return this;
    }

}
