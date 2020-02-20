package org.alvin.springjdbc.system.bbtest;

/**
* 类说明: 测试表B--查询条件实体类
* @author 唐植超
* 生成日期 2020-02-20 20:09:06
**/
@lombok.Setter
@lombok.Getter
@lombok.experimental.Accessors(chain = true)
@lombok.Builder	
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@io.swagger.annotations.ApiModel(value = "BbTestCond", description = "测试表B--查询条件实体类")
public class BbTestCond extends org.alvin.code.gen.beans.BaseCondition {


    /**
    * 方法说明: 拼加自定义条件
    **/
    @Override
    public void addCondition() {
                    add(this.id , " AND t.id = ? " );
                            add(this.title , " AND t.title LIKE ? " ,3);
		        }

    //属性
    	@io.swagger.annotations.ApiModelProperty(value = "主键", dataType = "java.lang.Long")
    private  Long  id ;//  主键
		    	@io.swagger.annotations.ApiModelProperty(value = "标题", dataType = "java.lang.String")
    private  String  title ;//  标题
		    	//其他常用条件字段
	@io.swagger.annotations.ApiModelProperty(value = "主键", dataType = "java.lang.List")
    private java.util.List<Long> idList;// 主键列表

}