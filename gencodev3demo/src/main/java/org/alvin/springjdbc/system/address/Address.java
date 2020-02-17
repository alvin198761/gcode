package org.alvin.springjdbc.system.address;

/**
* @类说明: 收寄信息--实体类
* @author: 唐植超
* @date : 2020-02-17 22:40:15
**/
@lombok.Setter
@lombok.Getter
@lombok.experimental.Accessors(chain = true)
@lombok.Builder	
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Address {

    //数据库中的字段
        private Long id ;//  主键
        private Byte type ;//  类型
        private String targetPhone ;//  电话
        private String targetAddr ;//  地址
        private String targetName ;//  姓名
        private Long targetId ;//  收寄方id
        private String remark ;//  备注
    
    //此处可添加查询显示辅助字段

}