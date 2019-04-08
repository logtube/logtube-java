package io.github.logtube.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * mybatis interceptor for record and logging mybatis query / update operations
 * <p>
 * mybatis 拦截器，用来记录 query / update 操作性能
 */
@Intercepts({
        @Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(method = "update", type = Executor.class, args = {MappedStatement.class, Object.class})
})
public class LogtubeMybatisFilter implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MybatisTrackEventCommitter event = new MybatisTrackEventCommitter().setInvocation(invocation);
        try {
            return invocation.proceed();
        } catch (Throwable e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
