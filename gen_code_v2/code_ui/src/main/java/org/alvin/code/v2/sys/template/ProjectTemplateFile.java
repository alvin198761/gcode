package org.alvin.code.v2.sys.template;

import lombok.Data;

/**
 * 模板文件
 */
@Data
public class ProjectTemplateFile {
	//文件名称
	private String name;
	//文件类型 1 普通模板 2 实体模板 3 非模板 4 目录
	private Short type;
	//相对项目模板文件夹的位置，相对路径
	private String path;
	//项目名称
	private String templateName;
	//内容
	private String content;
	//父节点名称
	private String pid;
	//当前节点id
	private String id;

	//内容获取类型 content 编辑，upload.上传，3，url 地址
	private String contentType;
}
