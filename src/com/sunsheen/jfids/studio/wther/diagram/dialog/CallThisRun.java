package com.sunsheen.jfids.studio.wther.diagram.dialog;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;

public class CallThisRun extends DialogConfigParser {

	public CallThisRun(Map<String, Object> params) {
		super(CallThisRun.class.getResourceAsStream("thisrun.xml"), new Shell(), params);
	}
}
