package org.alvin.springjdbc.system.address;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "AddressFk", description = "收寄信息实体")
public class AddressFk extends Address {



    //此处可添加查询显示辅助字段

}