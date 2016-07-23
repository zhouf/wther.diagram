package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Map;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;

public class CustomDlgListener implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms,Map<String,Object> params,Event e,String currControl) {

		StyledText custom = (StyledText)coms.get("custom");
		Text nodeName = (Text)coms.get("nodename");
		Text comment = (Text)coms.get("comment");
		params.put("custom", custom.getText());
		params.put("nodename", nodeName.getText());
		params.put("comment", comment.getText());
	}

}
