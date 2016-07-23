package com.sunsheen.jfids.studio.wther.diagram.compiler;

import java.util.HashSet;
import java.util.Set;

import com.sunsheen.jfids.studio.logic.impl.FlowImpl;


/**
 * 这是一个编译对象，主要用于在编译时的对象及参数传递
 * @author zhouf
 *
 */
public class FlowCompileUnit {
	
	private FlowImpl flow;
	private String code;
	private String funcName;
	private Set<String> importSet;

	public FlowCompileUnit(FlowImpl flow) {
		this.flow = flow;
		importSet = new HashSet<String>();
	}

	public FlowImpl getFlow() {
		return flow;
	}

	public void setFlow(FlowImpl flow) {
		this.flow = flow;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public Set<String> getImportSet() {
		return importSet;
	}

	public void setImportSet(Set<String> importSet) {
		this.importSet = importSet;
	}
	
}
