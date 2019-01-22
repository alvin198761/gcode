package org.alvin.code.v2.sys.mock;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import org.alvin.code.v2.core.utils.VelocityUtil;
import org.alvin.code.v2.sys.mock.bean.ActionBean;
import org.alvin.code.v2.sys.mock.bean.ActionMethodBean;
import org.alvin.code.v2.sys.mock.bean.JSMockApiConfig;
import org.alvin.code.v2.sys.mock.service.HttpClientService;
import org.alvin.utils.zip.ZipUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JsMockBus implements InitializingBean {

	@Autowired
	private HttpClientService httpClientService;

	public List<JSONObject> queryList(JSMockApiConfig config) {
		String html = httpClientService.get(config.getUrl());
		JSONObject jsonObject = JSONObject.parseObject(html);
		return jsonObject.getJSONArray("tags").stream().map(item -> (JSONObject) item).collect(Collectors.toList());
	}


	public String genCode(JSMockApiConfig config) throws Exception {
//		Method method = this.methodMap.get(config.getCtype());
//		if (method == null) {
//			throw new Exception("没实现的客户端");
//		}
		String html = httpClientService.get(config.getUrl());
		html = html.replaceAll("[$]ref", "_ref");
		JSONObject jsonObject = JSONObject.parseObject(html);
		String dir = "D:/mock_gen_dir";
		new File(dir).mkdirs();

		String templateName = "/swgclitemplate/Model" + config.getCtype() + "Api_js.vm";
		config.getTags().stream().forEach(item -> {
			ActionBean actionBean = new ActionBean();
			actionBean.setAuthor("唐植超");
			actionBean.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			actionBean.setActionName(item.getString("name"));
			actionBean.setNote(item.getString("description"));
			createActionMethods(actionBean, jsonObject.getJSONObject("paths"));
			File output = new File(dir + "/" + actionBean.getActionName() + ".js");
			VelocityUtil.parse(templateName, actionBean, output.getAbsolutePath(), VelocityUtil.classPathVelocityEngine());
		});
		String file = "D:/swagger_api_client_mock.zip";
		ZipUtils.doCompress(dir, file);
		System.gc();
		File dirFile = new File(dir);
		//只有两层
		for (File f : dirFile.listFiles()) {
			for (File subF : f.listFiles()) {
				subF.delete();
			}
			f.delete();
		}
		dirFile.delete();
		return file;

	}

	/**
	 * 根据tag 拼装 method
	 *
	 * @param actionBean
	 * @param paths
	 * @return
	 */
	private void createActionMethods(ActionBean actionBean, JSONObject paths) {
		List<ActionMethodBean> methods = Lists.newArrayList();
		for (Map.Entry<String, Object> pathEntry : paths.entrySet()) {
			if (pathEntry.getKey().equals("/")) {
				continue;
			}
			JSONObject pathJson = (JSONObject) pathEntry.getValue();

			String url = pathEntry.getKey();
			for (String mk : pathJson.keySet()) {
				JSONObject method = pathJson.getJSONObject(mk);
				if (!method.getJSONArray("tags").contains(actionBean.getActionName())) {
					continue;
				}
				methods.add(createMethod(mk, method, url));
			}

		}
		actionBean.setMethods(methods);
	}

	/**
	 * 构建方法
	 *
	 * @param method
	 * @return
	 */
	private ActionMethodBean createMethod(String mk, JSONObject method, String url) {
		ActionMethodBean methodBean = new ActionMethodBean();
		methodBean.setName(url.substring(url.lastIndexOf("/") + 1));
		url = "'" + url + "'";
		methodBean.setNote(method.getString("summary"));
		methodBean.setMethod(mk);
		methodBean.setDescription(method.getString("description"));
		methodBean.setConsumes(method.getJSONArray("consumes").getString(0));
		JSONArray jsonArray = method.getJSONArray("parameters");
		if (jsonArray == null) {
			return methodBean;
		}
		methodBean.setParams(new JSONObject());
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject param = jsonArray.getJSONObject(i);
			if (param.getString("in").equals("header")) {
				continue;
			}
			if (param.getString("in").equals("body")) {
				methodBean.setReqParamType("body");
				methodBean.setReqParamType("body");
				continue;
			}
			if (param.getString("in").equals("query")) {
				methodBean.setReqParamType("formdata");
				String name = param.getString("name");
				methodBean.getParams().put(name, param);
			}
			if (param.getString("in").equals("path")) {
				url += " + '/' + " + param.getString("name");
			}
		}
		methodBean.setUrl(url);
		return methodBean;
	}


	@SwaggerMockMethod("axios")
	public void genApiAxios(String dir, String actionName, JSONObject jsonObject) throws IOException {
		String fileName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, actionName.replace('-', '_'));
		Path path = Paths.get(dir, "api", fileName + "Service.js");
		path.getParent().toFile().mkdirs();
		for (Map.Entry<String, Object> pathEntry : jsonObject.entrySet()) {
			if (pathEntry.getKey().equals("/")) {
				continue;
			}
			JSONObject pathJson = (JSONObject) pathEntry.getValue();
			for (String mk : pathJson.keySet()) {
				JSONObject method = pathJson.getJSONObject(mk);
				if (!method.getJSONArray("tags").contains(actionName)) {
					continue;
				}
				String url = pathEntry.getKey();
				String methodName = url.substring(url.lastIndexOf("/") + 1);
				String data = "     data: {...payload}\n";
				if (url.contains("{")) {
					int index = url.indexOf("{");
					methodName = url.substring(0, index - 1);
					methodName = methodName.substring(methodName.lastIndexOf("/") + 1);
				}
				//地址栏参数 body参数
//				if (method.containsKey("parameters")) {
//					JSONArray jsonArray = method.getJSONArray("parameters");
//					for (int j = 0; j < jsonArray.size(); j++) {
//						JSONObject pJson = jsonArray.getJSONObject(j);
//						if (pJson.getString("in").equals("path")) {
//							data = "";
//							String name = pJson.getString("name");
//							url = url.replace("{" + name + "}", "'+payload." + name + "+'");
//						}
//						if (pJson.getString("in").equals("body")) {
//							data = "     contentType: 'application/json',\n     data: JSON.stringify(payload)";
//						}
//					}
//				}
//				sb.append("// ").append(method.getString("description")).append(System.lineSeparator());
//				String regex =
//						"export function " + methodName + "(payload) {\n" +
//								"   return axios({\n" +
//								"     url: '" + url + "',\n" +
//								"     method: '" + mk + "',\n";
//				sb.append(regex);
//				if (method.containsKey("parameters")) {
//					JSONArray jsonArray = method.getJSONArray("parameters");
//					String in = jsonArray.getJSONObject(0).getString("in");
//					String name = jsonArray.getJSONObject(0).getString("name");
//					if (in.equals("header") && name.equals("Authorization")) {
//						sb.append("     headers: {'Authorization': getGlobalToken()},\n");
//					}
//				}
//				regex = "     type: 'json',\n" + data +
//						"  })\n" +
//						" }";
//				sb.append(regex).append(System.lineSeparator());
			}
		}
//		Files.write(path, sb.toString().getBytes("utf-8"));

	}


	@SwaggerMockMethod("reqwest")
	public void genApi(String dir, String actionName, JSONObject jsonObject) throws IOException {
		String fileName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, actionName.replace('-', '_'));
		Path path = Paths.get(dir, "api", fileName + "Service.js");
		path.getParent().toFile().mkdirs();
		StringBuilder sb = new StringBuilder();
		sb.append("import reqwest from '../utils/reqwest';").append(System.lineSeparator());
		sb.append("import {getGlobalToken} from '../utils/constant';").append(System.lineSeparator());
		for (Map.Entry<String, Object> pathEntry : jsonObject.entrySet()) {
			if (pathEntry.getKey().equals("/")) {
				continue;
			}
			JSONObject pathJson = (JSONObject) pathEntry.getValue();
			for (String mk : pathJson.keySet()) {
				JSONObject method = pathJson.getJSONObject(mk);
				if (!method.getJSONArray("tags").contains(actionName)) {
					continue;
				}
				String url = pathEntry.getKey();
				String methodName = url.substring(url.lastIndexOf("/") + 1);
				String data = "     data: {...payload}\n";
				if (url.contains("{")) {
					int index = url.indexOf("{");
					methodName = url.substring(0, index - 1);
					methodName = methodName.substring(methodName.lastIndexOf("/") + 1);
				}
				//地址栏参数 body参数
				if (method.containsKey("parameters")) {
					JSONArray jsonArray = method.getJSONArray("parameters");
					for (int j = 0; j < jsonArray.size(); j++) {
						JSONObject pJson = jsonArray.getJSONObject(j);
						if (pJson.getString("in").equals("path")) {
							data = "";
							String name = pJson.getString("name");
							url = url.replace("{" + name + "}", "'+payload." + name + "+'");
						}
						if (pJson.getString("in").equals("body")) {
							data = "     contentType: 'application/json',\n     data: JSON.stringify(payload)";
						}
					}
				}
				sb.append("// ").append(method.getString("description")).append(System.lineSeparator());
				String regex =
						"export function " + methodName + "(payload) {\n" +
								"   return reqwest({\n" +
								"     url: '" + url + "',\n" +
								"     method: '" + mk + "',\n";
				sb.append(regex);
				if (method.containsKey("parameters")) {
					JSONArray jsonArray = method.getJSONArray("parameters");
					String in = jsonArray.getJSONObject(0).getString("in");
					String name = jsonArray.getJSONObject(0).getString("name");
					if (in.equals("header") && name.equals("Authorization")) {
						sb.append("     headers: {'Authorization': getGlobalToken()},\n");
					}
				}
				regex = "     type: 'json',\n" + data +
						"  })\n" +
						" }";
				sb.append(regex).append(System.lineSeparator());
			}
		}
		Files.write(path, sb.toString().getBytes("utf-8"));
	}

//	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//	private void genMock(String dir, String actionName, JSONObject root) throws IOException {
//		String fileName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, actionName.replace('-', '_'));
//		System.out.println(actionName);
////        if (actionName.equals("dyn-msg-action")) {
////            System.out.println("dyn-msg-action");
////            return;
////        }
//		Path path = Paths.get(dir, "mock", fileName + "Mock.js");
//		path.getParent().toFile().mkdirs();
//		StringBuilder sb = new StringBuilder();
//		sb.append("/*" + actionName + ",:author:tangzhichao.,DATE:" + format.format(new Date()) + "*/").append(System.lineSeparator());
//		sb.append(" 'use strict';").append(System.lineSeparator());
//		sb.append(" var Mock = require('mockjs')").append(System.lineSeparator());
//		sb.append(" var Random = Mock.Random;").append(System.lineSeparator());
//		sb.append(" module.exports = {").append(System.lineSeparator());
//		JSONObject jsonObject = root.getJSONObject("paths");
//		JSONObject definitionsObj = root.getJSONObject("definitions");
//		for (Map.Entry<String, Object> pathEntry : jsonObject.entrySet()) {
//			if (pathEntry.getKey().equals("/")) {
//				continue;
//			}
//			JSONObject pathJson = (JSONObject) pathEntry.getValue();
//			for (String mk : pathJson.keySet()) {
//				JSONObject method = pathJson.getJSONObject(mk);
//				if (!method.getJSONArray("tags").contains(actionName)) {
//					continue;
//				}
//				String url = pathEntry.getKey();
//				System.out.println(url);
//				String responseRef = method.getJSONObject("responses").getJSONObject("200").getJSONObject("schema").getString("_ref");
//				//空数据
//				String text = "'" + mk.toUpperCase() + " " + url + "': function (req, res, next) {\n" +
//						"    var data = Mock.mock({\n" +
//						"      \"code\": '0',\n" +
//						"      \"data\": " + genMockByRef(responseRef, definitionsObj) + "\n" +
//						"      \"errorMsg\": '',\n" +
//						"      \"success\": '',\n" +
//						"    });\n" +
//						"    setTimeout(function () {\n" +
//						"      res.json(data);\n" +
//						"    }, 500);\n" +
//						"  },";
//
//				sb.append(text).append(System.lineSeparator());
//			}
//		}
//		sb.append(" }").append(System.lineSeparator());
//		Files.write(path, sb.toString().getBytes("utf-8"));
//	}

	/**
	 * @param responseRef
	 * @return
	 */
	private String genMockByRef(String responseRef, JSONObject definitionsObj) {
		if (responseRef == null) {
			return "'@integer(1,100)',";
		}
		responseRef = responseRef.substring("#/definitions/".length());
		JSONObject targetRef = definitionsObj.getJSONObject(responseRef);
		JSONObject dataObj = targetRef.getJSONObject("properties").getJSONObject("data");
		if (dataObj == null) {
			JSONObject properties = targetRef.getJSONObject("properties");
			StringBuilder sb = new StringBuilder(); //这段代码可以复用
			sb.append("{");
			for (Map.Entry<String, Object> entry : properties.entrySet()) {
				JSONObject fieldObj = (JSONObject) entry.getValue();
				String type = fieldObj.getString("type");
				sb.append("'" + entry.getKey() + "':").append(createBaseClassMockByType(type, definitionsObj, fieldObj)).append(System.lineSeparator());
			}
			sb.append("},");
			return sb.toString();
		}
		String type = dataObj.getString("type");
		if (type == null) {
			String ref = targetRef.getJSONObject("properties").getJSONObject("data").getString("_ref");
			return createObjectRef(ref, definitionsObj);
		}
		return createBaseClassMockByType(type, definitionsObj, dataObj);
	}

	private String createBaseClassMockByType(String type, JSONObject definitionsObj, JSONObject dataObj) {
		if (type == null) {
			String ref = dataObj.getString("_ref");
			if (ref == null) {
				return "{},";
			}
			return createObjectRef(ref, definitionsObj);
		}
		if (type.equals("number")) {
			return "'@integer(1,100)',";
		}
		if (type.equals("integer")) {
			return "'@integer(1,100)',";
		}
		if (type.equals("string")) {
			if (dataObj.containsKey("format")) {
				String format = dataObj.getString("format");
				if (format.equals("date-time")) {
					return "new Date(),";
				}
			}
			return "'@word(1,100)',";
		}
		if (type.equals("boolean")) {
			return "'@word(1,100)',";
		}
		if (type.equals("array")) {
			String ref = dataObj.getJSONObject("items").getString("_ref");
			String itemType = dataObj.getJSONObject("items").getString("type");
			if (ref != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(System.lineSeparator()).append(createObjectRef(ref, definitionsObj)).append(System.lineSeparator());
				return "[" + sb.toString() + "],";
			} else {
				return mockArrayByItemType(itemType);
			}
		}
		return "{},";
	}

	private String mockArrayByItemType(String type) {
		if (type.equals("number")) {
			return "[1,2,3],";
		}
		if (type.equals("integer")) {
			return "[1,2,3],";
		}
		if (type.equals("string")) {
			return "['test string'],";
		}
		if (type.equals("boolean")) {
			return "[true],";
		}
		if (type.equals("object")) {
			return "[{}],";
		}
		return "[],";
	}

	private String createObjectRef(String targetRef, JSONObject definitionsObj) {
		targetRef = targetRef.substring("#/definitions/".length());
		if ("File".equals(targetRef)) {
			return "{},";
		}
		if ("Department".equals(targetRef)) {
			return "{}";
		}
		JSONObject targetJsonObject = definitionsObj.getJSONObject(targetRef);
		JSONObject propertiesObj = targetJsonObject.getJSONObject("properties");
		if (propertiesObj == null) {
			return "{}";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (Map.Entry<String, Object> entry : propertiesObj.entrySet()) {
			JSONObject fieldObj = (JSONObject) entry.getValue();
			String type = fieldObj.getString("type");
			sb.append("'" + entry.getKey() + "':").append(createBaseClassMockByType(type, definitionsObj, fieldObj)).append(System.lineSeparator());
		}
		sb.append("},");
		return sb.toString();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
//		Method[] ms = this.getClass().getMethods();
//		for (Method m : ms) {
//
//		}
	}
}
