package com.sunsheen.jfids.studio.wther.diagram.compiler.item;

import java.util.HashMap;

public class Nodes {
	private String id;
	private String name;
	private String link;
	private String type;
	private String external;
	private String elselink;
	private String endlink;
	private String funcName;
	private String retType;
	private String args;
	private String breaklink;
	private String comment;
	private String disptip;
	private String tip;
	private String hide;
	private String exceptionlink;
	private String lineNum;

	protected HashMap<Object, IfLinks> linkMap = new HashMap<Object, IfLinks>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExternal() {
		return external;
	}
	public void setExternal(String external) {
		this.external = external;
	}
	public String getElselink() {
		return elselink;
	}
	public void setElselink(String elselink) {
		this.elselink = elselink;
	}
	public String getEndlink() {
		return endlink;
	}
	public void setEndlink(String endlink) {
		this.endlink = endlink;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getRetType() {
		return retType;
	}
	public void setRetType(String retType) {
		this.retType = retType;
	}
	public String getArgs() {
		return args;
	}
	public void setArgs(String args) {
		this.args = args;
	}
	public String getBreaklink() {
		return breaklink;
	}
	public void setBreaklink(String breaklink) {
		this.breaklink = breaklink;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDisptip() {
		return disptip;
	}
	public void setDisptip(String disptip) {
		this.disptip = disptip;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getHide() {
		return hide;
	}
	public void setHide(String hide) {
		this.hide = hide;
	}
	public String getExceptionlink() {
		return exceptionlink;
	}
	public void setExceptionlink(String exceptionlink) {
		this.exceptionlink = exceptionlink;
	}
	public String getLineNum() {
		return lineNum;
	}
	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	}
	public String getNextNode(){
		return getLink()==null? getBreaklink() : getLink();
	}
	
	public HashMap<Object, IfLinks> getLinkMap() {
		return linkMap;
	}
	public boolean hasMoreNextNode(){
		boolean retVal = false;
		if(this.link!=null && this.link.length()>0){
			return this.link.split(" ").length>1;
		}
		return retVal;
	}
	
	/**
	 * 向节点添加判断连接信息，以tip为key存放
	 * @param link
	 */
	public void addLinks(IfLinks link){
		linkMap.put(link.getPriority(), link);
	}
}
