package com.sunsheen.jfids.studio.wther.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;

import com.sunsheen.jfids.studio.logic.LogicPackage;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.AssignmentNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BixrefNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BlankNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.CallNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.CustomNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfLinkTipEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransactionNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TranscommitNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.TransrollbackNameEditPart;
import com.sunsheen.jfids.studio.wther.diagram.parsers.MessageFormatParser;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry;

/**
 * @generated
 */
public class LogicParserProvider extends AbstractProvider implements IParserProvider {

	/**
	 * @generated
	 */
	private IParser assignmentName_5003Parser;

	/**
	 * @generated
	 */
	private IParser getAssignmentName_5003Parser() {
		if (assignmentName_5003Parser == null) {
			EAttribute[] features = new EAttribute[] { LogicPackage.eINSTANCE.getNode_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			assignmentName_5003Parser = parser;
		}
		return assignmentName_5003Parser;
	}

	/**
	 * @generated
	 */
	private IParser customName_5011Parser;

	/**
	 * @generated
	 */
	private IParser getCustomName_5011Parser() {
		if (customName_5011Parser == null) {
			EAttribute[] features = new EAttribute[] { LogicPackage.eINSTANCE.getNode_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			customName_5011Parser = parser;
		}
		return customName_5011Parser;
	}

	/**
	 * @generated
	 */
	private IParser blankName_5007Parser;

	/**
	 * @generated
	 */
	private IParser getBlankName_5007Parser() {
		if (blankName_5007Parser == null) {
			EAttribute[] features = new EAttribute[] { LogicPackage.eINSTANCE.getNode_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			blankName_5007Parser = parser;
		}
		return blankName_5007Parser;
	}

	/**
	 * @generated
	 */
	private IParser forName_5004Parser;

	/**
	 * @generated
	 */
	private IParser getForName_5004Parser() {
		if (forName_5004Parser == null) {
			EAttribute[] features = new EAttribute[] { LogicPackage.eINSTANCE.getNode_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			forName_5004Parser = parser;
		}
		return forName_5004Parser;
	}

	/**
	 * @generated
	 */
	private IParser ifName_5005Parser;

	/**
	 * @generated
	 */
	private IParser getIfName_5005Parser() {
		if (ifName_5005Parser == null) {
			EAttribute[] features = new EAttribute[] { LogicPackage.eINSTANCE.getNode_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			ifName_5005Parser = parser;
		}
		return ifName_5005Parser;
	}

	/**
	 * @generated
	 */
	private IParser callName_5006Parser;

	/**
	 * @generated
	 */
	private IParser getCallName_5006Parser() {
		if (callName_5006Parser == null) {
			EAttribute[] features = new EAttribute[] { LogicPackage.eINSTANCE.getNode_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			callName_5006Parser = parser;
		}
		return callName_5006Parser;
	}

	/**
	 * @generated
	 */
	private IParser bixrefName_5012Parser;

	/**
	 * @generated
	 */
	private IParser getBixrefName_5012Parser() {
		if (bixrefName_5012Parser == null) {
			EAttribute[] features = new EAttribute[] { LogicPackage.eINSTANCE.getNode_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			bixrefName_5012Parser = parser;
		}
		return bixrefName_5012Parser;
	}

	/**
	 * @generated
	 */
	private IParser transactionName_5008Parser;

	/**
	 * @generated
	 */
	private IParser getTransactionName_5008Parser() {
		if (transactionName_5008Parser == null) {
			EAttribute[] features = new EAttribute[] { LogicPackage.eINSTANCE.getNode_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			transactionName_5008Parser = parser;
		}
		return transactionName_5008Parser;
	}

	/**
	 * @generated
	 */
	private IParser transcommitName_5009Parser;

	/**
	 * @generated
	 */
	private IParser getTranscommitName_5009Parser() {
		if (transcommitName_5009Parser == null) {
			EAttribute[] features = new EAttribute[] { LogicPackage.eINSTANCE.getNode_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			transcommitName_5009Parser = parser;
		}
		return transcommitName_5009Parser;
	}

	/**
	 * @generated
	 */
	private IParser transrollbackName_5010Parser;

	/**
	 * @generated
	 */
	private IParser getTransrollbackName_5010Parser() {
		if (transrollbackName_5010Parser == null) {
			EAttribute[] features = new EAttribute[] { LogicPackage.eINSTANCE.getNode_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			transrollbackName_5010Parser = parser;
		}
		return transrollbackName_5010Parser;
	}

	/**
	 * @generated
	 */
	private IParser ifLinkTip_6001Parser;

	/**
	 * @generated
	 */
	private IParser getIfLinkTip_6001Parser() {
		if (ifLinkTip_6001Parser == null) {
			EAttribute[] features = new EAttribute[] { LogicPackage.eINSTANCE.getIfLink_Tip() };
			MessageFormatParser parser = new MessageFormatParser(features);
			ifLinkTip_6001Parser = parser;
		}
		return ifLinkTip_6001Parser;
	}

	/**
	 * @generated
	 */
	protected IParser getParser(int visualID) {
		switch (visualID) {
		case AssignmentNameEditPart.VISUAL_ID:
			return getAssignmentName_5003Parser();
		case CustomNameEditPart.VISUAL_ID:
			return getCustomName_5011Parser();
		case BlankNameEditPart.VISUAL_ID:
			return getBlankName_5007Parser();
		case ForNameEditPart.VISUAL_ID:
			return getForName_5004Parser();
		case IfNameEditPart.VISUAL_ID:
			return getIfName_5005Parser();
		case CallNameEditPart.VISUAL_ID:
			return getCallName_5006Parser();
		case BixrefNameEditPart.VISUAL_ID:
			return getBixrefName_5012Parser();
		case TransactionNameEditPart.VISUAL_ID:
			return getTransactionName_5008Parser();
		case TranscommitNameEditPart.VISUAL_ID:
			return getTranscommitName_5009Parser();
		case TransrollbackNameEditPart.VISUAL_ID:
			return getTransrollbackName_5010Parser();
		case IfLinkTipEditPart.VISUAL_ID:
			return getIfLinkTip_6001Parser();
		}
		return null;
	}

	/**
	 * Utility method that consults ParserService
	 * @generated
	 */
	public static IParser getParser(IElementType type, EObject object, String parserHint) {
		return ParserService.getInstance().getParser(new HintAdapter(type, object, parserHint));
	}

	/**
	 * @generated
	 */
	public IParser getParser(IAdaptable hint) {
		String vid = (String) hint.getAdapter(String.class);
		if (vid != null) {
			return getParser(LogicVisualIDRegistry.getVisualID(vid));
		}
		View view = (View) hint.getAdapter(View.class);
		if (view != null) {
			return getParser(LogicVisualIDRegistry.getVisualID(view));
		}
		return null;
	}

	/**
	 * @generated
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetParserOperation) {
			IAdaptable hint = ((GetParserOperation) operation).getHint();
			if (LogicElementTypes.getElement(hint) == null) {
				return false;
			}
			return getParser(hint) != null;
		}
		return false;
	}

	/**
	 * @generated
	 */
	private static class HintAdapter extends ParserHintAdapter {

		/**
		 * @generated
		 */
		private final IElementType elementType;

		/**
		 * @generated
		 */
		public HintAdapter(IElementType type, EObject object, String parserHint) {
			super(object, parserHint);
			assert type != null;
			elementType = type;
		}

		/**
		 * @generated
		 */
		public Object getAdapter(Class adapter) {
			if (IElementType.class.equals(adapter)) {
				return elementType;
			}
			return super.getAdapter(adapter);
		}
	}

}
