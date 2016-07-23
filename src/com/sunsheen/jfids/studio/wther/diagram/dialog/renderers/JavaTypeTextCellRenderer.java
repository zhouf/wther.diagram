package com.sunsheen.jfids.studio.wther.diagram.dialog.renderers;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.renderers.TextCellRenderer;

/**
 * 这是一个用于显示JAVA类型的一个Render,将完整的类名如java.lang.String显示为String
 * @author zhoufeng Date:2013-7-7
 */
public class JavaTypeTextCellRenderer extends TextCellRenderer {

	public JavaTypeTextCellRenderer(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
	}

	public JavaTypeTextCellRenderer(AgileGrid agileGrid) {
		super(agileGrid);
	}

	@Override
	protected void doDrawCellContent(GC gc, Rectangle rect, int row, int col) {
		Object obj = agileGrid.getContentAt(row, col);

		if (obj != null) {
			//int alignment = getAlignment();
			String inputStr = obj.toString();
			String items[] = inputStr.split("\\.");
			if(items.length>=2){
				inputStr = items[items.length-1];
			}
			gc.drawString(inputStr, rect.x + 3, rect.y + 0);
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
