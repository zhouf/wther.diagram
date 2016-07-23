package com.sunsheen.jfids.studio.wther.diagram.dialog.validator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateListener;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateStatus;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.InputDataValidate;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;

/**
 * 这是一个对赋值节点的验证
 * 
 * @author zhoufeng Date:2013-6-20
 */
public class AssignmentDlgValidator implements ValidateListener {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(AssignmentDlgValidator.class.getName());

	@SuppressWarnings("unchecked")
	@Override
	public ValidateStatus validate(Map<String, Widget> coms, Map<String, Object> param) {
		ValidateStatus status = new ValidateStatus();
		Text nodeName = (Text) coms.get("nodename");

		// 如果ID输入框内容为空，提示出错
		if (nodeName != null && nodeName.getText().length() == 0) {
			status.setStatus(ValidateStatus.FAILURE);
			status.setMessages(new String[] { "'节点名'不能为空" });
		}
		
		VarStore varStore = (VarStore) param.get("varstore");
		
		/*List<GlobalVarEntity> globalVarSet = (List<GlobalVarEntity>)param.get("glbVarSet");
		if(globalVarSet!=null){
			//将globalVarSet合并到VarStore中处理
			// int oldSize = varStore.listAll().size();
			for(GlobalVarEntity glbVar : globalVarSet){
				varStore.appendVar(glbVar.getVarName() + ":" + glbVar.getVarType() + ":GLOBAL");
			}
		}*/
		
		// *
		AgileGrid table = (AgileGrid) coms.get("expandTable");
		InnerTableContentProvider provider = (InnerTableContentProvider) table.getContentProvider();
		if (provider.data.size() > 0) {
			// 有数据，获取表格数据
			Map<Integer, Map<Integer, Object>> map = provider.data.get(0);
			List<Integer> keys = Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
			Iterator<Integer> iterator = keys.iterator();
			while (iterator.hasNext()) {
				Map<Integer, Object> row = map.get(iterator.next());
				String varName = row.get(1).toString();// 变量名
				String varValue = row.get(2).toString();// 变量值
				String varType = row.get(3).toString();// 变量类型
				String varName2 = clearArrayMark(varName);
				
				//去掉/标记，/标记用于作为成员属性提示
				int i = varName2.indexOf("/");
				if(i>0){
					varName2 = varName2.substring(0, i);
				}
				
				//TODO 去掉了数组括号后的测试
				if (JavaTypeUtil.isKeyWords(varName2)) {
					// 如果是关键字
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "变量【" + varName2 + "】为关键字" });
					break;
				}
				if (!varStore.contains(varName2)) {
					// 变量没有被定义
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "变量【" + varName + "】未定义" });
					break;
				}
				if (varName == null || "".equals(varName)) {
					// 变量名为空
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "变量名不能为空" });
					break;
				}
				if (!InputDataValidate.checkValidSpell(varName2)) {
					// 变量拼写不合法
					// TODO 通过未定义变量的检查，这个方法应该不会被执行
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "变量【" + varName + "】拼写不合法" });
					break;
				}
				String checkMsg = eachRowCheck(varValue, varType);
				if (!"PASS".equalsIgnoreCase(checkMsg)) {
					// 验证不通过
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "变量【" + varName + "】" + checkMsg });
					break;
				}
				//对varType为变量，常量，表达式的验证
				if("变量".equals(varType)){
					if(!varStore.contains(varValue)){
						//如果不在变量集合中
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { "变量【" + varValue + "】未定义" });
						break;
					}else{
						//变量已定义，判断类型是否匹配
						String leftType = varStore.findVarType(varName);
						String rightType = varStore.findVarType(varValue);
						if(!JavaTypeUtil.typeMatch(rightType, leftType)){
							status.setStatus(ValidateStatus.FAILURE);
							status.setMessages(new String[] { "变量【" + varValue + "】与所需类型不匹配" });
							break;
						}
					}
				}else if("常量".equals(varType)){
					String type = row.get(0).toString();// 变量名对应的数据类型，第一列
					log.debug("AssignmentDlgValidator.validate()->常量检查type:" + type + " varName:" + varName + " varValue:" + varValue + " varType:" + varType);
					if("short".equals(type) || "int".equals(type) || "long".equals(type)){
						//整数类型
						if(!InputDataValidate.checkIntegerValidate(varValue)){
							status.setStatus(ValidateStatus.FAILURE);
							status.setMessages(new String[] { "变量" + varName + "常量设置值【"+varValue+"】不正确" });
							break;
						}
						
					}else if("float".equals(type) || "double".equals(type)){
						//小数类型
						if(!InputDataValidate.checkNumStrValidate(varValue)){
							status.setStatus(ValidateStatus.FAILURE);
							status.setMessages(new String[] { "变量" + varName + "常量设置值【"+varValue+"】不正确" });
							break;
						}
					}else if("Date".equals(type)){
						//日期类型
						if(!InputDataValidate.checkDateStrValidate(varValue)){
							status.setStatus(ValidateStatus.FAILURE);
							status.setMessages(new String[] { "变量" + varName + "常量设置值【"+varValue+"】不正确" });
							break;
						}
					}else if("char".equals(type)){
						//按char类型数据进行验证
						if(!InputDataValidate.checkConstantChar(varValue)){
							status.setStatus(ValidateStatus.FAILURE);
							status.setMessages(new String[] { "变量" + varName + "常量设置需要添加单引号，如'A'" });
							break;
						}
					}else if("boolean".equals(type)){
						//按布尔类型数据进行验证
						if(!InputDataValidate.checkConstantBoolean(varValue)){
							status.setStatus(ValidateStatus.FAILURE);
							status.setMessages(new String[] { "变量" + varName + "常量设置布尔类型数据不正确，应如True,1,False,0" });
							break;
						}
					}else if("String".equals(type)){
						//按字符串类型进行验证，需要添加引号
						if(!InputDataValidate.checkConstantString(varValue)){
							status.setStatus(ValidateStatus.FAILURE);
							status.setMessages(new String[] { "变量" + varName + "常量设置需要添加引号" });
							break;
						}
					}else{
						log.debug("AssignmentDlgValidator.validate()->:对常量验证通过");
					}
					
				}
				// FIXME 对表达式未检测
			}

		} else {
			// 没有数据，不进行验证
			status.setStatus(ValidateStatus.SUCCESS);
		}
		// */

		return status;
	}
	
	/**
	 * 去掉变量定义中的数组方括号
	 * @param varName 输入的变量名，如arr[1]
	 * @return String 去掉方括号后的数组名arr
	 */
	private String clearArrayMark(String varName) {
		String retVal = "";
		varName = (varName==null? "" : varName);
		int left = varName.indexOf("[");
		int right = varName.indexOf("]");
		if(left>=0 && right>=0 && left<right){
			retVal = varName.substring(0,left);
		}else{
			retVal = varName;
		}
		return retVal;
	}

	/**
	 * 验证每行的数据，没有数据，则直接返回T
	 * 
	 * @param varValue
	 *            待验证数据串
	 * @param varType
	 *            数据类型
	 * @return 通过验证则返回T,不通过则返回F
	 */
	public static String eachRowCheck(String varValue, String varType) {
		String retVal = "PASS";
		if (varValue != null && varValue.length() > 0) {
			if ("Date".equalsIgnoreCase(varType) && InputDataValidate.checkDateStrValidate(varValue)==false) {
				retVal = "日期格式不正确";
			} else if ("short,int,long,float,double".indexOf(varType)>=0 && InputDataValidate.checkNumStrValidate(varValue)==false) {
				retVal = "输入数字不正确";
			}else if("String".indexOf(varType)>=0 && InputDataValidate.checkString(varValue)==false){
				retVal = "初始化不正确";
			}
		}
		return retVal;
	}

}
