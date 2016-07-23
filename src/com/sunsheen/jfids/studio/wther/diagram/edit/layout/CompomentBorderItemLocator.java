package com.sunsheen.jfids.studio.wther.diagram.edit.layout;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;

/**
 * 控制每个activity下Label的位置,永远居中,永远在south。<br><br>
 * 
 * 重写了GMF中BorderItemLocator中的relocate的方法。
 * 
 * @author Len
 * @since 2012-1-2
 * 
 */
public class CompomentBorderItemLocator extends BorderItemLocator {
	public CompomentBorderItemLocator(IFigure parentFigure, int preferredSide) {
		super(parentFigure, preferredSide);
	}

	@Override
	public void relocate(IFigure borderItem) {
		Dimension size = getSize(borderItem);
		Rectangle parentRectangle = getParentBorder();
		int diff = parentRectangle.width - size.width;
		Point ptNewLocation = new Point(parentRectangle.x + diff / 2,
				parentRectangle.y + parentRectangle.height
						+ getBorderItemOffset().height);
		borderItem.setBounds(new Rectangle(ptNewLocation, size));
	}
}
