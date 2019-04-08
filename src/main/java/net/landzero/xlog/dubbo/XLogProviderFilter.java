package net.landzero.xlog.dubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import net.landzero.xlog.XLog;
import net.landzero.xlog.constants.Constants;
import org.slf4j.MDC;

@Activate(group = {"provider"})
public class XLogProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcAccessEventBuilder builder = new RpcAccessEventBuilder();
        try {
            setupXLog();
            return invoker.invoke(invocation);
        } finally {
            builder.commit();
            resetXLog();
        }
    }

    private void setupXLog() {
        XLog.setCrid(RpcContext.getContext().getAttachment(Constants.DUBBO_CRID_KEY));
        XLog.setPath(RpcContext.getContext().getAttachment("interface") + "." + RpcContext.getContext().getMethodName());
        MDC.put(Constants.MDC_CRID_KEY, XLog.crid());
        MDC.put(Constants.MDC_CRID_MARK_KEY, XLog.cridMark());
    }

    private void resetXLog() {
        XLog.clearCrid();
        XLog.clearPath();
        MDC.remove(Constants.MDC_CRID_KEY);
        MDC.remove(Constants.MDC_CRID_MARK_KEY);
    }

}
