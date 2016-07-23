package com.sunsheen.jfids.studio.wther.diagram.dialog.renderers;


import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.renderers.TextCellRenderer;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
/**
 * 用于调用和构件和逻辑流时对话框中的表格图标前缀和数据验证 
 * @author zhoufeng
 */
public class TypeTextCellRenderer extends TextCellRenderer {

	public static Image DRAW_IMG = LogicDiagramEditorPlugin.getImage("icons/light.png");
	public static final Image VAR_IMG = LogicDiagramEditorPlugin.getImage("icons/var.gif");
	public static final Image RET_IMG = LogicDiagramEditorPlugin.getImage("icons/ret.gif");
	public static final Image VARITEM_IMG = LogicDiagramEditorPlugin.getImage("icons/varitem.gif");
	public static final Image RETITEM_IMG = LogicDiagramEditorPlugin.getImage("icons/retitem.gif");
	public static final Image ERR_IMG = LogicDiagramEditorPlugin.getImage("icons/obj16/exclamation.png");
	
	public TypeTextCellRenderer(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
	}

	public TypeTextCellRenderer(AgileGrid agileGrid) {
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
			}else if(col==1){
				//画第二列，数据类型
				//验证命名合法
				Object leftObj = agileGrid.getContentAt(row, col-1);
				if(leftObj!=null && "参数".equalsIgnoreCase(leftObj.toString())){
					DRAW_IMG = VARITEM_IMG;
				}else{
					DRAW_IMG = RETITEM_IMG;
				}
				
			}else{
				//第三列，数据名
//				boolean checkOk = true;
//				String inputStr = obj.toString();
//				if (inputStr == null || "".equals(inputStr)) {
//					// 变量名为空
//					checkOk = false;
//				}else if (!InputDataValidate.checkValidSpell(inputStr)) {
//					// 变量拼写不合法
//					checkOk = false;
//				}else if(!InputDataValidate.checkKeyWords(inputStr)){
//					//变量不能为关键字
//					checkOk = false;
//				}
//				if(checkOk){
//					//变量名验证通过
//					Object leftObj = agileGrid.getContentAt(row, col-2);
//					if(leftObj!=null && "参数".equalsIgnoreCase(leftObj.toString())){
//						DRAW_IMG = VARITEM_IMG;
//					}else{
//						DRAW_IMG = RETITEM_IMG;
//					}
//				}else{
//					DRAW_IMG = ERR_IMG;
//				}
			}
			String displayText = obj.toString();
			//修改显示，如java.util.Map 显示为 Map
			if(displayText.contains(".")){
				displayText = displayText.substring(displayText.lastIndexOf(".")+1);
			}
			drawTextImage(gc, displayText, alignment, DRAW_IMG, alignment,rect.x + 3, rect.y + 2, rect.width - 6, rect.height - 4);
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
