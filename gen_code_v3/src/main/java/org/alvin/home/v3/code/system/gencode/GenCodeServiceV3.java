package org.alvin.home.v3.code.system.gencode;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import org.alvin.home.v3.code.beans.FieldBean;
import org.alvin.home.v3.code.beans.TableBean;
import org.alvin.home.v3.code.system.JDBC2MbTypeService;
import org.alvin.home.v3.code.system.VmFileService;
import org.alvin.home.v3.code.utils.VelocityUtil;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GenCodeServiceV3 {

    @Autowired
    private GenCodeDaoV3 genCodeDaoV3;
    @Value("${gencode.template.dir}")
    private String templateDir;
    @Autowired
    private VmFileService vmFileService;
    @Autowired
    private JDBC2MbTypeService jdbc2MbTypeService;

    /**
     * 获取表格数据
     *
     * @return
     */
    public List<TableBean> queryTables() {
        return this.genCodeDaoV3.queryTables().parallelStream().map(item -> {
            item.setClassName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, item.getTableName()));
            item.setVarName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, item.getTableName()));
            item.setCnName(item.getComment());
            return item;
        }).collect(Collectors.toList());
    }

    /**
     * 获取项目模板列表
     *
     * @return
     */
    public List<String> getTemplates() {
        return null;
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
        Assert.isTrue(vms.isEmpty(), "没有扫描到模板");
        //根据选择的表 查询表信息
        for (TableBean tableBean : codeCond.getTables()) {
            //查询字段
            List<FieldBean> fieldBeans = this.genCodeDaoV3.getFields(tableBean.getTableName()).parallelStream().map(item -> {
                item.setLowerCamel(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, item.getName()));
                item.setUpperCamel(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, item.getName()));
                item.setLowerUnderscore(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getUpperCamel()));
                if (item.getAllTypeName().contains(".")) {
                    int index = item.getAllTypeName().lastIndexOf(".");
                    item.setType(item.getAllTypeName().substring(index + 1));
                } else {
                    item.setType(item.getAllTypeName());
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
            //主键可能是多个
            jsonObject.put("id", fieldBeans.parallelStream().filter(FieldBean::isPk).collect(Collectors.toList()));
            //外键可能是多个
            jsonObject.put("fk", fieldBeans.parallelStream().filter(FieldBean::isFk).collect(Collectors.toList()));
            //查询语句
            jsonObject.put("selectFields", fieldBeans.parallelStream().map(item -> "t.".concat(item.getName())).collect(Collectors.joining(",")));
            jsonObject.put("selectFillParams", fieldBeans.parallelStream().map(item -> "vo.get".concat(item.getUpperCamel())).collect(Collectors.joining(",")));
            jsonObject.put("selectParams", fieldBeans.parallelStream().map(item -> item.getName().concat("=?")).collect(Collectors.joining(",")));
            jsonObject.put("selectParamsColon", fieldBeans.parallelStream().map(item -> ":".concat(item.getLowerCamel())).collect(Collectors.joining(",")));
            //添加或者修改
            jsonObject.put("updateFields", fieldBeans.parallelStream().filter(item -> !item.isPk()).map(item -> item.getName().concat("=?")).collect(Collectors.joining(",")));
            jsonObject.put("updateParams", fieldBeans.parallelStream().filter(item -> item.isPk()).map(item -> "vo.get".concat(item.getUpperCamel()).concat("()")).collect(Collectors.joining(",")));
            jsonObject.put("insertFields", fieldBeans.parallelStream().filter(item -> !item.isPk()).map(FieldBean::getName).collect(Collectors.joining(",")));
            jsonObject.put("insertParams", fieldBeans.parallelStream().filter(item -> !item.isPk()).map(item -> "?").collect(Collectors.joining(",")));
            jsonObject.put("insertParamsColon", fieldBeans.parallelStream().filter(item -> !item.isPk()).map(item -> ":".concat(item.getLowerCamel())).collect(Collectors.joining(",")));
            //导入
            jsonObject.put("imports", fieldBeans.parallelStream().filter(item -> item.getAllTypeName().startsWith("java[.]lang")).collect(Collectors.toList()));
            VelocityUtil.parseEntityTemplate(vms, outPath, jsonObject, codeCond.getPackageName(), tableBean.getClassName().toLowerCase(), suffix, fileEngine);
        }
        //根据信息生成数据
        return null;
    }
}
