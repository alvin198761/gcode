package org.alvin.code.gen.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "响应结果实体")
@Data
public class RestfullResp<T> {

	@ApiModelProperty(value = "失败原因", notes = "失败内容")
	private String errorMsg;
	@ApiModelProperty(value = "成功响应结果实体", notes = "错误信息", required = true)
	private Integer code;
	@ApiModelProperty(value = "成功响应结果实体", notes = "错误")
	private T data;

	public RestfullResp() {
	}

	public RestfullResp(T data) {
		this.errorMsg = null;
		this.data = data;
		this.code = 0;
	}

	public RestfullResp(Integer code, String errorMsg) {
		this.errorMsg = errorMsg;
		this.code = code;
		this.data = null;
	}

	public RestfullResp(Integer code, String errorMsg, T data) {
		this.errorMsg = errorMsg;
		this.code = code;
		this.data = data;
	}

}