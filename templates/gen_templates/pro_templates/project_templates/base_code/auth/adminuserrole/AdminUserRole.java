package ${project.base_package}.sys.auth.adminuserrole;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @类说明:用户角色实体类
 * @author:${project.author}
 * @date:${project.date}
 **/
@Setter
@Getter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserRole {

	// 数据库中的字段
	private Long id;// 主键
	private Integer user_id;// 用户主键
	private Integer role_id;// 角色主键

	// 此处可添加查询显示辅助字段

}