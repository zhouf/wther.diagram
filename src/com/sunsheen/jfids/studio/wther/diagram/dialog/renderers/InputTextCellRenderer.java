package com.sunsheen.jfids.studio.wther.diagram.dialog.renderers;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.renderers.TextCellRenderer;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.InputDataValidate;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;

/**
 * 这是赋值节点对变量值输入单元格的渲染，包含出错提示
 * 
 * @author zhoufeng Date:2013-6-7
 */
public class InputTextCellRenderer extends TextCellRenderer {

	public static Image DRAW_IMG = LogicDiagramEditorPlugin.getImage("icons/obj16/exclamation.png");

	public InputTextCellRenderer(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
	}

	public InputTextCellRenderer(AgileGrid agileGrid) {
		super(agileGrid);
	}

	@Override
	protected void doDrawCellContent(GC gc, Rectangle rect, int row, int col) {
		Object obj = agileGrid.getContentAt(row, col);
		Object rightObj = agileGrid.getContentAt(row, col + 1);
		if (obj != null && rightObj != null) {
			int alignment = getAlignment();
			boolean checkOk = true;
			String inputStr = obj.toString();
			if ("Date".equalsIgnoreCase(rightObj.toString())) {
				checkOk = InputDataValidate.checkDateStrValidate(inputStr);
			} else if ("Integer".equalsIgnoreCase(rightObj.toString())) {
				// FIXME 其它数据类型
				checkOk = InputDataValidate.checkNumStrValidate(inputStr);
			}
			if (checkOk) {
				gc.drawString(inputStr, rect.x + 3, rect.y + 0);
			} else {
				drawTextImage(gc, inputStr, alignment, DRAW_IMG, alignment, rect.x + 3, rect.y + 2, rect.width - 6, rect.height - 4);
			}

			// ((SDialog) this.agileGrid.getParams().get("SDIALOG")).validate();
			// gc.drawImage(IMAGE_CHECKED, rect.x+10, rect.y);
			// gc.drawString("Hello", rect.x+30, rect.y+0);
			this.drawCellBorder(gc, rect, row, col);
			// super.doDrawCellContent(gc, rect, row, col);
		}
	}

	@Override
	protected Rectangle drawCellBorder(GC gc, Rectangle rect, int row, int col) {
		Color vBorderColor = COLOR_LINE_DARKGRAY;
		Color hBorderColor = COLOR_LINE_DARKGRAY;

		return drawDefaultCellLine(gc, rect, vBorderColor, hBorderColor);
	}

}
