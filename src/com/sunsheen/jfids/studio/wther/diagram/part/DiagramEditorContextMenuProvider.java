package com.sunsheen.jfids.studio.wther.diagram.part;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.providers.DiagramContextMenuProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchPart;

import com.sunsheen.jfids.studio.logic.impl.NodeImpl;

/**
 * @generated
 */
public class DiagramEditorContextMenuProvider extends DiagramContextMenuProvider {

	/**
	 * @generated
	 */
	private IWorkbenchPart part;

	/**
	 * @generated
	 */
	private DeleteElementAction deleteAction;

	//	private EnableTryAction enableTryAction;
	//	private DisableTryAction disableTryAction;

	/**
	 * @generated
	 */
	public DiagramEditorContextMenuProvider(IWorkbenchPart part, EditPartViewer viewer) {
		super(part, viewer);
		this.part = part;
		deleteAction = new DeleteElementAction(part);
		deleteAction.init();
	}

	/**
	 * @generated
	 */
	public void dispose() {
		if (deleteAction != null) {
			deleteAction.dispose();
			deleteAction = null;
		}
		super.dispose();
	}

	/**
	 * @generated NOT
	 */
	public void buildContextMenu(final IMenuManager menu) {
		getViewer().flush();

		try {
			TransactionUtil.getEditingDomain((EObject) getViewer().getContents().getModel()).runExclusive(
					new Runnable() {

						public void run() {
							ContributionItemService.getInstance().contributeToPopupMenu(
									DiagramEditorContextMenuProvider.this, part);
							//menu.remove(ActionIds.ACTION_DELETE_FROM_MODEL);
							//menu.appendToGroup("editGroup", deleteAction);

							menu.remove("umlAddGroup");
							menu.remove("addGroup");
							menu.remove("diagramAddMenu");
							menu.remove("navigateGroup");
							menu.remove("navigateMenu");
							//menu.remove("fileGroup");
							//menu.remove("fileMenu");
							menu.remove("editGroup");
							//menu.remove("editMenu");
							//menu.remove("deleteFromModelAction");
							//menu.remove("findGroup");
							//menu.remove("filterFormatGroup");
							//menu.remove("selectMenu");
							menu.remove("toolbarArrangeAllAction");
							menu.remove("filtersMenu");
							//menu.remove("viewGroup");
							//menu.remove("viewMenu");
							//menu.remove("zoomContributionItem");
							menu.remove("restGroup");
							menu.remove("additions");
							//menu.remove("propertiesGroup");
							//menu.remove("showPropertiesViewAction");
							menu.remove("properties");

							//---------------------------

						}
					});
		} catch (Exception e) {
			LogicDiagramEditorPlugin.getInstance().logError("Error building context menu", e);
		}
	}

	@Override
	public IContributionItem[] getItems() {
		IContributionItem[] items = super.getItems();

		filterItems(items);
		return items;
	}

	private void filterItems(IContributionItem[] items) {
		List selectedEditParts = getViewer().getSelectedEditParts();
		boolean tryFlag = false;
		if (selectedEditParts.size() > 0) {
			//有选择
			EditPart editPart = (EditPart) selectedEditParts.get(0);
			View view = (View) editPart.getModel();
			EObject element = view.getElement();
			if (element instanceof NodeImpl) {
				NodeImpl node = (NodeImpl) element;
				if (node != null) {
					tryFlag = node.isStarttry();
				}
			}
		}
		for (IContributionItem contributionItem : items) {
			if (contributionItem instanceof MenuManager) {
				filterItems(((MenuManager) contributionItem).getItems());
			} else {
				if ("com.sunsheen.jfids.studio.wther.diagram.enableTryAction".equals(contributionItem.getId())) {
					contributionItem.setVisible(!tryFlag);
				} else if ("com.sunsheen.jfids.studio.wther.diagram.disableTryAction".equals(contributionItem.getId())) {
					contributionItem.setVisible(tryFlag);
				}
			}

		}

	}
}
