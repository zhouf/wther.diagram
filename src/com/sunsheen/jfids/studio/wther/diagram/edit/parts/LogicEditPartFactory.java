package com.sunsheen.jfids.studio.wther.diagram.edit.parts;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry;

/**
 * @generated
 */
public class LogicEditPartFactory implements EditPartFactory {

	/**
	 * @generated
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof View) {
			View view = (View) model;
			switch (LogicVisualIDRegistry.getVisualID(view)) {

			case FlowEditPart.VISUAL_ID:
				return new FlowEditPart(view);

			case StartEditPart.VISUAL_ID:
				return new StartEditPart(view);

			case StartLabelEditPart.VISUAL_ID:
				return new StartLabelEditPart(view);

			case EndEditPart.VISUAL_ID:
				return new EndEditPart(view);

			case EndLabelEditPart.VISUAL_ID:
				return new EndLabelEditPart(view);

			case AssignmentEditPart.VISUAL_ID:
				return new AssignmentEditPart(view);

			case AssignmentNameEditPart.VISUAL_ID:
				return new AssignmentNameEditPart(view);

			case CustomEditPart.VISUAL_ID:
				return new CustomEditPart(view);

			case CustomNameEditPart.VISUAL_ID:
				return new CustomNameEditPart(view);

			case BlankEditPart.VISUAL_ID:
				return new BlankEditPart(view);

			case BlankNameEditPart.VISUAL_ID:
				return new BlankNameEditPart(view);

			case ForEditPart.VISUAL_ID:
				return new ForEditPart(view);

			case ForNameEditPart.VISUAL_ID:
				return new ForNameEditPart(view);

			case IfEditPart.VISUAL_ID:
				return new IfEditPart(view);

			case IfNameEditPart.VISUAL_ID:
				return new IfNameEditPart(view);

			case CallEditPart.VISUAL_ID:
				return new CallEditPart(view);

			case CallNameEditPart.VISUAL_ID:
				return new CallNameEditPart(view);

			case BixrefEditPart.VISUAL_ID:
				return new BixrefEditPart(view);

			case BixrefNameEditPart.VISUAL_ID:
				return new BixrefNameEditPart(view);

			case TransactionEditPart.VISUAL_ID:
				return new TransactionEditPart(view);

			case TransactionNameEditPart.VISUAL_ID:
				return new TransactionNameEditPart(view);

			case TranscommitEditPart.VISUAL_ID:
				return new TranscommitEditPart(view);

			case TranscommitNameEditPart.VISUAL_ID:
				return new TranscommitNameEditPart(view);

			case TransrollbackEditPart.VISUAL_ID:
				return new TransrollbackEditPart(view);

			case TransrollbackNameEditPart.VISUAL_ID:
				return new TransrollbackNameEditPart(view);

			case NodeLinkEditPart.VISUAL_ID:
				return new NodeLinkEditPart(view);

			case EntityExceptionlinkEditPart.VISUAL_ID:
				return new EntityExceptionlinkEditPart(view);

			case IfElselinkEditPart.VISUAL_ID:
				return new IfElselinkEditPart(view);

			case ForEndlinkEditPart.VISUAL_ID:
				return new ForEndlinkEditPart(view);

			case EntityBreakEditPart.VISUAL_ID:
				return new EntityBreakEditPart(view);

			case IfLinkEditPart.VISUAL_ID:
				return new IfLinkEditPart(view);

			case IfLinkTipEditPart.VISUAL_ID:
				return new IfLinkTipEditPart(view);

			}
		}
		return createUnrecognizedEditPart(context, model);
	}

	/**
	 * @generated
	 */
	private EditPart createUnrecognizedEditPart(EditPart context, Object model) {
		// Handle creation of unrecognized child node EditParts here
		return null;
	}

	/**
	 * @generated
	 */
	public static CellEditorLocator getTextCellEditorLocator(ITextAwareEditPart source) {
		if (source.getFigure() instanceof WrappingLabel)
			return new TextCellEditorLocator((WrappingLabel) source.getFigure());
		else {
			return new LabelCellEditorLocator((Label) source.getFigure());
		}
	}

	/**
	 * @generated
	 */
	static private class TextCellEditorLocator implements CellEditorLocator {

		/**
		 * @generated
		 */
		private WrappingLabel wrapLabel;

		/**
		 * @generated
		 */
		public TextCellEditorLocator(WrappingLabel wrapLabel) {
			this.wrapLabel = wrapLabel;
		}

		/**
		 * @generated
		 */
		public WrappingLabel getWrapLabel() {
			return wrapLabel;
		}

		/**
		 * @generated
		 */
		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getWrapLabel().getTextBounds().getCopy();
			getWrapLabel().translateToAbsolute(rect);
			if (!text.getFont().isDisposed()) {
				if (getWrapLabel().isTextWrapOn() && getWrapLabel().getText().length() > 0) {
					rect.setSize(new Dimension(text.computeSize(rect.width, SWT.DEFAULT)));
				} else {
					int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
					rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr * 2, 0));
				}
			}
			if (!rect.equals(new Rectangle(text.getBounds()))) {
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}

	/**
	 * @generated
	 */
	private static class LabelCellEditorLocator implements CellEditorLocator {

		/**
		 * @generated
		 */
		private Label label;

		/**
		 * @generated
		 */
		public LabelCellEditorLocator(Label label) {
			this.label = label;
		}

		/**
		 * @generated
		 */
		public Label getLabel() {
			return label;
		}

		/**
		 * @generated
		 */
		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getLabel().getTextBounds().getCopy();
			getLabel().translateToAbsolute(rect);
			if (!text.getFont().isDisposed()) {
				int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
				rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr * 2, 0));
			}
			if (!rect.equals(new Rectangle(text.getBounds()))) {
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}
}
