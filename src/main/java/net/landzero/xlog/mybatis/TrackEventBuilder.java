package net.landzero.xlog.mybatis;

import net.landzero.xlog.XLogEventBuilder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

public class TrackEventBuilder implements XLogEventBuilder<TrackEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackEventBuilder.class);

    private TrackEvent event = new TrackEvent();

    private long startTime = System.currentTimeMillis();

    @NotNull
    private static String compileParameterValue(@Nullable Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            return "'" + formatter.format((Date) obj) + "'";
        } else {
            return obj.toString();
        }
    }

    @Nullable
    private static String compileSQL(@Nullable Configuration configuration, @Nullable BoundSql boundSql) {
        if (configuration == null || boundSql == null) {
            return null;
        }
        Object object = boundSql.getParameterObject();
        List<ParameterMapping> mappings = boundSql.getParameterMappings();
        if (object == null || mappings == null) {
            return null;
        }
        String sql = boundSql.getSql();//.replaceAll("[\\s]+", " ");
        if (mappings.size() > 0) {
            TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
            if (registry.hasTypeHandler(object.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(compileParameterValue(object)));
            } else {
                MetaObject meta = configuration.newMetaObject(object);
                for (ParameterMapping mapping : mappings) {
                    String property = mapping.getProperty();
                    if (meta.hasGetter(property)) {
                        Object value = meta.getValue(property);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(compileParameterValue(value)));
                    } else if (boundSql.hasAdditionalParameter(property)) {
                        Object obj = boundSql.getAdditionalParameter(property);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(compileParameterValue(obj)));
                    }
                }
            }
        }
        return sql;
    }

    @Nullable
    private static String extractDatabaseURL(@NotNull Executor executor) {
        Transaction transaction = executor.getTransaction();
        if (transaction == null) {
            LOGGER.debug("executor.transaction is null");
            return null;
        }
        if (!(transaction instanceof SpringManagedTransaction)) {
            LOGGER.debug("executor.transaction is not a SpringManagedTransaction, but is " + transaction.getClass().getCanonicalName());
            return null;
        }
        SpringManagedTransaction managedTransaction = (SpringManagedTransaction) transaction;
        Field field = ReflectionUtils.findField(managedTransaction.getClass(), "dataSource", DataSource.class);
        if (field == null) {
            LOGGER.debug("executor.transaction does not have a field 'dataSource' with type 'javax.sql.DataSource'");
            return null;
        }
        ReflectionUtils.makeAccessible(field);
        DataSource dataSource = (DataSource) ReflectionUtils.getField(field, managedTransaction);
        if (dataSource == null) {
            LOGGER.debug("executor.transaction.dataSource is null");
            return null;
        }
        return extractDatabaseURL(dataSource);
    }

    @Nullable
    private static String extractDatabaseURL(@NotNull DataSource dataSource) {
        if (dataSource instanceof AbstractRoutingDataSource) {
            AbstractRoutingDataSource abstractRoutingDataSource = (AbstractRoutingDataSource) dataSource;
            Method method = ReflectionUtils.findMethod(abstractRoutingDataSource.getClass(), "determineTargetDataSource");
            if (method == null) {
                LOGGER.debug("executor.transaction.dataSource with type AbstractRoutingDataSource has no method named 'determineTargetDataSource'");
                return null;
            }
            ReflectionUtils.makeAccessible(method);
            DataSource dataSource1 = (DataSource) ReflectionUtils.invokeMethod(method, abstractRoutingDataSource);
            if (dataSource1 == null) {
                LOGGER.debug("executor.transaction.dataSource.determineTargetDataSource() returns null");
                return null;
            }
            return extractDatabaseURL(dataSource1);
        }
        Method method = ReflectionUtils.findMethod(dataSource.getClass(), "getUrl");
        if (method == null) {
            LOGGER.debug("dataSource has no method getUrl()");
            return null;
        }
        ReflectionUtils.makeAccessible(method);
        Object result = ReflectionUtils.invokeMethod(method, dataSource);
        if (result == null) {
            LOGGER.debug("dataSource.getUrl() returns null");
            return null;
        }
        return result.toString();
    }

    @NotNull
    @Contract("_ -> this")
    public TrackEventBuilder setInvocation(@NotNull Invocation invocation) {
        this.event.setMethod(invocation.getMethod().getName());
        Object tgt = invocation.getTarget();
        if (!(tgt instanceof Executor)) {
            LOGGER.debug("invocation.target is not a Executor");
            return this;
        }
        Executor executor = (Executor) tgt;
        if (invocation.getArgs().length == 0) {
            LOGGER.debug("invocation.args.length == 0");
            return this;
        }
        Object arg1 = invocation.getArgs()[0];
        if (!(arg1 instanceof MappedStatement)) {
            LOGGER.debug("invocation.args[0] is not a MappedStatement");
            return this;
        }
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        this.event.setSql(boundSql.getSql()); // this automatically set sqlDigest
        this.event.setDatabaseUrl(extractDatabaseURL(executor));
        try {
            this.event.setRawSql(compileSQL(mappedStatement.getConfiguration(), boundSql));
        } catch (Exception e) {
            this.event.setRawSql("Failed to compile:" + e.getMessage());
        }
        LOGGER.debug("reflection duration = %d ms", System.currentTimeMillis() - this.startTime);
        return this;
    }

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
    @Override
    public TrackEvent build() {
        this.event.setDuration(System.currentTimeMillis() - this.startTime);
        return this.event;
    }

}
