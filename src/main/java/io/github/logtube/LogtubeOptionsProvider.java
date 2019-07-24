package io.github.logtube;

import org.jetbrains.annotations.Nullable;

import java.util.Properties;

public interface LogtubeOptionsProvider {

    @Nullable Properties loadOptions();

}
