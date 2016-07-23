package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 处理JAVA类型的一个辅助类，将simple类型转换为完成类型
 * @author zhoufeng
 * Date:2013-6-28
 */
public class JavaTypeUtil {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(JavaTypeUtil.class.getName());
	
	private static Map<String,String> types = new HashMap<String,String>();
	private static Map<String,String> convtypes = new HashMap<String,String>();
	private static Set<String> primitiveTypes = new HashSet<String>();
	private static Set<String> keywords = new HashSet<String>();
	
	static{
		//"Date","Double","Float","HashMap","Integer","Long","Short","String"
		types.put("Date", "java.util.Date");
		types.put("Float", "java.lang.Float");
		types.put("HashMap", "java.util.HashMap");
		types.put("Integer", "java.lang.Integer");
		types.put("Long", "java.lang.Long");
		types.put("Double", "java.lang.Double");
		types.put("Short", "java.lang.Short");
		types.put("String", "java.lang.String");
		types.put("Boolean", "java.lang.Boolean");
		types.put("ArrayList", "java.util.ArrayList");
		types.put("Object", "java.lang.Object");
		
		convtypes.put("char", "java.lang.Character");
		convtypes.put("boolean", "java.lang.Boolean");
		convtypes.put("short", "java.lang.Short");
		convtypes.put("int", "java.lang.Integer");
		convtypes.put("long", "java.lang.Long");
		convtypes.put("float", "java.lang.Float");
		convtypes.put("double", "java.lang.Double");
		
		primitiveTypes.add("char");
		primitiveTypes.add("boolean");
		primitiveTypes.add("byte");
		primitiveTypes.add("short");
		primitiveTypes.add("int");
		primitiveTypes.add("long");
		primitiveTypes.add("float");
		primitiveTypes.add("double");
		
		keywords.add("boolean");
		keywords.add("break");
		keywords.add("byte");
		keywords.add("case");
		keywords.add("catch");
		keywords.add("char");
		keywords.add("class");
		keywords.add("continue");
		keywords.add("default");
		keywords.add("do");
		keywords.add("double");
		keywords.add("else");
		keywords.add("extends");
		keywords.add("false");
		keywords.add("final");
		keywords.add("finally");
		keywords.add("float");
		keywords.add("for");
		keywords.add("if");
		keywords.add("implements");
		keywords.add("import");
		keywords.add("instanceof");
		keywords.add("int");
		keywords.add("interface");
		keywords.add("long");
		keywords.add("native");
		keywords.add("new");
		keywords.add("null");
		keywords.add("package");
		keywords.add("private");
		keywords.add("protected");
		keywords.add("public");
		keywords.add("return");
		keywords.add("short");
		keywords.add("static");
		keywords.add("super");
		keywords.add("switch");
		keywords.add("synchronized");
		keywords.add("this");
		keywords.add("throw");
		keywords.add("throws");
		keywords.add("transient");
		keywords.add("true");
		keywords.add("try");
		keywords.add("void");
		keywords.add("volatile");
		keywords.add("while");
		keywords.add("const");
		keywords.add("goto");
		keywords.add("abstract");
		
	}
	
	public static boolean containsKey(String shortType){
		return types.containsKey(shortType);
	}
	
	public static String convertShortToLongType(String shortType){
		return types.get(shortType);
	}

	//验证输入类型是否为基本类型
	public static boolean isPrimitiveType(String checkType){
		return primitiveTypes.contains(checkType);
	}
	
	/**
	 * 判断字串是否为关键字
	 * @param checkStr 输入待检测字串
	 * @return 如果是关键字，返回true，否则返回false
	 */
	public static boolean isKeyWords(String checkStr){
		return keywords.contains(checkStr);
	}
	
	/**
	 * 将返回中的基本类型转换为类类型
	 * @param primitiveName 基本类型名
	 * @return 类类型名
	 */
	public static String convertPrimitiveToClass(String primitiveName){
		String retVal = "";
		if(convtypes.containsKey(primitiveName)){
			retVal = convtypes.get(primitiveName);
		}else{
			retVal = primitiveName;
		}
		return retVal;
	}
	
	/**
	 * 比较提示类型是否是当前类型
	 * @param varType数据类型，如：java.lang.String,int
	 * @param requestType 需要的数据类型，如：String,Integer
	 * @return boolean 匹配返回true
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean typeMatch(String varType, String requestType) {
		boolean retVal = false;
		
		//对数组类型的处理
		boolean isArray1 = false,isArray2 = false;
		if(varType.endsWith("[]")){
			isArray1 = true;
			varType = varType.substring(0, varType.lastIndexOf("["));
		}
		if(requestType.endsWith("[]")){
			isArray2 = true;
			requestType = requestType.substring(0,requestType.lastIndexOf("["));
		}
		if(!((isArray1 && isArray2)||(!isArray1 && !isArray2))){
			//如果数组类型不匹配
			log.debug("JavaTypeUtil typeMatch()-> :数组类型不匹配，isArray1["+isArray1+"] isArray2["+isArray2+"]");
			return false;
		}
		
		if("Object".equalsIgnoreCase(requestType) || "java.lang.Object".equalsIgnoreCase(requestType)){
			//如果是Object类型，则接收所有类型数据
			retVal = true;
		}else if(requestType.equals(varType)){
			retVal = true;
		}else if(!JavaTypeUtil.isPrimitiveType(requestType)){
			//对完整类和简单类的比较，比较类之间是否有继承关系，符合继承的类型都列出
			try{
				Class requestClassType = findClassType(requestType);
				
				if(JavaTypeUtil.isPrimitiveType(varType)){
					//对基本类型的提示处理，如Integer提示包含Integer以及int
					if(Boolean.class.getName().equals(requestClassType.getName()) && "boolean".equals(varType)){
						retVal = true;
					}else if(Character.class.getName().equals(requestClassType.getName()) && "char".equals(varType)){
						retVal = true;
					}else if(Short.class.getName().equals(requestClassType.getName()) && "short".equals(varType)){
						retVal = true;
					}else if(Integer.class.getName().equals(requestClassType.getName()) && "int".equals(varType)){
						retVal = true;
					}else if(Long.class.getName().equals(requestClassType.getName()) && "long".equals(varType)){
						retVal = true;
					}else if(Float.class.getName().equals(requestClassType.getName()) && "float".equals(varType)){
						retVal = true;
					}else if(Double.class.getName().equals(requestClassType.getName()) && "double".equals(varType)){
						retVal = true;
					}
					
				}else{
					Class varTypeClass = findClassType(varType);
					//提示时对子类型的处理
					if(requestClassType.isAssignableFrom(varTypeClass)){
						retVal = true;
					}
				}
			}catch(ClassNotFoundException e){
				// 如果存在类型转换异常，则直接返回false
				log.debug("JavaTypeUtil typeMatch()-> varType["+varType+"] reqType["+requestType+"]:" + e.toString());
				retVal = false;
			}
		}else{
			//requestType为普通类型int,short等
			if(!JavaTypeUtil.isPrimitiveType(varType)){
				if(JavaTypeUtil.containsKey(varType)){
					varType = JavaTypeUtil.convertShortToLongType(varType);
				}
				if(Boolean.class.getName().equals(varType) && "boolean".equals(requestType)){
					retVal = true;
				}else if(Character.class.getName().equals(varType) && "char".equals(requestType)){
					retVal = true;
				}else if(Short.class.getName().equals(varType) && "short".equals(requestType)){
					retVal = true;
				}else if(Integer.class.getName().equals(varType) && "int".equals(requestType)){
					retVal = true;
				}else if(Long.class.getName().equals(varType) && "long".equals(requestType)){
					retVal = true;
				}else if(Float.class.getName().equals(varType) && "float".equals(requestType)){
					retVal = true;
				}else if(Double.class.getName().equals(varType) && "double".equals(requestType)){
					retVal = true;
				}
			}
			
		}
		//log.debug("JavaTypeUtil typeMatch()-> varType:" + varType + " requestType:" + requestType + " retVal:" + retVal);
		return retVal;
	}

	/**
	 * 将变量类型转换为class类型，如果是简单表示，需要转换为完成类表示
	 * @param varType 字符串表示的变量类型，如String或java.lang.String
	 * @return Class 对应的Class，用于判断是否是了类
	 * @throws ClassNotFoundException 
	 */
	public static Class findClassType(String varType) throws ClassNotFoundException {
		if(JavaTypeUtil.containsKey(varType)){
			varType = JavaTypeUtil.convertShortToLongType(varType);
		}
		
		return Class.forName(varType);
	}
	
}
