package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;

public class OnSelected implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params,
			Event event, String currentKey) {
		List show=(List)coms.get("show");//列表框；	
		Label showText=(Label)coms.get("showText");
		
		if(show.getSelectionIndex()>=0){
			String dispText = show.getSelection()[0];
			showText.setText(dispText);
		}
	}
	
}
