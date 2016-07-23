package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;

import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.logic.Entity;
import com.sunsheen.jfids.studio.logic.IfLink;
import com.sunsheen.jfids.studio.logic.Node;
import com.sunsheen.jfids.studio.logic.impl.AssignmentImpl;
import com.sunsheen.jfids.studio.logic.impl.BixrefImpl;
import com.sunsheen.jfids.studio.logic.impl.BlankImpl;
import com.sunsheen.jfids.studio.logic.impl.CallImpl;
import com.sunsheen.jfids.studio.logic.impl.CustomImpl;
import com.sunsheen.jfids.studio.logic.impl.EndImpl;
import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.logic.impl.ForImpl;
import com.sunsheen.jfids.studio.logic.impl.IfImpl;
import com.sunsheen.jfids.studio.logic.impl.StartImpl;
import com.sunsheen.jfids.studio.logic.impl.TransactionImpl;
import com.sunsheen.jfids.studio.logic.impl.TranscommitImpl;
import com.sunsheen.jfids.studio.logic.impl.TransrollbackImpl;
import com.sunsheen.jfids.studio.wther.diagram.compiler.JavaAssignmentArgParse;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.Constant;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;
import com.sunsheen.jfids.studio.wther.diagram.parser.CallArgParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.DataPortArgParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.FlowDefineParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.CallArgItem;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.DataPortArgItem;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.FlowDefineItem;

public class GenerateJavaCodeUtil {
	public static final String SUNSHEEN_LOGIC_LINENUMBER_VALUE = "_SUNSHEEN_HEARKEN_LOGIC_LINENUMBER_VALUE_";
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(GenerateJavaCodeUtil.class.getName());

	private static int tryCounter = 0;
	private static VarStore varStore = null;

	/**
	 * 根据模型创建方法体逻辑字符串
	 * 
	 * @param flow
	 * @return
	 */
	public static String genMethodBody(FlowImpl flow) {
		EList<Node> nodes = flow.getNodes();
		StringBuffer sb = new StringBuffer();
		Node startNode = null;
		Node endNode = null;
		tryCounter = 0;
		varStore = new VarStore(flow.getVarstore());

		// 查找出开始节点和结束节点
		for (Node node : nodes) {
			if (node instanceof StartImpl) {
				startNode = node;
			}
			if (node instanceof EndImpl) {
				endNode = node;
			}
		}
		Node node = startNode.getLink().get(0);
		while (!(node instanceof EndImpl)) {
			sb.append(genJavaCode(flow, node));
			node = getNextNode(node);
		}
		return sb.toString();
	}

	/**
	 * 通过节点找到下一个节点
	 * 
	 * @param node
	 * @return
	 */
	private static Node getNextNode(Node node) {
		if (node != null) {
			if (node instanceof ForImpl) {
				return ((ForImpl) node).getEndlink();
			}
			if (node instanceof IfImpl) {
				return findEndIfNode((IfImpl) node);
			}

			if (node.getLink().size() == 0) {
				if (node instanceof BlankImpl) {
					return ((BlankImpl) node).getBreak();// 这人取第一个不知道会不会出错。
				} else if (node instanceof EndImpl) {// change by LCK 20160322
					// ,为了解决能够让结束结点作业条件的汇聚结点
					return node;
				}
			} else {
				return node.getLink().get(0);// 这人取第一个不知道会不会出错。
			}
		}
		return null;
	}

	/**
	 * 找到结束if的节点
	 * 
	 * @param ifNode
	 * @return
	 */
	private static Node findEndIfNode(IfImpl ifNode) {
		Node retNode = null;

		Node elselink = ifNode.getElselink();

		EList<IfLink> ifLinks = ifNode.getIflinks();
		if (ifLinks.size() > 0) {
			// FIXME 对多条件连线的处理
			/*
			 * for(IfLink ifLink : ifLinks){ if(ifLink.getPriority()==1){//TODO
			 * 2014.04.18 这儿的必须保证连线中有优先级为1的情况，如果把优先级为1的线条删掉了，可能会出问题。以后考虑优化。
			 * retNode = ifLink.getTarget(); } }
			 */
			retNode = ifLinks.get(0).getTarget();
		} else {
			Log.debug("没找到IF相应的下一系列节点");
		}

		ArrayList<Node> nodelist = new ArrayList<Node>();
		if (retNode instanceof EndImpl) {
			nodelist.add(retNode);
		}
		while (retNode != null && !(retNode instanceof EndImpl)) {
			if (retNode instanceof IfImpl) {
				retNode = ((IfImpl) retNode).getElselink();
			} else {
				retNode = getNextNode(retNode);
			}
			nodelist.add(retNode);
		}

		// 判断else中的节点是否出现重合，重合节点即为ENDIF节点
		while (!nodelist.contains(elselink)) {
			elselink = getNextNode(elselink);
		}
		retNode = elselink;

		return retNode;
	}

	private static String genJavaCode(FlowImpl flow, Node node) {
		StringBuffer sb = new StringBuffer();
		int line = 0;

		// 添加行号，为debug做准备。该字符串只在源代码中，编译前会取消掉。
		if (!(node instanceof BlankImpl)) {
			line = node.getLineNum();
			if (line != 0) {
				sb.append(SUNSHEEN_LOGIC_LINENUMBER_VALUE + "=").append(line).append(";\n");
			}
		}

		Entity entity = ((Entity) node);

		if (node.isStarttry() || (entity.getExceptionlink() != null && tryCounter == 0)) {
			// 如果有try设置，则开始生成try语句，如果当前节点有异常连线，并且前面没有设置try，则生成try
			sb.append("try{\n");
			tryCounter++;
		}
		// if (entity.getExceptionlink() != null && tryCounter==0) {
		// sb.append("try{\n");
		// tryCounter++;
		// }

		// 赋值语句
		if (node instanceof AssignmentImpl) {
			AssignmentImpl assignmentImpl = (AssignmentImpl) node;
			sb.append(genAssignment(assignmentImpl.getExternal(), flow.getVarstore()));
		}

		// 判断语句
		if (node instanceof IfImpl) {
			IfImpl ifImpl = (IfImpl) node;
			sb.append(genIfCode(flow, ifImpl,line));
		}
		// 逻辑引用语句
		if (node instanceof CallImpl) {
			CallImpl callImpl = (CallImpl) node;
			sb.append(genCallCode(flow, callImpl));
		}
		// 循环语句
		if (node instanceof ForImpl) {
			ForImpl forImpl = (ForImpl) node;
			sb.append(genForCode(flow, forImpl));
		}
		// 逻辑流引用语句
		if (node instanceof BixrefImpl) {
			BixrefImpl bixrefImpl = (BixrefImpl) node;
			sb.append(genBixrefCode(flow, bixrefImpl));
		}
		// 逻辑流引用语句
		if (node instanceof CustomImpl) {
			CustomImpl customImpl = (CustomImpl) node;
			sb.append(genCustomCode(flow, customImpl));
		}

		// -------------------以下为事务类型--------------------
		// 事务开始
		if (node instanceof TransactionImpl) {
			TransactionImpl transactionImpl = (TransactionImpl) node;
			// ASTUtil.getInstance().importPkg(
			// unit,
			// new String[] { "org.hibernate.Session", "java.util.HashMap",
			// "java.util.Map", "com.sunsheen.jfids.system.database.IdsSession",
			// "com.sunsheen.jfids.util.IdsDataBaseUtil" });
			String transactionBeginCode = TransactionCodeUtil
					.getTransactionBeginCode(transactionImpl.getExternal(),
							transactionImpl.isTransfer());

			// 如果前面的代码已生成try,则把try放在session定义和事务开始之间
			int lastTryLoc = sb.lastIndexOf("try");
			if (lastTryLoc != -1) {
				int firstLoc = transactionBeginCode.indexOf(";") + 1;
				String leftCode = transactionBeginCode.substring(0, firstLoc);
				String rightCoder = transactionBeginCode.substring(firstLoc);

				sb.insert(lastTryLoc, leftCode);
				sb.append(rightCoder);
			} else {
				sb.append(transactionBeginCode);
			}

		}
		// 事务提交
		if (node instanceof TranscommitImpl) {
			TranscommitImpl transcommitImpl = (TranscommitImpl) node;

			// 如果是事务提交节点
			sb.append(TransactionCodeUtil
					.getTransactionCommitCode(transcommitImpl.getExternal()));
			// 检查提交节点是否有连接异常连线.@SS 2014-04-20 这儿可能可以去掉。
			// Entity checkExceptionLink = transcommitImpl.getExceptionlink();
			// if (checkExceptionLink == null) {
			// // 没有设置回调连线时，在此补充catch代码
			// sb.append("}catch(Exception e").append(sb.hashCode()).append("){}").append("\n");
			// }

		}
		// 事务回滚
		if (node instanceof TransrollbackImpl) {
			TransrollbackImpl transrollbackImpl = (TransrollbackImpl) node;

			/*
			 * Entity entity = transrollbackImpl.getExceptionlink();
			 * if(entity!=null){ sb.append("try{\n"); }
			 */

			// 如果是事务回滚节点
			sb.append(TransactionCodeUtil
					.getTransactionRollbackCode(transrollbackImpl.getExternal()));

			/*
			 * //如果是JAVA考虑exception if(entity!=null){ //生成catch语句
			 * sb.append("}catch(Exception e"
			 * ).append(sb.hashCode()).append("){").append("\n");
			 * sb.append(genBranchFromNode(unit, flow,(Node)entity));
			 * sb.append("}").append("\n"); }
			 */
		}

		if (entity.getBreak() != null) {
			sb.append("break;\n");
		}
		// 如果是JAVA考虑exception
		if (entity.getExceptionlink() != null) {
			// 生成catch语句
			String eVar = "e" + sb.hashCode();
			sb.append("}catch(Exception ").append(eVar).append("){")
					.append("\n");
			// e.printStackTrace();
			sb.append(eVar).append(".printStackTrace();");
			sb.append(genBranchFromNode(flow, (Node) entity.getExceptionlink()));
			sb.append("}").append("\n");
		}

		return sb.toString();
	}

	/**
	 * 生成Custom节点代码
	 * 
	 * @param customNode
	 * @return
	 */
	public static String genCustomCode(FlowImpl flow, CustomImpl customNode) {
		StringBuilder sb = new StringBuilder();
		String ext = customNode.getExternal();
		if (ext != null && ext.length() > 0) {
			// 如果有设置值，每行数据单独处理，统一缩进格式
			String lines[] = ext.split("\r\n");
			for (String line : lines) {
				sb.append(line).append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 返回Bixref调用节点的代码
	 * 
	 * @param node
	 *            传入的bixref节点
	 * @return 生成的调用的代码
	 */
	private static String genBixrefCode(FlowImpl flow, BixrefImpl bixref) {
		String retVal = "";
		String ext = bixref.getExternal();
		String args = bixref.getArgs();
		String rets = bixref.getRetType();
		if (args == null)
			args = "";
		if (rets == null)
			rets = "";

		if (ext != null && ext.length() > 0) {
			String callCode = genMethodCallStr(ext, args, rets);
			retVal = callCode + ";\n";
		}
		return retVal;
	}

	/**
	 * 将文件路径和参数信息处理为调用代码
	 * 
	 * @param relativePath
	 *            相对工程的路径，如：/abc/book/src/pkg/gg.bix/StringConcat.bix
	 * @param argStr
	 *            参数信息，如：String:arg1:aaa:常量:<NOSET>:<NOSET>|String:arg2:bbb:常量:<
	 *            NOSET>:<NOSET>|
	 * @param rets
	 *            返回数据，如：String:ret1:name1:变量:<NOSET>:<NOSET>
	 * @return 调用字串如：pkg.gg.StringConcat("aaa","bbb")
	 */
	private static String genMethodCallStr(String relativePath, String argStr,
			String rets) {
		StringBuilder sb = new StringBuilder();

		// 处理返回
		if (rets != null && rets.length() > 0) {
			// FIXME 对返回数据只处理一个，不支持多个返回，
			// 一个调用只处理一个返回，如果有多个返回，则后面的返回设置忽略
			// 只处理返回变量，所有的返回设置只支持变量返回
			// 将字串中的返回变量拼接到代码中
			// String retItems[] = rets.split(":");
			String retItems[] = ParamUtil.praseStrToArray(rets, 6);
			if (StringUtils.isNotBlank(retItems[2])) {
				sb.append(retItems[2]).append(" = ");
			}
		}

		// 生成调用串，如：(new pkg.classname()).method
		String methodFullStr = PathConvert.convertMethodStr(relativePath);
		int dotIndex = methodFullStr.lastIndexOf(".");
		String classStr = methodFullStr.substring(0, dotIndex);
		String methodName = methodFullStr.substring(dotIndex + 1);
		sb.append("(new ").append(classStr).append("()).").append(methodName);// @SS
		// 2014-04-20 注释掉。改成静态调用。
		// sb.append(classStr).append(".").append(methodName);

		if (argStr != null && argStr.trim().length() > 0) {
			StringBuilder arg = new StringBuilder();

			// 如果有参数
			for (String eachArg : argStr.split("\\|")) {
				String argItems[] = ParamUtil.praseStrToArray(eachArg, 6);
				// FIXME 对不同类型参数的处理

				if (StringUtils.isNotBlank(argItems[2])) {
					// 目前只考虑了字符串参数
					if ("常量".equalsIgnoreCase(argItems[3])) {
						if (!argItems[2].startsWith("\"")) {
							arg.append("\"");
						}
						if (argItems[2].endsWith("\"")) {
							arg.append(argItems[2]).append(",");
						} else {
							arg.append(argItems[2]).append("\",");
						}
					} else {
						arg.append(argItems[2]).append(",");
					}
				}
			}

			// 去掉最后一个逗号
			if (arg.toString().endsWith(",")) {
				arg.deleteCharAt(arg.lastIndexOf(","));
			}

			sb.append("(").append(arg).append(")");
		} else {
			sb.append("()");
		}
		return sb.toString();
	}

	/**
	 * 生成FOR节点代码
	 * 
	 * @param forid
	 * @return
	 */
	private static String genForCode(FlowImpl flow, ForImpl forNode) {
		StringBuilder sb = new StringBuilder();
		// String forid = forNode.getId();
		Node nextNode = null;
		EList<Node> nodes = forNode.getLink();
		if (nodes.size() > 0) {
			nextNode = nodes.get(0);
		} else {
			nextNode = forNode.getBreak();
		}
		// Node nextNode = getNextNode(forNode);//可能有问题
		String ext = forNode.getExternal();
		// 开始输出代码
		sb.append(genLoopStart(ext)).append("\n");
		do {
			// nextNode = getNextNode(nextNode);
			sb.append(genJavaCode(flow, nextNode));
			nextNode = getNextNode(nextNode);
		} while (nextNode.hashCode() != forNode.hashCode());
		// 用结束的空节点作为最后行号
		sb.append(genLoopEnd(ext, forNode.getEndlink().getLineNum())).append(
				"\n");

		return sb.toString();
	}

	private static String genLoopStart(String str) {
		StringBuffer sb = new StringBuffer();
		// 传入的str有两种格式，一种是[-sp-]i:20，另一种是普通代码如for(int i=1,.......
		if (str.toLowerCase().startsWith(Constant.LOOP_SIMPLE_MARK)) {
			// 如果是以迭代格式
			String vars[] = str.substring(str.indexOf("]") + 1).split(":");
			if (vars.length == 2) {
				// for(int i=0;i<var;i++){
				sb.append("for(int ").append(vars[0]).append("=0;");
				sb.append(vars[0]).append("<").append(vars[1]).append(";");
				sb.append(vars[0]).append("++){");
			}
		} else {
			// 如果是普通代码，分解字串
			String lines[] = str.split("\r\n");
			if (lines.length > 0) {
				// 返回第一行
				String firstLine = lines[0];
				if (firstLine.endsWith("{")) {
					sb.append(firstLine);
				} else {
					sb.append(firstLine).append("{");
				}

				// FIXME 处理如while do-while循环
				// 添加普通循环变量到类类型的转换
				// 提取for循环变量名
				/*
				 * int eqIndex = firstLine.indexOf("="); if (eqIndex > 0) { //
				 * 如果有= int blankIndex = firstLine.substring(0,
				 * eqIndex).lastIndexOf(" "); if (blankIndex >= 0 && blankIndex
				 * < eqIndex) { // 如果有空格，则截取变量名，进行类型转换 String vStr =
				 * firstLine.substring(blankIndex + 1, eqIndex);
				 * Log.debug("GenerateJavaCodeUtil genLoopStart()-> 循环变量名vStr:"
				 * + vStr);
				 * sb.append("\n").append("java.lang.Integer _obj_").append
				 * (vStr);
				 * sb.append(" = new java.lang.Integer(").append(vStr).append
				 * (");"); } }
				 */

			} else {
				Log.error("JavaGenerator.genLoopStart() 解析行数据异常str:" + str);
			}
		}
		return sb.toString();
	}

	/**
	 * 处理结束循环的语句
	 */
	public static String genLoopEnd(String str, int line) {
		StringBuffer sb = new StringBuffer();
		sb.append(SUNSHEEN_LOGIC_LINENUMBER_VALUE + "=").append(line)
				.append(";\n");
		if (str.startsWith(Constant.LOOP_SIMPLE_MARK)) {
			// 迭代方式
			sb.append("}");
		} else {
			String lines[] = str.split("\r\n");
			if (lines.length > 0) {
				// 返回最后一行
				String lastLine = lines[lines.length - 1];

				if (lastLine.length() > 1) {
					sb.append(lastLine);
					if (!sb.toString().endsWith(";")) {
						sb.append(";");
					}
				} else {
					sb.append("}\n");
				}
			} else {
				Log.error("JavaGenerator.genFor() 解析行数据异常str:" + str);
			}
		}
		return sb.toString();
	}

	/**
	 * 生成Call节点代码
	 * 
	 * @param callNode
	 * @return
	 */
	private static String genCallCode(FlowImpl flow, CallImpl callNode) {
		StringBuffer sb = new StringBuffer();
		String args = callNode.getArgs();
		String retStr = callNode.getRetType();
		String funcName = callNode.getFuncName();
		String ext = callNode.getExternal();

		if (Constant.IDATAPORT_STR.equals(ext)) {
			// 对IDataPort类型的单独处理
			return getIDataPortCallCode(flow, callNode);
		} else if (Constant.MAP_PUT_COMPONENT.equals(ext)) {
			return getMapPutComponentCallCode(callNode.getArgs());
		}
		// 如果是this.*.run形式的构件，不参与验证
		Pattern ptn = Pattern.compile("^this\\.(.*)\\.run$");
		Matcher m = ptn.matcher(callNode.getName());
		if (m.matches()) {
			return getThisRunComponentCallCode(callNode.getArgs(),callNode.getName());
		}
		StringBuffer argStr = new StringBuffer();// 这个是放在括号中的字符

		LogicComponentArgUtil logicComponentArgUtil = new LogicComponentArgUtil(flow.getVarstore());
		Map<String, String> argsMap = new HashMap<String, String>();
		if (args != null && args.length() > 0) {
			// 如果存在参数
			for (String eachArg : args.split("\\|")) {
				String[] argItems = ParamUtil.praseStrToArray(eachArg, 5);
				String type = argItems[0]; // 数据类型
				String argVal = argItems[2]; // 值
				String valType = argItems[3]; // 值类型
				type = (type == null ? "" : type);
				argsMap.put(argVal, type);
				logicComponentArgUtil.appendArg(type, argVal, valType);

				// 处理String类型的常量添加引号，char类型的常量添加单引号
				if ("常量".equals(valType)) {
					if (type.endsWith("String")) {
						if (!argVal.startsWith("\"")) {
							argStr.append("\"");
						}
						if (!argVal.endsWith("\"")) {
							argStr.append(argVal).append("\",");
						} else {
							argStr.append(argVal).append(",");
						}
					} else if (type.endsWith("char")) {
						argStr.append("'").append(argVal).append("',");
					} else {
						argStr.append(argVal).append(",");
					}
				} else {
					argStr.append(argVal).append(",");
				}
			}
			if (argStr.toString().endsWith(",")) {
				argStr.deleteCharAt(argStr.lastIndexOf(","));
			}
		}

		// 取返回值设置
		String retVal = "";
		String retType = "";
		if (retStr != null) {// @SS 2014-04-20 不判断，构件引用或者逻辑流引用会出错-空指针异常。
			String retItems[] = ParamUtil.praseStrToArray(retStr, 5);
			retType = retItems[0];
			retVal = retItems[2];
		}

		// 生成funcStr和（
		String funcStr = "";
		String argstring = argStr.toString();
		boolean isLogicComponentMode = false;
		Map<String, String> defineMap = getVariableDefine(flow);
		// FIXME 检查Object数组的代码生成
		if (Constant.LOGIC_COMPONENT_EXECUTE.equalsIgnoreCase(funcName)) {
			isLogicComponentMode = true;
			if (StringUtils.isNotEmpty(argstring)) {
				String callObjectArrayStr = wrapArg(defineMap, argsMap, logicComponentArgUtil.argStr());
				if (callObjectArrayStr.endsWith(",")) {
					callObjectArrayStr = callObjectArrayStr.substring(0, callObjectArrayStr.length() - 1);
				}
				funcStr = funcName + "(\"" + ext + "\",new Object[]{" + callObjectArrayStr + "});";
			} else {
				funcStr = funcName + "(\"" + ext + "\",null);";
			}
		} else {
			funcStr = funcName + "(" + argstring + ");";
		}

		if (retVal.length() > 0) {
			// 如果有返回
			if (isLogicComponentMode && StringUtils.isNotEmpty(retType)) {
				//需要判断是否需要int v = (Integer)...的类型转换
				String convertFunction = checkConvert(varStore.findVarType(retVal),retType);
				if(StringUtils.isNotEmpty(convertFunction)){
					//如果需要转换
					sb.append(retVal).append(" = ((").append(retType).append(")").append(funcStr.substring(0, funcStr.lastIndexOf(";")));
					sb.append(").").append(convertFunction).append(";");
				}else{
					sb.append(retVal).append(" = (").append(retType).append(")").append(funcStr);
				}
			} else {
				sb.append(retVal).append(" = ").append(funcStr);
			}
		} else {
			sb.append(funcStr);
		}
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * 检查业务逻辑流调用时的返回是否需要类型转换，主要是处理int与Integer这类转换
	 * @param leftType 如:int
	 * @param rightType 如：java.lang.Integer
	 * @return 返回类型到常规类型的转换方法，如:intValue();
	 */
	private static String checkConvert(String leftType, String rightType) {
		String retVal = "";
		//boolean,char,short,int,long,float,double
		if("boolean".equals(leftType) && "java.lang.Boolean".equals(rightType)){
			retVal = "booleanValue()";
		}else if("char".equals(leftType) && "java.lang.Character".equals(rightType)){
			retVal = "charValue()";
		}else if("short".equals(leftType) && "java.lang.Short".equals(rightType)){
			retVal = "shortValue()";
		}else if("int".equals(leftType) && "java.lang.Integer".equals(rightType)){
			retVal = "intValue()";
		}else if("long".equals(leftType) && "java.lang.Long".equals(rightType)){
			retVal = "longValue()";
		}else if("float".equals(leftType) && "java.lang.Float".equals(rightType)){
			retVal = "floatValue()";
		}else if("double".equals(leftType) && "java.lang.Double".equals(rightType)){
			retVal = "doubleValue()";
		}
		
		return retVal;
	}

	/**
	 * 如果参数为基本数据类型 如：int 需要包装类型：Integer.valueOf();
	 * 
	 * @param defineMap
	 *            参数名-变量类型映射
	 * @param argsMap
	 *            构建参数名-变量映射
	 * @param argStr
	 *            参数字符
	 * @return
	 */
	public static String wrapArg(Map<String, String> defineMap,
			Map<String, String> argsMap, String argStr) {
		StringBuilder sb = new StringBuilder();
		argStr = argStr.substring(0, argStr.length() - 1);
		String type = defineMap.get(argStr);
		if (type != null) {
			if (argsMap.get(argStr) != null && argsMap.get(argStr).equalsIgnoreCase("Object")) {
				if (type.endsWith("char")) {
					// Character.valueOf('c');
					sb.append("Character.valueOf(").append(argStr).append("),");
				} else if ("boolean".equalsIgnoreCase(type)) {
					// Boolean.valueOf(i)
					sb.append("Boolean.valueOf(").append(argStr).append("),");
				} else if ("short".equalsIgnoreCase(type)) {
					// Short.valueOf(i)
					sb.append("Short.valueOf(").append(argStr).append("),");
				} else if ("int".equalsIgnoreCase(type) || "Integer".equalsIgnoreCase(type)) {
					// Integer.valueOf(i)
					sb.append("Integer.valueOf(").append(argStr).append("),");
				} else if ("long".equalsIgnoreCase(type)) {
					// Long.valueOf(i)
					sb.append("Long.valueOf(").append(argStr).append("),");
				} else if ("float".equalsIgnoreCase(type)) {
					// Float.valueOf(i)
					sb.append("Float.valueOf(").append(argStr).append("),");
				} else if ("double".equalsIgnoreCase(type)) {
					// Double.valueOf(i)
					sb.append("Double.valueOf(").append(argStr).append("),");
				} else {
					return argStr + ",";
				}
				return sb.toString();
			}
		}
		return argStr + ",";
	}

	/**
	 * 返回方法中变量定义变量名和值类型
	 * 
	 * @param flow
	 * 
	 * @return 变量名与变量类型对于Map映射
	 */
	private static Map<String, String> getVariableDefine(FlowImpl flow) {
		String def = flow.getDefine();
		log.debug("GenerateJavaCodeUtil.getVariableDefine()->def:" + def);

		// 调用转换器进行处理
		FlowDefineParser defParser = new FlowDefineParser(def);
		Map<String, String> map = new HashMap<String, String>();
		for (FlowDefineItem item : defParser.getItemSet()) {
			String name = item.getVarName(); // 变量名
			String valType = item.getType(); // 值类型
			map.put(name, valType);
		}
		return map;
	}

	/**
	 * 返回ThisRunComponent组件的代码生成
	 * 
	 * @param arg
	 *            参数如："key1":表达式:value1:变量:|"key2":表达式:value2:变量:|obj|obj
	 * @return String 生成的代码串，如：return = this.*.run(Map);
	 */
	private static String getThisRunComponentCallCode(String arg, String nodeName) {
		String retVal = "";
		StringBuilder sb = new StringBuilder();
		String argArray[] = StringUtils.split(arg, "|");
		String mapName = "";
		String retVlaueName = "";
		if (argArray.length == 2) {
			mapName = argArray[argArray.length - 1];
		} else if (argArray.length > 2) {
			String[] p = argArray[argArray.length - 2].split(":");
			if (p.length == 1) {
				mapName = argArray[argArray.length - 2];
				retVlaueName = argArray[argArray.length - 1];
			} else {
				mapName = argArray[argArray.length - 1];
			}
		}
		int count = retVlaueName == "" ? 1 : 2;
		for (int i = 0; i < argArray.length - count; i++) {
			String items[] = StringUtils.split(argArray[i], ":");
			sb.append(mapName).append(".put(").append(items[0]).append(",").append(items[2]).append(");\n");
		}
		if (retVlaueName == "") {
			sb.append(nodeName).append("(").append(mapName).append(");\n");
		} else {
			sb.append(retVlaueName).append("=").append(nodeName).append("(").append(mapName).append(");\n");
		}
		retVal = sb.toString();
		return retVal;
	}

	/**
	 * 返回MapPutComponent组件的代码生成
	 * 
	 * @param arg
	 *            参数如："key1":表达式:value1:变量:|"key2":表达式:value2:变量:|obj
	 * @return String 生成的代码串，如：obj.put("key1",value1);obj.put("key2",value2);
	 */
	private static String getMapPutComponentCallCode(String arg) {
		String retVal = "";
		StringBuilder sb = new StringBuilder();
		String argItems[] = StringUtils.split(arg, "|");
		if (argItems.length > 0) {

			String objName = argItems[argItems.length - 1];

			for (int i = 0; i < argItems.length - 1; i++) {
				String items[] = StringUtils.split(argItems[i], ":");
				sb.append(objName).append(".put(").append(items[0]).append(",").append(items[2]).append(");\n");
			}
			retVal = sb.toString();
		}
		return retVal;
	}

	/**
	 * 处理生成IDataPort接口调用的方法
	 * 
	 * @param flow
	 * @param callNode
	 * @return String
	 */
	private static String getIDataPortCallCode(FlowImpl flow, CallImpl callNode) {
		String args = callNode.getArgs();
		String retStr = callNode.getRetType();
		String funcName = callNode.getFuncName();
		
		//1:java.lang.String:name1|2:float:name3


		StringBuffer str = new StringBuffer();// 这个是放在括号中的字符
		Random rand = new Random();
		int randInt = rand.nextInt();
		randInt = (randInt < 0 ? -randInt : randInt);
		String argMapName = "_argMap" + randInt;

		LogicComponentArgUtil logicComponentArgUtil = new LogicComponentArgUtil(flow.getVarstore());
		
		
		DataPortArgParser dataPortArgParser = new DataPortArgParser(args);
		//标记是否封装Map参数
		boolean haveEncapsulationMap = false;
		StringBuilder prepareMapStr = new StringBuilder();
		if(dataPortArgParser.getItemSet().size()==1 && dataPortArgParser.getItemSet().get(0).getVarType().endsWith("Map")){
			//如果只有一个Map类型参数，则不再继续将参数封装成Map
			str.append(funcName).append("(").append(dataPortArgParser.getItemSet().get(0).getVarName()).append(")");
			
		}else{
			haveEncapsulationMap = true;
			str.append(funcName).append("(").append(argMapName).append(")");
			
			prepareMapStr.append("java.util.Map ").append(argMapName).append(" = new com.sunsheen.jfids.system.bizass.util.BixParamMap();\n");
			//prepareMap.append("java.util.Map ").append(argMapName).append(" = new java.util.HashMap();\n");
			for (DataPortArgItem argItem : dataPortArgParser.getItemSet()) {
				int seq = argItem.getSeq();
				String type = argItem.getVarType();
				String varName = argItem.getVarName();
				
				prepareMapStr.append(argMapName).append(".put(\"param").append(seq).append("\",");
				StringBuilder objectDescribe = new StringBuilder(logicComponentArgUtil.parseVarToObjectDescribe(type, varName,"变量"));
				if (objectDescribe.toString().endsWith(",")) {
					objectDescribe.deleteCharAt(objectDescribe.lastIndexOf(","));
				}
				prepareMapStr.append(objectDescribe).append(");\n");
			}
			if (prepareMapStr.toString().endsWith(",")) {
				prepareMapStr.deleteCharAt(prepareMapStr.lastIndexOf(","));
			}
		}


		//处理返回 String:name2
		String retItems[] = StringUtils.split(retStr, ":");
		if(retItems.length>=2){
			//name2 = (java.lang.String)
			String varType = varStore.findVarType(retItems[1]);
			boolean isPrimitiveType = JavaTypeUtil.isPrimitiveType(varType);
			log.debug("GenerateJavaCodeUtil.getIDataPortCallCode()->varType:" + varType);
			log.debug("GenerateJavaCodeUtil.getIDataPortCallCode()->isPrimitiveType:" + isPrimitiveType);
			
			if("boolean".equals(retItems[0])){
				if(isPrimitiveType){
					str.insert(0, retItems[1]+" = ((java.lang.Boolean)(").append(")).booleanValue();\n");
				}else{
					str.insert(0, retItems[1]+" = (java.lang.Boolean)(").append(");\n");
				}
			}else if("char".equals(retItems[0])){
				if(isPrimitiveType){
					str.insert(0, retItems[1]+" = ((java.lang.Character)(").append(")).charValue();\n");
				}else{
					str.insert(0, retItems[1]+" = (java.lang.Character)(").append(");\n");
				}
			}else if("short".equals(retItems[0])){
				if(isPrimitiveType){
					str.insert(0, retItems[1]+" = ((java.lang.Short)(").append(")).shortValue();\n");
				}else{
					str.insert(0, retItems[1]+" = (java.lang.Short)(").append(");\n");
				}
			}else if("int".equals(retItems[0])){
				if(isPrimitiveType){
					str.insert(0, retItems[1]+" = ((java.lang.Integer)(").append(")).intValue();\n");
				}else{
					str.insert(0, retItems[1]+" = (java.lang.Integer)(").append(");\n");
				}
			}else if("long".equals(retItems[0])){
				if(isPrimitiveType){
					str.insert(0, retItems[1]+" = ((java.lang.Long)(").append(")).longValue();\n");
				}else{
					str.insert(0, retItems[1]+" = (java.lang.Long)(").append(");\n");
				}
			}else if("float".equals(retItems[0])){
				if(isPrimitiveType){
					str.insert(0, retItems[1]+" = ((java.lang.Float)(").append(")).floatValue();\n");
				}else{
					str.insert(0, retItems[1]+" = (java.lang.Float)(").append(");\n");
				}
			}else if("double".equals(retItems[0])){
				if(isPrimitiveType){
					str.insert(0, retItems[1]+" = ((java.lang.Double)(").append(")).doubleValue();\n");
				}else{
					str.insert(0, retItems[1]+" = (java.lang.Double)(").append(");\n");
				}
			}else{
				//java.lang.String
				str.insert(0, retItems[1]+" = ("+retItems[0]+")").append(";\n");
			}
		}else{
			log.error("GenerateJavaCodeUtil.getIDataPortCallCode()->:对话框返回参数不合法：" + retStr);
		}
		if(haveEncapsulationMap){
			str.insert(0, prepareMapStr);
		}
		if(!str.toString().endsWith(";\n")){
			str.append(";\n");
		}
		
		log.debug("GenerateJavaCodeUtil.getIDataPortCallCode()->str:" + str);
		
		return str.toString();
	}

	/**
	 * 生成从当前节点开始的分支
	 * 
	 * @param branchNode
	 *            分支开始节点
	 * @return String 返回生成的分支代码字串
	 */
	private static String genBranchFromNode(FlowImpl flow, Node node) {
		StringBuilder sb = new StringBuilder();
		sb.append(genJavaCode(flow, node)).append("\n");
		Node nextNode = getNextNode(node);
		while (nextNode != null) {
			sb.append(genJavaCode(flow, nextNode)).append("\n");
			nextNode = getNextNode(nextNode);
		}
		return sb.toString();
	}

	/**
	 * 处理赋值节点的代码生成，和页面初始变量定义
	 * 
	 * @param str
	 *            模型中的定义字串，如：name1::String:true:true|变量名:默认值:变量类型:是否数组:是否定义
	 * @param tabStr
	 *            生成代码时的制表符
	 * @param define
	 *            已定义好的字串，在赋值时用于判断变量类型，以确定是否添加引号
	 * @return 初始变量的定义代码
	 */
	public static String genAssignment(String str, String varstore) {
		StringBuffer sb = new StringBuffer();
		log.debug("GenerateJavaCodeUtil.genAssignment()->str:" + str);
		if (str != null) {
			for (String eachLine : str.split("\\|")) {
				// 对每行进行处理，调用AssignmentArgParse处理每个赋值语句的生成
				JavaAssignmentArgParse argParse = new JavaAssignmentArgParse(eachLine, varstore);
				sb.append(argParse.getCode()).append("\n");
				log.debug("GenerateJavaCodeUtil.genAssignment()->eachLine:" + eachLine);
				log.debug("GenerateJavaCodeUtil.genAssignment()->argParse.getCode():" + argParse.getCode());
			}
		}
		return sb.toString();
	}

	
	
	/**
	 * 生成IF节点代码
	 * @param flow 容器模型
	 * @param ifImpl 判断节点
	 * @param line 行号
	 * @return
	 */
	public static String genIfCode(FlowImpl flow, IfImpl ifImpl,int line) {
		StringBuilder sb = new StringBuilder();
		Node nextNode = null;
		Node endIfNode = findEndIfNode(ifImpl);
		Node elselink = ifImpl.getElselink();

		EList<IfLink> links = ifImpl.getIflinks();// if判断连线。可能有多条连线，则通过优先级来进行代码的生成顺序.优先级从1开始依次递增，数字越大优先级越小。

		Map<Integer, IfLink> ifLinkMap = new HashMap<Integer, IfLink>();
		for (IfLink ifLink : links) {
			ifLinkMap.put(ifLink.getPriority(), ifLink);
		}

		int linksNumber = links.size();
		for (int i = 1; i <= linksNumber; i++) {
			IfLink ifLink = ifLinkMap.get(i);
			String condition = ifLink.getCondition();
			// 开始输出代码
			if (i == 1) {// TODO 如果最小不为1的情况需要考虑。即优先级为1的连线被删除了。
				sb.append("if(").append(condition).append("){\n");
			} else {
				sb.append(SUNSHEEN_LOGIC_LINENUMBER_VALUE + "=").append(line).append(";\n");
				sb.append("}else if(").append(condition).append("){\n");
			}

			// 找到该线路第一个节点
			nextNode = ifLink.getTarget();
			while (endIfNode.hashCode() != nextNode.hashCode()) {
				sb.append(genJavaCode(flow, nextNode));
				nextNode = getNextNode(nextNode);
			}
		}

		// ---------------------------------------------- end of yes
		if (elselink.hashCode() == endIfNode.hashCode()) {
			// 如果默认节点刚好是ENDIF节点，说明没有else语句
			sb.append(SUNSHEEN_LOGIC_LINENUMBER_VALUE + "=").append(line).append(";\n");
			sb.append("}\n");
		} else {
			sb.append(SUNSHEEN_LOGIC_LINENUMBER_VALUE + "=").append(line).append(";\n");
			sb.append("}else{\n");
			// 输出DEFAULT块
			nextNode = elselink;
			while (endIfNode.hashCode() != nextNode.hashCode()) {
				sb.append(genJavaCode(flow, nextNode));
				nextNode = getNextNode(nextNode);
			}
			sb.append(SUNSHEEN_LOGIC_LINENUMBER_VALUE + "=").append(line).append(";\n");
			sb.append("}\n");
		}
		return sb.toString();

	}
}
