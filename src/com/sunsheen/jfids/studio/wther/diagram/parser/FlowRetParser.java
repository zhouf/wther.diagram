package com.sunsheen.jfids.studio.wther.diagram.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.parser.item.FlowRetItem;


/**
 * 处理页面返回的解释器
 * @author zhouf
 */
public class FlowRetParser {
	
	List<FlowRetItem> itemSet;
	
	public FlowRetParser() {
		this("");
	}
	public FlowRetParser(String initStr) {
		itemSet = new ArrayList<FlowRetItem>();
		if (StringUtils.isNotEmpty(initStr)) {
			for (String eachStr : initStr.split("\\|")) {
				itemSet.add(new FlowRetItem(eachStr));
			}
		}
	}
	
	public List<FlowRetItem> getItemSet() {
		return itemSet;
	}

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		for (FlowRetItem item : itemSet) {
			retVal.append(item.toString()).append("|");
		}
		// 去掉最后一个|
		if (retVal.length() > 0) {
			retVal.setLength(retVal.length() - 1);
		}
		return retVal.toString();
	}

	public void addItem(FlowRetItem item) {
		this.itemSet.add(item);
	}
}
