package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;

public class DBClick implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params, Event event, String currentKey) {
		//调用确定操作，并关闭窗口
		SelectBixOKListener selectBixOKListener = new SelectBixOKListener();
		selectBixOKListener.run(coms, params, event, currentKey);
		
		List show = (List) coms.get("show");// 列表框
		if (show.getSelectionCount() > 0) {
			// 当有选择时才关闭对话框
			SDialog dialog = (SDialog) params.get("object");
			dialog.closed();
		}
	}
}
