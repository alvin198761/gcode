package org.alvin.code.v2.sys.pro;

import lombok.Data;

@Data
public class DbConfig {

    private  String driverName;
    private String url;
    private String username;
    private String password;
}
