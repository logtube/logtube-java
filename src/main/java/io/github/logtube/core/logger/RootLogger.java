package io.github.logtube.core.logger;

import io.github.logtube.core.*;
import io.github.logtube.core.event.Event;
import io.github.logtube.core.event.NOPEvent;
import io.github.logtube.core.topic.TopicAware;
import io.github.logtube.core.utils.StringUtil;
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

    @Override
    public void clearCrid() {
        this.cridThreadLocal.remove();
    }

    @Override
    public void setCrid(@Nullable String crid) {
        if (crid != null) {
            this.cridThreadLocal.set(crid);
        } else {
            this.cridThreadLocal.set(StringUtil.randomHex(8));
        }
    }

    @Override
    public @NotNull String getCrid() {
        return this.cridThreadLocal.get();
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
        return new LoggerEvent()
                .timestamp(System.currentTimeMillis())
                .hostname(getHostname())
                .env(getEnv())
                .topic(topic)
                .crid(getCrid())
                .project(getProject());
    }

    /**
     * 从 Event 继承而来的子类，将 commit 方法交给 Logger 来执行
     */
    private class LoggerEvent extends Event {

        @Override
        public void commit() {
            if (isTopicEnabled(getTopic())) {
                getOutputs().forEach(o -> o.appendEvent(this));
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
