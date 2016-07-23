package com.sunsheen.jfids.studio.wther.diagram.compiler;

import java.util.ArrayList;
import java.util.List;

public enum JavaType {
	CHAR("char"),
	BOOLEAN("boolean"),
//	BYTE("byte"),
	SHORT("short"),
	INT("int"),
	LONG("long"),
	FLOAT("float"),
	DOUBLE("double"),
	DATE("Date"),
	STRING("String"),
	ARRAYLIST("ArrayList"),
	HASHMAP("HashMap");
//	VARIABLE("变量"),
//	CONSTANT("常量"),
//	EXPRESSTION("表达式");
	//"Boolean",/*"Byte",*/"char","int","Date","Double","Float","HashMap","Integer","Long","Short","String","表达式"}
	//字符类型char，布尔类型boolean以及数值类型byte、short、int、long、float、double
	
	private String type;
	JavaType(String type){
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
	public boolean equals(String inStr){
		return this.type.equalsIgnoreCase(inStr); 
	}
	
	/**
	 * 将枚举转换成数组，供类型下拉列表使用
	 * @return
	 */
	public static List<String> toArray(){
		ArrayList<String> list = new ArrayList<String>();
		for(JavaType t : JavaType.values()){
			list.add(t.toString());
		}
		return list;
	}
}
