package net.landzero.xlog.logger;

import net.landzero.xlog.XLogger;
import net.landzero.xlog.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class XLoggerImpl implements XLogger {

    private final Logger logger;

    public XLoggerImpl(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz.getName());
    }

    @Override
    public Logger withK(Object... keywords) {
        return new LoggerWrapper(keywords);
    }

    private class LoggerWrapper implements Logger {

        private final String keywordMark;

        LoggerWrapper(Object... keywords) {
            this.keywordMark = Strings.keyword(keywords);
        }

        private Object[] arrayPrepend(Object[] objects, Object o) {
            Object[] newObjects = new Object[objects.length + 1];
            newObjects[0] = o;
            System.arraycopy(objects, 0, newObjects, 1, objects.length);
            return newObjects;
        }

        @Override
        public String getName() {
            return logger.getName();
        }

        @Override
        public boolean isTraceEnabled() {
            return logger.isTraceEnabled();
        }

        @Override
        public void trace(String s) {
            logger.trace(" " + keywordMark + " " + Strings.safe(s));
        }

        @Override
        public void trace(String s, Object o) {
            logger.trace(" {} " + Strings.safe(s), keywordMark, o);
        }

        @Override
        public void trace(String s, Object o, Object o1) {
            logger.trace(" {} " + Strings.safe(s), keywordMark, o, o1);
        }

        @Override
        public void trace(String s, Object... objects) {
            logger.trace(" {} " + Strings.safe(s), arrayPrepend(objects, keywordMark));
        }

        @Override
        public void trace(String s, Throwable throwable) {
            logger.trace(" " + keywordMark + " " + Strings.safe(s), throwable);
        }

        @Override
        public boolean isTraceEnabled(Marker marker) {
            return logger.isTraceEnabled(marker);
        }

        @Override
        public void trace(Marker marker, String s) {
            logger.trace(marker, " " + keywordMark + " " + Strings.safe(s));
        }

        @Override
        public void trace(Marker marker, String s, Object o) {
            logger.trace(marker, " {} " + Strings.safe(s), keywordMark, o);
        }

        @Override
        public void trace(Marker marker, String s, Object o, Object o1) {
            logger.trace(marker, " {} " + Strings.safe(s), keywordMark, o, o1);
        }

        @Override
        public void trace(Marker marker, String s, Object... objects) {
            logger.trace(marker, " {} " + Strings.safe(s), arrayPrepend(objects, keywordMark));
        }

        @Override
        public void trace(Marker marker, String s, Throwable throwable) {
            logger.trace(marker, " " + keywordMark + " " + Strings.safe(s), throwable);
        }

        @Override
        public boolean isDebugEnabled() {
            return logger.isDebugEnabled();
        }

        @Override
        public void debug(String s) {
            logger.debug(" " + keywordMark + " " + Strings.safe(s));
        }

        @Override
        public void debug(String s, Object o) {
            logger.debug(" {} " + Strings.safe(s), keywordMark, o);
        }

        @Override
        public void debug(String s, Object o, Object o1) {
            logger.debug(" {} " + Strings.safe(s), keywordMark, o, o1);
        }

        @Override
        public void debug(String s, Object... objects) {
            logger.debug(" {} " + Strings.safe(s), arrayPrepend(objects, keywordMark));
        }

        @Override
        public void debug(String s, Throwable throwable) {
            logger.debug(" " + keywordMark + " " + Strings.safe(s), throwable);
        }

        @Override
        public boolean isDebugEnabled(Marker marker) {
            return logger.isDebugEnabled(marker);
        }

        @Override
        public void debug(Marker marker, String s) {
            logger.debug(marker, " " + keywordMark + " " + Strings.safe(s));
        }

        @Override
        public void debug(Marker marker, String s, Object o) {
            logger.debug(marker, " {} " + Strings.safe(s), keywordMark, o);
        }

        @Override
        public void debug(Marker marker, String s, Object o, Object o1) {
            logger.debug(marker, " {} " + Strings.safe(s), keywordMark, o, o1);
        }

        @Override
        public void debug(Marker marker, String s, Object... objects) {
            logger.debug(marker, " {} " + Strings.safe(s), arrayPrepend(objects, keywordMark));
        }

        @Override
        public void debug(Marker marker, String s, Throwable throwable) {
            logger.debug(marker, " " + keywordMark + " " + Strings.safe(s), throwable);
        }


        @Override
        public boolean isInfoEnabled() {
            return logger.isInfoEnabled();
        }

        @Override
        public void info(String s) {
            logger.info(" " + keywordMark + " " + Strings.safe(s));
        }

        @Override
        public void info(String s, Object o) {
            logger.info(" {} " + Strings.safe(s), keywordMark, o);
        }

        @Override
        public void info(String s, Object o, Object o1) {
            logger.info(" {} " + Strings.safe(s), keywordMark, o, o1);
        }

        @Override
        public void info(String s, Object... objects) {
            logger.info(" {} " + Strings.safe(s), arrayPrepend(objects, keywordMark));
        }

        @Override
        public void info(String s, Throwable throwable) {
            logger.info(" " + keywordMark + " " + Strings.safe(s), throwable);
        }

        @Override
        public boolean isInfoEnabled(Marker marker) {
            return logger.isInfoEnabled(marker);
        }

        @Override
        public void info(Marker marker, String s) {
            logger.info(marker, " " + keywordMark + " " + Strings.safe(s));
        }

        @Override
        public void info(Marker marker, String s, Object o) {
            logger.info(marker, " {} " + Strings.safe(s), keywordMark, o);
        }

        @Override
        public void info(Marker marker, String s, Object o, Object o1) {
            logger.info(marker, " {} " + Strings.safe(s), keywordMark, o, o1);
        }

        @Override
        public void info(Marker marker, String s, Object... objects) {
            logger.info(marker, " {} " + Strings.safe(s), arrayPrepend(objects, keywordMark));
        }

        @Override
        public void info(Marker marker, String s, Throwable throwable) {
            logger.info(marker, " " + keywordMark + " " + Strings.safe(s), throwable);
        }

        @Override
        public boolean isWarnEnabled() {
            return logger.isWarnEnabled();
        }

        @Override
        public void warn(String s) {
            logger.warn(" " + keywordMark + " " + Strings.safe(s));
        }

        @Override
        public void warn(String s, Object o) {
            logger.warn(" {} " + Strings.safe(s), keywordMark, o);
        }

        @Override
        public void warn(String s, Object o, Object o1) {
            logger.warn(" {} " + Strings.safe(s), keywordMark, o, o1);
        }

        @Override
        public void warn(String s, Object... objects) {
            logger.warn(" {} " + Strings.safe(s), arrayPrepend(objects, keywordMark));
        }

        @Override
        public void warn(String s, Throwable throwable) {
            logger.warn(" " + keywordMark + " " + Strings.safe(s), throwable);
        }

        @Override
        public boolean isWarnEnabled(Marker marker) {
            return logger.isWarnEnabled(marker);
        }

        @Override
        public void warn(Marker marker, String s) {
            logger.warn(marker, " " + keywordMark + " " + Strings.safe(s));
        }

        @Override
        public void warn(Marker marker, String s, Object o) {
            logger.warn(marker, " {} " + Strings.safe(s), keywordMark, o);
        }

        @Override
        public void warn(Marker marker, String s, Object o, Object o1) {
            logger.warn(marker, " {} " + Strings.safe(s), keywordMark, o, o1);
        }

        @Override
        public void warn(Marker marker, String s, Object... objects) {
            logger.warn(marker, " {} " + Strings.safe(s), arrayPrepend(objects, keywordMark));
        }

        @Override
        public void warn(Marker marker, String s, Throwable throwable) {
            logger.warn(marker, " " + keywordMark + " " + Strings.safe(s), throwable);
        }

        @Override
        public boolean isErrorEnabled() {
            return logger.isErrorEnabled();
        }

        @Override
        public void error(String s) {
            logger.error(" " + keywordMark + " " + Strings.safe(s));
        }

        @Override
        public void error(String s, Object o) {
            logger.error(" {} " + Strings.safe(s), keywordMark, o);
        }

        @Override
        public void error(String s, Object o, Object o1) {
            logger.error(" {} " + Strings.safe(s), keywordMark, o, o1);
        }

        @Override
        public void error(String s, Object... objects) {
            logger.error(" {} " + Strings.safe(s), arrayPrepend(objects, keywordMark));
        }

        @Override
        public void error(String s, Throwable throwable) {
            logger.error(" " + keywordMark + " " + Strings.safe(s), throwable);
        }

        @Override
        public boolean isErrorEnabled(Marker marker) {
            return logger.isErrorEnabled(marker);
        }

        @Override
        public void error(Marker marker, String s) {
            logger.error(marker, " " + keywordMark + " " + Strings.safe(s));
        }

        @Override
        public void error(Marker marker, String s, Object o) {
            logger.error(marker, " {} " + Strings.safe(s), keywordMark, o);
        }

        @Override
        public void error(Marker marker, String s, Object o, Object o1) {
            logger.error(marker, " {} " + Strings.safe(s), keywordMark, o, o1);
        }

        @Override
        public void error(Marker marker, String s, Object... objects) {
            logger.error(marker, " {} " + Strings.safe(s), arrayPrepend(objects, keywordMark));
        }

        @Override
        public void error(Marker marker, String s, Throwable throwable) {
            logger.error(marker, " " + keywordMark + " " + Strings.safe(s), throwable);
        }


    }

}
