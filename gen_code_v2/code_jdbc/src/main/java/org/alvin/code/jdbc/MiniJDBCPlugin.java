package org.alvin.code.jdbc;

import org.alvin.code.jdbc.annotations.JDBCTransction;
import org.alvin.mini_inject.InjectContext;
import org.alvin.mini_inject.plugins.MiniPlugin;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class MiniJDBCPlugin implements MiniPlugin {
//---------------
    @Override
    public String name() {
        return "MiniJDBCPlugin_gen";
    }

    /**
     * jdbc事务 代理
     *
     * @param clazzList
     * @return
     */
    public Map<Class<?>, Object> doRun(List<Class> clazzList) {
        Map<Class<?>, Object> res = new HashMap<>();
        clazzList.stream().filter(item -> {
            for (Method method : item.getDeclaredMethods()) {
                JDBCTransction transction = method.getAnnotation(JDBCTransction.class);
                if (transction != null) {
                    return true;
                }
            }
            return false;
        }).map(item -> new JDBCProxy().newProxyInstance(InjectContext.getInstance(item))).forEach(item -> {
            res.put(item.getClass(), item);
        });
        return res;
    }
}
