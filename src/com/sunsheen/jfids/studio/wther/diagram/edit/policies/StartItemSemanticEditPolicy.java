package com.sunsheen.jfids.studio.wther.diagram.edit.policies;

import java.util.Iterator;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

import com.sunsheen.jfids.studio.wther.diagram.edit.commands.IfElselinkCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.IfElselinkReorientCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.IfLinkCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.IfLinkReorientCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.NodeLinkCreateCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.NodeLinkReorientCommand;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfElselinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfLinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.NodeLinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;

/**
 * @generated
 */
public class StartItemSemanticEditPolicy extends LogicBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public StartItemSemanticEditPolicy() {
		super(LogicElementTypes.Start_2001);
	}

	/**
	 * @generated
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		View view = (View) getHost().getModel();
		CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(getEditingDomain(), null);
		cmd.setTransactionNestingEnabled(false);
		for (Iterator<?> it = view.getTargetEdges().iterator(); it.hasNext();) {
			Edge incomingLink = (Edge) it.next();
			if (LogicVisualIDRegistry.getVisualID(incomingLink) == NodeLinkEditPart.VISUAL_ID) {
				DestroyReferenceRequest r = new DestroyReferenceRequest(incomingLink.getSource().getElement(), null,
						incomingLink.getTarget().getElement(), false);
				cmd.add(new DestroyReferenceCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
				continue;
			}
			if (LogicVisualIDRegistry.getVisualID(incomingLink) == IfElselinkEditPart.VISUAL_ID) {
				DestroyReferenceRequest r = new DestroyReferenceRequest(incomingLink.getSource().getElement(), null,
						incomingLink.getTarget().getElement(), false);
				cmd.add(new DestroyReferenceCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
				continue;
			}
			if (LogicVisualIDRegistry.getVisualID(incomingLink) == IfLinkEditPart.VISUAL_ID) {
				DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(), false);
				cmd.add(new DestroyElementCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
				continue;
			}
		}
		for (Iterator<?> it = view.getSourceEdges().iterator(); it.hasNext();) {
			Edge outgoingLink = (Edge) it.next();
			if (LogicVisualIDRegistry.getVisualID(outgoingLink) == NodeLinkEditPart.VISUAL_ID) {
				DestroyReferenceRequest r = new DestroyReferenceRequest(outgoingLink.getSource().getElement(), null,
						outgoingLink.getTarget().getElement(), false);
				cmd.add(new DestroyReferenceCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
				continue;
			}
		}
		EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
		if (annotation == null) {
			// there are indirectly referenced children, need extra commands: false
			addDestroyShortcutsCommand(cmd, view);
			// delete host element
			cmd.add(new DestroyElementCommand(req));
		} else {
			cmd.add(new DeleteCommand(getEditingDomain(), view));
		}
		return getGEFWrapper(cmd.reduce());
	}

	/**
	 * @generated
	 */
	protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
		Command command = req.getTarget() == null ? getStartCreateRelationshipCommand(req)
				: getCompleteCreateRelationshipCommand(req);
		return command != null ? command : super.getCreateRelationshipCommand(req);
	}

	/**
	 * @generated
	 */
	protected Command getStartCreateRelationshipCommand(CreateRelationshipRequest req) {
		if (LogicElementTypes.NodeLink_4001 == req.getElementType()) {
			return getGEFWrapper(new NodeLinkCreateCommand(req, req.getSource(), req.getTarget()));
		}
		if (LogicElementTypes.IfElselink_4002 == req.getElementType()) {
			return null;
		}
		if (LogicElementTypes.IfLink_4004 == req.getElementType()) {
			return null;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
		if (LogicElementTypes.NodeLink_4001 == req.getElementType()) {
			return getGEFWrapper(new NodeLinkCreateCommand(req, req.getSource(), req.getTarget()));
		}
		if (LogicElementTypes.IfElselink_4002 == req.getElementType()) {
			return getGEFWrapper(new IfElselinkCreateCommand(req, req.getSource(), req.getTarget()));
		}
		if (LogicElementTypes.IfLink_4004 == req.getElementType()) {
			return getGEFWrapper(new IfLinkCreateCommand(req, req.getSource(), req.getTarget()));
		}
		return null;
	}

	/**
	 * Returns command to reorient EClass based link. New link target or source
	 * should be the domain model element associated with this node.
	 * 
	 * @generated
	 */
	protected Command getReorientRelationshipCommand(ReorientRelationshipRequest req) {
		switch (getVisualID(req)) {
		case IfLinkEditPart.VISUAL_ID:
			return getGEFWrapper(new IfLinkReorientCommand(req));
		}
		return super.getReorientRelationshipCommand(req);
	}

	/**
	 * Returns command to reorient EReference based link. New link target or source
	 * should be the domain model element associated with this node.
	 * 
	 * @generated
	 */
	protected Command getReorientReferenceRelationshipCommand(ReorientReferenceRelationshipRequest req) {
		switch (getVisualID(req)) {
		case NodeLinkEditPart.VISUAL_ID:
			return getGEFWrapper(new NodeLinkReorientCommand(req));
		case IfElselinkEditPart.VISUAL_ID:
			return getGEFWrapper(new IfElselinkReorientCommand(req));
		}
		return super.getReorientReferenceRelationshipCommand(req);
	}

}
