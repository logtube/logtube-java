package io.github.logtube.core;

import org.jetbrains.annotations.NotNull;

/**
 * 日志中间件，可以将日志进行改变，用于 派生型日志器
 */
public interface IEventMiddleware {

    /**
     * 修改日志
     *
     * @param event 原始日志
     * @return 修改后的日志
     */
    @NotNull IMutableEvent handle(@NotNull IMutableEvent event);

}
