package com.sunsheen.jfids.studio.wther.diagram.dialog.validator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateListener;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateStatus;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.Constant;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.InputDataValidate;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;

/**
 * 这是一个对赋值节点的验证
 * @author zhoufeng Date:2013-6-20
 */
public class CallDlgValidator implements ValidateListener {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(CallDlgValidator.class.getName());

	// 记录未定义且不选择定义的变量集合
	Set<String> ignoreDefineVarSet = new HashSet<String>();
	
	@Override
	public ValidateStatus validate(Map<String, Widget> coms, Map<String, Object> param) {
		ValidateStatus status = new ValidateStatus();
		VarStore varStore = (VarStore) param.get("varstore");
		
		AgileGrid table = (AgileGrid) coms.get("table");
		InnerTableContentProvider provider = (InnerTableContentProvider) table.getContentProvider();
		int providerDataSize = provider.data.size();
		if (providerDataSize > 0) {
			// 有数据，获取表格数据
			Map<Integer, Map<Integer, Object>> map = provider.data.get(0);
			checkEachTable(status, varStore, map,param, true);
			
			if(providerDataSize>1 && status.getStatus()==ValidateStatus.SUCCESS){
				Map<Integer, Map<Integer, Object>> map2 = provider.data.get(1);
				checkEachTable(status, varStore, map2,param, false);
			}

		} else {
			// 没有数据，不进行验证
			status.setStatus(ValidateStatus.SUCCESS);
		}

		return status;
	}



	private void checkEachTable(ValidateStatus status, VarStore varStore, Map<Integer, Map<Integer, Object>> map,Map<String, Object> param,boolean mustHaveVal) {
		List<Integer> keys = Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
		Iterator<Integer> iterator = keys.iterator();
		while (iterator.hasNext()) {
			Map<Integer, Object> row = map.get(iterator.next());
			String typeName = getRowData(row,1);// 参数类型
			String argName = getRowData(row,2);// 参数名
			String varValue = getRowData(row,3);// 填写的参数值
			String varType = getRowData(row,4);// 参数类型，变量，常量，表达式

			if(StringUtils.isEmpty(varValue)){
				if(mustHaveVal){
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "参数"+argName+"值未填写" });
					break;
				}else{
					//如果没有填写内容，是返回，允许为空，则不继续检查，直接返回
					return;
				}
			}else{
				//借用mustHaveVal来区分是参数还是返回，返回值设置里不允许有常量
				if(mustHaveVal==false && "常量".equals(varType)){
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "返回"+argName+"值类型不能为常量" });
					break;
				}
			}
//			if(StringUtils.isNotEmpty(varValue) && isNumeric && !("常量".equals(varType))){
//				status.setStatus(ValidateStatus.FAILURE);
//				status.setMessages(new String[] { "参数"+argName+"值设置【" + varValue + "】类型不正确" });
//				break;
//			}
			if("常量".equals(varType)){
				// 对数值类型，字符型，以及日期型数据允许使用常量值
				
				if(isNumberType(typeName)){
					boolean isNumeric = StringUtils.isNumeric(varValue);
					if(!isNumeric){
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { "参数"+argName+"常量值【" + varValue + "】设置不正确" });
						break;
					}
				}else if(isFloatType(typeName)){
					boolean isNumber = InputDataValidate.checkNumStrValidate(varValue);
					if(!isNumber){
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { "参数"+argName+"常量值【" + varValue + "】设置不正确" });
						break;
					}
				}else if(isDateType(typeName)){
					if(!InputDataValidate.checkDateStrValidate(varValue)){
						//日期类型
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { "参数"+argName+"日期【" + varValue + "】格式不正确" });
						break;
					}
				}else if(typeName.endsWith("String")){
					if(!validateEscape(varValue)){
						//字符串则检查转义字符
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { "参数"+argName+"值设置【" + varValue + "】包含不正确的转义字符" });
						break;
					}
				}else{
					//Other Type
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "参数"+argName+"类型【" + typeName + "】不支持常量设置" });
					break;
				}
				
				
				
			}else if ("变量".equals(varType)) {
				if(!(varStore.contains(varValue))){
					// 如果类型为变量，检查设置的值是否有在全局变量中或页面变量中被定义
					// 如果没有被定义，检查是否在需要被创建列表里
					String createStr = "";
					if(param.containsKey(Constant.NEED_CREATE_VAR)){
						createStr = (String) param.get(Constant.NEED_CREATE_VAR);
					}
					
					if(createStr.contains(varValue + ":")){
						//如果已包含在需要创建的定义中,do nothing
						log.debug("CallDlgValidator checkEachTable()-> :当前变量"+varValue+"已存在创建集合["+createStr+"]中");
						
					}else if(!ignoreDefineVarSet.contains(varValue) && InputDataValidate.checkValidSpell(varValue) && InputDataValidate.checkKeyWords(varValue)){
						if(MessageDialog.openConfirm(null, "变量未定义", "当前变量["+varValue+"]未定义，是否需要定义该变量")){
							
							//如果符合变量拼写规则，并且不是关键字，并同意创建
							//定义变量的操作，一个是放在变量定义中的字串，另一个是放在集合中的字串
							String newDefineStr = "|"+varValue+"::"+typeName+":false:true";
							String newStoreStr = "|"+varValue+":"+typeName+":_DEFINE_TYPE";
							
							if(param.containsKey(Constant.NEED_CREATE_VAR)){
								//如果已经存在变量，判断当前变量是否在需要创建列表中TODO...
								String tmpDefineStr = (String) param.get(Constant.NEED_CREATE_VAR);
								String tmpVarStoreStr = (String) param.get(Constant.NEED_VARSTORE_VAR);
								param.put(Constant.NEED_CREATE_VAR, tmpDefineStr+newDefineStr);
								param.put(Constant.NEED_VARSTORE_VAR, tmpVarStoreStr+newStoreStr);
							}else{
								param.put(Constant.NEED_CREATE_VAR, newDefineStr);
								param.put(Constant.NEED_VARSTORE_VAR, newStoreStr);
							}
						}else{
							//不同意创建，则放入忽略集合里
							ignoreDefineVarSet.add(varValue);
						}
						
					}else if(!ignoreDefineVarSet.contains(varValue)){
						//选择不定义变量，则给出错误信息
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { "变量【" + varValue + "】未定义" });
						break;
					}
				}else{
					//如果变量存在，检查变量类型是否匹配
					String vType = varStore.findVarType(varValue);
					boolean checkType = false;
					//mustHaveVal==false 为返回表格数据
					if(mustHaveVal==true){
						checkType = JavaTypeUtil.typeMatch(vType, typeName);
					}else{
						checkType = JavaTypeUtil.typeMatch(typeName, vType);
					}
					
					if(checkType==false){
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { "变量【" + varValue + "】与参数类型不匹配" });
						break;
					}
				}
			}else{
				//对表达式进行验证varValue，不允许进行直接运算，如+-*/,表达式需包含.()
				if(varValue.contains("+") || varValue.contains("-") || varValue.contains("*") || varValue.contains("/")){
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "表达式【"+varValue+"】不允许在参数中进行直接运算" });
					break;
				}else if(!(varValue.contains(".") && varValue.contains("(") && varValue.contains(""))){
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "表达式【"+varValue+"】不符合obj.funName(...)格式" });
					break;
				}
			}
			if(StringUtils.isEmpty(varType)){
				status.setStatus(ValidateStatus.FAILURE);
				status.setMessages(new String[] { "参数"+argName+"值类型不正确" });
				break;
			}
			
		}
	}



	/**
	 * 判断当前数据类型是不是日期类型
	 * @param typeName
	 * @return
	 */
	private boolean isDateType(String typeName) {
		boolean retVal = false;
		int dotIndex = typeName.lastIndexOf(".");
		if(dotIndex>0){
			typeName = typeName.substring(dotIndex+1);
		}
		retVal = "date".equalsIgnoreCase(typeName);
		return retVal;
	}


	/**
	 * 判断数据类型是否是包含含小数数值类型
	 * @param typeName 如float,double等数值类型
	 * @return 如果是数值类型，返回true
	 */
	private boolean isFloatType(String typeName) {
		return matchType(typeName,new String[]{"float","double"});
	}

	/**
	 * 检查数据类型是否在给定类型中
	 * @param typeName 待检测的数据类型
	 * @param types 匹配的数据类型数组
	 * @return 如果包含则返回true
	 */
	private boolean matchType(String typeName,String[] types){
		boolean retVal = false;
		//如果输入类型为java.lang.Integer这种完整类型
		int dotIndex = typeName.lastIndexOf(".");
		if(dotIndex>0){
			typeName = typeName.substring(dotIndex+1);
		}
		for(String type : types){
			if(type.equalsIgnoreCase(typeName)){
				retVal = true;
				break;
			}
		}
		//log.debug("CallDlgValidator matchType()-> typeName:" + typeName + " types[0]:" + types[0] + " retVal:" + retVal);
		return retVal;
	}


	/**
	 * 判断数据类型是否是不含小数数值类型
	 * @param typeName 如int,long,short等数值类型
	 * @return 如果是数值类型，返回true
	 */
	private boolean isNumberType(String typeName) {
		return matchType(typeName,new String[]{"byte","short","int","long","Integer"});
	}



	/**
	 * 验证转义字符是否有效
	 * @param str 输入的待验证字符如：c:\aest.txt
	 * @return boolean \a不存在，返回false
	 */
	private boolean validateEscape(String str) {
		//log.debug("CallDlgValidator validateEscape()-> str:" + str);
		boolean retVal = true;
		boolean start = false;
		for(char c : str.toCharArray()){
			//log.debug("CallDlgValidator validateEscape()-> c:" + c);
			if(start){
				if(!"bfnrt'\"\\".contains(String.valueOf(c))){
					retVal = false;
				}
			}
			if(start){
				start = false;
			}else{
				if(c=='\\'){
					start = true;
				}
			}
		}
		log.debug("CallDlgValidator validateEscape("+str+")-> retVal:" + retVal);
		return retVal;
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
