package net.landzero.xlog.http;

import net.landzero.xlog.XLogEventBuilder;
import net.landzero.xlog.utils.Flatten;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessEventBuilder implements XLogEventBuilder<AccessEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessEventBuilder.class);

    private AccessEvent event = new AccessEvent();

    private long startAt = System.currentTimeMillis();

    private HttpServletRequest httpRequest;

    @NotNull
    @Contract("_ -> this")
    public AccessEventBuilder setServletRequest(@NotNull ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            LOGGER.debug("request is not a HttpServletRequest, " + request.toString());
            return this;
        }
        httpRequest = (HttpServletRequest) request;
        this.event.setMethod(httpRequest.getMethod());
        this.event.setHost(httpRequest.getServerName());
        this.event.setQuery(httpRequest.getQueryString());
        this.event.setHeaderUserToken(httpRequest.getHeader("UserToken"));
        this.event.setHeaderAppInfo(Flatten.flattenJSON(httpRequest.getHeader("X-Defined-AppInfo")));
        this.event.setHeaderVerInfo(Flatten.flattenJSON(httpRequest.getHeader("X-Defined-VerInfo")));

        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public AccessEventBuilder setServletResponse(@NotNull ServletResponse response) {
        if (!(response instanceof HttpServletResponse)) {
            LOGGER.debug("response is not a HttpServletRequest, " + response.toString());
            return this;
        }
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        this.event.setStatus(httpResponse.getStatus());

        // 设置返回值大小
        if (response instanceof XLogHttpServletResponseWrapper) {
            this.event.setResponseSize(((XLogHttpServletResponseWrapper) response).getContent().length);
        }
        return this;
    }

    @NotNull
    @Override
    public AccessEvent build() {
        this.event.setDuration(System.currentTimeMillis() - this.startAt);

        // 为避免调用getParameterMap()方法影响业务使用Request Body，放到过滤器退出时记录参数。
        if (httpRequest instanceof XLogHttpServletRequestWrapper) {
            this.event.setParams(((XLogHttpServletRequestWrapper) httpRequest).getParams());
        } else {
            this.event.setParams(Flatten.flattenParameters(httpRequest.getParameterMap()));
        }
        return this.event;
    }

}
