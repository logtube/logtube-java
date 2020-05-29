package io.github.logtube.dubbo27;

import io.github.logtube.Logtube;
import io.github.logtube.LogtubeConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Activate(group = {"consumer"})
public class LogtubeDubboConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(LogtubeConstants.DUBBO_CRID_KEY, Logtube.getProcessor().getCrid());
        RpcContext.getContext().setAttachment(LogtubeConstants.DUBBO_CRSRC_KEY, Logtube.getProcessor().getProject());
        return invoker.invoke(invocation);
    }

}
