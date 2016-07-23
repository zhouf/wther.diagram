package com.sunsheen.jfids.studio.wther.diagram.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.parser.item.AssignmentItem;


public class AssignmentParser{

	List<AssignmentItem> itemSet;
	
	public AssignmentParser() {
		this("");
	}
	
	public AssignmentParser(String initStr) {
		itemSet = new ArrayList<AssignmentItem>();
		if (StringUtils.isNotEmpty(initStr)) {
			for (String eachStr : initStr.split("\\|")) {
				itemSet.add(new AssignmentItem(eachStr));
			}
		}
	}
	
	public List<AssignmentItem> getItemSet() {
		return itemSet;
	}
	
	public void addItem(AssignmentItem item){
		this.itemSet.add((AssignmentItem)item);
	}

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		for (AssignmentItem item : itemSet) {
			retVal.append(item.toString()).append("|");
		}
		// 去掉最后一个|
		if (retVal.length() > 0) {
			retVal.setLength(retVal.length() - 1);
		}
		return retVal.toString();
	}
}
