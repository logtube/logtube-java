package io.github.logtube.perf;

import io.github.logtube.core.IEventLogger;

public class XPerf {

    /**
     * 创建一个事件追踪器
     * <p>
     * create a performance tracker
     *
     * @param logger event logger
     * @return a new performance tracker
     */
    public static XPerfCommitter create(IEventLogger logger) {
        return new XPerfCommitter(logger.topic("x-perf"));
    }

}
