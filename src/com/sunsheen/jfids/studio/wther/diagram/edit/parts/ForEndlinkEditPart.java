package com.sunsheen.jfids.studio.wther.diagram.edit.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

import com.sunsheen.jfids.studio.editor.editors.common.SWTResourceManager;
import com.sunsheen.jfids.studio.wther.diagram.edit.policies.ForEndlinkItemSemanticEditPolicy;

/**
 * @generated
 */
public class ForEndlinkEditPart extends ConnectionNodeEditPart implements ITreeBranchEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 4003;

	/**
	 * @generated
	 */
	public ForEndlinkEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ForEndlinkItemSemanticEditPolicy());
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */

	protected Connection createConnectionFigure() {
		return new EndLinkFigure();
	}

	/**
	 * @generated
	 */
	public EndLinkFigure getPrimaryShape() {
		return (EndLinkFigure) getFigure();
	}

	/**
	 * @generated
	 */
	public class EndLinkFigure extends PolylineConnectionEx {

		/**
		 * @generated
		 */
		public EndLinkFigure() {
			this.setLineWidth(2);
			this.setLineStyle(Graphics.LINE_DASHDOT);
			this.setForegroundColor(THIS_FORE);
			setTargetDecoration(createTargetDecoration());
		}

		/**
		 * @generated
		 */
		private RotatableDecoration createTargetDecoration() {
			PolylineDecoration df = new PolylineDecoration();
			return df;
		}

	}

	/**
	 * @generated NOT
	 */
	static final Color THIS_FORE = SWTResourceManager.getColor(99, 161, 0);

}
