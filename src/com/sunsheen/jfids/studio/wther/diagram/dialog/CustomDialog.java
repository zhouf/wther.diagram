package com.sunsheen.jfids.studio.wther.diagram.dialog;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;

public class CustomDialog extends DialogConfigParser {
	public CustomDialog(Map<String, Object> params) {
		super(CustomDialog.class.getResourceAsStream("custom.xml"), new Shell(), params);
	}
}
