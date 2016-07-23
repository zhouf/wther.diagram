package com.sunsheen.jfids.studio.wther.diagram.dialog.editor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FindClass;

public class TextEditorUtil {


	/**
	 * 从定义的变量里查找变量及类型，通过反射获取变量的成员方法
	 * 
	 * @param varName 输入变量名
	 * @param varArray {"varName varType","abc String&from"}
	 * @return 返回可执行的方法字串
	 */
	public static String[] getMethods(String varName,String[] varArray) {
		// TODO 变量方法的多级提示，如obj.toString().之后的方法提示
		String retArray[] = new String[] {};
		ArrayList<String> methodList = new ArrayList<String>();
		for (String eachVar : varArray) {
			String items[] = eachVar.split(" "); // 变量名和类型是按空格分开的
			if (items.length == 2) {
				if (varName.equals(items[0])) {
					// 找到了变量，判断变量类型
					String type = items[1];
					if(type.contains("&")){
						type = type.substring(0,type.indexOf("&"));
					}
					Log.debug("ConditionTextCellEditor getMethods()-> 判断类型:" + type);
					if (JavaTypeUtil.isPrimitiveType(type)) {
						Log.debug("ConditionTextCellEditor.getMethods() 简单类型，无可用方法");
						break;
					} else {
						if (JavaTypeUtil.containsKey(type)) {
							type = JavaTypeUtil.convertShortToLongType(type);
						}
						try {
							//Class<?> className = Class.forName(type);
							Class<?> className = FindClass.fromString(type);
							for (Method method : className.getDeclaredMethods()) {
								if ((method.getModifiers() & Modifier.PUBLIC) > 0) {
									// 只加载公有方法
									String methodName = method.getName();
									StringBuffer dispArg = new StringBuffer();
									for (Class<?> param : method.getParameterTypes()) {
										dispArg.append(param.getSimpleName()).append(",");
									}
									if (dispArg.toString().length() > 0) {
										dispArg.deleteCharAt(dispArg.lastIndexOf(","));
									}
									methodList.add(methodName + "(" + dispArg + ")");
								}
							}
							if (methodList.size() > 0) {
								retArray = methodList.toArray(retArray);
							}
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			} else {
				Log.error("ConditionTextCellEditor.getMethods-> :分解变量名及类型时出错：" + eachVar);
			}
		}
		return retArray;
	}
}
