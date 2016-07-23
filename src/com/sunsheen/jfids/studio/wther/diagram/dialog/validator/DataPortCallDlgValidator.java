package com.sunsheen.jfids.studio.wther.diagram.dialog.validator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateListener;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateStatus;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;

/**
 * 这是一个对赋值节点的验证
 * 
 * @author zhoufeng Date:2013-6-20
 */
public class DataPortCallDlgValidator implements ValidateListener {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(DataPortCallDlgValidator.class.getName());

	@Override
	public ValidateStatus validate(Map<String, Widget> coms, Map<String, Object> param) {
		log.debug("DataPortCallDlgValidator.validate()->:");
		ValidateStatus status = new ValidateStatus();
		VarStore varStore = (VarStore) param.get("varstore");

		AgileGrid table = (AgileGrid) coms.get("table");
		InnerTableContentProvider provider = (InnerTableContentProvider) table.getContentProvider();
		int providerDataSize = provider.data.size();
		if (providerDataSize > 0) {
			// 有数据，获取表格数据
			Map<Integer, Map<Integer, Object>> map = provider.data.get(0);
			checkEachTable(status, varStore, map, true);
			
			if(providerDataSize>1 && status.getStatus()==ValidateStatus.SUCCESS){
				Map<Integer, Map<Integer, Object>> map2 = provider.data.get(1);
				checkEachTable(status, varStore, map2, false);
			}

		} else {
			// 没有数据，不进行验证
			status.setStatus(ValidateStatus.SUCCESS);
		}
		
		//验证返回
		if(status.getStatus()==ValidateStatus.SUCCESS){
			//如果之前的验证通过，则继续验证返回设置
			String retValue = ((Text)coms.get("retValue")).getText();
			String retType = ((Combo)coms.get("retType")).getText();
			if(StringUtils.isNotEmpty(retValue)){
				//如果返回有设置，则验证类型匹配
				String vtype = varStore.findVarType(retValue);
				if(StringUtils.isNotEmpty(vtype)){
					if(!JavaTypeUtil.typeMatch(vtype, retType)){
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { "返回变量"+retValue+"与类型"+retType+"不匹配" });
					}
				}else{
					// FIXME 可改为提示定义
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "返回变量"+retValue+"未定义" });
				}
			}
			
		}

		return status;
	}

	private void checkEachTable(ValidateStatus status, VarStore varStore, Map<Integer, Map<Integer, Object>> map,boolean mustHaveVal) {
		List<Integer> keys = Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
		Iterator<Integer> iterator = keys.iterator();
		while (iterator.hasNext()) {
			Map<Integer, Object> row = map.get(iterator.next());
			//String seq = getRowData(row,0);	//序号
			String varType = getRowData(row,1);// 参数类型，变量，常量，表达式
			String varName = getRowData(row,2);// 填写的参数变量名
			
			if(StringUtils.isEmpty(varName)){
				status.setStatus(ValidateStatus.FAILURE);
				status.setMessages(new String[] { "变量名未填写" });
				break;
			}
			if(StringUtils.isEmpty(varType)){
				status.setStatus(ValidateStatus.FAILURE);
				status.setMessages(new String[] { "变量类型未设置" });
				break;
			}
			
		
			if (!varStore.contains(varName)) {
				// 如果类型为变量，检查设置的值是否有被定义
				status.setStatus(ValidateStatus.FAILURE);
				status.setMessages(new String[] { "变量【" + varName + "】未定义" });
				break;
			}
			
			
		}
	}

	private String getRowData(Map<Integer, Object> row,int i) {
		Object obj = row.get(i);
		if(obj!=null){
			return obj.toString();
		}else{
			return "";
		}
	}

}
