package org.alvin.code.v2.sys.pro;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import org.alvin.code.v2.core.utils.Utils;
import org.alvin.code.v2.core.utils.VelocityUtil;
import org.alvin.code.v2.sys.template.ProjectTemplateConfig;
import org.alvin.code.v2.sys.template.ProjectTemplateFile;
import org.alvin.code.v2.sys.template.ProjectTemplateService;
import org.alvin.utils.ZipUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Value("${gen.project.datadir}")
    private String projectDataDir;

    @Value("${gen.project.outdir}")
    private String prjectOutDir;

    @Autowired
    private ProjectTemplateService projectTemplateService;


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
     * @param templateName
     * @return
     */
    public String genProject(String projectName, String templateName) throws IOException {
        File file = new File(prjectOutDir, projectName);
        deleteFile(file);
        file = new File(projectDataDir, projectName.concat(".json"));
        try {
            ProjectConfig config = JSONObject.parseObject(Files.readAllBytes(Paths.get(file.toURI())), ProjectConfig.class);
            ProjectTemplateConfig templateConfig = this.projectTemplateService.getTemplateByName(templateName);
            return genCodeByTemplate(config, templateConfig);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 根据模板生成项目
     *
     * @param projectConfig
     * @param templateConfig
     */
    private String genCodeByTemplate(ProjectConfig projectConfig, ProjectTemplateConfig templateConfig) throws IOException {
        //创建跟目录
        //项目目录
        File outFileDir = new File(prjectOutDir, projectConfig.getName());
        outFileDir.mkdirs();
        //实体类生成数据库
        genTable(projectConfig);
        //循环所有模板,根据模板类型处理
        LinkedList<ProjectTemplateFile> files = Lists.newLinkedList();
        files.addAll(templateConfig.getTemplateFiles());
        JSONObject project = calcProject(projectConfig);
        VelocityEngine engine = VelocityUtil.fileVelocityEngine(new File(ProjectTemplateService.TEMPLATE_DIR.getAbsolutePath(), templateConfig.getName()).getAbsolutePath());
        for (ProjectTemplateFile f : templateConfig.getTemplateFiles()) {
            //  1 普通模板 2 实体模板 3 非模板 4 目录
            if (f.getType() == 4) {
                //创建目录
                new File(outFileDir, f.getPath()).mkdirs();
                continue;
            }
            if (f.getType() == 3) {
                //直接复制
                Path templateFile = Paths.get(ProjectTemplateService.TEMPLATE_DIR.getAbsolutePath(), templateConfig.getName(), f.getPath());
                Path distPath = Paths.get(projectDataDir, f.getPath());
                try {
                    Files.copy(templateFile, distPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                continue;
            }
            if (f.getType() == 2) {
                //循环替换,还要创建目录
                for (EntityConfig entityConfig : projectConfig.getEntities()) {
                    genEntityFile(projectConfig, templateConfig, entityConfig, f, project, engine);
                }
                continue;
            }
            if (f.getType() == 1) {
                //非实体属性,全部加载
                genProjectFile(projectConfig, f, project, engine);
                continue;
            }
        }
        File file = new File(System.getProperty("java.io.tmpdir"));
        File zipPath = new File(file, projectConfig.getName().concat(".zip"));
        ZipUtils.doCompress(outFileDir, zipPath);
        return zipPath.getAbsolutePath();
    }

    /**
     * 生成数据库表
     *
     * @param projectConfig
     */
    private void genTable(ProjectConfig projectConfig) throws IOException {
        JSONObject entityConfig = new JSONObject();
        entityConfig.put("entitys", projectConfig.getEntities());
        entityConfig.put("project", projectConfig);
        //生成sql文件
        File outBaseDir = new File("../../templates/gen_templates");
        VelocityEngine engine = VelocityUtil.fileVelocityEngine(outBaseDir.getAbsolutePath());
        String content = VelocityUtil.parse("sqltemplates/Project_db_table.vm", entityConfig, engine);
        Path sqlPath = Paths.get(prjectOutDir, projectConfig.getName(), projectConfig.getName().concat(".sql"));
        Files.write(sqlPath, content.getBytes("utf-8"));
        //执行sql
//        AntUtil.importSql(projectConfig.getDbConfig().getDriverName(),
//                projectConfig.getDbConfig().getUrl(),
//                projectConfig.getDbConfig().getUsername(),
//                projectConfig.getDbConfig().getPassword(), sqlPath.toFile().getAbsolutePath());
    }

    /**
     * 实体模板替换
     *
     * @param projectConfig
     * @param templateConfig
     * @param entityConfig
     * @param f
     * @param project
     * @param engine
     */
    private void genEntityFile(ProjectConfig projectConfig, ProjectTemplateConfig templateConfig, EntityConfig entityConfig, ProjectTemplateFile f, JSONObject project, VelocityEngine engine) throws IOException {
        //2.计算出目标文件的位置
        Path projectFile = Paths.get(prjectOutDir, projectConfig.getName(), entityConfig.getName(), f.getPath());
        //3.创建目录
        try {
            Files.createDirectories(projectFile.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //4.模板执行
        String baseUrl = new File(ProjectTemplateService.TEMPLATE_DIR, templateConfig.getName()).getCanonicalPath();
        VelocityEngine fileEngine = VelocityUtil.fileVelocityEngine(baseUrl);
        String suffix = ".vm";
        for (EntityConfig entity : projectConfig.getEntities()) {
            entity.getVar().setUpperCamel(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, entity.getName()));
            entity.getVar().setLowerCamel(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, entity.getName()));
            //计算出包名次
            Path srcDirPath = Paths.get(prjectOutDir, projectConfig.getName(), entityConfig.getName(), projectConfig.getSrcDir());
            Path entityPath = Paths.get(prjectOutDir, projectConfig.getName(), f.getPath());
            String packagePath = entityPath.getParent().toFile().getAbsolutePath();
            String srcDirPathStr = srcDirPath.toFile().getAbsolutePath();
            String packageName = packagePath.substring(srcDirPathStr.length()).replaceAll("/", ".");
            entity.getVar().setPackageName(packageName);
            entity.getVar().setAllTypeName(packageName.concat(".").concat(entity.getVar().getUpperCamel()));
            entity.getVar().setTableName(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entity.getVar().getUpperCamel()));
            //用工参数
            FieldConfig idField = entity.getFields().stream().map(item -> {
                item.setAllTypeName(item.getType());
                item.setUpperCamel(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, item.getName()));
                item.setLowerCamel(item.getName());
                item.setColName(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getName()));
                item.setBigName(Utils.firstUpper(item.getName()));
                item.setLowerName(item.getName().toLowerCase());
                if (item.getAllTypeName().indexOf(".") != -1) {
                    int index = item.getAllTypeName().lastIndexOf(".");
                    item.setType(item.getAllTypeName().substring(index + 1));
                } else {
                    item.setType(item.getAllTypeName());
                }
                return item;
            }).filter(item -> item.getName().equals(entity.getIdName())).findFirst().get();// 字段列表
            String upp = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, entityConfig.getName());// 驼峰类名(首字母大写)
            String low = upp.toLowerCase();// 类名小写(只用包路径)

            //组装对象
            List<String> colList = entity.getFields().stream().map(FieldConfig::getColName).collect(Collectors.toList());
            entity.getVar().setSelectItems(colList.stream().map(item -> "t.".concat(item)).collect(Collectors.joining(",")));
            entity.getVar().setInsertFields(colList.stream().collect(Collectors.joining(",")));
            entity.getVar().setInsertValuesFields(colList.stream().map(item -> ":".concat(item)).collect(Collectors.joining(",")));
            entity.getVar().setReplaceValuesFields(colList.stream().map(item -> "?").collect(Collectors.joining(",")));
            entity.getVar().setUpdateFields(colList.stream().map(item -> item.concat("=?")).collect(Collectors.joining(",")));
            entity.getVar().setParamsFields(entityConfig.getFields().stream().map(item -> "vo.".concat(item.getUpperCamel()).concat("()")).collect(Collectors.joining(",")));
            String updateParams = entity.getFields().stream().filter(FieldConfig::getIsKey).map(item -> "vo.".concat(item.getUpperCamel()).concat("()")).collect(Collectors.joining(","));
            entity.getVar().setUpdateParams(updateParams.concat(",vo.".concat(idField.getUpperCamel()).concat("()")));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", idField);
            jsonObject.put("entity", entityConfig);
            jsonObject.put("packageName", packageName);
            jsonObject.putAll(project);
            //java 代码生成
            VelocityUtil.parseEntityTemplate(Lists.newArrayList(f.getPath()), entityPath.toFile().getAbsolutePath(), jsonObject, packageName, low, suffix, fileEngine);
        }
    }

    /**
     * 非实体模板
     *
     * @param projectConfig
     * @param f
     * @param project
     * @param engine
     */
    private void genProjectFile(ProjectConfig projectConfig, ProjectTemplateFile f, JSONObject project, VelocityEngine engine) {
        //2.计算出目标文件的位置
        Path projectFile = Paths.get(prjectOutDir, projectConfig.getName(), f.getPath());
        //3.创建目录
        try {
            Files.createDirectories(projectFile.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //4.模板执行
        VelocityUtil.parse(f.getPath(), project, projectFile.toAbsolutePath().toFile().getAbsolutePath(), engine);
    }

    /**
     * 建立项目变量规范
     *
     * @param projectConfig
     * @return
     */
    private JSONObject calcProject(ProjectConfig projectConfig) {
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        JSONObject project = new JSONObject();
        project.put("dollar", "$");
        project.put("sharp", "#");
        project.put("time", dateFormat);
        //融合变量,project dyn 放在一起,如果变量重名,以project 为准
        projectConfig.setTemplateName(null);
        JSONObject dyn = projectConfig.getDynAttrs();
        project.put("project", projectConfig);
        dyn.putAll(project.getJSONObject("project"));
        project.put("project", dyn);
        return project;
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
            config.setEntities(config.getEntities().stream().filter(item ->
                    !item.getName().equals(entityName)
            ).collect(Collectors.toList()));
            return save(config);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
