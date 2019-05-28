package org.alvin.code.jdbc;

import org.alvin.code.jdbc.beans.BaseCondition;
import org.alvin.code.jdbc.beans.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jdbc 模板使用的父类
 */
public abstract class JDBCBaseDao {


    private static final int BATCH_SIZE = 100;

    /**
     * 增删改 ,这里不处理事务
     *
     * @param sql
     * @param params
     * @param connection
     * @return
     */
    public int update(String sql, Object[] params, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionUtils.getConnection();
            connection.setAutoCommit(false); //不主动提交事务
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, params);
            return preparedStatement.executeUpdate();
        } catch (Exception ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw ex;
        } finally {
            this.close(preparedStatement);
        }
    }

    /**
     * 增删改,批量，100 一个批次，不处理事务
     *
     * @param sql
     * @param params
     * @param connection
     * @return
     */
    public List<Integer> update(String sql, List<Object[]> params, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        List<int[]> fres = new ArrayList<>();
        try {
            connection = ConnectionUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for (int i = 0; i < params.size(); i++) {
                setParams(preparedStatement, params.get(i));
                preparedStatement.addBatch();
                if (i % BATCH_SIZE == 0 && i != 0) {
                    fres.add(preparedStatement.executeBatch());
                }
            }
            fres.add(preparedStatement.executeBatch());
            List<Integer> fresList = new ArrayList<>();
            for (int i = 0; i < fres.size(); i++) {
                for (int j : fres.get(i)) {
                    fresList.add(j);
                }
            }
            return fresList;
        } catch (Exception ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw ex;
        } finally {
            this.close(preparedStatement);
        }
    }

    /**
     * 关闭连接
     *
     * @param closeables
     */
    protected void close(AutoCloseable... closeables) {
        for (AutoCloseable c : closeables) {
            try {
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 设置参数
     *
     * @param preparedStatement
     * @param params
     * @throws SQLException
     */
    protected void setParams(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    /**
     * 分页查询
     *
     * @param sql
     * @param condition
     * @param connection
     * @return
     */
    protected Page<Map<String, Object>> queryPage(String sql, BaseCondition condition, Connection connection) throws SQLException {
        String countSQL = "SELECT count(1) FROM (" + sql + ") t";// 统计记录个数的SQL语句
        long rowCount = (Long) this.findCell(countSQL, (Object[]) null, connection);
        int pageSize = condition.getSize();// 页大小
        int curPage = condition.getPage();// 当前页
        String listSql = sql + " LIMIT " + curPage * pageSize + "," + pageSize;// 查询分页数据列表的SQL语句
        List<Map<String, Object>> list = this.queryList(listSql, condition, connection);
        return Page.map(list, condition.getPage(), condition.getSize(), rowCount);
    }


    /**
     * 查询返回map 列表
     *
     * @param sql
     * @param condition
     * @param connection
     * @return
     * @throws SQLException
     */
    protected List<Map<String, Object>> queryList(String sql, BaseCondition condition, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, condition.getArray());
            resultSet = preparedStatement.getResultSet();
            ResultSetMetaData data = resultSet.getMetaData();
            List<Map<String, Object>> list = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> item = new HashMap<>();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    String key = data.getCatalogName(i);
                    Object value = resultSet.getObject(key);
                    item.put(key, value);
                }
                list.add(item);
            }
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            close(preparedStatement, resultSet);
        }
    }

    /**
     * 返回一个对象
     *
     * @param sql
     * @param condition
     * @param connection
     * @return
     */
    protected Map<String, Object> findOne(String sql, BaseCondition condition, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, condition.getArray());
            resultSet = preparedStatement.getResultSet();
            ResultSetMetaData data = resultSet.getMetaData();
            Map<String, Object> item = new HashMap<>();
            if (resultSet.next()) {
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    String key = data.getCatalogName(i);
                    Object value = resultSet.getObject(key);
                    item.put(key, value);
                }
            }
            return item;
        } catch (Exception e) {
            throw e;
        } finally {
            close(preparedStatement, resultSet);
        }
    }

    /**
     * 返回单值
     *
     * @param sql
     * @param condition
     * @param connection
     * @return
     */
    protected Object findCell(String sql, BaseCondition condition, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, condition.getArray());
            resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getObject(1);
            }
            throw new RuntimeException("empty found");
        } catch (Exception e) {
            throw e;
        } finally {
            close(preparedStatement, resultSet);
        }
    }

    /**
     * 返回一个对象
     *
     * @param sql
     * @param params
     * @param connection
     * @return
     */
    protected Map<String, Object> findOne(String sql, Object[] params, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, params);
            resultSet = preparedStatement.getResultSet();
            ResultSetMetaData data = resultSet.getMetaData();
            Map<String, Object> item = new HashMap<>();
            if (resultSet.next()) {
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    String key = data.getCatalogName(i);
                    Object value = resultSet.getObject(key);
                    item.put(key, value);
                }
            }
            return item;
        } catch (Exception e) {
            throw e;
        } finally {
            close(preparedStatement, resultSet);
        }
    }

    /**
     * 返回单值
     *
     * @param sql
     * @param params
     * @param connection
     * @return
     */
    protected Object findCell(String sql, Object[] params, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, params);
            resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getObject(1);
            }
            throw new RuntimeException("empty found");
        } catch (Exception e) {
            throw e;
        } finally {
            close(preparedStatement, resultSet);
        }
    }

}