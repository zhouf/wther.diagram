package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ClasspathUtils;

/**
 * 通过这个类获取工程相关的信息，如工程名，当前文件路径信息等
 * @author zhouf
 */
public class ProjectInfoUtil{


	private String projectPath, filePath, eclipsePath, fileName;

	IProject activeProject = null;
	IFile currentFile = null;

	public ProjectInfoUtil() {

		try{
			IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			init(editorPart);
		}catch(NullPointerException e){
			
			PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
				public void run() {
					IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
					init(editorPart);
				}
			});
		}

	}

	/**
	 * 初始化方法
	 * @param editorPart
	 * @return void
	 */
	private void init(IEditorPart editorPart) {
		if (editorPart != null) {
			IFileEditorInput input = (IFileEditorInput) editorPart.getEditorInput();
			IFile file = input.getFile();
			fileName = file.getName();
			currentFile = file;
			activeProject = file.getProject();
			projectPath = activeProject.getFolder("resources").getLocation().toString();
			filePath = file.getLocation().toString();
			int projectNameLen = activeProject.getName().length();
			eclipsePath = activeProject.getLocation().toString();
			eclipsePath = eclipsePath.substring(0, eclipsePath.length() - projectNameLen);
		}
	}

	public IProject getActiveProject() {
		return activeProject;
	}

	public IFile getCurrentIFile() {
		return currentFile;
	}

	public IFolder getParentIFolder() {
		if (currentFile != null) {
			return (IFolder) currentFile.getParent();
		}
		return null;
	}

	public String getFileName() {
		return fileName;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public String getEclipsePath() {
		return eclipsePath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/*
	 * 获得当前活动文件所在的功能模块路径
	 */
	public IFolder getCurrentFuncModelFolder() {
		IFile curFile = this.getCurrentIFile();
		IFolder folder = null;
		
		if(curFile!=null){
			IProject prj = this.getActiveProject();
			List<String> srcList = ClasspathUtils.listSrcPath(curFile.getProject());
			String fileFullPath = curFile.getFullPath().toOSString();
			for (String srcPath : srcList) {
				srcPath=StringUtils.removeEnd(srcPath, "src");
				if(fileFullPath.startsWith(srcPath)){
					// 找到，srcPath为包含工程名的功能模块路径，如：\jfids_demo\resources\crud\custsingletable\
					srcPath = srcPath.substring(srcPath.indexOf("\\", 1));
					// 去掉工程名后获取IFolder对象
					folder = (IFolder) prj.findMember(srcPath);
					break;
				}
			}
		}
		return folder;
	}
	
}
