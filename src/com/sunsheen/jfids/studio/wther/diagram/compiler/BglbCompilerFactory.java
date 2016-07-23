package com.sunsheen.jfids.studio.wther.diagram.compiler;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;

import com.sunsheen.jfids.studio.run.Constants;
import com.sunsheen.jfids.studio.run.core.project.builder.IResourceCompiler;
import com.sunsheen.jfids.studio.run.core.project.builder.IResourceCompilerFactory;
import com.sunsheen.jfids.studio.run.core.project.builder.ResourceCompiler;
import com.sunsheen.jfids.studio.run.utils.ResourcePathConvert;

public class BglbCompilerFactory implements IResourceCompilerFactory {

	@Override
	public IResourceCompiler createResourceCompiler(IResource sourceResource, int compileType) {
		return new ResourceCompiler(sourceResource, compileType) {

			@Override
			public void build() {
				IFolder targetFolder = res.getProject().getFolder(Constants.PROJECT_PATH_WEBROOT_WEBINF_COMPONENT);
				com.sunsheen.jfids.studio.wther.diagram.compiler.BglbCompilerUtil.complier((IFile) res, targetFolder);
			}

			@Override
			public IResource toTargetResource() {
				return getTargetFile(toResourceTargetPath(res.getParent(), true));
			}
		};
	}

	@Override
	public String toResourceTargetPath(IResource res, boolean toWebRoot) {
		return ResourcePathConvert.toTargetRelativePath(res.getParent(), toWebRoot);
	}

}
