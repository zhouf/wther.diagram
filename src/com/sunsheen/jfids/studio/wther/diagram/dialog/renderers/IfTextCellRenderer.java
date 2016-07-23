package com.sunsheen.jfids.studio.wther.diagram.dialog.renderers;


import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.renderers.TextCellRenderer;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;

/**
 * 这是对输入单元格的渲染，包含出错提示
 * 
 * @author zhoufeng Date:2013-6-7
 */
public class IfTextCellRenderer extends TextCellRenderer {

	public static Image DRAW_IMG = LogicDiagramEditorPlugin
			.getImage("icons/obj16/exclamation.png");

	public IfTextCellRenderer(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
	}

	public IfTextCellRenderer(AgileGrid agileGrid) {
		super(agileGrid);
	}

	@Override
	protected void doDrawCellContent(GC gc, Rectangle rect, int row, int col) {
		Object obj = agileGrid.getContentAt(row, col);

		if (obj != null) {
			int alignment = getAlignment();
			String inputStr = obj.toString();
			boolean checkOk;
			checkOk = (inputStr != null && inputStr.length() > 0) ? true : false;

			if (checkOk) {
				gc.drawString(inputStr, rect.x + 3, rect.y + 0);
			} else {
				drawTextImage(gc, inputStr, alignment, DRAW_IMG, alignment, rect.x + 3, rect.y + 2, rect.width - 6, rect.height - 4);
			}
			this.drawCellBorder(gc, rect, row, col);
		}
		((SDialog) this.agileGrid.getParams().get("SDIALOG")).validate();
	}

	@Override
	protected Rectangle drawCellBorder(GC gc, Rectangle rect, int row, int col) {
		Color vBorderColor = COLOR_LINE_DARKGRAY;
		Color hBorderColor = COLOR_LINE_DARKGRAY;

		return drawDefaultCellLine(gc, rect, vBorderColor, hBorderColor);
	}

}
