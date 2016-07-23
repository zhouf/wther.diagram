package com.sunsheen.jfids.studio.wther.diagram.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.parser.item.DataPortArgItem;
/**
 * 这是一个处理this.run对话框表格参数的一个类
 * @author zhouf
 */
public class DataPortArgParser {
	List<DataPortArgItem> itemSet;

	public DataPortArgParser() {
		this("");
	}
	public DataPortArgParser(String initStr) {
		itemSet = new ArrayList<DataPortArgItem>();
		if (StringUtils.isNotEmpty(initStr)) {
			for (String eachStr : initStr.split("\\|")) {
				itemSet.add(new DataPortArgItem(eachStr));
			}
		}
	}
	
	public List<DataPortArgItem> getItemSet() {
		return itemSet;
	}
	
	public void addItem(DataPortArgItem item){
		this.itemSet.add(item);
	}

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		for(DataPortArgItem item : itemSet){
			retVal.append(item.toString()).append("|");
		}
		// 去掉最后一个|
		if (retVal.length() > 0) {
			retVal.setLength(retVal.length() - 1);
		}
		return retVal.toString();
	}
}
