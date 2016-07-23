package com.sunsheen.jfids.studio.wther.diagram.bglb.entity;

import java.io.Serializable;

public class DataVarEntity implements Serializable {
	
	private static final long serialVersionUID = 9217383500040818232L;
	private String varName;
	private String interfaceType;
	
	public DataVarEntity() {
		super();
		varName = "";
	}

	public DataVarEntity(String varName, String interfaceType) {
		super();
		this.varName = varName;
		this.interfaceType = interfaceType;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
	
}
