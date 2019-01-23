package org.alvin.code.v2.core.model;

import lombok.Data;
import org.alvin.code.gen.beans.BaseCondition;
import org.alvin.code.v2.core.utils.Utils;

import java.util.List;

@Data
public class CodeCond extends BaseCondition {

	@Override
	public void addCondition() {
		add(t_name, "AND table_name LIKE ?", 3);
		add(t_name_eq, "AND table_name = ?");
		add(db_user, "AND table_schema=?");
	}

	private String t_name;// 表名模糊
	private String t_name_eq;// 表名等于
	private String db_user;// 数据库用户名
	//
	private String author;// 作者名
	private String packageName; //包名
	private List<Table> c_list;// 生成代码用的数据

	private String sql;


}