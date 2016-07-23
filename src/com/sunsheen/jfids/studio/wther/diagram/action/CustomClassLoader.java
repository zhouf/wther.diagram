package com.sunsheen.jfids.studio.wther.diagram.action;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 自定义类加载器
 * 
 * @author litao
 * 
 */
public class CustomClassLoader extends URLClassLoader {

	public CustomClassLoader(URL[] urls) {
		super(urls);
	}

	// private List<String> rootDirs = new ArrayList<String>();

	// @Override
	// protected Class<?> loadClass(String name, boolean resolve) throws
	// ClassNotFoundException {
	// try {
	// Class<?> clzss = super.loadClass(name, resolve);
	// if (clzss != null)
	// return clzss;
	// } catch (ClassNotFoundException e) {
	// }
	// byte[] classData = getClassData(name);
	// if (classData == null) {
	// throw new ClassNotFoundException();
	// } else {
	// return defineClass(name, classData, 0, classData.length);
	// }
	// }

	// private byte[] getClassData(String className) {
	// for (String dir : rootDirs) {
	// File clsFile = classNameToPath(dir, className);
	// if (clsFile.exists()) {
	// try {
	// InputStream ins = new FileInputStream(clsFile);
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// int bufferSize = 4096;
	// byte[] buffer = new byte[bufferSize];
	// int bytesNumRead = 0;
	// while ((bytesNumRead = ins.read(buffer)) != -1) {
	// baos.write(buffer, 0, bytesNumRead);
	// }
	// return baos.toByteArray();
	// } catch (IOException e) {
	// RunLog.error(e.getMessage(), e);
	// }
	// }
	// }
	// return null;
	// }
	//
	// private File classNameToPath(String rootDir, String className) {
	// return new File(rootDir + File.separatorChar + className.replace('.',
	// File.separatorChar) + ".class");
	// }
	//
	// public void addClasspath(String classpath) {
	// rootDirs.add(classpath);
	// }
}