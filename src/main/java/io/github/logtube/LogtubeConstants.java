/**
 *
 */
package io.github.logtube;

public class LogtubeConstants {

    /**
     * dubbo key for correlation id
     */
    public static final String DUBBO_CRID_KEY = "crid";

    /**
     * header name for correlation id
     * <p>
     * CRID 的 HTTP Header 名
     */
    public static final String HTTP_CRID_HEADER = "X-Correlation-ID";

    /**
     * 例外名单参数名称
     */
    public static final String EXCLUSION_PATH_LIST_PARAM_NAME = "exclusionList";

}
