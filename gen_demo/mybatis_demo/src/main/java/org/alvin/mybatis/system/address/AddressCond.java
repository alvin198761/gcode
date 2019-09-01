package org.alvin.mybatis.system.address;

    
    
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.alvin.code.gen.beans.BaseCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/**
* @类说明: 收寄信息--查询条件实体类
* @author: 唐植超
* @date : 2019-08-09 13:36:03
**/
@Setter
@Getter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "AddressCond", description = "收寄信息查询条件实体")
public class AddressCond extends BaseCondition {


    /**
    * @方法说明: 拼加自定义条件
    **/
    @Override
    public void addCondition() {
                    add(id , " AND t.id = ? " );
                            add(type , " AND t.type = ? " );
                            add(targetPhone , " AND t.target_phone LIKE ? " ,3);
                            add(targetAddr , " AND t.target_addr LIKE ? " ,3);
                            add(targetName , " AND t.target_name LIKE ? " ,3);
                            add(targetId , " AND t.target_id = ? " );
                            add(remark , " AND t.remark LIKE ? " ,3);
                 add(ids, "AND t.id IN ");
    }

    //feilds
        @ApiModelProperty(value = "主键", dataType = "Long")
    private  Long  id ;//  主键
        @ApiModelProperty(value = "类型", dataType = "Byte")
    private  Byte  type ;//  类型
        @ApiModelProperty(value = "电话", dataType = "String")
    private  String  targetPhone ;//  电话
        @ApiModelProperty(value = "地址", dataType = "String")
    private  String  targetAddr ;//  地址
        @ApiModelProperty(value = "姓名", dataType = "String")
    private  String  targetName ;//  姓名
        @ApiModelProperty(value = "收寄方id", dataType = "Long")
    private  Long  targetId ;//  收寄方id
        @ApiModelProperty(value = "备注", dataType = "String")
    private  String  remark ;//  备注
    
    private List<Long> ids;// 主键列表
}