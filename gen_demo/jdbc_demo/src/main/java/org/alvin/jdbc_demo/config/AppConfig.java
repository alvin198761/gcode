package org.alvin.jdbc_demo.config;

import org.alvin.code.jdbc.ConnectionUtils;
import org.alvin.mini_inject.annotations.MiniComponent;
import org.alvin.mini_inject.annotations.MiniRun;
import org.alvin.mini_inject.annotations.MiniValue;

@MiniComponent
public class AppConfig {

    @MiniValue("driverClassName")
    private String driverClassName;
    @MiniValue("url")
    private String url;
    @MiniValue("username")
    private String username;
    @MiniValue("password")
    private String password;

    @MiniRun
    public void initJDBC() {
        try {
            ConnectionUtils.init(driverClassName, url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }




}
