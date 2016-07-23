package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;

public class DataPortRetTypeSelectListener implements SDialogOnClickListener {

	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(DataPortRetTypeSelectListener.class.getName());
	
	@Override
	public void run(Map<String, Widget> coms,Map<String,Object> params,Event e,String currControl) {
		
		log.debug("DataPortRetTypeSelectListener.run()->currControl:" + currControl);
		
//		AgileGrid table=(AgileGrid)coms.get("table");	
//		table.applyEditorValue();
//		InnerTableContentProvider privoder=(InnerTableContentProvider) table.getContentProvider();
//		params.put("table", privoder.data);
//
//		Text nodeName = (Text)coms.get("nodename");
//		Text comment = (Text)coms.get("comment");
//		Text bixfile = (Text)coms.get("bixfile");
//		Text retText=(Text) coms.get("retValue");
//		params.put("retValue", retText.getText());
//		params.put("nodename", nodeName.getText());
//		params.put("comment", comment.getText());
//		params.put("bixfile", bixfile.getText());
	}
}
