package com.sunsheen.jfids.studio.wther.diagram.compiler.item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.sunsheen.jfids.studio.logging.Log;


public class Flow {

	private String name;
	private String startId;
	private String args;
	private String define;
	private String ret;
	private String comment;
	private String createtime;
	private String author;
	private boolean canInvoked;
	private HashMap<String, Nodes> nodeMap = new HashMap<String, Nodes>();
	
	public Flow() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getDefine() {
		return define;
	}

	public void setDefine(String define) {
		this.define = define;
	}

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isCanInvoked() {
		return canInvoked;
	}

	public void setCanInvoked(boolean canInvoked) {
		this.canInvoked = canInvoked;
	}

	public void addNodes(Nodes node) {
		nodeMap.put(node.getId(), node);
		if("start".equalsIgnoreCase(node.getType())){
			startId = node.getId();
		}
	}
	
	public Nodes getStartNode(){
		if(startId == null || nodeMap.get(startId) == null){
			return null;
		}
		return nodeMap.get(startId);
	}
	
	public Nodes findNodes(String id){
		return (Nodes)nodeMap.get(id);
	}
	
	
	public Iterator<String> getNodesId(){
		return nodeMap.keySet().iterator();
	}
	
	public String findEndIfNodeId(Nodes ifNode){
		String retVal = "";
		String elseLink = ifNode.getElselink();
		HashMap<Object, IfLinks> linkMap = ifNode.linkMap;
		if(linkMap.size()>0){
			Iterator<Object> it = linkMap.keySet().iterator();
			if(it.hasNext()){
				IfLinks ifLink = (IfLinks) linkMap.get(it.next());
				retVal = ifLink.getTarget();
			}
		}else{
			Log.debug("没找到IF相应的下一系列节点");
		}
		//找到其中一条路径的IF下一节点
		
		//从其中一个节点遍历到最后一个节点
		String nextNodeId = retVal;
		ArrayList<String> alist = new ArrayList<String>();
		alist.add(nextNodeId);
		Nodes node = findNodes(nextNodeId);
		while(node!=null && !node.getType().equalsIgnoreCase("END")){
			if(node.getType().equalsIgnoreCase("if")){
				//nextNodeId = node.getElselink();
				nextNodeId = findEndIfNodeId(node);
			}else{
				//nextNodeId = node.getNextNode();
				nextNodeId = getNextNodeId(node);
			}
			alist.add(nextNodeId);
			node = findNodes(nextNodeId);
		}
		
		//判断else中的节点是否出现重合，重合节点即为ENDIF节点
		while(!alist.contains(elseLink)){
//			elseLink = findNodes(elseLink).getNextNode();
			elseLink = getNextNodeId(findNodes(elseLink));
		}
		retVal = elseLink;
		
		return retVal;
	}
	
	/**
	 * 查找下一个节点ID号，FOR节点和IF节点当成一个整体考虑
	 * @param node 当前节点
	 * @return 下一节点的ID号，如果没有或是异常，返回空串
	 */
	public String getNextNodeId(Nodes node){
		String retVal = "";
		if(node!=null){
			String nodeType = node.getType();
			nodeType = nodeType==null? "" : nodeType.toLowerCase();
			if("for".equalsIgnoreCase(nodeType)){
				//处理for循环的下一个节点，即为endfor后面的节点
				retVal = node.getEndlink();
			}else if("if".equalsIgnoreCase(nodeType)){
				//处理if节点的下一个节点，把if节点当成一个整体来处理
				retVal = findEndIfNodeId(node);
			}else{
				retVal = node.getNextNode();
			}
		}else{
			Log.error("Flow.getNextNodeId() node节点为空，请检查");
		}
//		Log.debug("Flow.getNextNodeId() 当前节点["+node.getId()+"]的下一个节点是["+retVal+"]");
		return retVal;
	}
}
