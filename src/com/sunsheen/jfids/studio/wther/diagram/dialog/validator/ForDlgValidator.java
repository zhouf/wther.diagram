package com.sunsheen.jfids.studio.wther.diagram.dialog.validator;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateListener;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateStatus;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.InputDataValidate;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;

/**
 * 这是一个对循环节点的验证
 * 
 * @author zhoufeng Date:2016-2-19
 */
public class ForDlgValidator implements ValidateListener {

	@SuppressWarnings("unchecked")
	@Override
	public ValidateStatus validate(Map<String, Widget> coms, Map<String, Object> param) {
		ValidateStatus status = new ValidateStatus();
		Text nodeName = (Text) coms.get("nodename");
		Button simpleRadio = (Button)coms.get("simpleRadio");
//		Button ordinaryRadio = (Button)coms.get("ordinaryRadio");
		Text simpleVarText = (Text)coms.get("simpleVar");
		Text simpleTimesText = (Text)coms.get("simpleTimes");
		Text textCodeText = (Text)coms.get("textCode");
		String simpleVar = simpleVarText.getText();
		String simpleTimes = simpleTimesText.getText();
		String textCode = textCodeText.getText();
		
		// 如果节点名内容为空，提示出错
		if (nodeName != null && nodeName.getText().length() == 0) {
			status.setStatus(ValidateStatus.FAILURE);
			status.setMessages(new String[] { "'节点名'不能为空" });
		}
		
		VarStore varStore = (VarStore) param.get("varstore");
		
		if(simpleRadio.getSelection()){
			//如果用户选择的是简单循环，则进行验证
			if(!InputDataValidate.checkValidSpell(simpleVar)){
				status.setStatus(ValidateStatus.FAILURE);
				status.setMessages(new String[] { "循环变量拼写不合法" });
				return status;
			}else if(JavaTypeUtil.isKeyWords(simpleVar)){
				status.setStatus(ValidateStatus.FAILURE);
				status.setMessages(new String[] { "循环变量不能是关键字" });
				return status;
			}else{
				if(varStore.contains(simpleVar)){
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "循环变量["+simpleVar+"]重复定义" });
					return status;
				}
			}

			if(!InputDataValidate.checkPositiveIntegerValidate(simpleTimes) && !varStore.contains(parseVar(simpleTimes))){
				status.setStatus(ValidateStatus.FAILURE);
				status.setMessages(new String[] { "循环次数拼写不是正整数或变量未定义" });
				return status;
			}
		}else{
			//普通循环模式 for while do-while
			if(StringUtils.isEmpty(textCode)){
				status.setStatus(ValidateStatus.FAILURE);
				status.setMessages(new String[] { "循环定义为空，请修改" });
				return status;
			}
		}
		
		return status;
	}
	

	/**
	 * 从字串中解析出变量名
	 * @param simpleTimes 如：obj.fun()
	 * @return 解析出：obj
	 */
	private String parseVar(String str) {
		String retStr = str;
		int dotIndex = str.indexOf(".");
		if(dotIndex>0){
			retStr = str.substring(0, dotIndex);
		}
		return retStr;
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
