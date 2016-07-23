package com.sunsheen.jfids.studio.wther.diagram.creation;


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateStatus;
import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.run.Constants;
import com.sunsheen.jfids.studio.run.ui.RunMessages;
import com.sunsheen.jfids.studio.run.ui.wizards.creation.BaseNewResourceWizard;
import com.sunsheen.jfids.studio.run.ui.wizards.creation.BaseNewWizardPage.NewResourceValidater;
import com.sunsheen.jfids.studio.run.ui.wizards.creation.SimpleNewWizardPage;
import com.sunsheen.jfids.studio.run.utils.ProjectPathUtils;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorUtil;

/**
 * 业务逻辑流创建向导
 * 
 * @author litao
 * 
 */
public class NewBixWizard extends BaseNewResourceWizard {

	private NewResourceValidater validater = new NewResourceValidater() {
		@Override
		public ValidateStatus validate(IContainer container, String newResourceName, ValidateStatus status) {
			int cp = ProjectPathUtils.compareWithSrcAndPagePath(container);
			if (cp == ProjectPathUtils.CPAGE_RESOURCES_BROTHER_SRC_CHILD) {
				if (Constants.FILE_EXT_BIX.equals(container.getFileExtension())) {
					return status;
				}
			}
			status.setStatus(ValidateStatus.FAILURE);
			status.setMessages(new String[] { RunMessages.Creation_NewBixWizard_containerNotSupport });
			return status;
		}
	};

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setWindowTitle(RunMessages.Creation_NewBixWizard_title);
	}

	@Override
	public void addPages() {
		fPage = new SimpleNewWizardPage(selection, RunMessages.Creation_NewBixWizard_page_containerName,
				RunMessages.Creation_NewBixWizard_page_newResourceName, null, false);
		fPage.setTitle(RunMessages.Creation_NewBixWizard_page_title);
		fPage.setDescription(RunMessages.Creation_NewBixWizard_page_description);
//		fPage.setImageDescriptor(LogicImageUtil.getInstance().getDescriptor("/icons/obj16/businesslogicdir.png")); //$NON-NLS-1$
		fPage.setValidater(validater);
		fPage.setExtention(Constants.FILE_EXT_BIX);
		fPage.setMode(SimpleNewWizardPage.MODE_FILE);
		//		page.setHelpContextId("com.sunsheen.jfids.studio.help.webNewBix"); //$NON-NLS-1$
		addPage(fPage);
	}

	@Override
	public void finishPage(IProgressMonitor monitor) throws InterruptedException {
		String p = "platform:/resource" + fPage.getNewFile().getFullPath().toString(); //$NON-NLS-1$
		URI uri = URI.createURI(p);
		monitor.worked(30);
		final Resource diagram = LogicDiagramEditorUtil.createDiagram(uri, new NullProgressMonitor());
		monitor.worked(60);
		if (diagram != null) {
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					try {
						LogicDiagramEditorUtil.openDiagram(diagram);
					} catch (PartInitException e) {
						Log.error(e.getMessage(), e);
						ErrorDialog.openError(Display.getDefault().getActiveShell(), "", //$NON-NLS-1$
								null, e.getStatus());
					}
				}
			});
		}
		monitor.worked(10);
	}

}
