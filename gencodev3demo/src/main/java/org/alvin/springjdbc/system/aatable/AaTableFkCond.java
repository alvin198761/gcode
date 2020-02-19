package org.alvin.springjdbc.system.aatable;

/**
* 类说明: 测试A--查询条件实体类
* @author 唐植超
* 生成日期 2020-02-19 23:05:35
**/
@lombok.Setter
@lombok.Getter
@lombok.experimental.Accessors(chain = true)
@io.swagger.annotations.ApiModel(value = "AaTableFkCond", description = "测试A--查询条件实体类-外键")
public class AaTableFkCond extends AaTableCond {

    /**
    * 方法说明: 拼加自定义条件
    **/
    @Override
    public void addCondition() {
		super.addCondition();
        }

    //属性
	
}