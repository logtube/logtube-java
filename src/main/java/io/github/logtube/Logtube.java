package io.github.logtube;

import io.github.logtube.core.IEventContext;
import io.github.logtube.core.IEventLogger;
import io.github.logtube.core.IEventProcessor;
import io.github.logtube.core.IMutableEvent;
import io.github.logtube.utils.Reflections;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class Logtube {

    @NotNull
    public static IEventContext captureContext() {
        return getProcessor().captureContext();
    }

    @NotNull
    public static IEventProcessor getProcessor() {
        return LogtubeLoggerFactory.getSingleton().getProcessor();
    }

    @NotNull
    public static IEventLogger getLogger(@NotNull String name) {
        return LogtubeLoggerFactory.getSingleton().getEventLogger(name);
    }

    @NotNull
    public static IEventLogger getLogger(@NotNull Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    @NotNull
    public static IEventLogger getLogger() {
        String className = Logger.ROOT_LOGGER_NAME;
        StackTraceElement element = Reflections.getStackTraceElement();
        if (element != null) {
            className = element.getClassName();
        }
        return getLogger(className);
    }

    @NotNull
    public static IMutableEvent topic(@NotNull String topic) {
        return getLogger().topic(topic);
    }

    @NotNull
    public static IEventLogger keyword(@Nullable Object... keywords) {
        return getLogger().keyword(keywords);
    }

    /**
     * @param keywords 关键字
     * @return 一个 Logger 带关键词
     * @see Logtube#keyword(Object...)
     */
    @Deprecated
    @NotNull
    public static IEventLogger withK(@Nullable Object... keywords) {
        return keyword(keywords);
    }

    /**
     * 生成 K[] 字符串，用以添加到消息内容
     *
     * @param keywords 关键字
     * @return 格式化的关键字
     * @see Logtube#keyword(Object...)
     * @deprecated 使用 Logtube.keyword().info() 这样的写法
     */
    @Deprecated
    @NotNull
    public static String K(@Nullable Object... keywords) {
        return Strings.keyword(keywords);
    }

    ////////////////////////// GENERATED CODES /////////////////////////////


    public static void trace(String msg) {
        getLogger().trace(msg);
    }

    public static void trace(String format, Object arg) {
        getLogger().trace(format, arg);
    }

    public static void trace(String format, Object arg1, Object arg2) {
        getLogger().trace(format, arg1, arg2);
    }

    public static void trace(String format, Object... argArray) {
        getLogger().trace(format, argArray);
    }

    public static void trace(String msg, Throwable t) {
        getLogger().trace(msg, t);
    }

    public static void trace(Marker marker, String msg) {
        getLogger().trace(marker, msg);
    }

    public static void trace(Marker marker, String format, Object arg) {
        getLogger().trace(marker, format, arg);
    }

    public static void trace(Marker marker, String format, Object arg1, Object arg2) {
        getLogger().trace(marker, format, arg1, arg2);
    }

    public static void trace(Marker marker, String format, Object... argArray) {
        getLogger().trace(marker, format, argArray);
    }

    public static void trace(Marker marker, String msg, Throwable t) {
        getLogger().trace(marker, msg, t);
    }

    public static boolean isTraceEnabled() {
        return getLogger().isTraceEnabled();
    }

    public static boolean isTraceEnabled(Marker marker) {
        return getLogger().isTraceEnabled();
    }


    public static void debug(String msg) {
        getLogger().debug(msg);
    }

    public static void debug(String format, Object arg) {
        getLogger().debug(format, arg);
    }

    public static void debug(String format, Object arg1, Object arg2) {
        getLogger().debug(format, arg1, arg2);
    }

    public static void debug(String format, Object... argArray) {
        getLogger().debug(format, argArray);
    }

    public static void debug(String msg, Throwable t) {
        getLogger().debug(msg, t);
    }

    public static void debug(Marker marker, String msg) {
        getLogger().debug(marker, msg);
    }

    public static void debug(Marker marker, String format, Object arg) {
        getLogger().debug(marker, format, arg);
    }

    public static void debug(Marker marker, String format, Object arg1, Object arg2) {
        getLogger().debug(marker, format, arg1, arg2);
    }

    public static void debug(Marker marker, String format, Object... argArray) {
        getLogger().debug(marker, format, argArray);
    }

    public static void debug(Marker marker, String msg, Throwable t) {
        getLogger().debug(marker, msg, t);
    }

    public static boolean isDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    public static boolean isDebugEnabled(Marker marker) {
        return getLogger().isDebugEnabled();
    }


    public static void info(String msg) {
        getLogger().info(msg);
    }

    public static void info(String format, Object arg) {
        getLogger().info(format, arg);
    }

    public static void info(String format, Object arg1, Object arg2) {
        getLogger().info(format, arg1, arg2);
    }

    public static void info(String format, Object... argArray) {
        getLogger().info(format, argArray);
    }

    public static void info(String msg, Throwable t) {
        getLogger().info(msg, t);
    }

    public static void info(Marker marker, String msg) {
        getLogger().info(marker, msg);
    }

    public static void info(Marker marker, String format, Object arg) {
        getLogger().info(marker, format, arg);
    }

    public static void info(Marker marker, String format, Object arg1, Object arg2) {
        getLogger().info(marker, format, arg1, arg2);
    }

    public static void info(Marker marker, String format, Object... argArray) {
        getLogger().info(marker, format, argArray);
    }

    public static void info(Marker marker, String msg, Throwable t) {
        getLogger().info(marker, msg, t);
    }

    public static boolean isInfoEnabled() {
        return getLogger().isInfoEnabled();
    }

    public static boolean isInfoEnabled(Marker marker) {
        return getLogger().isInfoEnabled();
    }


    public static void warn(String msg) {
        getLogger().warn(msg);
    }

    public static void warn(String format, Object arg) {
        getLogger().warn(format, arg);
    }

    public static void warn(String format, Object arg1, Object arg2) {
        getLogger().warn(format, arg1, arg2);
    }

    public static void warn(String format, Object... argArray) {
        getLogger().warn(format, argArray);
    }

    public static void warn(String msg, Throwable t) {
        getLogger().warn(msg, t);
    }

    public static void warn(Marker marker, String msg) {
        getLogger().warn(marker, msg);
    }

    public static void warn(Marker marker, String format, Object arg) {
        getLogger().warn(marker, format, arg);
    }

    public static void warn(Marker marker, String format, Object arg1, Object arg2) {
        getLogger().warn(marker, format, arg1, arg2);
    }

    public static void warn(Marker marker, String format, Object... argArray) {
        getLogger().warn(marker, format, argArray);
    }

    public static void warn(Marker marker, String msg, Throwable t) {
        getLogger().warn(marker, msg, t);
    }

    public static boolean isWarnEnabled() {
        return getLogger().isWarnEnabled();
    }

    public static boolean isWarnEnabled(Marker marker) {
        return getLogger().isWarnEnabled();
    }


    public static void error(String msg) {
        getLogger().error(msg);
    }

    public static void error(String format, Object arg) {
        getLogger().error(format, arg);
    }

    public static void error(String format, Object arg1, Object arg2) {
        getLogger().error(format, arg1, arg2);
    }

    public static void error(String format, Object... argArray) {
        getLogger().error(format, argArray);
    }

    public static void error(String msg, Throwable t) {
        getLogger().error(msg, t);
    }

    public static void error(Marker marker, String msg) {
        getLogger().error(marker, msg);
    }

    public static void error(Marker marker, String format, Object arg) {
        getLogger().error(marker, format, arg);
    }

    public static void error(Marker marker, String format, Object arg1, Object arg2) {
        getLogger().error(marker, format, arg1, arg2);
    }

    public static void error(Marker marker, String format, Object... argArray) {
        getLogger().error(marker, format, argArray);
    }

    public static void error(Marker marker, String msg, Throwable t) {
        getLogger().error(marker, msg, t);
    }

    public static boolean isErrorEnabled() {
        return getLogger().isErrorEnabled();
    }

    public static boolean isErrorEnabled(Marker marker) {
        return getLogger().isErrorEnabled();
    }


    public static void fatal(String msg) {
        getLogger().fatal(msg);
    }

    public static void fatal(String format, Object arg) {
        getLogger().fatal(format, arg);
    }

    public static void fatal(String format, Object arg1, Object arg2) {
        getLogger().fatal(format, arg1, arg2);
    }

    public static void fatal(String format, Object... argArray) {
        getLogger().fatal(format, argArray);
    }

    public static void fatal(String msg, Throwable t) {
        getLogger().fatal(msg, t);
    }

    public static void fatal(Marker marker, String msg) {
        getLogger().fatal(marker, msg);
    }

    public static void fatal(Marker marker, String format, Object arg) {
        getLogger().fatal(marker, format, arg);
    }

    public static void fatal(Marker marker, String format, Object arg1, Object arg2) {
        getLogger().fatal(marker, format, arg1, arg2);
    }

    public static void fatal(Marker marker, String format, Object... argArray) {
        getLogger().fatal(marker, format, argArray);
    }

    public static void fatal(Marker marker, String msg, Throwable t) {
        getLogger().fatal(marker, msg, t);
    }

    public static boolean isFatalEnabled() {
        return getLogger().isFatalEnabled();
    }

    public static boolean isFatalEnabled(Marker marker) {
        return getLogger().isFatalEnabled();
    }

    public static IMutableEvent trace() {
        return getLogger().trace();
    }

    public static IMutableEvent debug() {
        return getLogger().debug();
    }

    public static IMutableEvent info() {
        return getLogger().info();
    }

    public static IMutableEvent warn() {
        return getLogger().warn();
    }

    public static IMutableEvent error() {
        return getLogger().error();
    }

    public static IMutableEvent fatal() {
        return getLogger().fatal();
    }

}
