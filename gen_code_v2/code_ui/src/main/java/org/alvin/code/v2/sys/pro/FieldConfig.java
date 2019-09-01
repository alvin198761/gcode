package org.alvin.code.v2.sys.pro;

/**
 * 属性配置
 */

import lombok.Data;

import java.util.Objects;

@Data
public class FieldConfig {

    //属性名称
    private String name;
    //属性类型
    private String type;
    //引用类型对应的实体
    private String refName;
    //列备注
    private String remark;
    //数据长度
    private Integer length;
    //数据库类型（包含长度）
    private String sqlType;
    //是否为空
    private String isNull = "NULL";
    //是否主键
    private Boolean isKey;
    //扩展信息
    private String extra;

    //计算出来的数据
    private String bigName; //首字母大小
    private String lowerCamel; //单峰驼
    private String upperCamel; //双峰驼
    private String allTypeName;//类型全称
    private String colName; //列名称
    private String lowerName;//小写

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldConfig)) return false;
        FieldConfig that = (FieldConfig) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
