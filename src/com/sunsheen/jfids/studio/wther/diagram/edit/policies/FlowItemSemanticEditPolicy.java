package com.sunsheen.jfids.studio.wther.diagram.edit.policies;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;

import com.sunsheen.jfids.studio.wther.diagram.edit.commands.AssignmentCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.BixrefCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.BlankCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.CallCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.CustomCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.EndCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.ForCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.IfCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.StartCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.TransactionCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.TranscommitCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.TransrollbackCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;

/**
 * @generated
 */
public class FlowItemSemanticEditPolicy extends LogicBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public FlowItemSemanticEditPolicy() {
		super(LogicElementTypes.Flow_1000);
	}

	/**
	 * @generated
	 */
	protected Command getCreateCommand(CreateElementRequest req) {
		if (LogicElementTypes.Start_2001 == req.getElementType()) {
			return getGEFWrapper(new StartCreateCommand(req));
		}
		if (LogicElementTypes.End_2002 == req.getElementType()) {
			return getGEFWrapper(new EndCreateCommand(req));
		}
		if (LogicElementTypes.Assignment_2003 == req.getElementType()) {
			return getGEFWrapper(new AssignmentCreateCommand(req));
		}
		if (LogicElementTypes.Custom_2011 == req.getElementType()) {
			return getGEFWrapper(new CustomCreateCommand(req));
		}
		if (LogicElementTypes.Blank_2007 == req.getElementType()) {
			return getGEFWrapper(new BlankCreateCommand(req));
		}
		if (LogicElementTypes.For_2004 == req.getElementType()) {
			return getGEFWrapper(new ForCreateCommand(req));
		}
		if (LogicElementTypes.If_2005 == req.getElementType()) {
			return getGEFWrapper(new IfCreateCommand(req));
		}
		if (LogicElementTypes.Call_2006 == req.getElementType()) {
			return getGEFWrapper(new CallCreateCommand(req));
		}
		if (LogicElementTypes.Bixref_2012 == req.getElementType()) {
			return getGEFWrapper(new BixrefCreateCommand(req));
		}
		if (LogicElementTypes.Transaction_2008 == req.getElementType()) {
			return getGEFWrapper(new TransactionCreateCommand(req));
		}
		if (LogicElementTypes.Transcommit_2009 == req.getElementType()) {
			return getGEFWrapper(new TranscommitCreateCommand(req));
		}
		if (LogicElementTypes.Transrollback_2010 == req.getElementType()) {
			return getGEFWrapper(new TransrollbackCreateCommand(req));
		}
		return super.getCreateCommand(req);
	}

	/**
	 * @generated
	 */
	protected Command getDuplicateCommand(DuplicateElementsRequest req) {
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
		return getGEFWrapper(new DuplicateAnythingCommand(editingDomain, req));
	}

	/**
	 * @generated
	 */
	private static class DuplicateAnythingCommand extends DuplicateEObjectsCommand {

		/**
		 * @generated
		 */
		public DuplicateAnythingCommand(TransactionalEditingDomain editingDomain, DuplicateElementsRequest req) {
			super(editingDomain, req.getLabel(), req.getElementsToBeDuplicated(), req.getAllDuplicatedElementsMap());
		}

	}

}
