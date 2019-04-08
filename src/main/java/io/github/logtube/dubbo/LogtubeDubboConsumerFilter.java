package io.github.logtube.dubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import io.github.logtube.Logtube;
import io.github.logtube.LogtubeConstants;
import io.github.logtube.core.IRootEventLogger;

@Activate(group = {"consumer"})
public class LogtubeDubboConsumerFilter implements Filter {

    private static final IRootEventLogger ROOT_LOGGER = Logtube.getRootLogger();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(LogtubeConstants.DUBBO_CRID_KEY, ROOT_LOGGER.getCrid());
        return invoker.invoke(invocation);
    }

}
