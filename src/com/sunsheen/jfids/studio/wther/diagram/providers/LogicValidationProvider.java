package com.sunsheen.jfids.studio.wther.diagram.providers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IClientSelector;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.View;

import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.logic.Assignment;
import com.sunsheen.jfids.studio.logic.BixCall;
import com.sunsheen.jfids.studio.logic.Blank;
import com.sunsheen.jfids.studio.logic.End;
import com.sunsheen.jfids.studio.logic.Entity;
import com.sunsheen.jfids.studio.logic.Flow;
import com.sunsheen.jfids.studio.logic.For;
import com.sunsheen.jfids.studio.logic.If;
import com.sunsheen.jfids.studio.logic.IfLink;
import com.sunsheen.jfids.studio.logic.Node;
import com.sunsheen.jfids.studio.logic.Start;
import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.run.utils.ProjectUtils;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ErrorRecordsUtils;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.FlowEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.Constant;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FindClass;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;
import com.sunsheen.jfids.studio.wther.diagram.parser.AssignmentParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.CallArgParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.AssignmentItem;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.CallArgItem;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry;

/**
 * @generated
 */
public class LogicValidationProvider {

	/**
	 * @generated
	 */
	private static boolean constraintsActive = false;

	/**
	 * @generated
	 */
	public static boolean shouldConstraintsBePrivate() {
		return false;
	}

	/**
	 * @generated
	 */
	public static void runWithConstraints(TransactionalEditingDomain editingDomain, Runnable operation) {
		final Runnable op = operation;
		Runnable task = new Runnable() {
			public void run() {
				try {
					constraintsActive = true;
					op.run();
				} finally {
					constraintsActive = false;
				}
			}
		};
		if (editingDomain != null) {
			try {
				editingDomain.runExclusive(task);
			} catch (Exception e) {
				LogicDiagramEditorPlugin.getInstance().logError("Validation failed", e); //$NON-NLS-1$
			}
		} else {
			task.run();
		}
	}

	/**
	 * @generated
	 */
	static boolean isInDefaultEditorContext(Object object) {
		if (shouldConstraintsBePrivate() && !constraintsActive) {
			return false;
		}
		if (object instanceof View) {
			return constraintsActive && FlowEditPart.MODEL_ID.equals(LogicVisualIDRegistry.getModelID((View) object));
		}
		return true;
	}

	/**
	 * @generated
	 */
	public static class DefaultCtx implements IClientSelector {

		/**
		 * @generated
		 */
		public boolean selects(Object object) {
			return isInDefaultEditorContext(object);
		}
	}

	/**
	 * @generated
	 */
	public static class Adapter1 extends AbstractModelConstraint {

		/**
		 * 对图中孤立节点进行检查，分别考虑出度和入度
		 * @generated NOT
		 */
		public IStatus validate(IValidationContext ctx) {
			//暂时取消对节点的连接的验证
			Node node = (Node) ctx.getTarget();
			if (node.getLink() == null || node.getLink().size() == 0) {
				//IF,END,BLANK节点不检查出度，这几类节点允许没有出度，IF的出度为条件连线和默认连线
				if (!(node instanceof If || node instanceof End || node instanceof Blank)) {
					return ctx.createFailureStatus(new Object[] { node.getName(), "未连接，请检查" });
				}
			}

			// 对编译出错的处理
			String fileUri = node.eResource().getURI().toString();
			int errNode = node.getAbsLineNum();
			Map<Integer, String> errMap = ErrorRecordsUtils.getInstance().getMapByFile(fileUri);
			if (errMap != null && errMap.containsKey(errNode)) {
				return ctx.createFailureStatus(new Object[] { node.getName(), "编译错误:" + errMap.get(errNode) });
			}

			List<EObject> allNodes = node.eContainer().eContents();
			List<Node> targetList = new ArrayList<Node>();
			for (EObject obj : allNodes) {
				List<Node> curNodeTargets = ((Node) obj).getLink();

				//对判断节点，还考虑判断连线
				if (obj instanceof If) {
					List<IfLink> ifLinks = ((If) obj).getIflinks();
					for (IfLink ifLink : ifLinks) {
						Node targetCallBackNode = ifLink.getTarget();
						if (!targetList.contains(targetCallBackNode)) {
							targetList.add(targetCallBackNode);
						}
					}
					//默认连线
					Node elseNode = ((If) obj).getElselink();
					if (!targetList.contains(elseNode)) {
						targetList.add(elseNode);
					}
				}

				//对循环连接的处理
				if (obj instanceof For) {
					Node endLinkNode = ((For) obj).getEndlink();
					if (!targetList.contains(endLinkNode)) {
						targetList.add(endLinkNode);
					}
				}

				//对break连线的处理
				if (obj instanceof Entity) {
					Node breakNode = ((Entity) obj).getBreak();
					if (breakNode != null) {
						targetList.add(breakNode);
					}
				}

				//对异常连线的处理
				if (obj instanceof Entity) {
					Node nextNode = ((Entity) obj).getExceptionlink();
					if (nextNode != null) {
						targetList.add(nextNode);
					}
				}

				//TODO 对其它类型连线的处理

				//对一般的link连线处理
				for (Node targetNode : curNodeTargets) {
					if (!targetList.contains(targetNode)) {
						//Log.debug("向列表中添加目标节点:" + targetNode.getName());
						targetList.add(targetNode);
					}
				}
			}
			//获取完目标节点数据后，和当前节点进行比较，看当前节点是否有在目标列表中，如果没有，则说明没有入度
			if (!targetList.contains(node) && !(node instanceof Start)) {
				//当前节点不在目标列表中，开始节点可以没有入度
				return ctx.createFailureStatus(new Object[] { node.getName(), "未连接，请检查" });
			}

			return ctx.createSuccessStatus();
		}
	}

	/**
	 * @generated
	 */
	public static class Adapter2 extends AbstractModelConstraint {

		/**
		 * 赋值节点参数必须设置
		 * @generated NOT
		 */
		public IStatus validate(IValidationContext ctx) {
			Assignment assignment = (Assignment) ctx.getTarget();
			FlowImpl flow = (FlowImpl) assignment.eContainer();

			String external = assignment.getExternal();
			if (external == null || assignment.getExternal().length() <= 0) {
				return ctx.createFailureStatus(new Object[] { assignment.getName(), "参数值未设置" });
			} else {
				//有内容，验证输入变量是否在定义中被改变，只验证变量是否存在
				String varStoreStr = flow.getVarstore();
				VarStore varStore = new VarStore(varStoreStr);

				AssignmentParser assignParser = new AssignmentParser(external);
				for (AssignmentItem assignItem : assignParser.getItemSet()) {
					String varName = assignItem.getVarName();
					// varName 可以是变量，也可以是与对象的赋值obj/attr
					varName = varName.contains("/")? varName.substring(0, varName.indexOf("/")) : varName;
					if (!varStore.contains(varName)) {
						return ctx.createFailureStatus(new Object[] { assignment.getName(),"变量" + assignItem.getVarName() + "未定义" });
					}
				}

			}
			return ctx.createSuccessStatus();
		}
	}

	/**
	 * @generated
	 */
	public static class Adapter3 extends AbstractModelConstraint {

		/**
		 * 对判断节点的验证，判断节点要有默认连线和判断连线，并对判断节点的条件设置进行验证
		 * @generated NOT
		 */
		public IStatus validate(IValidationContext ctx) {
			If ifNode = (If) ctx.getTarget();
			if (ifNode.getIflinks() == null || ifNode.getIflinks().size() == 0) {
				return ctx.createFailureStatus(new Object[] { ifNode.getName(), "没有判断连线" });
			} else if (ifNode.getElselink() == null) {
				return ctx.createFailureStatus(new Object[] { ifNode.getName(), "没有默认连线" });
			}

			// branchSets里存放所有分支的后续节点集合，如{{cond1Set},{cond2Set}...}
			Set<Set> branchSets = new HashSet<Set>();
			//对连接条件的检查
			List<IfLink> links = ifNode.getIflinks();
			for (IfLink ifLink : links) {
				Set<Node> currentBranch = fineBehindNodes(ifLink.getTarget());
				if (ifLink.getCondition() == null || ifLink.getCondition().length() == 0) {
					return ctx.createFailureStatus(new Object[] { ifNode.getName(),
							"中条件连线【" + ifLink.getTip() + "】没有设置条件" });
				}
				branchSets.add(currentBranch);
			}

			// 如果前面的验证通过，再检查汇聚节点
			Set<Node> elseBranch = fineBehindNodes(ifNode.getElselink());
			Node lastConvergedNode = null, curConvergedNode = null;
			for (Set<Node> eachConditionNodeSet : branchSets) {
				curConvergedNode = findConvergedNode(eachConditionNodeSet, elseBranch);
				if (curConvergedNode != null) {
					if (lastConvergedNode == null) {
						lastConvergedNode = curConvergedNode;
					} else {
						//比较之前的汇聚节点
						if (!curConvergedNode.equals(lastConvergedNode)) {
							//不相等
							return ctx.createFailureStatus(new Object[] { ifNode.getName(), "汇聚节点不一致" });
						}
					}
				} else {
					//无汇聚节点
					return ctx.createFailureStatus(new Object[] { ifNode.getName(), "无汇聚节点" });
				}
			}

			return ctx.createSuccessStatus();
		}

		/**
		 * 从当前节点找出后续的节点，把后续的节点集返回
		 * @param curNode 当前的起始节点
		 * @return 包含当前节点的后续节点集合
		 */
		private Set<Node> fineBehindNodes(Node curNode) {
			Set<Node> retSet = new HashSet<Node>();
			while (curNode != null) {
				retSet.add(curNode);
				if (curNode instanceof For) {
					curNode = ((For) curNode).getEndlink();
				} else if (curNode instanceof If) {
					//如果是IF节点，下一个节是找else连接的节点
					curNode = ((If) curNode).getElselink();
				} else {
					List<Node> nextNodes = curNode.getLink();
					if (nextNodes != null && nextNodes.size() > 0) {
						curNode = nextNodes.get(0);
					} else {
						break;
					}
				}
			}

			return retSet;
		}

		/**
		 * 从两个节点集合中找出汇聚节点，即第一个公共节点
		 * @param set1 第一个集合
		 * @param set2 第二个集合
		 * @return 返回找到的节点对象，如果没有找到，则返回空
		 */
		private Node findConvergedNode(Set<Node> set1, Set<Node> set2) {
			for (Node eachNode : set1) {
				if (set2.contains(eachNode)) {
					return eachNode;
				}
			}
			return null;
		}
	}

	/**
	 * @generated
	 */
	public static class Adapter5 extends AbstractModelConstraint {

		/**
		 * 不允许两节点间相互连接，且开始节点只允许连出，结束节点只允许接入
		 * @generated NOT
		 */
		public IStatus validate(IValidationContext ctx) {

			Flow flow = (Flow) ctx.getTarget();
			String fileUri = flow.eResource().getURI().toString();

			// 对编译出错的处理
			Map<Integer, String> errMap = ErrorRecordsUtils.getInstance().getMapByFile(fileUri);
			if (errMap != null && errMap.containsKey(0)) {
				return ctx.createFailureStatus(new Object[] { "编译错误:" + errMap.get(0) });
			}

			List<Node> nodes = flow.getNodes();
			//用于验证Try和catch是否成对
			int tryCatchCounter = 0;

			for (Node node : nodes) {
				if (node.isStarttry()) {
					tryCatchCounter++;
				}

				if (node instanceof Entity) {
					Entity entity = (Entity) node;

					if (entity.getExceptionlink() != null) {
						tryCatchCounter--;
					}
				}

				//对每个节点进行判断
				if (node instanceof End) {
					//结束节点
					if (node.getLink().size() != 0) {
						return ctx.createFailureStatus(new Object[] { "结束节点不允许连出" });
					}
				} else {
					//其它节点
					List<Node> targetNodes = node.getLink();
					//获取每个节点的目标节点
					for (Node targetNode : targetNodes) {
						if (targetNode instanceof Start) {
							//如果目标节点是开始节点
							return ctx.createFailureStatus(new Object[] { "开始节点不允许接入" });
						} else if (targetNode instanceof For) {
							continue;
						} else {
							if (targetNode.getLink().contains(node)) {
								if (node instanceof For) {
									//如果连回到for节点，则不验证
									continue;
								}
								return ctx.createFailureStatus(new Object[] { "流程节点【" + node.getName() + "】【"
										+ targetNode.getName() + "】之间不能彼此相互连接" });
							}
						}
					}
				}
			}
			if (tryCatchCounter > 0) {
				return ctx.createFailureStatus(new Object[] { "流程中缺少异常连线" });
			}
			return ctx.createSuccessStatus();
		}
	}

	/**
	 * @generated
	 */
	public static class Adapter7 extends AbstractModelConstraint {

		/**
		 * 对FOR节点进行连接检查，连入线为2条（一条为上一节点，一条为循环返回），连出线为1条，结束连接为1条
		 * @generated NOT
		 */
		public IStatus validate(IValidationContext ctx) {
			For forNode = (For) ctx.getTarget();
			//检查连出线为1，在检查孤立节点时已作验证

			//检查循环结束连线，如果存在，则通过，只有一条的限制在模型中已定义
			if (forNode.getEndlink() == null) {
				return ctx.createFailureStatus(new Object[] { forNode.getName(), "结束循环连线未设置" });
			}
			//检查该循环节点入度
			List<EObject> allNodes = forNode.eContainer().eContents();
			int counter = 0;
			for (EObject node : allNodes) {
				if (((Node) node).getLink().contains(forNode)) {
					counter++;
				}
				//还要添加循环节点直接连接到下一循环节点的连线识别
//				if(node instanceof ForImpl){
//					if(((ForImpl)node).getEndlink().equals(forNode)){
//						counter++;
//					}
//				}
				// 不允许循环节点后没有空节点，空节点作为结束循环的行号;
				// FIXME 添加循环节点结束连线连接空节点的验证
			}
			if (counter != 2) {
				Log.error("LogicValidationProvider.Adapter7.validate -> 循环节点【" + forNode.getName() + "】连入线不正确，当前为（"
						+ counter + "）");
				return ctx.createFailureStatus(new Object[] { forNode.getName(), "连入线不正确" });
			}

			//对循环参数进行验证
			if (StringUtils.isBlank(forNode.getExternal())) {
				return ctx.createFailureStatus(new Object[] { forNode.getName(), "循环参数未配置" });
			}
			//通过验证
			return ctx.createSuccessStatus();
		}
	}

	/**
	 * @generated
	 */
	public static class Adapter8 extends AbstractModelConstraint {

		/**
		 * @generated NOT
		 * 对BixCall节点的设置进行验证
		 */
		public IStatus validate(IValidationContext ctx) {
			BixCall call = (BixCall) ctx.getTarget();
			String argStr = call.getArgs();
			String external = call.getExternal();
			String msg = "";
			boolean pass = true;

			if (Constant.MAP_PUT_COMPONENT.equals(external)) {
				//如果是mapPutComponent，则直接返回，不做验证
				return ctx.createSuccessStatus();
			}
			//如果是this.*.run形式的构件，不参与验证
			Pattern ptn = Pattern.compile("^this\\.(.*)\\.run$");
			Matcher m = ptn.matcher(call.getName());
			if (m.matches()) {
				return ctx.createSuccessStatus();
			}
			//验证变量改变
			FlowImpl flow = (FlowImpl) call.eContainer();
			VarStore varStore = new VarStore(flow.getVarstore());

			CallArgParser argParser = new CallArgParser(argStr);
			for (CallArgItem item : argParser.getItemSet()) {
				if (StringUtils.isEmpty(item.getValue())) {
					//如果有值没有填写
					msg = "参数值未设置值";
					pass = false;
					break;
				}
				if ("变量".equals(item.getValType())) {
					if (!varStore.contains(item.getValue())) {
						//如果有值没有定义
						msg = "参数" + item.getVarName() + "变量【" + item.getValue() + "】未定义值";
						pass = false;
						break;
					} else {
						//如果有定义，判断类型匹配
						String shortType = varStore.findVarType(item.getValue());
						String reqType = item.getVarType();
						boolean ret = checkType(shortType, reqType);

						if (ret == false) {
							//类型不匹配
							msg = "参数" + item.getVarName() + "与变量【" + item.getValue() + "】类型不匹配";
							pass = false;
							break;
						}
					}
				}
			}

			//检查返回设置
			/*if(pass){
				String retStr = call.getRetType();
				CallArgParser retParser = new CallArgParser(retStr);
				for(CallArgItem item : retParser.getItemSet()){
					if(StringUtils.isEmpty(item.getValue())){
						//如果有值没有填写
						msg = "参数值未设置值";
						pass = false;
						break;
					}
				}
			}*/

			//对引用文件的验证
			if(StringUtils.isNotEmpty(external)){
				
				if (external.startsWith("bixref:")) {
					String file = external.substring(7);
					IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot()
							.findMember(ProjectUtils.getResolvedProjectFilePath(file));
					if (workspaceResource == null) {
						msg = "找不到引用文件";
						pass = false;
					}
				}
			}else{
				//配置信息为空
				msg = "节点未配置";
				pass = false;
			}
			
			//对调用库的验证，刚打开的时候会出现找不到Class的情况，如果在当前工程中添加了业务逻辑构件类
			// FIXME 待修复
//			if(Constant.LOGIC_COMPONENT_EXECUTE.equals(call.getFuncName())){
//				String ext = call.getExternal();
//				System.err.println("LogicValidationProvider.Adapter8.validate()->ext:" + ext);
//				if(StringUtils.isNotBlank(ext)){
//					try {
//						Class callClass = FindClass.fromString(ext);
//					} catch (ClassNotFoundException e) {
//						msg = "找不到调用类";
//						pass = false;
//						System.err.println("LogicValidationProvider.Adapter8.validate()->工程找不到调用类:" + ext);
//					}
//				}
//			}

			if (!pass) {
				return ctx.createFailureStatus(new Object[] { call.getName(), msg });
			} else {
				return ctx.createSuccessStatus();
			}
		}

		/**
		 * 检查调用节点是要求的数据类型和填写的数据类型是否匹配
		 * @param shortType 变量集中的数据类型，如:String
		 * @param reqType 要求的数据类型，如：Object
		 * @return boolean 如果匹配或可用，返回true
		 */
		private boolean checkType(String shortType, String reqType) {
			boolean retVal = false;
			if (JavaTypeUtil.isPrimitiveType(shortType) && JavaTypeUtil.isPrimitiveType(reqType)) {
				//如果两者都是常规类型，则比较字串
				retVal = reqType.equalsIgnoreCase(shortType);
			}
			retVal = JavaTypeUtil.typeMatch(shortType, reqType);
			return retVal;
		}
	}

	/**
	 * @generated
	 */
	static String formatElement(EObject object) {
		return EMFCoreUtil.getQualifiedName(object, true);
	}

}
