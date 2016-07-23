package com.sunsheen.jfids.studio.wther.diagram.parser.item;


import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ParamUtil;


/**
 * 参数节点中的每个参数项
 * @author zhouf
 */
public class CallArgItemMapPut {
	private String argKey;
	private String argKeyType;
	private String value;
	private String valType;
	
	public CallArgItemMapPut() {
		this("");
	}
	
	public CallArgItemMapPut(String initStr){
		//type:name1:initval:valType:comment:tipconf
		if(StringUtils.isNotEmpty(initStr)){
			String items[] = ParamUtil.praseStrToArray(initStr, 6);
			this.argKey = "<NOSET>".equals(items[0])? "" : items[0];
			this.argKeyType = "<NOSET>".equals(items[1])? "" : items[1];
			this.value = "<NOSET>".equals(items[2])? "" : items[2];
			this.valType = "<NOSET>".equals(items[3])? "" : items[3];
		}
	}
	public String getArgKey() {
		return argKey;
	}
	public void setArgKey(String argKey) {
		this.argKey = argKey;
	}
	public String getArgKeyType() {
		return argKeyType;
	}
	public void setArgKeyType(String argKeyType) {
		this.argKeyType = argKeyType;
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

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append(StringUtils.isEmpty(argKey)?"<NOSET>":argKey).append(":");
		retVal.append(StringUtils.isEmpty(argKeyType)?"<NOSET>":argKeyType).append(":");
		retVal.append(StringUtils.isEmpty(value)?"<NOSET>":value).append(":");
		retVal.append(StringUtils.isEmpty(valType)?"<NOSET>":valType).append(":");
		return retVal.toString();
	}
}
