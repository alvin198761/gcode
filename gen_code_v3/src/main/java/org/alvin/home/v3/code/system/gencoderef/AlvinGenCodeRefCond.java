package org.alvin.home.v3.code.system.gencoderef;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @类说明: t_alvin_gen_code_ref--查询条件实体类
 * @author: 唐植超
 * @date : 2020-02-15 11:28:05
 **/
@Setter
@Getter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlvinGenCodeRefCond {

    //属性
    private Long id;//  主键
    private String tableSchema;//  数据库
    private String tableName;//  表名称
    private String colName;//  字段名
    private String refTableName;//  关联表
    private String refColName;//  关联字段
    // private List<Long> ids;// 主键列表
}