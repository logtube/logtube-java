package io.github.logtube.dubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import io.github.logtube.Logtube;
import io.github.logtube.LogtubeConstants;

@Activate(group = {"provider"})
public class LogtubeDubboProviderFilter implements Filter {

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
        Logtube.getProcessor().setCrid(RpcContext.getContext().getAttachment(LogtubeConstants.DUBBO_CRID_KEY));
        Logtube.getProcessor().setCrsrc(RpcContext.getContext().getAttachment(LogtubeConstants.DUBBO_CRSRC_KEY));
        Logtube.getProcessor().setPath(RpcContext.getContext().getAttachment("interface") + "." + RpcContext.getContext().getMethodName());
    }

    private void resetRootLogger() {
        Logtube.getProcessor().clearContext();
    }

}
