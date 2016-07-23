package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Map;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;


public class FlowDlgListener implements SDialogOnClickListener{
	@Override
	public void run(Map<String, Widget> coms,Map<String,Object> params,Event e,String currControl) {
		//Text flowName=(Text)coms.get("flowname");
		Text author=(Text)coms.get("author");
		Text createTime = (Text)coms.get("createtime");
		Text comment = (Text)coms.get("comment");
		Text retstr = (Text)coms.get("retstr");
		//params.put("flowname", flowName.getText());
		params.put("author", author.getText());
		params.put("createtime", createTime.getText());
		params.put("comment", comment.getText());

		AgileGrid defTable=(AgileGrid)coms.get("defTable");	
		InnerTableContentProvider defProvider=(InnerTableContentProvider) defTable.getContentProvider();
		params.put("defTable", defProvider.data);
		
		AgileGrid argTable=(AgileGrid)coms.get("argTable");	
		InnerTableContentProvider argProvider=(InnerTableContentProvider) argTable.getContentProvider();
		params.put("argTable", argProvider.data);

//		AgileGrid glbTable=(AgileGrid)coms.get("glbTable");	
//		InnerTableContentProvider glbProvider=(InnerTableContentProvider) glbTable.getContentProvider();
//		params.put("glbTable", glbProvider.data);
		
		Button canbeinvoked = (Button)coms.get("canbeinvoked");
		params.put("canbeinvoked", canbeinvoked.getSelection());
	}
}
