package com.sunsheen.jfids.studio.wther.diagram.edit.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import com.sunsheen.jfids.studio.dialog.DialogConstants;
import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.logic.BixCall;
import com.sunsheen.jfids.studio.logic.Bixref;
import com.sunsheen.jfids.studio.logic.Call;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.Convert;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ParamUtil;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.PixBixCallUtil;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.TableHiddenType;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.Constant;
import com.sunsheen.jfids.studio.wther.diagram.parser.CallArgParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.CallArgParserMapPut;
import com.sunsheen.jfids.studio.wther.diagram.parser.DataPortArgParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.ThisRunArgParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.CallArgItem;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.CallArgItemMapPut;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.DataPortArgItem;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.ThisRunArgItem;

/**
 * 这个类是保存Call和Bixref操作公共的部分，很多自定义的方法都在这里实现
 * @author zhoufeng
 * Date:2013-5-21
 */
public class BixCallEditPartUtil  {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(BixCallEditPartUtil.class.getName());
	

	/**
	 * 处理对话框确定后返回的数据
	 * 
	 * @param callNode
	 * @param params
	 */
	@SuppressWarnings("unchecked")
	public static void dealDlgOK(final BixCall callNode, final Map<String, Object> params) {
		// argStr为生成代码()中间的部分
		String  argStr = "";
		Map<Integer,String> hiddenMap = (Map<Integer, String>) params.get(TableHiddenType.HIDDEN);
		// 获取返回的数据
		List<Map<Integer, Map<Integer, Object>>> data = (List<Map<Integer, Map<Integer, Object>>>) params.get("table");

		int rowIndex = 0;
		if (data != null && data.size() > 0) {
			// 处理参数表格
			Map<Integer, Map<Integer, Object>> inputData = data.get(0);
			CallArgParser callArgParser = new CallArgParser();
			for (Entry<Integer, Map<Integer, Object>> eachRow : inputData.entrySet()) {
				Map<Integer, Object> map = eachRow.getValue();
				String argType = (String) map.get(1);// 获取第2列数据，变量类型
				String argName = (String) map.get(2);// 获取第3列数据，变量名
				String value = Convert.convertSpliter((String) map.get(3), true);// 获取参数值
				String type = Convert.convertSpliter((String) map.get(4), true);// 获取参数传递类型
				String comment = Convert.convertSpliter((String) map.get(5), true);// 获取参数描述信息
				String tipconf = (String) map.get(TableHiddenType.TIP_RENDER);// 获取提示配置
				// 对value,type,comment进行为空的处理
				if (StringUtils.isEmpty(value))
					value = "<NOSET>";
				if (StringUtils.isEmpty(type))
					type = "<NOSET>";
				if (StringUtils.isEmpty(comment))
					comment = "<NOSET>";
				if (StringUtils.isEmpty(tipconf))
					tipconf = "<NOSET>";
				
				if(hiddenMap!=null){
					String hiddenStr = hiddenMap.get(rowIndex);
					log.debug("BixCallEditPartUtil dealDlgOK()-> hiddenMap.get("+rowIndex+")=" + hiddenStr);
					while(StringUtils.isNotEmpty(hiddenStr)){
						CallArgItem hiddenItem = new CallArgItem(hiddenStr);
						String setVal = hiddenItem.getValue();
						if(StringUtils.isNumeric(setVal) || "true".equalsIgnoreCase(setVal) || "false".equalsIgnoreCase(setVal)){
							hiddenItem.setValType("表达式");
						}else{
							hiddenItem.setValType("常量");
						}
						callArgParser.addItem(hiddenItem);
						//添加一个后，需要再加一个计数
						rowIndex++;
						// 如果有连续的，接着找下面一个
						hiddenStr = hiddenMap.get(rowIndex);
					}
				}
				rowIndex++;

				CallArgItem argItem = new CallArgItem();
				argItem.setVarType(argType);
				argItem.setVarName(argName);
				argItem.setValue(value);
				argItem.setValType(type);
				argItem.setComment(comment);
				argItem.setTipconf(tipconf);
				
				callArgParser.addItem(argItem);
				
				if ("字符串".equalsIgnoreCase(type)) {
					argStr += ("\"" + value + "\",");
				} else {
					argStr += (value + ",");
				}
			}
			// 参数获取完毕，去掉字串最后的逗号

			if (argStr.length() > 0 && argStr.endsWith(",")) {
				argStr = argStr.substring(0, argStr.length() - 1);
			}
			callNode.setArgs(callArgParser.toString());

			// 如果有返回，则处理返回数据
			//if (!"void".equalsIgnoreCase(callNode.getRetType())) {
			if (data.size() > 1) {
				//当data.size()>1时，说明表格数据中含有返回数据
				Map<Integer, Map<Integer, Object>> returnData = data.get(1);
				CallArgParser retArgParser = new CallArgParser();
				for (Entry<Integer, Map<Integer, Object>> eachRow : returnData.entrySet()) {
					Map<Integer, Object> map = eachRow.getValue();
					String argType = Convert.convertSpliter((String) map.get(1), true);// 获取数据类型
					String argName = Convert.convertSpliter((String) map.get(2), true);// 获取变量名
					String value = Convert.convertSpliter((String) map.get(3), true);// 获取参数值
					String valType = Convert.convertSpliter((String) map.get(4), true);// 获取参数类型
					String describe = Convert.convertSpliter((String) map.get(5), true);// 获取参数描述
					String tipconf = Convert.convertSpliter((String) map.get(6), true);// 获取参数描述
					
					argType = argType.length()==0? "<NOSET>" : argType;
					argName = argName.length()==0? "<NOSET>" : argName;
					value = value.length()==0? "<NOSET>" : value;
					valType = valType.length()==0? "<NOSET>" : valType;
					describe = describe.length()==0? "<NOSET>" : describe;
					tipconf = tipconf.length()==0? "<NOSET>" : tipconf;
					
					CallArgItem argItem = new CallArgItem();
					argItem.setVarType(argType);
					argItem.setVarName(argName);
					argItem.setValue(value);
					argItem.setValType(valType);
					argItem.setComment(describe);
					argItem.setTipconf(tipconf);
					retArgParser.addItem(argItem);
					
					
					
				}//循环处理每个返回项结束
				
				callNode.setRetType(retArgParser.toString());
			}
			//处理返回数据结束

		}// end of (data!=null)

		callNode.setName((String) params.get("nodename"));

		// 将设置参数写入comment
		String bixfile = (String) params.get("bixfile");
		bixfile = (bixfile == null ? "" : bixfile.trim());
		callNode.setDisptip(bixfile);
		
		String comment = (String) params.get("comment");
		callNode.setComment(comment);

		Log.debug("BixCallEditPartUtil.dealDlgOK()-> bixfile:" + bixfile);
		String prefix = getPrefix(bixfile);

		if(callNode instanceof Bixref){
			//如果是bixref，不改External
			callNode.setFuncName(prefix);
			String chooseFile = (String) params.get("chooseFile");
			log.debug("BixCallEditPartUtil dealDlgOK()-> chooseFile:" + chooseFile);
			if(StringUtils.isNotEmpty(chooseFile)){
				//chooseFile = Apriori/Apriori_candidateset/src/datamining/apriori/kkkoui.bix/first.bix
				//oldVal	bixref:$DataMining/resources/Apriori/Apriori_candidateset/src/datamining/apriori/kkkoui.bix/three.bix
				String refStr = callNode.getExternal();
				int resIndex = refStr.indexOf("/resources/");
				if(resIndex>0){
					refStr = refStr.substring(0, resIndex+11).concat(chooseFile);
				}
				callNode.setExternal(refStr);
			}
		}else{
			//当不是IDataPort接口时，或不是逻辑流调用时，才修改扩展属性
			if(!(Constant.IDATAPORT_STR.equals(callNode.getExternal())||Constant.LOGIC_COMPONENT_EXECUTE.equalsIgnoreCase(callNode.getFuncName()))){
				if (bixfile != null && bixfile.contains("/")) {
					// 如果有引用工程文件
					callNode.setExternal(prefix + "(" + argStr + ")");
					callNode.setFuncName(prefix);
				} else {
					callNode.setExternal(callNode.getFuncName() + "(" + argStr + ")");
				}
			}
		}
	}
	/**
	 * 处理对话框确定后返回的数据
	 * 
	 * @param callNode
	 * @param params
	 */
	public static void mapPutdealDlgOK(Call callNode, Map<String, Object> params) {
		// TODO Auto-generated method stub
		// argStr为生成代码()中间的部分
		String  argStr = "";
		Map<Integer,String> hiddenMap = (Map<Integer, String>) params.get(TableHiddenType.HIDDEN);
		// 获取返回的数据
		@SuppressWarnings("unchecked")
		List<Map<Integer, Map<Integer, Object>>> data = (List<Map<Integer, Map<Integer, Object>>>) params.get("table");

		int rowIndex = 0;
		if (data != null && data.size() > 0) {
			// 处理参数表格
			Map<Integer, Map<Integer, Object>> inputData = data.get(0);
			CallArgParserMapPut callArgParserMapPut = new CallArgParserMapPut();
			for (Entry<Integer, Map<Integer, Object>> eachRow : inputData.entrySet()) {
				Map<Integer, Object> map = eachRow.getValue();
				String argKey = (String) map.get(0);// 获取键
				String argKeyType = (String) map.get(1);// 获取键类型
				String value = Convert.convertSpliter((String) map.get(2), true);// 获取值
				String type = Convert.convertSpliter((String) map.get(3), true);// 获取值类型
				// 对value,type,comment进行为空的处理
				if (StringUtils.isEmpty(value))
					value = "<NOSET>";
				if (StringUtils.isEmpty(type))
					type = "<NOSET>";
				
				if(hiddenMap!=null){
					String hiddenStr = hiddenMap.get(rowIndex);
					log.debug("BixCallEditPartUtil mapPutdealDlgOK()-> hiddenMap.get("+rowIndex+")=" + hiddenStr);
					while(StringUtils.isNotEmpty(hiddenStr)){
						CallArgItemMapPut hiddenItem = new CallArgItemMapPut(hiddenStr);
						String setVal = hiddenItem.getValue();
						if(StringUtils.isNumeric(setVal) || "true".equalsIgnoreCase(setVal) || "false".equalsIgnoreCase(setVal)){
							hiddenItem.setValType("表达式");
						}else{
							hiddenItem.setValType("常量");
						}
						callArgParserMapPut.addItem(hiddenItem);
						//添加一个后，需要再加一个计数
						rowIndex++;
						// 如果有连续的，接着找下面一个
						hiddenStr = hiddenMap.get(rowIndex);
					}
				}
				rowIndex++;

				CallArgItemMapPut argItem = new CallArgItemMapPut();
				argItem.setArgKey(argKey);
				argItem.setArgKeyType(argKeyType);
				argItem.setValue(value);
				argItem.setValType(type);
				
				callArgParserMapPut.addItem(argItem);
				
				if ("字符串".equalsIgnoreCase(type)) {
					argStr += ("\"" + value + "\",");
				} else {
					argStr += (value + ",");
				}
			}
			// 参数获取完毕，去掉字串最后的逗号

			if (argStr.length() > 0 && argStr.endsWith(",")) {
				argStr = argStr.substring(0, argStr.length() - 1);
			}
			String hashMap = (String)params.get("hashmap");//获取HashMap输入框的值
			callNode.setArgs(callArgParserMapPut.toString()+"|"+hashMap);		
		}// end of (data!=null)

		callNode.setName((String) params.get("nodename"));

		// 将设置参数写入comment
		String bixfile = (String) params.get("bixfile");
		bixfile = (bixfile == null ? "" : bixfile.trim());
		callNode.setDisptip(bixfile);
		
		String comment = (String) params.get("comment");
		callNode.setComment(comment);

		Log.debug("BixCallEditPartUtil.mapPutdealDlgOK()-> bixfile:" + bixfile);
		String prefix = getPrefix(bixfile);

		if(callNode instanceof Bixref){
			//如果是bixref，不改External
			callNode.setFuncName(prefix);
			String chooseFile = (String) params.get("chooseFile");
			log.debug("BixCallEditPartUtil mapPutdealDlgOK()-> chooseFile:" + chooseFile);
			if(StringUtils.isNotEmpty(chooseFile)){
				//chooseFile = Apriori/Apriori_candidateset/src/datamining/apriori/kkkoui.bix/first.bix
				//oldVal	bixref:$DataMining/resources/Apriori/Apriori_candidateset/src/datamining/apriori/kkkoui.bix/three.bix
				String refStr = callNode.getExternal();
				int resIndex = refStr.indexOf("/resources/");
				if(resIndex>0){
					refStr = refStr.substring(0, resIndex+11).concat(chooseFile);
				}
				callNode.setExternal(refStr);
			}
		}else{
			//当不是IDataPort接口时，或不是逻辑流调用时，才修改扩展属性
			if(!(Constant.IDATAPORT_STR.equals(callNode.getExternal())||Constant.LOGIC_COMPONENT_EXECUTE.equalsIgnoreCase(callNode.getFuncName()))){
				if (bixfile != null && bixfile.contains("/")) {
					// 如果有引用工程文件
					callNode.setExternal(prefix + "(" + argStr + ")");
					callNode.setFuncName(prefix);
				} else {
					callNode.setExternal(callNode.getFuncName() + "(" + argStr + ")");
				}
			}
		}
	}
	
	public static void dataPortDealDlgOK(Call callNode, Map<String, Object> params) {
		thisRundealDlgOK(callNode, params);
	}
	
	/**
	 * 处理对话框确定后返回的数据
	 * 
	 * @param callNode
	 * @param params
	 */
	public static void thisRundealDlgOK(Call callNode, Map<String, Object> params) {
		// TODO Auto-generated method stub
		// argStr为生成代码()中间的部分
		String  argStr = "";
		// 获取返回的数据
		@SuppressWarnings("unchecked")
		List<Map<Integer, Map<Integer, Object>>> data = (List<Map<Integer, Map<Integer, Object>>>) params.get("table");

		if (data != null && data.size() > 0) {
			// 处理参数表格
			Map<Integer, Map<Integer, Object>> inputData = data.get(0);
			log.debug("BixCallEditPartUtil.thisRundealDlgOK()->inputData.entrySet().size():" + inputData.entrySet().size());
			ThisRunArgParser thisRunArgParser = new ThisRunArgParser();
			for (Entry<Integer, Map<Integer, Object>> eachRow : inputData.entrySet()) {
				Map<Integer, Object> map = eachRow.getValue();
				Integer seq = (Integer) map.get(0);
				String varType = Convert.convertSpliter((String) map.get(1), true);// 获取值
				String varName = Convert.convertSpliter((String) map.get(2), true);// 获取值类型
				if (StringUtils.isEmpty(varType))
					varType = "<NOSET>";
				if (StringUtils.isEmpty(varName))
					varName = "<NOSET>";

				ThisRunArgItem argItem = new ThisRunArgItem();
				argItem.setSeq(seq.intValue());
				argItem.setVarType(varType);
				argItem.setVarName(varName);
				
				thisRunArgParser.addItem(argItem);
			}
			// 参数获取完毕，去掉字串最后的逗号

			String retValue = (String)params.get("retValue");//获取retValue输入框的值
			String retType = (String)params.get("retType");//获取retType下拉列表值
			callNode.setRetType(retType + ":" + retValue);
			callNode.setArgs(thisRunArgParser.toString());
		}// end of (data!=null)

		callNode.setName((String) params.get("nodename"));

		// 将设置参数写入comment
		String bixfile = (String) params.get("bixfile");
		bixfile = (bixfile == null ? "" : bixfile.trim());
		callNode.setDisptip(bixfile);
		
		String comment = (String) params.get("comment");
		callNode.setComment(comment);

		Log.debug("BixCallEditPartUtil.mapPutdealDlgOK()-> bixfile:" + bixfile);
		String prefix = getPrefix(bixfile);

		
		//当不是IDataPort接口时，或不是逻辑流调用时，才修改扩展属性
		if(!(Constant.IDATAPORT_STR.equals(callNode.getExternal())||Constant.LOGIC_COMPONENT_EXECUTE.equalsIgnoreCase(callNode.getFuncName()))){
			if (bixfile != null && bixfile.contains("/")) {
				// 如果有引用工程文件
				callNode.setExternal(prefix + "(" + argStr + ")");
				callNode.setFuncName(prefix);
			} else {
				callNode.setExternal(callNode.getFuncName() + "(" + argStr + ")");
			}
		}
	}

	/**
	 * 为构件对话框准备参数
	 * 
	 * @param call
	 *            传入的节点
	 * @return 返回的参数对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> paramBuilder(BixCall call) {
		Map<String, Object> params = new HashMap<String, Object>();
		String external = call.getExternal();
		String args = call.getArgs();
		String retType = call.getRetType();
		args = args == null ? "" : args.trim();
		retType = retType == null ? "" : retType.trim();
		//		retType="";

		Map<Integer, Map<Integer, Object>>[] data = new HashMap[2];
		Map<Integer, Map<Integer, Object>> tableInput;
		Map<Integer, Map<Integer, Object>> tableOutput;

		if (args.length() > 0) {
			tableInput = convertArgToTable(args,params);
			data[0] = tableInput;
		} else {
			//Log.debug("BixCallEditPartUtil.paramBuilder() args为空，不生成表格数据");
		}
		
		if (retType.length() > 0){
			tableOutput = convertReturnToTable(retType);
			data[1] = tableOutput;
		}else{
			//Log.debug("BixCallEditPartUtil.paramBuilder()-> retType为空，不生成返回数据");
		}
		params.put("table", data);

		params.put("nodename", call.getName());
		params.put("comment", call.getComment());
		params.put("bixfile", call.getDisptip());

		return params;
	}

	/**
	 * 将返回参数转换为对话框中的表格数据
	 * @param retType 其格式为=>  数据类型：数据名：值：值类型：描述|数据类型：数据名：值：值类型：描述
	 * @return
	 */
	public static Map<Integer, Map<Integer, Object>> convertReturnToTable(String retType) {
		Map<Integer, Map<Integer, Object>> tableOutput = new HashMap<Integer, Map<Integer, Object>>();

		String[] eachRets = retType.split("\\|");
		for (int i = 0; i < eachRets.length; i++) {
			Map<Integer, Object> row = new HashMap<Integer, Object>();
			String defItems[] = ParamUtil.praseStrToArray(eachRets[i], 6);

			row.put(0, "返回");
			for (int j = 0; j < defItems.length-1; j++) {
				if (j == 3) {
					if (defItems[j].length() == 0) {
						//从配置的库中拖出节点时，当值类型未配置时，初始化值类型
						defItems[3]="变量";
					}
				}
				row.put(j+1, defItems[j]);
			}
			row.put(TableHiddenType.TIP_RENDER, defItems[5]);
			
			tableOutput.put(i, row);
		}

		return tableOutput;
	}
	
	/**
	 * 将arg中的参数描述转化为对话框中的表格数据
	 * @param args 参数描述字符串
	 * @return
	 */
	public static Map<Integer, Map<Integer, Object>> convertArgToTable(String args,Map<String, Object> params) {
		Map<Integer, Map<Integer, Object>> tableInput = new HashMap<Integer, Map<Integer, Object>>();
		args = (args == null ? "" : args.trim());
		//Log.debug("BixCallEditPartUtil.convertArgToTable() args:" + args);

		// 处理参数
		String[] argArray = args.split("\\|");
		//tableInput = new HashMap<Integer, Map<Integer, Object>>();
		Map<Integer,String> hiddenMap = new HashMap<Integer,String>();

		for (int i = 0; i < argArray.length; i++) {
			Map<Integer, Object> row = new HashMap<Integer, Object>();

			// 将每个参数分解为名字和类型，如int:x:3:type:说明|数据类型：数据名：参数值：值类型：参数说明
			String p[] = ParamUtil.praseStrToArray(argArray[i], 6);
			
			if("hidden".equalsIgnoreCase(p[0])){
				hiddenMap.put(i, argArray[i]);
				log.debug("BixCallEditPartUtil convertArgToTable()-> :hiddenMap.put("+i+", "+argArray[i]+")");
			}else{
				
				row.put(0, "参数");
				for (int j = 0; j < p.length-1; j++) {
					if (j == 3) {
						if (p[j].length() == 0) {
							//从配置的库中拖出节点时，当值类型未配置时，初始化值类型
//						if ("String".equalsIgnoreCase(p[0]) || "Func".equalsIgnoreCase(p[0])) {
//							p[3] = "字符串";
//						} else if ("JSON".equalsIgnoreCase(p[0]) || "Object".equalsIgnoreCase(p[0])) {
//							p[3] = "表达式";
//						} else if (p[3].length() == 0) {
//							p[3] = "表达式";
//						}
							p[3]="变量";
						}
					}
					
					row.put(j + 1, p[j]);
				}
				row.put(TableHiddenType.TIP_RENDER, p[5]);
				tableInput.put(i, row);
			}

		}
		if(hiddenMap.size()>0){
			params.put(TableHiddenType.HIDDEN, hiddenMap);
		}
		return tableInput;
	}
	
	
	public static Map<String, Object> mapPutParamBuilder(Call callNode) {
		String args = callNode.getArgs();
		//System.out.println(args);
		args = (args == null ? "" : args.trim());
		//Log.debug("BixCallEditPartUtil.convertArgToTable() args:" + args);

		// 处理参数
		String[] argArray = args.split("\\|");
		Map<String, Object> params = new HashMap<String, Object>();
		
		String variadic = callNode.getVariadic();
		VelocityContext vc = new VelocityContext();
		if (variadic != null && variadic.length() > 0) {
			String[] items = variadic.split("\\|");
			params.put(Constant.VARIADIC_ARG, new CallArgItem(items[1]));
			params.put(Constant.VARIADIC_INDEX, new Integer(items[0]));
			vc.put("showbar", "true");
		}
		params.put(DialogConstants.ParamsAttributes.VELOCITYCONTEXT, vc);
		
		Map<Integer, Map<Integer, Object>> tableInput = new HashMap<Integer, Map<Integer, Object>>();
		boolean haveValue = true;
		for (int i = 0; i < argArray.length-1; i++) {
			Map<Integer, Object> row = new HashMap<Integer, Object>();
			// 将每个参数分解为名字和类型，如int:x:3:type:说明|数据类型：数据名：参数值：值类型：参数说明
			String p[] = ParamUtil.praseStrToArray(argArray[i], 7);
			for (int j = 0; j < p.length-1; j++) {
				//System.out.println(j+":"+p[j]);
				if (j == 0&&("java.util.HashMap".equals(p[0])||"java.lang.Object".equals(p[0]))) {
					p[0]="";
					haveValue = false;
				}else if (j == 1&&(!"表达式".equals(p[1])&&!"变量".equals(p[1]))) {
					p[1]="常量";
				}else if(j == 3&&(!"表达式".equals(p[3])&&!"常量".equals(p[3]))){
					p[3]="变量";
				}
				row.put(j, p[j]);
			}
			
			tableInput.put(i, row);

		}
		//System.out.println("tableInput:"+tableInput);
		Map<Integer, Map<Integer, Object>>[] data = new HashMap[1];
		data[0] = tableInput;
		
		
		params.put("table", data);
		if(haveValue){
			params.put("hashMap", argArray[argArray.length-1]);
		}
		params.put("nodename", callNode.getName());
		params.put("comment", callNode.getComment());
		params.put("bixfile", callNode.getDisptip());

		return params;
	}
	
	public static Map<String, Object> dataPortParamBuilder(Call callNode) {
		String args = callNode.getArgs();
		args = (args == null ? "" : args.trim());

		DataPortArgParser dataPortArgParser = new DataPortArgParser(args);
		log.debug("BixCallEditPartUtil.dataPortParamBuilder()->dataPortArgParser.getItemSet().size():" + dataPortArgParser.getItemSet().size());
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		
		
		Map<Integer, Map<Integer, Object>> tableInput = new HashMap<Integer, Map<Integer, Object>>();
		int index = 0;
		for (DataPortArgItem argItem : dataPortArgParser.getItemSet()) {
			Map<Integer, Object> row = new HashMap<Integer, Object>();
			row.put(0, argItem.getSeq());
			row.put(1, argItem.getVarType());
			row.put(2, argItem.getVarName());
			tableInput.put(index++, row);
		}
		Map<Integer, Map<Integer, Object>>[] data = new HashMap[1];
		data[0] = tableInput;
		
		
		params.put("table", data);
		String retItems[] = StringUtils.split(callNode.getRetType(), ":");
		if(retItems.length>=2){
			params.put("retType", retItems[0]);
			params.put("retValue", retItems[1]);
		}

		params.put("nodename", callNode.getName());
		params.put("comment", callNode.getComment());
		String disptip[] = StringUtils.split(callNode.getDisptip(),".");
		//处理显示名
		if(disptip.length>2){
			params.put("bixfile", "DATAPORT." + disptip[1]);
		}else{
			params.put("bixfile", callNode.getDisptip());
		}

		return params;
	}
	
	public static Map<String, Object> thisRunParamBuilder(Call callNode) {
		String args = callNode.getArgs();
		args = (args == null ? "" : args.trim());

		ThisRunArgParser thisRunArgParser = new ThisRunArgParser(args);
		log.debug("BixCallEditPartUtil.thisRunParamBuilder()->thisRunArgParser.getItemSet().size():" + thisRunArgParser.getItemSet().size());
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		
		
		Map<Integer, Map<Integer, Object>> tableInput = new HashMap<Integer, Map<Integer, Object>>();
		int index = 0;
		for (ThisRunArgItem argItem : thisRunArgParser.getItemSet()) {
			Map<Integer, Object> row = new HashMap<Integer, Object>();
			row.put(0, argItem.getSeq());
			row.put(1, argItem.getVarType());
			row.put(2, argItem.getVarName());
			tableInput.put(index++, row);
		}
		Map<Integer, Map<Integer, Object>>[] data = new HashMap[1];
		data[0] = tableInput;
		
		
		params.put("table", data);
		params.put("retValue", callNode.getRetType());

		params.put("nodename", callNode.getName());
		params.put("comment", callNode.getComment());
		String disptip[] = StringUtils.split(callNode.getDisptip(),".");
		//处理显示名
		if(disptip.length>2){
			params.put("bixfile", "DATAPORT." + disptip[1]);
		}else{
			params.put("bixfile", callNode.getDisptip());
		}

		return params;
	}


	/**
	 * 从文件路径中提取调用的前缀，如kkk/bbb/abc.bix/fun.bix，提取为abc.fun
	 * 
	 * @param file
	 *            传入的文件路径
	 * @return
	 */
	public static String getPrefix(String file) {
		return PixBixCallUtil.getBixPrefix(file);
	}
}
