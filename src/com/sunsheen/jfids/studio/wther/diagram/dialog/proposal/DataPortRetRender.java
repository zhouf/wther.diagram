package com.sunsheen.jfids.studio.wther.diagram.dialog.proposal;



import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

import com.sunsheen.jfids.studio.dialog.proposal.AbstractProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.IProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarItemEntity;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;

/**
 * DataPort对话框中返回参数的提示信息
 * @author Leo
 *
 */
public class DataPortRetRender extends AbstractProposalRender implements IProposalRender{
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
		//获取返回类型
		Combo comboRetType = (Combo)this.coms.get("retType");
		String retTypeVal = comboRetType.getText();
		
		VarStore varStore = (VarStore) this.params.get("varstore");
		List<String> list = new ArrayList<String>();
		String[] retArr = {};
		for(VarItemEntity entity : varStore.listAll()){
			String varType = entity.getVarType();
			if(JavaTypeUtil.typeMatch(varType, retTypeVal)){
				list.add(entity.getVarName() + " " + varType);
			}
		}
		retArr = list.toArray(new String[]{});
		return retArr;
	}
}
