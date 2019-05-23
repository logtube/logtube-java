package io.github.logtube;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IEventProcessor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class Logtube {

    @NotNull
    public static IEventProcessor getProcessor() {
        return LogtubeLoggerFactory.getSingleton().getProcessor();
    }

    @NotNull
    public static IEventLogger getLogger(@NotNull String name) {
        return LogtubeLoggerFactory.getSingleton().getEventLogger(name);
    }

    @NotNull
    public static IEventLogger getLogger(@NotNull Class clazz) {
        return getLogger(clazz.getName());
    }

    @NotNull
    public static IEventLogger getLogger() {
        return getLogger(Logger.ROOT_LOGGER_NAME);
    }

    ////////////////////////// GENERATED CODES /////////////////////////////


    public void trace(String msg) {
        getLogger().trace(msg);
    }

    public void trace(String format, Object arg) {
        getLogger().trace(format, arg);
    }

    public void trace(String format, Object arg1, Object arg2) {
        getLogger().trace(format, arg1, arg2);
    }

    public void trace(String format, Object... argArray) {
        getLogger().trace(format, argArray);
    }

    public void trace(String msg, Throwable t) {
        getLogger().trace(msg, t);
    }

    public void trace(Marker marker, String msg) {
        getLogger().trace(marker, msg);
    }

    public void trace(Marker marker, String format, Object arg) {
        getLogger().trace(marker, format, arg);
    }

    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        getLogger().trace(marker, format, arg1, arg2);
    }

    public void trace(Marker marker, String format, Object... argArray) {
        getLogger().trace(marker, format, argArray);
    }

    public void trace(Marker marker, String msg, Throwable t) {
        getLogger().trace(marker, msg, t);
    }

    public boolean isTraceEnabled() {
        return getLogger().isTraceEnabled();
    }

    public boolean isTraceEnabled(Marker marker) {
        return getLogger().isTraceEnabled();
    }


    public void debug(String msg) {
        getLogger().debug(msg);
    }

    public void debug(String format, Object arg) {
        getLogger().debug(format, arg);
    }

    public void debug(String format, Object arg1, Object arg2) {
        getLogger().debug(format, arg1, arg2);
    }

    public void debug(String format, Object... argArray) {
        getLogger().debug(format, argArray);
    }

    public void debug(String msg, Throwable t) {
        getLogger().debug(msg, t);
    }

    public void debug(Marker marker, String msg) {
        getLogger().debug(marker, msg);
    }

    public void debug(Marker marker, String format, Object arg) {
        getLogger().debug(marker, format, arg);
    }

    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        getLogger().debug(marker, format, arg1, arg2);
    }

    public void debug(Marker marker, String format, Object... argArray) {
        getLogger().debug(marker, format, argArray);
    }

    public void debug(Marker marker, String msg, Throwable t) {
        getLogger().debug(marker, msg, t);
    }

    public boolean isDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    public boolean isDebugEnabled(Marker marker) {
        return getLogger().isDebugEnabled();
    }


    public void info(String msg) {
        getLogger().info(msg);
    }

    public void info(String format, Object arg) {
        getLogger().info(format, arg);
    }

    public void info(String format, Object arg1, Object arg2) {
        getLogger().info(format, arg1, arg2);
    }

    public void info(String format, Object... argArray) {
        getLogger().info(format, argArray);
    }

    public void info(String msg, Throwable t) {
        getLogger().info(msg, t);
    }

    public void info(Marker marker, String msg) {
        getLogger().info(marker, msg);
    }

    public void info(Marker marker, String format, Object arg) {
        getLogger().info(marker, format, arg);
    }

    public void info(Marker marker, String format, Object arg1, Object arg2) {
        getLogger().info(marker, format, arg1, arg2);
    }

    public void info(Marker marker, String format, Object... argArray) {
        getLogger().info(marker, format, argArray);
    }

    public void info(Marker marker, String msg, Throwable t) {
        getLogger().info(marker, msg, t);
    }

    public boolean isInfoEnabled() {
        return getLogger().isInfoEnabled();
    }

    public boolean isInfoEnabled(Marker marker) {
        return getLogger().isInfoEnabled();
    }


    public void warn(String msg) {
        getLogger().warn(msg);
    }

    public void warn(String format, Object arg) {
        getLogger().warn(format, arg);
    }

    public void warn(String format, Object arg1, Object arg2) {
        getLogger().warn(format, arg1, arg2);
    }

    public void warn(String format, Object... argArray) {
        getLogger().warn(format, argArray);
    }

    public void warn(String msg, Throwable t) {
        getLogger().warn(msg, t);
    }

    public void warn(Marker marker, String msg) {
        getLogger().warn(marker, msg);
    }

    public void warn(Marker marker, String format, Object arg) {
        getLogger().warn(marker, format, arg);
    }

    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        getLogger().warn(marker, format, arg1, arg2);
    }

    public void warn(Marker marker, String format, Object... argArray) {
        getLogger().warn(marker, format, argArray);
    }

    public void warn(Marker marker, String msg, Throwable t) {
        getLogger().warn(marker, msg, t);
    }

    public boolean isWarnEnabled() {
        return getLogger().isWarnEnabled();
    }

    public boolean isWarnEnabled(Marker marker) {
        return getLogger().isWarnEnabled();
    }


    public void error(String msg) {
        getLogger().error(msg);
    }

    public void error(String format, Object arg) {
        getLogger().error(format, arg);
    }

    public void error(String format, Object arg1, Object arg2) {
        getLogger().error(format, arg1, arg2);
    }

    public void error(String format, Object... argArray) {
        getLogger().error(format, argArray);
    }

    public void error(String msg, Throwable t) {
        getLogger().error(msg, t);
    }

    public void error(Marker marker, String msg) {
        getLogger().error(marker, msg);
    }

    public void error(Marker marker, String format, Object arg) {
        getLogger().error(marker, format, arg);
    }

    public void error(Marker marker, String format, Object arg1, Object arg2) {
        getLogger().error(marker, format, arg1, arg2);
    }

    public void error(Marker marker, String format, Object... argArray) {
        getLogger().error(marker, format, argArray);
    }

    public void error(Marker marker, String msg, Throwable t) {
        getLogger().error(marker, msg, t);
    }

    public boolean isErrorEnabled() {
        return getLogger().isErrorEnabled();
    }

    public boolean isErrorEnabled(Marker marker) {
        return getLogger().isErrorEnabled();
    }

    public IEventLogger keyword(@NotNull String... keywords) {
        return getLogger().keyword(keywords);
    }

    public IEventLogger withK(@NotNull String... keywords) {
        return keyword(keywords);
    }

}
