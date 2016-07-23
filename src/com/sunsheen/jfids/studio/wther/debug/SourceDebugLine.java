package com.sunsheen.jfids.studio.wther.debug;

/**
 * 
 * @author litao
 * 
 */
public class SourceDebugLine {
	private int inputStartLine;
	private int repeatCount;
	private int outputStartLine;
	private int outputLineIncrement;

	public SourceDebugLine(int inputStartLine) {
		this.inputStartLine = inputStartLine;
		this.repeatCount = 1;
		this.outputStartLine = inputStartLine;
		this.outputLineIncrement = 1;
	}

	public int getInputStartLine() {
		return inputStartLine;
	}

	public void setInputStartLine(int inputStartLine) {
		this.inputStartLine = inputStartLine;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	public int getOutputStartLine() {
		return outputStartLine;
	}

	public void setOutputStartLine(int outputStartLine) {
		this.outputStartLine = outputStartLine;
	}

	public int getOutputLineIncrement() {
		return outputLineIncrement;
	}

	public void setOutputLineIncrement(int outputLineIncrement) {
		this.outputLineIncrement = outputLineIncrement;
	}

}
