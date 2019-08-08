package ${pName}.sys.${low} ;

#foreach($importItem in $importList)
    $importItem
#end
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.alvin.code.gen.beans.BaseCondition;
/**
* @类说明: ${cName}--查询条件实体类
* @author: ${auth}
* @date : ${time}
**/
@Setter
@Getter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "${clsUpp}", description = "${cName}查询条件实体")
public class ${clsUpp}Cond extends BaseCondition {


    /**
    * @方法说明: 拼加自定义条件
    **/
    @Override
    public void addCondition() {
    #foreach ($fie in $fList)
        #if($fie.type == "String")
        add(${fie.name} , " AND t.${fie.name} LIKE ? " ,3);
        #else
        add(${fie.name} , " AND t.${fie.name} = ? " );
        #end
    #end
    // add(ids, "AND t.${idName} IN ");");
    }

    //feilds
    #foreach ($fie in $fList)
    @ApiModelProperty(value = "${fie.comment}", dataType = "${fie.type}")
    private  ${fie.type}  ${fie.name} ;//  ${fie.comment}
    #end

    //private List<${idType}> ids;// 主键列表
}