package io.github.logtube.druid;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class LogtubeDruidFilter extends FilterEventAdapter {

    
    @Override
    public ResultSetProxy statement_executeQuery(FilterChain chain, StatementProxy statement, String sql)
            throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            return super.statement_executeQuery(chain, statement, sql);
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public boolean statement_execute(FilterChain chain, StatementProxy statement, String sql) throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            return super.statement_execute(chain, statement, sql);
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public boolean preparedStatement_execute(FilterChain chain, PreparedStatementProxy statement) throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            return super.preparedStatement_execute(chain, statement);
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public ResultSetProxy preparedStatement_executeQuery(FilterChain chain, PreparedStatementProxy statement)
            throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            ResultSetProxy resultSetProxy = super.preparedStatement_executeQuery(chain, statement);
            resultSetProxy.last();
            event.setAffectedRows(resultSetProxy.getRow());
            resultSetProxy.beforeFirst();
            return resultSetProxy;
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public int preparedStatement_executeUpdate(FilterChain chain, PreparedStatementProxy statement) throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            int affectedRows = super.preparedStatement_executeUpdate(chain, statement);
            event.setAffectedRows(affectedRows);
            return affectedRows;
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public boolean statement_execute(FilterChain chain, StatementProxy statement, String sql, int autoGeneratedKeys)
            throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            return super.statement_execute(chain, statement, sql, autoGeneratedKeys);
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public boolean statement_execute(FilterChain chain, StatementProxy statement, String sql, int[] columnIndexes)
            throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            return super.statement_execute(chain, statement, sql, columnIndexes);
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public boolean statement_execute(FilterChain chain, StatementProxy statement, String sql, String[] columnNames)
            throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            return super.statement_execute(chain, statement, sql, columnNames);
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public int statement_executeUpdate(FilterChain chain, StatementProxy statement, String sql) throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            int affectedRows = super.statement_executeUpdate(chain, statement, sql);
            event.setAffectedRows(affectedRows);
            return affectedRows;
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public int statement_executeUpdate(FilterChain chain, StatementProxy statement, String sql, int autoGeneratedKeys)
            throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            int affectedRows = super.statement_executeUpdate(chain, statement, sql, autoGeneratedKeys);
            event.setAffectedRows(affectedRows);
            return affectedRows;
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public int statement_executeUpdate(FilterChain chain, StatementProxy statement, String sql, int[] columnIndexes)
            throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            int affectedRows = super.statement_executeUpdate(chain, statement, sql, columnIndexes);
            event.setAffectedRows(affectedRows);
            return affectedRows;
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public int statement_executeUpdate(FilterChain chain, StatementProxy statement, String sql, String[] columnNames)
            throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            int affectedRows = super.statement_executeUpdate(chain, statement, sql, columnNames);
            event.setAffectedRows(affectedRows);
            return affectedRows;
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }

    @Override
    public int[] statement_executeBatch(FilterChain chain, StatementProxy statement) throws SQLException {
        DruidTrackEventCommitter event = new DruidTrackEventCommitter().setStatement(statement);
        try {
            int[] result = super.statement_executeBatch(chain, statement);
            int affectedRows = 0;
            for (int i : result)
            {
                affectedRows += i;
            }
            event.setAffectedRows(affectedRows);
            return result;
        } catch (SQLException e) {
            event.setThrowable(e);
            throw e;
        } finally {
            event.commit();
        }
    }
}
