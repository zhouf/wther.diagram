package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.Cell;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.STableButtonOnClickListener;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;

//添加变长参数的操作
public class VariadicMapPutListener implements STableButtonOnClickListener {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(VariadicMapPutListener.class.getName());

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params,Event e, String currControl,String tableKey) {
			AgileGrid agileGrid=(AgileGrid)coms.get(tableKey);
			InnerTableContentProvider provider=(InnerTableContentProvider)agileGrid.getContentProvider();
			Map<Integer,Map<Integer,Object>> map = provider.data.get(0);;
			
			if("addVariadicItem".equalsIgnoreCase(currControl)){
				//添加
				List<Integer> keys=Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
				int keySize = keys.size();
				
				Map<Integer,Object> data=new HashMap<Integer,Object>();
				data.put(0, keySize+1);
				data.put(1, "");
				data.put(2, "");
				map.put(keySize, data);//放入一行的数据
				
				
			}else if("delVariadicItem".equalsIgnoreCase(currControl)){
				
				Cell[] selectCell = agileGrid.getCellSelection();
				if (!(selectCell != null && selectCell.length > 0)) {
					// 如果表格中单元格没有被选择
					MessageDialog.openInformation(null, "删除操作", "请选择需要删除的行");
					return;
				}
				int row = agileGrid.getCellSelection()[0].row;
				
				if (provider.data.size() == 0) {
					provider.data.add(new HashMap<Integer, Map<Integer, Object>>());
				}
				
				List<Integer> keys = Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
				
				if (row < keys.size()) {
					// 如果焦点行在有效表格行内，解决删除最后一行的后出现的焦点bug
					
					map.remove(row);
					
					int index = keys.indexOf(row);
					for (int i = index + 1; i < keys.size(); i++) {
						Map<Integer, Object> mapTemp = map.remove(i);
						// mapTemp.put(0, i);
						map.put(i - 1, mapTemp);
					}
				}
			}else{
				return;
			}
			
			provider.adjustData();
			params.put(tableKey, provider.data.toArray(new Map[provider.data.size()]));
			agileGrid.redraw();


	}

}
