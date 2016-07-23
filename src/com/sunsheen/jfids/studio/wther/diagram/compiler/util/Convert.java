package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

public class Convert {

	/**
	 * 将原始字串进行转义 :==>[;] |==>[!]
	 * 
	 * @param str
	 *            需要处理的字串
	 * @param flag
	 *            标明处理方式，true对原始字串进行转义，false对转义字串进行还原
	 * @return
	 */
	public static String convertSpliter(String str, boolean flag) {
		String retVal = "";
		//str = (str==null||"null".equalsIgnoreCase(str)? "" : str);	//保留null字串
		str = (str==null? "" : str);
		if (flag) {
			// 转义
			retVal = str.replaceAll(":", "[;]").replaceAll("\\|", "[!]");
		} else {
			// 还原
			retVal = str.replaceAll("\\[;\\]", ":").replaceAll("\\[!\\]", "\\|");
		}
		return retVal;
	}
}
