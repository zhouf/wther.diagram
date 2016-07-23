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
import com.sunsheen.jfids.studio.wther.diagram.edit.util.Constant;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.CallArgItem;

//添加变长参数的操作
public class VariadicAddListener implements STableButtonOnClickListener {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(VariadicAddListener.class.getName());

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params,Event e, String currControl,String tableKey) {
		
		if(params.get(Constant.VARIADIC_ARG)==null){
			// 如果不存在变长参数，则直接返回
			return;
		}else{
			
			AgileGrid agileGrid=(AgileGrid)coms.get(tableKey);
			InnerTableContentProvider provider=(InnerTableContentProvider)agileGrid.getContentProvider();
			Map<Integer,Map<Integer,Object>> map = provider.data.get(0);;
			
			if("addVariadicItem".equalsIgnoreCase(currControl)){
				//添加
				List<Integer> keys=Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
				int keySize = keys.size();
				
				CallArgItem callArgItem = (CallArgItem) params.get(Constant.VARIADIC_ARG);
				log.debug("VariadicAddListener run()-> callArgItem.getVarType():" + callArgItem.getVarType());
				
				Map<Integer,Object> data=new HashMap<Integer,Object>();
				data.put(0, "参数");
				data.put(1, callArgItem.getVarType());
				data.put(2, callArgItem.getVarName());
				data.put(4, "变量");
				data.put(5, callArgItem.getComment());
				//data.put(4, type);
				map.put(keySize, data);//放入一行的数据
				
				
			}else if("delVariadicItem".equalsIgnoreCase(currControl)){
				int variadicIndex = (Integer) params.get(Constant.VARIADIC_INDEX);
				log.debug("VariadicAddListener run()-> variadicIndex:" + variadicIndex);
				
				Cell[] selectCell = agileGrid.getCellSelection();
				if (!(selectCell != null && selectCell.length > 0)) {
					// 如果表格中单元格没有被选择
					MessageDialog.openInformation(null, "删除操作", "请选择需要删除的行");
					return;
				}
				int row = agileGrid.getCellSelection()[0].row;
				
				if (row < variadicIndex) {
					// 如果选择行不属于可变参数，直接返回，可变参数至少保留一个，则改为<=即可
					return;
				}
				
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

}
