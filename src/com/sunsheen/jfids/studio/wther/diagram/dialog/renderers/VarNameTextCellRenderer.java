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
 * 这是对输入变量名单元格的渲染，如果变量命名不符合规范，则给出提示图标
 * 
 * @author zhoufeng Date:2013-6-7
 */
public class VarNameTextCellRenderer extends TextCellRenderer {

	public static Image DRAW_IMG = LogicDiagramEditorPlugin.getImage("icons/obj16/exclamation.png");

	public VarNameTextCellRenderer(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
	}

	public VarNameTextCellRenderer(AgileGrid agileGrid) {
		super(agileGrid);
	}

	@Override
	protected void doDrawCellContent(GC gc, Rectangle rect, int row, int col) {
		Object obj = agileGrid.getContentAt(row, col);
		if (obj != null) {
			int alignment = getAlignment();
			boolean checkOk = true;
			String inputStr = obj.toString();
			
			if (inputStr == null || "".equals(inputStr)) {
				// 变量名为空
				checkOk = false;
			}else if (!InputDataValidate.checkValidSpell(inputStr)) {
				// 变量拼写不合法
				checkOk = false;
			}else if(!InputDataValidate.checkKeyWords(inputStr)){
				//变量不能为关键字
				checkOk = false;
			}
			
			
			if (checkOk) {
				gc.drawString(inputStr, rect.x + 3, rect.y + 0);
			} else {
				drawTextImage(gc, inputStr, alignment, DRAW_IMG, alignment, rect.x + 3, rect.y + 2, rect.width - 6, rect.height - 4);
			}

			this.drawCellBorder(gc, rect, row, col);
		}
	}

	@Override
	protected Rectangle drawCellBorder(GC gc, Rectangle rect, int row, int col) {
		Color vBorderColor = COLOR_LINE_DARKGRAY;
		Color hBorderColor = COLOR_LINE_DARKGRAY;

		return drawDefaultCellLine(gc, rect, vBorderColor, hBorderColor);
	}

}
