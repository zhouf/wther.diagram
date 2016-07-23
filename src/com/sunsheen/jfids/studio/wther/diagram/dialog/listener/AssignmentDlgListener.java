package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;


public class AssignmentDlgListener implements SDialogOnClickListener{
	@Override
	public void run(Map<String, Widget> coms,Map<String,Object> params,Event e,String currControl) {
		AgileGrid table=(AgileGrid)coms.get("expandTable");	
		InnerTableContentProvider privoder=(InnerTableContentProvider) table.getContentProvider();
		params.put("expandTable", privoder.data);

		Text nodeName = (Text)coms.get("nodename");
		Text comment = (Text)coms.get("comment");
		params.put("nodename", nodeName.getText());
		params.put("comment", comment.getText());
	}
}
