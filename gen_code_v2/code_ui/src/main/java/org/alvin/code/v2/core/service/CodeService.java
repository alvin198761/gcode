package org.alvin.code.v2.core.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.alvin.code.gen.beans.RestfullResp;
import org.alvin.code.v2.core.dao.CodeDao;
import org.alvin.code.v2.core.model.CodeCond;
import org.alvin.code.v2.core.model.Field;
import org.alvin.code.v2.core.model.Table;
import org.alvin.code.v2.core.utils.Utils;
import org.alvin.code.v2.core.utils.VelocityUtil;
import org.alvin.code.v2.sys.pro.EntityConfig;
import org.alvin.code.v2.sys.pro.FieldConfig;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @author gzz_gzz@163.com
 * @功能描述:生成列表类型代码的实现类
 * @date 2018-02-15
 */
@Service
@Slf4j
public class CodeService {
    @Autowired
    protected CodeDao dao;
    @Autowired
    private VmFileService vmFileService;
    @Value("${db_name}")
    private String table_schema;
    @Autowired
    private JDBC2MbTypeService jdbc2MbTypeService;

    private static final File outBaseDir = new File("../../templates/gen_templates/codetemplate");

    public RestfullResp<Map<String, Object>> create(CodeCond cond) throws Exception {
        cond.setPackageName("com.szmj.logistics.energy_calc.system");
        String suffix = ".vm";
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        VelocityEngine fileEngine = VelocityUtil.fileVelocityEngine(outBaseDir.getCanonicalPath());
        List<String> vms = vmFileService.scanTemplate(outBaseDir.getAbsolutePath(), suffix, cond.getTemplateDirs());
        String uuid = UUID.randomUUID().toString();
        String outPath = "dist/" + uuid;
        if (vms.isEmpty()) {
            return new RestfullResp<>(1, "没有扫描到模板");
        }
        for (Table table : cond.getC_list()) {
            String auth = cond.getAuthor();// 作者
            //用工参数
            cond.setT_name_eq(table.getT_name());// 表名
            List<Field> fList = dao.queryFields(cond).stream().map(item -> {
                item.setBigName(Utils.firstUpper(item.getName()));
                item.setLower_camel(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, item.getName()));
                item.setUpper_camel(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, item.getName()));
                if (item.getAllTypeName().indexOf(".") != -1) {
                    int index = item.getAllTypeName().lastIndexOf(".");
                    item.setType(item.getAllTypeName().substring(index + 1));
                } else {
                    item.setType(item.getAllTypeName());
                }
                item.setMb_db_type(this.jdbc2MbTypeService.getType(item.getDbType()));
                return item;
            }).collect(Collectors.toList());// 字段列表
            String cName = table.getC_name();// 表注释中文名
            String upp = table.getCls_upp();// 驼峰类名(首字母大写)
            String low = upp.toLowerCase();// 类名小写(只用包路径)
            String lowUpp = Utils.firstLower(upp);// 驼峰变量类名(首字母小写)
            String idType = Utils.keyType(fList);// 主键数据类型
            Field idField = fList.stream().filter(item -> item.getColumn_key().equals("PRI")).findFirst().get();

            //组装对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fList", fList);
            jsonObject.put("auth", auth);
            jsonObject.put("cName", cName);
            jsonObject.put("lowUpp", lowUpp);
            jsonObject.put("idType", idType);
            jsonObject.put("id", idField);
            jsonObject.put("cond", cond);
            jsonObject.put("table", table.getT_name());
            jsonObject.put("tName", table.getT_name());
            jsonObject.put("comment", table.getComment());
            jsonObject.put("c_name", table.getC_name());
            jsonObject.put("idName", idField.getName());
            //类名称
            jsonObject.put("clsUpp", upp);
            jsonObject.put("upp", upp);
            jsonObject.put("time", dateFormat);
            //各种参数追加
            jsonObject.put("selectFields", Utils.add(fList, "t.", ",", false, "select"));
            jsonObject.put("insertFields", Utils.add(fList, "", ",", true, "insert"));
            jsonObject.put("insertValuesFields", Utils.addV2(fList, ":", ",", true, "insert"));
            jsonObject.put("replaceFields", Utils.add(fList, "", ",", false, "sql"));
            jsonObject.put("replaceValuesFields", Utils.add(fList));
            jsonObject.put("paramsFields", Utils.addV1(fList, "vo.get", "(),", false));
            jsonObject.put("updateFields", Utils.add(fList, "", "=?,", true, "sql"));
            jsonObject.put("updateParams", Utils.addV1(fList, "vo.get", "(),", true) + ",vo.get" + idField.getName() + "()");
            jsonObject.put("selectItems", Utils.add(fList, "t.", ","));
            jsonObject.put("caseMapper", Utils.caseMapper(fList));
            //v2 需要兼容的东西
            jsonObject.put("paramsFieldsV2", Utils.addV2(fList, "vo.get", "(),", false));
            jsonObject.put("updateParamsV2", Utils.addV2(fList, "vo.get", "(),", true) + ",vo.get" + idField.getUpper_camel() + "()");
            //其他附属数据
            List<String> importList = Lists.newArrayList();
            importList.add(Utils.dateImport(fList));
            importList.add(Utils.bigImport(fList));
            jsonObject.put("importList", importList);
            //
            jsonObject.put("dollar", "$");
            jsonObject.put("sharp", "#");
            //java 代码生成
            VelocityUtil.parseEntityTemplate(vms, outPath, jsonObject, cond.getPackageName(), low, suffix, fileEngine);
        }
        Map<String, Object> res = Maps.newHashMap();
        File file = new File(outPath);
        String fileName = file.getAbsolutePath().concat(".zip");
        Utils.createZip(outPath, fileName, true);
        res.put("download_url", uuid);
        return new RestfullResp<>(res);
    }


    /**
     * @功能描述: 查询数据库中表名列表
     */
    public List<Table> queryTables(CodeCond para) {
        para.setTable_schema(this.table_schema);
        List<Table> list = dao.queryTables(para);
        list.forEach(item -> {
            item.setCls_upp(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, Utils.delFirWord(item.getT_name())));
            item.setC_name(item.getComment());
        });
        return list;
    }

    /**
     * @功能描述: 查询数据库中字段名列表
     */
    public List<Field> queryFields(CodeCond cond) {
        return dao.queryFields(cond);
    }

    public List<FieldConfig> queryFieldConfig(CodeCond cond) {
        return this.dao.queryFieldConfig(cond.getT_name_eq());
    }

    public void executeSql(String sql) {
        if (Strings.isNullOrEmpty(sql)) {
            return;
        }
        dao.executeSqlCmdByAnt(sql);
    }

    /**
     * 获取模板的子目录列表
     */
    public List<String> templateDirs() {
        return Lists.newArrayList(outBaseDir.list());
    }

    /**
     * 预览sql
     *
     * @param entityConfig
     * @return
     */
    public String designPreview(EntityConfig entityConfig) throws IOException {
        VelocityEngine engine = VelocityUtil.fileVelocityEngine(outBaseDir.getParentFile().getAbsolutePath());
        return VelocityUtil.parse("sqltemplates/Mysql_createTable.vm", entityConfig, engine);

//        return VelocityUtil.parse("/sqltemplates/Mysql_createTable.vm", entityConfig, VelocityUtil.classPathVelocityEngine());
    }

    /**
     * 导出表结构
     *
     * @return
     */
    public String exportStruts() throws Exception {
        return this.dao.exportStruts();
    }

    /**
     * 导出结构和数据
     *
     * @return
     */
    public String exportData() throws Exception {
        return this.dao.backup("");
    }
}
