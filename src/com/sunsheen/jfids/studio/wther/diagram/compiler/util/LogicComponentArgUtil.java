package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;


/**
 * 这是一个用于处理构件调用参数的一个类
 * @author zhouf
 */
public class LogicComponentArgUtil {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(LogicComponentArgUtil.class.getName());
	
	private VarStore varStore;
	private StringBuilder str;
	
	public LogicComponentArgUtil(String varStoreStr) {
		varStore = new VarStore(varStoreStr);
		str = new StringBuilder();
	}

	/**
	 * 接收参数添加的一个方法
	 * @param type 参数类型
	 * @param argVal 参数值设置
	 * @param valType 常量，变量，表达式
	 * @return void
	 */
	public void appendArg(String type,String argVal,String valType){
		//对变长参数的处理，去掉最后的三个.
		if(type.endsWith("...")){
			type = type.substring(0, type.length()-3);
		}
		str.append(parseVarToObjectDescribe(type, argVal, valType));
	}

	/**
	 * 将参数调用转换为类型字串
	 * @param type 变量类型
	 * @param argVal 参数名
	 * @param valType 参数类型，常量、变量
	 * @return String 用于类类型描述的字串，如：Float.valueOf(abc)
	 */
	public String parseVarToObjectDescribe(String type, String argVal, String valType) {
		StringBuilder sb = new StringBuilder();
		if ("常量".equals(valType)) {
			if(type.endsWith("String")){
				if(!argVal.startsWith("\"")){
					sb.append("\"");
				}
				if(argVal.endsWith("\"")){
					sb.append(argVal).append(",");
				}else{
					sb.append(argVal).append("\",");
				}
			}else if(type.endsWith("char")){
				//Character.valueOf('c');
				sb.append("Character.valueOf('").append(argVal.trim().substring(0, 1)).append("'),");
			}else if("boolean".equalsIgnoreCase(type) || type.endsWith("Boolean")){
				//Boolean.valueOf(i)
				sb.append("Boolean.valueOf(").append(argVal.trim()).append("),");
			}else if("short".equalsIgnoreCase(type) || type.endsWith("Short")){
				//Short.valueOf(i)
				sb.append("Short.valueOf(").append(argVal.trim()).append("),");
			}else if("int".equalsIgnoreCase(type) || type.endsWith("Integer")){
				//Integer.valueOf(i)
				sb.append("Integer.valueOf(").append(argVal.trim()).append("),");
			}else if("long".equalsIgnoreCase(type) || type.endsWith("Long")){
				//Long.valueOf(i)
				sb.append("Long.valueOf(").append(argVal.trim()).append("),");
			}else if("float".equalsIgnoreCase(type) || type.endsWith("Float")){
				//Float.valueOf(i)
				sb.append("Float.valueOf(").append(argVal.trim()).append("),");
			}else if("double".equalsIgnoreCase(type) || type.endsWith("Double")){
				//Double.valueOf(i)
				sb.append("Double.valueOf(").append(argVal.trim()).append("),");
			}else if("Object".equalsIgnoreCase(type)){
				//当为常量时，考虑将填入参数转换为对象 Object... 1 2L 转换为Integer 和Long 1.1 2.2d 转换为Float和Double
				makeObjectTypeString(argVal);
			}else{
				sb.append(argVal).append(",");
			}
		} else {
			//变量的情况
			if("char".equals(type) || ("Character".equals(type) && varStore.isVarPrimitiveType(argVal))){
				sb.append("Character.valueOf('").append(argVal.trim().substring(0, 1)).append("'),");
			}else if("boolean".equals(type) || ("Boolean".equals(type) && varStore.isVarPrimitiveType(argVal))){
				sb.append("Boolean.valueOf(").append(argVal.trim()).append("),");
			}else if("short".equals(type) || ("Short".equals(type) && varStore.isVarPrimitiveType(argVal))){
				sb.append("Short.valueOf(").append(argVal.trim()).append("),");
			}else if("int".equals(type) || ("Integer".equals(type) && varStore.isVarPrimitiveType(argVal))){
				sb.append("Integer.valueOf(").append(argVal.trim()).append("),");
			}else if("long".equals(type) || ("Long".equals(type) && varStore.isVarPrimitiveType(argVal))){
				sb.append("Long.valueOf(").append(argVal.trim()).append("),");
			}else if("float".equals(type) || ("Float".equals(type) && varStore.isVarPrimitiveType(argVal))){
				sb.append("Float.valueOf(").append(argVal.trim()).append("),");
			}else if("double".equals(type) || ("Double".equals(type) && varStore.isVarPrimitiveType(argVal))){
				sb.append("Double.valueOf(").append(argVal.trim()).append("),");
				
			//}else if("Object".equals(type) && varStore.isVarPrimitiveType(argVal)){
				//构件调用时，Object类型会在编译时自动处理
				//sb.append(makeVarObjectDescribe(varStore.findVarType(argVal),argVal)).append(",");
			}else{
				sb.append(argVal).append(",");
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * 将变量转换为目标类描述字串
	 * @param targetClassType Float 或 float
	 * @param var abc
	 * @return String Float.valueOf(abc)
	 */
	private String makeVarObjectDescribe(String targetClassType,String var){
		log.debug("LogicComponentArgUtil makeVarObjectDescribe()-> :targetClassType["+targetClassType+"] var["+var+"]");
		if(JavaTypeUtil.isPrimitiveType(targetClassType)){
			targetClassType = JavaTypeUtil.convertPrimitiveToClass(targetClassType);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(targetClassType).append(".valueOf(").append(var.trim()).append(")");
		log.debug("LogicComponentArgUtil makeVarObjectDescribe()-> :sb:" + sb);
		return sb.toString();
	}

	/**
	 * 处理数字常量生成Object类型的代码字串
	 * @param argVal 传入的填写参数值，如2.3f 转换为常量表示的字串，如Float.valueOf(2.3f)
	 * @return void
	 */
	private void makeObjectTypeString(String argVal) {
		if(StringUtils.isNumeric(argVal)){
			if(argVal.contains(".")){
				//如果含有小数点，则转换为Float
				str.append("Float.valueOf(").append(argVal.trim()).append("),");
			}else{
				str.append("Integer.valueOf(").append(argVal.trim()).append("),");
			}
		}else if(argVal.toUpperCase().endsWith("L")){
			//如果是以L结束的，判断之前的内容是否是数字
			String subStr = argVal.substring(0, argVal.length()-1);
			if(StringUtils.isNumeric(subStr)){
				str.append("Long.valueOf(").append(argVal.trim()).append("),");
			}
		}else if(argVal.toUpperCase().endsWith("D")){
			//如果是以D结束的，判断之前的内容是否是数字
			String subStr = argVal.substring(0, argVal.length()-1);
			if(StringUtils.isNumeric(subStr)){
				str.append("Double.valueOf(").append(argVal.trim()).append("),");
			}
		}else{
			//其它的转换为字符串
			str.append("\"").append(argVal.trim()).append("\",");
		}
	}
	
	public String argStr() {
		return str.toString();
	}
}
