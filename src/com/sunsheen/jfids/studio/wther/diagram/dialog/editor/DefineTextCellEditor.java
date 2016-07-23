package com.sunsheen.jfids.studio.wther.diagram.dialog.editor;

import org.eclipse.swt.SWT;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.Cell;
import com.sunsheen.jfids.studio.dialog.agilegrid.editors.TextCellEditor;

/**
 * 这是一个用于页面定义变量时的输入编辑器，没有基本的提示设置
 * 
 * @author zhouf
 */
public class DefineTextCellEditor extends TextCellEditor {


	public DefineTextCellEditor(AgileGrid agileGrid) {
		this(agileGrid, SWT.SINGLE);
	}

	public DefineTextCellEditor(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
	}
	
	@Override
	protected boolean dependsOnExternalFocusListener() {
		return false;
	}

	@Override
	protected void focusLost() {
		Cell focusCell = agileGrid.getFocusCell();
		agileGrid.setContentAt(focusCell.row, focusCell.column, text.getText());
		agileGrid.applyEditorValue();
		((SDialog) agileGrid.getParams().get("SDIALOG")).validate();
		super.focusLost();
	}
}
