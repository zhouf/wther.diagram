package com.sunsheen.jfids.studio.wther.diagram.dialog.renderers;


import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
/**
 * 这是一个处理this.var.run对话框的一个Render,当选择一个变量后，获取其变量类型并显示到前一列
 * @author zhouf
 */
public class ThisRunVarNameRenderer extends DataPortVarNameRenderer {

	public ThisRunVarNameRenderer(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
	}
	public ThisRunVarNameRenderer(AgileGrid agileGrid) {
		super(agileGrid);
	}

}
