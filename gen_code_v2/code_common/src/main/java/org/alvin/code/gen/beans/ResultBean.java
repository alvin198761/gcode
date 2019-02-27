package org.alvin.code.gen.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 兼容领歌代码
 * @param <T>
 */
@ApiModel(description = "响应结果实体")
@Data
public class ResultBean<T> {

	@ApiModelProperty(value = "失败原因", notes = "失败内容")
	private String resultMsg;
	@ApiModelProperty(value = "成功响应结果实体", notes = "错误信息", required = true)
	private Integer resultCode;
	@ApiModelProperty(value = "成功响应结果实体", notes = "错误")
	private T resultData;
	public ResultBean() {
	}

	public ResultBean(T data) {
		this.resultMsg = null;
		this.resultCode = 200;
		this.resultData = data;
	}

	public ResultBean(Integer resultCode, String resultMsg) {
		this.resultMsg = resultMsg;
		this.resultCode = resultCode;
		this.resultData = null;
	}

	public ResultBean(Integer resultCode, String resultMsg, T resultData) {
		this.resultMsg = resultMsg;
		this.resultCode = resultCode;
		this.resultData = resultData;
	}

}