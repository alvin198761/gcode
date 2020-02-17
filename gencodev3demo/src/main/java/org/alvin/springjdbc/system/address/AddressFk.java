package org.alvin.springjdbc.system.address;

/**
* 类说明: 收寄信息--查询条件实体类
* @author 唐植超
* 生成日期 2020-02-17 22:40:15
**/
@lombok.Setter
@lombok.Getter
@lombok.experimental.Accessors(chain = true)
@lombok.Builder	
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@io.swagger.annotations.ApiModel(value = "AddressFk", description = "收寄信息--实体类-外键")
public class AddressFk extends Address{

    //数据库中的字段
    	@io.swagger.annotations.ApiModelProperty(value = "title", dataType = "java.lang.String")
    private String adminMenuTitle ;//  title
    	@io.swagger.annotations.ApiModelProperty(value = "名称", dataType = "java.lang.String")
    private String adminRoleName ;//  名称
    
    //此处可添加查询显示辅助字段

}