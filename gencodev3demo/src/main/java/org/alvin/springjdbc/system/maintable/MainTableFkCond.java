package org.alvin.springjdbc.system.maintable;

/**
* 类说明: 主表--查询条件实体类
* @author 唐植超
* 生成日期 2020-02-19 23:05:35
**/
@lombok.Setter
@lombok.Getter
@lombok.experimental.Accessors(chain = true)
@io.swagger.annotations.ApiModel(value = "MainTableFkCond", description = "主表--查询条件实体类-外键")
public class MainTableFkCond extends MainTableCond {

    /**
    * 方法说明: 拼加自定义条件
    **/
    @Override
    public void addCondition() {
		super.addCondition();
            			add(this.aaTableId , " AND aaTable.id = ? " );
                    			add(this.bbTestId , " AND bbTest.id = ? " );
                }

    //属性
	        @io.swagger.annotations.ApiModelProperty(value = "主键", dataType = "java.lang.Long")
    private  Long  aaTableId  ;//  主键
		//日期查询范围
		            @io.swagger.annotations.ApiModelProperty(value = "主键", dataType = "java.lang.Long")
    private  Long  bbTestId  ;//  主键
		//日期查询范围
		    
}