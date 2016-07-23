package com.sunsheen.jfids.studio.wther.diagram.compiler;

import org.apache.commons.lang.StringUtils;
/**
 * 处理全局变量每一项的结构
 * @author zhouf
 */
//改用GlobalVarEntity
@Deprecated
public class GlobalVarItem {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(GlobalVarItem.class.getName());
	
	private String varType;
	private String varVal;
	private boolean getter;
	private boolean setter;
	private boolean ref;
	public GlobalVarItem(String initStr) {
		String items[] = StringUtils.splitPreserveAllTokens(initStr, ":");
		if(items.length==5){
			varType = items[0];
			varVal = items[1];
			getter = "true".equals(items[2]);
			setter = "true".equals(items[3]);
			ref = "true".equals(items[4]);
		}else{
			log.warn("全局变量分解格式不合法：" + initStr);
		}
	}
	
	public String getVarType() {
		return varType;
	}
	public String getVarVal() {
		return varVal;
	}
	public boolean isGetter() {
		return getter;
	}
	public boolean isSetter() {
		return setter;
	}
	public boolean isRef() {
		return ref;
	}
	
	/**
	 * 获取get方法的字串
	 * @return String getFieldName
	 */
	public String getGetterFuncStr(){
		return makeGetterSetterName("get");
	}
	
	public String getSetterFuncStr(){
		return makeGetterSetterName("set");
	}
	
	private String makeGetterSetterName(String prefix){
		String fieldName = varVal;
		Character chr = Character.toUpperCase(fieldName.charAt(0));
		fieldName = chr + fieldName.substring(1);
		return prefix.concat(fieldName);
	}
}
