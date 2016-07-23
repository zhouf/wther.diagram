package com.sunsheen.jfids.studio.wther.diagram.dialog.popup.listener;

import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;
/**
 * 当选中列表项条目时，在showText控件中显示选中的值，showText为返回时获取值控件
 * @author zhoufeng
 * Date:2013-6-15
 */
public class OnBeanSelected implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params, Event event, String currentKey) {
		List show = (List) coms.get("show");// 列表框；
		Label showText = (Label) coms.get("showText");

		int itemCount = show.getItemCount();
		int selectionIndex = show.getSelectionIndex();
		if (itemCount > selectionIndex && selectionIndex>=0) {
			String name = show.getItem(selectionIndex);
			if (!"".equals(name)) {
				showText.setText(name);
			} else {
				showText.setText("");
			}
		}
	}
}
