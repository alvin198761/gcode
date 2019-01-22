package ${project.base_package}.sys.auth.adminrolefunc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @类说明:角色菜单(功能权限)前端控制器层
 * @author:${project.author}
 * @date:${project.date}
 **/
@RestController
@RequestMapping("api/adminRoleFunc")
public class AdminRoleFuncAction {
//	private final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private AdminRoleFuncBus bus; // 角色菜单(功能权限)Business层

	/**
	 * @方法说明:按条件查询不分页角色菜单(功能权限)列表
	 **/
	@PostMapping("queryList")
	public List<AdminRoleFunc> queryList(@RequestBody AdminRoleFuncCond cond, Principal principal) {
		return bus.queryList(cond);
	}

	/**
	 * @方法说明:按条件查询分页角色菜单列表
	 **/
	@PostMapping("insertBatch")
	public int[] insertBatch(@RequestBody List<AdminRoleFunc> list, @RequestParam("role_id") int role_id) {
		return bus.insertBatch(list, role_id);
	}
}