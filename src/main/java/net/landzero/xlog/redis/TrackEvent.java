/**
 *
 */
package net.landzero.xlog.redis;

import com.google.gson.annotations.SerializedName;
import net.landzero.xlog.XLogEvent;
import net.landzero.xlog.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Redis日志记录
 *
 * @author chenkeguang 2018年10月19日
 */
public class TrackEvent extends XLogEvent {
    private static final String TOPIC_REDIS = "x-redis-track";

    @SerializedName("cmd")
    private String cmd;

    @SerializedName("key")
    private String key;

    @SerializedName("param_value")
    private String paramValue;

    @SerializedName("duration")
    private long duration;

    @SerializedName("result_size")
    private long resultSize;

    @SerializedName("param_value_size")
    private long paramValueSize;


    /* (non-Javadoc)
     * @see net.landzero.xlog.XLogEvent#topic()
     */
    @Override
    public @NotNull
    String topic() {
        return TOPIC_REDIS;
    }

    /**
     * @return the cmd
     */
    @Nullable
    public String getCmd() {
        return cmd;
    }

    /**
     * @param cmd the cmd to set
     */
    public void setCmd(@Nullable String cmd) {
        this.cmd = Strings.normalize(cmd);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * @return the resultSize
     */
    public long getResultSize() {
        return resultSize;
    }

    /**
     * @param resultSize the resultSize to set
     */
    public void setResultSize(long resultSize) {
        this.resultSize = resultSize;
    }

    /**
     * @return the paramValueSize
     */
    public long getParamValueSize() {
        return paramValueSize;
    }

    /**
     * @param paramValueSize the paramValueSize to set
     */
    public void setParamValueSize(long paramValueSize) {
        this.paramValueSize = paramValueSize;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the paramValue
     */
    public String getParamValue() {
        return paramValue;
    }

    /**
     * @param paramValue the paramValue to set
     */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

}
