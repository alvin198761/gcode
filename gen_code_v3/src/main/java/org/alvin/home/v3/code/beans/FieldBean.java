package org.alvin.home.v3.code.beans;

import lombok.Data;

@Data
public class FieldBean {
    private String name;// 字段名
    private String comment;// 注释
    private String dbType; //数据库类型
    private Integer length; //字符长度

    private String bigName; //首字母大小
    private String lowerCamel; //单峰驼
    private String upperCamel; //双峰驼

    private String type;// 数据类型
    private String allTypeName;//类型全称
    private String mbDbType;//mybatis 数据库类型
    private String lowerUnderscore;//小写下划线
    //v3 新增的字段
    //外键
    private boolean isFk; //是否外键
    private String fkTable; //外键对应的表
    private String fkCol; //外键对应的列

    //主键
    private boolean isPk = false; //是否主键 主键可以多个
    //
    private String classVarName; //类名称

}
