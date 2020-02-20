package org.alvin.home.v3.code.system.gencode;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.alvin.home.v3.code.beans.EntityConfig;
import org.alvin.home.v3.code.beans.FieldBean;
import org.alvin.home.v3.code.beans.TableBean;
import org.alvin.home.v3.code.system.JDBC2MbTypeService;
import org.alvin.home.v3.code.system.VmFileService;
import org.alvin.home.v3.code.system.gencodeconfig.AlvinGenCodeConfig;
import org.alvin.home.v3.code.system.gencodeconfig.AlvinGenCodeConfigDao;
import org.alvin.home.v3.code.system.gencoderef.AlvinGenCodeRef;
import org.alvin.home.v3.code.system.gencoderef.AlvinGenCodeRefDao;
import org.alvin.home.v3.code.utils.Utils;
import org.alvin.home.v3.code.utils.VelocityUtil;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GenCodeService {

    @Autowired
    private GenCodeDao genCodeDao;
    private String templateDir = new File(System.getProperty("user.dir")).getParentFile().getAbsolutePath() + "/templates/gen_templates/codev3";
    @Autowired
    private VmFileService vmFileService;
    @Autowired
    private JDBC2MbTypeService jdbc2MbTypeService;
    @Autowired
    private AlvinGenCodeConfigDao genCodeConfigDao;
    @Autowired
    private AlvinGenCodeRefDao alvinGenCodeRefDao;


    /**
     * 获取表格数据
     *
     * @return
     */
    public List<TableBean> queryTables() {
        List<TableBean> list = this.genCodeDao.queryTables();
        List<AlvinGenCodeConfig> configList = this.genCodeConfigDao.queryList(list.parallelStream().map(TableBean::getTableName).collect(Collectors.toList()));
        Map<String, String> configMap = Maps.newHashMap();
        for (AlvinGenCodeConfig config : configList) {
            configMap.put(config.getTableName(), config.getLabelName());
        }

        return list.parallelStream().map(item -> {
//            item.setClassName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, item.getTableName()));
            String className = item.getTableName().substring(item.getTableName().indexOf("_") + 1);//去前缀
            item.setClassName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, className));
            item.setVarName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, className));
            item.setCnName(item.getComment());
            item.setLabelCol(configMap.get(item.getTableName()));
            return item;
        }).collect(Collectors.toList());
    }

    /**
     * 获取项目模板列表
     *
     * @return
     */
    public List<String> getTemplates() {
        return Lists.newArrayList(new File(templateDir).list());
    }


    /**
     * 根据条件生成数据
     *
     * @param codeCond
     * @return
     */
    public String gencode(GenCodeCond codeCond) throws IOException {
        String suffix = ".vm";
        File tempTempFile = new File(this.templateDir);
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        VelocityEngine fileEngine = VelocityUtil.fileVelocityEngine(tempTempFile.getCanonicalPath());
        List<String> vms = vmFileService.scanTemplate(tempTempFile.getAbsolutePath(), suffix, codeCond.getTemplateDirs());
        String uuid = UUID.randomUUID().toString();
        String outPath = "dist/" + uuid;
        String author = codeCond.getAuthor();// 作者
        if (vms.isEmpty()) {
            throw new RuntimeException("没有扫描到模板");
        }
        List<AlvinGenCodeConfig> configList = this.genCodeConfigDao.queryList(codeCond.getTables().parallelStream().map(TableBean::getTableName).collect(Collectors.toList()));
        Map<String, String> configMap = Maps.newHashMap();
        for (AlvinGenCodeConfig config : configList) {
            configMap.put(config.getTableName(), config.getLabelName());
        }
        //根据选择的表 查询表信息
        for (TableBean tableBean : codeCond.getTables()) {
            tableBean.setLabelCol(configMap.get(tableBean.getTableName()));
            //查询字段
            List<String> pks = this.genCodeDao.getPrimaryKey(tableBean.getTableName());
//            List<FKFieldBean> fkFieldList = this.genCodeDao.getFKs(tableBean.getTableName());
            List<AlvinGenCodeRef> refs = this.alvinGenCodeRefDao.queryListByTableName(tableBean.getTableName()).parallelStream().map(item -> {
                String className = item.getRefTableName().substring(item.getRefTableName().indexOf("_") + 1);//去前缀
                item.setRefTableName(className);
                item.setRefTableShortName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, item.getRefTableName()));
                return item;
            }).collect(Collectors.toList());
            List<FieldBean> fkLabelFieldList = this.genCodeDao.getRefFields(refs).parallelStream().map(item -> {
                String className = item.getClassVarName().substring(item.getClassVarName().indexOf("_") + 1);//去前缀
                item.setClassVarName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, className));
                item.setLowerCamel(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, item.getName()));
                item.setUpperCamel(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, item.getName()));
                item.setLowerUnderscore(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getUpperCamel()));
                if (item.getAllTypeName().contains(".")) {
                    int index = item.getAllTypeName().lastIndexOf(".");
                    item.setType(item.getAllTypeName().substring(index + 1));
                } else {
                    item.setType(item.getAllTypeName());
                }
                return item;
            }).collect(Collectors.toList());
            List<FieldBean> fieldBeans = this.genCodeDao.getFields(tableBean.getTableName()).parallelStream().map(item -> {
                item.setLowerCamel(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, item.getName()));
                item.setUpperCamel(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, item.getName()));
                item.setLowerUnderscore(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getUpperCamel()));
                item.setClassVarName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableBean.getVarName()));
                if (item.getAllTypeName().contains(".")) {
                    int index = item.getAllTypeName().lastIndexOf(".");
                    item.setType(item.getAllTypeName().substring(index + 1));
                } else {
                    item.setType(item.getAllTypeName());
                }
                item.setIsPk(pks.contains(item.getName()) ? 1 : 0);
                for (AlvinGenCodeRef fk : refs) {
                    if (fk.getColName().equalsIgnoreCase(item.getName())) {
                        item.setFkCol(fk.getRefColName());
                        item.setFkTable(fk.getRefTableName());
                        item.setFkTableClassName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fk.getRefTableName()));
                        item.setIsFk(1);
                    }
                }
                item.setMbDbType(this.jdbc2MbTypeService.getType(item.getDbType()));
                return item;
            }).collect(Collectors.toList());
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(tableBean));
            jsonObject.put("author", author);
            jsonObject.put("time", dateFormat);
            jsonObject.put("fList", fieldBeans);
            jsonObject.put("dollar", "$");
            jsonObject.put("sharp", "#");
            jsonObject.put("clsUpp", tableBean.getClassName());
            jsonObject.put("table", tableBean);
            jsonObject.put("packageName", codeCond.getPackageName());
            //主键可能是多个
            List<FieldBean> ids = fieldBeans.parallelStream().filter(item -> item.getIsPk() == 1).collect(Collectors.toList());
            jsonObject.put("ids", ids);
            jsonObject.put("id", ids.get(0)); //简化,一般只有一个主键
            //外键可能是多个
            jsonObject.put("fks", fieldBeans.parallelStream().filter(item -> item.getIsFk() == 1).collect(Collectors.toList()));
            jsonObject.put("fkLabels", fkLabelFieldList);

            jsonObject.put("pks", pks);
            //查询语句
            jsonObject.put("selectFields", fieldBeans.parallelStream().map(item -> "t.".concat(item.getName())).collect(Collectors.joining(",")));
            jsonObject.put("selectFillParams", fieldBeans.parallelStream().map(item -> "vo.get".concat(item.getUpperCamel()).concat("()")).collect(Collectors.joining(",")));
            jsonObject.put("selectParams", fieldBeans.parallelStream().map(item -> item.getName().concat("=?")).collect(Collectors.joining(",")));
            jsonObject.put("selectParamsColon", fieldBeans.parallelStream().map(item -> ":".concat(item.getLowerCamel())).collect(Collectors.joining(",")));
            //添加或者修改
            jsonObject.put("updateFields", fieldBeans.parallelStream().filter(item -> item.getIsPk() == 0).map(item -> item.getName().concat("=?")).collect(Collectors.joining(",")));
            jsonObject.put("updateParams", fieldBeans.parallelStream().filter(item -> item.getIsPk() == 0).map(item -> "vo.get".concat(item.getUpperCamel()).concat("()")).collect(Collectors.joining(",")));
            jsonObject.put("insertFields", fieldBeans.parallelStream().filter(item -> item.getIsPk() == 0).map(FieldBean::getName).collect(Collectors.joining(",")));
            jsonObject.put("insertParams", fieldBeans.parallelStream().filter(item -> item.getIsPk() == 0).map(item -> "?").collect(Collectors.joining(",")));
            jsonObject.put("insertParamsColon", fieldBeans.parallelStream().filter(item -> item.getIsPk() == 0).map(item -> ":".concat(item.getLowerCamel())).collect(Collectors.joining(",")));
            jsonObject.put("allFields", fieldBeans.parallelStream().map(FieldBean::getName).collect(Collectors.joining(",")));
            jsonObject.put("allFieldsParams", fieldBeans.parallelStream().map(item -> "?").collect(Collectors.joining(",")));
            //外键关联表等
            jsonObject.put("refFks", refs);
            //1.外键对应的标签字段 类型 注释

            //2.外键对应的表 连接的字段
            //3.查询的字段列表
            //4.查询的参数

            //导入
            VelocityUtil.parseEntityTemplate(vms, outPath, jsonObject, codeCond.getPackageName(), tableBean.getClassName().toLowerCase(), suffix, fileEngine);
        }
        //根据信息生成数据
        File file = new File(outPath);
        String fileName = file.getAbsolutePath().concat(".zip");
        Utils.createZip(outPath, fileName, true);
        return uuid;
    }

    @Transactional
    public void initData(String path) {
        try (InputStream is = this.getClass().getResourceAsStream(path)) {
            byte[] buff = new byte[is.available()];
            is.read(buff);
            String sql = new String(buff);
            this.genCodeDao.executeSql(sql);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public boolean executeSql(String sql) {
        return this.genCodeDao.executeSql(sql);
    }

    @Transactional
    public boolean executeSqlFile(File sqlFile) {
        return this.genCodeDao.executeSqlFileByAnt(sqlFile);
    }

    /**
     * 预览sql
     *
     * @param entityConfig
     * @return
     */
    public String designPreview(EntityConfig entityConfig) throws IOException {
        File tempTempFile = new File(this.templateDir);
        VelocityEngine engine = VelocityUtil.fileVelocityEngine(tempTempFile.getParentFile().getAbsolutePath());
        return VelocityUtil.parse("sqltemplates/Mysql_createTable.vm", entityConfig, engine);
    }

    /**
     * 查询数据库表
     *
     * @param tableName
     * @return
     */
    public List<RefTableDto> loadRefTables(String tableName) {
        //循环查询
        return this.genCodeDao.queryRefTables(tableName).parallelStream().map(item -> {
            item.setFields(Lists.newArrayList(item.getCols().split(",")));
            return item;
        }).collect(Collectors.toList());
    }

    /**
     * 查询属性
     *
     * @param tableName
     * @return
     */
    public List<AlvinGenCodeRef> loadFields(String tableName) {
        List<AlvinGenCodeRef> refFieldDtos = this.genCodeDao.getRefFields(tableName);
        Map<String, AlvinGenCodeRef> refMap = Maps.newHashMap();
        for (AlvinGenCodeRef fieldDto : refFieldDtos) {
            refMap.put(fieldDto.getColName(), fieldDto);
        }
        //去查询设置关系
        this.alvinGenCodeRefDao.queryListByTableName(tableName).forEach(item -> {
            AlvinGenCodeRef ref = refMap.get(item.getColName());
            ref.setRefColName(item.getRefColName());
            ref.setRefTableName(item.getRefTableName());
        });
        return refFieldDtos;
    }

    /**
     * 表格数据设置
     *
     * @param settings
     * @return
     */
    @Transactional
    public int tableSettings(TableSettingsDto settings) {
        //修改显示标签字段
        AlvinGenCodeConfig alvinGenCodeConfig = new AlvinGenCodeConfig();
        alvinGenCodeConfig.setTableName(settings.getTableName());
        alvinGenCodeConfig.setLabelName(settings.getLabelCol());
        int res = this.genCodeConfigDao.save(alvinGenCodeConfig);
        this.alvinGenCodeRefDao.batchInsert(settings);
        return res;
    }

//    public String backup(String struts) throws Exception {
//        String cmd = "mysqldump  " + struts + " " + database + "  -u" + username + " " + " -p" + password + " -h " + host;
//        File file = new File("db_backup", UUID.randomUUID().toString());
//        if (!file.exists()) {
//            file.getParentFile().mkdirs();
//        }
//        try {
//            int res = CMDUtils.execute(cmd + " > " + file.getAbsolutePath(), new StringBuffer(), new StringBuffer());
//            if (res == 0) {
//                return file.getAbsolutePath();
//            }
//            throw new Exception("导出出现故障");
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    public String exportStruts() throws Exception {
//        return backup("-d");
//    }
}
