package com.sunsheen.jfids.studio.wther.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

import com.sunsheen.jfids.studio.logic.Flow;
import com.sunsheen.jfids.studio.logic.LogicPackage;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.AssignmentEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.AssignmentNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BixrefEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BixrefNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BlankEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BlankNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.CallEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.CallNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.CustomEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.CustomNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.EndEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.EndLabelEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.FlowEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfLinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfLinkTipEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.StartEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.StartLabelEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransactionEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransactionNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TranscommitEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TranscommitNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransrollbackEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransrollbackNameEditPart;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented
 * by a domain model object.
 * 
 * @generated
 */
public class LogicVisualIDRegistry {

	/**
	 * @generated
	 */
	private static final String DEBUG_KEY = "com.sunsheen.jfids.studio.wther.diagram/debug/visualID"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static int getVisualID(View view) {
		if (view instanceof Diagram) {
			if (FlowEditPart.MODEL_ID.equals(view.getType())) {
				return FlowEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		return com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry.getVisualID(view.getType());
	}

	/**
	 * @generated
	 */
	public static String getModelID(View view) {
		View diagram = view.getDiagram();
		while (view != diagram) {
			EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
			if (annotation != null) {
				return (String) annotation.getDetails().get("modelID"); //$NON-NLS-1$
			}
			view = (View) view.eContainer();
		}
		return diagram != null ? diagram.getType() : null;
	}

	/**
	 * @generated
	 */
	public static int getVisualID(String type) {
		try {
			return Integer.parseInt(type);
		} catch (NumberFormatException e) {
			if (Boolean.TRUE.toString().equalsIgnoreCase(Platform.getDebugOption(DEBUG_KEY))) {
				LogicDiagramEditorPlugin.getInstance().logError(
						"Unable to parse view type as a visualID number: " + type);
			}
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static String getType(int visualID) {
		return Integer.toString(visualID);
	}

	/**
	 * @generated
	 */
	public static int getDiagramVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (LogicPackage.eINSTANCE.getFlow().isSuperTypeOf(domainElement.eClass()) && isDiagram((Flow) domainElement)) {
			return FlowEditPart.VISUAL_ID;
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static int getNodeVisualID(View containerView, EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		String containerModelID = com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry
				.getModelID(containerView);
		if (!FlowEditPart.MODEL_ID.equals(containerModelID)) {
			return -1;
		}
		int containerVisualID;
		if (FlowEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry
					.getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = FlowEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		switch (containerVisualID) {
		case FlowEditPart.VISUAL_ID:
			if (LogicPackage.eINSTANCE.getStart().isSuperTypeOf(domainElement.eClass())) {
				return StartEditPart.VISUAL_ID;
			}
			if (LogicPackage.eINSTANCE.getEnd().isSuperTypeOf(domainElement.eClass())) {
				return EndEditPart.VISUAL_ID;
			}
			if (LogicPackage.eINSTANCE.getAssignment().isSuperTypeOf(domainElement.eClass())) {
				return AssignmentEditPart.VISUAL_ID;
			}
			if (LogicPackage.eINSTANCE.getCustom().isSuperTypeOf(domainElement.eClass())) {
				return CustomEditPart.VISUAL_ID;
			}
			if (LogicPackage.eINSTANCE.getBlank().isSuperTypeOf(domainElement.eClass())) {
				return BlankEditPart.VISUAL_ID;
			}
			if (LogicPackage.eINSTANCE.getFor().isSuperTypeOf(domainElement.eClass())) {
				return ForEditPart.VISUAL_ID;
			}
			if (LogicPackage.eINSTANCE.getIf().isSuperTypeOf(domainElement.eClass())) {
				return IfEditPart.VISUAL_ID;
			}
			if (LogicPackage.eINSTANCE.getCall().isSuperTypeOf(domainElement.eClass())) {
				return CallEditPart.VISUAL_ID;
			}
			if (LogicPackage.eINSTANCE.getBixref().isSuperTypeOf(domainElement.eClass())) {
				return BixrefEditPart.VISUAL_ID;
			}
			if (LogicPackage.eINSTANCE.getTransaction().isSuperTypeOf(domainElement.eClass())) {
				return TransactionEditPart.VISUAL_ID;
			}
			if (LogicPackage.eINSTANCE.getTranscommit().isSuperTypeOf(domainElement.eClass())) {
				return TranscommitEditPart.VISUAL_ID;
			}
			if (LogicPackage.eINSTANCE.getTransrollback().isSuperTypeOf(domainElement.eClass())) {
				return TransrollbackEditPart.VISUAL_ID;
			}
			break;
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static boolean canCreateNode(View containerView, int nodeVisualID) {
		String containerModelID = com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry
				.getModelID(containerView);
		if (!FlowEditPart.MODEL_ID.equals(containerModelID)) {
			return false;
		}
		int containerVisualID;
		if (FlowEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry
					.getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = FlowEditPart.VISUAL_ID;
			} else {
				return false;
			}
		}
		switch (containerVisualID) {
		case FlowEditPart.VISUAL_ID:
			if (StartEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (EndEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (AssignmentEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (CustomEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (BlankEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ForEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IfEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (CallEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (BixrefEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (TransactionEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (TranscommitEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (TransrollbackEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case StartEditPart.VISUAL_ID:
			if (StartLabelEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case EndEditPart.VISUAL_ID:
			if (EndLabelEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case AssignmentEditPart.VISUAL_ID:
			if (AssignmentNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case CustomEditPart.VISUAL_ID:
			if (CustomNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case BlankEditPart.VISUAL_ID:
			if (BlankNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ForEditPart.VISUAL_ID:
			if (ForNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case IfEditPart.VISUAL_ID:
			if (IfNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case CallEditPart.VISUAL_ID:
			if (CallNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case BixrefEditPart.VISUAL_ID:
			if (BixrefNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case TransactionEditPart.VISUAL_ID:
			if (TransactionNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case TranscommitEditPart.VISUAL_ID:
			if (TranscommitNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case TransrollbackEditPart.VISUAL_ID:
			if (TransrollbackNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case IfLinkEditPart.VISUAL_ID:
			if (IfLinkTipEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * @generated
	 */
	public static int getLinkWithClassVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (LogicPackage.eINSTANCE.getIfLink().isSuperTypeOf(domainElement.eClass())) {
			return IfLinkEditPart.VISUAL_ID;
		}
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static boolean isDiagram(Flow element) {
		return true;
	}

}
