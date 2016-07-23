package com.sunsheen.jfids.studio.wther.debug;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author litao
 * 
 */
public class SourceDebugFile {
	private String filename;
	private String filepath;
	private String lineFileID;
	private List<SourceDebugLine> debugLines = new ArrayList<SourceDebugLine>();

	public SourceDebugFile(String filename, String filepath) {
		this.filename = filename;
		this.filepath = filepath;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getLineFileID() {
		return lineFileID;
	}

	public void setLineFileID(String lineFileID) {
		this.lineFileID = lineFileID;
	}

	public List<SourceDebugLine> getDebugLines() {
		return debugLines;
	}

	public void setDebugLines(List<SourceDebugLine> debugLines) {
		this.debugLines = debugLines;
	}

}
