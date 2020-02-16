package org.alvin.home.v3.code.system.gencode;

import org.alvin.home.v3.code.beans.EntityConfig;
import org.alvin.home.v3.code.beans.RestApiResult;
import org.alvin.home.v3.code.beans.TableBean;
import org.alvin.home.v3.code.system.gencoderef.AlvinGenCodeRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
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
 * @author 唐植超
 * @date 2020/01/20
 */
@RestController
@RequestMapping("/api/code")
public class GenCodeController {
    @Autowired
    private GenCodeService genCodeService;

    /**
     * 查询所有的表
     *
     * @return
     */
    @PostMapping("queryTables")
    public RestApiResult<List<TableBean>> queryTables() {
        return new RestApiResult<>(this.genCodeService.queryTables());
    }

    /**
     * 根据选择的条件生成文件
     *
     * @param codeCond
     * @return
     */
    @PostMapping("gencode")
    public RestApiResult<String> gencode(@RequestBody GenCodeCond codeCond) throws IOException {
        Assert.notNull(codeCond.getAuthor(), "作者不能为空");
        Assert.notNull(codeCond.getPackageName(), "包名不能为空");
        Assert.notNull(codeCond.getTables(), "请选择要生成的表");
        Assert.notNull(codeCond.getTemplateDirs(), "请选择要模板");
        return new RestApiResult<>(this.genCodeService.gencode(codeCond));
    }

    /**
     * 查询所有的表
     *
     * @return
     */
    @PostMapping("queryTemplateDir")
    public RestApiResult<List<String>> queryTemplateDir() {
        return new RestApiResult<>(this.genCodeService.getTemplates());
    }

    /**
     * 下载生成代码
     *
     * @param response
     * @param uuid
     * @throws IOException
     */
    @GetMapping("download/{uuid}")
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

    @PostMapping("executeSql")
    public RestApiResult<Boolean> executeSql(@RequestParam("sql") String sql) {
        Assert.notNull(sql, "sql 不能为空");
        return new RestApiResult<Boolean>(this.genCodeService.executeSql(sql));
    }

    @PostMapping("executeSqlFile")
    public RestApiResult<Boolean> upload(MultipartFile file) throws IOException {
        Assert.notNull(file, "文件不能为空");
        return new RestApiResult<Boolean>(this.genCodeService.executeSql(new String(file.getBytes())));
    }


    @PostMapping("designPreview")
    public RestApiResult<String> designPreview(@RequestBody EntityConfig entityConfig) throws IOException {
        return new RestApiResult(this.genCodeService.designPreview(entityConfig));
    }

    @GetMapping("exportStruts")
    public void exportStruts(HttpServletResponse response) throws Exception {
//        Path path = Paths.get(this.service.exportStruts());
//        response.setHeader("Content-Disposition", "attachment; filename=\"struts.sql\"");
//        response.setHeader("Content-Length", "" + Files.size(path));
//        response.setContentType("application/octet-stream");
//        OutputStream out = response.getOutputStream();
//        out.write(Files.readAllBytes(path));
//        out.flush();
//        out.close();
//        System.gc();
//        Files.delete(path);
    }

    @GetMapping("exportData")
    public void exportData(HttpServletResponse response) throws Exception {
//        Path path = Paths.get(this.service.exportData());
//        response.setHeader("Content-Disposition", "attachment; filename=\"data.sql\"");
//        response.setHeader("Content-Length", "" + Files.size(path));
//        response.setContentType("application/octet-stream");
//        OutputStream out = response.getOutputStream();
//        out.write(Files.readAllBytes(path));
//        out.flush();
//        out.close();
//        System.gc();
//        Files.delete(path);
    }

    /**
     * 查询关联表格
     *
     * @throws Exception
     */
    @PostMapping("loadRefTables")
    public RestApiResult<List<RefTableDto>> loadRefTables(@RequestParam("tableName") String tableName) throws Exception {
        return new RestApiResult<>(this.genCodeService.loadRefTables(tableName));
    }

    /**
     * 加载字段关联关系
     * @param tableName
     * @return
     * @throws Exception
     */
    @PostMapping("loadFields")
    public RestApiResult<List<AlvinGenCodeRef>>  loadFields(@RequestParam("tableName") String tableName) throws Exception {
        return new RestApiResult<>(this.genCodeService.loadFields(tableName));
    }

    /**
     * 表格设置
     * @param settings
     * @return
     */
    @PostMapping("tableSettings")
    public RestApiResult<Integer> tableSettings(@RequestBody TableSettingsDto settings){
        return new RestApiResult< >(this.genCodeService.tableSettings(settings));
    }

}
