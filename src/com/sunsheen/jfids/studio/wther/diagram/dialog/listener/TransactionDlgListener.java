package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Map;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;

public class TransactionDlgListener implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms,Map<String,Object> params,Event e,String currControl) {

		Text nodeName = (Text)coms.get("nodename");
		Text varName = (Text)coms.get("varname");
		Text comment = (Text)coms.get("comment");
		params.put("nodename", nodeName.getText());
		params.put("varname", varName.getText());
		params.put("comment", comment.getText());
		
		Button canTransfer = (Button)coms.get("cantransfer");
		params.put("cantransfer", canTransfer.getSelection());
	}

}
