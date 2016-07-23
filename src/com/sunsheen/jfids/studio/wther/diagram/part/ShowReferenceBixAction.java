package com.sunsheen.jfids.studio.wther.diagram.part;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

import com.sunsheen.jfids.studio.logic.Bixref;
import com.sunsheen.jfids.studio.run.ui.dialog.MessageNotifier;
import com.sunsheen.jfids.studio.run.utils.ProjectUtils;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BixrefEditPart;

/**
 * 这是打开BIX引用文件的右键菜单项执行的操作，用于打开引用的PIX文件
 * @author zhouf
 */
public class ShowReferenceBixAction extends AbstractHandler{

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editPart = HandlerUtil.getActiveEditorChecked(event);
		Object obj = HandlerUtil.getCurrentSelection(event);
		Shell shell = editPart.getEditorSite().getShell();
		boolean openExecuted = false;
		
		BixrefEditPart bixref = null;
		Object select = null;
		if(obj!=null){
			select = ((StructuredSelection)obj).getFirstElement();
		}
		if(select instanceof BixrefEditPart){
			bixref = (BixrefEditPart) select;
			Bixref node = (Bixref)(bixref.resolveSemanticElement());
			String ext = node.getExternal();
			String fileLocation = "";
			if(ext!=null && ext.startsWith("bixref")){
				fileLocation = ext.substring(7);
				
				IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(ProjectUtils.getResolvedProjectFilePath(fileLocation));
				if (workspaceResource instanceof IFile) {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						page.openEditor(new FileEditorInput((IFile) workspaceResource), LogicDiagramEditor.ID);
						openExecuted = true;
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(!openExecuted){
			//MessageDialog.openWarning(shell, "打开引用文件", "打开引用文件错误，找不到对应的引用文件");
			MessageNotifier.notify("打开引用文件错误，找不到对应的引用文件",MessageNotifier.WARING);
		}
		return null;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
