package com.sunsheen.jfids.studio.wther.diagram.edit.tip;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.sunsheen.jfids.studio.logic.IfLink;
import com.sunsheen.jfids.studio.logic.impl.AssignmentImpl;
import com.sunsheen.jfids.studio.logic.impl.CustomImpl;
import com.sunsheen.jfids.studio.logic.impl.EntityImpl;
import com.sunsheen.jfids.studio.logic.impl.ForImpl;
import com.sunsheen.jfids.studio.logic.impl.IfImpl;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.Constant;
import com.sunsheen.jfids.studio.wther.diagram.parser.AssignmentParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.AssignmentItem;

/**
 * 生成各节点提示内容项
 * @author zhouf
 *
 */
public class NodeContentTipGenerator {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(NodeContentTipGenerator.class.getName());

	/**
	 * 生成不同节点的提示内容项，如赋值节点就显示赋值语句
	 * @param activity 传入的节点
	 * @return 返回提示内容字串
	 */
	public static String getTipContent(EntityImpl activity) {
		StringBuilder sb = new StringBuilder();
		if(activity instanceof AssignmentImpl && StringUtils.isNotEmpty(activity.getExternal())){
			//赋值节点
			sb.append(genAssignmentTip(activity.getExternal()));
			
		}else if(activity instanceof CustomImpl && StringUtils.isNotEmpty(activity.getExternal())){
			//自定义节点
			sb.append("\t");
			sb.append(activity.getExternal().replaceAll(";\r\n", ";\r\n\t"));
			sb.append("\n");
			
		}else if(activity instanceof ForImpl && StringUtils.isNotEmpty(activity.getExternal())){
			//循环节点
			sb.append(genLoopTip(activity.getExternal()));
			
		}else if(activity instanceof IfImpl){
			//判断节点，判断节点的提示在连线上，不在节点上
			sb.append(genIfTip(activity));
			
		}else{
			//其它节点
			if(StringUtils.isNotEmpty(activity.getExternal())){
				sb.append("\t").append(activity.getExternal()).append("\n");
			}else{
				sb.append("\t").append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 生成判断节点的提示项
	 * @param activity
	 * @return
	 */
	private static String genIfTip(EntityImpl activity) {
		StringBuilder sb = new StringBuilder();
		IfImpl ifNode = (IfImpl)activity;
		Map<Integer,String> map = new HashMap<Integer,String>();
		for(IfLink link : ifNode.getIflinks()){
			map.put(link.getPriority(), link.getTip() + ":  " + link.getCondition());
		}
		Integer[] sortedKey = map.keySet().toArray(new Integer[]{});
		Arrays.sort(sortedKey);
		for(Integer key : sortedKey){
			sb.append("\t").append(map.get(key)).append("\n");
		}
		return sb.toString();
	}

	/**
	 * 生成循环节点的内容提示项
	 * @param external
	 * @return
	 */
	private static String genLoopTip(String external) {
		//[-sp-]ee:200
		//for(int i=100; i<1000; i++){}
		StringBuilder sb = new StringBuilder();
		if(external.startsWith(Constant.LOOP_SIMPLE_MARK)){
			//迭代循环
			String vars[] = external.substring(external.indexOf("]")+1).split(":");
			if(vars.length==2){
				sb.append("for(int ").append(vars[0]).append("=0; ");
				sb.append(vars[0]).append("<").append(vars[1]).append("; ");
				sb.append(vars[0]).append("++)");
			}else{
				log.warn("NodeContentTipGenerator.genLoopTip()->:循环设置无法解析:" + external);
			}
		}else{
			//普通循环
			if(external.startsWith("for")||external.startsWith("while")){
				sb.append(external.substring(0,external.indexOf(")")+1));
			}else if(external.startsWith("do")){
				//do-while
				sb.append("do-").append(external.substring(external.indexOf("}")+1));
			}
		}
		sb.insert(0, "\t").append("\n");
		
		return sb.toString();
	}

	/**
	 * 生成赋值节点的提示
	 * @param external
	 * @return
	 */
	private static String genAssignmentTip(String external) {
		//a:i/100:表达式:false:false|b:i/10%10:表达式:false:false|c:i%10:表达式:false:false
		StringBuilder sb = new StringBuilder();
		AssignmentParser assignmentParser = new AssignmentParser(external);
		for(AssignmentItem item : assignmentParser.getItemSet()){
			sb.append("\t");
			sb.append(item.getVarName()).append(" = ");
			sb.append(item.getInitVal()).append(";");
			sb.append("\n");
		}
		return sb.toString();
	}

	
}
