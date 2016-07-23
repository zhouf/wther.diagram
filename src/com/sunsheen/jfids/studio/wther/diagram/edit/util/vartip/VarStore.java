package com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.GlobalVarEntity;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.dialog.proposal.ConstantProposalType;

/**
 * 一个用于管理页面内变量提示，变量验证等
 * @author zhouf
 */
public class VarStore {
	
	public static String DEFINE_TYPE = "_DEFINE_TYPE";
	public static String ARG_TYPE = "_ARG_TYPE";
	public static String RET_TYPE = "_RET_TYPE";
	public static String TRANSACTION_TYPE = "_TRANSACTION_TYPE";
	public static String GLOBAL_TYPE = "_GLB_TYPE";
	
	private List<VarItemEntity> varList;
	
	public VarStore() {
		varList = new ArrayList<VarItemEntity>();
	}
	
	public VarStore(String initStr) {
		this();
		//initStr=> name1:String:from1|name2:type2:from2
		if(StringUtils.isNotEmpty(initStr)){
			//varList = gson.fromJson(initStr,new TypeToken<List<VarItemEntity>>(){}.getType());
			for(String eachVar : initStr.split("\\|")){
				this.appendVar(eachVar);
			}
		}
	}
	
	/**
	 * 验证输入的变量是否在变量列表中
	 * @param varName 输入的待验证变量名
	 * @return boolean 如果存在，返回TRUE
	 */
	public boolean contains(String varName){
		boolean retVal = false;
		for(VarItemEntity item : varList){
			if(item.getVarName().equalsIgnoreCase(varName)){
				retVal = true;
				break;
			}
		}
		return retVal;
	}
	
	/**
	 * 获取变量的类型，如果找到了变量的类型，返回表示类型的字串
	 * @param varName 输入的待检测变量的名字串
	 * @return String 返回变量类型字串，没有则返回空串
	 */
	public String findVarType(String varName){
		String retStr = "";
		for(VarItemEntity item : varList){
			if(item.getVarName().equalsIgnoreCase(varName)){
				retStr = item.getVarType();
				break;
			}
		}
		return retStr;
	}
	
	/**
	 * 验证当前变量名类型是否是常规类型
	 * @param varName 输入的待检测变量名
	 * @return boolean 如果是常规类型，返回true
	 */
	public boolean isVarPrimitiveType(String varName){
		String varType = findVarType(varName);
		return JavaTypeUtil.isPrimitiveType(varType);
	}
	
	public void removeByFrom(String from){
		List<VarItemEntity> dropList = new ArrayList<VarItemEntity>();
		for(VarItemEntity item : varList){
			if(item.getFrom().equalsIgnoreCase(from)){
				dropList.add(item);
			}
		}
		if(dropList.size()>0){
			varList.removeAll(dropList);
		}
	}
	
	public void appendVar(String varStr){
		//varStr	varname:vartype:from
		if(StringUtils.isNotEmpty(varStr)){
			String items[] = varStr.split(":");
			if(items.length>=3){
				VarItemEntity varItem = new VarItemEntity();
				varItem.setVarName(items[0]);
				varItem.setVarType(items[1]);
				varItem.setFrom(items[2]);
				
				varList.add(varItem);
			}
		}
	}

	public List<VarItemEntity> listAll(){
		return varList;
	}
	
	@Override
	public String toString() {
		//return gson.toJson(varList);
		StringBuilder retVal = new StringBuilder();
		for(VarItemEntity item : varList){
			retVal.append(item.getVarName()).append(":");
			retVal.append(item.getVarType()).append(":");
			retVal.append(item.getFrom()).append("|");
		}
		if(retVal.length()>0){
			retVal.setLength(retVal.length()-1);
		}
		return retVal.toString();
	}

	/**
	 * 将全局变量添加到变量集合中
	 * @param globalVarEntityList 全局变量集合
	 * @return void
	 */
	public void appendGlobalVarEntityList(List<GlobalVarEntity> globalVarEntityList) {
		for(GlobalVarEntity entity : globalVarEntityList){
			VarItemEntity item = new VarItemEntity();
			item.setVarName(entity.getVarName());
			item.setVarType(entity.getVarType());
			item.setFrom(GLOBAL_TYPE);
			varList.add(item);
		}
	}

	/**
	 * 把当前变量集合转换为Porposal数据
	 * @return
	 * @return String[]
	 */
	public String[] toProposalArray(){
		/*List<String> retList = new ArrayList<String>();
		for(VarItemEntity item : varList){
			retList.add(item.getVarName()+" "+item.getVarType());
		}*/
		return toProposalArrayWithFrom();
	}
	

	/**
	 * 把当前变量集合转换为Porposal数据,附加有变量来源信息
	 * @return String[] 如：{"name5 String&变量","arg2 String&参数","ret1 String&返回"}
	 */
	public String[] toProposalArrayWithFrom(){
		List<String> retList = new ArrayList<String>();
		for(VarItemEntity item : varList){
			StringBuilder sb = new StringBuilder();
			sb.append(item.getVarName()).append(" ").append(item.getVarType());
			if(ARG_TYPE.equals(item.getFrom())){
				sb.append("&").append(ConstantProposalType.ARG_TYPE);
			}else if(RET_TYPE.equals(item.getFrom())){
				sb.append("&").append(ConstantProposalType.RET_TYPE);
			}else if(GLOBAL_TYPE.equals(item.getFrom())){
				sb.append("&").append(ConstantProposalType.GLOBAL_TYPE);
			}else{
				sb.append("&").append(ConstantProposalType.VAR_TYPE);
			}
			retList.add(sb.toString());
			// FIXME 加上来源
		}
		return retList.toArray(new String[]{});
	}
}
