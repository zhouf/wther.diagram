package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;

import com.sunsheen.jfids.studio.logging.LogFactory;
import com.sunsheen.jfids.studio.logic.Bixref;
import com.sunsheen.jfids.studio.logic.Node;
import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.logic.util.LogicResourceImpl;

import com.sunsheen.jfids.studio.run.core.resources.cache.FileAssociationsUtils;

/**
 * 处理文件关联的辅助类
 * @author zhouf
 */
public class AssociationUtil {

	static final Logger log = LogFactory.getLogger(AssociationUtil.class.getName());

	public static void scanBixAssociation(IFile file) {
		log.debug("AssociationUtil.scanBixAssociation()->file:" + file);
		Set<String> files = new HashSet<String>();
		if(!file.exists()){
			return;
		}
		org.eclipse.emf.ecore.resource.Resource res = new LogicResourceImpl(null);
		try {
			res.load(file.getContents(), null);
		} catch (IOException e) {
			System.out.println("AssociationUtil.scanBixAssociation() :IOException");
			//源文件错误
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		EObject obj0 = res.getContents().get(0);
		if(obj0 instanceof FlowImpl){
			FlowImpl flow = (FlowImpl) obj0;// 第二个为diagram
			//log.info("AssociationUtil scanBixAssociation()->IFile转换为FLow模型为:" + flow);
			
			for(Node node : flow.getNodes()){
				if(node instanceof Bixref){
					String ext = ((Bixref) node).getExternal();
					if(ext!=null && ext.startsWith("bixref:")){
						String refFilePath = ext.substring(7);
						files.add(refFilePath);
						log.debug("AssociationUtil.scanBixAssociation()->refFilePath:" + refFilePath);
					}
				}
			}
			FileAssociationsUtils.getInstance().addFileSource(file.getFullPath(), files);
		}else{
			//obj0 instance of DiagramImpl
		}
	}
}
