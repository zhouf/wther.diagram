package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

/**
 * 这是一个处理列隐藏的一个常量
 * @author zhoufeng
 * Date:2013-7-18
 */
public class TableHiddenType {
	//保存条件的UUID值
	public static int CONDITION_UUID = 101;
	
	//提示标记，是弹出框还是变量
	public static int TIP_RENDER = 102;
	
	//是否参数是否可编辑
	public static int ARG_EDITABLE = 103;
	
	//回调function中的参数
	public static int FUNC_PARAMS = 104;

	//是否允许被忽略，非必填写项目，不检查值是否输入
	public static int IGNORE = 105;
	
	//暂存隐藏参数
	public static String HIDDEN = "_hidden_params";

}
