package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;

public class HttpIgnore {

    @NotNull
    public final String method;

    @NotNull
    public final String path;

    public HttpIgnore(@NotNull String method, @NotNull String path) {
        this.method = method;
        this.path = path;
    }

}
