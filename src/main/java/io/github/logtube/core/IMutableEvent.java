package io.github.logtube.core;

import io.github.logtube.LogtubeConstants;
import io.github.logtube.utils.Reflections;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
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

    void setCrsrc(@Nullable String crsrc);

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
    default @NotNull IMutableEvent crsrc(@Nullable String crsrc) {
        setCrsrc(crsrc);
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
    default @NotNull IMutableEvent keyword(@Nullable Object... ks) {
        if (ks == null || ks.length == 0 || ks.length > 100) return this;
        String[] kws = new String[ks.length];
        for (int i = 0; i < ks.length; i++) {
            kws[i] = Strings.safeNormalizeKeyword(String.valueOf(ks[i]));
        }
        String current = getKeyword();
        if (current == null) {
            setKeyword(String.join(",", kws));
        } else {
            setKeyword(current + "," + String.join(",", kws));
        }
        return this;
    }

    @Contract("_, _ -> this")
    default @NotNull IMutableEvent extra(@NotNull String k, @Nullable Object v) {
        Map<String, Object> extra = getExtra();
        if (extra == null) {
            if (v == null) {
                return this;
            }
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
    default @NotNull IMutableEvent extras(@Nullable Object... kvs) {
        if (kvs == null || kvs.length == 0 || kvs.length % 2 != 0) {
            throw new IllegalArgumentException("extras key value not match");
        }
        Map<String, Object> extra = getExtra();
        if (extra == null) {
            extra = new HashMap<>();
        }
        for (int i = 0; i < kvs.length; i += 2) {
            Object k = kvs[i];
            Object v = kvs[i + 1];
            if (k == null) {
                continue;
            }
            if (v == null) {
                extra.remove(k.toString());
                continue;
            }
            if (v instanceof String || v instanceof Number || v instanceof Boolean) {
                extra.put(k.toString(), v);
            } else {
                extra.put(k.toString(), v.toString());
            }
        }
        setExtra(extra);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent xPath(@Nullable String path) {
        return this.extra("path", path);
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent xPathDigest(@Nullable String pathDigest) {
        return this.extra("path_digest", pathDigest);
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent xDuration(long duration) {
        return this.extra("duration", duration);
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent xException(@Nullable Throwable t) {
        if (t == null) {
            return this.xExceptionClass(null).xExceptionStack(null);
        } else {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            return this.xExceptionClass(t.getClass().getCanonicalName())
                    .xExceptionStack(sw.toString());
        }
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent xExceptionStack(@Nullable String stack) {
        if (stack == null) {
            boolean autoStackTrace = false;
            for (String topic : LogtubeConstants.TOPICS_AUTO_STACK_TRACE) {
                if (topic.equalsIgnoreCase(getTopic())) {
                    autoStackTrace = true;
                    break;
                }
            }

            if (autoStackTrace) {
                @Nullable StackTraceElement[] elements = Reflections.getStackTraceElements();
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement element : elements) {
                    if (element == null) {
                        continue;
                    }
                    sb.append("\tat ");
                    sb.append(element.getClassName());
                    sb.append(".");
                    sb.append(element.getMethodName());
                    sb.append("(");
                    sb.append(element.getFileName());
                    sb.append(":");
                    sb.append(element.getLineNumber());
                    sb.append(")");
                    sb.append("\n");
                }
                return this.extra("exception_stack", sb.toString());
            }
        }
        return this.extra("exception_stack", stack);
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent xExceptionClass(@Nullable String clazz) {
        return this.extra("exception_class", clazz);
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent xThreadName(@Nullable String name) {
        return this.extra("thread_name", name);
    }

    @Contract("_,_ -> this")
    default @NotNull IMutableEvent xStackTraceElement(@Nullable StackTraceElement element, @Nullable String prefix) {
        if (element == null) {
            return this.extras(
                    prefix + "class_name", null,
                    prefix + "class_line", null,
                    prefix + "method_name", null
            );
        } else {
            if (prefix == null) {
                prefix = "";
            }
            return this.extras(
                    prefix + "class_name", element.getClassName(),
                    prefix + "class_line", element.getLineNumber(),
                    prefix + "method_name", element.getMethodName()
            );
        }
    }

    @Contract("_ -> this")
    default @NotNull IMutableEvent xTargetProject(@Nullable String targetProject) {
        return this.extra("target_project", targetProject);
    }

}
