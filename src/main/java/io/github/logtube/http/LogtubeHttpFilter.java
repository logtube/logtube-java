package io.github.logtube.http;

import io.github.logtube.Logtube;
import io.github.logtube.LogtubeConstants;
import io.github.logtube.utils.Requests;
import io.github.logtube.utils.Strings;
import org.jetbrains.annotations.NotNull;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class provides a servlet Filter, setup correlation id, outputs structured events
 * <p>
 * MDC added:
 * cridMark - standard correlation id mark, i.e. "CRID[xxxxxxxxxxxxxxxx]"
 * crid - correlation id
 */
public class LogtubeHttpFilter implements Filter {

    /**
     * -例外名单。此名单中的请求将不记录xlog
     */
    public static final Set<String> EXCLUSION_PATH_LIST = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionListStr = filterConfig.getInitParameter(LogtubeConstants.EXCLUSION_PATH_LIST_PARAM_NAME);
        if (!Strings.isEmpty(exclusionListStr)) {
            Arrays.stream(exclusionListStr.split(",")).map(Strings::safeNormalize).forEach(EXCLUSION_PATH_LIST::add);
        }
        EXCLUSION_PATH_LIST.add("/check");
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
        if (EXCLUSION_PATH_LIST.contains(httpRequest.getRequestURI()))
            chain.doFilter(request, response);
        else
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
