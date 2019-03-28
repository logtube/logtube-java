package io.github.logtube.logger;

import org.jetbrains.annotations.NotNull;

public class Logger extends BaseLogger {

    private final String hostname;

    private final String project;

    private final String environment;

    public Logger(@NotNull String hostname, @NotNull String project, @NotNull String environment) {
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
    public String getEnvironment() {
        return this.environment;
    }

}
