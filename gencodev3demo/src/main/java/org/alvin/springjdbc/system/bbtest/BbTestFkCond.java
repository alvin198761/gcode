package org.alvin.springjdbc.system.bbtest;

/**
* 类说明: 测试表B--查询条件实体类
* @author 唐植超
* 生成日期 2020-02-20 20:09:06
**/
@lombok.Setter
@lombok.Getter
@lombok.experimental.Accessors(chain = true)
@io.swagger.annotations.ApiModel(value = "BbTestFkCond", description = "测试表B--查询条件实体类-外键")
public class BbTestFkCond extends BbTestCond {

    /**
    * 方法说明: 拼加自定义条件
    **/
    @Override
    public void addCondition() {
		super.addCondition();
        }

    //属性
	
}