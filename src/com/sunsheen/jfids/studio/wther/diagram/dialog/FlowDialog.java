package com.sunsheen.jfids.studio.wther.diagram.dialog;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;

public class FlowDialog extends DialogConfigParser {

	public FlowDialog(Map<String, Object> params) {
		super(FlowDialog.class.getResourceAsStream("flow.xml"), new Shell(), params);
	}

}
