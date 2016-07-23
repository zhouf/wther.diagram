package com.sunsheen.jfids.studio.wther.diagram.parser.item;


import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ParamUtil;

/**
 * 页面属性中的每个变量定义项
 * @author zhouf
 */
public class FlowDefineItem {
	private String varName;
	private String initVal;
	private String type;
	private boolean arrayType;
	private boolean defined;
	
	public FlowDefineItem(){
		this("");
	}
	
	public FlowDefineItem(String initStr) {
		//name1:initval:String:false:true
		// FIXME 字符转义
		if(StringUtils.isNotEmpty(initStr)){
			String items[] = ParamUtil.praseStrToArray(initStr, 5);
			this.varName = items[0];
			this.initVal = items[1];
			this.type = items[2];
			this.arrayType = "true".equalsIgnoreCase(items[3]);
			this.defined = "true".equalsIgnoreCase(items[4]);
		}
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getInitVal() {
		return initVal;
	}

	public void setInitVal(String initVal) {
		this.initVal = initVal;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isArrayType() {
		return arrayType;
	}

	public void setArrayType(boolean arrayType) {
		this.arrayType = arrayType;
	}

	public boolean isDefined() {
		return defined;
	}

	public void setDefined(boolean defined) {
		this.defined = defined;
	}
	
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append(varName).append(":");
		retVal.append(initVal).append(":");
		retVal.append(type).append(":");
		retVal.append(arrayType?"true":"false").append(":");
		retVal.append(defined?"true":"false");
		
		return retVal.toString();
	}

}
