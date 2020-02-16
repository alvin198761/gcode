package org.alvin.code.gen.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 下拉框选项
 */
@ApiModel(description = "下拉框选项实体")
@Data
public class SelectOption<T> {

    @ApiModelProperty(value = "值", notes = "值")
    private T value;
    @ApiModelProperty(value = "显示文本", notes = "显示文本")
    private String label;

}
