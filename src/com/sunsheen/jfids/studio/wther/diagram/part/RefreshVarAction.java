package com.sunsheen.jfids.studio.wther.diagram.part;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.BglbFileEntity;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.FlowEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.GlobalBixUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.RefreshVarTreeUtil;

/**
 * 变量刷新菜单
 * 
 * @author lz
 * 
 */
public class RefreshVarAction extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editPart = HandlerUtil.getActiveEditorChecked(event);
		Object obj = HandlerUtil.getCurrentSelection(event);

		Object select = null;
		if (obj != null) {
			select = ((StructuredSelection) obj).getFirstElement();
		}
		if (select instanceof FlowEditPart) {
			IFile inputFile = ((FileEditorInput) HandlerUtil.getActiveEditor(event).getEditorInput()).getFile();
			// 编辑器获得焦点，向构件库中传输页面中已定义的变量
			FlowImpl flow = (FlowImpl) ((FlowEditPart) select).resolveSemanticElement();
			BglbFileEntity globalEntity = getGlobalEntity(inputFile);
			RefreshVarTreeUtil.initGlobalVarToMap(globalEntity);
			RefreshVarTreeUtil.refresh(flow, true);
		}
		return null;
	}

	/**
	 * 获得一个全局定义的实体对象
	 * 
	 * @return BglbFileEntity
	 */
	private BglbFileEntity getGlobalEntity(IFile file) {
		BglbFileEntity retObj = null;
		IFile globalFile = GlobalBixUtil.getGlobalFile(file);
		if (globalFile != null && globalFile.exists()) {
			retObj = GlobalBixUtil.getGlobalEntityFromIFile(globalFile);
		}

		return retObj;
	}
	
	
	@Override
	public boolean isEnabled() {
		return true;
	}
}
