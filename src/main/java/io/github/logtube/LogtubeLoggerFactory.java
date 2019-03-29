package io.github.logtube;

import io.github.logtube.logger.EventLogger;
import io.github.logtube.outputs.EventConsoleOutput;
import io.github.logtube.outputs.EventJSONFileOutput;
import io.github.logtube.outputs.EventPlainFileOutput;
import io.github.logtube.outputs.EventRemoteOutput;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LogtubeLoggerFactory implements ILoggerFactory {

    private static final LogtubeLoggerFactory SINGLETON = new LogtubeLoggerFactory();

    public static LogtubeLoggerFactory getSingleton() {
        return SINGLETON;
    }

    private final ConcurrentMap<String, IEventLogger> derivedLoggers = new ConcurrentHashMap<>();

    private final EventLogger rootLogger;

    private LogtubeLoggerFactory() {
        LogtubeOptions options = LogtubeOptions.fromClasspath();

        this.rootLogger = new EventLogger();
        this.rootLogger.setHostname(LogtubeOptions.getHostname());
        this.rootLogger.setProject(options.getProject());
        this.rootLogger.setEnv(options.getEnv());
        this.rootLogger.setTopics(options.getTopics());

        if (options.getConsoleEnabled()) {
            EventConsoleOutput output = new EventConsoleOutput();
            output.setTopics(options.getConsoleTopics());
            this.rootLogger.addOutput(output);
        }

        if (options.getFilePlainEnabled()) {
            EventPlainFileOutput output = new EventPlainFileOutput(
                    options.getFilePlainDir(),
                    options.getFilePlainSignal()
            );
            output.setTopics(options.getFilePlainTopics());
            this.rootLogger.addOutput(output);
        }

        if (options.getFileJSONEnabled()) {
            EventJSONFileOutput output = new EventJSONFileOutput(
                    options.getFileJSONDir(),
                    options.getFileJSONSignal()
            );
            output.setTopics(options.getFileJSONTopics());
            this.rootLogger.addOutput(output);
        }

        if (options.getRemoteEnabled()) {
            EventRemoteOutput output = new EventRemoteOutput(
                    options.getRemoteHosts()
            );
            output.setTopics(options.getRemoteTopics());
            this.rootLogger.addOutput(output);
        }
    }

    public EventLogger getRootLogger() {
        return this.rootLogger;
    }

    @Override
    public Logger getLogger(String name) {
        IEventLogger logger = this.derivedLoggers.get(name);
        if (logger != null) {
            return logger;
        } else {
            IEventLogger newInstance = this.rootLogger.derive(name, null);
            IEventLogger oldInstance = this.derivedLoggers.putIfAbsent(name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }

}
