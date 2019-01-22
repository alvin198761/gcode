package org.alvin.code.v2.sys.pro;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 项目配置
 */
@Data
public class ProjectConfig {

	private String name; //项目名称
	private String base_package; //包名

	private String date; //创建日期
	private String author; //作者

	private List<EntityConfig> entitys = Lists.newArrayList(); //实体类列表

	private String remark; //备注
}
