package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class FileNameValidator {

	//创建一个错误标记
	public static IMarker createMarker(IResource res, String message)
			throws CoreException {
		IMarker marker = res.createMarker(IMarker.PROBLEM);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		marker.setAttribute(IMarker.MESSAGE, message);
		return marker;
	}
	
	//验证文件名是否合法，不能含有空格
	public static boolean checkNameOK(String fileName){
		//return fileName.contains(" ");
		
		//去掉最后的文件名后缀
		int lastDotIndex = fileName.lastIndexOf(".");
		if(lastDotIndex>0){
			fileName = fileName.substring(0,lastDotIndex);
		}
		Pattern pattern = Pattern.compile("^[_a-zA-Z][_a-zA-Z0-9]*$");
		Matcher matcher = pattern.matcher(fileName);
		return matcher.matches();
	}
	
	public static boolean validateFileName(IFile file) {
		boolean retVal = true;
		String TIPSTR = "文件名不合法";
		try {
			if(!checkNameOK(file.getName())){
				//文件名不合法，避免重复添加marker，先判断一下
				boolean alreadyMark = false;
				IMarker markers[] = file.findMarkers(IMarker.PROBLEM, true, 1);
				for(IMarker m : markers){
					if(m.getAttribute(IMarker.MESSAGE, "").equalsIgnoreCase(TIPSTR)){
						alreadyMark = true;
						break;
					}
				}
				if(!alreadyMark){
					createMarker(file, TIPSTR);
				}
				retVal = false;
			}else{
				file.deleteMarkers(IMarker.PROBLEM, true, 1);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return retVal;
	}
}
