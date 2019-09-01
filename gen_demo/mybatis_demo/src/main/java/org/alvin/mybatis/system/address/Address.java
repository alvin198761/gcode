package org.alvin.mybatis.system.address;

    
    
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
* @类说明: 收寄信息--实体类
* @author: 唐植超
* @date : 2019-08-09 13:36:03
**/
@Setter
@Getter
@Accessors(chain = true)
@Builder	
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Address", description = "收寄信息实体")
public class Address {

    //数据库中的字段
                @ApiModelProperty(value = "主键", dataType = "Long")
    private Long id ;//  主键
                @ApiModelProperty(value = "类型", dataType = "Byte")
    private Byte type ;//  类型
                @ApiModelProperty(value = "电话", dataType = "String")
    private String targetPhone ;//  电话
                @ApiModelProperty(value = "地址", dataType = "String")
    private String targetAddr ;//  地址
                @ApiModelProperty(value = "姓名", dataType = "String")
    private String targetName ;//  姓名
                @ApiModelProperty(value = "收寄方id", dataType = "Long")
    private Long targetId ;//  收寄方id
                @ApiModelProperty(value = "备注", dataType = "String")
    private String remark ;//  备注
    
    //此处可添加查询显示辅助字段

}