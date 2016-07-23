package com.sunsheen.jfids.studio.wther.diagram.dialog.validator;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateStatus;
import com.sunsheen.jfids.studio.run.utils.ProjectUtils;
/**
 * 这是一个对引用节点的验证
 * @author zhouf
 */
public class BixrefDlgValidator extends CallDlgValidator {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(BixrefDlgValidator.class.getName());

	@Override
	public ValidateStatus validate(Map<String, Widget> coms, Map<String, Object> param) {
		ValidateStatus status = new ValidateStatus();
		
		//检查引用文件是否存在
		String ext = (String) param.get("ext");
		if(ext!=null && ext.startsWith("bixref")){
			String fileLocation = ext.substring(7);
			
			IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(ProjectUtils.getResolvedProjectFilePath(fileLocation));
			
			if (!(workspaceResource!=null && workspaceResource instanceof IFile)) {
				status.setStatus(ValidateStatus.FAILURE);
				status.setMessages(new String[] { "引用业务逻辑流不存在" });
				return status;
			}
		}
		status = super.validate(coms, param);
		
		return status;
	}
	
}
