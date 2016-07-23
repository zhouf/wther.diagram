package com.sunsheen.jfids.studio.wther.diagram.providers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

import com.sunsheen.jfids.studio.logic.LogicPackage;
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
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;

/**
 * @generated
 */
public class LogicElementTypes {

	/**
	 * @generated
	 */
	private LogicElementTypes() {
	}

	/**
	 * @generated
	 */
	private static Map<IElementType, ENamedElement> elements;

	/**
	 * @generated
	 */
	private static ImageRegistry imageRegistry;

	/**
	 * @generated
	 */
	private static Set<IElementType> KNOWN_ELEMENT_TYPES;

	/**
	 * @generated
	 */
	public static final IElementType Flow_1000 = getElementType("com.sunsheen.jfids.studio.wther.diagram.Flow_1000"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType Start_2001 = getElementType("com.sunsheen.jfids.studio.wther.diagram.Start_2001"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType End_2002 = getElementType("com.sunsheen.jfids.studio.wther.diagram.End_2002"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType Assignment_2003 = getElementType("com.sunsheen.jfids.studio.wther.diagram.Assignment_2003"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType Custom_2011 = getElementType("com.sunsheen.jfids.studio.wther.diagram.Custom_2011"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Blank_2007 = getElementType("com.sunsheen.jfids.studio.wther.diagram.Blank_2007"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType For_2004 = getElementType("com.sunsheen.jfids.studio.wther.diagram.For_2004"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType If_2005 = getElementType("com.sunsheen.jfids.studio.wther.diagram.If_2005"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType Call_2006 = getElementType("com.sunsheen.jfids.studio.wther.diagram.Call_2006"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType Bixref_2012 = getElementType("com.sunsheen.jfids.studio.wther.diagram.Bixref_2012"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Transaction_2008 = getElementType("com.sunsheen.jfids.studio.wther.diagram.Transaction_2008"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Transcommit_2009 = getElementType("com.sunsheen.jfids.studio.wther.diagram.Transcommit_2009"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Transrollback_2010 = getElementType("com.sunsheen.jfids.studio.wther.diagram.Transrollback_2010"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType NodeLink_4001 = getElementType("com.sunsheen.jfids.studio.wther.diagram.NodeLink_4001"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType EntityExceptionlink_4007 = getElementType("com.sunsheen.jfids.studio.wther.diagram.EntityExceptionlink_4007"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType IfElselink_4002 = getElementType("com.sunsheen.jfids.studio.wther.diagram.IfElselink_4002"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType ForEndlink_4003 = getElementType("com.sunsheen.jfids.studio.wther.diagram.ForEndlink_4003"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType EntityBreak_4006 = getElementType("com.sunsheen.jfids.studio.wther.diagram.EntityBreak_4006"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType IfLink_4004 = getElementType("com.sunsheen.jfids.studio.wther.diagram.IfLink_4004"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	private static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
		}
		return imageRegistry;
	}

	/**
	 * @generated
	 */
	private static String getImageRegistryKey(ENamedElement element) {
		return element.getName();
	}

	/**
	 * @generated
	 */
	private static ImageDescriptor getProvidedImageDescriptor(ENamedElement element) {
		if (element instanceof EStructuralFeature) {
			EStructuralFeature feature = ((EStructuralFeature) element);
			EClass eContainingClass = feature.getEContainingClass();
			EClassifier eType = feature.getEType();
			if (eContainingClass != null && !eContainingClass.isAbstract()) {
				element = eContainingClass;
			} else if (eType instanceof EClass && !((EClass) eType).isAbstract()) {
				element = eType;
			}
		}
		if (element instanceof EClass) {
			EClass eClass = (EClass) element;
			if (!eClass.isAbstract()) {
				return LogicDiagramEditorPlugin.getInstance().getItemImageDescriptor(
						eClass.getEPackage().getEFactoryInstance().create(eClass));
			}
		}
		// TODO : support structural features
		return null;
	}

	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(ENamedElement element) {
		String key = getImageRegistryKey(element);
		ImageDescriptor imageDescriptor = getImageRegistry().getDescriptor(key);
		if (imageDescriptor == null) {
			imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
		}
		return imageDescriptor;
	}

	/**
	 * @generated
	 */
	public static Image getImage(ENamedElement element) {
		String key = getImageRegistryKey(element);
		Image image = getImageRegistry().get(key);
		if (image == null) {
			ImageDescriptor imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
			image = getImageRegistry().get(key);
		}
		return image;
	}

	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(IAdaptable hint) {
		ENamedElement element = getElement(hint);
		if (element == null) {
			return null;
		}
		return getImageDescriptor(element);
	}

	/**
	 * @generated
	 */
	public static Image getImage(IAdaptable hint) {
		ENamedElement element = getElement(hint);
		if (element == null) {
			return null;
		}
		return getImage(element);
	}

	/**
	 * Returns 'type' of the ecore object associated with the hint.
	 * 
	 * @generated
	 */
	public static ENamedElement getElement(IAdaptable hint) {
		Object type = hint.getAdapter(IElementType.class);
		if (elements == null) {
			elements = new IdentityHashMap<IElementType, ENamedElement>();

			elements.put(Flow_1000, LogicPackage.eINSTANCE.getFlow());

			elements.put(Start_2001, LogicPackage.eINSTANCE.getStart());

			elements.put(End_2002, LogicPackage.eINSTANCE.getEnd());

			elements.put(Assignment_2003, LogicPackage.eINSTANCE.getAssignment());

			elements.put(Custom_2011, LogicPackage.eINSTANCE.getCustom());

			elements.put(Blank_2007, LogicPackage.eINSTANCE.getBlank());

			elements.put(For_2004, LogicPackage.eINSTANCE.getFor());

			elements.put(If_2005, LogicPackage.eINSTANCE.getIf());

			elements.put(Call_2006, LogicPackage.eINSTANCE.getCall());

			elements.put(Bixref_2012, LogicPackage.eINSTANCE.getBixref());

			elements.put(Transaction_2008, LogicPackage.eINSTANCE.getTransaction());

			elements.put(Transcommit_2009, LogicPackage.eINSTANCE.getTranscommit());

			elements.put(Transrollback_2010, LogicPackage.eINSTANCE.getTransrollback());

			elements.put(NodeLink_4001, LogicPackage.eINSTANCE.getNode_Link());

			elements.put(EntityExceptionlink_4007, LogicPackage.eINSTANCE.getEntity_Exceptionlink());

			elements.put(IfElselink_4002, LogicPackage.eINSTANCE.getIf_Elselink());

			elements.put(ForEndlink_4003, LogicPackage.eINSTANCE.getFor_Endlink());

			elements.put(EntityBreak_4006, LogicPackage.eINSTANCE.getEntity_Break());

			elements.put(IfLink_4004, LogicPackage.eINSTANCE.getIfLink());
		}
		return (ENamedElement) elements.get(type);
	}

	/**
	 * @generated
	 */
	private static IElementType getElementType(String id) {
		return ElementTypeRegistry.getInstance().getType(id);
	}

	/**
	 * @generated
	 */
	public static boolean isKnownElementType(IElementType elementType) {
		if (KNOWN_ELEMENT_TYPES == null) {
			KNOWN_ELEMENT_TYPES = new HashSet<IElementType>();
			KNOWN_ELEMENT_TYPES.add(Flow_1000);
			KNOWN_ELEMENT_TYPES.add(Start_2001);
			KNOWN_ELEMENT_TYPES.add(End_2002);
			KNOWN_ELEMENT_TYPES.add(Assignment_2003);
			KNOWN_ELEMENT_TYPES.add(Custom_2011);
			KNOWN_ELEMENT_TYPES.add(Blank_2007);
			KNOWN_ELEMENT_TYPES.add(For_2004);
			KNOWN_ELEMENT_TYPES.add(If_2005);
			KNOWN_ELEMENT_TYPES.add(Call_2006);
			KNOWN_ELEMENT_TYPES.add(Bixref_2012);
			KNOWN_ELEMENT_TYPES.add(Transaction_2008);
			KNOWN_ELEMENT_TYPES.add(Transcommit_2009);
			KNOWN_ELEMENT_TYPES.add(Transrollback_2010);
			KNOWN_ELEMENT_TYPES.add(NodeLink_4001);
			KNOWN_ELEMENT_TYPES.add(EntityExceptionlink_4007);
			KNOWN_ELEMENT_TYPES.add(IfElselink_4002);
			KNOWN_ELEMENT_TYPES.add(ForEndlink_4003);
			KNOWN_ELEMENT_TYPES.add(EntityBreak_4006);
			KNOWN_ELEMENT_TYPES.add(IfLink_4004);
		}
		return KNOWN_ELEMENT_TYPES.contains(elementType);
	}

	/**
	 * @generated
	 */
	public static IElementType getElementType(int visualID) {
		switch (visualID) {
		case FlowEditPart.VISUAL_ID:
			return Flow_1000;
		case StartEditPart.VISUAL_ID:
			return Start_2001;
		case EndEditPart.VISUAL_ID:
			return End_2002;
		case AssignmentEditPart.VISUAL_ID:
			return Assignment_2003;
		case CustomEditPart.VISUAL_ID:
			return Custom_2011;
		case BlankEditPart.VISUAL_ID:
			return Blank_2007;
		case ForEditPart.VISUAL_ID:
			return For_2004;
		case IfEditPart.VISUAL_ID:
			return If_2005;
		case CallEditPart.VISUAL_ID:
			return Call_2006;
		case BixrefEditPart.VISUAL_ID:
			return Bixref_2012;
		case TransactionEditPart.VISUAL_ID:
			return Transaction_2008;
		case TranscommitEditPart.VISUAL_ID:
			return Transcommit_2009;
		case TransrollbackEditPart.VISUAL_ID:
			return Transrollback_2010;
		case NodeLinkEditPart.VISUAL_ID:
			return NodeLink_4001;
		case EntityExceptionlinkEditPart.VISUAL_ID:
			return EntityExceptionlink_4007;
		case IfElselinkEditPart.VISUAL_ID:
			return IfElselink_4002;
		case ForEndlinkEditPart.VISUAL_ID:
			return ForEndlink_4003;
		case EntityBreakEditPart.VISUAL_ID:
			return EntityBreak_4006;
		case IfLinkEditPart.VISUAL_ID:
			return IfLink_4004;
		}
		return null;
	}

}
