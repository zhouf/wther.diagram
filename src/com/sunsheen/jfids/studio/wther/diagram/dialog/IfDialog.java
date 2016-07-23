package com.sunsheen.jfids.studio.wther.diagram.dialog;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;

public class IfDialog extends DialogConfigParser {
	public IfDialog(Map<String, Object> params) {
		super(IfDialog.class.getResourceAsStream("branch.xml"), new Shell(), params);
	}
}
