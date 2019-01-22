package org.alvin.code.v2.sys.pro;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectAction {
	@Autowired
	private ProjectService projectService;

	@PostMapping("save")
	public int save(@RequestBody ProjectConfig projectConfig) {
		Date date = new Date();
		String dateText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		projectConfig.setDate(dateText);
		return this.projectService.save(projectConfig);
	}

	@PostMapping("delete")
	public int delete(@RequestBody ProjectConfig projectConfig) {
		return this.projectService.deleteProject(projectConfig.getName());
	}

	@RequestMapping("genProject")
	public String genProject(@RequestBody ProjectConfig projectConfig) throws IOException {
		save(projectConfig);//先保存
		return this.projectService.genProject(projectConfig.getName());
	}

	@RequestMapping("genEntity")
	public String genEntity(@RequestParam("templateName") String projectName, @RequestParam("entityName[]") String entityName) {
		return this.projectService.genEntity(projectName, entityName);
	}

	@GetMapping("types")
	public List<TypeBean> getTypes() {
		return this.projectService.getTypes();
	}

	@RequestMapping("download")
	public void download(@RequestParam("filePath") String filePath, HttpServletResponse response) throws IOException {
		Path path = Paths.get(filePath);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + path.toFile().getName() + "\"");
		response.setHeader("Content-Length", "" + Files.size(path));
		response.setContentType("application/zip");
		try (OutputStream out = response.getOutputStream()) {
			out.write(Files.readAllBytes(path));
		}
		System.gc();
		Files.delete(path);
	}

	@RequestMapping("list")
	public List<ProjectConfig> list() {
		return this.projectService.getProjects();
	}

	@GetMapping("test")
	public void test() {
		ProjectConfig projectConfig = new ProjectConfig();
		projectConfig.setAuthor("alvin - 111");
		projectConfig.setName("test");
		projectConfig.setBase_package("org.alvin.test");
		projectConfig.setRemark("测试项目");

		EntityConfig entityConfig = new EntityConfig();
		Date date = new Date();
		String dateText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		entityConfig.setIdName("uid");
		entityConfig.setLabalName("name");
		entityConfig.setName("UserBean");
		entityConfig.setFields(Lists.newArrayList());

		FieldConfig fieldConfig = new FieldConfig();
		fieldConfig.setIsNull("not null");
		fieldConfig.setLength(11);
		fieldConfig.setName("uid");
		fieldConfig.setSql_type("bigint(11)");
		fieldConfig.setRemark("用户ID");
		fieldConfig.setType("java.lang.Long");
		entityConfig.getFields().add(fieldConfig);

		fieldConfig = new FieldConfig();
		fieldConfig.setIsNull("null");
		fieldConfig.setLength(50);
		fieldConfig.setName("uname");
		fieldConfig.setSql_type("varchar(50)");
		fieldConfig.setRemark("用户名称");
		fieldConfig.setType("java.lang.String");
		entityConfig.getFields().add(fieldConfig);

		fieldConfig = new FieldConfig();
		fieldConfig.setIsNull("null");
		fieldConfig.setLength(50);
		fieldConfig.setName("test");
		fieldConfig.setSql_type("varchar(150)");
		fieldConfig.setRemark("测试");
		fieldConfig.setType("java.lang.String");
		entityConfig.getFields().add(fieldConfig);

		fieldConfig = new FieldConfig();
		fieldConfig.setIsNull("null");
		fieldConfig.setLength(50);
		fieldConfig.setName("time");
		fieldConfig.setSql_type("datetime");
		fieldConfig.setRemark("日期");
		fieldConfig.setType("java.util.Date");
		entityConfig.getFields().add(fieldConfig);
		entityConfig.setRemark("测试类");
		projectConfig.setEntitys(Lists.newArrayList(entityConfig));

		this.save(projectConfig);
		try {
			this.genProject(projectConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
