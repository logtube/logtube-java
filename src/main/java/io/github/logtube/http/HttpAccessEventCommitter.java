package io.github.logtube.http;

import io.github.logtube.Logtube;
import io.github.logtube.core.IMutableEvent;
import io.github.logtube.utils.Flatten;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HttpAccessEventCommitter {

    private @NotNull
    final IMutableEvent event = Logtube.getLogger(HttpAccessEventCommitter.class).topic("x-access");

    private final long startAt = System.currentTimeMillis();

    private HttpServletRequest httpRequest;

    @Contract("_ -> this")
    public @NotNull HttpAccessEventCommitter setServletRequest(@NotNull ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            return this;
        }
        this.httpRequest = (HttpServletRequest) request;
        this.event
                .extra("method", httpRequest.getMethod())
                .extra("host", httpRequest.getServerName())
                .extra("query", httpRequest.getQueryString())
                .extra("header_user_token", httpRequest.getHeader("UserToken"))
                .extra("header_app_info", Flatten.flattenJSON(httpRequest.getHeader("X-Defined-AppInfo")))
                .extra("header_ver_info", Flatten.flattenJSON(httpRequest.getHeader("X-Defined-VerInfo")));
        return this;
    }

    @Contract("_ -> this")
    public @NotNull HttpAccessEventCommitter setServletResponse(@NotNull ServletResponse response) {
        if (!(response instanceof HttpServletResponse)) {
            return this;
        }
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        this.event.extra("status", httpResponse.getStatus());

        // 设置返回值大小
        if (response instanceof LogtubeHttpServletResponseWrapper) {
            this.event.extra("response_size", ((LogtubeHttpServletResponseWrapper) response).getResponseSize());
        }
        return this;
    }

    public void commit() {
        List<String> params = null;

        // 为避免调用getParameterMap()方法影响业务使用Request Body，放到过滤器退出时记录参数。
        if (this.httpRequest instanceof LogtubeHttpServletRequestWrapper) {
            params = ((LogtubeHttpServletRequestWrapper) httpRequest).getParams();
        } else {
            params = Flatten.flattenParameters(httpRequest.getParameterMap());
        }

        this.event.xDuration(System.currentTimeMillis() - this.startAt);

        if (params != null) {
            this.event.extra("params", String.join(",", params));
        }

        this.event.commit();
    }

}
