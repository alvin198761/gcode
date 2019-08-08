package ${project.base_package}.sys.auth.adminroledept;

import lombok.Getter;
import lombok.Setter;
import ${project.base_package}.sys.util.base.BaseCondition;

/**
 * @类说明:角色部门查询条件实体类
 * @author:${project.author}
 * @date:${project.date}
 **/
@Setter
@Getter
public class AdminRoleDeptCond extends BaseCondition {

	/**
	 * @方法说明:拼加自定义条件 
	 **/
	@Override
	public void addCondition() { 
		//add(id, "AND t.id = ?");
		add(role_id, "AND t.role_id = ?");
		//add(dept_id, "AND t.dept_id = ?");
		//add(ids, "AND t.id IN ");
	}

	//查询条件,把不用的条件清理掉
	private Long id;// 主键
	private Integer role_id;// 角色主键
	private Integer dept_id;// 菜单主键
	//private List<Long> ids;// 主键列表

}