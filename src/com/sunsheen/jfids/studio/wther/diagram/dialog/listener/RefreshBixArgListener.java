package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.STableButtonOnClickListener;
import com.sunsheen.jfids.studio.logging.Log;

public class RefreshBixArgListener implements STableButtonOnClickListener {

	@Override
	public void run(Map<String, Widget> coms,Map<String,Object> params,Event e,String currControl,String tableKey) {
		String external = (String) params.get("ext");
		external = (external==null? "" : external);
		String bixFilePath = external.substring(external.indexOf("/")+1);
		
		IEditorPart  editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IProject activeProject = null;
		if(editorPart  != null){
		    IFileEditorInput input = (IFileEditorInput)editorPart.getEditorInput() ;
		    IFile file = input.getFile();
		    activeProject = file.getProject();
		}
		//加上resources前缀
		IResource res =  activeProject.findMember(bixFilePath);
		if(res!=null){
			String fname = res.getLocation().toString();
			if(fname!=null && fname.endsWith(".bix")){
				CallButtonListener.putFileInfoIntoTable(coms, params, fname);
			}
		}else{
			Log.error("RefreshBixArgListener.run() 找不到文件:" + bixFilePath);
		}
	}
}
