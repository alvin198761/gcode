package org.alvin.code.v2.sys.mock;

import com.alibaba.fastjson.JSONObject;
import org.alvin.code.v2.sys.mock.bean.JSMockApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/msiteMock")
public class JSMockAction {

	@Autowired
	private JsMockBus bus;

	//http://localhost:8003/v2/api-docs
	@RequestMapping("queryList")
	public List<JSONObject> queryList(@RequestBody JSMockApiConfig config) {
		return this.bus.queryList(config);
	}

	@RequestMapping("gencode")
	public String genCode(@RequestBody JSMockApiConfig config, HttpServletResponse response) throws IOException {
		try {
			return this.bus.genCode(config);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping("download")
	public void download(@RequestParam("filePath") String filePath, HttpServletResponse response) throws IOException {
		Path path = Paths.get(filePath);
		response.setHeader("Content-Disposition", "attachment; filename=\"app_center_mock.zip\"");
		response.setHeader("Content-Length", "" + Files.size(path));
		response.setContentType("application/zip");
		try (OutputStream out = response.getOutputStream()) {
			out.write(Files.readAllBytes(path));
		}
		System.gc();
		Files.delete(path);
	}
}
