package org.alvin.code.v2.core.action;


import lombok.extern.slf4j.Slf4j;
import org.alvin.code.gen.beans.RestfullResp;
import org.alvin.code.v2.core.dao.CodeDao;
import org.alvin.code.v2.core.model.CodeCond;
import org.alvin.code.v2.core.model.Field;
import org.alvin.code.v2.core.model.Table;
import org.alvin.code.v2.core.service.CodeService;
import org.alvin.code.v2.core.utils.VelocityUtil;
import org.alvin.code.v2.sys.pro.EntityConfig;
import org.alvin.code.v2.sys.pro.FieldConfig;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @author gzz_gzz@163.com
 * @功能描述:代码生成器控制器类
 * @date 2018-02-15
 */
@Slf4j
@RestController
@RequestMapping("/api/code")
public class CodeAction {
    @Autowired
    private CodeService service;// 生成器业务罗辑接口

    /**
     * @功能描述: 查询数据库中表名列表
     */
    @PostMapping("/queryList")
    public List<Table> queryList(@RequestBody CodeCond cond) {
        cond.setDb_user(CodeDao.DBUSER);
        return service.queryTables(cond);
    }

    /**
     * @功能描述: 查询数据库中表名列表
     */
    @PostMapping("/queryField")
    public List<FieldConfig> queryField(@RequestBody CodeCond cond) {
        cond.setDb_user(CodeDao.DBUSER);
        return service.queryFieldConfig(cond);
    }

    /**
     * @功能描述: 生成代码
     */
    @PostMapping("/create")
    public RestfullResp<Map<String, Object>> create(@RequestBody CodeCond cond) throws Exception {
        cond.setDb_user(CodeDao.DBUSER);
        return service.create(cond);
    }

    @GetMapping("/downCode/{uuid}")
    public void downCode(HttpServletResponse response, @PathVariable("uuid") String uuid) throws IOException {
        String fileName = "code_" + uuid + ".zip";
        Path path = Paths.get("dist", uuid.concat(".zip"));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("Content-Length", "" + Files.size(path));
        response.setContentType("application/zip");
        OutputStream out = response.getOutputStream();
        out.write(Files.readAllBytes(path));
        out.flush();
        out.close();
        System.gc();
        Files.delete(path);
    }

    @GetMapping("/executeSql")
    public void executeSql(String sql) {
        service.executeSql(sql);
    }

    @GetMapping("/templateDirs")
    public List<String> templateDirs() {
        return this.service.templateDirs();
    }

    @PostMapping("upload")
    public void upload(MultipartFile file) throws IOException {
        this.service.executeSql(new String(file.getBytes()));
    }

    @PostMapping("designPreview")
    public String designPreview(@RequestBody EntityConfig entityConfig) throws IOException {
        return this.service.designPreview(entityConfig);
    }

    @GetMapping("exportStruts")
    public void exportStruts(HttpServletResponse response) throws Exception {
        Path path = Paths.get(this.service.exportStruts());
        response.setHeader("Content-Disposition", "attachment; filename=\"struts.sql\"");
        response.setHeader("Content-Length", "" + Files.size(path));
        response.setContentType("application/octet-stream");
        OutputStream out = response.getOutputStream();
        out.write(Files.readAllBytes(path));
        out.flush();
        out.close();
        System.gc();
        Files.delete(path);
    }

    @GetMapping("exportData")
    public void exportData(HttpServletResponse response) throws Exception {
        Path path = Paths.get(this.service.exportData());
        response.setHeader("Content-Disposition", "attachment; filename=\"data.sql\"");
        response.setHeader("Content-Length", "" + Files.size(path));
        response.setContentType("application/octet-stream");
        OutputStream out = response.getOutputStream();
        out.write(Files.readAllBytes(path));
        out.flush();
        out.close();
        System.gc();
        Files.delete(path);
    }

}
