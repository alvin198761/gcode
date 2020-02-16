package org.alvin.home.v3.code.system.gencodeconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
/**
* @类说明: t_alvin_gen_code_config--实体类
* @author: 唐植超
* @date : 2020-02-14 20:14:36
**/
@Setter
@Getter
@Accessors(chain = true)
@Builder	
@AllArgsConstructor
@NoArgsConstructor
public class AlvinGenCodeConfig {

    //数据库中的字段
        private Long id ;//  主键
        private String tableSchema ;//  数据库
        private String tableName ;//  表名称
        private String labelName ;//  显示字段名
    
    //此处可添加查询显示辅助字段

}