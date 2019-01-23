package org.alvin.code.v2.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Files;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;
import org.apache.velocity.runtime.resource.loader.JarResourceLoader;

import java.io.File;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Properties;

public class VelocityUtil {

	/**
	 * 通过class path 加载
	 *
	 * @return
	 */
	public static VelocityEngine classPathVelocityEngine() {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.setProperty(Velocity.ENCODING_DEFAULT, "utf-8");
		ve.setProperty(Velocity.INPUT_ENCODING, "utf-8");
		ve.setProperty(Velocity.OUTPUT_ENCODING, "utf-8");
		ve.init();
		return ve;
	}

	/**
	 * 通过文件加载
	 *
	 * @return
	 */
	public static VelocityEngine fileVelocityEngine(String path) {
		//初始化参数
		Properties properties = new Properties();
		//设置velocity资源加载方式为file
		properties.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
		//设置velocity资源加载方式为file时的处理类
		properties.setProperty("file.resource.loader.class", FileResourceLoader.class.getName());
		properties.setProperty(Velocity.ENCODING_DEFAULT, "utf-8");
		properties.setProperty(Velocity.INPUT_ENCODING, "utf-8");
		properties.setProperty(Velocity.OUTPUT_ENCODING, "utf-8");
		properties.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, path);
		//实例化一个VelocityEngine对象
		return new VelocityEngine(properties);
	}

	/**
	 * 通过jar 加载
	 *
	 * @param jarURI
	 * @return
	 */
	public static VelocityEngine jarVelocityEngine(String jarURI) {
		//初始化参数
		Properties properties = new Properties();
		//设置velocity资源加载方式为jar
		properties.setProperty("resource.loader", "jar");
		//设置velocity资源加载方式为file时的处理类
		properties.setProperty("jar.resource.loader.class", JarResourceLoader.class.getName());
		properties.setProperty(Velocity.ENCODING_DEFAULT, "utf-8");
		properties.setProperty(Velocity.INPUT_ENCODING, "utf-8");
		properties.setProperty(Velocity.OUTPUT_ENCODING, "utf-8");
		//设置jar包所在的位置 jarURI "jar:file:WebRoot/WEB-INF/lib/vm.jar"
		properties.setProperty("jar.resource.loader.path", jarURI);
		//实例化一个VelocityEngine对象
		return new VelocityEngine(properties);
	}

	/**
	 * 转换
	 *
	 * @param templateName
	 * @param obj
	 * @param outPath
	 * @param velocityEngine
	 */
	public static void parse(String templateName, Object obj, String outPath, VelocityEngine velocityEngine) {
		Template t = velocityEngine.getTemplate(templateName);
		t.setEncoding("utf-8");
		VelocityContext ctx = new VelocityContext();
		JSONObject.parseObject(JSONObject.toJSONString(obj)).forEach(ctx::put);
		try (Writer writer = Files.newWriter(new File(outPath), Charset.forName("utf-8"))) {
			t.merge(ctx, writer);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
