package io.github.logtube.core;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 日志事件序列化器，将一个日志序列化到一个输出流上
 */
public interface IEventSerializer {

    /**
     * 序列化一个日志事件
     *
     * @param e 日志事件
     * @param w 输出流
     * @throws IOException 输出流的异常
     */
    void serialize(@NotNull IEvent e, @NotNull Writer w) throws IOException;

    /**
     * 序列化为字符串
     *
     * @param e 日志事件
     * @return 序列化结果
     */
    default @NotNull String toString(IEvent e) {
        StringWriter s = new StringWriter();
        try {
            serialize(e, s);
        } catch (IOException ignored) {
        }
        return s.toString();
    }

}
