package io.github.logtube;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 根日志器暴露的接口，跟日志器负责存储 主机名，项目名，环境名 和 当前线程的 CRID，并可以设置输出
 */
public interface IRootEventLogger extends IEventLogger, ITopicMutableAware {

    void clearCrid();

    void setCrid(@Nullable String crid);

    @NotNull String getCrid();

    void setHostname(@Nullable String hostname);

    @Nullable String getHostname();

    void setProject(@Nullable String project);

    @Nullable String getProject();

    void setEnv(@Nullable String env);

    @Nullable String getEnv();

    void setOutputs(@NotNull List<IEventOutput> outputs);

    @NotNull List<IEventOutput> getOutputs();

    default void addOutput(@NotNull IEventOutput output) {
        getOutputs().add(output);
    }

}
