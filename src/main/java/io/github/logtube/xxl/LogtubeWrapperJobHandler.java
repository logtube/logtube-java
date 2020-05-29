package io.github.logtube.xxl;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import io.github.logtube.Logtube;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LogtubeWrapperJobHandler extends IJobHandler {

    @Nullable
    private final String name;

    @NotNull
    private final IJobHandler internal;

    public LogtubeWrapperJobHandler(@Nullable String name, @NotNull IJobHandler internal) {
        this.name = name;
        this.internal = internal;
    }

    @Override
    public void init() {
        this.internal.init();
    }

    @Override
    public void destroy() {
        this.internal.destroy();
    }

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            Logtube.getProcessor().setCrid(null); // set null to create a random one
            Logtube.getProcessor().setCrsrc("xxljob");
            Logtube.getProcessor().setPath(this.name);
            return this.internal.execute(s);
        } finally {
            Logtube.getProcessor().clearContext();
        }
    }

}
