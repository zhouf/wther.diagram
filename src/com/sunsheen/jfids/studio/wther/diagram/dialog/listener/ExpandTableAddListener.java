package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.STableButtonOnClickListener;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;
import com.sunsheen.jfids.studio.logging.Log;

public class ExpandTableAddListener implements STableButtonOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params,
			Event e, String currControl, String tableKey) {
		AgileGrid agileGrid = (AgileGrid) coms.get(tableKey);
		InnerTableContentProvider provider = (InnerTableContentProvider) agileGrid
				.getContentProvider();
		if (provider.data.size() == 0) {
			provider.data.add(new HashMap<Integer, Map<Integer, Object>>());
		}
		Map<Integer, Map<Integer, Object>> map = provider.data.get(0);
		List<Integer> keys = Arrays.asList(map.keySet().toArray(
				new Integer[map.keySet().size()]));
		boolean definedItem = false, isArray = false;
		
		//遍历已存在的变量，为避免生成名重复
		Set<String> definedNames = new HashSet<String>();
		Iterator<Integer> iterator = keys.iterator();
		while (iterator.hasNext()) {
			definedNames.add(String.valueOf(map.get(iterator.next()).get(0)));
		}

		int keySize = keys.size();
		Map<Integer, Object> data = new HashMap<Integer, Object>();
		String defType = "";
		String namePrefix = "name";
		int nameIndex = 1;
		while(definedNames.contains(namePrefix+nameIndex)){
			nameIndex++;
		}
		if ("addVarItem".equalsIgnoreCase(currControl)) {
			// 添加页面定义变量
			definedItem = true;
			defType = "String";
			
			data.put(0, namePrefix + nameIndex);// name
			data.put(1, "");// value
			data.put(2, defType);// 参数类型
			data.put(3, isArray);// 是否数组
			data.put(4, definedItem);// 是否定义
			
		} else if ("addAssigItem".equalsIgnoreCase(currControl)) {
			// 添加赋值项
			definedItem = false;
			defType = "变量";
			
			data.put(0, "");// name
			data.put(1, "");// value
			data.put(2, "");// 参数类型
			data.put(3, defType);// 参数类型
		} else if ("addGlobalItem".equalsIgnoreCase(currControl)) {
			// 添加全局变量定义
			definedItem = true;
			defType = "String";
			namePrefix = "glbVar";
			
			data.put(0, namePrefix + (keySize + 1));// name
			data.put(1, "");// value
			data.put(2, defType);// 参数类型
			data.put(3, isArray);// 是否数组
			data.put(4, definedItem);// 是否定义
		} else {
			Log.error("ExpandTableAddListener.run -> currControl[" + currControl + "]参数值不合法");
		}
		// String valuePrefix = "value";
//		List<String> list = new ArrayList<String>();
//		Iterator<Integer> iterator = keys.iterator();
//		while (iterator.hasNext()) {
//			list.add(String.valueOf(map.get(iterator.next()).get(0)));
//		}

		
		map.put(keySize, data);// 放入新增的数据

		agileGrid.redraw();
		params.put(tableKey,
				provider.data.toArray(new Map[provider.data.size()]));
		((SDialog) agileGrid.getParams().get("SDIALOG")).validate();
	}

}
