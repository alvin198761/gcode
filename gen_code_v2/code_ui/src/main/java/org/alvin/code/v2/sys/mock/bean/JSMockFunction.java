package org.alvin.code.v2.sys.mock.bean;

import lombok.Data;

import java.util.Map;

@Data
public class JSMockFunction {

	private String name;
	private String url;
	private String method;
	private String description;
	private Map<String, Object> headers;
	private Map<String, Object> params;
	private Map<String, Object> response;

}
