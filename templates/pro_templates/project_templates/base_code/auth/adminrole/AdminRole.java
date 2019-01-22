package ${project.base_package}.sys.auth.adminrole;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @类说明:角色实体类
 * @author:${project.author}
 * @date:${project.date}
 **/
@Setter
@Getter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRole {

	//数据库中的字段
	private Integer id;// 主键
	private String name;// 名称
	private String remark;// 备注

	//此处可添加查询显示辅助字段

}