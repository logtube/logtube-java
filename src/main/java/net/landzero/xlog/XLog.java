package net.landzero.xlog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.landzero.xlog.logger.XLoggerImpl;
import net.landzero.xlog.utils.Hex;
import net.landzero.xlog.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * This class encapsulates xlog related thread-local variables, and provides convenient accesses
 * <p>
 * 此类封装了 XLog 相关的线程隔离的变量，并提供了便于使用的静态访问方法
 */
public class XLog {

    /**
     * default correlation id is "-"
     * <p>
     * 默认的 CRID 为 "-"
     */
    public static final String EMPTY_CRID = "-";

    /**
     * name of logger for all structured events
     * <p>
     * 结构化日志的 Logger 名，结构化日志使用独立的系统进行序列化
     */
    public static final String LOGGER_NAME_STRUCTURED_EVENT = "xlog.Event";

    /**
     * normal logger
     * <p>
     * 普通的 LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(XLog.class);

    /**
     * special logger for structured event
     * <p>
     * 结构化日志专用的 LOGGER
     */
    private static final Logger EVENT_LOGGER = LoggerFactory.getLogger(LOGGER_NAME_STRUCTURED_EVENT);

    /**
     * use ISO8601 with timezone for #{java.util.Date} serialization
     * <p>
     * 使用带时区的 ISO8601 标准作为 #{java.util.Data} 的序列化格式
     */
    private static final Gson EVENT_GSON = new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();

    /**
     * store crid for current thread
     * <p>
     * 保存当前线程的 CRID
     */
    private static final CridThreadLocal CRID_THREAD_LOCAL = new CridThreadLocal();

    /**
     * store path for current thread
     * <p>
     * 保存当前线程的 HTTP 路径
     */
    private static final PathThreadLocal PATH_THREAD_LOCAL = new PathThreadLocal();

    /**
     * get correlation id for current thread
     * <p>
     * 获取当前线程的 CRID
     *
     * @return crid correlation id
     */
    @NotNull
    public static String crid() {
        return CRID_THREAD_LOCAL.get();
    }

    /**
     * get the standard correlation id mark, in format "CRID[1234567890abcdef]"
     * <p>
     * 获取标准的 CRID 标记，格式为 "CRID[1234567890abcdef]"
     *
     * @return crid_mark correlation id mark
     */
    @NotNull
    public static String cridMark() {
        return "CRID[" + crid() + "]";
    }

    /**
     * set the correlation id for current thread, use a 16 random hex characters if setting to null or an empty string
     * <p>
     * 为当前线程设置 CRID，如果为空字符串，则设置为长度为16的随机16进制字符串
     *
     * @param v new correlation id
     */
    public static void setCrid(@Nullable String v) {
        v = Strings.normalize(v);
        if (v == null) {
            CRID_THREAD_LOCAL.set(Hex.randomHex16());
            return;
        }
        CRID_THREAD_LOCAL.set(v);
    }

    /**
     * clear correlation id for current thread
     * <p>
     * 清除当前线程的 CRID
     */
    public static void clearCrid() {
        CRID_THREAD_LOCAL.remove();
    }

    /**
     * get path for current thread
     * <p>
     * 获取当前线程的 HTTP 路径
     *
     * @return path
     */
    @Nullable
    public static String path() {
        return PATH_THREAD_LOCAL.get();
    }

    /**
     * set path for current thread
     * <p>
     * 设置当前线程的 HTTP 路径
     *
     * @param path path
     */
    public static void setPath(@Nullable String path) {
        PATH_THREAD_LOCAL.set(Strings.normalize(path));
    }

    /**
     * clear path for current thread
     * <p>
     * 清除当前线程的 HTTP 路径
     */
    public static void clearPath() {
        PATH_THREAD_LOCAL.remove();
    }

    /**
     * add a structured event, will be json serialized immediately, topic field must be set
     * <p>
     * 添加一个结构化日志事件，会被立即执行 JSON 序列化，必须指定 topic 字段
     *
     * @param event event 结构化事件
     * @param <T>   #{XLogEvent} 的子类
     */
    public static <T extends XLogEvent> void appendEvent(@NotNull T event) {
        // set timestamp to now if timestamp is not set
        if (event.getTimestamp() == null) {
            event.setTimestamp(new Date());
        }
        // set crid if crid is not set
        if (event.getCrid() == null) {
            event.setCrid(crid());
        }
        // set path if path not set
        if (event.getPath() == null) {
            event.setPath(path());
        }
        // do the serialization and write to INFO level
        try {
            EVENT_LOGGER.info(XLog.EVENT_GSON.toJson(event));
        } catch (Exception e) {
            LOGGER.error("failed to serialize structured event [" + event.getClass().getCanonicalName() + "]", e);
        }
    }

    @NotNull
    public static String keyword(@Nullable Object... os) {
        return Strings.keyword(os);
    }

    @NotNull
    public static String K(@Nullable Object... os) {
        return Strings.keyword(os);
    }

    /**
     * get the decorated logger
     *
     * @param clazz the class
     * @return the Logger
     */
    public static XLogger getLogger(Class<?> clazz) {
        return new XLoggerImpl(clazz);
    }

    /**
     * thread-local class designed for crid
     */
    private static class CridThreadLocal extends ThreadLocal<String> {
        @Override
        protected String initialValue() {
            return EMPTY_CRID;
        }
    }

    /**
     * thread-local class designed for path
     */
    private static class PathThreadLocal extends ThreadLocal<String> {

        @Override
        protected String initialValue() {
            return null;
        }

    }

}
