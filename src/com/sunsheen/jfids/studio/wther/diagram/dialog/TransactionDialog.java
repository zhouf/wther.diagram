package com.sunsheen.jfids.studio.wther.diagram.dialog;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;

public class TransactionDialog extends DialogConfigParser {

	public TransactionDialog(Map<String, Object> params) {
		super(TransactionDialog.class.getResourceAsStream("transaction.xml"), new Shell(), params);
	}
}
