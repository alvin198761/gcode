package org.alvin.code.v2.core.model;

import lombok.Data;

@Data
public class Field {
	private String name;// 字段名
	private String comment;// 注释
	private String type;// 数据类型

	private String bigName; //首字母大小
}
