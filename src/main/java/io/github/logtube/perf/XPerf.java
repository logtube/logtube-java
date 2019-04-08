package io.github.logtube.perf;

import io.github.logtube.utils.CallerInfo;
import io.github.logtube.utils.Reflections;

public class XPerf {

    /**
     * 创建一个事件追踪器
     * <p>
     * create a performance tracker
     *
     * @param action    action name
     * @param arguments arguments
     * @return a new performance tracker
     */
    public static XPerfCommitter create(String action, Object... arguments) {
        CallerInfo info = Reflections.getCallerInfo();
        return new XPerfCommitter(info.getClassName(), info.getMethodName()).setCommand(action, arguments);
    }

}
