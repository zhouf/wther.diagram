package com.sunsheen.jfids.studio.wther.diagram.dialog.proposal;

import org.eclipse.swt.widgets.Control;

import com.sunsheen.jfids.studio.dialog.proposal.AbstractProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.IProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;

public class ColorProposalRender extends AbstractProposalRender implements IProposalRender {

	@Override
	public SSContentProposalAdapter initAdapter(Control widget) {
		super.init(widget);
		return adapter;
	}
	
	/**
	 * 获取变量提示所显示的数据
	 * @return
	 */
	public String[] getPopupData(){
		String retArr[] = new String[]{"red","green","blue"};
		return retArr;
	}
}
