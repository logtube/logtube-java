package io.github.logtube.core;

import io.github.logtube.utils.ILifeCycle;
import org.jetbrains.annotations.NotNull;

/**
 * 日志输出
 */
public interface IEventOutput extends ILifeCycle {

    /**
     * 输出一个日志事件
     *
     * @param e 日志事件
     */
    void appendEvent(@NotNull IEvent e);

}
