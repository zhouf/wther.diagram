package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;


/**
 * 这是一个对表格单元填写的数据进行验证的一个类
 * @author zhoufeng
 * Date:2013-6-4
 */
public class InputDataValidate{
	
	// 设置其它的过滤项
	static Set<String> extCheck = new HashSet<String>();


	/**
	 * 对日期格式的检查，如以下几种
	 * 2012-12
	 * 2012-12-30
	 * 2012-12-30 12:32
	 * 2012-12-30 12:32:25
	 * @param dateStr 输入的待检验字符串
	 * @return 如果符合上述格式，则返回true，否则返回false
	 */
	public static boolean checkDateStrValidate(String dateStr){
		Pattern pattern = Pattern.compile("^\\d{4}-\\d{1,2}(-\\d{1,2}( \\d{1,2}:\\d{1,2}(:\\d{1,2})?)?)?");
		Matcher matcher = pattern.matcher(dateStr);
		return matcher.matches();
	}
	
	
	/**
	 * 对数字格式的字串检查
	 * @param str 转入待检验的字串
	 * @return 如果是数字格式返回true,否则返回false
	 */
	public static boolean checkNumStrValidate(String str){
//		Pattern pattern = Pattern.compile("^[1-9](\\d*(.\\d+)?)?");
//		Pattern pattern = Pattern.compile("^[1-9][0-9]*(\\.[0-9]+)?$");//这个不能检查负数和零
		Pattern pattern = Pattern.compile("^-?(0|[1-9][0-9]*)(\\.[0-9]+)?$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	

	/**
	 * 检查整数类型数据
	 * @param str
	 * @return 如果符合整数格式返回true,否则返回false
	 */
	public static boolean checkIntegerValidate(String str){
		Pattern pattern = Pattern.compile("^-?[1-9](\\d*)?$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * 检查正整数
	 * @param str
	 * @return 如果符合正整数格式返回true,否则返回false
	 */
	public static boolean checkPositiveIntegerValidate(String str){
		Pattern pattern = Pattern.compile("^[1-9](\\d*)?$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * 检查数组的初始化
	 * @param dateStr 转入待检验的字串
	 * @return 如果是数字格式返回true,否则返回false
	 */
	public static boolean checkArray(String varValue, String varType){
		Pattern ptn1 = Pattern.compile("^\\{.*}$");
		Pattern ptn2 = Pattern.compile("^new "+varType+"\\[\\]\\{.*}$");
	    Matcher m1 = ptn1.matcher(varValue);
	    Matcher m2 = ptn2.matcher(varValue);
	    boolean retVal;
	    if(m1.matches()||m2.matches()){
	    	retVal = true;
	    }else{
	    	retVal = false;
	    }
	    return retVal;
	}
	
	/**
	 * 判断是否是字符串
	 * @param dateStr 转入待检验的字串
	 * @return 如果是字符串格式返回true,否则返回false
	 */
	public static boolean checkString(String dateStr){
		Pattern pattern = Pattern.compile("^(.*)$");
		Matcher matcher = pattern.matcher(dateStr);
		return matcher.matches();
	}
	/**
	 * 检查变量拼写是否合法
	 * @param varName 输入待检查变量
	 * @return 合法返回true,否则返回false
	 */
	public static boolean checkValidSpell(String varName) {
		Pattern pattern = Pattern.compile("^[_a-zA-Z][_a-zA-Z0-9]*$");
		Matcher matcher = pattern.matcher(varName);
		return matcher.matches();
	}
	
	
	/**
	 * 验证字符型数据常量
	 * @param str
	 * @return
	 */
	public static boolean checkConstantChar(String str){
		return str.matches("^'\\S'$");
	}
	
	/**
	 * 验证字串是否符合boolean类型数据常量
	 * @param str
	 * @return
	 */
	public static boolean checkConstantBoolean(String str){
		return ("TRUE".equalsIgnoreCase(str) || "1".equals(str)||"FALSE".equalsIgnoreCase(str)||"0".equals(str));
	}
	
	/**
	 * 验证字串是否符合String类型数据常量，要求以""包含
	 * @param str
	 * @return
	 */
	public static boolean checkConstantString(String str){
		return str.matches("^\".*\"$");
	}
	
	/**
	 * 检查变量名是否为关键字
	 * @param varName 输入的待检查变量名
	 * @return 如果不是关键字，则返回true,否则返回false
	 */
	public static boolean checkKeyWords(String varName){
		boolean retVal = false;
		retVal = !JavaTypeUtil.isKeyWords(varName);
		return retVal;
	}
	
	/**
	 * 检查附加项
	 * @param arg 需要检查的字串
	 * @return boolean 如果通过，则返回true，不通过，返回false;
	 */
	public static boolean checkExt(String arg){
		boolean retVal = false;
		retVal = !extCheck.contains(arg);
		return retVal;
	}

	/**
	 * 设置附加检查集合项
	 * @param extCheck
	 * @return void
	 */
	public static void setExtCheck(Set<String> extCheck) {
		InputDataValidate.extCheck = extCheck;
	}
	
	/**
	 * 清除附加检查集合数据
	 * @return void
	 */
	public static void cleanExtCheckData(){
		extCheck.removeAll(extCheck);
	}

}
