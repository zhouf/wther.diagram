package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.Cell;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.STableButtonOnClickListener;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;

public class ExpandTableDeleteListener implements STableButtonOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params,
			Event e, String currControl, String tableKey) {
		AgileGrid agileGrid = (AgileGrid) coms.get(tableKey);
		InnerTableContentProvider provider = (InnerTableContentProvider) agileGrid
				.getContentProvider();
		// int row=agileGrid.getFocusCell().row;
		Cell[] selectCell = agileGrid.getCellSelection();
		if (!(selectCell != null && selectCell.length > 0)) {
			// 如果表格中单元格没有被选择
			MessageDialog.openInformation(null, "删除操作", "请选择需要删除的行");
			return;
		}
		int row = agileGrid.getCellSelection()[0].row;
		if (row < 0) {
			// 如果没有选择行，则不执行操作，直接返回
			return;
		}

		if (provider.data.size() == 0) {
			provider.data.add(new HashMap<Integer, Map<Integer, Object>>());
		}
		Map<Integer, Map<Integer, Object>> map = provider.data.get(0);

		List<Integer> keys = Arrays.asList(map.keySet().toArray(
				new Integer[map.keySet().size()]));

		if (row < keys.size()) {
			// 如果焦点行在有效表格行内，解决删除最后一行的后出现的焦点bug

			map.remove(row);

			int index = keys.indexOf(row);
			for (int i = index + 1; i < keys.size(); i++) {
				Map<Integer, Object> mapTemp = map.remove(i);
				// mapTemp.put(0, i);
				map.put(i - 1, mapTemp);
			}
			provider.adjustData();
			agileGrid.redraw();
			params.put(tableKey,
					provider.data.toArray(new Map[provider.data.size()]));
		}
		((SDialog) agileGrid.getParams().get("SDIALOG")).validate();
	}

}
