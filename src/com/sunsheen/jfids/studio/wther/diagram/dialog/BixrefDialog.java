package com.sunsheen.jfids.studio.wther.diagram.dialog;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;

public class BixrefDialog extends DialogConfigParser {

	public BixrefDialog(Map<String, Object> params) {
		super(BixrefDialog.class.getResourceAsStream("bixref.xml"), new Shell(), params);
	}
}
