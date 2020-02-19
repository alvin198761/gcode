package org.alvin.springjdbc.system.maintable;

/**
* @类说明: 主表--实体类
* @author: 唐植超
* @date : 2020-02-19 23:05:35
**/
@lombok.Setter
@lombok.Getter
@lombok.experimental.Accessors(chain = true)
@lombok.Builder	
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class MainTable {

    //数据库中的字段
        private Long id ;//  主键
        private String mainTitle ;//  显示文本
        private Long aId ;//  a表主键
        private Long bId ;//  b表主键
        private java.util.Date date ;//  日期
    
    //此处可添加查询显示辅助字段

}