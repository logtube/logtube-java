package io.github.logtube;

import org.jetbrains.annotations.NotNull;

/**
 * Logtube 大量采用 provided 的 maven 依赖，目的是为了让各个组件可以松耦合
 * <p>
 * 如果项目没有使用某个组件，而 LogtubeOptions 里面包含了该组件的配置，直接 import 该组件并设置配置会导致崩溃
 * 因此使用该接口进行一层抽象，使用 Class.forName 尝试载入该组件，如果能载入，就进行配置，如果不能载入，就忽略
 */
public interface LogtubeComponentConfigurator {

    void configure(@NotNull LogtubeOptions options);

}
