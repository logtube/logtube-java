/**
 *
 */
package net.landzero.xlog.constants;

public class Constants {

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
     * MDC key for correlation id
     * <p>
     * CRID 的 MDC 键值
     */
    public static final String MDC_CRID_KEY = "crid";

    /**
     * MDC key for correlation id mark
     * <p>
     * CRID 标记 的 MDC 键值
     */
    public static final String MDC_CRID_MARK_KEY = "cridMark";

    /**
     * Http请求参数分割符 
     */
    public static final String REQUEST_PARAM_SPLIT_CHAR = "&";

    /**
     * Http请求参数分割符 
     */
    public static final String EQUAL_SYMBOL = "=";

    /**
     * -逗号
     */
    public static final String COMMA = ",";

    /**
     * -例外名单参数名称
     */
    public static final String EXCLUSION_PATH_LIST_PARAM_NAME = "exclusionList";
}
