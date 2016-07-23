package com.sunsheen.jfids.studio.wther.diagram.bglb.entity;

import java.io.Serializable;

public class CompParamEntity implements Serializable{

	private static final long serialVersionUID = 3243849043208279084L;
	private String ptype = "";
	private String pname = "";
	private String description = "";
	
	
	public CompParamEntity() {
		this("","","");
	}
	public CompParamEntity(String ptype, String pname) {
		this(ptype,pname,"");
	}
	public CompParamEntity(String ptype, String pname, String description) {
		super();
		this.ptype = ptype;
		this.pname = pname;
		this.description = description;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
