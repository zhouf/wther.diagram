package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import com.sunsheen.jfids.studio.run.utils.io.FileUtil;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.BglbFileEntity;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.GlobalVarEntity;

/**
 * 处理与全局变量相关的公共操作
 * @author zhouf
 */
public class GlobalBixUtil {
	
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(GlobalBixUtil.class.getName());

	
	/**
	 * 获取当前bix所在目录下的全局变量设置字串
	 * @param file 当前bix文件
	 * @return 如果文件存在则返回文件内容，不存在则返回空串
	 */
	public static String getGlobalFileString(IFile file) {
		String retVal = "";
		IFile globalFile = getGlobalFile(file);
		if(globalFile!=null){
			try {
				retVal = IOUtils.toString(globalFile.getContents());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return retVal;
	}
	
	/**
	 * 获取当前bix所在目录下的全局文件
	 * @param file 当前bix文件
	 * @return IFile 如果存在全局.bglb文件，则返回文件，没有则返回null
	 */
	public static IFile getGlobalFile(IFile file){
		IFile retFile = null;
		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		String memberPath = "/"+file.getProject().getName()+"/"+file.getProjectRelativePath().removeLastSegments(1)+"/"+ Constant.GLOBALBIXFILE;
		IResource res = myWorkspaceRoot.findMember(memberPath);
		if(res!=null && res instanceof IFile){
			retFile = (IFile)res;
		}
		return retFile;
	}
	
	/**
	 * 获取当前编辑器中的正在处理的文件
	 * @return IFile
	 */
	public static IFile getCurrentIFile(){
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editorPart != null) {
			IFileEditorInput input = (IFileEditorInput) editorPart.getEditorInput();
			return input.getFile();
		}
		return null;
	}
	

	/**
	 * 将全局文件转换为对象
	 * @param globalFile 传入的全局文件对象
	 * @return BglbFileEntity 返回全局对象，不正确返回空
	 */
	public static BglbFileEntity getGlobalEntityFromIFile(IFile globalFile) {
		BglbFileEntity fileEntity = null;
		if(globalFile!=null){
			
			try {
				InputStream contents = globalFile.getContents();
				if(contents.available()>0){
					ObjectInputStream oins = new ObjectInputStream(contents);
					fileEntity = (BglbFileEntity)oins.readObject();
				}
				
			} catch (IOException e) {
				log.error("GlobalBixUtil.getGlobalEntityFromIFile()->e.toString():" + e.toString());
				if(e.getMessage().contains("invalid stream header")){
					FileUtil.writeStringToFile(globalFile, "", null);
					log.debug("GlobalBixUtil.getGlobalEntityFromIFile()->:旧版本.bglb文件里内容被重置");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			} 

		}else{
			log.debug("GlobalBixUtil getGlobalEntity()-> :globalFile is null");
		}
		return fileEntity;
	}
	
	/**
	 * 获取当前全局文件中的变量对象列表
	 * @return List<GlobalVarEntity>
	 */
	public static List<GlobalVarEntity> getGlobalVarEntityList(){
		List<GlobalVarEntity> retList = new ArrayList<GlobalVarEntity>();
		IFile file = getCurrentIFile();
		if(file!=null){
			
			IFile globalFile = getGlobalFile(file);
			if(globalFile!=null){
				
				BglbFileEntity fileEntity = getGlobalEntityFromIFile(globalFile);
				if(fileEntity!=null){
					retList = fileEntity.getGlobalVarList();
				}

			}else{
				log.debug("GlobalBixUtil getGlobalVarEntityList()-> :globalFile is null");
			}
		}
		return retList;
	}
	
	/**
	 * 只获得返回全局变量中的变量名集合
	 * @return Set<String>
	 */
	public static Set<String> getGlobalVarNameSet(){
		Set<String> retSet = new HashSet<String>();
		List<GlobalVarEntity> list = getGlobalVarEntityList();
		for(GlobalVarEntity var : list){
			retSet.add(var.getVarName());
		}
		return retSet;
	}
	
	/**
	 * 通过当前的bix文件，获取相关的全局变量文件
	 * @param currentBixFile 当前的bix文件
	 * @return IFile 返回的全局变量文件
	 */
	@Deprecated
	private static IFile findGlobalIFile(IFile currentBixFile){
		IFile retFile = null;
		IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(currentBixFile.getFullPath().removeLastSegments(1));
		
		IResource globalRes = folder.findMember(Constant.GLOBALBIXFILE);
		if(globalRes!=null && globalRes instanceof IFile){
			retFile = (IFile) globalRes;
		}else{
			//没有找到
			retFile = folder.getFile(Constant.GLOBALBIXFILE);
		}
		return retFile;
	}
}
