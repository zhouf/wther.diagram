package com.sunsheen.jfids.studio.wther.diagram.bglb.entity;

import java.io.Serializable;

public class GlobalVarEntity implements Serializable {
	
	private static final long serialVersionUID = 5665163543406427057L;
	private String varName;
	private String varType;
	
	
	public GlobalVarEntity() {
		super();
		varName="";
		varType="";
	}
	public GlobalVarEntity(String varName, String varType) {
		super();
		this.varName = varName;
		this.varType = varType;
	}
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public String getVarType() {
		return varType;
	}
	public void setVarType(String varType) {
		this.varType = varType;
	}
}
