package io.github.logtube.core;

import io.github.logtube.core.events.NOPEvent;
import io.github.logtube.utils.ITopicAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * Logtube 暴露的主要接口，同时实现 slf4j 的 Logger 接口，包含大量糖方法
 */
public interface IEventLogger extends ITopicAware, Logger {

    /**
     * 派生日志器，用于添加默认的关键词等
     *
     * @param middleware 中间件
     * @return 新的日志器，或者自己
     */
    @NotNull IEventLogger derive(@NotNull IEventMiddleware middleware);

    /**
     * 创建一个事件，使用该主题，因为一个事件必须有主题，因此，此方法为创建事件的唯一方法
     *
     * @param topic 主题
     * @return 可修改，可提交的事件
     */
    @NotNull IMutableEvent topic(@NotNull String topic);

    //////////////////////// default methods //////////////////////////////

    default @NotNull IEventLogger keyword(@Nullable Object... keywords) {
        return derive(e -> e.keyword(keywords));
    }

    /**
     * @param keywords 关键字
     * @return 带关键字的 Logger
     * @see IEventLogger#keyword(Object...)
     */
    @Deprecated
    default @NotNull IEventLogger withK(@Nullable Object... keywords) {
        return keyword(keywords);
    }

    //////////////////////// 与 slf4j 兼容的代码 /////////////////////////////

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
        topic(topic).message(msg).xException(t).commit();
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


    default void fatal(String msg) {
        message("fatal", msg);
    }

    default void fatal(String format, Object arg) {
        message("fatal", format, arg);
    }

    default void fatal(String format, Object arg1, Object arg2) {
        message("fatal", format, arg1, arg2);
    }

    default void fatal(String format, Object... arguments) {
        message("fatal", format, arguments);
    }

    default void fatal(String msg, Throwable t) {
        message("fatal", msg, t);
    }

    default void fatal(Marker marker, String msg) {
        fatal(msg);
    }

    default void fatal(Marker marker, String format, Object arg) {
        fatal(format, arg);
    }

    default void fatal(Marker marker, String format, Object arg1, Object arg2) {
        fatal(format, arg1, arg2);
    }

    default void fatal(Marker marker, String format, Object... argArray) {
        fatal(format, argArray);
    }

    default void fatal(Marker marker, String msg, Throwable t) {
        fatal(msg, t);
    }

    default IMutableEvent trace() {
        if (isTraceEnabled()) {
            return topic("trace");
        }
        return NOPEvent.getSingleton();
    }

    default IMutableEvent debug() {
        if (isDebugEnabled()) {
            return topic("debug");
        }
        return NOPEvent.getSingleton();
    }

    default IMutableEvent info() {
        if (isInfoEnabled()) {
            return topic("info");
        }
        return NOPEvent.getSingleton();
    }

    default IMutableEvent warn() {
        if (isWarnEnabled()) {
            return topic("warn");
        }
        return NOPEvent.getSingleton();
    }

    default IMutableEvent error() {
        if (isErrorEnabled()) {
            return topic("error");
        }
        return NOPEvent.getSingleton();
    }

    default IMutableEvent fatal() {
        if (isFatalEnabled()) {
            return topic("fatal");
        }
        return NOPEvent.getSingleton();
    }

}
