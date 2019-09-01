package org.alvin.code.jdbc;

import org.alvin.code.jdbc.annotations.JDBCTransction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

public class JDBCProxy implements InvocationHandler {

    private Object obj;

    public Object newProxyInstance(Object obj) {
        this.obj = obj;
        return Proxy.newProxyInstance(this.obj.getClass().getClassLoader(), this.obj.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        JDBCTransction transction = method.getAnnotation(JDBCTransction.class);
        Connection connection = ConnectionUtils.getConnection();
        if (transction != null && !transction.readOnly()) {
            try {
                ConnectionUtils.beginTransction(connection);
                Object res = method.invoke(proxy, args);
                ConnectionUtils.endTransction(connection);
                return res;
            } catch (Throwable e) {
                ConnectionUtils.rollback(connection);
            } finally {
                ConnectionUtils.close(connection);
            }
        }
        return method.invoke(proxy, args);
    }
}
