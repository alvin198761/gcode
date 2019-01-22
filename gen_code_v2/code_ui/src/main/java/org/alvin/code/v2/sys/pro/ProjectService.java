package org.alvin.code.v2.sys.pro;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.alvin.code.v2.core.model.Field;
import org.alvin.code.v2.core.utils.Utils;
import org.alvin.code.v2.core.utils.VelocityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

	@Value("${gen.project.datadir}")
	private String projectDataDir;

	@Value("${gen.project.outdir}")
	private String prjectOutDir;


	/**
	 * 生成项目，返回生成后的路径
	 *
	 * @param projectConfig
	 * @return
	 */
	public int save(ProjectConfig projectConfig) {
		File file = new File(projectDataDir, projectConfig.getName().concat(".json"));
		file.getParentFile().mkdirs();
		try {
			Files.write(Paths.get(file.toURI()), JSONObject.toJSONBytes(projectConfig));
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 生成项目
	 *
	 * @param projectName
	 * @return
	 */
	public String genProject(String projectName) throws IOException {
		File file = new File(prjectOutDir, projectName);
		deleteFile(file);
		file = new File(projectDataDir, projectName.concat(".json"));
		try {
			ProjectConfig config = JSONObject.parseObject(Files.readAllBytes(Paths.get(file.toURI())), ProjectConfig.class);
			initProject(config);
			String zipPath = zipProject(config);
			return zipPath;
		} catch (Exception ex) {
			throw ex;
		}
	}

	private String zipProject(ProjectConfig config) {
		return "";
	}


	private void initProject(ProjectConfig projectConfig) throws IOException {
		JSONObject config = new JSONObject();
		config.put("project", projectConfig);
		config.put("dollar", "$");
		//项目目录
		File outFileDir = new File(prjectOutDir, projectConfig.getName());
		outFileDir.mkdirs();
		//第一层目录文件
		File outSrcFile = mkdir(outFileDir, "src");
		File sourceDir = new File(System.getProperty("user.dir"), "project_templates");
		VelocityUtil.parse("/project_templates/pom.xml", config, new File(outFileDir, "pom.xml").getAbsolutePath(), VelocityUtil.fileVelocityEngine());
		//第二层目录文件
		File outMainFile = mkdir(outSrcFile, "main");
		File outJavaFile = mkdir(outMainFile, "java");
		//创建包路径
		File outPackageFile = mkdir(outJavaFile, projectConfig.getBase_package().replace('.', '/'));
		//打包文件
		File assembly = mkdir(outMainFile, "assembly");
		VelocityUtil.parse("/project_templates/assembly/assembly.xml", config, new File(assembly, "assembly.xml").getAbsolutePath(), VelocityUtil.fileVelocityEngine());
		//配置文件
		File outResourceDir = mkdir(outMainFile, "resources");
		copyDir(new File(sourceDir, "resources"), outResourceDir, config, System.getProperty("user.dir"));
		//web项目
		File webapp = mkdir(outMainFile, "webapp");
		copyDir(new File(sourceDir, "vue_2_cli3"), webapp, config, System.getProperty("user.dir"));
		//基础代码
		copyDir(new File(sourceDir, "base_code"), outPackageFile, config, System.getProperty("user.dir"));
		//生成数据库代码
		File sqlDir = mkdir(outMainFile, "sql");
		copyDir(new File(sourceDir, "sql"), sqlDir, config, System.getProperty("user.dir"));
		//生成实体类，查询条件类，控制器，业务类，dao,前端页面
		genCode(projectConfig, sourceDir, outFileDir);

	}

	private void genCode(ProjectConfig projectConfig, File sourceDir, File outFileDir) throws IOException {
		List<File> vms = Lists.newArrayList(new File(sourceDir, "entity").listFiles());
		for (EntityConfig table : projectConfig.getEntitys().stream().filter(item -> item.getType() == 0).collect(Collectors.toList())) {
			List<Field> fList = table.getFields().stream().filter(item -> item.getName() != null).map(item -> {
				Field field = new Field();
				field.setBigName(Utils.firstUpper(item.getName()));
				field.setComment(item.getRemark());
				field.setName(item.getName());
				if (item.getType().equals("ref")) {
					//找到原来的对象，然后找到对象的主键的类型
					EntityConfig refEntity = projectConfig.getEntitys().stream().filter(eitem -> eitem.getName().equals(item.getRef_name())).findFirst().get();
					if (refEntity.getType().intValue() == 0) {
						field.setType(refEntity.getFields().stream().filter(fitem -> refEntity.getIdName().equals(fitem.getName())).findFirst().get().getType());
					} else {
						field.setType("java.lang.String");
					}
				} else {
					field.setType(item.getType());
				}
				return field;
			}).collect(Collectors.toList());

			String cName = table.getRemark();// 表注释中文名
			String upp = table.getName();// 驼峰类名(首字母大写)
			String low = upp.toLowerCase();// 类名小写(只用包路径)
			String lowUpp = Utils.firstLower(upp);// 驼峰变量类名(首字母小写)

			FieldConfig idField = table.getFields().stream().filter(item -> item.getName().equals(table.getIdName())).findFirst().get();
			String idName = idField.getName();

			//组装对象
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("fList", fList);
			jsonObject.put("auth", projectConfig.getAuthor());
			jsonObject.put("cName", cName);
			jsonObject.put("lowUpp", lowUpp);
			jsonObject.put("idType", idField.getType());
			jsonObject.put("table", table);
			jsonObject.put("id", idField);
			jsonObject.put("tName", table.getName());
			jsonObject.put("idName", idField.getName());
			jsonObject.put("time", projectConfig.getDate());
			//类名称
			jsonObject.put("upp", upp);
			jsonObject.put("clsUpp", upp);
			//各种参数追加
			jsonObject.put("selectFields", Utils.add(fList, "t.", ",", false, "select"));
			jsonObject.put("insertFields", Utils.add(fList, "", ",", true, "insert"));
			jsonObject.put("insertValuesFields", Utils.add(fList, ":", ",", true, "insert"));
			jsonObject.put("replaceFields", Utils.add(fList, "", ",", false, "sql"));
			jsonObject.put("replaceValuesFields", Utils.add(fList));
			jsonObject.put("paramsFields", Utils.add(fList, "vo.get", "(),", false));
			jsonObject.put("updateFields", Utils.add(fList, "", "=?,", true, "sql"));
			jsonObject.put("updateParams", Utils.add(fList, "vo.get", "(),", true) + ",vo.get" + Utils.firstUpper(idName) + "()");
			jsonObject.put("selectItems", Utils.add(fList, "t.", ","));

			StringBuilder sb = new StringBuilder();
			table.getFields().stream().filter(item -> item.getType() == "ref").forEach(item -> {
				EntityConfig refEntity = projectConfig.getEntitys().stream().filter(eitem -> eitem.getName().equals(item.getRef_name())).findFirst().get();
				if (refEntity.getType() == 0) {
					sb.append(" left join " + refEntity.getName() + " as " + refEntity.getName().toLowerCase() + " on " + refEntity.getName().toLowerCase() + "." + refEntity.getIdName() + " = t." + table.getIdName() + " ");
				}
			});

			jsonObject.put("joinTables", sb.toString());
			//其他附属数据
			List<String> importList = Lists.newArrayList();
			importList.add(Utils.dateImport(fList));
			importList.add(Utils.bigImport(fList));
			jsonObject.put("importList", importList);
			//
			jsonObject.put("dollar", "$");
			//java 代码生成
			parseVmTemplate(vms, projectConfig, jsonObject, upp, low, outFileDir);
		}
	}


	/**
	 * 只生成java 类，目前
	 *
	 * @param vms
	 * @param projectConfig
	 * @param jsonObject
	 * @param upp
	 * @param low
	 * @throws IOException
	 */
	public void parseVmTemplate(List<File> vms, ProjectConfig projectConfig, JSONObject jsonObject, String upp, String low, File outSysDir) throws IOException {
		//循环模板，进行合并
		jsonObject.put("pName", projectConfig.getBase_package());
		jsonObject.put("upp", upp);
		jsonObject.put("low", low);
		//固定的目录
		String prefix = System.getProperty("user.dir");
		File outMainFile = mkdir(outSysDir, "src/main");
		File outJavaFile = mkdir(outMainFile, "java");
		File webapp = mkdir(outMainFile, "webapp");
		File outPackageFile = mkdir(outJavaFile, projectConfig.getBase_package().replace('.', '/'));
		File mockDir = mkdir(webapp, "mock");
		File webViewDir = mkdir(webapp, "src/views/sys/" + low);
		File sysDir = mkdir(outPackageFile, "sys/" + low);
		//固定的规则，还有待改进
		for (File vmFile : vms) {
			String vm = vmFile.getAbsolutePath();
			//获得文件名称
			System.out.println("template file :" + vm);
			String distName = vmFile.getName().replace("Model", upp);
			String templateName = vmFile.getAbsolutePath().substring(prefix.length()).replace('\\', '/');
			System.out.println("=================start VelocityEngine==================");
			String outPath;
			if (vmFile.getName().toLowerCase().endsWith(".vue")) {
				outPath = new File(webViewDir, distName).getAbsolutePath();
			} else if (vmFile.getName().endsWith("Mock.js")) {
				outPath = new File(mockDir, distName).getAbsolutePath();
			} else {
				outPath = new File(sysDir, distName).getAbsolutePath();
			}
			VelocityUtil.parse(templateName, jsonObject, outPath, VelocityUtil.fileVelocityEngine());
			System.out.println("=================end VelocityEngine==================");
		}
	}

	private String findFkType(String ref_name, ProjectConfig projectConfig) {
		EntityConfig entityConfig = projectConfig.getEntitys().stream().filter(item -> item.getName().equals(ref_name)).findFirst().get();
		return entityConfig.getFields().stream().filter(item -> item.getName().equals(entityConfig.getIdName())).findFirst().get().getType();
	}

	private void copyDir(File sourceDir, File targetDir, JSONObject config, String prefix) throws IOException {
		targetDir.mkdirs();
		LinkedList<File> files = new LinkedList<>(Lists.newArrayList(sourceDir.listFiles()));
		while (!files.isEmpty()) {
			File f = files.removeFirst();
			String path = f.getAbsolutePath().substring(sourceDir.getAbsolutePath().length());
			File targetFile = new File(targetDir, path);
			if (f.isDirectory()) {
				//文件夹直接创建
				targetFile.mkdirs();
				files.addAll(Lists.newArrayList(f.listFiles()));
				continue;
			}
			String templateName = f.getAbsolutePath().substring(prefix.length()).replace('\\', '/');
			VelocityUtil.parse(templateName, config, targetFile.getAbsolutePath(), VelocityUtil.fileVelocityEngine());
		}
	}

	private File mkdir(File parent, String child) {
		File file = new File(parent, child);
		file.mkdirs();
		return file;
	}

	private void deleteFile(File file) {
		LinkedList<File> files = new LinkedList<>();
		files.add(file);
		while (!files.isEmpty()) {
			File f = files.removeFirst();
			if (!f.isDirectory()) {
				f.delete();
				continue;
			}
			File[] fs = f.listFiles();
			if (fs == null || fs.length == 0) {
				f.delete();
				continue;
			}
			for (File subF : fs) {
				files.addLast(subF);
			}
			files.addLast(f);
		}
	}

	/**
	 * 生成实体类
	 *
	 * @param projectName
	 * @param entityName
	 * @return
	 */
	public String genEntity(String projectName, String entityName) {

		return null;
	}

	/**
	 * 类型列表
	 *
	 * @return
	 */
	public List<TypeBean> getTypes() {
		List<TypeBean> types = Lists.newArrayList();
		types.add(new TypeBean("java.lang.String", "VARCHAR", 50));
		types.add(new TypeBean("java.lang.Byte", "TINYINT", 50));
		types.add(new TypeBean("java.lang.Integer", "INT", 4));
		types.add(new TypeBean("java.lang.Long", "BIGINT", 11));
		types.add(new TypeBean("java.lang.Boolean", "bit", 0));
		types.add(new TypeBean("引用", "ref", 0));
		types.add(new TypeBean("java.lang.Float", "FLOAT", 11));
		types.add(new TypeBean("java.util.Date", "DATETIME", 11));
		return types;
	}

	/**
	 * 获取所有的项目
	 *
	 * @return
	 */
	public List<ProjectConfig> getProjects() {
		File file = new File(projectDataDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		if (!file.isDirectory()) {
			return Lists.newArrayList();
		}
		return Lists.newArrayList(file.listFiles((dir, name) -> name.toLowerCase().endsWith(".json")))
				.stream()
				.map(item ->
						{
							try {
								return JSONObject.parseObject(Files.readAllBytes(Paths.get(item.toURI())), ProjectConfig.class);
							} catch (IOException e) {
								e.printStackTrace();
							}
							return (ProjectConfig) null;
						}
				).filter(item -> item != null).collect(Collectors.toList());
	}

	/**
	 * 删除项目，
	 *
	 * @param projectName
	 * @return
	 */
	public int deleteProject(String projectName) {
		File file = new File(projectDataDir, projectName.concat(".json"));
		if (file.exists()) {
			if (file.delete()) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 删除实体类
	 *
	 * @param projectName
	 * @param entityName
	 * @return
	 */
	public int deleteEntity(String projectName, String entityName) {
		File file = new File(projectDataDir, projectName.concat(".json"));
		try {
			ProjectConfig config = JSONObject.parseObject(Files.readAllBytes(Paths.get(file.toURI())), ProjectConfig.class);
			config.setEntitys(config.getEntitys().stream().filter(item ->
					!item.getName().equals(entityName)
			).collect(Collectors.toList()));
			return save(config);
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
