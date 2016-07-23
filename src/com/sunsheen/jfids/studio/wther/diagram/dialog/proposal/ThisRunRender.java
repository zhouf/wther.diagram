package com.sunsheen.jfids.studio.wther.diagram.dialog.proposal;



import org.eclipse.swt.widgets.Control;

import com.sunsheen.jfids.studio.dialog.proposal.AbstractProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.IProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;

public class ThisRunRender extends AbstractProposalRender implements IProposalRender{
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
		VarStore varStore = (VarStore) this.params.get("varstore");
		String[] retArr = {};
		if(varStore.toString().equals("")){
			return retArr;
		}
		String varList[] = varStore.toString().split("\\|");
		String pullDown = "";
		for(int i = 0;i< varList.length;i++){
			String[] varThis = varList[i].split(":");
			if(pullDown.equals("")){
				pullDown = varThis[0];
			}else{
				pullDown = pullDown+","+varThis[0];
			}
		}
		retArr = pullDown.split(",");
		retArr = varStore.toProposalArray();
		return retArr;
	}
}
