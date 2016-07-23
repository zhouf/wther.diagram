package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;


/**
 * 环境变量操作工具
 * 
 * @author litao
 * 
 */
public class ClasspathUtils {

	/** 默认环境变量路径前缀 */
	public static final String JARPATH_VARIABLE_PREFIX = "ECLIPSE_HOME" + File.separator;

	public static List<String> listJarLibsWithoutEncrypt(IProject project) {
		
		// 排除jar包环境变量 与 工程src目录，hearken-core.jar是加密处理过的，不加载
		final String eclipsePath = Platform.getInstallLocation().getURL().getPath().substring(1);
		return ClasspathUtils.listClasspathEntries(JavaCore.create(project), new ClasspathEntryFilter<String>() {
			@Override
			public String accept(IClasspathEntry classpathEntry) {
				String jarPath = classpathEntry.getPath().toString();
				if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_VARIABLE && jarPath.contains("lib") && !jarPath.endsWith("hearken-core.jar")) {
					return jarPath.replace("ECLIPSE_HOME/", eclipsePath);
				}
				return null;
			}
		});
	}

	/**
	 * 获取环境变量实体集合
	 * 
	 * @param project
	 * @param filter 过滤器
	 * @return
	 */
	public static IClasspathEntry[] listClasspathEntries(IJavaProject project) {
		IClasspathEntry[] currEntries = null;
		try {
			currEntries = project.getRawClasspath();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return currEntries;
	}

	/**
	 * 过滤环境变量实体数组
	 * 
	 * @param project
	 * @param filter 过滤器
	 * @return
	 */
	public static IClasspathEntry[] listClasspathEntriesToArray(IJavaProject project, ClasspathEntryFilter<IClasspathEntry> filter) {
		return listClasspathEntries(project, filter).toArray(new IClasspathEntry[0]);
	}

	/**
	 * 获取项目的所有的sourcfolder的路径
	 * 
	 * @param project
	 * @return
	 */
	public static List<String> getAllSrcPath(IProject project) {
		IJavaProject javaProject = JavaCore.create(project);
		return listClasspathEntries(javaProject, new ClasspathEntryFilter<String>() {
			@Override
			public String accept(IClasspathEntry classpathEntry) {
				if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					return classpathEntry.getPath().toOSString();
				}
				return null;
			}
		});
	}

	/**
	 * 过滤环境变量实体集合
	 * 
	 * @param project
	 * @param filter 过滤器
	 * @return
	 */
	public static <T> List<T> listClasspathEntries(IJavaProject project, ClasspathEntryFilter<T> filter) {
		IClasspathEntry[] currEntries;
		List<T> finalEntry = new ArrayList<T>();
		try {
			currEntries = project.getRawClasspath();
			for (IClasspathEntry ce : currEntries) {
				T newCE = filter.accept(ce);
				if (newCE != null) {
					finalEntry.add(newCE);
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return finalEntry;
	}




	/**
	 * 环境变量过滤器
	 * 
	 * @author litao
	 * 
	 */
	public interface ClasspathEntryFilter<T> {
		/**
		 * 环境变量处理
		 * 
		 * @param classpathEntry
		 * @return 处理后的环境变量,{@code null}表示忽略
		 */
		public T accept(IClasspathEntry classpathEntry);
	}
	
	/**
	 * 获取项目的所有的sourcfolder的路径
	 * 
	 * @param project
	 * @return
	 */
	public static List<String> listSrcPath(IProject project) {
		IJavaProject javaProject = JavaCore.create(project);
		return listClasspathEntries(javaProject, new ClasspathEntryFilter<String>() {
			@Override
			public String accept(IClasspathEntry classpathEntry) {
				if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					return classpathEntry.getPath().toOSString();
				}
				return null;
			}
		});
	}
}
