package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

public class ParamUtil {

	/**
	 * 将参数字串转换为数组
	 * @param inStr 输入的参数字串如：a:b:c:d
	 * @param num 设置返回数组的长度
	 * @return
	 */
	public static String[] praseStrToArray(String inStr , int num){
		String[] tmpArray = new String[num];
		for (int i = 0; i < num; i++) {
			tmpArray[i] = "";
		}
		String items[] = inStr.split(":");
		int limit = (tmpArray.length>items.length? items.length : tmpArray.length);
		for (int i = 0; i < limit; i++) {
			if("<NOSET>".equalsIgnoreCase(items[i])){
				tmpArray[i] = "";
			}else{
				tmpArray[i] =  Convert.convertSpliter(items[i],false);
			}
		}
		return tmpArray;
	}
}
