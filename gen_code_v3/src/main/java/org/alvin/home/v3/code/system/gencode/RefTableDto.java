package org.alvin.home.v3.code.system.gencode;

import lombok.Data;

import java.util.List;

@Data
public class RefTableDto {

    private String tableName; //表名称
    private String cols;//列字符
    private List<String> fields; //所有字段
}
