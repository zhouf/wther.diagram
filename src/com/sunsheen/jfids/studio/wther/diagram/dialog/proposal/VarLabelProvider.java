package com.sunsheen.jfids.studio.wther.diagram.dialog.proposal;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.sunsheen.jfids.studio.dialog.image.DialogImageUtil;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposal;

public class VarLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(Object element) {
		String from = ((SSContentProposal)element).getFrom();
		Image retImg = DialogImageUtil.getInstance().getImage("icons/obj16/var_item.png");
		if(from.endsWith(ConstantProposalType.ARG_TYPE)){
			retImg = DialogImageUtil.getInstance().getImage("icons/obj16/arg_item.png");
		}else if(from.endsWith(ConstantProposalType.RET_TYPE)){
			retImg = DialogImageUtil.getInstance().getImage("icons/obj16/ret_item.png");
		}else if(from.endsWith(ConstantProposalType.GLOBAL_TYPE)){
			retImg = DialogImageUtil.getInstance().getImage("icons/obj16/proposal_global.png");
		}
		return retImg;
	}

	@Override
	public String getText(Object element) {
		String label = ((ContentProposal)element).getLabel();
		String items[] = label.split(" ");
		if(items.length>1 && label.startsWith("this.")){
			return items[1];
		}
		return label;
	}
}
