package com.sunsheen.jfids.studio.wther.diagram.navigator;

import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITreePathLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import com.sunsheen.jfids.studio.logic.Flow;
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
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.EntityBreakEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.EntityExceptionlinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.FlowEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForEndlinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfElselinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfLinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfLinkTipEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.NodeLinkEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.StartEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.StartLabelEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransactionEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransactionNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TranscommitEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TranscommitNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransrollbackEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransrollbackNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicParserProvider;

/**
 * @generated
 */
public class LogicNavigatorLabelProvider extends LabelProvider implements ICommonLabelProvider, ITreePathLabelProvider {

	/**
	 * @generated
	 */
	static {
		LogicDiagramEditorPlugin.getInstance().getImageRegistry()
				.put("Navigator?UnknownElement", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
		LogicDiagramEditorPlugin.getInstance().getImageRegistry()
				.put("Navigator?ImageNotFound", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	public void updateLabel(ViewerLabel label, TreePath elementPath) {
		Object element = elementPath.getLastSegment();
		if (element instanceof LogicNavigatorItem && !isOwnView(((LogicNavigatorItem) element).getView())) {
			return;
		}
		label.setText(getText(element));
		label.setImage(getImage(element));
	}

	/**
	 * @generated
	 */
	public Image getImage(Object element) {
		if (element instanceof LogicNavigatorGroup) {
			LogicNavigatorGroup group = (LogicNavigatorGroup) element;
			return LogicDiagramEditorPlugin.getInstance().getBundledImage(group.getIcon());
		}

		if (element instanceof LogicNavigatorItem) {
			LogicNavigatorItem navigatorItem = (LogicNavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return super.getImage(element);
			}
			return getImage(navigatorItem.getView());
		}

		return super.getImage(element);
	}

	/**
	 * @generated
	 */
	public Image getImage(View view) {
		switch (LogicVisualIDRegistry.getVisualID(view)) {
		case NodeLinkEditPart.VISUAL_ID:
			return getImage("Navigator?Link?http://logic?Node?link", LogicElementTypes.NodeLink_4001); //$NON-NLS-1$
		case EntityBreakEditPart.VISUAL_ID:
			return getImage("Navigator?Link?http://logic?Entity?break", LogicElementTypes.EntityBreak_4006); //$NON-NLS-1$
		case TranscommitEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?Transcommit", LogicElementTypes.Transcommit_2009); //$NON-NLS-1$
		case StartEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?Start", LogicElementTypes.Start_2001); //$NON-NLS-1$
		case ForEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?For", LogicElementTypes.For_2004); //$NON-NLS-1$
		case TransrollbackEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?Transrollback", LogicElementTypes.Transrollback_2010); //$NON-NLS-1$
		case EntityExceptionlinkEditPart.VISUAL_ID:
			return getImage(
					"Navigator?Link?http://logic?Entity?exceptionlink", LogicElementTypes.EntityExceptionlink_4007); //$NON-NLS-1$
		case BlankEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?Blank", LogicElementTypes.Blank_2007); //$NON-NLS-1$
		case ForEndlinkEditPart.VISUAL_ID:
			return getImage("Navigator?Link?http://logic?For?endlink", LogicElementTypes.ForEndlink_4003); //$NON-NLS-1$
		case TransactionEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?Transaction", LogicElementTypes.Transaction_2008); //$NON-NLS-1$
		case BixrefEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?Bixref", LogicElementTypes.Bixref_2012); //$NON-NLS-1$
		case FlowEditPart.VISUAL_ID:
			return getImage("Navigator?Diagram?http://logic?Flow", LogicElementTypes.Flow_1000); //$NON-NLS-1$
		case CallEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?Call", LogicElementTypes.Call_2006); //$NON-NLS-1$
		case EndEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?End", LogicElementTypes.End_2002); //$NON-NLS-1$
		case AssignmentEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?Assignment", LogicElementTypes.Assignment_2003); //$NON-NLS-1$
		case IfLinkEditPart.VISUAL_ID:
			return getImage("Navigator?Link?http://logic?IfLink", LogicElementTypes.IfLink_4004); //$NON-NLS-1$
		case CustomEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?Custom", LogicElementTypes.Custom_2011); //$NON-NLS-1$
		case IfEditPart.VISUAL_ID:
			return getImage("Navigator?TopLevelNode?http://logic?If", LogicElementTypes.If_2005); //$NON-NLS-1$
		case IfElselinkEditPart.VISUAL_ID:
			return getImage("Navigator?Link?http://logic?If?elselink", LogicElementTypes.IfElselink_4002); //$NON-NLS-1$
		}
		return getImage("Navigator?UnknownElement", null); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private Image getImage(String key, IElementType elementType) {
		ImageRegistry imageRegistry = LogicDiagramEditorPlugin.getInstance().getImageRegistry();
		Image image = imageRegistry.get(key);
		if (image == null && elementType != null && LogicElementTypes.isKnownElementType(elementType)) {
			image = LogicElementTypes.getImage(elementType);
			imageRegistry.put(key, image);
		}

		if (image == null) {
			image = imageRegistry.get("Navigator?ImageNotFound"); //$NON-NLS-1$
			imageRegistry.put(key, image);
		}
		return image;
	}

	/**
	 * @generated
	 */
	public String getText(Object element) {
		if (element instanceof LogicNavigatorGroup) {
			LogicNavigatorGroup group = (LogicNavigatorGroup) element;
			return group.getGroupName();
		}

		if (element instanceof LogicNavigatorItem) {
			LogicNavigatorItem navigatorItem = (LogicNavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return null;
			}
			return getText(navigatorItem.getView());
		}

		return super.getText(element);
	}

	/**
	 * @generated
	 */
	public String getText(View view) {
		if (view.getElement() != null && view.getElement().eIsProxy()) {
			return getUnresolvedDomainElementProxyText(view);
		}
		switch (LogicVisualIDRegistry.getVisualID(view)) {
		case NodeLinkEditPart.VISUAL_ID:
			return getNodeLink_4001Text(view);
		case EntityBreakEditPart.VISUAL_ID:
			return getEntityBreak_4006Text(view);
		case TranscommitEditPart.VISUAL_ID:
			return getTranscommit_2009Text(view);
		case StartEditPart.VISUAL_ID:
			return getStart_2001Text(view);
		case ForEditPart.VISUAL_ID:
			return getFor_2004Text(view);
		case TransrollbackEditPart.VISUAL_ID:
			return getTransrollback_2010Text(view);
		case EntityExceptionlinkEditPart.VISUAL_ID:
			return getEntityExceptionlink_4007Text(view);
		case BlankEditPart.VISUAL_ID:
			return getBlank_2007Text(view);
		case ForEndlinkEditPart.VISUAL_ID:
			return getForEndlink_4003Text(view);
		case TransactionEditPart.VISUAL_ID:
			return getTransaction_2008Text(view);
		case BixrefEditPart.VISUAL_ID:
			return getBixref_2012Text(view);
		case FlowEditPart.VISUAL_ID:
			return getFlow_1000Text(view);
		case CallEditPart.VISUAL_ID:
			return getCall_2006Text(view);
		case EndEditPart.VISUAL_ID:
			return getEnd_2002Text(view);
		case AssignmentEditPart.VISUAL_ID:
			return getAssignment_2003Text(view);
		case IfLinkEditPart.VISUAL_ID:
			return getIfLink_4004Text(view);
		case CustomEditPart.VISUAL_ID:
			return getCustom_2011Text(view);
		case IfEditPart.VISUAL_ID:
			return getIf_2005Text(view);
		case IfElselinkEditPart.VISUAL_ID:
			return getIfElselink_4002Text(view);
		}
		return getUnknownElementText(view);
	}

	/**
	 * @generated
	 */
	private String getFlow_1000Text(View view) {
		Flow domainModelElement = (Flow) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getAuthor();
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("No domain element for view with visualID = " + 1000); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getIfLink_4004Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.IfLink_4004,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(IfLinkTipEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 6001); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getNodeLink_4001Text(View view) {
		return ""; //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private String getEntityExceptionlink_4007Text(View view) {
		return ""; //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private String getBixref_2012Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.Bixref_2012,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(BixrefNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5012); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getFor_2004Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.For_2004,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(ForNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5004); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getIf_2005Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.If_2005,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(IfNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5005); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getBlank_2007Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.Blank_2007,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(BlankNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5007); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getStart_2001Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.Start_2001,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(StartLabelEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5001); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getTranscommit_2009Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.Transcommit_2009,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(TranscommitNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5009); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getTransrollback_2010Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.Transrollback_2010,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(TransrollbackNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5010); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getAssignment_2003Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.Assignment_2003,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(AssignmentNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5003); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getEntityBreak_4006Text(View view) {
		return ""; //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private String getEnd_2002Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.End_2002,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(EndLabelEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5002); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getForEndlink_4003Text(View view) {
		return ""; //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private String getTransaction_2008Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.Transaction_2008,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(TransactionNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5008); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getCall_2006Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.Call_2006,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(CallNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5006); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getCustom_2011Text(View view) {
		IParser parser = LogicParserProvider.getParser(LogicElementTypes.Custom_2011,
				view.getElement() != null ? view.getElement() : view,
				LogicVisualIDRegistry.getType(CustomNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			LogicDiagramEditorPlugin.getInstance().logError("Parser was not found for label " + 5011); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getIfElselink_4002Text(View view) {
		return ""; //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private String getUnknownElementText(View view) {
		return "<UnknownElement Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	/**
	 * @generated
	 */
	private String getUnresolvedDomainElementProxyText(View view) {
		return "<Unresolved domain element Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	/**
	 * @generated
	 */
	public void init(ICommonContentExtensionSite aConfig) {
	}

	/**
	 * @generated
	 */
	public void restoreState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public void saveState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public String getDescription(Object anElement) {
		return null;
	}

	/**
	 * @generated
	 */
	private boolean isOwnView(View view) {
		return FlowEditPart.MODEL_ID.equals(LogicVisualIDRegistry.getModelID(view));
	}

}
