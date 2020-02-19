package org.alvin.springjdbc.system.maintable;

/**
* 类说明: 主表--查询条件实体类
* @author 唐植超
* 生成日期 2020-02-19 23:05:35
**/
@lombok.Setter
@lombok.Getter
@lombok.experimental.Accessors(chain = true)
@io.swagger.annotations.ApiModel(value = "MainTableFk", description = "主表--实体类-外键")
public class MainTableFk extends MainTable{

    //数据库中的字段
    	@io.swagger.annotations.ApiModelProperty(value = "主键", dataType = "java.lang.Long")
    private Long aaTableId ;//  主键
    	@io.swagger.annotations.ApiModelProperty(value = "主键", dataType = "java.lang.Long")
    private Long bbTestId ;//  主键
    
    //此处可添加查询显示辅助字段

}