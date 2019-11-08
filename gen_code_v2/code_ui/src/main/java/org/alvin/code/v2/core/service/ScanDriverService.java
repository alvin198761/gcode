package org.alvin.code.v2.core.service;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Component
public class ScanDriverService {

	/**
	 * 扫描业务类
	 * 	 *
	 * 	 * @param packageURL
	 * 	 * @throws Exception
	 * @param packageURL
	 * @param suffix
	 * @return
	 * @throws Exception
	 */
	public List<String> doScanService(String packageURL , String suffix) throws Exception {
		List<String> list = Lists.newArrayList();
		try {
			String packageName = packageURL;
			packageURL = packageURL.replaceAll("[.]", "/");
			URL baseURL = Thread.currentThread().getContextClassLoader().getResource(packageURL);
			if ("file".equals(baseURL.getProtocol())) {
				list.addAll(doDevScan(baseURL, packageName,suffix));
			} else if ("jar".equals(baseURL.getProtocol())) {
				list.addAll(doRuntimeScan(baseURL, packageName,suffix));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 运行时只会通过这个方法调用
	 *
	 * @param baseURL
	 * @param packageName
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static List<String> doRuntimeScan(URL baseURL, String packageName , String suffix) throws IOException {
		List<String> classList = Lists.newArrayList();
		JarFile jar = ((JarURLConnection) baseURL.openConnection()).getJarFile();
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry je = entries.nextElement();
			String urlName = je.getName().replace('/', '.');
			if (!urlName.startsWith(packageName)) {
				continue;
			}
			if (!urlName.endsWith(suffix)) {
				continue;
			}
			classList.add(urlName);
		}
		return classList;
	}

	/**
	 * 开发时会用到的方法
	 *
	 * @param baseURL
	 * @param packageName
	 * @throws Exception
	 */
	private static List<String> doDevScan(URL baseURL, String packageName, String suffix) throws Exception {
		String filePath = URLDecoder.decode(baseURL.getFile(), "UTF-8");
		List<String> classList = Lists.newArrayList();

		LinkedList<File> files = Lists.newLinkedList();
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			throw new Exception("没有找到对应的包");
		}
		files.add(dir);
		// 循环读取目录以及子目录下的所有类文件
		while (!files.isEmpty()) {
			File f = files.removeFirst();
			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				int i = 0;
				for (File childFile : fs) {
					files.add(i++, childFile);
				}
				continue;
			}
			if (!f.getName().toLowerCase().endsWith(suffix)) {
				continue;
			}
			String subPath = f.getAbsolutePath().substring(dir.getAbsolutePath().length());
			String classUrl = packageName + subPath.replace(File.separatorChar, '.');
			classList.add(classUrl);
		}
		return classList;
	}
}
