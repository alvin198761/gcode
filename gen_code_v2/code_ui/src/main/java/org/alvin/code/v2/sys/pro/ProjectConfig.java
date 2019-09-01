package org.alvin.code.v2.sys.pro;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 项目配置
 */
@Data
public class ProjectConfig {

    private String name; //项目名称
    private String srcDir; //源文件根目录,也是包的开始位置
    private String date; //创建日期
    private String author; //作者
    private List<EntityConfig> entities = Lists.newArrayList(); //实体类列表
    private String remark; //备注
    private String templateName; //生成用的模板
    private JSONObject dynAttrs = new JSONObject(); //动态属性
    private DbConfig dbConfig; //数据库配置

}
