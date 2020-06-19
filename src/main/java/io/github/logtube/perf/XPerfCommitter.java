package io.github.logtube.perf;

import io.github.logtube.core.IMutableEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class XPerfCommitter {

    private final IMutableEvent event;

    private final long startTime = System.currentTimeMillis();

    public XPerfCommitter(@NotNull IMutableEvent event) {
        this.event = event;
    }

    /**
     * 设置操作类型，用于分类
     *
     * @param action 操作
     * @return this
     */
    @NotNull
    @Contract("_->this")
    public XPerfCommitter setAction(String action) {
        this.event.extra("action", action);
        return this;
    }

    /**
     * 设置操作详情，用于记录
     *
     * @param actionDetail 操作详情
     * @return this
     */
    @NotNull
    @Contract("_->this")
    public XPerfCommitter setActionDetail(String actionDetail) {
        this.event.extra("action_detail", actionDetail);
        return this;
    }

    /**
     * 设置一个整数返回值，用于记录和查询
     *
     * @param value 返回值
     * @return this
     */
    @NotNull
    @Contract("_->this")
    public XPerfCommitter setValueInteger(long value) {
        this.event.extra("value_integer", value);
        return this;
    }

    public void commit() {
        this.event.xDuration(System.currentTimeMillis() - this.startTime).commit();
    }

}
