package com.sunsheen.jfids.studio.wther.diagram.component.views;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IProject;

import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ClasspathUtils;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FilePathManager;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.ProjectInfoUtil;

/**
 * 
 * @author Administrator
 * 
 */
public class Scanner {
	
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(Scanner.class.getName());
	private static final ClassLoader loader = Thread.currentThread().getContextClassLoader();
	private static String loaderPath = loader.getResource("").getPath();
	private static int loaderPathLength = loaderPath.length() - 1;

	// 解决mac os 下无法按照规则扫描到文件的问题
	private static String OS = System.getProperty("os.name").toLowerCase();
	static {
		try {
			log.debug("Scanner.enclosing_method()->loaderPath:" + loaderPath);
			loaderPath = java.net.URLDecoder.decode(loaderPath, "utf-8");
			log.debug("Scanner.enclosing_method()->loaderPath:" + loaderPath);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (isMacOSX()) {
			loaderPathLength = loaderPath.length();
		} else {
			loaderPathLength = loaderPath.length() - 1;
		}
	}

	public static boolean isMacOSX() {
		return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
	}

	// 解决mac os 下无法按照规则扫描到文件的问题

	public static void main(String[] args) throws Exception {
		// List<String> classNames = getClassName(packageName);

		long start = System.currentTimeMillis();
		loaderPath = "D:\\SERVER\\jboss-4.2.2.GA_jfids\\server\\default\\deploy\\jfids.war\\WEB-INF\\classes";
		// List<URL> classNames = Scanner
		// .findResource("(com|org).*Listener\\.class$");
		System.out.println(loaderPath);
		List<URL> classNames = Scanner.findResource(".*\\.sqlMap$");
		System.out.println("数量：：" + classNames.size());
		if (classNames != null) {
			for (URL className : classNames) {
				String fileName = className.getPath();
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				System.out.println("结果：：" + fileName);
			}
		}
		System.out.println(System.currentTimeMillis() - start);
		System.out.println("-----------------");

	}

	public static String getClassName(URL classPath) {
		String path = classPath.getPath();
		if (path.endsWith(".class")) {
			if (path.indexOf("jar!/") > 0) {// jar包里的
				path = path.substring(path.indexOf("jar!/") + 5);
				return path.substring(0, path.length() - 6).replaceAll("/", ".");
			} else {
				return path.substring(loaderPath.length(), path.length() - 6).replaceAll("/", ".");
			}
		}
		return null;
	}

	/**
	 * 获取某包下（包括该包的所有子包）所有类
	 * 
	 * @param packageName
	 *            包名
	 * @return 类的完整名称
	 */
	public static List<URL> findResource(String patternStr) {
		// 解决陈刚在国家气象局的问题临时加的东西20150204，3.5的考虑不周
		// if ("com.sunsheen.*Listener\\.class$".equals(patternStr)) {
		// patternStr = ".*StartupListener\\.class$";
		// }
		return findResource(patternStr, true);
	}

	/**
	 * 获取某包下所有类
	 * 
	 * @param packageName
	 *            包名
	 * @param patternStr
	 *            查找类的正则
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	private static List<URL> findResource(String patternStr, boolean childPackage) {
		Pattern pattern = Pattern.compile(patternStr);
		List<URL> fileNames = new ArrayList<URL>();

		try {
			String path = java.net.URLDecoder.decode(loaderPath, "utf-8");
			path = "D:/Hearken/platform-4.1-luna-win64/deploycontent/WebRoot/WEB-INF/lib";
			List<URL> fileNames1 = findClassNameByFile(pattern, path);
			if (fileNames1 != null) {
				fileNames.addAll(fileNames1);
			}
			log.debug("Scanner.findResource()->loader:" + loader);
			List<URL> fileNames2 = findClassNameByJars(pattern, ((URLClassLoader) loader).getURLs());
			if (fileNames2 != null) {
				fileNames2.addAll(fileNames);
				return fileNames2;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fileNames;
	}

	/**
	 * 从项目文件获取某包下所有类
	 * 
	 * @param filePath
	 *            文件路径
	 * @param className
	 *            类名集合
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 * @throws MalformedURLException
	 */
	private static List<URL> findClassNameByFile(Pattern pattern, String filePath) throws MalformedURLException {

		log.debug("Scanner.findClassNameByFile()->pattern:" + pattern);
		log.debug("Scanner.findClassNameByFile()->filePath:" + filePath);

		List<URL> myClassName = new ArrayList<URL>();
		/*File file = new File(filePath);
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				myClassName.addAll(findClassNameByFile(pattern, childFile.getPath()));
			} else {
				String childFilePath = childFile.getPath();

				if (pattern.matcher(childFilePath.substring(loaderPathLength)).matches()) {
					myClassName.add(childFile.toURI().toURL());
				}
			}
		}*/

		return myClassName;
	}

	/**
	 * 从jar获取某包下所有类
	 * 
	 * @param jarPath
	 *            jar文件路径
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	private static List<URL> findClassNameByJar(Pattern pattern, String jarPath) {
		log.debug("Scanner.findClassNameByJar()->pattern:" + pattern + " jarPath:" + jarPath);
		List<URL> myClassName = new ArrayList<URL>();
		String[] jarInfo = jarPath.split("!");
		String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
		try {
			if (!new File(jarFilePath).isFile()) {
				return myClassName;
			}
			jarFilePath = java.net.URLDecoder.decode(jarFilePath, "utf-8");
			JarFile jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (pattern.matcher(entryName).matches()) {
					URL url = new URL(new StringBuilder("jar:file:").append(jarFilePath).append("!/").append(entryName).toString());
					myClassName.add(url);
					InputStream in = jarFile.getInputStream(jarEntry);
					String content = IOUtils.toString(in);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myClassName;
	}

	/**
	 * 从所有jar中搜索该包，并获取该包下所有类
	 * 
	 * @param urls
	 *            URL集合
	 * @param packagePath
	 *            包路径
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	private static List<URL> findClassNameByJars(Pattern pattern, URL[] urls) {
		List<URL> myClassName = new ArrayList<URL>();
		// 得到classes目录
		if (urls != null) {
			for (int i = 0; i < urls.length; i++) {
				URL url = urls[i];
				String urlPath = url.getPath();
				if (urlPath.equals(loaderPath)) {
					continue;
				}
				String jarPath = urlPath + "!/";
				myClassName.addAll(findClassNameByJar(pattern, jarPath));
			}
		}
		return myClassName;
	}

	/**
	 * 遍历工程库中的jar文件并获取其中内容
	 * @param patternStr
	 * @return
	 */
	public static List<String> findResourceContent(String patternStr) {
		Pattern pattern = Pattern.compile(patternStr);
		List<String> contents = new ArrayList<String>();
		
		String platformPath = FilePathManager.basePath();
		List<String> jarFiles = getPlatformJarFiles(platformPath);
		for(String jarfile : jarFiles){
			contents.addAll(findContentByJar(pattern,jarfile));
		}
		return contents;
	}

	/**
	 * 获取平台下的可用jar文件集合，包括lib和runlib下未加密的jar包
	 * @param platformPath 平台路径
	 * @return
	 */
	private static List<String> getPlatformJarFiles(String platformPath) {
		List<String> retList = new ArrayList<String>();
		//log.debug("Scanner.getJarFiles()->platformPath:" + platformPath);
		File libFolder = new File(platformPath + "deploycontent/WebRoot/WEB-INF/lib");
		for(File jarFile : libFolder.listFiles()){
			if(!"hearken-core.jar".equals(jarFile.getName()) && jarFile.getName().endsWith(".jar")){
				log.debug("Scanner.getJarFiles()->jarFile.getAbsolutePath():" + jarFile.getAbsolutePath());
				retList.add(jarFile.getAbsolutePath());
			}else{
				System.err.println("hearken-core.jar");
			}
		}
		File runlibFolder = new File(platformPath + "deploycontent/WebRoot/WEB-INF/runlib");
		for(File jarFile : runlibFolder.listFiles()){
			if(jarFile.getName().endsWith(".jar")){
				//log.debug("Scanner.getJarFiles()->jarFile.getAbsolutePath():" + jarFile.getAbsolutePath());
				retList.add(jarFile.getAbsolutePath());
			}
		}
		return retList;
	}

	/**
	 * 根据传入的jar文件，查找包中的文件，并返回文件内容
	 * @param pattern
	 * @param jarPath
	 * @return
	 */
	private static List<String> findContentByJar(Pattern pattern, String jarFilePath) {
		//log.debug("Scanner.findClassNameByJar()->pattern:" + pattern + " jarFilePath:" + jarFilePath);
		List<String> contents = new ArrayList<String>();
		try {
			if (!new File(jarFilePath).isFile()) {
				return contents;
			}
			jarFilePath = java.net.URLDecoder.decode(jarFilePath, "utf-8");
			JarFile jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (pattern.matcher(entryName).matches()) {
					log.debug("Scanner.findContentByJar()->entryName:" + entryName);
					InputStream in = jarFile.getInputStream(jarEntry);
					contents.add(IOUtils.toString(in));
				}
			}
			jarFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contents;
	}
}
