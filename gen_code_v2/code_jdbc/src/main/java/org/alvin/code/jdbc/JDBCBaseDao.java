package org.alvin.code.jdbc;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import org.alvin.code.jdbc.beans.BaseCondition;
import org.alvin.code.jdbc.beans.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * jdbc 模板使用的父类
 */
public abstract class JDBCBaseDao {


    private static final int BATCH_SIZE = 2;


    public int update(String sql) throws SQLException {
        return this.update(sql, (Object[]) null);
    }

    /**
     * 增删改 ,这里不处理事务
     *
     * @param sql
     * @param params
     * @return
     */
    public int update(String sql, Object[] params) throws SQLException {
        PreparedStatement preparedStatement = null;
        Connection connection = ConnectionUtils.getConnection();
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, params);
            return preparedStatement.executeUpdate();
        } finally {
            this.close(preparedStatement);
        }
    }

    /**
     * 增删改,批量，100 一个批次，不处理事务
     *
     * @param sql
     * @param params
     * @return
     */
    public int[] update(String sql, List<Object[]> params) throws SQLException {
        PreparedStatement preparedStatement = null;
        List<int[]> fres = new ArrayList<>();
        Connection connection = ConnectionUtils.getConnection();
        try {
            preparedStatement = connection.prepareStatement(sql);
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
            //目前没有找到好的办法
            int[] res = new int[fresList.size()];
            for (int i = 0; i < fresList.size(); i++) {
                res[i] = fresList.get(i);
            }
            return res;
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
            if (c == null) {
                continue;
            }
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
        if (params == null || params.length == 0) {
            return;
        }
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    /**
     * 分页查询
     *
     * @param sql
     * @param condition
     * @return
     */
    protected Page<Map<String, Object>> queryPage(String sql, BaseCondition condition) throws SQLException {
        String countSQL = "SELECT count(1) FROM (" + sql + ") t";// 统计记录个数的SQL语句
        long rowCount = (Long) this.findCell(countSQL, (Object[]) null);
        int pageSize = condition.getSize();// 页大小
        int curPage = condition.getPage();// 当前页
        String listSql = sql + " LIMIT " + curPage * pageSize + "," + pageSize;// 查询分页数据列表的SQL语句
        List<Map<String, Object>> list = this.queryList(listSql, condition);
        return Page.map(list, condition.getPage(), condition.getSize(), rowCount);
    }


    /**
     * 查询返回map 列表
     *
     * @param sql
     * @param condition
     * @return
     * @throws SQLException
     */
    protected List<Map<String, Object>> queryList(String sql, BaseCondition condition) throws SQLException {
        Connection connection = ConnectionUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, condition.getArray());
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData data = resultSet.getMetaData();
            List<Map<String, Object>> list = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> item = new HashMap<>();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    String key = data.getColumnName(i);
                    Object value = resultSet.getObject(key);
                    item.put(key, value);
                }
                list.add(item);
            }
            return list;
        } finally {
            close(preparedStatement, resultSet);
        }
    }

    /**
     * 返回一个对象
     *
     * @param sql
     * @param condition
     * @return
     */
    protected Map<String, Object> findOne(String sql, BaseCondition condition) throws SQLException {
        Connection connection = ConnectionUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, condition.getArray());
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData data = resultSet.getMetaData();
            Map<String, Object> item = new HashMap<>();
            if (resultSet.next()) {
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    String key = data.getColumnName(i);
                    Object value = resultSet.getObject(key);
                    item.put(key, value);
                }
            }
            return item;
        } finally {
            close(preparedStatement, resultSet);
        }
    }

    /**
     * 返回单值
     *
     * @param sql
     * @param condition
     * @return
     */
    protected Object findCell(String sql, BaseCondition condition) throws SQLException {
        Connection connection = ConnectionUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, condition.getArray());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getObject(1);
            }
            throw new RuntimeException("empty found");
        } finally {
            close(preparedStatement, resultSet);
        }
    }

    /**
     * 返回一个对象
     *
     * @param sql
     * @param params
     * @return
     */
    protected Map<String, Object> findOne(String sql, Object[] params) throws SQLException {
        Connection connection = ConnectionUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, params);
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData data = resultSet.getMetaData();
            Map<String, Object> item = new HashMap<>();
            if (resultSet.next()) {
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    String key = data.getColumnName(i);
                    Object value = resultSet.getObject(key);
                    item.put(key, value);
                }
            }
            return item;
        } finally {
            close(preparedStatement, resultSet);
        }
    }

    /**
     * 返回单值
     *
     * @param sql
     * @param params
     * @return
     */
    protected Object findCell(String sql, Object[] params) throws SQLException {
        Connection connection = ConnectionUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, params);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getObject(1);
            }
            throw new RuntimeException("empty found");
        } finally {
            close(preparedStatement, resultSet);
        }
    }


    /**
     * 分页查询
     *
     * @param sql
     * @param condition
     * @param clazz
     * @return
     */
    protected <T> Page<T> queryPage(String sql, BaseCondition condition, Class<T> clazz) throws SQLException {
        Page<Map<String, Object>> page = this.queryPage(sql, condition);
        List<T> list = page.getContent().stream().map(item -> {
            JSONObject json = new JSONObject();
            item.forEach((k, v) -> {
                String key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, k);
                json.put(key, v);
            });
            return json.toJavaObject(clazz);
        }).collect(Collectors.toList());
        return page.map(list);
    }


    /**
     * 查询返回 列表
     *
     * @param sql
     * @param condition
     * @param clazz
     * @return
     * @throws SQLException
     */
    protected <T> List<T> queryList(String sql, BaseCondition condition, Class<T> clazz) throws SQLException {
        return this.queryList(sql, condition).stream().map(item -> {
            JSONObject json = new JSONObject();
            item.forEach((k, v) -> {
                String key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, k);
                if (v != null) {
                    json.put(key, v);
                }
            });
            return json.toJavaObject(clazz);
        }).collect(Collectors.toList());
    }

    /**
     * 返回一个对象
     *
     * @param sql
     * @param condition
     * @param clazz
     * @return
     */
    protected <T> T findOne(String sql, BaseCondition condition, Class<T> clazz) throws SQLException {
        Map<String, Object> data = this.findOne(sql, condition);
        JSONObject json = new JSONObject();
        data.forEach((k, v) -> {
            String key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, k);
            if (v != null) {
                json.put(key, v);
            }
        });
        return json.toJavaObject(clazz);
    }

    /**
     * 返回单值
     *
     * @param sql
     * @param condition
     * @param clazz
     * @return
     */
    protected <T> T findCell(String sql, BaseCondition condition, Class<T> clazz) throws SQLException {
        return (T) this.findCell(sql, condition);
    }

    /**
     * 返回一个对象
     *
     * @param sql
     * @param params
     * @return
     */
    protected <T> T findOne(String sql, Object[] params, Class<T> clazz) throws SQLException {
        Map<String, Object> data = this.findOne(sql, params);
        JSONObject json = new JSONObject();
        data.forEach((k, v) -> {
            String key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, k);
            json.put(key, v);
        });
        return json.toJavaObject(clazz);
    }


    /**
     * 返回单值
     *
     * @param sql
     * @param params
     * @param clazz
     * @return
     */
    protected <T> T findCell(String sql, Object[] params, Class<T> clazz) throws SQLException {
        return (T) this.findCell(sql, params);
    }


    /**
     * 增删改 ,这里不处理事务
     *
     * @param sql
     * @param params
     * @return
     */
    public int saveReturnPK(String sql, Object[] params, Map<String, Object> keyMap) throws SQLException {
        PreparedStatement preparedStatement = null;
        Connection connection = ConnectionUtils.getConnection();
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParams(preparedStatement, params);
            int res = preparedStatement.executeUpdate();
            //输出主键id
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                keyMap.put("id", resultSet.getObject(1));
            }
            return res;
        } finally {
            this.close(preparedStatement);
        }
    }


    /**
     * 增删改,批量，100 一个批次，不处理事务
     *
     * @param sql
     * @param params
     * @return
     */
    public int[] saveBatchReturnPk(String sql, List<Object[]> params, List<Map<String, Object>> keyMaps) throws SQLException {
        PreparedStatement preparedStatement = null;
        List<int[]> fres = new ArrayList<>();
        Connection connection = ConnectionUtils.getConnection();
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.size(); i++) {
                setParams(preparedStatement, params.get(i));
                preparedStatement.addBatch();
                if (i % BATCH_SIZE == 0 && i != 0) {
                    fres.add(preparedStatement.executeBatch());
                    //产生主键
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    getGenKey(keyMaps, resultSet);
                }
            }
            fres.add(preparedStatement.executeBatch());
            //产生主键
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            getGenKey(keyMaps, resultSet);
            //计算返回结果
            List<Integer> fresList = new ArrayList<>();
            for (int i = 0; i < fres.size(); i++) {
                for (int j : fres.get(i)) {
                    fresList.add(j);
                }
            }
            //目前没有找到好的办法
            int[] res = new int[fresList.size()];
            for (int i = 0; i < fresList.size(); i++) {
                res[i] = fresList.get(i);
            }
            return res;
        } finally {
            this.close(preparedStatement);
        }
    }

    private void getGenKey(List<Map<String, Object>> keyMaps, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Map<String, Object> resultKeyMap = new HashMap<>();
            resultKeyMap.put("id", resultSet.getObject(1));
            keyMaps.add(resultKeyMap);
        }
    }
}