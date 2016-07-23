package com.sunsheen.jfids.studio.wther.diagram.parser.item;


import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ParamUtil;

/**
 * 参数节点中的每个参数项
 * @author zhouf
 */
public class CallArgItem {
	private String varType;
	private String varName;
	private String value;
	private String valType;
	private String comment;
	private String tipconf;
	
	public CallArgItem() {
		this("");
	}
	
	public CallArgItem(String initStr){
		//type:name1:initval:valType:comment:tipconf
		if(StringUtils.isNotEmpty(initStr)){
			String items[] = ParamUtil.praseStrToArray(initStr, 6);
			this.varType = "<NOSET>".equals(items[0])? "" : items[0];
			this.varName = "<NOSET>".equals(items[1])? "" : items[1];
			this.value = "<NOSET>".equals(items[2])? "" : items[2];
			this.valType = "<NOSET>".equals(items[3])? "" : items[3];
			this.comment = "<NOSET>".equals(items[4])? "" : items[4];
			this.tipconf = "<NOSET>".equals(items[5])? "" : items[5];
		}
	}
	public String getVarType() {
		return varType;
	}
	public void setVarType(String varType) {
		this.varType = varType;
	}
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValType() {
		return valType;
	}
	public void setValType(String valType) {
		this.valType = valType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getTipconf() {
		return tipconf;
	}
	public void setTipconf(String tipconf) {
		this.tipconf = tipconf;
	}

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append(StringUtils.isEmpty(varType)?"<NOSET>":varType).append(":");
		retVal.append(StringUtils.isEmpty(varName)?"<NOSET>":varName).append(":");
		retVal.append(StringUtils.isEmpty(value)?"<NOSET>":value).append(":");
		retVal.append(StringUtils.isEmpty(valType)?"<NOSET>":valType).append(":");
		retVal.append(StringUtils.isEmpty(comment)?"<NOSET>":comment).append(":");
		retVal.append(StringUtils.isEmpty(tipconf)?"<NOSET>":tipconf);
		
		return retVal.toString();
	}
}
