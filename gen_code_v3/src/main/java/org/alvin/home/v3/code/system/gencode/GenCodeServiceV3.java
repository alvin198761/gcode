package org.alvin.home.v3.code.system.gencode;

import com.google.common.base.CaseFormat;
import org.alvin.home.v3.code.beans.TableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenCodeServiceV3 {

    @Autowired
    private GenCodeDaoV3 genCodeDaoV3;

    /**
     * 获取表格数据
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
}
