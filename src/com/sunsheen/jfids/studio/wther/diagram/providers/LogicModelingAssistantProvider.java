package com.sunsheen.jfids.studio.wther.diagram.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantProvider;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.sunsheen.jfids.studio.wther.diagram.edit.parts.AssignmentEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BixrefEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BlankEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.CallEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.CustomEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.EndEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.FlowEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.StartEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransactionEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TranscommitEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransrollbackEditPart;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
import com.sunsheen.jfids.studio.wther.diagram.part.Messages;

/**
 * @generated
 */
public class LogicModelingAssistantProvider extends ModelingAssistantProvider {

	/**
	 * @generated
	 */
	public List getTypesForPopupBar(IAdaptable host) {
		IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
		if (editPart instanceof FlowEditPart) {
			ArrayList<IElementType> types = new ArrayList<IElementType>(12);
			types.add(LogicElementTypes.Start_2001);
			types.add(LogicElementTypes.End_2002);
			types.add(LogicElementTypes.Assignment_2003);
			types.add(LogicElementTypes.Custom_2011);
			types.add(LogicElementTypes.Blank_2007);
			types.add(LogicElementTypes.For_2004);
			types.add(LogicElementTypes.If_2005);
			types.add(LogicElementTypes.Call_2006);
			types.add(LogicElementTypes.Bixref_2012);
			types.add(LogicElementTypes.Transaction_2008);
			types.add(LogicElementTypes.Transcommit_2009);
			types.add(LogicElementTypes.Transrollback_2010);
			return types;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public List getRelTypesOnSource(IAdaptable source) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
		if (sourceEditPart instanceof StartEditPart) {
			return ((StartEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof EndEditPart) {
			return ((EndEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof AssignmentEditPart) {
			return ((AssignmentEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof CustomEditPart) {
			return ((CustomEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof BlankEditPart) {
			return ((BlankEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof ForEditPart) {
			return ((ForEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof IfEditPart) {
			return ((IfEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof CallEditPart) {
			return ((CallEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof BixrefEditPart) {
			return ((BixrefEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof TransactionEditPart) {
			return ((TransactionEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof TranscommitEditPart) {
			return ((TranscommitEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof TransrollbackEditPart) {
			return ((TransrollbackEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public List getRelTypesOnTarget(IAdaptable target) {
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
		if (targetEditPart instanceof StartEditPart) {
			return ((StartEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof EndEditPart) {
			return ((EndEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof AssignmentEditPart) {
			return ((AssignmentEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof CustomEditPart) {
			return ((CustomEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof BlankEditPart) {
			return ((BlankEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof ForEditPart) {
			return ((ForEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof IfEditPart) {
			return ((IfEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof CallEditPart) {
			return ((CallEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof BixrefEditPart) {
			return ((BixrefEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof TransactionEditPart) {
			return ((TransactionEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof TranscommitEditPart) {
			return ((TranscommitEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof TransrollbackEditPart) {
			return ((TransrollbackEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public List getRelTypesOnSourceAndTarget(IAdaptable source, IAdaptable target) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
		if (sourceEditPart instanceof StartEditPart) {
			return ((StartEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof EndEditPart) {
			return ((EndEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof AssignmentEditPart) {
			return ((AssignmentEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof CustomEditPart) {
			return ((CustomEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof BlankEditPart) {
			return ((BlankEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof ForEditPart) {
			return ((ForEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof IfEditPart) {
			return ((IfEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof CallEditPart) {
			return ((CallEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof BixrefEditPart) {
			return ((BixrefEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof TransactionEditPart) {
			return ((TransactionEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof TranscommitEditPart) {
			return ((TranscommitEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof TransrollbackEditPart) {
			return ((TransrollbackEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public List getTypesForSource(IAdaptable target, IElementType relationshipType) {
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
		if (targetEditPart instanceof StartEditPart) {
			return ((StartEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof EndEditPart) {
			return ((EndEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof AssignmentEditPart) {
			return ((AssignmentEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof CustomEditPart) {
			return ((CustomEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof BlankEditPart) {
			return ((BlankEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof ForEditPart) {
			return ((ForEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof IfEditPart) {
			return ((IfEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof CallEditPart) {
			return ((CallEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof BixrefEditPart) {
			return ((BixrefEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof TransactionEditPart) {
			return ((TransactionEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof TranscommitEditPart) {
			return ((TranscommitEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof TransrollbackEditPart) {
			return ((TransrollbackEditPart) targetEditPart).getMATypesForSource(relationshipType);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public List getTypesForTarget(IAdaptable source, IElementType relationshipType) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
		if (sourceEditPart instanceof StartEditPart) {
			return ((StartEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof EndEditPart) {
			return ((EndEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof AssignmentEditPart) {
			return ((AssignmentEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof CustomEditPart) {
			return ((CustomEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof BlankEditPart) {
			return ((BlankEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof ForEditPart) {
			return ((ForEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof IfEditPart) {
			return ((IfEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof CallEditPart) {
			return ((CallEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof BixrefEditPart) {
			return ((BixrefEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof TransactionEditPart) {
			return ((TransactionEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof TranscommitEditPart) {
			return ((TranscommitEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof TransrollbackEditPart) {
			return ((TransrollbackEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public EObject selectExistingElementForSource(IAdaptable target, IElementType relationshipType) {
		return selectExistingElement(target, getTypesForSource(target, relationshipType));
	}

	/**
	 * @generated
	 */
	public EObject selectExistingElementForTarget(IAdaptable source, IElementType relationshipType) {
		return selectExistingElement(source, getTypesForTarget(source, relationshipType));
	}

	/**
	 * @generated
	 */
	protected EObject selectExistingElement(IAdaptable host, Collection types) {
		if (types.isEmpty()) {
			return null;
		}
		IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
		if (editPart == null) {
			return null;
		}
		Diagram diagram = (Diagram) editPart.getRoot().getContents().getModel();
		HashSet<EObject> elements = new HashSet<EObject>();
		for (Iterator<EObject> it = diagram.getElement().eAllContents(); it.hasNext();) {
			EObject element = it.next();
			if (isApplicableElement(element, types)) {
				elements.add(element);
			}
		}
		if (elements.isEmpty()) {
			return null;
		}
		return selectElement((EObject[]) elements.toArray(new EObject[elements.size()]));
	}

	/**
	 * @generated
	 */
	protected boolean isApplicableElement(EObject element, Collection types) {
		IElementType type = ElementTypeRegistry.getInstance().getElementType(element);
		return types.contains(type);
	}

	/**
	 * @generated
	 */
	protected EObject selectElement(EObject[] elements) {
		Shell shell = Display.getCurrent().getActiveShell();
		ILabelProvider labelProvider = new AdapterFactoryLabelProvider(LogicDiagramEditorPlugin.getInstance()
				.getItemProvidersAdapterFactory());
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
		dialog.setMessage(Messages.LogicModelingAssistantProviderMessage);
		dialog.setTitle(Messages.LogicModelingAssistantProviderTitle);
		dialog.setMultipleSelection(false);
		dialog.setElements(elements);
		EObject selected = null;
		if (dialog.open() == Window.OK) {
			selected = (EObject) dialog.getFirstResult();
		}
		return selected;
	}
}
