package com.sunsheen.jfids.studio.wther.diagram.edit.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

import com.sunsheen.jfids.studio.editor.editors.common.SWTResourceManager;
import com.sunsheen.jfids.studio.wther.diagram.edit.policies.IfLinkItemSemanticEditPolicy;

/**
 * @generated
 */
public class IfLinkEditPart extends ConnectionNodeEditPart implements ITreeBranchEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 4004;

	/**
	 * @generated
	 */
	public IfLinkEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new IfLinkItemSemanticEditPolicy());
	}

	/**
	 * @generated
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof IfLinkTipEditPart) {
			((IfLinkTipEditPart) childEditPart).setLabel(getPrimaryShape().getFigureIfLinkNameFigure());
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (addFixedChild(childEditPart)) {
			return;
		}
		super.addChildVisual(childEditPart, index);
	}

	/**
	 * @generated
	 */
	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof IfLinkTipEditPart) {
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected void removeChildVisual(EditPart childEditPart) {
		if (removeFixedChild(childEditPart)) {
			return;
		}
		super.removeChildVisual(childEditPart);
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model so
	 * you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */

	protected Connection createConnectionFigure() {
		return new IfLinkFigure();
	}

	/**
	 * @generated
	 */
	public IfLinkFigure getPrimaryShape() {
		return (IfLinkFigure) getFigure();
	}

	/**
	 * @generated
	 */
	public class IfLinkFigure extends PolylineConnectionEx {

		/**
		 * @generated
		 */
		private WrappingLabel fFigureIfLinkNameFigure;

		/**
		 * @generated
		 */
		public IfLinkFigure() {
			this.setForegroundColor(THIS_FORE);

			createContents();
			setTargetDecoration(createTargetDecoration());
		}

		/**
		 * @generated
		 */
		private void createContents() {

			fFigureIfLinkNameFigure = new WrappingLabel();
			fFigureIfLinkNameFigure.setText("<...>");

			this.add(fFigureIfLinkNameFigure);

		}

		/**
		 * @generated
		 */
		private RotatableDecoration createTargetDecoration() {
			PolylineDecoration df = new PolylineDecoration();
			return df;
		}

		/**
		 * @generated
		 */
		public WrappingLabel getFigureIfLinkNameFigure() {
			return fFigureIfLinkNameFigure;
		}

	}

	/**
	 * @generated NOT
	 */
	static final Color THIS_FORE = SWTResourceManager.getColor(13, 115, 231);

	@Override
	public void performRequest(Request request) {
		// 禁止创建时的编辑，为防止和快捷键冲突
		// super.performRequest(request);
	}
}
