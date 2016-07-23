package com.sunsheen.jfids.studio.wther.diagram.component.views;

import java.util.ArrayList;
import java.util.List;

import com.sunsheen.jfids.studio.logic.impl.CallImpl;

/**
 * 业务逻辑流组件库或者变量树节点数据模型
 * @author xh
 *
 */
public class JavaComponentTreeNode extends CallImpl implements ITree {

	private List<Parameter> paramList;
	StringBuffer retStr;
	
	public JavaComponentTreeNode() {
		super();
		paramList = new ArrayList<Parameter>();
		retStr = new StringBuffer();
	}

	public List<Parameter> getParamList() {
		return paramList;
	}

	public void setParamList(List<Parameter> paramList) {
		this.paramList = paramList;
	}
	
	public void addParam(Parameter param){
		this.paramList.add(param);
	}
	
	/**
	 * 拼接返回字串
	 * @param type
	 * @param name
	 * @param comment
	 */
	public void addReturnStr(String type,String name,String comment){
		retStr.append(type).append(":");
		retStr.append(name).append(":");
		retStr.append("<NOSET>").append(":");
		retStr.append("<NOSET>").append(":");
		retStr.append(comment).append("|");
	}
	
	/**
	 * 返回组合好的字串
	 * @return
	 */
	public String getReturnStr(){
		if(retStr.toString().endsWith("|"))
			retStr.deleteCharAt(retStr.lastIndexOf("|"));
		
		return retStr.toString();
	}
	
	/**
	 * 获得参数的字串，用于显示构件库信息
	 * @return
	 */
	public String getParamStr(){
		String retStr = "";
		for (Parameter p : this.paramList) {
			retStr += p.getPtype();
			retStr += ",";
		}
		int length = retStr.length();
		if(retStr.endsWith(",")){
			retStr = retStr.substring(0, length-1);
		}
		return retStr;
	}
	
	/**
	 * 将参数列表转换为字串，用于将XML中的参数配置转换为Call节点属性
	 * 每个参数格式为=> 参数类型：参数名：参数值：值类型：参数说明
	 * 隐藏参数格式为=> 级别（区分生成的代码在哪一层）:参数名:参数值
	 * 若某项为空，则用<NOSET>代替
	 */
	public void coverParamToStr(){
		StringBuffer argStr = new StringBuffer();
		for (Parameter p : this.paramList) {
			argStr.append(p.getPtype()).append(":");
			argStr.append(p.getPname()).append(":");
			argStr.append(p.getDefaultVal());
			argStr.append(":<NOSET>:");
			argStr.append(p.getComment()).append(":");
			argStr.append(p.getTipconf()).append("|");
		}
		
		this.setArgs(argStr.toString());
		return;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void setChildren(List children) {}

	@SuppressWarnings("rawtypes")
	@Override
	public List getChildren() {
		return null;
	}

}
