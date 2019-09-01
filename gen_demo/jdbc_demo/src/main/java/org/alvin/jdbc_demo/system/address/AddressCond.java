package org.alvin.jdbc_demo.system.address;


import lombok.*;
import lombok.experimental.Accessors;
import org.alvin.code.jdbc.beans.BaseCondition;

import java.util.List;


/**
 * @类说明: 收寄信息--查询条件实体类
 * @author: 唐植超
 * @date : 2019-08-09 19:18:43
 **/
@Setter
@Getter
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressCond extends BaseCondition {


    /**
     * @方法说明: 拼加自定义条件
     **/
    @Override
    public void addCondition() {
        add(id, " AND t.id = ? ");
        add(type, " AND t.type = ? ");
        add(targetPhone, " AND t.target_phone LIKE ? ", 3);
        add(targetAddr, " AND t.target_addr LIKE ? ", 3);
        add(targetName, " AND t.target_name LIKE ? ", 3);
        add(targetId, " AND t.target_id = ? ");
        add(remark, " AND t.remark LIKE ? ", 3);
        add(ids, "AND t.id IN ");
    }

    //feilds
    private Long id;//  主键
    private Byte type;//  类型
    private String targetPhone;//  电话
    private String targetAddr;//  地址
    private String targetName;//  姓名
    private Long targetId;//  收寄方id
    private String remark;//  备注

    private List<Long> ids;// 主键列表
}