package com.sunsheen.jfids.studio.wther.diagram.dialog.proposal;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;

import com.sunsheen.jfids.studio.dialog.proposal.AbstractProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.IProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarItemEntity;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;

public class ForDialogProposalRender extends AbstractProposalRender implements IProposalRender{
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(ForDialogProposalRender.class.getName());
	
	@Override
	public SSContentProposalAdapter initAdapter(Control widget) {
		log.info("ForDialogProposalRender.initAdapter()->widget:" + widget);
		super.init(widget);
		return adapter;
	}
	
	/**
	 * 获取变量提示所显示的数据
	 * @return
	 */
	public String[] getPopupData(){
		VarStore varStore = (VarStore) this.params.get("varstore");
		String[] retArr = getIntProposal(varStore);
		//log.debug("ForDialogProposalRender.getPopupData()->retArr.length:" + retArr.length);
		
		return retArr;
	}
	
	/**
	 * 从变量集中过滤出整型数据，用于提供用户选择循环次数
	 * @param varStore
	 * @return
	 */
	private String[] getIntProposal(VarStore varStore){
		List<String> retList = new ArrayList<String>();
		for(VarItemEntity item : varStore.listAll()){
			String varType = item.getVarType();
			if("int".equals(varType)){
				retList.add(item.getVarName() + " " + varType);
			}else if(!JavaTypeUtil.isPrimitiveType(varType)){
				try {
					Class<?> typeClass = JavaTypeUtil.findClassType(varType);
					
					for(Method m : typeClass.getMethods()){
						//过滤public 返回int且参数个数为0的方法，且不是hashCode方法，hashCode方法对for循环无意义
						if(Modifier.isPublic(m.getModifiers()) && "int".equals(m.getGenericReturnType().toString()) && m.getParameterTypes().length==0 && !"hashCode".equals(m.getName())){
							retList.add(item.getVarName() + "."+m.getName()+"() " + varType);
						}
					}
				} catch (ClassNotFoundException e) {
					log.error("ForDialogProposalRender.getIntProposal()->将字串" + varType + "转换为类型异常:" + e.toString());
				}
				
			}
		}
		return retList.toArray(new String[]{});
	}
}
