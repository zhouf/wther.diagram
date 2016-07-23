package com.sunsheen.jfids.studio.wther.diagram.part;

import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramActionBarContributor;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @generated
 */
public class LogicDiagramActionBarContributor extends DiagramActionBarContributor {

	/**
	 * @generated
	 */
	protected Class getEditorClass() {
		return LogicDiagramEditor.class;
	}

	/**
	 * 
	 * @generated 
	 */
	protected String getEditorId() {
		return LogicDiagramEditor.ID;
	}

	/**
	 * 删除不需要的工具按钮
	 * @generated NOT
	 */
	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
		// print preview
		IMenuManager fileMenu = bars.getMenuManager().findMenuUsingPath(IWorkbenchActionConstants.M_FILE);
		assert fileMenu != null;
		fileMenu.remove("pageSetupAction"); //$NON-NLS-1$

		//----------------
		IToolBarManager toolBarManager = bars.getToolBarManager();
		toolBarManager.remove("toolbarFontGroup");
		toolBarManager.remove("fontNameContributionItem");
		toolBarManager.remove("fontSizeContributionItem");
		toolBarManager.remove("fontBoldAction");
		toolBarManager.remove("fontItalicAction");
		toolBarManager.remove("toolbarColorLineGroup");
		toolBarManager.remove("fontColorContributionItem");
		toolBarManager.remove("fillColorContributionItem");
		toolBarManager.remove("lineColorContributionItem");
		toolBarManager.remove("routerMenu");
		toolBarManager.remove("toolbarCopyAppearanceGroup");
		toolBarManager.remove("copyAppearancePropertiesAction");
		toolBarManager.remove("toolBarViewGroup");
		//toolBarManager.remove("selectMenu");
		//toolBarManager.remove("arrangeMenu");
		//toolBarManager.remove("alignMenu");
		toolBarManager.remove("toolbarEditGroup");
		toolBarManager.remove("toolbarFormatGroup");
		toolBarManager.remove("autoSizeAction");
		toolBarManager.remove("toolbarFilterGroup");
		toolBarManager.remove("showConnectorLabels");
		toolBarManager.remove("hideConnectorLabels");
		toolBarManager.remove("compartmentMenu");
		toolBarManager.remove("toolbarNavigateGroup");
		toolBarManager.remove("zoomContributionItem");//放大缩小启用
		toolBarManager.remove("toolbarRestGroup");
		toolBarManager.remove("toolbarAdditions");

		//----------------

		//------------ 删除主菜单上的“图”菜单----
		IContributionItem[] items = bars.getMenuManager().getItems();
		for (IContributionItem i : items) {
			if (i != null && "diagramMenu".equals(i.getId())) {
				SubContributionItem diagramMenu = (SubContributionItem) i;
				IContributionManager diagramMenuMagager = (IContributionManager) diagramMenu.getInnerItem();
				diagramMenuMagager.remove(i);
				IContributionItem[] subDiagramsMenu = diagramMenuMagager.getItems();

				for (IContributionItem j : subDiagramsMenu) {

					//删除arrangeMenu
					//if (j.getId().equals("arrangeMenu"))
					//{
					diagramMenuMagager.remove(j);
					//}
				}
			}
		}
		//----------------

		IMenuManager editMenu = bars.getMenuManager().findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		assert editMenu != null;
		if (editMenu.find("validationGroup") == null) { //$NON-NLS-1$
			editMenu.add(new GroupMarker("validationGroup")); //$NON-NLS-1$
		}
		IAction validateAction = new ValidateAction(page);
		editMenu.appendToGroup("validationGroup", validateAction); //$NON-NLS-1$
	}
}
