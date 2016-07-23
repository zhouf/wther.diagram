package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 这是一个扫描工程中文的方法，用于构建对话框中的显示数据项
 * @author zhouf
 */
public class FileScanUtil {

	/**
	 * 从目录下获得文件资源集合
	 * @param path 初始化目录
	 * @param suffix 后缀
	 * @param isdepth 是否扫描子目录
	 * @return ArrayList<String> 返回资源路径集合
	 */
	public static ArrayList<String> getListFiles(String path, String suffix, boolean isdepth) {
		ArrayList<String> lstFileNames = new ArrayList<String>();
		File file = new File(path);
		return FileScanUtil.listFile(lstFileNames, file, suffix, isdepth);
	}

	/**
	 * 列出目录结构下的资源
	 * @param lstFileNames 列表资源
	 * @param f
	 * @param suffix 后缀
	 * @param isdepth 是否要深入到子目录
	 * @return ArrayList<String> 返回资源集合
	 */
	public static ArrayList<String> listFile(ArrayList<String> lstFileNames, File f, String suffix, boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
		if (f.isDirectory()) {
			File[] t = f.listFiles();

			for (int i = 0; i < t.length; i++) {
				if (isdepth || t[i].isFile()) {
					listFile(lstFileNames, t[i], suffix, isdepth);
				}
			}
		} else {
			String filePath = f.getAbsolutePath();
			if (!suffix.equals("")) {
				int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";

				if (begIndex != -1) {
					tempsuffix = filePath.substring(begIndex + 1, filePath.length());
					if (tempsuffix.equals(suffix)) {
						lstFileNames.add(filePath);
					}
				}
			} else {
				lstFileNames.add(filePath);
			}
		}
		return lstFileNames;
	}
	
	/**
	 * 将java文件路径转换为包含包名的类路径，按src截取
	 * @param filePathList
	 * @return
	 */
	public static List<String> convertFilePathToPackagePath(List<String> filePathList){
		List<String> retList = new ArrayList<String>();
		for(String path : filePathList){
			path = StringUtils.substringAfterLast(path, "\\src\\");
			path = StringUtils.removeEnd(path, ".java");
			path = StringUtils.replaceChars(path, '\\', '.');
			retList.add(path);
		}
		return retList;
	}
}
