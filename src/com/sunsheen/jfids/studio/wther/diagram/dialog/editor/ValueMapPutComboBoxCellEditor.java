package com.sunsheen.jfids.studio.wther.diagram.dialog.editor;

import org.eclipse.swt.SWT;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.editors.ComboBoxCellEditor;

/**
 * 值类型列表
 * @author zhoufeng
 * Date:2013-6-27
 */
public class ValueMapPutComboBoxCellEditor extends ComboBoxCellEditor {

	public ValueMapPutComboBoxCellEditor(AgileGrid agileGrid) {
		super(agileGrid,SWT.READ_ONLY);
		setItems(new String[]{"变量","常量","表达式"});
		super.select(0);
	}
	
	@Override
	protected void focusLost() {
		super.focusLost();
		((SDialog) agileGrid.getParams().get("SDIALOG")).validate();
	}
}
