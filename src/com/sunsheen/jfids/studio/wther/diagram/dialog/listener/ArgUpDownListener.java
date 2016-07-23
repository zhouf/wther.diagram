package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.Cell;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.STableButtonOnClickListener;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;

public class ArgUpDownListener implements STableButtonOnClickListener {

	@Override
	public void run(Map<String, Widget> coms,Map<String,Object> params,Event e,String currControl,String tableKey) {
		AgileGrid agileGrid=(AgileGrid)coms.get(tableKey);
		InnerTableContentProvider provider=(InnerTableContentProvider)agileGrid.getContentProvider();
		Map<Integer,Map<Integer,Object>> map;
		Cell focusCell = agileGrid.getFocusCell(); 
		int row = focusCell.row;
		if(provider.data.size()==0 || row<0){
			//没有数据表格或未选中行时，不执行操作，直接返回
			MessageDialog.openInformation(null,"移动操作","请选择需要移动的行");
			return;
		}
		int argSize = provider.data.get(0).size();
		boolean argMode = (row<argSize);
		int low = 0,limit = 0;
		
		if(argMode){
			//删除参数
			map=provider.data.get(0);
		}else{
			//删除返回
			map=provider.data.get(1);
		}

		List<Integer> keys=Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
		int keySize = keys.size();
		//调整上下边界
		low = argMode? 0 : argSize;
		limit = argMode? keySize : keySize + argSize;
		
		
		if("moveUp".equalsIgnoreCase(currControl)){
			//第一行不能上移，范围是1~size-1
			if(row>low && row <limit){
				Map<Integer,Object> rowTemp =  map.get(row);
				map.put(row, map.get(row-1));
				map.put(row-1, rowTemp);
				agileGrid.focusCell(new Cell(agileGrid,focusCell.row-1,focusCell.column));
			}
		}else if("moveDown".equalsIgnoreCase(currControl)){
			//最后一行不能下移，范围是0~size-2
			if(row>=low && row<(limit-1)){
				Map<Integer,Object> rowTemp =  map.get(row);
				map.put(row, map.get(row+1));
				map.put(row+1, rowTemp);
				agileGrid.focusCell(new Cell(agileGrid,focusCell.row+1,focusCell.column));
			}
		}

		provider.adjustData();
		agileGrid.redraw();
 		params.put(tableKey, provider.data.toArray(new Map[provider.data.size()]));
	}

}
