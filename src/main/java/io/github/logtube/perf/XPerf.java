package io.github.logtube.perf;

import io.github.logtube.core.IEventLogger;
import io.github.logtube.utils.Reflections;

public class XPerf {

    @Deprecated
    public static XPerfCommitter create(String action, Object... arguments) {
        return create(null, action, arguments);
    }

    /**
     * 创建一个事件追踪器
     * <p>
     * create a performance tracker
     *
     * @param logger    event logger
     * @param action    action name
     * @param arguments arguments
     * @return a new performance tracker
     */
    public static XPerfCommitter create(IEventLogger logger, String action, Object... arguments) {
        String className = "MISSING";
        String methodName = "MISSING";
        StackTraceElement element = Reflections.getStackTraceElement(XPerf.class);
        if (element != null) {
            className = element.getClassName();
            methodName = element.getMethodName();
        }
        return new XPerfCommitter(logger, className, methodName).setCommand(action, arguments);
    }

}
