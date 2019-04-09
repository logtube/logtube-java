package io.github.logtube.perf;

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
        String className = "MISSING";
        String methodName = "MISSING";
        StackTraceElement element = Reflections.getStackTraceElement(XPerf.class);
        if (element != null) {
            className = element.getClassName();
            methodName = element.getMethodName();
        }
        return new XPerfCommitter(className, methodName).setCommand(action, arguments);
    }

}
