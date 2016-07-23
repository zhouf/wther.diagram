package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.BglbFileEntity;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.DataVarEntity;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.GlobalVarEntity;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.RefreshVarTreeAdapter;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarItemEntity;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;


public class RefreshVarTreeUtil {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(RefreshVarTreeUtil.class.getName());

	private static Map<String,String> globalMap;
	public static void refresh(FlowImpl flow){
		refresh(flow, false);
	}
	
	public static void setGlobalMap(Map<String, String> globalMap) {
		RefreshVarTreeUtil.globalMap = globalMap;
	}
	
	/**
	 * 刷新业务逻辑流的变量树
	 * @param flow 当前模型
	 * @param forced 同一页面是否强制刷新
	 */
	@SuppressWarnings("rawtypes")
	public static void refresh(Map<String, String> defVarMap,boolean forced){

		defVarMap.putAll(globalMap);
		
		ProjectInfoUtil projectInfo = new ProjectInfoUtil();
		//把变量的String表示通过反射转换为Class类型.
		Map<String, Class> classMap = convertToClassMap(defVarMap,projectInfo.getProjectPath());
		
		RefreshVarTreeAdapter.transferMap(classMap, projectInfo.getFilePath(),forced);
	}
	
	/**
	 * 这个方法保存是为了兼容之前的代码，待删除
	 * @param flow
	 * @param forced
	 * @return void
	 */
	public static void refresh(FlowImpl flow,boolean forced){
		
		Map<String,String> defVarMap = convertToMap(flow.getVarstore());

		defVarMap.putAll(globalMap);
		
		ProjectInfoUtil projectInfo = new ProjectInfoUtil();
		//把变量的String表示通过反射转换为Class类型.
		Map<String, Class> classMap = convertToClassMap(defVarMap,projectInfo.getProjectPath());
		
		RefreshVarTreeAdapter.transferMap(classMap, projectInfo.getFilePath(),forced);
	}
	
	public static Map<String,String> covertFlowToMap(FlowImpl flow){
		return convertToMap(flow.getVarstore());
	}
	
	/**
	 * 从全局变量里解析出变量集合
	 * @param globalEntity
	 * @return Map<String,String>
	 */
	public static void initGlobalVarToMap(BglbFileEntity globalEntity){
		globalMap = new HashMap<String,String>();
		if(globalEntity!=null){
			for(GlobalVarEntity var : globalEntity.getGlobalVarList()){
				globalMap.put("this."+var.getVarName(), var.getVarType());
			}
			for(DataVarEntity portData : globalEntity.getDataVarList()){
				//globalMap.put("this."+portData.getVarName(), "com.sunsheen.jfids.system.bizass.port.IDataPort");
				globalMap.put("this."+portData.getVarName(), portData.getInterfaceType());
			}
		}
	}
	
	/**
	 * 将字串表示的MAP对象转换成存放Class的Map
	 * @param defVarMap 存放变量的集合{变量名=>变量类型}
	 * @param projectPath 当前工程路径，如果有用到工程中的类，需要通过路径去处理Class
	 * 		如：D:/eclipse-SDK-3.7.1_GMF/runtime-EclipseApplication/jfids_demo/resources
	 * 		需要的路径是file:/D:/eclipse-SDK-3.7.1_GMF/runtime-EclipseApplication/jfids_demo/WebRoot/WEB-INF/component/
	 * @return Map<String,Class> 返回处理好的集合{变量名=>变量类}
	 */
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> convertToClassMap(Map<String, String> defVarMap, String projectPath) {
		Map<String,Class> retMap = new HashMap<String,Class>();
		Set<String> keys = defVarMap.keySet();
		for(String key : keys){
			if(key==null || key.length()==0){
				continue;
			}
			
			String type = defVarMap.get(key);
			if(type.endsWith("[]")){
				type = type.substring(0, type.lastIndexOf("["));
			}
			if(!JavaTypeUtil.isPrimitiveType(type)){
				//如果不是普通类型
				Class<?> className = null;
				try {
					className = FindClass.fromString(type);
					//className = JavaTypeUtil.findClassType(type);
					retMap.put(key, className);
				} catch (ClassNotFoundException e) {
					log.debug("变量树刷新时无法获取类：" + type);
				}
			}else{
				log.debug("RefreshVarTreeUtil convertToClassMap()-> 常规类型不放入变量树窗口 type:" + type);
			}
		}
		return retMap;
	}


	/**
	 * 当页面失去焦点时重置变量树
	 */
	public static void reset(){
		RefreshVarTreeAdapter.reset();
	}
	
	
	/**
	 * 将页面中定义的变量字串转换成变量集合，传递给构件库中显示
	 * @param argStr 模型中记录变量集合的字串
	 * @return 返回Map如：{{name,java.lang.String},{name2,java.lang.Short}....}
	 */
	private static Map<String, String> convertToMap(String argStr) {
		Map<String, String> varMap = new HashMap<String, String>();
		
		VarStore varStore = new VarStore(argStr);
		for(VarItemEntity item : varStore.listAll()){
			String varType = item.getVarType();
			if (JavaTypeUtil.containsKey(varType)) {
				varType = JavaTypeUtil.convertShortToLongType(varType);
			}
			//基本类型不放入，如char、boolean、byte、short、int、long、float、double
			if (!JavaTypeUtil.isPrimitiveType(varType)) {
				varMap.put(item.getVarName(), varType);
				//Log.debug("RefreshVarTreeUtil convertDefineToMap()-> varName:" + item.getVarName() + "\t:varType:" + varType);
			}
		}
		return varMap;
	}
}
