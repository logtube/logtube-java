package io.github.logtube.core.logger;

import io.github.logtube.core.*;
import io.github.logtube.core.event.Event;
import io.github.logtube.core.event.NOPEvent;
import io.github.logtube.core.topic.TopicAware;
import io.github.logtube.utils.Hex;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 根日志器，通常一个项目只有一个根日志器，存储 主机名、项目名 和 环境名，包含多个日志输出，并且存储线程级 CRID
 */
public class RootLogger extends TopicAware implements IRootEventLogger {

    private @Nullable String hostname = null;

    private @Nullable String project = null;

    private @Nullable String env = null;

    @Override
    public @Nullable String getHostname() {
        return hostname;
    }

    @Override
    public void setHostname(@Nullable String hostname) {
        this.hostname = hostname;
    }

    @Override
    public @Nullable String getProject() {
        return project;
    }

    @Override
    public void setProject(@Nullable String project) {
        this.project = project;
    }

    @Override
    public @Nullable String getEnv() {
        return env;
    }

    @Override
    public void setEnv(@Nullable String env) {
        this.env = env;
    }

    @NotNull
    private List<IEventOutput> outputs = new ArrayList<>();

    @Override
    public void setOutputs(@NotNull List<IEventOutput> outputs) {
        this.outputs = outputs;
    }

    @Override
    public @NotNull List<IEventOutput> getOutputs() {
        return this.outputs;
    }

    private final CridThreadLocal cridThreadLocal = new CridThreadLocal();

    private final ThreadLocal<String> pathThreadLocal = new ThreadLocal<>();

    private final ThreadLocal<String> pathDigestThreadLocal = new ThreadLocal<>();

    @Override
    public void clearCrid() {
        this.cridThreadLocal.remove();
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
    public @NotNull String getCrid() {
        return this.cridThreadLocal.get();
    }

    @Override
    public void clearPath() {
        this.pathDigestThreadLocal.remove();
        this.pathThreadLocal.remove();
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
        } else {
            clearPath();
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
    public String getName() {
        return Logger.ROOT_LOGGER_NAME;
    }

    @Override
    public @NotNull IEventLogger derive(@Nullable String name, @Nullable IEventMiddleware middleware) {
        if (name == null) {
            if (middleware == null) {
                return this;
            }
            name = getName();
        }
        return new DerivedLogger(this, name, middleware);
    }

    @Override
    public @NotNull IMutableEvent topic(@NotNull String topic) {
        if (!isTopicEnabled(topic)) {
            return NOPEvent.getSingleton();
        }
        return new LoggerEvent().timestamp(System.currentTimeMillis()).topic(topic);
    }

    private IEvent decorate(@NotNull IMutableEvent e) {
        return e
                .hostname(getHostname())
                .env(getEnv())
                .project(getProject())
                .crid(getCrid())
                .extras("path", getPath(), "path_digest", getPathDigest());
    }

    /**
     * 从 Event 继承而来的子类，将 commit 方法交给 Logger 来执行
     */
    private class LoggerEvent extends Event {

        @Override
        public void commit() {
            if (isTopicEnabled(getTopic())) {
                getOutputs().forEach(o -> o.appendEvent(decorate(this)));
            }
        }

    }

    private class CridThreadLocal extends ThreadLocal<String> {

        @Override
        protected String initialValue() {
            return "-";
        }

    }

}
