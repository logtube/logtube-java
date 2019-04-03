package io.github.logtube.logger;

import io.github.logtube.*;
import io.github.logtube.event.Event;
import io.github.logtube.event.NOPEvent;
import io.github.logtube.topic.TopicAware;
import io.github.logtube.utils.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RootLogger extends TopicAware implements IRootEventLogger {

    private String hostname = null;

    private String project = null;

    private String env = null;

    @Override
    @Nullable
    public String getHostname() {
        return hostname;
    }

    @Override
    public void setHostname(@Nullable String hostname) {
        this.hostname = hostname;
    }

    @Override
    @Nullable
    public String getProject() {
        return project;
    }

    @Override
    public void setProject(@Nullable String project) {
        this.project = project;
    }

    @Override
    @Nullable
    public String getEnv() {
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
    @NotNull
    public List<IEventOutput> getOutputs() {
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
    @NotNull
    public String getCrid() {
        return this.cridThreadLocal.get();
    }

    @Override
    public String getName() {
        return Logger.ROOT_LOGGER_NAME;
    }

    @Override
    @NotNull
    public IEventLogger derive(@NotNull String name, @Nullable IEventFilter filter) {
        return new DerivedLogger(this, name, filter);
    }

    @Override
    public @NotNull IMutableEvent topic(@NotNull String topic) {
        if (!isTopicEnabled(topic)) {
            return NOPEvent.getSingleton();
        }
        return new MutableEvent()
                .timestamp(System.currentTimeMillis())
                .hostname(getHostname())
                .env(getEnv())
                .topic(topic)
                .crid(getCrid())
                .project(getProject());
    }

    private class MutableEvent extends Event {

        @Override
        public void commit() {
            if (isTopicEnabled(getTopic())) {
                getOutputs().forEach(o -> o.appendEvent(this));
            }
        }

    }

    public class CridThreadLocal extends ThreadLocal<String> {

        @Override
        protected String initialValue() {
            return "-";
        }

    }

}
