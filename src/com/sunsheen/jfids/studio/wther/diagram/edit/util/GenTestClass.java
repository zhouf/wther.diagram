package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import com.sunsheen.jfids.studio.javaassist.ClassPool;
import com.sunsheen.jfids.studio.javaassist.CtClass;
import com.sunsheen.jfids.studio.javaassist.CtMethod;
import com.sunsheen.jfids.studio.javaassist.CtNewMethod;

public class GenTestClass {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(GenTestClass.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	public static void genClass(String mainStr, String targetFolder) {
		try {
			//生成测试类的包和类名
			String pkgStr = "test";
			String className = "TestCode";

			ClassPool pool = ClassPool.getDefault();
			pool.appendClassPath(targetFolder);
			//pool.appendClassPath(targetFolder.replace("component", "classes"));
			log.debug("GenTestClass.genClass()->appendClassPath:" + targetFolder);
			log.debug("GenTestClass.genClass()->appendClassPath:" + targetFolder.replace("component", "classes"));
			log.debug("GenTestClass.genClass()-> 创建类:" + targetFolder + "/" + pkgStr + "." + className);
			log.debug("GenTestClass.genClass()-> mainStr:"+ mainStr);

			CtClass cc = pool.makeClass(pkgStr + "." + className);
			log.debug("GenTestClass.genClass()->pool.makeClass:" + pkgStr + "." + className);
			
			cc.getClassFile().setVersionToJava5();
			
			CtMethod method = CtNewMethod.make(mainStr, cc);
			cc.addMethod(method);

			log.debug("GenTestClass.genClass()-> targetFolder:" + targetFolder);
			cc.writeFile(targetFolder);
			cc.detach();
			
		} catch (Exception e) {
			log.error("GenTestClass.genClass()->e.toString:" + e);
			//e.printStackTrace();
		}
	}

}
