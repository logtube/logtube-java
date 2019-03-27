package io.github.logtube.classic;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClassicLoggerFactory implements ILoggerFactory {

    private final ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    @Override
    public Logger getLogger(String name) {
        Logger simpleLogger = loggerMap.get(name);
        if (simpleLogger != null) {
            return simpleLogger;
        } else {
            Logger newInstance = new ClassicLogger(name);
            Logger oldInstance = loggerMap.putIfAbsent(name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }

    void reset() {
        loggerMap.clear();
    }

}
