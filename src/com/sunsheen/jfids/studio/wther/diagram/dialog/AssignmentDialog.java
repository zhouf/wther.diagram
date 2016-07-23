package com.sunsheen.jfids.studio.wther.diagram.dialog;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;

public class AssignmentDialog extends DialogConfigParser {

	public AssignmentDialog(Map<String, Object> params) {
		super(AssignmentDialog.class.getResourceAsStream("assig.xml"), new Shell(), params);
	}
}
