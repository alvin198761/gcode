package org.alvin.home.v3.code.system.gencode;

import lombok.Data;
import org.alvin.home.v3.code.beans.TableBean;

import java.util.List;

@Data
public class GenCodeCond {

    private List<TableBean> tables; //选择的表
    private List<String> templateDirs; //选择的模板
    private String packageName; // 生成的包名
    private String author; //作者名称
}
