package org.alvin.home.v3.code.beans;

import lombok.Data;

/**
 * @author 唐植超
 * @date 2019/11/28
 * 带外键信息的属性名称
 */
@Data
public class FKFieldBean {
    //外键属性
    private String constraintCatalog; //
    private String constraintSchema; //
    private String constraintName; //外键名
    private String ftableCatalog; //
    private String ftableSchema;//数据库
    private String ftableName; //表
    private String fcolumnName;//字段
    private long fordinalPosition;
    private long positionInUniqueConstraint;
    private String referencedTableSchema; //关联数据库
    private String referencedTableName; //关联表
    private String referencedColumnName; //关联字段


    //列属性 冲突字段
    private String tableCatalog;
    private String tableSchema;
    private String tableName;
    private String columnName;
    private long ordinalPosition;
    //不冲突字段
    private String columnDefault;
    private String isNullable;
    private String dataType; //数据类型
    private String characterMaximumLength; //数据长度
    private String characterOctetLength;
    private String numericPrecision;
    private String numericScale;
    private String datetimePrecision;
    private String characterSetName; //字符集
    private String collationName;
    private String columnType;
    private String columnKey;
    private String extra;
    private String privileges;
    private String columnComment; //字段说明

}
