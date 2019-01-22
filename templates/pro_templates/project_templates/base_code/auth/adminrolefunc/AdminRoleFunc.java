package ${project.base_package}.sys.auth.adminrolefunc;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @类说明:角色菜单(功能权限)实体类
 * @author:${project.author}
 * @date:${project.date}
 **/
@Setter
@Getter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoleFunc {

	//数据库中的字段
	private Long id;// 主键
	private Integer role_id;// 角色主键
	private Integer user_id;// 菜单主键

	//此处可添加查询显示辅助字段

}