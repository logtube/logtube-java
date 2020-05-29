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
     * dubbo key for correlation source
     */
    public static final String DUBBO_CRSRC_KEY = "crsrc";

    /**
     * header name for correlation id
     * <p>
     * CRID 的 HTTP Header 名
     */
    public static final String HTTP_CRID_HEADER = "X-Correlation-ID";

    /**
     * header for correlation source
     *
     * CRSRC 的 HTTP Header 名
     */
    public static final String HTTP_CRSRC_HEADER = "X-Correlation-Src";

    /**
     * 例外名单参数名称
     */
    public static final String EXCLUSION_PATH_LIST_PARAM_NAME = "exclusionList";

}
