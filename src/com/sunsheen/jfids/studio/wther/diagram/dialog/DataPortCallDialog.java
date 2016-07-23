package com.sunsheen.jfids.studio.wther.diagram.dialog;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;

public class DataPortCallDialog extends DialogConfigParser {

	public DataPortCallDialog(Map<String, Object> params) {
		super(DataPortCallDialog.class.getResourceAsStream("dataportcall.xml"), new Shell(), params);
	}
}
