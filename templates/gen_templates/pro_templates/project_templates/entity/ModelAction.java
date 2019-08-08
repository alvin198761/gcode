package ${pName}.sys.${low} ;
import java.util.List;
import org.alvin.code.gen.utils.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
/**
* @类说明: ${cName}--数据控制器层
* @author: ${auth}
* @date : ${time}
**/
@RestController
@RequestMapping("${lowUpp}")
public class ${clsUpp}Action {

	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private ${clsUpp}Service service; //注入${cName}数据逻辑层

    /**
    * @方法说明：  新增[${cName}]记录
    */
	@PostMapping("save")
	public int save(@RequestBody ${clsUpp} ${lowUpp}) {
		return service.save(${lowUpp});
	}

    /**
    * @方法说明： 删除${cName}记录(多条)
    */
	@RequestMapping("delete")
	public int delete(@RequestParam("id") ${idType} id) {
		return service.delete(new ${idType} [] {id});
	}

    /**
    * @方法说明： 修改${cName}记录
    */
	@PostMapping("update")
	public int update(@RequestBody ${clsUpp} ${lowUpp}) {
		return service.update(${lowUpp});
	}

    /**
    * @方法说明： 按条件查询分页${cName}列表
    */
	@PostMapping("queryPage")
	public Page<${clsUpp}> queryPage(@RequestBody ${clsUpp}Cond cond ){
		return service.queryPage(cond);
	}

    /**
    * @方法说明： 按条件查询不分页${cName}列表
    */
	@PostMapping("queryList")
	public List<${clsUpp}> queryList(@RequestBody ${clsUpp}Cond cond ){
		return service.queryList(cond);
	}

    /**
    * @方法说明： 按主键查单个${cName}记录
    */
	@RequestMapping("findById")
	public ${clsUpp} findById(@RequestParam("id") ${idType} id) {
		return service.findById(id);
	}

    /**
    * @方法说明： 按条件查询${cName}记录个数
    */
	@PostMapping("queryCount")
	public long queryCount(@RequestBody ${clsUpp}Cond cond ){
		return service.queryCount(cond);
	}
}