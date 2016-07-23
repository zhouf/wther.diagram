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
 * 用于页面对话框中参数返回表格中的图标前缀和变量名验证
 * @author zhoufeng
 */
public class FlowTextCellRenderer extends TextCellRenderer {

	public static Image DRAW_IMG = LogicDiagramEditorPlugin.getImage("icons/light.png");
	public static final Image VAR_IMG = LogicDiagramEditorPlugin.getImage("icons/var.gif");
	public static final Image RET_IMG = LogicDiagramEditorPlugin.getImage("icons/ret.gif");
	public static final Image VARITEM_IMG = LogicDiagramEditorPlugin.getImage("icons/varitem.gif");
	public static final Image RETITEM_IMG = LogicDiagramEditorPlugin.getImage("icons/retitem.gif");
	public static final Image ERR_IMG = LogicDiagramEditorPlugin.getImage("icons/obj16/exclamation.png");
	
	public FlowTextCellRenderer(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
	}

	public FlowTextCellRenderer(AgileGrid agileGrid) {
		super(agileGrid);
	}
	
	@Override
	protected void doDrawCellContent(GC gc, Rectangle rect, int row, int col) {
		Object obj = agileGrid.getContentAt(row, col);
		if(obj!=null){
			int alignment = getAlignment();
			if(col==0){
				//第一列
				if("参数".equalsIgnoreCase(obj.toString())){
					DRAW_IMG = VAR_IMG;
				}else{
					DRAW_IMG = RET_IMG;
				}
			}else{
				//画第二列
				//验证命名合法
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
				if(checkOk){
					//变量名验证通过
					Object leftObj = agileGrid.getContentAt(row, col-1);
					if(leftObj!=null && "参数".equalsIgnoreCase(leftObj.toString())){
						DRAW_IMG = VARITEM_IMG;
					}else{
						DRAW_IMG = RETITEM_IMG;
					}
				}else{
					DRAW_IMG = ERR_IMG;
				}
			}
			drawTextImage(gc, obj.toString(), alignment, DRAW_IMG, alignment,rect.x + 3, rect.y + 2, rect.width - 6, rect.height - 4);
//			gc.drawImage(IMAGE_CHECKED, rect.x+10, rect.y);
//			gc.drawString("Hello", rect.x+30, rect.y+0);
			this.drawCellBorder(gc, rect, row, col);
//			super.doDrawCellContent(gc, rect, row, col);
		}
	}
	
	@Override
	protected Rectangle drawCellBorder(GC gc, Rectangle rect, int row, int col) {
		Color vBorderColor = COLOR_LINE_DARKGRAY;
		Color hBorderColor = COLOR_LINE_DARKGRAY;

		return drawDefaultCellLine(gc, rect, vBorderColor, hBorderColor);
	}
	

}
