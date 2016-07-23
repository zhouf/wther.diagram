package com.sunsheen.jfids.studio.wther.diagram.parser.item;


import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ParamUtil;


/**
 * 页面定义中的每个返回项
 * @author zhouf
 */
public class FlowArgItem {
	private String type;
	private String valName;
	private boolean arrayType;
	private String comment;

	public FlowArgItem() {
		this("");
	}
	
	public FlowArgItem(String initStr){
		//String:ret1:true:comment
		if(StringUtils.isNotEmpty(initStr)){
			String items[] = ParamUtil.praseStrToArray(initStr, 4);
			this.type = items[0];
			this.valName = items[1];
			this.arrayType = "true".equalsIgnoreCase(items[2]);
			this.comment = items[3];
		}
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValName() {
		return valName;
	}
	public void setValName(String valName) {
		this.valName = valName;
	}
	public boolean isArrayType() {
		return arrayType;
	}
	public void setArrayType(boolean arrayType) {
		this.arrayType = arrayType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append(type).append(":");
		retVal.append(valName).append(":");
		retVal.append(arrayType? "true" : "false").append(":");
		retVal.append(comment);
		
		return retVal.toString();
	}
	

}
