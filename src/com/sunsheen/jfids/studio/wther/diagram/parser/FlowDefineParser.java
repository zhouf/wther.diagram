package com.sunsheen.jfids.studio.wther.diagram.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.parser.item.FlowDefineItem;

/**
 * 处理页面定义的解析器，将字串转换为对象
 * @author zhouf
 */
public class FlowDefineParser {

	List<FlowDefineItem> itemSet;

	public FlowDefineParser() {
		this("");
	}

	public FlowDefineParser(String initStr) {
		itemSet = new ArrayList<FlowDefineItem>();
		if (StringUtils.isNotEmpty(initStr)) {
			for (String eachStr : initStr.split("\\|")) {
				itemSet.add(new FlowDefineItem(eachStr));
			}
		}
	}

	public List<FlowDefineItem> getItemSet() {
		return itemSet;
	}
	
	public void addItem(FlowDefineItem item){
		this.itemSet.add(item);
	}

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		for (FlowDefineItem item : itemSet) {
			retVal.append(item.toString()).append("|");
		}
		// 去掉最后一个|
		if (retVal.length() > 0) {
			retVal.setLength(retVal.length() - 1);
		}
		return retVal.toString();
	}

}
