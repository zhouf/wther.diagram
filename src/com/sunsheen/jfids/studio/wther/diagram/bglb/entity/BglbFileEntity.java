package com.sunsheen.jfids.studio.wther.diagram.bglb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BglbFileEntity implements Serializable {

	private static final long serialVersionUID = -8511009647712790303L;

	private List<InterfaceTree> interfaceTree;
	private List<GlobalVarEntity> globalVarList;
	private List<DataVarEntity> dataVarList;
	
	private boolean standComponent;
	private String dirName;
	private String compName;
	private String compDescription;
	private String compExample;
	private List<CompParamEntity> compParamList;
	private List<CompParamEntity> compRetList;
	
	public BglbFileEntity() {
		super();
		interfaceTree = new ArrayList<InterfaceTree>();
		globalVarList = new ArrayList<GlobalVarEntity>();
		dataVarList = new ArrayList<DataVarEntity>();
		
		compParamList = new ArrayList<CompParamEntity>();
		compRetList = new ArrayList<CompParamEntity>();
	}

	public List<InterfaceTree> getInterfaceTree() {
		return interfaceTree;
	}

	public void setInterfaceTree(List<InterfaceTree> interfaceTree) {
		this.interfaceTree = interfaceTree;
	}

	public List<GlobalVarEntity> getGlobalVarList() {
		return globalVarList;
	}

	public void setGlobalVarList(List<GlobalVarEntity> globalVarList) {
		this.globalVarList = globalVarList;
	}

	public List<DataVarEntity> getDataVarList() {
		return dataVarList;
	}

	public void setDataVarList(List<DataVarEntity> dataVarList) {
		this.dataVarList = dataVarList;
	}

	public boolean isStandComponent() {
		return standComponent;
	}

	public void setStandComponent(boolean standComponent) {
		this.standComponent = standComponent;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getCompDescription() {
		return compDescription;
	}

	public void setCompDescription(String compDescription) {
		this.compDescription = compDescription;
	}

	public String getCompExample() {
		return compExample;
	}

	public void setCompExample(String compExample) {
		this.compExample = compExample;
	}

	public List<CompParamEntity> getCompParamList() {
		return compParamList;
	}

	public void setCompParamList(List<CompParamEntity> compParamList) {
		this.compParamList = compParamList;
	}

	public List<CompParamEntity> getCompRetList() {
		return compRetList;
	}

	public void setCompRetList(List<CompParamEntity> compRetList) {
		this.compRetList = compRetList;
	}
}
