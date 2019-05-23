package io.github.logtube;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.ILifeCycle;
import io.github.logtube.core.IRootEventLogger;
import io.github.logtube.core.logger.ChildLogger;
import io.github.logtube.core.logger.NOPLogger;
import io.github.logtube.core.logger.RootLogger;
import io.github.logtube.core.outputs.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LogtubeLoggerFactory implements ILoggerFactory, ILifeCycle {

    ///////////////////////  SINGLETON ////////////////////////////

    private static final LogtubeLoggerFactory SINGLETON;

    static {
        SINGLETON = new LogtubeLoggerFactory();
        SINGLETON.start();
    }

    public static LogtubeLoggerFactory getSingleton() {
        return SINGLETON;
    }

    ///////////////////////  LOGGERS ////////////////////////

    @NotNull
    private IRootEventLogger rootLogger = NOPLogger.getSingleton();

    private final ConcurrentMap<String, IEventLogger> childLoggers = new ConcurrentHashMap<>();

    private Map<String, Set<String>> customTopics = new HashMap<>();

    private LogtubeLoggerFactory() {
    }

    @Nullable
    private Set<String> findTopics(@NotNull String name) {
        String found = null;
        Set<String> topics = null;
        for (Map.Entry<String, Set<String>> entry : this.customTopics.entrySet()) {
            String prefix = entry.getKey();
            Set<String> value = entry.getValue();
            if (name.toLowerCase().startsWith(prefix)) {
                if (found == null) {
                    found = prefix;
                    topics = value;
                } else if (prefix.length() > found.length()) {
                    found = prefix;
                    topics = value;
                }
            }
        }
        return topics;
    }

    @NotNull
    public IRootEventLogger getRootLogger() {
        return this.rootLogger;
    }

    @NotNull
    public IEventLogger getChildLogger(@Nullable String name) {
        if (name == null) {
            return getRootLogger();
        }
        IEventLogger logger = this.childLoggers.get(name);
        if (logger != null) {
            return logger;
        } else {
            IEventLogger newInstance = new ChildLogger(getRootLogger(), name, findTopics(name));
            IEventLogger oldInstance = this.childLoggers.putIfAbsent(name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }

    @Override
    public Logger getLogger(String name) {
        return getChildLogger(name);
    }

    private boolean isStarted = false;

    public synchronized void start() {
        if (this.isStarted) throw new RuntimeException("already started");
        this.isStarted = true;

        LogtubeOptions options = LogtubeOptions.fromClasspath();

        this.customTopics = options.getCustomTopics();

        RootLogger rootLogger = new RootLogger();

        rootLogger.setHostname(LogtubeOptions.getHostname());
        rootLogger.setProject(options.getProject());
        rootLogger.setEnv(options.getEnv());
        rootLogger.setTopics(options.getTopics());
        rootLogger.setTopicMappings(options.getTopicMappings());

        if (options.getConsoleEnabled()) {
            EventConsoleOutput output = new EventConsoleOutput();
            output.setTopics(options.getConsoleTopics());
            rootLogger.addOutput(output);
        }

        if (options.getFilePlainEnabled()) {
            EventPlainFileOutput output = new EventPlainFileOutput(
                    options.getFilePlainDir(),
                    options.getFilePlainSignal()
            );
            output.setTopics(options.getFilePlainTopics());
            rootLogger.addOutput(output);
        }

        if (options.getFileJSONEnabled()) {
            EventJSONFileOutput output = new EventJSONFileOutput(
                    options.getFileJSONDir(),
                    options.getFileJSONSignal()
            );
            output.setTopics(options.getFileJSONTopics());
            rootLogger.addOutput(output);
        }

        if (options.getRemoteEnabled()) {
            EventRemoteOutput output = new EventRemoteOutput(
                    options.getRemoteHosts()
            );
            output.setTopics(options.getRemoteTopics());
            rootLogger.addOutput(output);
        }
        if (options.getRedisEnabled()) {
            EventRedisOutput output = new EventRedisOutput(
                    options.getRedisHosts(),
                    options.getRedisKey()
            );
            output.setTopics(options.getRedisTopics());
            rootLogger.addOutput(output);
        }

        rootLogger.start();

        this.rootLogger = rootLogger;
    }

    @Override
    public synchronized void stop() {
        if (!this.isStarted) throw new RuntimeException("not started");
        this.isStarted = false;

        // switch root logger
        IRootEventLogger rootLogger = this.rootLogger;
        this.rootLogger = NOPLogger.getSingleton();

        rootLogger.stop();
    }

}
