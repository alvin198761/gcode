package org.alvin.code.gen.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 树节点
 */
@ApiModel(description = "树节点实体")
@Data
public class TreeNode<T> extends SelectOption {

    @ApiModelProperty(value = "子节点", notes = "子节点")
    private List<TreeNode> children;
}
