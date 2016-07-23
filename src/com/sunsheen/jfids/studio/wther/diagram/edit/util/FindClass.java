package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;

/**
 * 查找系统中所加载的class
 * @author zhouf
 */
public class FindClass {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(FindClass.class.getName());
	
	@Deprecated 
	public static Class<?> fromString(String type) throws ClassNotFoundException{
		// 为兼容之前的代码，不建议使用
		return fromString(type,null);
	}
	
	/**
	 * 这是一个将表示类的字串转化为类对象的一个方法
	 * @param type 表示类的字串如：java.util.Date
	 * @throws ClassNotFoundException 如果找不到，则抛出此异常
	 * @return Class<?> 如果找到，则返回类
	 */
	public static Class<?> fromString(String type,IProject project) throws ClassNotFoundException{

		String projectPath = "";
		if(project != null){
			projectPath = project.getLocation().toString();
		}else{
			ProjectInfoUtil projectInfo = new ProjectInfoUtil();
			projectPath = projectInfo.getProjectPath();
			projectPath = projectPath.substring(0,projectPath.lastIndexOf("/"));
		}
		
		String classPathComponent = projectPath.concat("/WebRoot/WEB-INF/component/");
		String classPathClasses = projectPath.concat("/WebRoot/WEB-INF/classes/");
		
		//添加下面的jar文件是为了反射事务处理中的IdsSession
		//是否需要循环加载lib和runlib下面的jar,如果有同名的jar文件，以runlib优先
		String webinfPath = Platform.getInstallLocation().getURL().getPath().substring(1).concat("deploycontent/WebRoot/WEB-INF/");
		URL[] runJars = loadRunJars(webinfPath);
//		String jarFile1 = jarPath.concat("runlib/jfids_base.jar");
//		String jarFile3 = jarPath.concat("runlib/jfids_wps.jar");
//		String jarFile4 = jarPath.concat("runlib/jfids_wps_core.jar");
//		String jarFile2 = jarPath.concat("lib/hibernate3-base.jar");
		
		boolean classNotFound = false;
		
		Class<?> className = null;
		try {
			className = Class.forName(type);
		} catch (ClassNotFoundException e) {
			classNotFound = true;
			//log.debug("FindClass无法在库中获取类：" + type + "，将在工程中查找");
		}

		if(classNotFound){
			//如果不是库中的类，按工程中的类处理，在component目录和classes目录中查找
			URL[] urls;
			try {
				urls = new URL[] { new URL("file:/" + classPathComponent), new URL("file:/" + classPathClasses)};
				URLClassLoader ucl = new URLClassLoader(urls);  
				className = ucl.loadClass(type);
				classNotFound = false;
			} catch (MalformedURLException e) {
				log.error(e.toString());
			} catch (ClassNotFoundException e) {
				classNotFound = true;
				//log.debug("在component目录和classes目录中无法解析类型："+type);
			}
		}
		
		if(classNotFound){
			//在平台的jar文件中查找
			try {
				URLClassLoader ucl = new URLClassLoader(runJars);  
				className = ucl.loadClass(type);
				classNotFound = false;
			} catch (ClassNotFoundException e) {
				//log.error("在classes目录中无法解析类型【"+type+"】：" + e.toString());
			}  
		}
		
		if(classNotFound){
			throw new ClassNotFoundException();
		}else{
			return className;
		}
		
	}

	//加载lib和runlib下面的jar,如果有同名的jar文件，以runlib优先
	private static URL[] loadRunJars(String webinfPath) {
		HashMap<String,URL> tmpMap = new HashMap<String,URL>();
		File runLibDir = new File(webinfPath + "runlib/");
		File libDir = new File(webinfPath + "lib/");
		String fileName = "";
		try {
			for(File jarFile : runLibDir.listFiles()){
				fileName = jarFile.getName();
				if(fileName.endsWith(".jar")){
					tmpMap.put(fileName, jarFile.toURI().toURL());
				}
			}
			
			for(File jarFile : libDir.listFiles()){
				fileName = jarFile.getName();
				// 去掉了加密包
				if(fileName.endsWith(".jar") && !"hearken-core.jar".equals(fileName)){
					if(!tmpMap.containsKey(fileName)){
						tmpMap.put(fileName, jarFile.toURI().toURL());
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return tmpMap.values().toArray(new URL[]{});
	}

}
