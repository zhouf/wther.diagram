package com.sunsheen.jfids.studio.wther.diagram.edit.util;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.sunsheen.jfids.studio.wther.diagram.part.ValidateAction;

/**
 * 
 * @author Administrator
 * 
 */
public class BixCheckError {
	public static boolean checkError(IFile file) {
		ValidateAction.runValidation(file);
		boolean hasError = false;
		try {
			hasError = file.findMaxProblemSeverity(IMarker.PROBLEM, true,
					IResource.DEPTH_ONE) == IMarker.SEVERITY_ERROR;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return hasError;
	}
}
