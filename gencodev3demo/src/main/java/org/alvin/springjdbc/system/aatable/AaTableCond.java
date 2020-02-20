package org.alvin.springjdbc.system.aatable;

/**
* 类说明: 测试A--查询条件实体类
* @author 唐植超
* 生成日期 2020-02-20 20:09:06
**/
@lombok.Setter
@lombok.Getter
@lombok.experimental.Accessors(chain = true)
@lombok.Builder	
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@io.swagger.annotations.ApiModel(value = "AaTableCond", description = "测试A--查询条件实体类")
public class AaTableCond extends org.alvin.code.gen.beans.BaseCondition {


    /**
    * 方法说明: 拼加自定义条件
    **/
    @Override
    public void addCondition() {
                    add(this.id , " AND t.id = ? " );
                            add(this.tip , " AND t.tip LIKE ? " ,3);
		            		add(this.date , " AND t.date = ? " );
		if(this.dateStart != null){
			add(org.alvin.utils.DateUtil.setStart(this.dateStart ), " AND t.date >= ? "); //日期开始
		}
		if(this.dateStart != null){
			add(org.alvin.utils.DateUtil.setEnd(this.dateEnd), " AND t.date <= ? "); //  日期结束
		}
                }

    //属性
    	@io.swagger.annotations.ApiModelProperty(value = "主键", dataType = "java.lang.Long")
    private  Long  id ;//  主键
		    	@io.swagger.annotations.ApiModelProperty(value = "提示文本", dataType = "java.lang.String")
    private  String  tip ;//  提示文本
		    	@io.swagger.annotations.ApiModelProperty(value = "日期", dataType = "java.util.Date")
    private  java.util.Date  date ;//  日期
					//日期查询范围
			@io.swagger.annotations.ApiModelProperty(value = "日期开始", dataType = "java.util.Date")
			private  java.util.Date  dateStart ;//  日期开始
			@io.swagger.annotations.ApiModelProperty(value = "日期结束", dataType = "java.util.Date")
			private  java.util.Date  dateEnd ;//  日期结束
		    	//其他常用条件字段
	@io.swagger.annotations.ApiModelProperty(value = "主键", dataType = "java.lang.List")
    private java.util.List<Long> idList;// 主键列表

}