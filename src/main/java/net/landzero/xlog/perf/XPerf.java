package net.landzero.xlog.perf;

import net.landzero.xlog.XLogCommitter;
import net.landzero.xlog.utils.CallerInfo;
import net.landzero.xlog.utils.Reflections;

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
    public static XLogCommitter create(String action, Object... arguments) {
        CallerInfo info = Reflections.getCallerInfo();
        return new XPerfEventBuilder(info.getClassName(), info.getMethodName()).setCommand(action, arguments);
    }

}
