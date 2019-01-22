package org.alvin.code.v2.sys.template;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.alvin.utils.zip.UnZipFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 项目模板 业务处理
 */
@Service
public class ProjectTemplateService {

	@Value("${gen.project.templatedir}")
	private String templateDir;

	/**
	 * 保存项目
	 *
	 * @param templateConfig
	 * @return
	 */
	public int save(ProjectTemplateConfig templateConfig) {
		templateConfig.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		File templateDir = new File(this.templateDir, templateConfig.getName());
		templateDir.mkdirs();
		try {
			File configFile = new File(templateDir, "config.json");
			Files.write(Paths.get(configFile.toURI()), JSONObject.toJSONBytes(templateConfig));
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 接收zip 上传
	 *
	 * @param multipartFile
	 * @return
	 */
	public int uploadTemplate(MultipartFile multipartFile) throws IOException {
		String uuid = UUID.randomUUID().toString();
		File file = new File(System.getProperty("java.io.tmpdir"), uuid);
		Files.write(Paths.get(file.toURI()), multipartFile.getBytes());
		File dist = new File(templateDir, uuid);
		UnZipFile.unZipFiles(file, dist.getAbsolutePath());
		LinkedList<File> files = new LinkedList<>();
		files.addAll(Lists.newArrayList(dist.listFiles()));
		ProjectTemplateConfig projectTemplateConfig = new ProjectTemplateConfig();
		projectTemplateConfig.setName(uuid);
		projectTemplateConfig.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		while (!files.isEmpty()) {
			File subFile = files.removeFirst();
			ProjectTemplateFile templateFile = new ProjectTemplateFile();
			templateFile.setName(subFile.getName());
			if (subFile.isDirectory()) {
				templateFile.setType((short) 4);
			} else {
				//一律按普通模板处理
				templateFile.setType((short) 1);
			}
			templateFile.setPath(subFile.getAbsolutePath().substring(dist.getAbsolutePath().length() - 1));
			projectTemplateConfig.getTemplateFiles().add(templateFile);
		}
		return save(projectTemplateConfig);
	}


	public ProjectTemplateConfig getTemplateByName(String templateName) throws IOException {
		return JSONObject.parseObject(Files.readAllBytes(Paths.get(new File(templateDir, templateName + "/config.json").toURI())), ProjectTemplateConfig.class);
	}

	/**
	 * 添加文件
	 *
	 * @param file
	 * @return
	 */
	public int addFile(ProjectTemplateFile file) {
//		1 普通模板 2 实体模板 3 非模板 4 目录
		File templateDir = new File(this.templateDir, file.getTemplateName());
		Path targetPath = Paths.get(templateDir.getAbsolutePath(), file.getPath(), file.getName());
		if (file.getType() == 4) {
			try {
				Files.createDirectories(targetPath);
				ProjectTemplateConfig projectTemplateConfig = getTemplateByName(file.getTemplateName());
				projectTemplateConfig.getTemplateFiles().add(file);
				file.setTemplateName(null);
				save(projectTemplateConfig);
				return 1;
			} catch (IOException e) {
				e.printStackTrace();
				return 0;
			}
		}
		try {
			if (file.getContentType().equals("url")) {
				//下载
				URL url = new URL(file.getContent().trim());
				try (InputStream is = url.openStream()) {
					Files.copy(is, targetPath);
				}
			} else if (file.getContentType().equals("upload")) {
				//移动文件
				Files.move(Paths.get(System.getProperty("java.io.tmpdir"), file.getContent()), targetPath);
			} else {
				//直接存
				Files.write(targetPath, file.getContent().getBytes("utf-8"));
			}
			ProjectTemplateConfig projectTemplateConfig = getTemplateByName(file.getTemplateName());
			projectTemplateConfig.getTemplateFiles().add(file);
			file.setContent(null);
			file.setTemplateName(null);
			save(projectTemplateConfig);
			return 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}


	/**
	 * 上传文件
	 *
	 * @param part
	 * @param file
	 * @return
	 */
	public int uploadFile(MultipartFile part, ProjectTemplateFile file) {
		File templateDir = new File(this.templateDir, file.getTemplateName());
		try {
			Files.write(Paths.get(templateDir.getAbsolutePath(), file.getPath()), part.getBytes());
			ProjectTemplateConfig projectTemplateConfig = getTemplateByName(file.getTemplateName());
			projectTemplateConfig.getTemplateFiles().add(file);
			file.setContent(null);
			file.setTemplateName(null);
			save(projectTemplateConfig);
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 列表显示文件
	 *
	 * @return
	 */
	public List<ProjectTemplateConfig> list() {
		File file = new File(templateDir);
		if (!file.exists()) {
			file.mkdirs();
			return Lists.newArrayList();
		}
		File[] files = file.listFiles();
		if (files == null) {
			return Lists.newArrayList();
		}
		return Lists.newArrayList(files).stream().filter(item -> {
			//判断是否有固定的文件
			File configFile = new File(item, "config.json");
			return configFile.exists();
		}).map(item -> {
			try {
				return JSONObject.parseObject(Files.readAllBytes(Paths.get(new File(item, "config.json").toURI())), ProjectTemplateConfig.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return (ProjectTemplateConfig) null;
		}).collect(Collectors.toList());
	}
}
