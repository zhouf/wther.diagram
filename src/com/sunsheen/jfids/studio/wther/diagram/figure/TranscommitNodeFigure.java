package com.sunsheen.jfids.studio.wther.diagram.figure;


import org.eclipse.draw2d.Graphics;

import com.sunsheen.jfids.studio.logic.impl.NodeImpl;

public class TranscommitNodeFigure extends NodeFigure {
	public TranscommitNodeFigure(NodeImpl node) {
		super(node);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		setFigureImage(graphics, "icons/obj32/transcommit_32.png");
		super.paintFigure(graphics);
	}
}
