package com.sunsheen.jfids.studio.wther.diagram.part;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;

import com.sunsheen.jfids.studio.logic.Assignment;
import com.sunsheen.jfids.studio.logic.Bixref;
import com.sunsheen.jfids.studio.logic.Blank;
import com.sunsheen.jfids.studio.logic.Call;
import com.sunsheen.jfids.studio.logic.Custom;
import com.sunsheen.jfids.studio.logic.End;
import com.sunsheen.jfids.studio.logic.Entity;
import com.sunsheen.jfids.studio.logic.Flow;
import com.sunsheen.jfids.studio.logic.For;
import com.sunsheen.jfids.studio.logic.If;
import com.sunsheen.jfids.studio.logic.IfLink;
import com.sunsheen.jfids.studio.logic.LogicPackage;
import com.sunsheen.jfids.studio.logic.Node;
import com.sunsheen.jfids.studio.logic.Start;
import com.sunsheen.jfids.studio.logic.Transaction;
import com.sunsheen.jfids.studio.logic.Transcommit;
import com.sunsheen.jfids.studio.logic.Transrollback;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.AssignmentEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BixrefEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BlankEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.CallEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.CustomEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.EndEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.EntityBreakEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.EntityExceptionlinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.FlowEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForEndlinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfElselinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfLinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.NodeLinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.StartEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransactionEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TranscommitEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransrollbackEditPart;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;

/**
 * @generated
 */
public class LogicDiagramUpdater {

	/**
	 * @generated
	 */
	public static List<LogicNodeDescriptor> getSemanticChildren(View view) {
		switch (LogicVisualIDRegistry.getVisualID(view)) {
		case FlowEditPart.VISUAL_ID:
			return getFlow_1000SemanticChildren(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<LogicNodeDescriptor> getFlow_1000SemanticChildren(View view) {
		if (!view.isSetElement()) {
			return Collections.emptyList();
		}
		Flow modelElement = (Flow) view.getElement();
		LinkedList<LogicNodeDescriptor> result = new LinkedList<LogicNodeDescriptor>();
		for (Iterator<?> it = modelElement.getNodes().iterator(); it.hasNext();) {
			Node childElement = (Node) it.next();
			int visualID = LogicVisualIDRegistry.getNodeVisualID(view, childElement);
			if (visualID == StartEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == EndEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == AssignmentEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == CustomEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == BlankEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ForEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == IfEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == CallEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == BixrefEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == TransactionEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == TranscommitEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == TransrollbackEditPart.VISUAL_ID) {
				result.add(new LogicNodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getContainedLinks(View view) {
		switch (LogicVisualIDRegistry.getVisualID(view)) {
		case FlowEditPart.VISUAL_ID:
			return getFlow_1000ContainedLinks(view);
		case StartEditPart.VISUAL_ID:
			return getStart_2001ContainedLinks(view);
		case EndEditPart.VISUAL_ID:
			return getEnd_2002ContainedLinks(view);
		case AssignmentEditPart.VISUAL_ID:
			return getAssignment_2003ContainedLinks(view);
		case CustomEditPart.VISUAL_ID:
			return getCustom_2011ContainedLinks(view);
		case BlankEditPart.VISUAL_ID:
			return getBlank_2007ContainedLinks(view);
		case ForEditPart.VISUAL_ID:
			return getFor_2004ContainedLinks(view);
		case IfEditPart.VISUAL_ID:
			return getIf_2005ContainedLinks(view);
		case CallEditPart.VISUAL_ID:
			return getCall_2006ContainedLinks(view);
		case BixrefEditPart.VISUAL_ID:
			return getBixref_2012ContainedLinks(view);
		case TransactionEditPart.VISUAL_ID:
			return getTransaction_2008ContainedLinks(view);
		case TranscommitEditPart.VISUAL_ID:
			return getTranscommit_2009ContainedLinks(view);
		case TransrollbackEditPart.VISUAL_ID:
			return getTransrollback_2010ContainedLinks(view);
		case IfLinkEditPart.VISUAL_ID:
			return getIfLink_4004ContainedLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getIncomingLinks(View view) {
		switch (LogicVisualIDRegistry.getVisualID(view)) {
		case StartEditPart.VISUAL_ID:
			return getStart_2001IncomingLinks(view);
		case EndEditPart.VISUAL_ID:
			return getEnd_2002IncomingLinks(view);
		case AssignmentEditPart.VISUAL_ID:
			return getAssignment_2003IncomingLinks(view);
		case CustomEditPart.VISUAL_ID:
			return getCustom_2011IncomingLinks(view);
		case BlankEditPart.VISUAL_ID:
			return getBlank_2007IncomingLinks(view);
		case ForEditPart.VISUAL_ID:
			return getFor_2004IncomingLinks(view);
		case IfEditPart.VISUAL_ID:
			return getIf_2005IncomingLinks(view);
		case CallEditPart.VISUAL_ID:
			return getCall_2006IncomingLinks(view);
		case BixrefEditPart.VISUAL_ID:
			return getBixref_2012IncomingLinks(view);
		case TransactionEditPart.VISUAL_ID:
			return getTransaction_2008IncomingLinks(view);
		case TranscommitEditPart.VISUAL_ID:
			return getTranscommit_2009IncomingLinks(view);
		case TransrollbackEditPart.VISUAL_ID:
			return getTransrollback_2010IncomingLinks(view);
		case IfLinkEditPart.VISUAL_ID:
			return getIfLink_4004IncomingLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getOutgoingLinks(View view) {
		switch (LogicVisualIDRegistry.getVisualID(view)) {
		case StartEditPart.VISUAL_ID:
			return getStart_2001OutgoingLinks(view);
		case EndEditPart.VISUAL_ID:
			return getEnd_2002OutgoingLinks(view);
		case AssignmentEditPart.VISUAL_ID:
			return getAssignment_2003OutgoingLinks(view);
		case CustomEditPart.VISUAL_ID:
			return getCustom_2011OutgoingLinks(view);
		case BlankEditPart.VISUAL_ID:
			return getBlank_2007OutgoingLinks(view);
		case ForEditPart.VISUAL_ID:
			return getFor_2004OutgoingLinks(view);
		case IfEditPart.VISUAL_ID:
			return getIf_2005OutgoingLinks(view);
		case CallEditPart.VISUAL_ID:
			return getCall_2006OutgoingLinks(view);
		case BixrefEditPart.VISUAL_ID:
			return getBixref_2012OutgoingLinks(view);
		case TransactionEditPart.VISUAL_ID:
			return getTransaction_2008OutgoingLinks(view);
		case TranscommitEditPart.VISUAL_ID:
			return getTranscommit_2009OutgoingLinks(view);
		case TransrollbackEditPart.VISUAL_ID:
			return getTransrollback_2010OutgoingLinks(view);
		case IfLinkEditPart.VISUAL_ID:
			return getIfLink_4004OutgoingLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getFlow_1000ContainedLinks(View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getStart_2001ContainedLinks(View view) {
		Start modelElement = (Start) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getEnd_2002ContainedLinks(View view) {
		End modelElement = (End) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getAssignment_2003ContainedLinks(View view) {
		Assignment modelElement = (Assignment) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getCustom_2011ContainedLinks(View view) {
		Custom modelElement = (Custom) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getBlank_2007ContainedLinks(View view) {
		Blank modelElement = (Blank) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getFor_2004ContainedLinks(View view) {
		For modelElement = (For) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_For_Endlink_4003(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getIf_2005ContainedLinks(View view) {
		If modelElement = (If) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_If_Elselink_4002(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		result.addAll(getContainedTypeModelFacetLinks_IfLink_4004(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getCall_2006ContainedLinks(View view) {
		Call modelElement = (Call) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getBixref_2012ContainedLinks(View view) {
		Bixref modelElement = (Bixref) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getTransaction_2008ContainedLinks(View view) {
		Transaction modelElement = (Transaction) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getTranscommit_2009ContainedLinks(View view) {
		Transcommit modelElement = (Transcommit) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getTransrollback_2010ContainedLinks(View view) {
		Transrollback modelElement = (Transrollback) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getIfLink_4004ContainedLinks(View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getStart_2001IncomingLinks(View view) {
		Start modelElement = (Start) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getEnd_2002IncomingLinks(View view) {
		End modelElement = (End) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getAssignment_2003IncomingLinks(View view) {
		Assignment modelElement = (Assignment) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_For_Endlink_4003(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Break_4006(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getCustom_2011IncomingLinks(View view) {
		Custom modelElement = (Custom) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_For_Endlink_4003(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Break_4006(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getBlank_2007IncomingLinks(View view) {
		Blank modelElement = (Blank) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_For_Endlink_4003(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Break_4006(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getFor_2004IncomingLinks(View view) {
		For modelElement = (For) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_For_Endlink_4003(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Break_4006(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getIf_2005IncomingLinks(View view) {
		If modelElement = (If) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_For_Endlink_4003(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Break_4006(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getCall_2006IncomingLinks(View view) {
		Call modelElement = (Call) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_For_Endlink_4003(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Break_4006(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getBixref_2012IncomingLinks(View view) {
		Bixref modelElement = (Bixref) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_For_Endlink_4003(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Break_4006(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getTransaction_2008IncomingLinks(View view) {
		Transaction modelElement = (Transaction) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_For_Endlink_4003(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Break_4006(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getTranscommit_2009IncomingLinks(View view) {
		Transcommit modelElement = (Transcommit) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_For_Endlink_4003(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Break_4006(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getTransrollback_2010IncomingLinks(View view) {
		Transrollback modelElement = (Transrollback) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view
				.eResource().getResourceSet().getResources());
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getIncomingFeatureModelFacetLinks_Node_Link_4001(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_If_Elselink_4002(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_For_Endlink_4003(modelElement, crossReferences));
		result.addAll(getIncomingFeatureModelFacetLinks_Entity_Break_4006(modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_IfLink_4004(modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getIfLink_4004IncomingLinks(View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getStart_2001OutgoingLinks(View view) {
		Start modelElement = (Start) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getEnd_2002OutgoingLinks(View view) {
		End modelElement = (End) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getAssignment_2003OutgoingLinks(View view) {
		Assignment modelElement = (Assignment) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getCustom_2011OutgoingLinks(View view) {
		Custom modelElement = (Custom) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getBlank_2007OutgoingLinks(View view) {
		Blank modelElement = (Blank) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getFor_2004OutgoingLinks(View view) {
		For modelElement = (For) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_For_Endlink_4003(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getIf_2005OutgoingLinks(View view) {
		If modelElement = (If) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_If_Elselink_4002(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_IfLink_4004(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getCall_2006OutgoingLinks(View view) {
		Call modelElement = (Call) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getBixref_2012OutgoingLinks(View view) {
		Bixref modelElement = (Bixref) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getTransaction_2008OutgoingLinks(View view) {
		Transaction modelElement = (Transaction) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getTranscommit_2009OutgoingLinks(View view) {
		Transcommit modelElement = (Transcommit) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getTransrollback_2010OutgoingLinks(View view) {
		Transrollback modelElement = (Transrollback) view.getElement();
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		result.addAll(getOutgoingFeatureModelFacetLinks_Node_Link_4001(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(modelElement));
		result.addAll(getOutgoingFeatureModelFacetLinks_Entity_Break_4006(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<LogicLinkDescriptor> getIfLink_4004OutgoingLinks(View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getContainedTypeModelFacetLinks_IfLink_4004(If container) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		for (Iterator<?> links = container.getIflinks().iterator(); links.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof IfLink) {
				continue;
			}
			IfLink link = (IfLink) linkObject;
			if (IfLinkEditPart.VISUAL_ID != LogicVisualIDRegistry.getLinkWithClassVisualID(link)) {
				continue;
			}
			Node dst = link.getTarget();
			If src = link.getSource();
			result.add(new LogicLinkDescriptor(src, dst, link, LogicElementTypes.IfLink_4004, IfLinkEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getIncomingFeatureModelFacetLinks_Node_Link_4001(Node target,
			Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		Collection<EStructuralFeature.Setting> settings = crossReferences.get(target);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEStructuralFeature() == LogicPackage.eINSTANCE.getNode_Link()) {
				result.add(new LogicLinkDescriptor(setting.getEObject(), target, LogicElementTypes.NodeLink_4001,
						NodeLinkEditPart.VISUAL_ID));
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getIncomingFeatureModelFacetLinks_Entity_Exceptionlink_4007(
			Entity target, Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		Collection<EStructuralFeature.Setting> settings = crossReferences.get(target);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEStructuralFeature() == LogicPackage.eINSTANCE.getEntity_Exceptionlink()) {
				result.add(new LogicLinkDescriptor(setting.getEObject(), target,
						LogicElementTypes.EntityExceptionlink_4007, EntityExceptionlinkEditPart.VISUAL_ID));
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getIncomingFeatureModelFacetLinks_If_Elselink_4002(Node target,
			Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		Collection<EStructuralFeature.Setting> settings = crossReferences.get(target);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEStructuralFeature() == LogicPackage.eINSTANCE.getIf_Elselink()) {
				result.add(new LogicLinkDescriptor(setting.getEObject(), target, LogicElementTypes.IfElselink_4002,
						IfElselinkEditPart.VISUAL_ID));
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getIncomingFeatureModelFacetLinks_For_Endlink_4003(Entity target,
			Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		Collection<EStructuralFeature.Setting> settings = crossReferences.get(target);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEStructuralFeature() == LogicPackage.eINSTANCE.getFor_Endlink()) {
				result.add(new LogicLinkDescriptor(setting.getEObject(), target, LogicElementTypes.ForEndlink_4003,
						ForEndlinkEditPart.VISUAL_ID));
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getIncomingFeatureModelFacetLinks_Entity_Break_4006(Entity target,
			Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		Collection<EStructuralFeature.Setting> settings = crossReferences.get(target);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEStructuralFeature() == LogicPackage.eINSTANCE.getEntity_Break()) {
				result.add(new LogicLinkDescriptor(setting.getEObject(), target, LogicElementTypes.EntityBreak_4006,
						EntityBreakEditPart.VISUAL_ID));
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getIncomingTypeModelFacetLinks_IfLink_4004(Node target,
			Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		Collection<EStructuralFeature.Setting> settings = crossReferences.get(target);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEStructuralFeature() != LogicPackage.eINSTANCE.getIfLink_Target()
					|| false == setting.getEObject() instanceof IfLink) {
				continue;
			}
			IfLink link = (IfLink) setting.getEObject();
			if (IfLinkEditPart.VISUAL_ID != LogicVisualIDRegistry.getLinkWithClassVisualID(link)) {
				continue;
			}
			If src = link.getSource();
			result.add(new LogicLinkDescriptor(src, target, link, LogicElementTypes.IfLink_4004,
					IfLinkEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getOutgoingFeatureModelFacetLinks_Node_Link_4001(Node source) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		for (Iterator<?> destinations = source.getLink().iterator(); destinations.hasNext();) {
			Node destination = (Node) destinations.next();
			result.add(new LogicLinkDescriptor(source, destination, LogicElementTypes.NodeLink_4001,
					NodeLinkEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getOutgoingFeatureModelFacetLinks_Entity_Exceptionlink_4007(
			Entity source) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		Entity destination = source.getExceptionlink();
		if (destination == null) {
			return result;
		}
		result.add(new LogicLinkDescriptor(source, destination, LogicElementTypes.EntityExceptionlink_4007,
				EntityExceptionlinkEditPart.VISUAL_ID));
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getOutgoingFeatureModelFacetLinks_If_Elselink_4002(If source) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		Node destination = source.getElselink();
		if (destination == null) {
			return result;
		}
		result.add(new LogicLinkDescriptor(source, destination, LogicElementTypes.IfElselink_4002,
				IfElselinkEditPart.VISUAL_ID));
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getOutgoingFeatureModelFacetLinks_For_Endlink_4003(For source) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		Entity destination = source.getEndlink();
		if (destination == null) {
			return result;
		}
		result.add(new LogicLinkDescriptor(source, destination, LogicElementTypes.ForEndlink_4003,
				ForEndlinkEditPart.VISUAL_ID));
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getOutgoingFeatureModelFacetLinks_Entity_Break_4006(Entity source) {
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		Entity destination = source.getBreak();
		if (destination == null) {
			return result;
		}
		result.add(new LogicLinkDescriptor(source, destination, LogicElementTypes.EntityBreak_4006,
				EntityBreakEditPart.VISUAL_ID));
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<LogicLinkDescriptor> getOutgoingTypeModelFacetLinks_IfLink_4004(If source) {
		If container = null;
		// Find container element for the link.
		// Climb up by containment hierarchy starting from the source
		// and return the first element that is instance of the container class.
		for (EObject element = source; element != null && container == null; element = element.eContainer()) {
			if (element instanceof If) {
				container = (If) element;
			}
		}
		if (container == null) {
			return Collections.emptyList();
		}
		LinkedList<LogicLinkDescriptor> result = new LinkedList<LogicLinkDescriptor>();
		for (Iterator<?> links = container.getIflinks().iterator(); links.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof IfLink) {
				continue;
			}
			IfLink link = (IfLink) linkObject;
			if (IfLinkEditPart.VISUAL_ID != LogicVisualIDRegistry.getLinkWithClassVisualID(link)) {
				continue;
			}
			Node dst = link.getTarget();
			If src = link.getSource();
			if (src != source) {
				continue;
			}
			result.add(new LogicLinkDescriptor(src, dst, link, LogicElementTypes.IfLink_4004, IfLinkEditPart.VISUAL_ID));
		}
		return result;
	}

}
