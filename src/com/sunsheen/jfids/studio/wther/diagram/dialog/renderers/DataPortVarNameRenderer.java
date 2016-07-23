package com.sunsheen.jfids.studio.wther.diagram.dialog.renderers;


import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.renderers.TextCellRenderer;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;
/**
 * 这是一个处理this.var.run对话框的一个Render,当选择一个变量后，获取其变量类型并显示到前一列
 * @author zhouf
 */
public class DataPortVarNameRenderer extends TextCellRenderer {

	public DataPortVarNameRenderer(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
	}
	public DataPortVarNameRenderer(AgileGrid agileGrid) {
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
		String varType = varStore.findVarType(varName);
		//转换为完整类名
		System.out.println("ThisRunVarNameRenderer.findVarType()->varType:" + varType);
		if(JavaTypeUtil.containsKey(varType)){
			varType = JavaTypeUtil.convertShortToLongType(varType);
		}
		return varType;
	}
	
	@Override
	protected Rectangle drawCellBorder(GC gc, Rectangle rect, int row, int col) {
		Color vBorderColor = COLOR_LINE_DARKGRAY;
		Color hBorderColor = COLOR_LINE_DARKGRAY;

		return drawDefaultCellLine(gc, rect, vBorderColor, hBorderColor);
	}

}
