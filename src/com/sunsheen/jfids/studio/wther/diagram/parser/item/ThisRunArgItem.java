package com.sunsheen.jfids.studio.wther.diagram.parser.item;


import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ParamUtil;

/**
 * 参数节点中的每个参数项
 * @author zhouf
 */
public class ThisRunArgItem {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(ThisRunArgItem.class.getName());
	
	private int seq;
	private String varType;
	private String varName;
	
	public ThisRunArgItem() {
		this("");
	}
	
	public ThisRunArgItem(String initStr){
		//type:name1:initval:valType:comment:tipconf
		if(StringUtils.isNotEmpty(initStr)){
			String items[] = ParamUtil.praseStrToArray(initStr, 3);
			try{
				this.seq = Integer.parseInt(items[0]);
			}catch(NumberFormatException e){
				this.seq = 1;
				log.warn("ThisRunArgItem.ThisRunArgItem()->:seq转换异常，seq=" + items[0]);
			}
			this.varType = "<NOSET>".equals(items[1])? "" : items[1];
			this.varName = "<NOSET>".equals(items[2])? "" : items[2];
			
		}
	}
	

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
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

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		retVal.append(seq).append(":");
		retVal.append(StringUtils.isEmpty(varType)?"<NOSET>":varType).append(":");
		retVal.append(StringUtils.isEmpty(varName)?"<NOSET>":varName);
		
		return retVal.toString();
	}
}
