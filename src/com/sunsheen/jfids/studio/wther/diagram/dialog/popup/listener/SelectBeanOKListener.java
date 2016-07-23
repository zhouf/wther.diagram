package com.sunsheen.jfids.studio.wther.diagram.dialog.popup.listener;

import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;
/**
 * 点击确定按钮时执行的操作
 * @author zhoufeng
 * Date:2013-6-15
 */
public class SelectBeanOKListener  implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params,
			Event event, String currentKey) {
		Label showText=(Label)coms.get("showText");
		//由于改显示值，因此就不再再传showText的值，要传所选值；
		List show=(List)coms.get("show");//列表框；
		if(show.getSelectionCount()>0){//则表示有选
			params.put("showText",showText.getText());
		}else{
			params.put("showText","");
		}		
	}
}