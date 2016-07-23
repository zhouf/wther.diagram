package com.sunsheen.jfids.studio.wther.diagram.dialog.proposal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.proposal.AbstractProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.IProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;

/*
 * 只处理返回的提示内容，返回的提示与参数的提示变量类型相反
 */
public class ReturnProposalRender extends AbstractProposalRender implements IProposalRender {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(ReturnProposalRender.class.getName());
	
	private String filterStr = "";
	
	public void setFilterStr(String filterStr) {
		this.filterStr = filterStr;
	}

	@Override
	public SSContentProposalAdapter initAdapter(Control widget) {
		super.init(widget);
		this.adapter.setLabelProvider(new VarLabelProvider());
		return adapter;
	}
	
	public SSContentProposalAdapter initAdapter(Control widget, Map<String, Widget> coms, Map<String, Object> params) {
		//设置ContentProposalProvider，如果没有设置则使用默认的SimpleContentProposalProvider
		//setContentProvider(new MyContentProposalProvider(getPopupData()));
		super.init(widget,coms,params);
		this.adapter.setLabelProvider(new VarLabelProvider());
		return adapter;
	}


	
	/**
	 * 获取变量提示所显示的数据
	 * @return
	 */
	public String[] getPopupData(){
		String retArr[] = new String[]{};
		//根据filter过滤提示内容
		retArr = (String[]) this.params.get("proposalData");
		List<String> tipList = new ArrayList<String>();
		if(StringUtils.isNotBlank(filterStr)){
			for(String eachVar : retArr){
				if(typeMatch(eachVar,filterStr)){
					tipList.add(eachVar);
				}
			}
			return tipList.toArray(new String[]{});
		}else{
			return retArr;
		}
	}

	/**
	 * 比较提示类型是否是当前类型
	 * @param varStr 提示数据类型，如：varName java.lang.String
	 * @param requestType 需要的数据类型，如：String
	 * @return boolean 匹配返回true
	 */
	private boolean typeMatch(String varStr, String requestType) {
		String varType = "";
		boolean retVal = false;
		//从var中分离出类型
		int blankIndex = varStr.indexOf(" ");
		int endIndex = varStr.indexOf("&");
		//空格和&是在proposal中的变量
		if(blankIndex>0){
			if(endIndex>0){
				varType = varStr.substring(blankIndex+1,endIndex);
			}else{
				varType = varStr.substring(blankIndex+1);
			}
			retVal = JavaTypeUtil.typeMatch(requestType,varType);
		}else{
			//没有空格，则说明没有类型，直接给出提示
			retVal = true;
		}
		return retVal;
	}
	
}
