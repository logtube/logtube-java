package io.github.logtube.logger;

import io.github.logtube.*;
import io.github.logtube.event.Event;
import io.github.logtube.topic.TopicAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class EventLogger extends TopicAware implements ICompatibleLogger {

    private String hostname = null;

    private String project = null;

    private String env = null;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    @NotNull
    private List<IEventOutput> outputs = new ArrayList<>();

    @NotNull
    private List<IEventFilter> filters = new ArrayList<>();

    public void setOutputs(@NotNull List<IEventOutput> outputs) {
        this.outputs = outputs;
    }

    @Nullable
    public List<IEventOutput> getOutputs() {
        return this.outputs;
    }

    public void addOutput(@NotNull IEventOutput output) {
        this.outputs.add(output);
    }

    @NotNull
    public List<IEventFilter> getFilters() {
        return filters;
    }

    public void setFilters(@NotNull List<IEventFilter> filters) {
        this.filters = filters;
    }

    public void addFilter(@NotNull IEventFilter filter) {
        this.filters.add(filter);
    }

    @Override
    public String getName() {
        return Logger.ROOT_LOGGER_NAME;
    }

    @Override
    @NotNull
    public IEventLogger derive(@NotNull String name, @Nullable IEventFilter filter) {
        return new DerivedEventLogger(this, name, filter);
    }

    @Override
    public @NotNull ICommittableEvent event() {
        ICommittableEvent e = new CommittableEvent()
                .timestamp(System.currentTimeMillis())
                .hostname(getHostname())
                .env(getEnv())
                .project(getProject());
        getFilters().forEach(f -> f.handle(e));
        return e;
    }

    private class CommittableEvent extends Event {

        @Override
        public void commit() {
            if (isTopicEnabled(getTopic())) {
                if (getOutputs() != null) {
                    getOutputs().forEach(o -> o.appendEvent(this));
                }
            }
        }

    }

}
