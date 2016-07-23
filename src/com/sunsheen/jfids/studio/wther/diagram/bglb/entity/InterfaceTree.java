package com.sunsheen.jfids.studio.wther.diagram.bglb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 这是为接口显示添加的树结构
 * @author zhouf
 */
public class InterfaceTree implements Serializable {
	
	private static final long serialVersionUID = 9221166037199651671L;
	private String labelStr;
	private String argStr;
	private String retStr;
	private boolean checked;
	private List<InterfaceTree> children;
	private InterfaceTree parent = null;
	
	public InterfaceTree() {
		labelStr = "";
		argStr = "";
		retStr = "";
		children = new ArrayList<InterfaceTree>();
	}

	public String getLabelStr() {
		return labelStr;
	}

	public void setLabelStr(String labelStr) {
		this.labelStr = labelStr;
	}

	public String getArgStr() {
		return argStr;
	}

	public void setArgStr(String argStr) {
		this.argStr = argStr;
	}

	public String getRetStr() {
		return retStr;
	}

	public void setRetStr(String retStr) {
		this.retStr = retStr;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<InterfaceTree> getChildren() {
		return children;
	}

	public void setChildren(List<InterfaceTree> children) {
		this.children = children;
	}

	public InterfaceTree getParent() {
		return parent;
	}

	public void setParent(InterfaceTree parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return labelStr;
	}
}
