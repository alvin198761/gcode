package org.alvin.code.v2.sys.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * 项目模板业务
 */
@RestController
@RequestMapping("/api/template")
public class ProjectTemplateAction {

	@Autowired
	private ProjectTemplateService service;

	@RequestMapping("save")
	public int save(@RequestBody ProjectTemplateConfig templateConfig) {
		return this.service.save(templateConfig);
	}

	@RequestMapping("list")
	public List<ProjectTemplateConfig> list() {
		return this.service.list();
	}

	@PostMapping("upload")
	public String upload(MultipartFile file) throws IOException {
		//传入临时目录
		String uuid = UUID.randomUUID().toString();
		String dir = System.getProperty("java.io.tmpdir");
		Files.write(Paths.get(dir, uuid), file.getBytes());
		return uuid;
	}

	/**
	 * 添加文件
	 * @param file
	 * @return
	 */
	@PostMapping("addFile")
	public int addFile(@RequestBody ProjectTemplateFile file){
		return this.service.addFile(file);
	}
}
