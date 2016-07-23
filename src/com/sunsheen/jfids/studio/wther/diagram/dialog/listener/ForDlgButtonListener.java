package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;

public class ForDlgButtonListener implements SDialogOnClickListener {

	@Override
	@SuppressWarnings("unchecked")
	public void run(Map<String, Widget> coms, Map<String, Object> params,Event e, String currControlKey) {

		if("simpleRadio".equals(currControlKey)){
			controlEnable(coms, new String[]{"textCode","forBtn","whileBtn","dowhileBtn"}, false);
			controlEnable(coms, new String[]{"simpleVar","simpleTimes"}, true);
		}else if("ordinaryRadio".equals(currControlKey)){
			controlEnable(coms, new String[]{"simpleVar","simpleTimes"}, false);
			controlEnable(coms, new String[]{"textCode","forBtn","whileBtn","dowhileBtn"}, true);
		}else if("forBtn".equalsIgnoreCase(currControlKey)){
			Text text = (Text)coms.get("textCode");
			String vari = getAvailableVari((Set<String>)params.get("definedVarSet"));
			text.setText("for(int "+vari+"=0; "+vari+"<limit; "+vari+"++){\n\n}");
		}else if("whileBtn".equalsIgnoreCase(currControlKey)){
			Text text = (Text)coms.get("textCode");
			text.setText("while(){\n\n}");
		}else if("dowhileBtn".equalsIgnoreCase(currControlKey)){
			Text text = (Text)coms.get("textCode");
			text.setText("do{\n\n}while();");
		}
	}
	
	/**
	 * 获得一个可用的循环变量
	 * @param object
	 * @return
	 */
	private String getAvailableVari(Set<String> definedVarSet) {
		String retVal = "",prefix = "i";
		retVal = prefix;
		int suffixIndex = 1;
		while(definedVarSet.contains(retVal)){
			//如果已经包含了当前变量，则找新的
			retVal = prefix + (suffixIndex++);
		}
		return retVal;
	}

	/**
	 * @param coms 窗口各表单组件
	 * @param args 要设置是否启用的表单元素key值
	 * @param isEnable true-启用/fasle-不启用
	 */
	public static void controlEnable(Map<String, Widget> coms,String[] keys,Boolean isEnable){
		Widget widget;
		for(String key:keys){
			widget = coms.get(key);
			if(widget instanceof Control ){
				((Control)widget).setEnabled(isEnable);
			}
			if(widget instanceof ToolItem){
				((ToolItem)widget).setEnabled(isEnable);
			}
		}
	}

}
