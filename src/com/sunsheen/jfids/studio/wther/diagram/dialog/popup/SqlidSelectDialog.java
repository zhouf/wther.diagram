package com.sunsheen.jfids.studio.wther.diagram.dialog.popup;


import org.eclipse.core.resources.IFolder;

import com.sunsheen.jfids.studio.run.ui.node.MixVirtualNode;
import com.sunsheen.jfids.studio.run.ui.node.VirtualNodeNew;
import com.sunsheen.jfids.studio.ued.dialog.dataSelect.SearchResourceUI;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.ProjectInfoUtil;

public class SqlidSelectDialog {

	private String retVal = "";
	public static String SQLID = "sqlid";

	public SqlidSelectDialog(String initText) {
		ProjectInfoUtil projectInfo = new ProjectInfoUtil();
		IFolder folder = projectInfo.getCurrentFuncModelFolder();

		Object resoult = SearchResourceUI.selectDataResourceFile(projectInfo.getActiveProject(), folder, "", SQLID, 0);
		if (resoult != null && resoult instanceof VirtualNodeNew) {
			String res = "";
			VirtualNodeNew node = (VirtualNodeNew) resoult;
			res = node.getText();
			if (node instanceof MixVirtualNode) {
				MixVirtualNode mixVirtualNode = (MixVirtualNode) node;
				res = mixVirtualNode.getNamespace() + "." + res;
			}
			retVal = res;
		}
	}

	public String getRetVal() {
		return retVal;
	}
}
