package com.sunsheen.jfids.studio.wther.diagram.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.parser.item.CallArgItemMapPut;

public class CallArgParserMapPut {
	List<CallArgItemMapPut> itemSet;

	public CallArgParserMapPut() {
		this("");
	}
	public CallArgParserMapPut(String initStr) {
		itemSet = new ArrayList<CallArgItemMapPut>();
		if (StringUtils.isNotEmpty(initStr)) {
			for (String eachStr : initStr.split("\\|")) {
				itemSet.add(new CallArgItemMapPut(eachStr));
			}
		}
	}
	
	public List<CallArgItemMapPut> getItemSet() {
		return itemSet;
	}
	
	public void addItem(CallArgItemMapPut item){
		this.itemSet.add(item);
	}

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		for(CallArgItemMapPut item : itemSet){
			retVal.append(item.toString()).append("|");
		}
		// 去掉最后一个|
		if (retVal.length() > 0) {
			retVal.setLength(retVal.length() - 1);
		}
		return retVal.toString();
	}
}
