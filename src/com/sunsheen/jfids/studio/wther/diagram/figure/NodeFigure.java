package com.sunsheen.jfids.studio.wther.diagram.figure;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.graphics.Image;

import com.sunsheen.jfids.studio.logic.impl.NodeImpl;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;

public abstract class NodeFigure extends Figure {

	NodeImpl node;
	boolean isDebug = false;

	boolean isDebugRun = false;

	boolean isTry = false;

	public NodeFigure(NodeImpl node) {
		this.node = node;
		setTry(node.isStarttry());
		setDebug(node.isDebug());
	}

	protected void setFigureImage(Graphics graphics, String icon) {
		Image image = LogicDiagramEditorPlugin.getImage(icon);
		graphics.drawImage(image, getLocation());
		if (isDebug) {
			Image debugimage = LogicDiagramEditorPlugin.getImage("icons/debug.png");
			graphics.drawImage(debugimage, getLocation());
		}

		if (isDebugRun) {
			Image debugrunimage = LogicDiagramEditorPlugin.getImage("icons/debug_run.png");
			graphics.drawImage(debugrunimage, getLocation());
		}

		if (isTry) {
			Image debugrunimage = LogicDiagramEditorPlugin.getImage("icons/obj16/try_16.png");
			graphics.drawImage(debugrunimage, getLocation().getTranslated(16, 16));
		}

		setPreferredSize(32, 32);
	}

	public void setDebug(boolean isdebug) {
		this.isDebug = isdebug;
		this.repaint();
	}

	public void setDebugRun(boolean isdebugrun) {
		this.isDebugRun = isdebugrun;
		this.repaint();
	}

	public void setTry(boolean isTry) {
		this.isTry = isTry;
		this.repaint();
	}

}
