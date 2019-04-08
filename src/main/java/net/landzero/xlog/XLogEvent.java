package net.landzero.xlog;

import com.google.gson.annotations.SerializedName;
import net.landzero.xlog.utils.Hex;
import net.landzero.xlog.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;

/**
 * base class for structured event, subclass must implements topic() and provides a valid 'topic' field
 * <p>
 * 结构化日志的基类，所有子类必须实现 topic() 并提供一个有效的值
 */
public abstract class XLogEvent {

    @NotNull
    @SerializedName("topic")
    private String topic = "";

    @Nullable
    @SerializedName("crid")
    private String crid = null;

    @Nullable
    @SerializedName("timestamp")
    private Date timestamp = null;

    @Nullable
    @SerializedName("project")
    private String project = null;

    @Nullable
    @SerializedName("path")
    private String path = null;

    @Nullable
    @SerializedName("path_digest")
    private String pathDigest = null;

    public XLogEvent() {
        setTopic(topic());
    }

    @NotNull
    public final String getTopic() {
        return topic;
    }

    public final void setTopic(@NotNull String topic) {
        this.topic = Objects.requireNonNull(Strings.normalize(topic), "event must have a 'topic'");
    }

    @Nullable
    public final String getCrid() {
        return crid;
    }

    public final void setCrid(@Nullable String crid) {
        this.crid = Strings.normalize(crid);
    }

    @Nullable
    public final Date getTimestamp() {
        return timestamp;
    }

    public final void setTimestamp(@Nullable Date timestamp) {
        this.timestamp = timestamp;
    }

    @Nullable
    public final String getProject() {
        return project;
    }

    public final void setProject(@Nullable String project) {
        this.project = Strings.normalize(project);
    }

    @Nullable
    public final String getPath() {
        return path;
    }

    public final void setPath(@Nullable String path) {
        path = Strings.normalize(path);
        this.path = path;

        /**
         * /api/v1/goods/detail/3656081/1/68/4403
         * /api/v1/goods/detail/3656082/1/68/4404
         * /api/v1/goods/detail/3656082/1/68/4405
         *
         *  以上请求判定为同类请求
         */
        if (path != null)
            this.pathDigest = Hex.md5(path.replaceAll("(\\b/)\\d+", "/:num"));
    }

    @Nullable
    public final String getPathDigest() {
        return pathDigest;
    }

    public final void setPathDigest(@Nullable String pathDigest) {
        this.pathDigest = Strings.normalize(pathDigest);
    }

    @NotNull
    abstract public String topic();

}
