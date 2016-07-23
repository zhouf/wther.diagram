package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;

public class SelectBixOKListener  implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params,
			Event event, String currentKey) {
		List show = (List) coms.get("show");// 列表框；
		
		java.util.List<String> flagPath=(java.util.List<String>) params.get("flagPath");		
		if (show.getSelectionCount() > 0) {
			// 则表示有选
			String path=flagPath.get(show.getSelectionIndex());//从fLagPath中找到所选编号所对应的路径；	
			params.put("showText", path);
		} else {
			params.put("showText", "");
		}
	}
}