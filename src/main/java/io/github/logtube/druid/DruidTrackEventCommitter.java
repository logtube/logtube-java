package io.github.logtube.druid;

import com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.sql.SQLUtils;
import io.github.logtube.Logtube;
import io.github.logtube.core.IMutableEvent;
import io.github.logtube.utils.Hex;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DruidTrackEventCommitter {

    private final IMutableEvent event = Logtube.getLogger(DruidTrackEventCommitter.class).topic("x-druid-track");

    private final long startTime = System.currentTimeMillis();

    private final Pattern hostPattern = Pattern.compile("(?<=//)((\\w)+(\\.)*)+\\w+");

    private final Pattern dbPattern = Pattern.compile("(?<=/)([a-zA-Z][a-zA-Z0-9_]*)");

    private final Pattern digestPattern = Pattern.compile("(\\'[A-Za-z0-9_-]*\\')|(\\\"[A-Za-z0-9_-]*\\\")|\\d+");

    private @NotNull StatementProxy statement;

    @Contract("_ -> this")
    public @NotNull DruidTrackEventCommitter setThrowable(@Nullable Throwable e) {
        if (e == null) {
            this.event.extra("error", null);
            return this;
        }
        this.event.extra("error", e.getMessage());
        return this;
    }

    public void commit() {
        try {
            // duration
            this.event.xDuration(System.currentTimeMillis() - this.startTime);

            // databaseUrl
            Connection connection = statement.getConnectionProxy();
            if (connection instanceof ConnectionProxyImpl) {
                String url = ((ConnectionProxyImpl) connection).getDirectDataSource().getUrl();
                Matcher hostMatcher = hostPattern.matcher(url);
                if (hostMatcher.find()) {
                    this.event.extra("db_host", hostMatcher.group());
                }

                Matcher dbMatcher = dbPattern.matcher(url);

                //从jdbc:mysql://之后开始匹配
                if (dbMatcher.find(14)) {
                    this.event.extra("db_name", dbMatcher.group());
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
            this.event.extra("sql_digest", Hex.md5(digestPattern
                    .matcher(afterReplaceIn).replaceAll(":paramValue")));

            // originalSql
            String rawSql = sql;
            int parametersSize = statement.getParametersSize();
            if (parametersSize > 0) {
                List<Object> parameters = new ArrayList<>(parametersSize);
                for (int i = 0; i < parametersSize; ++i) {
                    JdbcParameter jdbcParam = statement.getParameter(i);
                    parameters.add(jdbcParam != null
                            ? jdbcParam.getValue()
                            : null);
                }
                String dbType = statement.getConnectionProxy().getDirectDataSource().getDbType();
                rawSql = SQLUtils.format(sql, dbType, parameters, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION);
            }
            this.event.extra("sql", rawSql);
        } catch (Throwable t) {
            setThrowable(t);
        }

        this.event.commit();
    }

    @Contract("_ -> this")
    public @NotNull DruidTrackEventCommitter setStatement(@NotNull StatementProxy statement) {
        this.statement = statement;
        return this;
    }

    public void setAffectedRows(long affectedRows) {
        this.event.extra("affected_rows", affectedRows);
    }

}
