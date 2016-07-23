package com.sunsheen.jfids.studio.wther.diagram.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.parser.item.CallArgItem;

public class CallArgParser {
	List<CallArgItem> itemSet;

	public CallArgParser() {
		this("");
	}
	public CallArgParser(String initStr) {
		itemSet = new ArrayList<CallArgItem>();
		if (StringUtils.isNotEmpty(initStr)) {
			for (String eachStr : initStr.split("\\|")) {
				itemSet.add(new CallArgItem(eachStr));
			}
		}
	}
	
	public List<CallArgItem> getItemSet() {
		return itemSet;
	}
	
	public void addItem(CallArgItem item){
		this.itemSet.add(item);
	}

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		for(CallArgItem item : itemSet){
			retVal.append(item.toString()).append("|");
		}
		// 去掉最后一个|
		if (retVal.length() > 0) {
			retVal.setLength(retVal.length() - 1);
		}
		return retVal.toString();
	}
}
