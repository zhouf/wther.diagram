package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import com.sunsheen.jfids.studio.logging.Log;

/**
 * 这是一个专门处理路径信息转换的一个类
 * @author zhoufeng
 * Date:2013-6-9
 */
public class PathConvert {
	

	/**
	 * 将工程路径转换成显示路径
	 * @param pathInProject 如：f1/f2/m1/page/a.pix/aa.pix
	 * @return 返回显示的路径，如：f1.f2.m1.a.aa
	 */
	public static String convertToDisplayPath(String pathInProject) {
		StringBuilder sb = new StringBuilder();
		//如果包含resources，则取后面的字串
		if(pathInProject.contains("/resources/")){
			pathInProject = pathInProject.split("/resources/")[1];
		}
		//去掉第一个/
		if(pathInProject.startsWith("/")){
			pathInProject = pathInProject.substring(1);
		}
		
		String items[] = pathInProject.split("/");
		for(String item : items){
			if("page".equalsIgnoreCase(item)){
				continue;
			}
			//如果文件夹名字包含点，则去掉点及其后字串
			int dotIndex = item.indexOf(".");
			if(dotIndex>=0){
				sb.append(item.substring(0,dotIndex));
			}else{
				sb.append(item);
			}
			sb.append(".");
		}
		if(sb.toString().length()>0){
			sb.deleteCharAt(sb.lastIndexOf("."));
		}
		return sb.toString();
	}
	
	/**
	 * 将文件相对路径转换为调用的方法字串
	 * @param relativePath 如：/abc/book/src/pkg/gg.bix/StringConcat.bix
	 * @return pkg.gg.StringConcat
	 */
	public static String convertMethodStr(String relativePath) {
		StringBuilder sb = new StringBuilder();
		int srcIndex = relativePath.indexOf("src");
		if (srcIndex > 0) {
			for (String item : relativePath.substring(srcIndex + 4).split("/")) {
				int dot = item.indexOf(".");
				if (dot > 0) {
					item = item.substring(0, dot);
				}
				sb.append(item).append(".");
			}
			if (sb.toString().endsWith(".")) {
				sb.deleteCharAt(sb.lastIndexOf("."));
			}
		}
		return sb.toString();
	}
	
	

	/**
	 * 将显示的字串转化为路径字串
	 * @param disp 如：abc.book.src.aa.insert
	 * @return 转化结果，如：abc/book/src/aa.bix/insert.bix
	 */
	public static String convertDisplayToPath(String disp){
		StringBuffer sb = new StringBuffer();
		String items[] = disp.split("\\.");
		for (int i = 0; i < items.length; i++) {
			/*if(i==items.length-2){
				//插入page/
				sb.append("page/");
			}*/
			if(i>=items.length-2){
				//最后两项，添加pix后缀
				sb.append(items[i]).append(".bix/");
			}else{
				sb.append(items[i]).append("/");
			}
		}
		if(sb.toString().length()>0){
			sb.deleteCharAt(sb.lastIndexOf("/"));
		}
		return sb.toString();
	}
	
	/**
	 * 将PIX文件路径转换为包含包路径的完整类名
	 * @param path 文件路径，如：D:/eclipse-SDK-3.7.1_GMF/runtime-EclipseApplication/kkk/resources/fun/usr/src/ppkg/aaaa.bix/bbbb.bix
	 * @return String 返回：com.sunsheen.jfids.logic.database.OpStu
	 */
	public static String convertFullClassStr(String path){
		String retStr = "";
		retStr = convertMethodStr(path);
		//先转换为方法字串，再去掉最后一段
		Log.debug("PathConvert.convertFullClassStr() path:" + path);
		Log.debug("PathConvert.convertFullClassStr() retStr:" + retStr);
		retStr = retStr.substring(0,retStr.lastIndexOf("."));
		Log.debug("PathConvert.convertFullClassStr() retStr:" + retStr);
		return retStr;
	}

}
