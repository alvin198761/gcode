package org.alvin.code.v2.sys.template;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * 项目模板业务
 */
@RestController
@RequestMapping("/api/template")
@Slf4j
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
	public int upload(
			MultipartFile file,HttpServletRequest request) throws IOException {
		JSONObject params = new JSONObject();
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			params.put(key, request.getParameter(key));
		}
		ProjectTemplateFile projectTemplateFile = params.toJavaObject(ProjectTemplateFile.class);
		//传入临时目录
		return this.service.uploadFile(file,projectTemplateFile);
	}

	/**
	 * 添加文件
	 *
	 * @param file
	 * @return
	 */
	@PostMapping("addFile")
	public int addFile(@RequestBody ProjectTemplateFile file) {
		return this.service.addFile(file);
	}

	@PostMapping("deleteFile")
	public int deleteFile(@RequestBody ProjectTemplateFile file) {
		return this.service.deleteFile(file);
	}


	@GetMapping("getIcon")
	public void getIcon(@RequestParam("templateName") String templateName, @RequestParam("path") String path, @RequestParam("type") int type, HttpServletResponse response) throws IOException {
		response.setContentType("image/gif");
		File targetFile = this.service.getLocalFile(templateName, path, type);
		Icon icon = FileSystemView.getFileSystemView().getSystemIcon(targetFile);
		BufferedImage bf = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
		icon.paintIcon(null, bf.createGraphics(), 0, 0);
		ImageIO.write(bf, "gif", response.getOutputStream());
	}


}
