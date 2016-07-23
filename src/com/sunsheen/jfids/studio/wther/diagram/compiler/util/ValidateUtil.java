package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.lang.reflect.Method;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.sunsheen.jfids.studio.run.utils.ProjectPathUtils;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FileNameValidator;

/**
 * 文件验证工具类
 * 
 * @author lz
 * 
 */
public class ValidateUtil {

	public static boolean validateInterfacesImpl(IFile sourceFile, Set<Method> unimplementMethodSet) {
		try {
			sourceFile.deleteMarkers(IMarker.PROBLEM, true, 1);
			IFolder folder = ProjectPathUtils.getFolder(sourceFile.getFullPath().removeLastSegments(1));
			IResource[] files;
			try {
				if (folder != null && folder.exists()) {
					files = folder.members();
					for (Method method : unimplementMethodSet) {
						boolean succ = false;
						for (IResource res : files) {
							IFile file = (IFile) res;
							method.getDeclaringClass().getName();
							if (method.getName().equals(file.getName())) {
								succ = true;
							}
						}
						if (!succ) {
							FileNameValidator.createMarker(sourceFile, "接口" + method.getDeclaringClass().getName()
									+ " 方法" + method.getName() + "未实现,请重新选择接口,生成实现");
							return succ;
						}
					}
					// if ("bix".equalsIgnoreCase(file.getFileExtension())) {
					// FlowImpl flow = BixCompilerUtil.parserFlowModel(file);
					// System.out.println(flow);
					// }

				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
		return true;
	}

}
