package io.github.logtube;

import org.jetbrains.annotations.NotNull;

/**
 * 日志输出
 */
public interface IEventOutput {

    /**
     * 输出一个日志事件
     *
     * @param e 日志事件
     */
    void appendEvent(@NotNull IEvent e);

}
