package org.slf4j.impl;

import io.github.logtube.LogtubeLoggerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {

    private final LogtubeLoggerFactory loggerFactory;

    private static final String loggerFactoryClassStr = LogtubeLoggerFactory.class.getName();

    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    // to avoid constant folding by the compiler, this field must *not* be final
    public static String REQUESTED_API_VERSION = "1.7.26"; // !final

    public StaticLoggerBinder() {
        loggerFactory = LogtubeLoggerFactory.getSingleton();
    }

    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return loggerFactoryClassStr;
    }

}
