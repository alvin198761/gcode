package org.alvin.springjdbc.system.aatable;

/**
* @类说明: 测试A--实体类
* @author: 唐植超
* @date : 2020-02-19 23:05:35
**/
@lombok.Setter
@lombok.Getter
@lombok.experimental.Accessors(chain = true)
@lombok.Builder	
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class AaTable {

    //数据库中的字段
        private Long id ;//  主键
        private String tip ;//  提示文本
        private java.util.Date date ;//  日期
    
    //此处可添加查询显示辅助字段

}