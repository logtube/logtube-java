package io.github.logtube;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;

public interface IEventLogger extends ITopicAware, Logger {

    @NotNull
    IEventLogger derive(@Nullable String name, @Nullable IEventFilter filter);

    @NotNull
    IMutableEvent topic(@NotNull String topic);

    @NotNull
    default IEventLogger derive(@Nullable IEventFilter filter) {
        return derive(null, filter);
    }

    @NotNull
    default IEventLogger derive(@Nullable String name) {
        return derive(name, null);
    }

    @NotNull
    default IEventLogger keyword(@NotNull String... keywords) {
        return derive(getName(), e -> e.keyword(keywords));
    }

    default void message(@NotNull String topic, @NotNull String msg) {
        if (!isTopicEnabled(topic)) {
            return;
        }
        _message(topic, msg, null);
    }

    default void message(@NotNull String topic, @NotNull String format, @Nullable Object arg) {
        if (!isTopicEnabled(topic)) {
            return;
        }
        message(topic, format, arg, null);
    }

    default void message(@NotNull String topic, @NotNull String format, @Nullable Object arg1, @Nullable Object arg2) {
        if (!isTopicEnabled(topic)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        message(topic, tp.getMessage(), tp.getThrowable());
    }

    default void message(@NotNull String topic, @NotNull String format, @NotNull Object... arguments) {
        if (!isTopicEnabled(topic)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
        message(topic, tp.getMessage(), tp.getThrowable());
    }

    default void message(@NotNull String topic, @NotNull String msg, @Nullable Throwable t) {
        if (!isTopicEnabled(topic)) {
            return;
        }
        _message(topic, msg, t);
    }

    default void _message(@NotNull String topic, @NotNull String msg, @Nullable Throwable t) {
        StringWriter buf = new StringWriter();
        buf.append('[');
        buf.append(Thread.currentThread().getName());
        buf.append("] ");
        buf.append(String.valueOf(getName())).append(" - ");
        buf.append(msg);
        if (t != null) {
            buf.append("\r\n");
            t.printStackTrace(new PrintWriter(buf));
        }
        topic(topic).message(buf.toString()).commit();
    }

    default IMutableEvent trace() {
        return topic("trace");
    }

    default IMutableEvent debug() {
        return topic("debug");
    }

    default IMutableEvent info() {
        return topic("info");
    }

    default IMutableEvent warn() {
        return topic("warn");
    }

    default IMutableEvent error() {
        return topic("error");
    }

    @Override
    default boolean isTraceEnabled() {
        return isTopicEnabled("trace");
    }

    @Override
    default boolean isDebugEnabled() {
        return isTopicEnabled("debug");
    }

    @Override
    default boolean isInfoEnabled() {
        return isTopicEnabled("debug");
    }

    @Override
    default boolean isWarnEnabled() {
        return isTopicEnabled("warn");
    }

    @Override
    default boolean isErrorEnabled() {
        return isTopicEnabled("error");
    }

    @Override
    default boolean isTraceEnabled(Marker marker) {
        return isTraceEnabled();
    }

    @Override
    default boolean isDebugEnabled(Marker marker) {
        return isDebugEnabled();
    }

    @Override
    default boolean isInfoEnabled(Marker marker) {
        return isInfoEnabled();
    }

    @Override
    default boolean isWarnEnabled(Marker marker) {
        return isWarnEnabled();
    }

    @Override
    default boolean isErrorEnabled(Marker marker) {
        return isErrorEnabled();
    }


    //////////////////////// GENERATED /////////////////////////////


    @Override
    default void trace(String msg) {
        message("trace", msg);
    }

    @Override
    default void trace(String format, Object arg) {
        message("trace", format, arg);
    }

    @Override
    default void trace(String format, Object arg1, Object arg2) {
        message("trace", format, arg1, arg2);
    }

    @Override
    default void trace(String format, Object... arguments) {
        message("trace", format, arguments);
    }

    @Override
    default void trace(String msg, Throwable t) {
        message("trace", msg, t);
    }

    @Override
    default void trace(Marker marker, String msg) {
        trace(msg);
    }

    @Override
    default void trace(Marker marker, String format, Object arg) {
        trace(format, arg);
    }

    @Override
    default void trace(Marker marker, String format, Object arg1, Object arg2) {
        trace(format, arg1, arg2);
    }

    @Override
    default void trace(Marker marker, String format, Object... argArray) {
        trace(format, argArray);
    }

    @Override
    default void trace(Marker marker, String msg, Throwable t) {
        trace(msg, t);
    }


    @Override
    default void debug(String msg) {
        message("debug", msg);
    }

    @Override
    default void debug(String format, Object arg) {
        message("debug", format, arg);
    }

    @Override
    default void debug(String format, Object arg1, Object arg2) {
        message("debug", format, arg1, arg2);
    }

    @Override
    default void debug(String format, Object... arguments) {
        message("debug", format, arguments);
    }

    @Override
    default void debug(String msg, Throwable t) {
        message("debug", msg, t);
    }

    @Override
    default void debug(Marker marker, String msg) {
        debug(msg);
    }

    @Override
    default void debug(Marker marker, String format, Object arg) {
        debug(format, arg);
    }

    @Override
    default void debug(Marker marker, String format, Object arg1, Object arg2) {
        debug(format, arg1, arg2);
    }

    @Override
    default void debug(Marker marker, String format, Object... argArray) {
        debug(format, argArray);
    }

    @Override
    default void debug(Marker marker, String msg, Throwable t) {
        debug(msg, t);
    }


    @Override
    default void info(String msg) {
        message("info", msg);
    }

    @Override
    default void info(String format, Object arg) {
        message("info", format, arg);
    }

    @Override
    default void info(String format, Object arg1, Object arg2) {
        message("info", format, arg1, arg2);
    }

    @Override
    default void info(String format, Object... arguments) {
        message("info", format, arguments);
    }

    @Override
    default void info(String msg, Throwable t) {
        message("info", msg, t);
    }

    @Override
    default void info(Marker marker, String msg) {
        info(msg);
    }

    @Override
    default void info(Marker marker, String format, Object arg) {
        info(format, arg);
    }

    @Override
    default void info(Marker marker, String format, Object arg1, Object arg2) {
        info(format, arg1, arg2);
    }

    @Override
    default void info(Marker marker, String format, Object... argArray) {
        info(format, argArray);
    }

    @Override
    default void info(Marker marker, String msg, Throwable t) {
        info(msg, t);
    }


    @Override
    default void warn(String msg) {
        message("warn", msg);
    }

    @Override
    default void warn(String format, Object arg) {
        message("warn", format, arg);
    }

    @Override
    default void warn(String format, Object arg1, Object arg2) {
        message("warn", format, arg1, arg2);
    }

    @Override
    default void warn(String format, Object... arguments) {
        message("warn", format, arguments);
    }

    @Override
    default void warn(String msg, Throwable t) {
        message("warn", msg, t);
    }

    @Override
    default void warn(Marker marker, String msg) {
        warn(msg);
    }

    @Override
    default void warn(Marker marker, String format, Object arg) {
        warn(format, arg);
    }

    @Override
    default void warn(Marker marker, String format, Object arg1, Object arg2) {
        warn(format, arg1, arg2);
    }

    @Override
    default void warn(Marker marker, String format, Object... argArray) {
        warn(format, argArray);
    }

    @Override
    default void warn(Marker marker, String msg, Throwable t) {
        warn(msg, t);
    }


    @Override
    default void error(String msg) {
        message("error", msg);
    }

    @Override
    default void error(String format, Object arg) {
        message("error", format, arg);
    }

    @Override
    default void error(String format, Object arg1, Object arg2) {
        message("error", format, arg1, arg2);
    }

    @Override
    default void error(String format, Object... arguments) {
        message("error", format, arguments);
    }

    @Override
    default void error(String msg, Throwable t) {
        message("error", msg, t);
    }

    @Override
    default void error(Marker marker, String msg) {
        error(msg);
    }

    @Override
    default void error(Marker marker, String format, Object arg) {
        error(format, arg);
    }

    @Override
    default void error(Marker marker, String format, Object arg1, Object arg2) {
        error(format, arg1, arg2);
    }

    @Override
    default void error(Marker marker, String format, Object... argArray) {
        error(format, argArray);
    }

    @Override
    default void error(Marker marker, String msg, Throwable t) {
        error(msg, t);
    }


}
