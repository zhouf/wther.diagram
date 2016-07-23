package com.sunsheen.jfids.studio.wther.diagram.component.views;
/**
 * 这是一个描述构件参数的类
 * @author zhouf
 *
 */
public class Parameter {

	private String pname;
	private String ptype;
	private String defaultVal;
	private String comment;
	private String tipconf;
	private String editable;
	private String funcParam;
	private String ignore;
	
	
	public Parameter() {
		super();
	}
	public Parameter(String pname, String ptype, String defaultVal, String comment,String tipconf) {
		this(pname,ptype,defaultVal,comment,tipconf,"","","");
	}
	public Parameter(String pname, String ptype, String defaultVal, String comment,String tipconf,String editable,String funcParam,String ignore) {
		super();
		this.pname = pname;
		this.ptype = ptype;
		this.defaultVal = defaultVal;
		this.comment = comment;
		this.tipconf = tipconf;
		this.editable = editable;
		this.funcParam = funcParam;
		this.ignore = ignore;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	public String getDefaultVal() {
		return defaultVal;
	}
	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getTipconf() {
		return tipconf;
	}
	public void setTipconf(String tipconf) {
		this.tipconf = tipconf;
	}
	public String getEditable() {
		return editable;
	}
	public void setEditable(String editable) {
		this.editable = editable;
	}
	public String getFuncParam() {
		return funcParam;
	}
	public void setFuncParam(String funcParam) {
		this.funcParam = funcParam;
	}
	public String getIgnore() {
		return ignore;
	}
	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}
	
}
