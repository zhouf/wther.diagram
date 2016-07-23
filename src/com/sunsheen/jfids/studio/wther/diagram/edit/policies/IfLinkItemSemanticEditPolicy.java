package com.sunsheen.jfids.studio.wther.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;

/**
 * @generated
 */
public class IfLinkItemSemanticEditPolicy extends LogicBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public IfLinkItemSemanticEditPolicy() {
		super(LogicElementTypes.IfLink_4004);
	}

	/**
	 * @generated
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		return getGEFWrapper(new DestroyElementCommand(req));
	}

}
