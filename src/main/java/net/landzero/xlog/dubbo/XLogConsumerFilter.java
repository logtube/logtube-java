package net.landzero.xlog.dubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import net.landzero.xlog.XLog;
import net.landzero.xlog.constants.Constants;

@Activate(group = {"consumer"})
public class XLogConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(Constants.DUBBO_CRID_KEY, XLog.crid());
        return invoker.invoke(invocation);
    }

}
