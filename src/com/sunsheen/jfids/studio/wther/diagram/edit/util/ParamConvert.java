package com.sunsheen.jfids.studio.wther.diagram.edit.util;

/**
 * 这是一个参数格式类型转换的一个类
 * @author zhoufeng
 */
public class ParamConvert {


	/**
	 * 将四个字段的参数格式转换为六个字段的参数格式
	 * 用于从工程树中拖入节点到业务流编辑器参数格式的转换
	 * String:ret1:false:<NOSET>|String:ret2:false:<NOSET>|String:ret3:false:<NOSET>|类型：变量名：是否数组：描述
	 * 转换为：String:ret1:<NOSET>:变量:<NOSET>:<NOSET>|String:ret2:<NOSET>:变量:<NOSET>:<NOSET>|String:ret3:<NOSET>:变量:<NOSET>:<NOSET>
	 * 类型：变量名：值：参数类型：描述：TIP（隐藏）
	 * @param inStr
	 * @return
	 */
	public static String convert4to6(String inStr){
		String retType = inStr;
		StringBuffer sb = new StringBuffer();
		if(retType!=null && retType.length()>0){
			String rets[] = retType.split("\\|");
			int retItemLength = rets[0].split(":").length;
			if(retItemLength==4){
				for(String eachRet : rets){
					String items[] = eachRet.split(":");
					sb.append(items[0]).append(":");
					sb.append(items[1]).append(":");
					sb.append("<NOSET>").append(":");
					sb.append("变量").append(":");
					sb.append(items[3]).append(":");
					sb.append("<NOSET>").append("|");
				}
			}else{
				sb.append(retType);
			}
		}
		return sb.toString();
	}

}
