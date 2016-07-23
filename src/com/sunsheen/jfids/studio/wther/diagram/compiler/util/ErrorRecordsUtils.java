package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import com.sunsheen.jfids.studio.run.core.cache.AResourceCacheUtils;
import com.sunsheen.jfids.studio.run.core.extensionpoint.ResourceCacheManager;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;

public class ErrorRecordsUtils extends AResourceCacheUtils<ErrorRecorders> {

	public static ErrorRecordsUtils getInstance() {
		return (ErrorRecordsUtils) ResourceCacheManager.getResourceCache("com.sunsheen.jfids.studio.wther.diagram.errorrecords");
	}

	private ErrorRecorders records;

	@Override
	public void clear(IProject project) {
	}

	@Override
	public String[] getEffectedResources(IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ErrorRecorders getCaches() {
		return records == null ? records = new ErrorRecorders() : records;
	}

	@Override
	protected void setCaches(ErrorRecorders cache) {
		this.records = cache;
	}

	@Override
	protected File getSaveFile() {
		return LogicDiagramEditorPlugin.getMetadataFile("ErrorRecords.cache");
	}

	@Override
	protected ErrorRecorders readCache(ObjectInputStream is) throws ClassNotFoundException, IOException {
		return (ErrorRecorders) is.readObject();
	}

	public void appendInfo(String fileUri, Integer lineNum, String msg) {
		getCaches().appendInfo(fileUri, lineNum, msg);
	}

	public Map<Integer, String> getMapByFile(String fileUri) {
		return getCaches().getMapByFile(fileUri);
	}

	public void removeMapByFile(String fileUri) {
		getCaches().removeMapByFile(fileUri);
	}

}
