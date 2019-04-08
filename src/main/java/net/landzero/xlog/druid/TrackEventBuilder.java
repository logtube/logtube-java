package net.landzero.xlog.druid;

import com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import net.landzero.xlog.utils.Hex;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TrackEventBuilder {

    private TrackEvent event = new TrackEvent();

    private long startTime = System.currentTimeMillis();

    @NotNull
    private StatementProxy statement;

    @NotNull
    @Contract("_ -> this")
    public TrackEventBuilder setThrowable(@Nullable Throwable e) {
        if (e == null) {
            this.event.setError(null);
            return this;
        }
        this.event.setError(e.getMessage());
        return this;
    }

    @NotNull
    public TrackEvent build() {
        try {
            // duration
            this.event.setDuration(System.currentTimeMillis() - this.startTime);

            // databaseUrl
            Connection connection = statement.getConnectionProxy();
            if (connection instanceof ConnectionProxyImpl) {
                String url = ((ConnectionProxyImpl) connection).getDirectDataSource().getUrl();
                Pattern hostPattern = Pattern.compile("(?<=//)((\\w)+(\\.)*)+\\w+");
                Matcher hostMatcher = hostPattern.matcher(url);
                if (hostMatcher.find()) {
                    this.event.setDbHost(hostMatcher.group());
                }

                Pattern dbPattern = Pattern.compile("(?<=/)([a-zA-Z][a-zA-Z0-9_]*)");
                Matcher dbMatcher = dbPattern.matcher(url);

                //从jdbc:mysql://之后开始匹配
                if (dbMatcher.find(14)) {
                    this.event.setDbName(dbMatcher.group());
                }
            }

            String sql = statement.getLastExecuteSql();

            // sql digest. (?,?, ... ,?) 统一替换为 (:in_parameters)后再进行MD5加密
            String afterReplaceIn = sql.replaceAll("\\([\\?\\, ]*\\)", "(:in_parameters)");

            /**
             * -替换所有数字、单引号中的内容和双引号中的内容为:paramValue
             * -select a, b, c from table where a = 123 and b = 'abc'
             * -替换为select a, b, c from table where a = :paramValue and b = :paramValue
             */
            this.event.setSqlDigest(Hex.md5(Pattern.compile("(\\'[A-Za-z0-9_-]*\\')|(\\\"[A-Za-z0-9_-]*\\\")|\\d+")
                    .matcher(afterReplaceIn).replaceAll(":paramValue")));

            // originalSql
            String rawSql = sql;
            Map<Integer, JdbcParameter> paramsMap = statement.getParameters();
            if (paramsMap != null) {
                List<Object> values = paramsMap.entrySet().stream()
                        .sorted(Comparator.comparing(Map.Entry::getKey))
                        .map(e -> e.getValue().getValue())
                        .collect(Collectors.toList());
                for (Object v : values) {
                    rawSql = rawSql.replaceFirst("\\?", Matcher.quoteReplacement(v == null ? "" : v.toString()));
                }
            }
            this.event.setSql(rawSql);
        } catch (Throwable t) {
            this.setThrowable(t);
        }

        return this.event;
    }

    @NotNull
    @Contract("_ -> this")
    public TrackEventBuilder setStatement(@NotNull StatementProxy statement) {
        this.statement = statement;
        return this;
    }

    public void setAffectedRows(long affectedRows) {
        this.event.setAffectedRows(affectedRows);
    }

}
