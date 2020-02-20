package org.alvin.springjdbc.system.bbtest;

/**
* @类说明: 测试表B--实体类
* @author: 唐植超
* @date : 2020-02-20 20:09:06
**/
@lombok.Setter
@lombok.Getter
@lombok.experimental.Accessors(chain = true)
@lombok.Builder	
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class BbTest {

    //数据库中的字段
        private Long id ;//  主键
        private String title ;//  标题
    
    //此处可添加查询显示辅助字段

}