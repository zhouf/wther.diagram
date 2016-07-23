package com.sunsheen.jfids.studio.wther.diagram.dialog.proposal;

import org.eclipse.swt.widgets.Control;

import com.sunsheen.jfids.studio.dialog.proposal.AbstractProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.IProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;

public class MapPutRender extends AbstractProposalRender implements IProposalRender{
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
		String varStoreStr = (String) this.params.get("varstore");
		VarStore varStore = new VarStore(varStoreStr);
		String[] retArr = {};
		if(varStore.toString().equals("")){
			return retArr;
		}
		String varList[] = varStore.toString().split("\\|");
		String pullDown = "";
		for(int i = 0;i< varList.length;i++){
			String[] varThis = varList[i].split(":");
			if(varThis[1].equals("java.util.HashMap")||varThis[1].equals("HashMap")){
				if(pullDown.equals("")){
					pullDown = varThis[0];
				}else{
					pullDown = pullDown+","+varThis[0];
				}
			}
		}
		retArr = pullDown.split(",");
		return retArr;
	}
}
