package org.alvin.code.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接操作类
 */
public class ConnectionUtils {

    private static String url ;
    private static String userName;
    private static String password;

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
    }

    /**
     * 获取数据库连接，这里目前没有做连接池处理
     * @return
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,userName,password);
    }


}
