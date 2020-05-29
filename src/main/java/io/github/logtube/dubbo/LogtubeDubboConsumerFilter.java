package io.github.logtube.dubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import io.github.logtube.Logtube;
import io.github.logtube.LogtubeConstants;

@Activate(group = {"consumer"})
public class LogtubeDubboConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(LogtubeConstants.DUBBO_CRID_KEY, Logtube.getProcessor().getCrid());
        RpcContext.getContext().setAttachment(LogtubeConstants.DUBBO_CRSRC_KEY, Logtube.getProcessor().getProject());
        return invoker.invoke(invocation);
    }

}
