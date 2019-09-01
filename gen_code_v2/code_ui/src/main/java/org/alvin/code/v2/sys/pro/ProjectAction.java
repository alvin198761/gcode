package org.alvin.code.v2.sys.pro;

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
		return this.projectService.genProject(projectConfig.getName() ,projectConfig.getTemplateName());
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
}
