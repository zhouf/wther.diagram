package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;

import com.sunsheen.jfids.studio.javaassist.CannotCompileException;
import com.sunsheen.jfids.studio.javaassist.ClassPool;
import com.sunsheen.jfids.studio.javaassist.CtClass;
import com.sunsheen.jfids.studio.javaassist.CtMethod;
import com.sunsheen.jfids.studio.javaassist.CtNewMethod;
import com.sunsheen.jfids.studio.javaassist.NotFoundException;
import com.sunsheen.jfids.studio.javaassist.bytecode.AnnotationsAttribute;
import com.sunsheen.jfids.studio.javaassist.bytecode.AttributeInfo;
import com.sunsheen.jfids.studio.javaassist.bytecode.ConstPool;
import com.sunsheen.jfids.studio.javaassist.bytecode.MethodInfo;
import com.sunsheen.jfids.studio.javaassist.bytecode.annotation.Annotation;
import com.sunsheen.jfids.studio.javaassist.bytecode.annotation.AnnotationMemberValue;
import com.sunsheen.jfids.studio.javaassist.bytecode.annotation.ArrayMemberValue;
import com.sunsheen.jfids.studio.javaassist.bytecode.annotation.EnumMemberValue;
import com.sunsheen.jfids.studio.javaassist.bytecode.annotation.StringMemberValue;
import com.sunsheen.jfids.studio.javaassist.util.GenClassHashMap;
import com.sunsheen.jfids.studio.javaassist.util.RowNumber;
import com.sunsheen.jfids.studio.logging.LogFactory;
import com.sunsheen.jfids.studio.logic.Node;
import com.sunsheen.jfids.studio.logic.impl.EndImpl;
import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.logic.impl.StartImpl;
import com.sunsheen.jfids.studio.run.Constants;
import com.sunsheen.jfids.studio.run.utils.ProjectPathUtils;
import com.sunsheen.jfids.studio.run.utils.ProjectUtils;
import com.sunsheen.jfids.studio.wther.debug.SourceDebugExtensionUtils;
import com.sunsheen.jfids.studio.wther.debug.SourceDebugFile;
import com.sunsheen.jfids.studio.wther.debug.SourceDebugLine;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.BglbFileEntity;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.CompParamEntity;
import com.sunsheen.jfids.studio.wther.diagram.compiler.BixCompilerUtil;
import com.sunsheen.jfids.studio.wther.diagram.compiler.FlowCompileUnit;

public class JavaCompiler {
	static Logger log = LogFactory.getLogger(JavaCompiler.class.getName());
	private String pkgName = "";
	private String className = "";
	private IProject curProject = null;
	private CtClass cc = null;
	private ClassPool pool = null;
	private Map<String, SourceDebugFile> sourceDebugFileMap;

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setCurProject(IProject curProject) {
		this.curProject = curProject;
	}

	public String getFullClassName() {
		return pkgName + "." + className;
	}

	/**
	 * 生成class框架
	 * 
	 * @return void
	 */
	public void prepareClass() {
		sourceDebugFileMap = new HashMap<String, SourceDebugFile>();
		pool = ClassPool.getDefault();
		CtClass superClass = null;
		log.debug("JavaCompiler.prepareClass()->:");

		String fullClassName = getFullClassName();

		// 添加编译路径
		try {
			List<String> poolPaths = findPoolPath(curProject);
			for (String addPath : poolPaths) {
				pool.appendClassPath(addPath);
			}
		} catch (NotFoundException e) {
			log.error("添加classpath异常：" + e.toString());
		}

		// 获得类
		try {
			superClass = pool.getCtClass("com.sunsheen.jfids.system.base.service.bix.ABixService");
			// 添加常用类
			pool.importPackage("java.util.Date");
			pool.importPackage("java.util.List");
			pool.importPackage("java.util.Map");
			pool.importPackage("java.util.Set");
			pool.importPackage("java.util.ArrayList");
			pool.importPackage("java.util.Arrays");
			pool.importPackage("java.util.Date");
			pool.importPackage("java.util.HashMap");
			pool.importPackage("java.util.HashSet");
			pool.importPackage("java.util.Hashtable");
			pool.importPackage("java.util.Vector");

			pool.importPackage("com.sunsheen.jfids.system.base.logic.core.LogicComponent");

			cc = pool.getCtClass(fullClassName);
			log.info("获取类fullClassName:" + fullClassName);
		} catch (NotFoundException e) {
			// 没有找到类，则创建
			log.error("获取类异常:" + e.toString());
			// cc = pool.makeClass(fullClassName);
			cc = pool.makeClass(fullClassName, superClass);
			log.info("创建新类fullClassName:" + fullClassName);
		}
	}

	/**
	 * 生成方法结构
	 * 
	 * @param structList
	 * @return void
	 */
	public void genStructure(List<String> bixFiles) throws CannotCompileException {
		// 获取类中已存在的方法
		List<String> existedMethods = new ArrayList<String>();
		for (CtMethod m : cc.getDeclaredMethods()) {
			existedMethods.add(makeMethodStr(m));
		}
		bixFiles.removeAll(existedMethods);
		// 遍历需要生成方法的文件，生成方法结构
		for (String bixFileStr : bixFiles) {
			String createMethodStr = BixCompilerUtil.makeCreateStatement(bixFileStr);
			log.info("JavaCompiler genStructure()-> createMethodStr:" + createMethodStr);
			cc.addMethod(CtNewMethod.make(createMethodStr, cc));
		}
	}
	
	/**
	 * 完善方法体，不需要添加run构件注解
	 * @param flowCompileUnit
	 * @throws CannotCompileException
	 */
	public void appendMethodBody(FlowCompileUnit flowCompileUnit) throws CannotCompileException {
		appendMethodBody(flowCompileUnit,null);
	}

	/**
	 * 完善方法体
	 * @param flowCompileUnit
	 * @param fileEntity
	 * @throws CannotCompileException
	 */
	@SuppressWarnings("unchecked")
	public void appendMethodBody(FlowCompileUnit flowCompileUnit,BglbFileEntity fileEntity) throws CannotCompileException {
		if(flowCompileUnit==null){
			return;
		}
		CtMethod fMethod = null;
		for (String pkgName : flowCompileUnit.getImportSet()) {
			cc.getClassPool().importPackage(pkgName);
			log.info("添加包pkgName:" + pkgName);
			log.debug("JavaCompiler appendMethodBody()->添加包pkgName:" + pkgName);
		}
		cc.getClassFile().setVersionToJava5();

		// 方法获取
//		TypeDeclaration typeDeclaration = (TypeDeclaration) unit.types().get(0);// 第一个类。这儿以后可能需要修改
//		MethodDeclaration methodDeclaration = (org.eclipse.jdt.core.dom.MethodDeclaration) typeDeclaration.bodyDeclarations().get(0);// 类里面的方法。这儿以后可能需要修改
//		String methodName = methodDeclaration.getName().getFullyQualifiedName();
		String methodName = flowCompileUnit.getFuncName();
		log.debug("JavaCompiler.appendMethodBody()->methodName:" + methodName);
		try {
			// 如果方法已存在，则移除，用while是处理同名的重载方法
			while ((fMethod = cc.getDeclaredMethod(methodName)) != null){
				cc.removeMethod(fMethod);
			}
			log.debug("JavaCompiler appendMethodBody()->已移除所有" + fMethod.getName() + "方法：");
		} catch (NotFoundException e) {
			log.debug("JavaCompiler appendMethodBody()->原有CLASS中没有[" + methodName + "]方法");
			// print("BixCompolier.complier()-> 查找方法[" + funcName + "]异常：" +
			// e.toString());
		} catch (Exception e) {
			log.warn(e);
		}

		boolean fileCanBeInvoked = flowCompileUnit.getFlow().isCanInvoked();
		int baseLineNum = flowCompileUnit.getFlow().getBaseLineNum();
		String methodBodySrc = flowCompileUnit.getCode();
		log.debug("JavaCompiler appendMethodBody()-> methodBodySrc:" + methodBodySrc);

		// 代码行号转换
		GenClassHashMap codeMap = genCodeMap(methodBodySrc);
		
		// cc.defrost();
		// ------------------------ throws CannotCompileException
		fMethod = CtNewMethod.make(baseLineNum, codeMap, cc);
		cc.addMethod(fMethod);
		// ------------------------------------------------

		log.info("JavaCompiler compileJavaFile()->创建新方法：" + fMethod.getName());

		ConstPool cp = cc.getClassFile().getConstPool();
		// 如果可以被调用，则添加注解
		if (fileCanBeInvoked) {
			// FIXME 注解的删除
			
			log.info("生成注解");
			// 添加类注解
			//AttributeInfo attributeInfo = cc.getClassFile().getAttribute(AnnotationsAttribute.visibleTag);
			AnnotationsAttribute attribute = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
			
			// @Name("Gg")
			Annotation nameAnnotation = new Annotation("org.jboss.seam.annotations.Name", cp);
			nameAnnotation.addMemberValue("value", new StringMemberValue(getFullClassName(), cp));
			//((AnnotationsAttribute)attributeInfo).addAnnotation(nameAnnotation);
			attribute.addAnnotation(nameAnnotation);
			
			// @Scope(ScopeType.SESSION)
			Annotation scopeAnnotation = new Annotation("org.jboss.seam.annotations.Scope", cp);
			EnumMemberValue emv = new EnumMemberValue(cp);
			emv.setType("org.jboss.seam.ScopeType");
			emv.setValue("SESSION");
			scopeAnnotation.addMemberValue("value", emv);
			attribute.addAnnotation(scopeAnnotation);

			cc.getClassFile().addAttribute(attribute);
			//cc.getClassFile().setVersionToJava5();

			// 添加方法注解
			MethodInfo minfo = fMethod.getMethodInfo();
			AnnotationsAttribute attr1 = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
			Annotation a1 = new Annotation("org.jboss.seam.annotations.remoting.WebRemote", cp);
			attr1.setAnnotation(a1);
			minfo.getAttributes().add(attr1);
		}
		
		if(fileEntity!=null && fileEntity.isStandComponent()){
			//添加标准构件run方法注解
			genComponentAnnotation(fileEntity, fMethod, cp);
		}
		
	}

	/**
	 * 生成标准构件所需要的Annotation
	 * @param fileEntity
	 * @param fMethod
	 * @param cp
	 */
	@SuppressWarnings("unchecked")
	private void genComponentAnnotation(BglbFileEntity fileEntity, CtMethod fMethod, ConstPool cp) {
		MethodInfo methodInfo = fMethod.getMethodInfo();
		AnnotationsAttribute attribute = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
		//@Component(name = "add", memo = "")
		Annotation componentAnnotation = new Annotation("com.sunsheen.jfids.system.bizass.annotation.Component", cp);
		componentAnnotation.addMemberValue("name", new StringMemberValue(fileEntity.getCompName(), cp));
		componentAnnotation.addMemberValue("memo", new StringMemberValue(fileEntity.getCompDescription(), cp));
		attribute.addAnnotation(componentAnnotation);
		
		log.debug("BglbCompilerUtil.complier()->:添加方法注解Component完成");
		
		/*
		 * @Params({
				@ParamItem(type = "Integer", name = "param1", comment = "参数1"),
				@ParamItem(type = "Integer", name = "param2", comment = "参数2")
			})
		 * */
		Annotation paramsAnnotion = new Annotation("com.sunsheen.jfids.system.bizass.annotation.Params", cp);
		
		ArrayMemberValue member = new ArrayMemberValue(cp);
		List<CompParamEntity> paramList = fileEntity.getCompParamList();
		AnnotationMemberValue[] members = new AnnotationMemberValue[paramList.size()];
		int i = 0;
		for (CompParamEntity param : paramList){
			Annotation memberAnnotation = new Annotation("com.sunsheen.jfids.system.bizass.annotation.ParamItem", cp);
			memberAnnotation.addMemberValue("type", new StringMemberValue(param.getPtype(), cp));
			memberAnnotation.addMemberValue("name", new StringMemberValue(param.getPname(), cp));
			memberAnnotation.addMemberValue("comment", new StringMemberValue(param.getDescription(), cp));
			members[i++] = new AnnotationMemberValue(memberAnnotation, cp);
		}
		member.setValue(members);
		paramsAnnotion.addMemberValue("value", member);
		log.debug("BglbCompilerUtil.complier()->:添加方法注解Params完成");
		attribute.addAnnotation(paramsAnnotion);
		
		//@Returns(retValue = { @ReturnItem(type = "Integer", name = "ret", comment = "返回值") })
		Annotation returnsAnnotion = new Annotation("com.sunsheen.jfids.system.bizass.annotation.Returns", cp);
		
		ArrayMemberValue retMember = new ArrayMemberValue(cp);
		List<CompParamEntity> retList = fileEntity.getCompRetList();
		AnnotationMemberValue[] retMembers = new AnnotationMemberValue[retList.size()];
		i = 0;
		for (CompParamEntity ret : retList){
			Annotation memberAnnotation = new Annotation("com.sunsheen.jfids.system.bizass.annotation.ReturnItem", cp);
			memberAnnotation.addMemberValue("type", new StringMemberValue(ret.getPtype(), cp));
			memberAnnotation.addMemberValue("name", new StringMemberValue(ret.getPname(), cp));
			memberAnnotation.addMemberValue("comment", new StringMemberValue(ret.getDescription(), cp));
			retMembers[i++] = new AnnotationMemberValue(memberAnnotation, cp);
		}
		retMember.setValue(retMembers);
		returnsAnnotion.addMemberValue("retValue", retMember);
		log.debug("BglbCompilerUtil.complier()->:添加方法注解Returns完成");
		attribute.addAnnotation(returnsAnnotion);
		
		/*
		 * @Example(exeampleValue = {
				@ExampleItem(exeCom = "Java业务构件示例")
			})
		 * */
		Annotation exampleAnnotion = new Annotation("com.sunsheen.jfids.system.bizass.annotation.Example", cp);
		
		ArrayMemberValue exampleMember = new ArrayMemberValue(cp);
		String example = fileEntity.getCompExample();
		String lines[] = StringUtils.split(example);
		AnnotationMemberValue[] exampleMembers = new AnnotationMemberValue[lines.length];
		i = 0;
		for (String exampleLine : lines){
			Annotation memberAnnotation = new Annotation("com.sunsheen.jfids.system.bizass.annotation.ExampleItem", cp);
			memberAnnotation.addMemberValue("exeCom", new StringMemberValue(exampleLine, cp));
			exampleMembers[i++] = new AnnotationMemberValue(memberAnnotation, cp);
		}
		exampleMember.setValue(exampleMembers);
		exampleAnnotion.addMemberValue("exeampleValue", exampleMember);
		log.debug("BglbCompilerUtil.complier()->:添加方法注解Returns完成");
		attribute.addAnnotation(exampleAnnotion);
		
		
		methodInfo.getAttributes().add(attribute);
	}

	// 记录debug信息
	public void addSourceDebugLine(IFile file, FlowImpl flowImpl) {
//		String fullpath = file.getFullPath().toString();
		String fullpath = ProjectPathUtils.getModuleSubTargetPath(file).toString();
		SourceDebugFile sdf = sourceDebugFileMap.get(fullpath);
		if (sdf == null) {
			sourceDebugFileMap.put(fullpath, sdf = new SourceDebugFile(file.getName(), fullpath));
		}

		EList<Node> nodes = flowImpl.getNodes();
		// 查找出开始节点和结束节点
		for (Node node : nodes) {
			if (node instanceof StartImpl)
				continue;
			if (node instanceof EndImpl)
				continue;
			sdf.getDebugLines().add(new SourceDebugLine(node.getAbsLineNum()));
		}
	}

	public void finishAndSave(IFolder sourceFolder, IFolder targetFolder) {
		SourceDebugExtensionUtils.addSourceDebugExtensionAttribute(cc.getClassFile(), sourceFolder.getName(), Constants.FILE_EXT_BIX,
				sourceDebugFileMap.values().toArray(new SourceDebugFile[0]));
		cc.getClassFile().setVersionToJava5();
		String targetPath = targetFolder.getLocation().toString();
		try {
			cc.writeFile(targetPath);
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cc.detach();// 从pool中移除，防止内存过度消耗。
		log.info("已生成文件:" + targetPath + "/" + getFullClassName());

	}

	// @SuppressWarnings("restriction")
	// public static void compileJavaFile(IFile sourceFile, IFolder
	// targetFolder, FlowImpl flowImpl, String pakName, String className,
	// CompilationUnit unit) {
	// Boolean isComplier = true;// 是否进行编译保存操作
	// // 基础数据构造
	// // String sourceFilePath = sourceFile.getLocation().toString();
	// String targetPath = targetFolder.getLocation().toString();
	//
	// String fullClassName = pakName + "." + className;
	//
	// // 遍历当前目录文件，以生成空方法体结构
	// List<String> bixFiles = BixCompilerUtil.scanBixFile(sourceFile);
	//
	// // ----------------------下面为字解码操作--------------------------
	// ClassPool pool = ClassPool.getDefault();
	// CtClass cc = null;
	// CtClass superClass = null;
	// CtMethod fMethod = null;
	//
	// // 添加编译路径
	// try {
	// List<String> poolPaths = findPoolPath(sourceFile.getProject());
	// for(String addPath : poolPaths){
	// pool.appendClassPath(addPath);
	// }
	// } catch (NotFoundException e) {
	// log.error("添加classpath异常：" + e.toString());
	// }
	//
	// // 获得类
	// try {
	// superClass =
	// pool.getCtClass("com.sunsheen.jfids.system.base.service.bix.ABixService");
	// cc = pool.getCtClass(fullClassName);
	// log.info("获取类fullClassName:" + fullClassName);
	// } catch (NotFoundException e) {
	// // 没有找到类，则创建
	// log.error("获取类异常:" + e.toString());
	// //cc = pool.makeClass(fullClassName);
	// cc = pool.makeClass(fullClassName, superClass);
	// log.info("创建新类fullClassName:" + fullClassName);
	// }
	// // cc.defrost();
	//
	// // 获取类中已存在的方法
	// List<String> existedMethods = new ArrayList<String>();
	// for(CtMethod m : cc.getDeclaredMethods()){
	// existedMethods.add(makeMethodStr(m));
	// }
	// bixFiles.removeAll(existedMethods);
	// try {
	// //遍历需要生成方法的文件，生成方法结构
	// for(String bixFileStr : bixFiles){
	// String createMethodStr = BixCompilerUtil.makeCreateStatement(bixFileStr);
	// cc.addMethod(CtNewMethod.make(createMethodStr,cc));
	// }
	// } catch (CannotCompileException e) {
	// e.printStackTrace();
	// }
	//
	//
	// // 插入导入的包
	// Set<String> importPkgSet = new HashSet<String>();
	// for (Object impPkg : unit.imports()) {
	// importPkgSet.add(impPkg.toString().replace("import", "").replace(";",
	// "").trim());
	// }
	// for (String pkgName : importPkgSet) {
	// pool.importPackage(pkgName);
	// log.info("添加包pkgName:" + pkgName);
	// }
	//
	// // 方法获取
	// TypeDeclaration typeDeclaration = (TypeDeclaration)
	// unit.types().get(0);// 第一个类。这儿以后可能需要修改
	// MethodDeclaration methodDeclaration =
	// (org.eclipse.jdt.core.dom.MethodDeclaration)
	// typeDeclaration.bodyDeclarations().get(0);// 类里面的方法。这儿以后可能需要修改
	// String methodName = methodDeclaration.getName().getFullyQualifiedName();
	// try {
	// // 如果方法已存在，则移除
	// fMethod = cc.getDeclaredMethod(methodName);
	// cc.removeMethod(fMethod);
	// log.info("已移除方法：" + fMethod.getName());
	// } catch (NotFoundException e) {
	// log.info("原有CLASS中没有[" + methodName + "]方法");
	// // print("BixCompolier.complier()-> 查找方法[" + funcName + "]异常：" +
	// // e.toString());
	// } catch (Exception e) {
	// log.info(e);
	// }
	//
	// boolean fileCanBeInvoked = flowImpl.isCanInvoked();
	// int baseLineNum = flowImpl.getBaseLineNum();
	// String methodBodySrc = methodDeclaration.toString();
	//
	// try {
	//
	// // 代码行号转换
	// Map<Integer, String> codeMap = genCodeMap(methodBodySrc);
	// // FIXME todo...
	// // cc.defrost();
	// fMethod = CtNewMethod.make(baseLineNum, codeMap, cc);
	//
	// cc.addMethod(fMethod);
	// log.info("JavaCompiler compileJavaFile()->创建新方法：" + fMethod.getName());
	// log.info("JavaCompiler compileJavaFile()->方法体为:\n" + methodBodySrc);
	//
	// // 如果可以被调用，则添加注解
	// if (fileCanBeInvoked) {
	// // FIXME 注解的删除
	//
	// log.info("生成注解");
	// // 添加类注解
	// ConstPool cp = cc.getClassFile().getConstPool();
	// AnnotationsAttribute attr = new AnnotationsAttribute(cp,
	// AnnotationsAttribute.visibleTag);
	// // @Name("Gg")
	// Annotation a = new Annotation("org.jboss.seam.annotations.Name", cp);
	// a.addMemberValue("value", new StringMemberValue(fullClassName, cp));
	// attr.addAnnotation(a);
	// // @Scope(ScopeType.SESSION)
	// Annotation a2 = new Annotation("org.jboss.seam.annotations.Scope", cp);
	// EnumMemberValue emv = new EnumMemberValue(cp);
	// emv.setType("org.jboss.seam.ScopeType");
	// emv.setValue("SESSION");
	// a2.addMemberValue("value", emv);
	// attr.addAnnotation(a2);
	//
	// cc.getClassFile().addAttribute(attr);
	// cc.getClassFile().setVersionToJava5();
	//
	// // 添加方法注解
	// MethodInfo minfo = fMethod.getMethodInfo();
	// AnnotationsAttribute attr1 = new AnnotationsAttribute(cp,
	// AnnotationsAttribute.visibleTag);
	// Annotation a1 = new
	// Annotation("org.jboss.seam.annotations.remoting.WebRemote", cp);
	// attr1.setAnnotation(a1);
	// minfo.getAttributes().add(attr1);
	// }
	// } catch (CannotCompileException e1) {
	// // 处理引用相关的class没有编辑的情况
	//
	// isComplier = false;// 遇到异常，则对该次编译不进行保存操作。
	// // [source error] index() not found in
	// // test.sunsheen.jfids.studio.index
	// // [source error] no such class: test.sunsheen.jfids.studio.index
	// String errorMsg = e1.getMessage();
	// if (errorMsg != null && errorMsg.lastIndexOf("not found in") != -1) {
	// String[] strs = errorMsg.split("not found in");
	// if (strs[1] != null && strs[0] != null) {
	// IProject project = sourceFile.getProject();
	// String methodNameNoComplier = strs[0].replace("[source error]",
	// "").trim();// 获取方法名
	// String filePath = strs[1].trim().replace(".", "/") + ".bix/" +
	// methodNameNoComplier.substring(0, methodNameNoComplier.lastIndexOf("("))
	// + ".bix";
	// String projectName = project.getName();
	//
	// List<String> srcs = ClasspathUtils.getAllSrcPath(project);
	// Boolean successComplier = false;
	// for (String src : srcs) {
	// // System.out.println(src.replace(projectName,
	// // "").substring(1).replace("\\", "/")+"/"+filePath);
	// IFile iFile = project.getFile(src.replace(projectName,
	// "").substring(1).replace("\\", "/") + "/" + filePath);
	// if (iFile.exists() && iFile.isAccessible()) {
	// successComplier = logic.diagram.compiler.BixCompilerUtil.complier(iFile,
	// targetFolder);
	// }
	// // System.out.println(iFile);
	// }
	// if (successComplier) {
	// logic.diagram.compiler.BixCompilerUtil.complier(sourceFile,
	// targetFolder);
	// }
	//
	// }
	// }
	// // 编译出错
	// log.error("编译出错：" + e1.toString());
	//
	// log.error("\n" + methodBodySrc);
	// }
	// if (isComplier) {
	// // 保存class文件
	// try {
	// cc.getClassFile().setVersionToJava5();
	// cc.writeFile(targetPath);
	// cc.detach();// 从pool中移除，防止内存过度消耗。
	// log.info("已生成文件:" + targetPath + "/" + fullClassName);
	//
	// refreshFile(sourceFile, targetFolder, className);
	// } catch (CannotCompileException e) {
	// log.error(e.toString());
	// } catch (IOException e) {
	// log.error(e.toString());
	// } catch (CoreException e) {
	// log.error(e.toString());
	// }
	// }
	//
	// }

	/**
	 * 将Method方法转换为字串表示
	 * 
	 * @param m
	 *            CtMethod对象
	 * @return String 返回字串表示如：void_funcName_arg1_arg2_...
	 */
	private static String makeMethodStr(CtMethod m) {
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(m.getReturnType().getName()).append("_");
			sb.append(m.getName()).append("_");

			for (CtClass paramType : m.getParameterTypes()) {
				sb.append(paramType.getName()).append("_");
			}

		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		// log.info("JavaCompiler makeMethodStr()-> sb.toString():" +
		// sb.toString());
		return sb.toString();
	}

	/**
	 * 从工程中加载编译路径
	 * 
	 * @param project
	 *            当前工程
	 * @return List<String> 返回jar路径和classes以及component目录，用于编译时加载类
	 */
	public static List<String> findPoolPath(IProject project) {
		List<String> retList = new ArrayList<String>();
		String prjPath = project.getLocation().toString();
		retList.add(prjPath.concat("/WebRoot/WEB-INF/classes"));
		retList.add(prjPath.concat("/WebRoot/WEB-INF/component"));
		//添加相关库工程
		for(IProject rprj : ProjectUtils.listRefrenceLibProjects(project)){
			prjPath = rprj.getLocation().toString();
			retList.add(prjPath.concat("/WebRoot/WEB-INF/classes"));
			retList.add(prjPath.concat("/WebRoot/WEB-INF/component"));
		}
		// FIXME 引用库工程中添加了jar包需处理
		
		List<String> jarList = ClasspathUtils.listJarLibsWithoutEncrypt(project);
		retList.addAll(jarList);
		return retList;
	}

	/**
	 * 触发eclipse的文件更新事件
	 * 
	 * @param sourceFile
	 * @param targetFolder
	 * @param className
	 * @throws CoreException
	 */
	private static void refreshFile(IFile sourceFile, IFolder targetFolder, String className) throws CoreException {
		String classDir = sourceFile.getLocation().removeLastSegments(2).toString();
		int srcIndex = classDir.lastIndexOf("src");
		int comIndex = classDir.length();
		srcIndex = srcIndex < 0 ? 0 : srcIndex + 4;
		if (srcIndex < comIndex) {
			classDir = classDir.substring(srcIndex, comIndex);
			IFile javaFile = targetFolder.getFolder(classDir).getFile(className + ".class");
			javaFile.refreshLocal(0, null);
		} else {
			targetFolder.getFolder(classDir).refreshLocal(0, null);
		}

	}

	/**
	 * 通过代码字串生成构造行号所需的代码集合
	 * 
	 * @param methodBodyStr
	 *            方法代码字串，含换行符
	 * @return 生成代码集合，每行为一个元素，方法申明必须是0
	 */
	private static GenClassHashMap genCodeMap(String methodBodyStr) {

		GenClassHashMap code = new GenClassHashMap();
		StringBuilder tmpLine = new StringBuilder(); // 用于保存集合中的一行文本
		int lineNum = 0; // 放入集合中的行号

		String lines[] = methodBodyStr.split("\n");
		boolean hasReturnStatement = false; // 标记生成语句中是否含有return语句

		boolean markComment = false;
		for (int i = 0; i < lines.length - 1; i++) {
			//log.debug("line["+i+"]=>" + lines[i]);
			// 去掉注释
			if (lines[i].startsWith("//")) {
				continue;
			} else if (lines[i].startsWith("/*")) {
				markComment = true;
				continue;
			} else if (lines[i].endsWith("*/")) {
				markComment = false;
				continue;
			}
			if (markComment) {
				continue;
			}

			if (i == 0) {
				// 第一行，方法声明，单独放在0行
				if (code.containsKey(lineNum)) {
					log.warn("JavaCompiler genCodeMap()-> :lineNum[" + lineNum + "]已存在");
				}
				code.put(lineNum, lines[i]);
				log.debug("JavaCompiler genCodeMap()-> code.put(" + lineNum + "=>" + lines[i]);

				lineNum++;
				// 如果注释掉上一行,则会有两个0行,编译出错

			} else {
				if (lines[i].contains(GenerateJavaCodeUtil.SUNSHEEN_LOGIC_LINENUMBER_VALUE)) {
					// 有新行号，将之前的数据放入集合
					if (code.containsKey(lineNum)) {
						log.warn("JavaCompiler genCodeMap()-> :lineNum[" + lineNum + "]已存在");
					}
					code.put(lineNum, tmpLine.toString());
					log.debug("JavaCompiler genCodeMap()-> code.put(" + lineNum + "=>" + tmpLine.toString());
					lineNum = parseLineNum(lines[i]);
					tmpLine.setLength(0);

				} else if (lines[i].trim().startsWith("//")) {
					// 跳过注释行
					continue;
				} else {
					if (lines[i].trim().startsWith("return")) {
						hasReturnStatement = true;
						if (code.containsKey(lineNum)) {
							log.warn("JavaCompiler genCodeMap()-> :lineNum[" + lineNum + "]已存在");
						}
						code.put(lineNum, tmpLine.toString());
						log.debug("JavaCompiler genCodeMap()-> code.put(" + lineNum + "=>" + tmpLine.toString());

						// return 应该是最后一句，处理完后跳出循环
						code.put(-2, lines[i]);
						log.debug("JavaCompiler genCodeMap()-> code.put(-2=>" + lines[i]);
						break;
					} else {
						tmpLine.append(StringUtils.chomp(lines[i]));
					}
				}
			}
		}

		// 处理最后一个行号
		if (!hasReturnStatement && tmpLine.toString().length() > 0) {
			if (code.containsKey(lineNum)) {
				log.warn("JavaCompiler genCodeMap()-> :lineNum[" + lineNum + "]已存在");
			}
			code.put(lineNum, tmpLine.toString());
			code.put(-2, "return;");
			log.debug("JavaCompiler genCodeMap()-> code.put(" + lineNum + "=>" + tmpLine.toString());
			log.debug("JavaCompiler genCodeMap()-> code.put(-2=>return;");
		}
		// 放方法的最后一行，应该是一个后括号}
		code.put(-1, lines[lines.length - 1]);
		log.debug("JavaCompiler genCodeMap()-> code.put(-1=>" + lines[lines.length - 1]);
		
		// 下面的输入信息只为了调试用
		//*
		StringBuilder sb = new StringBuilder();
		for(RowNumber i : code.keySet()){
			sb.append("code.put(").append(i).append(",\"").append(code.get(i).replaceAll("\"", "\\\\\"")).append("\");\n");
		}
		log.debug("JavaCompiler.genCodeMap()->:Map\n" + sb);
		//*/
		

		return code;
	}

	/**
	 * 从带有行号信息的注释串中解析出行号
	 * 
	 * @param str
	 *            传入包含行号信息的注释如///[node-line]=3
	 * @return 返回行号值，如：3
	 */
	private static int parseLineNum(String str) {
		//log.debug("JavaCompiler.parseLineNum()->str:" + str);
		int retVal = 0;
		int index = str.indexOf("=");
		String num = str.substring(index + 1).replace(";", "");
		if (num != null && num.length() > 0) {
			retVal = Integer.valueOf(num);
		}
		return retVal;
	}
}
