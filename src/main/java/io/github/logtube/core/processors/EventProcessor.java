package io.github.logtube.core.processors;

import io.github.logtube.core.*;
import io.github.logtube.core.context.EventContext;
import io.github.logtube.core.events.Event;
import io.github.logtube.utils.Hex;
import io.github.logtube.utils.ILifeCycle;
import io.github.logtube.utils.LifeCycle;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理器，通常一个项目只有一个处理器，存储 主机名、项目名 和 环境名，包含多个日志输出，并且存储线程级 CRID，产生具备有效 commit 方法的日志事件
 */
public class EventProcessor extends LifeCycle implements IEventProcessor {

    public static final String UNKNOWN_PROJECT = "unknown-project";

    private @Nullable String hostname = null;

    private @Nullable String project = null;

    private @Nullable String env = null;

    public @Nullable String getHostname() {
        return hostname;
    }

    public void setHostname(@Nullable String hostname) {
        this.hostname = hostname;
    }

    public @NotNull String getProject() {
        return project == null ? UNKNOWN_PROJECT : project;
    }

    public void setProject(@Nullable String project) {
        this.project = project;
    }

    public @Nullable String getEnv() {
        return env;
    }

    public void setEnv(@Nullable String env) {
        this.env = env;
    }

    @NotNull
    private List<IEventOutput> outputs = new ArrayList<>();

    public void setOutputs(@NotNull List<IEventOutput> outputs) {
        this.outputs = outputs;
    }

    public @NotNull List<IEventOutput> getOutputs() {
        return this.outputs;
    }

    public void addOutput(@NotNull IEventOutput output) {
        this.outputs.add(output);
    }

    @NotNull
    private Map<String, String> topicMappings = new HashMap<>();

    public void setTopicMappings(@Nullable Map<String, String> topicMappings) {
        if (topicMappings == null) {
            this.topicMappings = new HashMap<>();
        } else {
            this.topicMappings = topicMappings;
        }
    }

    private @NotNull String resolveTopic(@NotNull String topic) {
        return this.topicMappings.getOrDefault(topic, topic);
    }

    private final CridThreadLocal cridThreadLocal = new CridThreadLocal();

    private final CrsrcThreadLocal crsrcThreadLocal = new CrsrcThreadLocal();

    private final ThreadLocal<String> pathThreadLocal = new InheritableThreadLocal<>();

    private final ThreadLocal<String> pathDigestThreadLocal = new InheritableThreadLocal<>();

    @Override
    @NotNull
    public IEventContext captureContext() {
        return new EventContext(this.getCrid(), this.getCrsrc(), this.getPath());
    }

    @Override
    public void clearContext() {
        this.cridThreadLocal.remove();
        this.crsrcThreadLocal.remove();
        this.pathDigestThreadLocal.remove();
        this.pathThreadLocal.remove();
    }

    @Override
    public void setCrid(@Nullable String crid) {
        if (crid != null) {
            this.cridThreadLocal.set(crid);
        } else {
            this.cridThreadLocal.set(Hex.randomHex16());
        }
    }

    @Override
    public void setCrsrc(@Nullable String crsrc) {
        if (crsrc != null) {
            this.crsrcThreadLocal.set(crsrc);
        } else {
            this.crsrcThreadLocal.remove();
        }
    }

    @Override
    public @NotNull String getCrid() {
        return this.cridThreadLocal.get();
    }

    @Override
    public @NotNull String getCrsrc() {
        return this.crsrcThreadLocal.get();
    }

    @Override
    public void setPath(@Nullable String path) {
        path = Strings.normalize(path);
        if (path != null) {
            /*
             * /api/v1/goods/detail/3656081/1/68/4403
             * /api/v1/goods/detail/3656082/1/68/4404
             * /api/v1/goods/detail/3656082/1/68/4405
             *
             *  以上请求判定为同类请求
             */
            this.pathDigestThreadLocal.set(Hex.md5(path.replaceAll("(\\b/)\\d+", "/:num")));
            this.pathThreadLocal.set(path);
        }
    }

    @Override
    public @Nullable String getPath() {
        return this.pathThreadLocal.get();
    }

    @Override
    public @Nullable String getPathDigest() {
        return this.pathDigestThreadLocal.get();
    }

    @Override
    @NotNull
    public IMutableEvent event() {
        return new LoggerEvent().timestamp(System.currentTimeMillis());
    }

    private IEvent decorate(@NotNull IMutableEvent e) {
        return e
                .topic(resolveTopic(e.getTopic()))
                .hostname(getHostname())
                .env(getEnv())
                .project(getProject())
                .crid(getCrid())
                .crsrc(getCrsrc())
                .extras("path", getPath(), "path_digest", getPathDigest());
    }

    @Override
    public void doStart() {
        super.doStart();
        this.outputs.forEach(ILifeCycle::start);
    }

    @Override
    public void doStop() {
        this.outputs.forEach(ILifeCycle::stop);
        super.doStop();
    }

    /**
     * 从 Event 继承而来的子类，将 commit 方法交给 Logger 来执行
     */
    private class LoggerEvent extends Event {

        @Override
        public void commit() {
            if (!EventProcessor.this.isStarted) {
                return;
            }
            getOutputs().forEach(o -> {
                try {
                    o.appendEvent(decorate(this));
                } catch (Exception ignored) {
                }
            });
        }

    }

    private static class CridThreadLocal extends InheritableThreadLocal<String> {

        @Override
        protected String initialValue() {
            return "-";
        }

    }

    private static class CrsrcThreadLocal extends InheritableThreadLocal<String> {

        @Override
        protected String initialValue() {
            return "-";
        }

    }

}
