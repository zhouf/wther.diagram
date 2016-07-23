package com.sunsheen.jfids.studio.wther.diagram.compiler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ParamUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FindClass;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.InputDataValidate;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;

/**
 * 这是对赋值对话框参数解析的一个类
 * @author zhouf
 */
public class JavaAssignmentArgParse {

	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(JavaAssignmentArgParse.class.getName());
	
	private String vName;
	private String vVal;
	private String vType;
	private boolean vDef;
	private boolean bArray;
	VarStore varStore = null;
	
	/**
	 * 构造方法，对输入的拼接字串进行拆分 
	 * @param arg 变量名:变量初值:变量类型:是否定义
	 */
	public JavaAssignmentArgParse(String arg) {
		this(arg,null);
	}
	
	public JavaAssignmentArgParse(String arg,String varStoreStr) {
		String args[] = ParamUtil.praseStrToArray(arg, 5);
		//如果是JAVA，在参数里的格式为=>变量名：变量值：类型：是否数组：是否定义
		this.vName = convertSpliter(args[0]);
		this.vVal = convertSpliter(args[1]);
		this.vType = args[2];
		this.bArray = "true".equalsIgnoreCase(args[3]);
		this.vDef = "true".equalsIgnoreCase(args[4]);
		//TODO 对数组的代码生成处理
		varStore = new VarStore(varStoreStr);
	}
	
	/**
	 * 生成相应的代码
	 * @return
	 */
	public String getCode() {
		return genJavaCode(vDef,vType,vName,vVal,bArray);
	}

	
	
	/**
	 * 返回生成JAVA代码的语句
	 * @return
	 */
	private String genJavaCode(boolean varDef,String varType,String varName,String varVal,boolean isArray) {
		String retVal;
		if(JavaType.STRING.equals(varType) || "java.lang.String".equals(varType)){
			retVal = genStrCode(varDef,varName,varVal,isArray);
		}else if(JavaTypeUtil.isPrimitiveType(varType)){
			retVal = genPrimitiveInitCode(varDef,varType,varName,varVal,isArray);
		}else if(JavaType.DATE.equals(varType)){
			retVal = genDateCode(varDef,varName,varVal);
		}else if(JavaType.HASHMAP.equals(varType) || "java.util.HashMap".equals(varType)){
			retVal = genHashMapCode(varDef,varName);
		}else if(JavaType.ARRAYLIST.equals(varType) || "java.util.ArrayList".equals(varType)){
			retVal = genArrayListCode(varDef,varName);
		}else if("变量".equalsIgnoreCase(varType)){
			//变量
			retVal = genExpressionCode(varDef,varName,varVal);
		}else if("常量".equalsIgnoreCase(varType)){
			//常量
			retVal = genConstantCode(varType,varName,varVal);
		}else if("表达式".equalsIgnoreCase(varType)){
			//表达式
			retVal = genExpressionCode(varDef,varName,varVal);
		}else{
			//java类数据类型，varType如java.util.Date
			boolean isClassType = true;
			try {
//				Class<?> objClass = Class.forName(varType, false, this.getClass().getClassLoader());
//				Class<?> objClass = Class.forName(varType, false, Thread.currentThread().getContextClassLoader());
				Class<?> objClass = FindClass.fromString(varType);
			} catch (ClassNotFoundException e) {
				Log.error("JavaAssignmentArgParse.genJavaCode-> :" + e.toString());
				isClassType = false;
			}
			if(isClassType){
				retVal = genClassTypeCode(varDef,varType,varName,varVal);
			}else{
				Log.error("JavaAssignmentArgParse.genJavaCode-> :找不到与数据类型【"+varType+"】相关的匹配项");
				retVal = "//ERROR 找不到与数据类型【"+varType+"】相关的匹配项";
			}
		}
		return retVal;
	}
	
	/**
	 * 生成基本数据类型的初始化值
	 * @param varType2
	 * @return
	 */
	private String genPrimitiveInitCode(boolean varDef,String varType,String varName,String varVal,boolean isArray) {
		String retVal = "";
		if(JavaType.INT.equals(varType)){
			retVal = genNumCode(varDef,varType,varName,varVal,isArray);
		}else if(JavaType.BOOLEAN.equals(varType)){
			retVal = genBooleanCode(varDef,varName,varVal,isArray);
		}else if(JavaType.CHAR.equals(varType)){
			retVal = genCharCode(varDef,varName,varVal,isArray);
		}else if(JavaType.DOUBLE.equals(varType)){
			retVal = genNumberCodeTemplate('D',varDef,varType,varName,varVal,isArray);
		}else if(JavaType.FLOAT.equals(varType)){
			retVal = genNumberCodeTemplate('F',varDef,varType,varName,varVal,isArray);
		}else if(JavaType.LONG.equals(varType)){
			retVal = genNumberCodeTemplate('L',varDef,varType,varName,varVal,isArray);
		}else if(JavaType.SHORT.equals(varType)){
			retVal = genNumCode(varDef,varType,varName,varVal,isArray);
		}
		return retVal;
	}

	//生成JAVA中的ArrayList赋值语句
	private String genArrayListCode(boolean varDef,String varName) {
		StringBuffer sb = new StringBuffer();
		if(varDef == true){
			sb.append("java.util.ArrayList ");
		}
		sb.append(varName).append(" = ").append("new java.util.ArrayList()").append(";");
	    //ASTUtil.importPkg(unit, "java.util.ArrayList");
		
		return sb.toString();
	}

	
	private String genBooleanCode(boolean varDef,String varName,String varVal,boolean isArray) {
		//boolean[] abc = {true,false,true,true,false};
		StringBuffer sb = new StringBuffer();
		if(varDef == true){
			sb.append(JavaType.BOOLEAN).append(" ");
//			sb.append("Boolean ");
		}

		sb.append(varName);
		
		if(isArray){
			
			if(StringUtils.isNotEmpty(varVal)){
				StringBuilder tmpStr = new StringBuilder();
				for(String eachVar : varVal.split(",")){
					tmpStr.append(booleanVal(eachVar)).append(",");
				}
				if(tmpStr.toString().endsWith(",")){
					tmpStr.deleteCharAt(tmpStr.lastIndexOf(","));
				}
				
				sb.append("[] = {").append(tmpStr).append("};");
			}else{
				sb.append("[];");
			}
		}else{
			//sb.append(varName).append(" = ").append(booleanVal(varVal)).append(";");
			
			if(StringUtils.isNotEmpty(varVal)){
				sb.append(" =").append(booleanVal(varVal)).append(";");
			}else{
				sb.append(";");
			}
		}
		
		return sb.toString();
	}

	/**
	 * 对输入的字串进行判断，转换为boolean类型数据
	 * @param varVal2 输入的字串如"true","1","0"
	 * @return boolean 
	 */
	private boolean booleanVal(String varVal2) {
		boolean retVal = true;
		if(StringUtils.isNumeric(varVal2)){
			//如果是数值类型表示，将0转换为false
			retVal = (0==NumberUtils.toInt(varVal2) ? false : true);
		}else{
			retVal = "true".equalsIgnoreCase(varVal2);
		}
		return retVal;
	}

	private String genCharCode(boolean varDef,String varName,String varVal,boolean isArray) {
		StringBuffer sb = new StringBuffer();
		if(varDef == true){
			sb.append("char ");
		}
		sb.append(varName);
		if(isArray){
			if(StringUtils.isNotEmpty(varVal)){
				//用这种方式定义数组 char[] abc= "string".toCharArray();
				//去掉引号和转义
				//varVal
				sb.append("[] = \"").append(convertRemark(clearQuot(varVal))).append("\".toCharArray();");
			}else{
				sb.append("[] = {};");
			}
		}else{
			if(StringUtils.isNotEmpty(varVal)){
				sb.append(" = '").append(varVal).append("';");
			}else{
				sb.append(";");
			}
		}
		return sb.toString();
	}

	/**
	 * 检查首尾双引号，有则去掉，没有则忽略，对中间的引号进行转义
	 * @param varVal2 传入需要处理的字串
	 * @return String 处理后的字串
 	 */
	private String clearQuot(String varVal2) {
		StringBuilder sb = new StringBuilder(varVal2);
		if(StringUtils.isNotEmpty(varVal2) && varVal2.length()>1){
			
			if(sb.charAt(sb.length()-1)=='\"'){
				sb.deleteCharAt(sb.length()-1);
			}
			if(sb.charAt(0)=='\"'){
				sb.deleteCharAt(0);
			}
		}
		return sb.toString();
	}

	

	/**
	 * 返回生成Float赋值语句的字串
	 * @return
	 */
	private String genNumberCodeTemplate(char suffix,boolean varDef,String varType,String varName,String varVal,boolean isArray) {
		StringBuffer sb = new StringBuffer();
		if(varDef==true){
			sb.append(varType).append(" ");
		}
		
		sb.append(varName);
		if(isArray){
			if(StringUtils.isNotEmpty(varVal)){
				
				//结float类型的数据添加f标记
				String valStr = appendSuffix(varVal, suffix);
				
				sb.append("[] = {").append(valStr).append("};");
			}else{
				sb.append("[] = {};");
			}
		}else{
			if(StringUtils.isNotEmpty(varVal)){
				if(varVal.toLowerCase().endsWith(String.valueOf(suffix))){
					sb.append("=").append(varVal).append(";");
				}else{
					sb.append("=").append(varVal).append(suffix).append(";");
				}
			}else{
				sb.append("=0").append(suffix).append(";");
			}
		}
		
		return sb.toString();
	}

	/**
	 * 给数组字串添加后缀标记
	 * @param str 1,2,3
	 * @param suffix f
	 * @return String 1f,2f,3f
	 */
	private String appendSuffix(String str,char suffix) {
		StringBuilder sb = new StringBuilder();
		for(String eachVar : str.split(",")){
			if(eachVar.toLowerCase().endsWith(String.valueOf(suffix))){
				sb.append(eachVar).append(",");
			}else{
				sb.append(eachVar).append(suffix).append(",");
			}
		}
		if(sb.toString().endsWith(",")){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.toString();
	}
	

	/**
	 * 生成HashMap定义代码
	 * @return
	 */
	private String genHashMapCode(boolean varDef,String varName) {
		StringBuffer sb = new StringBuffer();
		if(varDef==true){
			sb.append("java.util.HashMap ");
		}
		sb.append(varName).append(" = new java.util.HashMap()").append(";");
		//ASTUtil.importPkg(unit, "java.util.HashMap");
		return sb.toString();
	}
	/**
	 * 生成HashTable定义代码
	 * @return
	 */
	private String genHashTableCode(boolean varDef,String varName) {
		StringBuffer sb = new StringBuffer();
		if(varDef==true){
			sb.append("java.util.Hashtable ");
		}
		sb.append(varName).append(" = new java.util.Hashtable()").append(";");
		//ASTUtil.importPkg(unit, "java.util.Hashtable");
		return sb.toString();
	}

	/**
	 * 返回字符串类型的代码
	 * @return
	 */
	private String genStrCode(boolean varDef,String varName,String varVal,boolean isArray) {
		StringBuffer sb = new StringBuffer();
		//TODO 对值的处理，如果有引号，去掉首尾的引号
		if(varDef==true){
			sb.append("java.lang.String ");
		}
		varVal = clearQuot(varVal);
		//对字串中间的双引号进行转义
		if(isArray){
			sb.append(varName).append("[] = new java.lang.String[]{\"").append(varVal).append("\"};");
		}else{
			if(InputDataValidate.checkConstantString(varVal)){
				//如果是字符串常量，则包含双引号，则需要处理中间的双引号
				sb.append(varName).append(" = ").append(convertRemark(varVal)).append(";");
			}else{
				sb.append(varName).append(" = \"").append(convertRemark(varVal)).append("\";");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 处理int类型数据的生成
	 * @return String
	 */
	private String genNumCode(boolean varDef,String varType,String varName,String varVal,boolean isArray) {
		StringBuffer sb = new StringBuffer();
		if(varDef==true){
			sb.append(varType+" ");
		}
		if(varVal!=null && varVal.length()>0){
			//如果有值
			if(isArray){
				sb.append(varName).append("[] = {").append(varVal).append("};");
			}else{
				sb.append(varName).append(" = ").append(varVal).append(";");
			}
		}else{
			//如果没有值
			if(isArray){
				sb.append(varName).append("[] = {0};");
			}else{
				sb.append(varName).append(" = 0;");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 对日期格式的检查，如以下几种
	 * 2012-12
	 * 2012-12-30
	 * 2012-12-30 12:32
	 * 2012-12-30 12:32:25
	 * @return
	 */
	private String genDateCode(boolean varDef,String varName,String varVal) {
		//ASTUtil.importPkg(unit, "java.util.Date");
		StringBuffer sb = new StringBuffer();
		String argStr = "";
		if(varVal!=null && varVal.trim().length()==0){
			//如果没有初始数据，则构造空参数
			
		}else{
			//如果有初始数据，则根据初始数据构造
			//分解年-月-日
			int year = 0, month = 0, day = 1, hour = 0, minute = 0, second = 0;
			String dateItem[] = StringUtils.split(varVal, "-: ");
			
			year = Short.parseShort(dateItem[0])-1900;
			month = Short.parseShort(dateItem[1])-1;
			if(dateItem.length>=3){
				day = Short.parseShort(dateItem[2]);
			}
			if(dateItem.length>=4){
				hour = Short.parseShort(dateItem[3]);
			}
			if(dateItem.length>=5){
				minute = Short.parseShort(dateItem[4]);
			}
			if(dateItem.length>=6){
				second = Short.parseShort(dateItem[5]);
			}
			
			switch(dateItem.length){
			case 2:
			case 3:
				argStr = year + "," + month + "," + day;
				break;
			case 5:
				argStr = year + "," + month + "," + day + "," + hour + "," + minute;
				break;
			case 6:
				argStr = year + "," + month + "," + day + "," + hour + "," + minute + "," + second;
				break;
			default:
				log.warn("JavaAssignmentArgParse.genDateCode()->:将初始化字串分解为日期异常:" + varVal);
			}
			
		}
		if(varDef==true){
			sb.append("java.util.Date ");
		}
		//Date myDate=new Date(2000,0,1)
		//TODO 日期类型的数组处理
		sb.append(varName).append(" = new java.util.Date(").append(argStr).append(");");
		
		return sb.toString();
	}
	
	/**
	 * 表达式的生成
	 * @return
	 */
	private String genExpressionCode(boolean varDef,String varName,String varVal) {
		StringBuffer sb = new StringBuffer();
		if(varDef==true){
			sb.append("var ");
		}
		if(varName.contains("/")){
			sb.append(transToSetMethod(varName)).append("(").append(varVal).append(");");
		}else{
			sb.append(varName).append(" = ").append(varVal).append(";");
		}
		return sb.toString();
	}
	
	/**
	 * 将对属性的赋值字串进行转换
	 * @param varName2 传入的赋值串如：obj/name
	 * @return String 返回的方法调用字串如：obj.setName
	 */
	private String transToSetMethod(String varName2) {
		String retStr = "";
		int index = varName2.indexOf("/");
		String left = varName2.substring(0,index);
		String mid = varName2.substring(index+1,index+2);
		String right = varName2.substring(index+2);
		retStr = left + ".set" + mid.toUpperCase() + right;
		return retStr;
	}

	/**
	 * 常量的生成
	 * @return
	 */
	private String genConstantCode(String varType,String varName,String varVal) {
		StringBuffer sb = new StringBuffer();
		if(varName.contains("/")){
			//支持obj/attr的方式赋值
			sb.append(transToSetMethod(varName)).append("(\"").append(varVal).append("\");");
		}else{
			// FIXME 对数组 array[0] = abc处理有问题
			// 根据varStore中的数据定义记录，判断常量是否添加引号
			String type = varStore.findVarType(varName);
			if(JavaTypeUtil.isPrimitiveType(type)){
				sb.append(genPrimitiveInitCode(false,type,varName,varVal,false));
			}else{
				if(InputDataValidate.checkConstantString(varVal)){
					sb.append(varName).append(" = ").append(varVal).append(";");
				}else{
					sb.append(varName).append(" = \"").append(varVal).append("\";");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * JAVA类变量的生成
	 * @return
	 */
	private String genClassTypeCode(boolean varDef,String varType,String varName,String varVal){
		StringBuffer sb = new StringBuffer();
		if(varDef==true){
			sb.append(varType);
		}
		sb.append(" ").append(varName).append(" = new ").append(varType);
		if(StringUtils.isNotEmpty(varVal)){
			//如果有初始值，则放在构造函数括号里
			sb.append("(").append(varVal).append(");");
		}else{
			//如果不设置初始值，则初始为null
			sb.append("();");
		}
		
		return sb.toString();
	}
	
	/**
	 * 对转义字符进行还原
	 * @param str
	 * @return
	 */
	private String convertSpliter(String str){
		String retVal = "";
		retVal = str.replaceAll("\\[;\\]", ":").replaceAll("\\[!\\]", "\\|");
		return retVal;
	}
	

	/**
	 * 对引号进行转义
	 * @param str
	 * @return
	 */
	private String convertRemark(String str){
		String retVal = "";
		//将双引号“转换成转义引号\"
		retVal = str.replaceAll("\"", "\\\\\"");
		return retVal;
	}
}
