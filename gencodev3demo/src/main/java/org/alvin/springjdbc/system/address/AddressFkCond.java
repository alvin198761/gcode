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
@io.swagger.annotations.ApiModel(value = "AddressFkCond", description = "收寄信息--查询条件实体类-外键")
public class AddressFkCond extends AddressCond {

    /**
    * 方法说明: 拼加自定义条件
    **/
    @Override
    public void addCondition() {
		super.addCondition();
            			add(this.adminMenuTitle , " AND adminMenu.title LIKE ? " ,3);
		            			add(this.adminRoleName , " AND adminRole.name LIKE ? " ,3);
		        }

    //属性
	        @io.swagger.annotations.ApiModelProperty(value = "title", dataType = "java.lang.String")
    private  String  adminMenuTitle  ;//  title
		//日期查询范围
		            @io.swagger.annotations.ApiModelProperty(value = "名称", dataType = "java.lang.String")
    private  String  adminRoleName  ;//  名称
		//日期查询范围
		    
}