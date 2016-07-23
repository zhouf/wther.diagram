package com.sunsheen.jfids.studio.wther.diagram.dialog.popup;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;
import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicImageUtil;
/**
 * 这是一个处理变量选择弹出选择项提示框的一个类
 * @author zhouf
 */
public class ItemSelectDialog {
	private String retVal = "";

	public ItemSelectDialog(String xmlfile,String initText){
		Map<String, Object> params = new HashMap<String, Object>();
		IEditorPart editorPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();

		String path = "",filePath = "";
		IProject activeProject = null;
		if (editorPart != null) {
			IFileEditorInput input = (IFileEditorInput) editorPart
					.getEditorInput();
			IFile file = input.getFile();
			activeProject = file.getProject();
			path = activeProject.getFolder("resources").getLocation().toString();
			filePath=file.getLocation().toString();
		}

		params.put("path", path);
		params.put("filePath", filePath);
		params.put("beanName", initText);
		params.put("showText", "");
		DialogConfigParser parser = new DialogConfigParser(
				ItemSelectDialog.class.getResourceAsStream(xmlfile),
				new Shell(), params);
		SDialog dialog = parser.getDialog();
		params.put("object", dialog);

		dialog.show(LogicImageUtil.getInstance(),
				LogicDiagramEditorPlugin.getImage("icons/dialog_logo.gif"),
				"对象选择器");

		// 处理获得的文本
		retVal = (String) params.get("showText");
	}

	public String getRetVal() {
		return retVal;
	}
	
}
