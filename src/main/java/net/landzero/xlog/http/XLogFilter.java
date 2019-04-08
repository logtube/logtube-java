package net.landzero.xlog.http;

import net.landzero.xlog.XLog;
import net.landzero.xlog.constants.Constants;
import net.landzero.xlog.utils.Requests;
import net.landzero.xlog.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a servlet Filter, setup correlation id, outputs structured events
 * <p>
 * MDC added:
 * cridMark - standard correlation id mark, i.e. "CRID[xxxxxxxxxxxxxxxx]"
 * crid - correlation id
 */
public class XLogFilter implements Filter {

    /**
     * header name for correlation id
     * <p>
     * CRID 的 HTTP Header 名
     * <p>
     * xlog 1.11 版本使用Constants.CRID_HEADER_NAME替代。
     */
    @Deprecated
    public static final String CRID_HEADER_NAME = Constants.HTTP_CRID_HEADER;

    /**
     * -例外名单。此名单中的请求将不记录xlog
     */
    public static final Map<String, String> EXCLUSION_PATH_LIST = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionListStr = filterConfig.getInitParameter(Constants.EXCLUSION_PATH_LIST_PARAM_NAME);
        if (!Strings.isEmpty(exclusionListStr)) {
            Arrays.asList(exclusionListStr.split(Constants.COMMA)).stream().map(Strings::safeNormalize).forEach(path -> {
                EXCLUSION_PATH_LIST.put(path, path);
            });
        }
        EXCLUSION_PATH_LIST.put("/check", "/check");
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
        if (EXCLUSION_PATH_LIST.containsKey(httpRequest.getRequestURI()))
            chain.doFilter(request, response);
        else
            xlogProcess(httpRequest, httpResponse, chain);
    }

    private void xlogProcess(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request = wrapRequest(request);
        XLogHttpServletResponseWrapper responseWrapper = new XLogHttpServletResponseWrapper(response);
        setupXLog(request, response);
        AccessEventBuilder event = new AccessEventBuilder().setServletRequest(request);
        try {
            chain.doFilter(request, responseWrapper);

            // 将Wrapper的内容回填到原生Response
            if (responseWrapper.useOutputStream()) {
                response.getOutputStream().write(responseWrapper.getContent());
            }
            event.setServletResponse(responseWrapper);
        } finally {
            event.commit();
            resetXLog();
        }
    }

    @NotNull
    private HttpServletRequest wrapRequest(@NotNull HttpServletRequest request) throws IOException {
        if (Requests.hasJsonBody(request)
                || Requests.hasFormUrlencodedBody(request)) {
            return new XLogHttpServletRequestWrapper(request);
        }

        return request;
    }

    private void setupXLog(HttpServletRequest request, HttpServletResponse response) {
        XLog.setPath(request.getRequestURI());
        XLog.setCrid(request.getHeader(CRID_HEADER_NAME));
        response.setHeader(CRID_HEADER_NAME, XLog.crid());
        MDC.put(Constants.MDC_CRID_KEY, XLog.crid());
        MDC.put(Constants.MDC_CRID_MARK_KEY, XLog.cridMark());
    }

    private void resetXLog() {
        XLog.clearCrid();
        XLog.clearPath();
        MDC.remove(Constants.MDC_CRID_KEY);
        MDC.remove(Constants.MDC_CRID_MARK_KEY);
    }

    @Override
    public void destroy() {
    }

}
