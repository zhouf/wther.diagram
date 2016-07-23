package com.sunsheen.jfids.studio.wther.diagram.dialog.renderers;


import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.renderers.TextCellRenderer;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;

public class AssignmentVarNameRenderer extends TextCellRenderer {

	public AssignmentVarNameRenderer(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
	}
	public AssignmentVarNameRenderer(AgileGrid agileGrid) {
		super(agileGrid);
	}

	@Override
	protected void doDrawCellContent(GC gc, Rectangle rect, int row, int col) {
		Object obj = agileGrid.getContentAt(row, col);
		if (obj != null) {
			String varName = obj.toString();
			String type = findVarType(varName);
			String text = varName;
			if(type!=null && !type.isEmpty()){
				agileGrid.setContentAt(row, col-1, type);
			}
			gc.drawString(text, rect.x + 3, rect.y + 0);
		}
		
		this.drawCellBorder(gc, rect, row, col);
	}
	
	/**
	 * 查找变量的类型，用于显示在赋值对话框变量名列
	 * @param varName 如：var1,var2/id
	 * @return String
	 */
	private String findVarType(String varName) {
		//将变量名截取/左边的字串
		int ind = varName.indexOf("/");
		if(ind>0){
			varName = varName.substring(0, ind);
		}
		VarStore varStore = (VarStore)this.getParams().get("varstore");
		return varStore.findVarType(varName);
	}
	
	@Override
	protected Rectangle drawCellBorder(GC gc, Rectangle rect, int row, int col) {
		Color vBorderColor = COLOR_LINE_DARKGRAY;
		Color hBorderColor = COLOR_LINE_DARKGRAY;

		return drawDefaultCellLine(gc, rect, vBorderColor, hBorderColor);
	}

}
