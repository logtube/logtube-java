package net.landzero.xlog.mybatis;

import com.google.gson.annotations.SerializedName;
import net.landzero.xlog.XLogEvent;
import net.landzero.xlog.utils.Hex;
import net.landzero.xlog.utils.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrackEvent extends XLogEvent {

    @Override
    @NotNull
    public String topic() {
        return "x-mybatis-track";
    }

    @SerializedName("duration")
    private long duration;

    @SerializedName("method")
    @Nullable
    private String method = null;

    @Nullable
    @SerializedName("error")
    private String error = null;

    @Nullable
    @SerializedName("sql")
    private String sql = null;

    @Nullable
    @SerializedName("sql_digest")
    private String sqlDigest = null;

    @Nullable
    @SerializedName("raw_sql")
    private String rawSql = null;

    @Nullable
    @SerializedName("database_url")
    private String databaseUrl = null;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @NotNull
    public String getMethod() {
        return method;
    }

    @NotNull
    public void setMethod(@NotNull String method) {
        this.method = Strings.normalize(method);
    }

    @Nullable
    public String getError() {
        return error;
    }

    public void setError(@Nullable String error) {
        this.error = Strings.normalize(error);
    }

    @Nullable
    public String getSql() {
        return sql;
    }

    public void setSql(@Nullable String sql) {
        sql = Strings.normalize(sql);
        this.sql = sql;
        this.sqlDigest = Hex.md5(sql);
    }

    @Nullable
    public String getSqlDigest() {
        return sqlDigest;
    }

    public void setSqlDigest(@Nullable String sqlDigest) {
        this.sqlDigest = Strings.normalize(sqlDigest);
    }

    @Nullable
    public String getRawSql() {
        return rawSql;
    }

    public void setRawSql(@Nullable String rawSql) {
        this.rawSql = Strings.normalize(rawSql);
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(@Nullable String databaseUrl) {
        this.databaseUrl = Strings.normalize(databaseUrl);
    }

}
