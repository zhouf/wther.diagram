package com.sunsheen.jfids.studio.wther.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.debug.core.IJavaLineBreakpoint;
import org.eclipse.jdt.debug.core.IJavaStratumLineBreakpoint;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.jdt.internal.debug.core.JDIDebugPlugin;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.run.Constants;
import com.sunsheen.jfids.studio.run.utils.ProjectPathUtils;

/**
 * 
 * @author litao
 * 
 */
@SuppressWarnings("restriction")
public class BreakPointUtils {

	public static final String JDT_STRATUM_BREAKPOINT_MARKER = "org.eclipse.jdt.debug.javaStratumLineBreakpointMarker";

	public static void toggleBreakpoint(IEditorPart editorPart, int lineNumber, boolean createNew, Map<String, Object> attributes) {
		createStratumBreakpoint(editorPart, createNew, lineNumber, attributes);
	}

	public static void createStratumBreakpoint(IEditorPart editorPart, boolean createNew, int lineNumber, Map<String, Object> attributes) {
		IEditorInput input = editorPart.getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			createStratumBreakpoint(file, createNew, lineNumber, attributes);
		}
	}

	public static void createStratumBreakpoint(IFile file, boolean createNew, int lineNumber, Map<String, Object> attributes) {
		try {
			String suffix = file.getFileExtension();
			if (!Constants.FILE_EXT_BIX.equals(suffix))
				return;

			IPath typePath = ProjectPathUtils.getModuleSubTargetPath(file);
			String typeName = typePath.removeLastSegments(1).removeFileExtension().toString().replace('/', '.');
			IJavaLineBreakpoint existingBreakpoint = lineBreakpointExists(file, lineNumber);
			if (existingBreakpoint != null) {
				boolean tmp = false;
				Map<String, Object> map = existingBreakpoint.getMarker().getAttributes();
				if (map != null) {
					Object o = map.get("temp_mark");
					if ((o != null) && (((Boolean) o).booleanValue())) {
						tmp = true;
					}
				}

				DebugPlugin.getDefault().getBreakpointManager().removeBreakpoint(existingBreakpoint, true);
				if (!tmp) {
					return;
				}
			}
			if (createNew)
				JDIDebugModel.createStratumBreakpoint(file, suffix, file.getName(), typePath.toString(), typeName, lineNumber, -1, -1, -1, true, attributes);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
	}

	public static IJavaLineBreakpoint lineBreakpointExists(IResource resource, int lineNumber) throws CoreException {
		String modelId = JDIDebugPlugin.getUniqueIdentifier();
		IBreakpointManager manager = DebugPlugin.getDefault().getBreakpointManager();
		IBreakpoint[] breakpoints = manager.getBreakpoints(modelId);
		for (int i = 0; i < breakpoints.length; ++i) {
			if (!(breakpoints[i] instanceof IJavaStratumLineBreakpoint)) {
				continue;
			}
			IJavaStratumLineBreakpoint breakpoint = (IJavaStratumLineBreakpoint) breakpoints[i];
			IMarker marker = breakpoint.getMarker();
			if ((marker != null) && (marker.exists()) && (marker.getType().equals(JDT_STRATUM_BREAKPOINT_MARKER)) && (breakpoint.getLineNumber() == lineNumber)
					&& (resource.equals(marker.getResource()))) {
				return breakpoint;
			}
		}

		return null;
	}

	public static List<IJavaLineBreakpoint> listLineBreakpoints(IResource resource) throws CoreException {
		String modelId = JDIDebugPlugin.getUniqueIdentifier();
		IBreakpointManager manager = DebugPlugin.getDefault().getBreakpointManager();
		IBreakpoint[] breakpoints = manager.getBreakpoints(modelId);
		List<IJavaLineBreakpoint> breakpointList = new ArrayList<IJavaLineBreakpoint>();
		for (int i = 0; i < breakpoints.length; ++i) {
			if (!(breakpoints[i] instanceof IJavaStratumLineBreakpoint)) {
				continue;
			}
			IJavaStratumLineBreakpoint breakpoint = (IJavaStratumLineBreakpoint) breakpoints[i];
			IMarker marker = breakpoint.getMarker();
			if ((marker != null) && (marker.exists()) && (marker.getType().equals(JDT_STRATUM_BREAKPOINT_MARKER)) && (resource.equals(marker.getResource()))) {
				breakpointList.add(breakpoint);
			}
		}
		return breakpointList;
	}

	public static List<IJavaLineBreakpoint> filterResourceLineBreakpoints(List<IBreakpoint> breakpoints, IResource resource) {
		List<IJavaLineBreakpoint> breakpointList = new ArrayList<IJavaLineBreakpoint>();
		for (Object breakpoint : breakpoints) {
			if (!(breakpoint instanceof IJavaStratumLineBreakpoint)) {
				continue;
			}
			IJavaStratumLineBreakpoint jslbp = (IJavaStratumLineBreakpoint) breakpoint;
			IMarker marker = jslbp.getMarker();
			try {
				if ((marker != null) && (marker.exists()) && (marker.getType().equals(JDT_STRATUM_BREAKPOINT_MARKER)) && (resource.equals(marker.getResource()))) {
					breakpointList.add(jslbp);
				}
			} catch (CoreException e) {
				Log.error(e.getMessage(), e);
			}
		}
		return breakpointList;
	}
}
