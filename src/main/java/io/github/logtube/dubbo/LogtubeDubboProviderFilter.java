package io.github.logtube.dubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import io.github.logtube.Logtube;
import io.github.logtube.LogtubeConstants;
import io.github.logtube.core.IEventProcessor;

@Activate(group = {"provider"})
public class LogtubeDubboProviderFilter implements Filter {

    private static final IEventProcessor ROOT_LOGGER = Logtube.getProcessor();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        DubboAccessEventCommitter builder = new DubboAccessEventCommitter();
        try {
            setupRootLogger();
            return invoker.invoke(invocation);
        } finally {
            builder.commit();
            resetRootLogger();
        }
    }

    private void setupRootLogger() {
        ROOT_LOGGER.setCrid(RpcContext.getContext().getAttachment(LogtubeConstants.DUBBO_CRID_KEY));
        ROOT_LOGGER.setPath(RpcContext.getContext().getAttachment("interface") + "." + RpcContext.getContext().getMethodName());
    }

    private void resetRootLogger() {
        ROOT_LOGGER.clearCrid();
        ROOT_LOGGER.clearPath();
    }

}
