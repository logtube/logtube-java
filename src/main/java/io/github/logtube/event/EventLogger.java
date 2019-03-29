package io.github.logtube.event;

import org.jetbrains.annotations.NotNull;

public class EventLogger extends BaseEventLogger {

    private final String hostname;

    private final String project;

    private final String environment;

    public EventLogger(@NotNull String hostname, @NotNull String project, @NotNull String environment) {
        this.hostname = hostname;
        this.project = project;
        this.environment = environment;
    }

    @Override
    @NotNull
    public String getHostname() {
        return this.hostname;
    }

    @Override
    @NotNull
    public String getProject() {
        return this.project;
    }

    @Override
    @NotNull
    public String getEnv() {
        return this.environment;
    }

}
