package com.sunsheen.jfids.studio.wther.diagram.dialog.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.dialogs.OpenTypeSelectionDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.PlatformUI;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.Cell;
import com.sunsheen.jfids.studio.dialog.agilegrid.editors.ComboBoxCellEditor;
import com.sunsheen.jfids.studio.wther.diagram.compiler.JavaType;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.Constant;

/**
 * 变量类型列表
 * @author zhoufeng
 * Date:2013-6-30
 */
public class VarTypeComboBoxCellEditor extends ComboBoxCellEditor {

	public VarTypeComboBoxCellEditor(AgileGrid agileGrid) {
		super(agileGrid,SWT.READ_ONLY);
		ArrayList<String> itemList = new ArrayList<String>();
		
		Cell cell = agileGrid.getFocusCell();
		String oldVal = (String) agileGrid.getContentAt(cell.row, cell.column);
		
		itemList.add(Constant.TYPE_JAVA_BROWSE);
		List<String> javaTypeList = JavaType.toArray();
		itemList.addAll(javaTypeList);
		if(oldVal!=null && !javaTypeList.contains(oldVal)){
			itemList.add(oldVal);
		}
		
		setItems(itemList.toArray(new String[]{}));
		super.select(0);
		combo.setVisibleItemCount(13);
		combo.addSelectionListener(new SelectionListener(){

			@SuppressWarnings("restriction")
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = VarTypeComboBoxCellEditor.this.combo.getSelectionIndex();
				if(selectionIndex ==0){
					//如果选择了第一项，即Java浏览，则弹出类型选择框
					OpenTypeSelectionDialog dialog = new OpenTypeSelectionDialog(
							null,false,PlatformUI.getWorkbench().getProgressService(),
							SearchEngine.createWorkspaceScope(),IJavaSearchConstants.TYPE);
					dialog.setTitle("类型选择");
					dialog.setMessage("请查找相应的Java类型");
					int result = dialog.open();
					IType type = (IType) dialog.getFirstResult();
					if(type!=null){
						//String className = type.getTypeQualifiedName();
						String fullClassName = type.getFullyQualifiedName();
						int row = VarTypeComboBoxCellEditor.this.cell.row;
						int col = VarTypeComboBoxCellEditor.this.cell.column;
						VarTypeComboBoxCellEditor.this.agileGrid.setContentAt(row, col, fullClassName);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
	}
	
	@Override
	protected void focusLost() {
		agileGrid.applyEditorValue();
		super.focusLost();
		((SDialog) agileGrid.getParams().get("SDIALOG")).validate();
	}
}
