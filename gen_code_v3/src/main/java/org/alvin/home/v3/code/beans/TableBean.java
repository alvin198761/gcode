package org.alvin.home.v3.code.beans;

import lombok.Data;

@Data
public class TableBean {
    private String tableName;// 表名
    private String comment;// 表名注释
    private String className;// 类名(首字母大写)
    private String varName ;//变量名 驼峰命名
    private String cnName;// 类中文名
}
