package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.sunsheen.jfids.studio.logic.util.LogicResourceImpl;
import com.sunsheen.jfids.studio.wther.diagram.compiler.item.Flow;

/**
 * 从pix文件里解析出flow对象
 * @author zhouf
 */
public class ParseFlow {
	
	/**
	 * 将文件转换为flow对象
	 * @param fileName 输入的pix文件系统路径
	 * @return flow对象
	 */
	public static Flow fromFile(String fileName) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ConvertFile cfile = new ConvertFile(inputStream);
		return cfile.getFlow();
	}
	
	/**
	 * 将bix文件转换为模型
	 * @param bixFile
	 * @return Flow
	 */
	public static Flow convertFromBix(IFile bixFile){
		Flow retFlow = null;
		org.eclipse.emf.ecore.resource.Resource res = new LogicResourceImpl(null);
		try {
			res.load(bixFile.getContents(), null);
			retFlow = (Flow)res.getContents().get(0);//第二个为diagram
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return retFlow;
	}

}
