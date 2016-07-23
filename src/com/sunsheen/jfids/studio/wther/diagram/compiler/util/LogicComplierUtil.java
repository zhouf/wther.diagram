package com.sunsheen.jfids.studio.wther.diagram.compiler.util;


/**
 * 路径转换和编译为class工具类
 * @author xiaohui
 *
 */
public class LogicComplierUtil {
	/**
	 * 生成相应的包名
	 * 
	 * @param path
	 * @param f
	 * @return
	 */
	public static String getPackageName(String path, boolean f) {
		int srcIndex = path.lastIndexOf("src");
		int comIndex = path.length();
		srcIndex = srcIndex < 0 ? 0 : srcIndex + 4;
		if (srcIndex < comIndex) {
			String pkgStr = path.substring(srcIndex, comIndex);

			if (f) {
				pkgStr = pkgStr.replaceAll("\\\\", ".").replaceAll("/", ".");// 去掉两种不同方式表示的路径
			}
			if (pkgStr.endsWith("."))
				pkgStr = pkgStr.substring(0, pkgStr.length() - 1);

			return pkgStr;
		} else {
			return "";
		}
	}
	/**
	 * 将文件夹名转换为类名
	 * 
	 * @param classStr
	 *            文件夹名如：book.bix
	 * @param upperCase
	 *            是否大写首字母
	 * @return 生成类名如：Book
	 */
	public static String covertFolderToClassAndMethodName(String classStr, boolean upperCase) {
		String retStr = "";
		String first = classStr.split("\\.")[0];
		if (upperCase) {
			retStr = first.toUpperCase().charAt(0) + first.substring(1);
		} else {
			retStr = first;
		}
		return retStr;
	}
	
}
