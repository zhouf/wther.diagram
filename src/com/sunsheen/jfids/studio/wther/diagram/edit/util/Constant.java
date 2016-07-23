package com.sunsheen.jfids.studio.wther.diagram.edit.util;

public class Constant {
	
	//业务逻辑流中用于保存模块全局变量的文件名
	public static String GLOBALBIXFILE = ".bglb";
	
	//将变长参数对象放入params中的key值
	public static String VARIADIC_ARG = "_variadic_arg";
	
	//放入params中的key,变长参数在参数中的位置
	public static String VARIADIC_INDEX = "_variadic_index";
	
	public static String TYPE_JAVA_BROWSE = "Java浏览...";

	public static String IDATAPORT_STR = "com.sunsheen.jfids.system.bizass.port.IDataPort";
	public static String MAP_PUT_COMPONENT = "com.sunsheen.jfids.system.base.logic.component.base.lang.putdata.MapPutComponent";
	
	//在对话中带回需要创建的变量设置，此值为params中的key
	public static String NEED_CREATE_VAR = "_param_need_create_var";
	public static String NEED_VARSTORE_VAR = "_param_need_varstore_var";
	
	public static String LOGIC_COMPONENT_EXECUTE = "LogicComponent.execute";
	
	
	//配置循环节点时，标记当前循环为简单循环设置
	public static String LOOP_SIMPLE_MARK = "[-sp-]";
	

}
