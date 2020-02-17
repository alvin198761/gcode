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
@io.swagger.annotations.ApiModel(value = "AddressCond", description = "收寄信息--查询条件实体类")
public class AddressCond extends org.alvin.code.gen.beans.BaseCondition {


    /**
    * 方法说明: 拼加自定义条件
    **/
    @Override
    public void addCondition() {
                    add(this.id , " AND t.id = ? " );
                            add(this.type , " AND t.type = ? " );
                            add(this.targetPhone , " AND t.target_phone LIKE ? " ,3);
		                    add(this.targetAddr , " AND t.target_addr LIKE ? " ,3);
		                    add(this.targetName , " AND t.target_name LIKE ? " ,3);
		                    add(this.targetId , " AND t.target_id = ? " );
                            add(this.remark , " AND t.remark LIKE ? " ,3);
		        }

    //属性
    	@io.swagger.annotations.ApiModelProperty(value = "主键", dataType = "java.lang.Long")
    private  Long  id ;//  主键
		    	@io.swagger.annotations.ApiModelProperty(value = "类型", dataType = "java.lang.Byte")
    private  Byte  type ;//  类型
		    	@io.swagger.annotations.ApiModelProperty(value = "电话", dataType = "java.lang.String")
    private  String  targetPhone ;//  电话
		    	@io.swagger.annotations.ApiModelProperty(value = "地址", dataType = "java.lang.String")
    private  String  targetAddr ;//  地址
		    	@io.swagger.annotations.ApiModelProperty(value = "姓名", dataType = "java.lang.String")
    private  String  targetName ;//  姓名
		    	@io.swagger.annotations.ApiModelProperty(value = "收寄方id", dataType = "java.lang.Long")
    private  Long  targetId ;//  收寄方id
		    	@io.swagger.annotations.ApiModelProperty(value = "备注", dataType = "java.lang.String")
    private  String  remark ;//  备注
		    	//其他常用条件字段
	@io.swagger.annotations.ApiModelProperty(value = "主键", dataType = "java.lang.List")
    private java.util.List<Long> idList;// 主键列表

}