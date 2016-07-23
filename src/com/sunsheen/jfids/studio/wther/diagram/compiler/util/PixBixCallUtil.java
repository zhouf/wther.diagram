package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import com.sunsheen.jfids.studio.logging.Log;

/**
 * 这是一个处理pix路径转化的一个类
 * 
 * @author zhouf
 */
public class PixBixCallUtil {

	/**
	 * 从文件路径中提取调用的前缀，如/kkk/resources/abc/book/page/aa.pix/ff.pix，提取为abc_book_aa.
	 * ff 截取resources和page之间的字串作为前缀，以区别不同模块下相同的文件名 如果文件名中含有下划线，则用两个下划线转义
	 * 20130902修复了page后含有目录的问题
	 * @param file
	 *            传入的文件路径
	 * @return
	 */
	public static String getPixPrefix(String file) {
		String retVal = "";
		// 生成前缀
		int resourcesIndex = file.indexOf("resources");
		if (resourcesIndex >= 0) {
			retVal = file.substring(resourcesIndex + 10);
			//去掉page，将.pix/转换为.将最后.pix去掉
			retVal = retVal.replaceAll("page/", "").replaceAll("\\.pix/", "\\.").replaceAll("\\.pix", "");
			retVal = retVal.replaceAll("_", "__").replaceAll("/", "_"); // 转义
		} else {
			Log.error("PixBixCallUtil getPixPrefix()-> 确定关键字位置异常 file[" + file + "]");
		}

		return retVal;
	}
	
	/**
	 * 从文件路径中提取调用的前缀，如kkk/bbb/abc.bix/fun.bix，提取为abc.fun
	 * @param file 传入的文件路径
	 * @return
	 */
	public static String getBixPrefix(String file) {
		StringBuffer sb = new StringBuffer();
		String path = file;
		String[] p = path.split("/");

		int plength = p.length;
		if (plength >= 2) {
			// 如果大于等于2，取最后两个元素提取出调用元素
			String folderName = p[plength - 2];
			String fileName = p[plength - 1];
			if (folderName.contains(".")) {
				folderName = folderName.substring(0, folderName.indexOf("."));
			}
			if (fileName.contains(".")) {
				fileName = fileName.substring(0, fileName.indexOf("."));
			}
			sb.append(folderName).append(".").append(fileName);
		} else {
			Log.error("路径含参小于两个单位，请检查[" + path + "]");
		}
		return sb.toString();
	}
}
