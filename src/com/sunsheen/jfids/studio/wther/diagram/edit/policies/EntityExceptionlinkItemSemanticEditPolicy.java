package com.sunsheen.jfids.studio.wther.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;

import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;

/**
 * @generated
 */
public class EntityExceptionlinkItemSemanticEditPolicy extends LogicBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public EntityExceptionlinkItemSemanticEditPolicy() {
		super(LogicElementTypes.EntityExceptionlink_4007);
	}

	/**
	 * @generated
	 */
	protected Command getDestroyReferenceCommand(DestroyReferenceRequest req) {
		return getGEFWrapper(new DestroyReferenceCommand(req));
	}

}
