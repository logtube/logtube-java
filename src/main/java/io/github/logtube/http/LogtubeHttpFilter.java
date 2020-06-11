package io.github.logtube.http;

import io.github.logtube.Logtube;
import io.github.logtube.LogtubeConstants;
import io.github.logtube.utils.HttpIgnore;
import io.github.logtube.utils.Requests;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class provides a servlet Filter, setup correlation id, outputs structured events
 * <p>
 * MDC added:
 * cridMark - standard correlation id mark, i.e. "CRID[xxxxxxxxxxxxxxxx]"
 * crid - correlation id
 */
public class LogtubeHttpFilter implements Filter {

    public static HttpIgnore[] GLOBAL_HTTP_IGNORES = new HttpIgnore[0];

    private HttpIgnore[] LOCAL_HTTP_IGNORES = new HttpIgnore[0];

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 兼容旧配置，从 HttpFilter 配置中获取要忽略的 HTTP 请求
        ArrayList<HttpIgnore> httpIgnores = new ArrayList<>();
        String exclusionListStr = filterConfig.getInitParameter(LogtubeConstants.EXCLUSION_PATH_LIST_PARAM_NAME);
        if (!Strings.isEmpty(exclusionListStr)) {
            Arrays.stream(exclusionListStr.split(",")).map(Strings::safeNormalize).forEach((s) -> {
                httpIgnores.add(new HttpIgnore("GET", s));
            });
        }
        this.LOCAL_HTTP_IGNORES = httpIgnores.toArray(new HttpIgnore[]{});
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 如果不是HTTP请求，不走xlog处理流程，直接往下去
        if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 如果是例外名单，不走xlog处理流程，直接往下去

        // 兼容的旧配置，从 ServletFilter 初始化出来的配置
        for (HttpIgnore hi : this.LOCAL_HTTP_IGNORES) {
            if (hi.method.equalsIgnoreCase(httpRequest.getMethod()) && hi.path.equals(httpRequest.getRequestURI())) {
                chain.doFilter(request, response);
                return;
            }
        }

        // logtube.yml/logtube.properties 的全局配置
        for (HttpIgnore hi : GLOBAL_HTTP_IGNORES) {
            if (hi.method.equalsIgnoreCase(httpRequest.getMethod()) && hi.path.equals(httpRequest.getRequestURI())) {
                chain.doFilter(request, response);
                return;
            }
        }

        // 例外没有匹配，执行
        process(httpRequest, httpResponse, chain);
    }

    private void process(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request = wrapRequest(request);
        response = wrapResponse(response);
        setupRootLogger(request, response);
        HttpAccessEventCommitter event = new HttpAccessEventCommitter().setServletRequest(request);
        try {
            chain.doFilter(request, response);
            event.setServletResponse(response);
        } finally {
            event.commit();
            resetRootLogger();
        }
    }

    @NotNull
    private HttpServletRequest wrapRequest(@NotNull HttpServletRequest request) throws IOException {
        if (Requests.hasJsonBody(request)
                || Requests.hasFormUrlencodedBody(request)) {
            return new LogtubeHttpServletRequestWrapper(request);
        }

        return request;
    }

    @NotNull
    private HttpServletResponse wrapResponse(@NotNull HttpServletResponse response) {
        return new LogtubeHttpServletResponseWrapper(response);
    }

    private void setupRootLogger(HttpServletRequest request, HttpServletResponse response) {
        Logtube.getProcessor().setPath(request.getRequestURI());
        Logtube.getProcessor().setCrid(request.getHeader(LogtubeConstants.HTTP_CRID_HEADER));
        Logtube.getProcessor().setCrsrc(request.getHeader(LogtubeConstants.HTTP_CRSRC_HEADER));
        response.setHeader(LogtubeConstants.HTTP_CRID_HEADER, Logtube.getProcessor().getCrid());
    }

    private void resetRootLogger() {
        Logtube.getProcessor().clearContext();
    }

    @Override
    public void destroy() {
    }

}
