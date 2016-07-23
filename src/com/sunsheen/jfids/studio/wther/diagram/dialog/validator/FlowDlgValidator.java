package com.sunsheen.jfids.studio.wther.diagram.dialog.validator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateListener;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateStatus;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.Constant;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FindClass;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.InputDataValidate;

/**
 * 这是一个对页面对话框的验证
 * 
 * @author zhoufeng Date:2013-6-20
 */
public class FlowDlgValidator implements ValidateListener {
	
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(FlowDlgValidator.class.getName());
	
	private static final int NOARG = 1;
	private static final int STRINGARG1 = 2;

	@Override
	public ValidateStatus validate(Map<String, Widget> coms, Map<String, Object> param) {
		ValidateStatus status = new ValidateStatus();

		Set<String> definedNameList = validateDefTable(coms, status, "defTable");
//		if (status.getStatus() == ValidateStatus.SUCCESS) {
//			//如果通过了验证，则继续验证全局变量表格数据
//			validateDefTable(coms, status, "glbTable");
//		}

		// 如果定义验证通过，则继续验证参数表格数据
		if (status.getStatus() == ValidateStatus.SUCCESS) {

			// 验证参数和返回数据
			AgileGrid argTable = (AgileGrid) coms.get("argTable"); // 获取变量定义表
			InnerTableContentProvider provider2 = (InnerTableContentProvider) argTable.getContentProvider();
			for (int i = 0; i < provider2.data.size(); i++) {
				Map<Integer, Map<Integer, Object>> map = provider2.data.get(i);
				List<Integer> keys = Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
				Set<String> definedVarSet = new HashSet<String>();//记录已定义的参数名，用于重复名判断
				Iterator<Integer> iterator = keys.iterator();
				while (iterator.hasNext()) {
					Map<Integer, Object> row = map.get(iterator.next());
					String classify = row.get(0).toString();// 分类
					String varName = row.get(1).toString();// 变量名
					String varType = row.get(2).toString();// 变量类型

					if (varName == null || "".equals(varName)) {
						// 变量名为空
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { classify + "变量名不能为空" });
						break;
					}
					if (!InputDataValidate.checkValidSpell(varName)) {
						// 变量拼写不合法
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { classify + "变量【" + varName + "】拼写不合法" });
						break;
					}
					if(!InputDataValidate.checkKeyWords(varName)){
						//变量不能为关键字
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[]{classify + "变量【"+varName+"】命名不能为关键字"});
						break;
					}
					
					//判断变量名是否在定义时被使用
					if(definedNameList.contains(varName)){
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[]{"变量名【" + varName+"】重复定义，请检查"});
						break;
					}
					
					String checkVarName = classify + "【"+varName+"】";//用于判断是否被定义的验证
					if(definedVarSet.contains(checkVarName)){
						//参数不能重复定义
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[]{checkVarName+"不能重复定义"});
						break;
					}else{
						definedVarSet.add(checkVarName);
					}
					
					if (varType.equals(Constant.TYPE_JAVA_BROWSE)) {
						// 变量类型不能为“Java浏览”
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { classify + "变量【" + varName + "】类型未设置" });
						return status;
					}
				}// 验证每行结束
			}
			// 验证参数表结束
		}

		return status;
	}

	private Set<String> validateDefTable(Map<String, Widget> coms, ValidateStatus status, String validateTableName) {
		Set<String> alreadyDefinedNameList = new HashSet<String>();
		
		AgileGrid table = (AgileGrid) coms.get(validateTableName); // 获取变量定义表
		InnerTableContentProvider provider = (InnerTableContentProvider) table.getContentProvider();
		if (provider.data.size() > 0) {
			// 有数据，获取表格数据
			Map<Integer, Map<Integer, Object>> map = provider.data.get(0);
			List<Integer> keys = Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
			Iterator<Integer> iterator = keys.iterator();
			Set<String> varSet = new HashSet<String>();
			while (iterator.hasNext()) {
				Map<Integer, Object> row = map.get(iterator.next());
				String varName = row.get(0).toString();// 变量名
				String varValue = row.get(1).toString();//变量值
				String varType = row.get(2).toString();// 变量类型
				String isArrayStr = row.get(3).toString();// 变量类型
				boolean isArray = "true".equalsIgnoreCase(isArrayStr);
				
				if (varName == null || "".equals(varName)) {
					// 变量名为空
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "变量名不能为空" });
					break;
				}
				if (!InputDataValidate.checkValidSpell(varName)) {
					// 变量拼写不合法
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "变量【" + varName + "】拼写不合法" });
					break;
				}
				if(!InputDataValidate.checkKeyWords(varName)){
					//变量不能为关键字
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[]{"变量【"+varName+"】命名不能为关键字"});
					break;
				}
				if(varSet.contains(varName)){
					//变量不能重复定义
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[]{"变量【"+varName+"】不能重复定义"});
					break;
				}else{
					varSet.add(varName);
				}
				
				if (varType.startsWith("Java浏览")) {
					// 变量类型不能为“Java浏览”
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "变量【" + varName + "】类型未设置" });
					break;
				}
				
				if (JavaTypeUtil.isPrimitiveType(varType)) {
					if("char".equals(varType)){
						if(isArray){
							//如果是数组，则要求初始字串不能为空
							if(StringUtils.isEmpty(varValue)){
								status.setStatus(ValidateStatus.FAILURE);
								status.setMessages(new String[] { "基本类型变量【" + varName + "】数组初始值可设置为一字串" });
								break;
							}
						}else{
							//不是数组的验证
							if(!InputDataValidate.checkConstantChar(varValue)){
								status.setStatus(ValidateStatus.FAILURE);
								status.setMessages(new String[] { "基本类型变量【" + varName + "】仅可接受一个字符初始值设置" });
								break;
							}
						}
					}else if("short".equals(varType)){
						if(!isArray && !checkShortDefaultVal(varValue)){
							status.setStatus(ValidateStatus.FAILURE);
							status.setMessages(new String[] { "基本类型变量【" + varName + "】初始值设置不合法或超过表示范围" });
							break;
						}
					}else if("boolean".equals(varType)){
						if(isArray){
							//如果是boolean类型数组
							for(String eachBooleanVal : StringUtils.split(varValue,",")){
								if(!InputDataValidate.checkConstantBoolean(eachBooleanVal)){
									status.setStatus(ValidateStatus.FAILURE);
									status.setMessages(new String[] { "基本类型变量【" + varName + "】数组初始值【"+eachBooleanVal+"】设置不合法，参考设置如：True,False,0,1" });
									break;
								}
							}
						}else{
							if(!InputDataValidate.checkConstantBoolean(varValue)){
								status.setStatus(ValidateStatus.FAILURE);
								status.setMessages(new String[] { "基本类型变量【" + varName + "】初始值设置不合法，参考设置如：True,False,0,1" });
								break;
							}
						}
					}else if (varValue == null || varValue.equals("") ) {
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[] { "基本类型变量【" + varName + "】类型未初始化" });
						break;
					}
				}else{
					//对类类型数据的验证
					if("String".equals(varType)){
						if(StringUtils.isNotEmpty(varValue)){
							if(isArray){
								for(String eachStrArrayItem : StringUtils.split(varValue,",")){
									if(!InputDataValidate.checkConstantString(eachStrArrayItem)){
										status.setStatus(ValidateStatus.FAILURE);
										status.setMessages(new String[] { "字符串变量【" + varName + "】数组数据【"+eachStrArrayItem+"】初始化需要加双引号\"\"" });
										break;
									}
								}
								
							}else{
								if(!InputDataValidate.checkConstantString(varValue)){
									status.setStatus(ValidateStatus.FAILURE);
									status.setMessages(new String[] { "字符串变量【" + varName + "】数据初始化需要加双引号\"\"" });
									break;
								}
								
							}
							//可以允许有双引号，在生成代码时会判断处理
							/*if(varValue.startsWith("\"") || varValue.endsWith("\"")){
								status.setStatus(ValidateStatus.FAILURE);
								status.setMessages(new String[] { "字符串变量【" + varName + "】数据初始化不需要加\"" });
								break;
							}*/
						}
					}else if("java.lang.Integer".equals(varType)){
						if(StringUtils.isNotEmpty(varValue) && !InputDataValidate.checkIntegerValidate(varValue)){
							status.setStatus(ValidateStatus.FAILURE);
							status.setMessages(new String[] { "变量【" + varName + "】整型值"+varValue+"设置不合法" });
							break;
						}
					}else if("Date".equals(varType)){
						if(StringUtils.isNotEmpty(varValue) && !InputDataValidate.checkDateStrValidate(varValue)){
							status.setStatus(ValidateStatus.FAILURE);
							status.setMessages(new String[] { "变量【" + varName + "】日期类型值"+varValue+"设置不合法" });
							break;
						}
					}else if("ArrayList".equals(varType) || "HashMap".equals(varType)){
						if(StringUtils.isNotEmpty(varValue)){
							status.setStatus(ValidateStatus.FAILURE);
							status.setMessages(new String[] { "变量【" + varName + "】类型"+varType+"不支持初始化参数设置" });
							break;
						}
					}else{
						//其它用户选择类，目前只处理了空参数和一个字符串形式的构造函数
						// FIXME 其他类型构造函数和无构造函数的类初始化问题待考虑
						int param = checkClassParameter(varType);
						log.debug("FlowDlgValidator.validateDefTable()->varType:" + varType + " param:" + param + "[" + Integer.toBinaryString(param) + "]");
						if(StringUtils.isEmpty(varValue)){
							if((param&NOARG)==0){
								//如果不支持无参
								status.setStatus(ValidateStatus.FAILURE);
								status.setMessages(new String[] { "变量【" + varName + "】类型"+varType+"不支持无参设置" });
								break;
							}
						}else{
							if((param&STRINGARG1)==0){
								//如果不支持String参数
								status.setStatus(ValidateStatus.FAILURE);
								status.setMessages(new String[] { "变量【" + varName + "】类型"+varType+"不支持String类型初始化参数设置" });
								break;
							}else{
								//如果支持String，则验证参数值设置是否有添加“”
								if(!(varValue.startsWith("\"") && varValue.endsWith("\""))){
									status.setStatus(ValidateStatus.FAILURE);
									status.setMessages(new String[] { "变量【" + varName + "】可接受字符串类型数据初始化，请完善引号" });
									break;
								}
							}
						}
					}
					
				}
				
				String checkMsg = eachRowCheck(varValue, varType, isArray);
				if (!"PASS".equalsIgnoreCase(checkMsg)) {
					// 验证不通过
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[] { "变量【" + varName + "】" + checkMsg });
					break;
				}else{
					//验证通过，放入变量到集合
					alreadyDefinedNameList.add(varName);
				}
			}

		} else {
			// 没有数据，不进行验证
			status.setStatus(ValidateStatus.SUCCESS);
		}
		return alreadyDefinedNameList;
	}

	
	/**
	 * 检查类是否有无参构造函数，或是只有一个字符串构造函数
	 * @param varType 传入的类型字串，如：java.text.SimpleDateFormat
	 * @return 如果有无参构造函数，右第一位为1，如果有一个String参数构造函数，右第二位为1
	 */
	private int checkClassParameter(String varType) {
		int retVal = 0;
		try {
			Class typeClass = FindClass.fromString(varType);
			Constructor[] cons = typeClass.getDeclaredConstructors();
			for(Constructor c : cons){
				if(Modifier.PUBLIC == c.getModifiers()){
					if(c.getParameterTypes().length == 0){
						retVal = retVal|NOARG;
					}else if(c.getParameterTypes().length == 1){
						for(Class pc : c.getParameterTypes()){
							if("java.lang.String".equals(pc.getName())){
								retVal = retVal|STRINGARG1;
								break;
							}
						}
					}
				}
			}
		} catch (ClassNotFoundException e) {
			log.error("FlowDlgValidator.checkClassParameter()->:" + e.toString());
		}
		return retVal;
	}

	/**
	 * 验证short类型初始值设置是否符合要求，可允许为空，若有值，则有效范围为-32768~32767
	 * @param varValue 传入的待验证的字串
	 * @return 符合要求返回 true
	 */
	private boolean checkShortDefaultVal(String varValue) {
		boolean retVal = false;
		if(varValue==null || varValue.length()==0){
			return true;
		}
		try{
			short checkA = Short.valueOf(varValue);
			if(checkA<=32767 && checkA>=-32768){
				retVal = true;
			}
		}catch(NumberFormatException ex){
			retVal = false;
		}
		return retVal;
	}

	

	/**
	 * 验证每行的数据，没有数据，则直接返回T
	 * 
	 * @param varValue 待验证数据串
	 * @param varType 数据类型
	 * @param isArray 是否是数组标记
	 * @return 通过验证则返回"PASS",不通过则返回错误信息
	 */
	private String eachRowCheck(String varValue, String varType, boolean isArray) {
		String retVal = "";
		if(isArray){
			//if(InputDataValidate.checkArray(varValue, varType)){
			if (JavaTypeUtil.isPrimitiveType(varType) || "String".equals(varType)) {
				
				for(String eachVal : varValue.split(",")){
					retVal = AssignmentDlgValidator.eachRowCheck(eachVal, varType);
					if(!"PASS".equalsIgnoreCase(retVal)){
						break;
					}
				}
			}else{
				retVal = "类型"+varType+"不支持数组方式初始化数据";
			}
		}else{
			retVal = AssignmentDlgValidator.eachRowCheck(varValue, varType);
		}
		return retVal;
	}

}
