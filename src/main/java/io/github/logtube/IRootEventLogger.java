package io.github.logtube;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IRootEventLogger extends IEventLogger, ITopicMutableAware {

    void clearCrid();

    void setCrid(@Nullable String crid);

    @NotNull
    String getCrid();

    void setHostname(@Nullable String hostname);

    @Nullable
    String getHostname();

    void setProject(@Nullable String project);

    @Nullable
    String getProject();

    void setEnv(@Nullable String env);

    @Nullable
    String getEnv();

    void setOutputs(@NotNull List<IEventOutput> outputs);

    @NotNull
    List<IEventOutput> getOutputs();

    default void addOutput(@NotNull IEventOutput output) {
        getOutputs().add(output);
    }

}
