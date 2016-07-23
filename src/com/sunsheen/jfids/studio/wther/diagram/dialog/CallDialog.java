package com.sunsheen.jfids.studio.wther.diagram.dialog;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;

public class CallDialog extends DialogConfigParser {

	public CallDialog(Map<String, Object> params) {
		super(CallDialog.class.getResourceAsStream("call.xml"), new Shell(), params);
	}
}
