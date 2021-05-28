package io.github.logtube.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * 一个只读的日志事件
 */
public interface IEvent extends Cloneable {

    /**
     * 事件发生时间
     *
     * @return UNIX 时间戳，毫秒
     */
    long getTimestamp();

    /**
     * 主机名
     *
     * @return 主机名
     */
    @NotNull String getHostname();

    /**
     * 环境名
     *
     * @return 环境名
     */
    @NotNull String getEnv();

    /**
     * 项目名
     *
     * @return 项目名
     */
    @NotNull String getProject();

    /**
     * 主题，info, debug 等传统日志级别也算作主题
     *
     * @return 主题
     */
    @NotNull String getTopic();

    /**
     * CRID
     *
     * @return CRID
     */
    @NotNull String getCrid();

    /**
     * CRSRC
     *
     * @return CRSRC
     */
    @NotNull String getCrsrc();

    /**
     * 消息内容，对于结构化日志，消息内容可以为空
     *
     * @return 消息
     */
    @Nullable String getMessage();

    /**
     * 逗号分隔的关键字，一般情况下，消息内容不会被索引，只有关键字会被索引
     *
     * @return 逗号分隔的关键字
     */
    @Nullable String getKeyword();

    /**
     * 额外信息，结构化日志使用该字段保存额外信息，只支持一维对象，也就是说，只支持 Boolean, String 和 Number
     *
     * @return 额外信息
     */
    @Nullable Map<String, Object> getExtra();

    Object clone() throws CloneNotSupportedException;
}
