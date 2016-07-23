package com.sunsheen.jfids.studio.wther.diagram.dialog.proposal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;

import com.sunsheen.jfids.studio.dialog.proposal.AbstractProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.IProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;
import com.sunsheen.jfids.studio.run.Constants;
import com.sunsheen.jfids.studio.run.core.extensionpoint.ResourcePromptManager;
import com.sunsheen.jfids.studio.run.core.resources.prompt.IResourcePrompt;

/**
 * 在选择服务组件时，提示服务组件里所包含的接口名
 * 
 * @author zhouf
 */
public class ComponentInterfaceProposalRender extends AbstractProposalRender implements IProposalRender {

	private List<String> list = null;

	public ComponentInterfaceProposalRender() {
		fillData();
	}

	@Override
	public SSContentProposalAdapter initAdapter(Control widget) {
		super.init(widget);
		return adapter;
	}

	private void fillData() {
		IResourcePrompt prompt = ResourcePromptManager.getResourcePrompt(Constants.FILE_EXT_SIX);
		if (prompt != null) {
			list = prompt.getService();
		}
		if (list == null) {
			list = new ArrayList<String>();
		}
	}

	/**
	 * 获取表单域的提示数据
	 * 
	 * @return
	 */
	public String[] getPopupData() {
		String retArr[] = list.toArray(new String[] {});
		return retArr;
	}
}
