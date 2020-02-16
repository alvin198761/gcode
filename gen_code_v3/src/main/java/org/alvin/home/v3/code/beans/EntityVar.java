package org.alvin.home.v3.code.beans;

import lombok.Data;

/**
 * 实体变量值
 */
@Data
public class EntityVar {

    //计算出来的名字
    private String lowerCamel; //单峰驼
    private String upperCamel; //双峰驼
    private String allTypeName;//类型全称
    private String tableName; //列名称
    private String packageName; //包名
    //需要拼装的数据
    private String selectItems;//查询用的基本属性列表
    private String insertFields;//输入插入用的字段
    private String insertValuesFields;//冒号占位符插入字段
    private String replaceValuesFields;//问号占位符
    private String updateFields; //

    private String caseMapper;//
    private String paramsFields;//
    private String updateParams;//

    private FieldConfig id; //主键
}
