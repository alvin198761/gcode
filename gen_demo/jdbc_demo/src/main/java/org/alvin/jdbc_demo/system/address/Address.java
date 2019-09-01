package org.alvin.jdbc_demo.system.address;


import lombok.*;
import lombok.experimental.Accessors;

/**
 * @类说明: 收寄信息--实体类
 * @author: 唐植超
 * @date : 2019-08-09 19:18:43
 **/
@Setter
@Getter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {

    //数据库中的字段
    private Long id;//  主键
    private Byte type;//  类型
    private String targetPhone;//  电话
    private String targetAddr;//  地址
    private String targetName;//  姓名
    private Long targetId;//  收寄方id
    private String remark;//  备注

    //此处可添加查询显示辅助字段

}