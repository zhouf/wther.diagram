package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Map;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;

public class ForDlgListener implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms,Map<String,Object> params,Event e,String currControl) {
		Text nodeName=(Text)coms.get("nodename");
		Text simpleVar = (Text)coms.get("simpleVar");
		Text simpleTimes = (Text)coms.get("simpleTimes");
		Text textCode = (Text)coms.get("textCode");
		Text comment = (Text)coms.get("comment");
		
		params.put("nodename", nodeName.getText());
		params.put("simpleVar", simpleVar.getText());
		params.put("simpleTimes", simpleTimes.getText());
		params.put("textCode", textCode.getText());
		params.put("comment", comment.getText());
		
		Button simpleRadio = (Button)coms.get("simpleRadio");
		params.put("simpleRadio", simpleRadio.getSelection());
	}
}
