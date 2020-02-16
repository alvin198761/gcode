package org.alvin.springjdbc.system.address;


import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;


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
@ApiModel(value = "AddressCond", description = "收寄信息查询条件实体")
public class AddressFkCond extends AddressCond {

    /**
     * @方法说明: 拼加自定义条件
     **/
    @Override
    public void addCondition() {
       super.addCondition();
    }

}