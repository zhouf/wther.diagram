package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.Cell;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;

public class CallTableListener implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params, Event event, String currentKey) {
		AgileGrid agileGrid = (AgileGrid) coms.get(currentKey);
		InnerTableContentProvider provider = (InnerTableContentProvider) agileGrid.getContentProvider();
		if (provider.data.size() == 0) {
			provider.data.add(new HashMap<Integer, Map<Integer, Object>>());
		}
		Cell focusCell = agileGrid.getFocusCell();
		
		Label tipLabel = (Label)coms.get("tiplabel");
		if(tipLabel!=null){
			if (focusCell.row < 0 || focusCell.column < 0 ){
				tipLabel.setText("");
				return;
			}else{
				Object obj = agileGrid.getContentAt(focusCell.row, focusCell.column);
				tipLabel.setText(obj + "");
			}
		}
	}

}
