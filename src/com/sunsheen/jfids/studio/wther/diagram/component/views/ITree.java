package com.sunsheen.jfids.studio.wther.diagram.component.views;

import java.util.List;

public interface ITree {
	public String getName();
	public void setName(String name);
	@SuppressWarnings("rawtypes")
	public void setChildren(List children);
	@SuppressWarnings("rawtypes")
	public List getChildren();
}
