package com.sunsheen.jfids.studio.wther.diagram.dialog.popup.listener;

import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;

public class BeanDBClick implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params, Event event, String currentKey) {
		//调用确定操作，并关闭窗口
		SelectBeanOKListener selectBeanOkListener = new SelectBeanOKListener();
		selectBeanOkListener.run(coms, params, event, currentKey);
		
		SDialog dialog = (SDialog) params.get("object");
		dialog.closed();
	}
}
