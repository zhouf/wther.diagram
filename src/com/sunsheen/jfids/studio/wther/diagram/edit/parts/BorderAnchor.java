package com.sunsheen.jfids.studio.wther.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.gef.ui.figures.SlidableAnchor;
/**
 * 这是一个处理节点锚点的类，根据相关节点位置计算连接锚点，限定于图形的上下左右四个点
 * @author zhouf
 */
public class BorderAnchor extends SlidableAnchor {

	public BorderAnchor(IFigure f) {
		super(f);
	}

	@Override
	public Point getLocation(Point reference) {
		Rectangle bounds = getOwner().getBounds();
		Dimension diff = reference.getDifference(bounds.getCenter());
		Point p;
		if (Math.abs(diff.width) < Math.abs(diff.height)) {
			// 上下锚点
			p = diff.height > 0 ? bounds.getBottom() : bounds.getTop();
		} else {
			// 左右锚点
			p = diff.width > 0 ? bounds.getRight() : bounds.getLeft();
		}

		PrecisionPoint pp = new PrecisionPoint(p);
		getOwner().translateToAbsolute(pp);
		return pp;
	}
}
