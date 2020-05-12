package io.github.logtube;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IEventProcessor;
import io.github.logtube.core.IEventProcessorFactory;
import io.github.logtube.core.loggers.EventLogger;
import io.github.logtube.core.outputs.*;
import io.github.logtube.core.processors.EventProcessor;
import io.github.logtube.core.processors.NOPProcessor;
import io.github.logtube.redis.RedisTrackEventCommitter;
import io.github.logtube.utils.ILifeCycle;
import io.github.logtube.utils.ITopicAware;
import io.github.logtube.utils.ITopicMutableAware;
import io.github.logtube.utils.TopicAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LogtubeLoggerFactory implements ILoggerFactory, IEventProcessorFactory, ILifeCycle {

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
    private IEventProcessor processor = NOPProcessor.getSingleton();

    @NotNull
    private LogtubeOptions options = LogtubeOptions.getDefault();

    @NotNull
    private ITopicMutableAware rootTopics = new TopicAware();

    private Map<String, ITopicAware> customTopics = new HashMap<>();

    private final ConcurrentMap<String, IEventLogger> loggers = new ConcurrentHashMap<>();

    private boolean isStarted = false;

    private LogtubeLoggerFactory() {
    }

    @NotNull
    private ITopicAware findTopicAware(@NotNull String name) {
        if (name.equals(Logger.ROOT_LOGGER_NAME)) {
            return this.rootTopics;
        }
        String found = null;
        ITopicAware topics = null;
        for (Map.Entry<String, ITopicAware> entry : this.customTopics.entrySet()) {
            String prefix = entry.getKey();
            ITopicAware value = entry.getValue();
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
        if (topics == null) {
            return this.rootTopics;
        }
        return topics;
    }

    @Override
    @NotNull
    public IEventProcessor getProcessor() {
        return this.processor;
    }

    @NotNull
    public LogtubeOptions getOptions() {
        return options;
    }

    @NotNull
    public IEventLogger getEventLogger(@Nullable String name) {
        if (name == null) {
            name = Logger.ROOT_LOGGER_NAME;
        }
        IEventLogger logger = this.loggers.get(name);
        if (logger != null) {
            return logger;
        } else {
            IEventLogger newInstance = new EventLogger(this, name, findTopicAware(name));
            IEventLogger oldInstance = this.loggers.putIfAbsent(name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }

    @Override
    public Logger getLogger(String name) {
        return getEventLogger(name);
    }


    public synchronized void start() {
        if (this.isStarted) throw new RuntimeException("already started");

        LogtubeOptions options = LogtubeOptions.fromClasspath();

        // setup topics
        this.rootTopics.setTopics(options.getTopics());

        options.getCustomTopics().forEach((k, v) -> {
            TopicAware topicAware = new TopicAware();
            topicAware.setTopics(v);
            this.customTopics.put(k, topicAware);
        });

        // setup processor
        EventProcessor processor = new EventProcessor();

        processor.setHostname(LogtubeOptions.getHostname());
        processor.setProject(options.getProject());
        processor.setEnv(options.getEnv());
        processor.setTopicMappings(options.getTopicMappings());

        if (options.getConsoleEnabled()) {
            EventConsoleOutput output = new EventConsoleOutput();
            output.setTopics(options.getConsoleTopics());
            processor.addOutput(output);
        }

        if (options.getFileEnabled()) {
            EventFileOutput output = new EventFileOutput(
                    options.getFileDir(),
                    options.getFileSubdirMappings(),
                    options.getFileSignal()
            );
            output.setTopics(options.getFileTopics());
            processor.addOutput(output);
        } else {
            if (options.getFilePlainEnabled()) {
                EventPlainFileOutput output = new EventPlainFileOutput(
                        options.getFilePlainDir(),
                        options.getFilePlainSubdirMappings(),
                        options.getFilePlainSignal()
                );
                output.setTopics(options.getFilePlainTopics());
                processor.addOutput(output);
            }

            if (options.getFileJSONEnabled()) {
                EventJSONFileOutput output = new EventJSONFileOutput(
                        options.getFileJSONDir(),
                        options.getFileJSONSubdirMappings(),
                        options.getFileJSONSignal()
                );
                output.setTopics(options.getFileJSONTopics());
                processor.addOutput(output);
            }
        }

        if (options.getRemoteEnabled()) {
            EventRemoteOutput output = new EventRemoteOutput(
                    options.getRemoteHosts()
            );
            output.setTopics(options.getRemoteTopics());
            processor.addOutput(output);
        }
        if (options.getRedisEnabled()) {
            EventRedisOutput output = new EventRedisOutput(
                    options.getRedisHosts(),
                    options.getRedisKey()
            );
            output.setTopics(options.getRedisTopics());
            processor.addOutput(output);
        }

        processor.start();

        // configure component
        RedisTrackEventCommitter.setMinDuration(options.getRedisMinDuration());
        RedisTrackEventCommitter.setMinResultSize(options.getRedisMinResultSize());

        this.processor = processor;
        this.options = options;
        this.isStarted = true;
    }

    @Override
    public synchronized void stop() {
        if (!this.isStarted) throw new RuntimeException("not started");
        this.isStarted = false;

        // clear topics
        this.rootTopics = new TopicAware();
        this.customTopics = new HashMap<>();

        // switch root logger
        IEventProcessor processor = this.processor;
        this.processor = NOPProcessor.getSingleton();

        // switch options
        this.options = LogtubeOptions.getDefault();

        processor.stop();
    }

}
