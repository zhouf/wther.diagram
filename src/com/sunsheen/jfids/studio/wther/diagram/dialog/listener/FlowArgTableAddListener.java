package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.STableButtonOnClickListener;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;

public class FlowArgTableAddListener implements STableButtonOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params,Event e, String currControl,String tableKey) {
		AgileGrid agileGrid=(AgileGrid)coms.get(tableKey);
		InnerTableContentProvider provider=(InnerTableContentProvider)agileGrid.getContentProvider();
		Map<Integer,Map<Integer,Object>> map;
		String firstColLabel,namePrefix;
		int offsetIndex = 0;	//插入位置偏移量
		
		if("addArgItemKey".equalsIgnoreCase(currControl)){
			firstColLabel = "参数";
			namePrefix = "arg";
			if(provider.data.size()==0){
				provider.data.add(new HashMap<Integer, Map<Integer,Object>>());
			}
			map=provider.data.get(0);
		}else if("addReturnItemKey".equalsIgnoreCase(currControl)){
			firstColLabel = "返回";
			namePrefix = "ret";
			if(provider.data.size()==1){
				provider.data.add(new HashMap<Integer, Map<Integer,Object>>());
			}
			map=provider.data.get(1);
			//如果是插入返回值，则加上第一个参数个数
			offsetIndex = provider.data.get(0).size();
		}else{
			return;
		}
		List<Integer> keys=Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
		
		int keySize = keys.size();
		
		Map<Integer,Object> data=new HashMap<Integer,Object>();
		data.put(0, firstColLabel);
		data.put(1, namePrefix+(keySize+1));//name
		data.put(2, "String");
		data.put(3, false);//是否数组
		//data.put(4, type);
//		map.put(keySize, data);//放入一行的数据
		if("addArgItemKey".equalsIgnoreCase(currControl)){
			provider.data.get(0).put(keySize, data);
		}else{
			provider.data.get(1).put(keySize + offsetIndex, data);
		}
		
		provider.adjustData();
		params.put(tableKey, provider.data.toArray(new Map[provider.data.size()]));
		agileGrid.redraw();


	}

}
