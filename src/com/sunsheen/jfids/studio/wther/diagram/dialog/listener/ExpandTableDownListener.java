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

public class ExpandTableDownListener implements STableButtonOnClickListener {

	@Override
	public void run(Map<String, Widget> coms,Map<String,Object> params,Event e,String currControl,String tableKey) {
		AgileGrid agileGrid=(AgileGrid)coms.get(tableKey);
		InnerTableContentProvider provider=(InnerTableContentProvider)agileGrid.getContentProvider();
		if(provider.data.size()==0){
			provider.data.add(new HashMap<Integer, Map<Integer,Object>>());
		}
		Map<Integer,Map<Integer,Object>> map=provider.data.get(0);
		
		List<Integer> keys=Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
		int row=agileGrid.getFocusCell().row;
		
		//最后一行不能下移，范围是0~size-2
		if(row>=0 && row<(keys.size()-1)){
			Map<Integer,Object> rowTemp =  map.get(row);
			map.put(row, map.get(row+1));
			map.put(row+1, rowTemp);
		}

		agileGrid.redraw();
 		params.put(tableKey, provider.data.toArray(new Map[provider.data.size()]));
	}

}
