package com.sunsheen.jfids.studio.wther.diagram.compiler;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.sunsheen.jfids.studio.run.Constants;
import com.sunsheen.jfids.studio.run.core.project.builder.IResourceCompiler;
import com.sunsheen.jfids.studio.run.core.project.builder.IResourceCompilerFactory;
import com.sunsheen.jfids.studio.run.core.project.builder.ResourceCompiler;
import com.sunsheen.jfids.studio.run.utils.ResourcePathConvert;

public class BixCompilerFactory implements IResourceCompilerFactory {

	@Override
	public IResourceCompiler createResourceCompiler(IResource sourceResource, int compileType) {
		return new ResourceCompiler(sourceResource, compileType) {
			
			@Override
			public void build() throws CoreException {
				IFolder targetFolder = res.getProject().getFolder(Constants.PROJECT_PATH_WEBROOT_WEBINF_COMPONENT);
				com.sunsheen.jfids.studio.wther.diagram.compiler.BixCompilerUtil.complierFolder((IFolder) res.getParent(), targetFolder);
				targetResource.refreshLocal(0, null);
			}

			@Override
			public void delete() {
				IFolder targetFolder = res.getProject().getFolder(Constants.PROJECT_PATH_WEBROOT_WEBINF_COMPONENT);
				// 当删除bix的时候 ，删除对应class 的pix对应的方法
				BixCompilerUtil.drop((IFile) res, targetFolder);
			}

			@Override
			public IResource toTargetResource() {
				return getTargetFile(toResourceTargetPath(res, true));
			}
		};
	}

	@Override
	public String toResourceTargetPath(IResource res, boolean toWebRoot) {
		return ResourcePathConvert.toBixTargetRelativePath(res, toWebRoot);
	}

}
