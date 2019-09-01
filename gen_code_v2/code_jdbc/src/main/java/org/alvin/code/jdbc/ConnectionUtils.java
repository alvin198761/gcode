package org.alvin.code.jdbc;

import org.alvin.mini_inject.InjectContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接操作类
 */
public class ConnectionUtils {

    private static String url;
    private static String userName;
    private static String password;
    private static ThreadLocal<Connection> local = new ThreadLocal<>();

    /**
     * 初始化连接
     *
     * @param dirverName
     * @param url
     * @param userName
     * @param password
     */
    public static void init(String dirverName, String url, String userName, String password) throws ClassNotFoundException {
        Class.forName(dirverName);
        ConnectionUtils.url = url;
        ConnectionUtils.userName = userName;
        ConnectionUtils.password = password;
        InjectContext.install(new MiniJDBCPlugin());
    }

    /**
     * 获取数据库连接，这里目前没有做连接池处理
     *
     * @return
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = local.get();
        if (connection != null) {
            return connection;
        }
        connection = DriverManager.getConnection(url, userName, password);
        local.set(connection);
        return connection;
    }

    /**
     * 开启事务
     *
     * @param connection
     * @throws SQLException
     */
    public static void beginTransction(Connection connection) throws SQLException {
        if (connection != null) {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
        }
    }

    /**
     * 关闭事务
     *
     * @param connection
     * @throws SQLException
     */
    public static void endTransction(Connection connection) throws SQLException {
        if (connection != null) {
            if (!connection.getAutoCommit()) {
                connection.close();
            }
        }
    }

    /**
     * 事务回滚
     *
     * @param connection
     */
    public static void rollback(Connection connection) throws SQLException {
        if (connection != null) {
            connection.rollback();
        }
    }

    /**
     * 关闭数据库连接
     *
     * @param connection
     * @throws SQLException
     */
    public static void close(Connection connection) throws SQLException {
        if (connection != null) {
            local.remove();
            connection.close();
        }
    }


}
