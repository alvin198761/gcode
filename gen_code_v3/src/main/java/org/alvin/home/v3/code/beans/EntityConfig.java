package org.alvin.home.v3.code.beans;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 实体类配置
 */
@Data
public class EntityConfig {

    private Byte type = 0;//0是实体类，1是常量字典
    //类名
    private String name;
    //id 属性名称
    private String idName;
    //显示文本的属性名称
    private String labelName;
    //备注
    private String remark;
    //所有属性
    private List<FieldConfig> fields;
    //变量值
    private EntityVar var = new EntityVar();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityConfig)) return false;
        EntityConfig that = (EntityConfig) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
