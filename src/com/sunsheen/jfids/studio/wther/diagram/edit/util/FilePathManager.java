package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;

/**
 * 这是处理XML配置文件存放位置的一个类，主要用于管理构件库的配置文件
 * 
 * @author zhouf
 */
public class FilePathManager {

	// 定义配置文件在Eclipse安装目录下的存放目录

	public static String basePath = "";
	static {
		basePath = Platform.getInstallLocation().getURL().getPath();
	}
	public static final String CONFIG_DIR = "configuration/com.sunsheen.jfids/component";

	public static String JS_BASE_FILE = "base.xml";

	public static String JS_COMP_FILE = "comp.xml";

	public static String JAVA_COMP_FILE = "java.xml";

	// 自定义页面逻辑流组件库配置文件名
	public static String CUST_PIX_FILENAME = "custom-comp.xml";

	// 自定义业务逻辑流组件库配置文件名
	public static String CUST_BIX_FILENAME = "custom-java.xml";

	// 模板相关路径
	public static final String TEMPLATE_DIR = "configuration/com.sunsheen.jfids/template";

	public static String basePath() {
		// 返回Eclipes安装路径
		// return Platform.getInstallLocation().getURL().getPath();
		return basePath;
	}

	public static String getJsComponentPath() {
		return basePath() + CONFIG_DIR + "/js/";
	}

	public static List<File> getJsComponentFiles() {
		List<File> configFiles = new ArrayList<File>();
		String[] compDirStrs = new String[] { getJsComponentPath() };
		for (String cds : compDirStrs) {
			File baseJsDir = new File(cds);
			if (baseJsDir.isDirectory()) {
				for (File cf : baseJsDir.listFiles()) {
					if (cf.getName().endsWith(".xml")) {
						configFiles.add(cf);
					}
				}
			}
		}
		return configFiles;
	}

	public static String getJavaComponentPath() {
		return basePath() + CONFIG_DIR + "/java/";
	}

	// 获取基本的JS组件的配置文件
	public static String getPixBaseFile() {
		return getJsComponentPath() + JS_BASE_FILE;
	}

	public static String getCustomPixCompomentFile() {
		return getJsComponentPath() + CUST_PIX_FILENAME;
	}

	public static String getCustomBixCompomentFile() {
		return getJavaComponentPath() + CUST_BIX_FILENAME;
	}

	public static String getPixCompomentFile() {
		return getJsComponentPath() + JS_COMP_FILE;
	}

	public static String getBixCompomentFile() {
		return getJavaComponentPath() + JAVA_COMP_FILE;
	}

	// 获取构件库中引用的库文件目录
	public static String getPixLibDir() {
		return getJsComponentPath() + "pixlib/";
	}

	public static String getBixLibDir() {
		return getJavaComponentPath() + "bixlib/";
	}

	/**
	 * 获取页面逻辑流的模板文件路径，所有页面逻辑流的模板都放在此目录下，其下还有具体路径
	 * 
	 * @return
	 */
	public static String getPlflowTemplatePath() {
		return TEMPLATE_DIR + "/plflow/";
	}
}
