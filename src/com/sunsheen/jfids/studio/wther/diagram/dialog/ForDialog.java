package com.sunsheen.jfids.studio.wther.diagram.dialog;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;

public class ForDialog extends DialogConfigParser {
	public ForDialog(Map<String, Object> params) {
		super(ForDialog.class.getResourceAsStream("for.xml"), new Shell(), params);
	}
}
