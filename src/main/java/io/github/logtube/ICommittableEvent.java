package io.github.logtube;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public interface ICommittableEvent extends IMutableEvent {

    void commit();

    @NotNull
    @Contract("_ -> this")
    default ICommittableEvent timestamp(long timestamp) {
        setTimestamp(timestamp);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    default ICommittableEvent hostname(@Nullable String hostname) {
        setHostname(hostname);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    default ICommittableEvent project(@Nullable String project) {
        setProject(project);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    default ICommittableEvent env(@Nullable String env) {
        setEnv(env);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    default ICommittableEvent topic(@Nullable String topic) {
        setTopic(topic);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    default ICommittableEvent crid(@Nullable String crid) {
        setCrid(crid);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    default ICommittableEvent message(@Nullable String message) {
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

    @NotNull
    @Contract("_ -> this")
    default ICommittableEvent keyword(@NotNull String... keywords) {
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

    @NotNull
    @Contract("_ -> this")
    default ICommittableEvent extra(@NotNull Object... kvs) {
        if (kvs.length == 0 || kvs.length % 2 != 0) {
            throw new IllegalArgumentException("extra key value not match");
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
