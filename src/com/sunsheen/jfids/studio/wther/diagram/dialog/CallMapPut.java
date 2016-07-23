package com.sunsheen.jfids.studio.wther.diagram.dialog;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;

public class CallMapPut extends DialogConfigParser {

	public CallMapPut(Map<String, Object> params) {
		super(CallMapPut.class.getResourceAsStream("mapput.xml"), new Shell(), params);
	}
}
