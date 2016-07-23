package com.sunsheen.jfids.studio.wther.diagram.edit.tip;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.gef.EditDomain;

import com.sunsheen.jfids.studio.logic.impl.EntityImpl;


public class ToolTipForActivityListener implements MouseMotionListener {
	public static Boolean showTipAble = false;
    ToolTipForActivityRender data = null;
    
	public ToolTipForActivityListener(EntityImpl activity, EditDomain domain) {
		data = new ToolTipForActivityRender(activity,domain);
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent me) {
		if (!showTipAble&&data!=null) {
		    data.renderTipTool(null);
		}
	}

	@Override
	public void mouseExited(MouseEvent me) {
		if(data!=null&&!showTipAble){
			ToolTipForActivityRender.stopTip();
		}
	}

	@Override
	public void mouseHover(MouseEvent me) {

	}

	@Override
	public void mouseMoved(MouseEvent me) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 提供给外部调用，关闭tip提示。
	 */
	public static  void stopTip(){
		 if(!showTipAble)
		 ToolTipForActivityRender.stopTip();
		 showTipAble = false;
	} 

	
	/**
	 * 提供给外部调用，显示tip提示。
	 */
	public static void showTip(){
		 if(ToolTipForActivityRender.getTip()!=null){
			 showTipAble = true;
		 }
		 ToolTipForActivityRender.showTip();
		//renderTipTool(new Shell(display, SWT.ON_TOP  | SWT.TOOL| SWT.RESIZE));
	}

}
