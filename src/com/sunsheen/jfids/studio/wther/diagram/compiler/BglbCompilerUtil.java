package com.sunsheen.jfids.studio.wther.diagram.compiler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

import com.sunsheen.jfids.studio.javaassist.CannotCompileException;
import com.sunsheen.jfids.studio.javaassist.ClassPool;
import com.sunsheen.jfids.studio.javaassist.CtClass;
import com.sunsheen.jfids.studio.javaassist.CtField;
import com.sunsheen.jfids.studio.javaassist.CtMethod;
import com.sunsheen.jfids.studio.javaassist.CtNewMethod;
import com.sunsheen.jfids.studio.javaassist.Modifier;
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
import com.sunsheen.jfids.studio.logging.LogFactory;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.BglbFileEntity;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.DataVarEntity;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.GlobalVarEntity;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.InterfaceTree;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaCompiler;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.LogicComplierUtil;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ValidateUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FindClass;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.GlobalBixUtil;

public class BglbCompilerUtil {
	static final Logger log = LogFactory.getLogger(BglbCompilerUtil.class.getName());

	private static String _SPLITER = "_";

	private static IProject currentProject = null;

	private static String pkgName;
	private static String classAndMethodName;
	private static String classFullName;
	private static String className;
	private static String methodName;
	private static String debugFieldName;
	private static BglbFileEntity fileEntity;

	/**
	 * 构造基础信息
	 * 
	 * @param sourceFile
	 * @param targetFolder
	 */
	private static void init(IFile sourceFile) {
		// 包名
		pkgName = LogicComplierUtil.getPackageName(sourceFile.getLocation().removeLastSegments(2).toString(), true);
		// 类到方法的全名
		classAndMethodName = sourceFile.getLocation().removeLastSegments(1).lastSegment();
		// 类名
		className = LogicComplierUtil.covertFolderToClassAndMethodName(classAndMethodName, false);
		// 类全名
		classFullName = pkgName + "." + className;

		log.debug("BglbCompilerUtil init()-> pkgName:" + pkgName);
		log.debug("BglbCompilerUtil init()-> classAndMethodName:" + classAndMethodName);
		log.debug("BglbCompilerUtil init()-> className:" + className);
		log.debug("BglbCompilerUtil init()-> classFullName:" + classFullName);
	}

	/**
	 * 将bix文件编译为class
	 * 
	 * @param sourceFile
	 *            传入的bix文件
	 * @param targetFolder
	 * @return
	 */
	public static boolean complier(IFile sourceFile, IFolder targetFolder) {
		log.info("BglbCompilerUtil complier()-> sourceFile.toString():" + sourceFile.toString());
		currentProject = sourceFile.getProject();
		init(sourceFile);
		String targetPath = targetFolder.getLocation().toString();
		Set<String> interfaces = new HashSet<String>();

		boolean succ = false;
		// fetchFileStr(sourceFile);
		fileEntity = getEntityFromFile(sourceFile);
		if(fileEntity == null){
			log.debug("BglbCompilerUtil.complier()->:bglbFile is null,不进行编译");
			return false;
		}
		
		Set<Method> unimplementMethodSet = new HashSet<Method>();
		for (InterfaceTree interfaceTree : fileEntity.getInterfaceTree()) {
			String interfaceName = interfaceTree.getLabelStr();
			log.debug("BglbCompilerUtil complier()-> interfaceTree.getLabelStr():" + interfaceName);
			interfaces.add(interfaceName);
			unimplementMethodSet.addAll(getUnimplementMethodSet(interfaceName));
		}

		log.info("BglbCompilerUtil complier()-> :需要实现的方法：");
		for (Method method : unimplementMethodSet) {
			String retType = method.getReturnType().getName();
			String funName = method.getName();
			log.info("BglbCompilerUtil complier()-> m[" + method.getModifiers() + "]:" + retType + " " + funName
					+ "(...)");
		}

		ClassPool pool = ClassPool.getDefault();
		CtClass cc = null;
		// 添加路径
		try {
			// pool.appendClassPath(targetPath);
			List<String> poolPaths = JavaCompiler.findPoolPath(currentProject);
			for (String addPath : poolPaths) {
				pool.appendClassPath(addPath);
			}

			print("BglbCompilerUtil.drop()-> 添加classpath:" + targetPath);
		} catch (NotFoundException e) {
			log.error("BglbCompilerUtil.drop()-> 添加classpath异常：" + e.toString());
		}

		// 获得类
		try {
			cc = pool.getCtClass(classFullName);
			print("BglbCompilerUtil.drop()-> 获取类fullClassName:" + classFullName);

		} catch (NotFoundException e) {
			// 没有找到类或方法，不再继续删除操作
			log.error("BglbCompilerUtil.drop()-> 找不到类:" + e.toString());
			cc = pool.makeClass(classFullName);
			print("BglbCompilerUtil.drop()-> 创建新类fullClassName:" + classFullName);
		}

		// 添加接口，和处理全局变量
		try {
			Set<CtClass> interfaceSet = new HashSet<CtClass>();
			for (String interfaceName : interfaces) {
				CtClass interfaceClass = pool.getCtClass(interfaceName);
				interfaceSet.add(interfaceClass);
			}

			// 重新设置接口，即可移除原有接口
			cc.setInterfaces(interfaceSet.toArray(new CtClass[interfaceSet.size()]));

			// 处理全局变量
			dealGlobalVar(cc, pool);

			// 处理数据变量
			dealDataVar(cc, pool);

			log.info("BglbCompilerUtil complier()-> :已实现的方法：");
			filterMethod(unimplementMethodSet, getMethodListStr(cc));
			// 验证接口方法是否实现
			ValidateUtil.validateInterfacesImpl(sourceFile, unimplementMethodSet);

			// 根据集合生成方法
			// 接口实现方法的实现，已经通过生成方法文件bix来实现，这里需要在生成，防止方法但是class文件中未删除的情况
			// for(Method createMethod : unimplementMethodSet){
			// String methodStr = genMethodStr(createMethod);
			// log.info("BglbCompilerUtil complier()-> methodStr:" + methodStr);
			// cc.addMethod(CtNewMethod.make(methodStr, cc));
			// }
			
			//生成run 注解
			//CtMethod runMethod = cc.getDeclaredMethod("run", new CtClass[]{pool.getCtClass("java.util.Map")});
			if(fileEntity.isStandComponent()){
				//需要继承父类 com.sunsheen.jfids.system.bizass.core.ABaseComponent
				log.debug("BglbCompilerUtil.complier()->:添加标准构件父类 com.sunsheen.jfids.system.bizass.core.ABaseComponent");
				cc.setSuperclass(pool.getCtClass("com.sunsheen.jfids.system.bizass.core.ABaseComponent"));
				
				//模拟标准构件注解的生成
				log.debug("BglbCompilerUtil.complier()->:生成标准注解构件");
				//添加类注解
				ConstPool cp = cc.getClassFile().getConstPool();
				
				AnnotationsAttribute attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
				// @BixComponentPackage(dirname="SetName")
				Annotation componentPackageAnnotation = new Annotation("com.sunsheen.jfids.system.bizass.annotation.BixComponentPackage", cp);
				componentPackageAnnotation.addMemberValue("dirname", new StringMemberValue(fileEntity.getDirName(), cp));
				attr.addAnnotation(componentPackageAnnotation);
				cc.getClassFile().addAttribute(attr);
				log.debug("BglbCompilerUtil.complier()->:添加类注解完成");
				
				//-----------------------------------------------------------------------
				
				//添加方法注释
//				MethodInfo methodInfo = runMethod.getMethodInfo();
//				AnnotationsAttribute attribute = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
//				//@Component(name = "add", memo = "")
//				Annotation componentAnnotation = new Annotation("com.sunsheen.jfids.system.bizass.annotation.Component", cp);
//				componentAnnotation.addMemberValue("name", new StringMemberValue(fileEntity.getCompName(), cp));
//				componentAnnotation.addMemberValue("memo", new StringMemberValue(fileEntity.getCompDescription(), cp));
//				attribute.addAnnotation(componentAnnotation);
//				
//				log.debug("BglbCompilerUtil.complier()->:添加方法注解Component完成");
//				
//				/*
//				 * @Params({
//						@ParamItem(type = "Integer", name = "param1", comment = "参数1"),
//						@ParamItem(type = "Integer", name = "param2", comment = "参数2")
//					})
//				 * */
//				Annotation paramsAnnotion = new Annotation("com.sunsheen.jfids.system.bizass.annotation.Params", cp);
//				
//		        
//		        
//		        ArrayMemberValue member = new ArrayMemberValue(cp);
//		        AnnotationMemberValue[] members = new AnnotationMemberValue[2];
//		        for (int i = 0; i < 2; i++){
//		        	Annotation memberAnnotation = new Annotation("com.sunsheen.jfids.system.bizass.annotation.ParamItem", cp);
//		        	memberAnnotation.addMemberValue("type", new StringMemberValue("java.lang.Integer", cp));
//		        	memberAnnotation.addMemberValue("name", new StringMemberValue("param"+i, cp));
//		        	memberAnnotation.addMemberValue("comment", new StringMemberValue("aabbccde"+i, cp));
//		        	members[i] = new AnnotationMemberValue(memberAnnotation, cp);
//		        }
//		        member.setValue(members);
//		        paramsAnnotion.addMemberValue("value", member);
//		        log.debug("BglbCompilerUtil.complier()->:添加方法注解Params完成");
//		        
//				attribute.addAnnotation(paramsAnnotion);
//				
//				methodInfo.getAttributes().add(attribute);
				
			}
		} catch (CannotCompileException e) {
			log.info("BglbCompilerUtil complier()-> :编译异常");
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			cc.getClassFile().setVersionToJava5();
			cc.writeFile(targetPath);
			cc.detach();
			print("BglbCompilerUtil complier()-> 已更新文件:" + targetPath + "/" + classFullName);

			succ = true;
		} catch (CannotCompileException e) {
			log.error("BglbCompilerUtil.complier -> " + e.toString());
		} catch (IOException e) {
			log.error("BglbCompilerUtil.complier -> " + e.toString());
		}

		succ = true;
		return succ;
	}

	public static BglbFileEntity getFileEntity() {
		return fileEntity;
	}
	
	public static void setFileEntity(BglbFileEntity fileEntity) {
		BglbCompilerUtil.fileEntity = fileEntity;
	}

	/**
	 * 处理全局变量的字段生成，删除原有的字段及get,set方法，重新创建新的字段及get,set方法
	 * 
	 * @param cc
	 * @throws NotFoundException
	 * @return void
	 * @throws CannotCompileException
	 */
	private static void dealGlobalVar(CtClass cc, ClassPool pool) throws NotFoundException, CannotCompileException {
		// 移除原有的字段和方法
		CtField fields[] = cc.getDeclaredFields();
		CtMethod methods[] = cc.getDeclaredMethods();
		for (CtField field : fields) {
			cc.removeField(field);
			log.info("删除字段信息：" + field.getName());

			String fieldName = field.getName();
			Character chr = Character.toUpperCase(fieldName.charAt(0));
			fieldName = chr + fieldName.substring(1);
			String getterFunc = "get".concat(fieldName);
			String setterFunc = "set".concat(fieldName);
			for (CtMethod method : methods) {
				if (method.getName().equals(getterFunc) || method.getName().equals(setterFunc)) {
					cc.removeMethod(method);
					log.info("删除方法" + method.getName());
				}
			}
		}

		// 添加字段和方法
		for (GlobalVarEntity glbVar : fileEntity.getGlobalVarList()) {
			String fieldName = glbVar.getVarName();
			log.debug("BglbCompilerUtil dealGlobalVar()-> :添加全局变量字段及方法 varName[" + fieldName + "] varType["
					+ glbVar.getVarType() + "]");
			CtField newField = new CtField(pool.get(glbVar.getVarType()), fieldName, cc);
			newField.setModifiers(Modifier.PRIVATE);
			cc.addField(newField);
			log.debug("BglbCompilerUtil dealGlobalVar()-> :" + newField);

			cc.addMethod(CtNewMethod.getter(genGetterSetterName("get", fieldName), newField));
			cc.addMethod(CtNewMethod.setter(genGetterSetterName("set", fieldName), newField));
			log.debug("BglbCompilerUtil dealGlobalVar()-> :完成getter和setter方法的创建" + fieldName);
		}
	}

	/**
	 * 处理数据变量的生成
	 * 
	 * @param cc
	 * @param pool
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	private static void dealDataVar(CtClass cc, ClassPool pool) throws NotFoundException, CannotCompileException {
		// 添加字段和方法
		for (DataVarEntity dataVar : fileEntity.getDataVarList()) {
			String fieldName = dataVar.getVarName();
			log.debug("BglbCompilerUtil dealDataVar()-> :添加数据变量字段及方法 varName[" + fieldName + "]");
			CtField newField = new CtField(pool.get(dataVar.getInterfaceType()), fieldName, cc);
			newField.setModifiers(Modifier.PRIVATE);
			cc.addField(newField);
			log.debug("BglbCompilerUtil dealDataVar()-> :" + newField);

			cc.addMethod(CtNewMethod.getter(genGetterSetterName("get", fieldName), newField));

			// 添加setter及注解
			CtMethod setter = CtNewMethod.setter(genGetterSetterName("set", fieldName), newField);

			// 设置引用 Annotation
			ConstPool cp = cc.getClassFile().getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
			attr.setAnnotation(new Annotation("org.osoa.sca.annotations.Reference", cp));
			setter.getMethodInfo().getAttributes().add(attr);

			cc.addMethod(setter);

			log.debug("BglbCompilerUtil dealDataVar()-> :完成getter和setter方法及注解的创建" + fieldName);
		}
	}

	/**
	 * 处理getter和setter方法名的生成
	 * 
	 * @param prefix
	 *            前缀标记，get/set
	 * @param fieldName
	 *            字段名field
	 * @return String 返回如getField/setField
	 */
	private static String genGetterSetterName(String prefix, String fieldName) {
		Character chr = Character.toUpperCase(fieldName.charAt(0));
		fieldName = chr + fieldName.substring(1);
		return prefix.concat(fieldName);
	}

	/**
	 * 根据方法生成创建空方法的代码
	 * 
	 * @param method
	 * @return String
	 */
	private static String genMethodStr(Method method) {
		StringBuilder sb = new StringBuilder();
		sb.append("public");
		// int modifier = method.getModifiers();
		// if((Modifier.PUBLIC & modifier)!=0){
		// //is public
		// log.info("BglbCompilerUtil genMethodStr()-> :is public");
		// sb.append("public ");
		// }
		String retType = method.getReturnType().getName();
		sb.append(" ").append(retType);
		sb.append(" ").append(method.getName()).append("(");
		int argIndex = 0;
		for (Class p : method.getParameterTypes()) {
			sb.append(p.getName()).append(" arg").append(argIndex++).append(",");
		}
		if (sb.toString().endsWith(",")) {
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		sb.append("){");

		// 对返回的处理
		if ("boolean".equals(retType)) {
			sb.append("return true;");
		} else if ("int".equals(retType)) {
			sb.append("return 0;");
		} else if ("long".equals(retType)) {
			sb.append("return 0;");
		} else if ("float".equals(retType)) {
			sb.append("return 0;");
		} else if ("double".equals(retType)) {
			sb.append("return 0;");
		} else if ("char".equals(retType)) {
			sb.append("return '0';");
		} else if ("short".equals(retType)) {
			sb.append("return 0;");
		} else if ("byte".equals(retType)) {
			sb.append("return 0;");
		} else {
			sb.append("return null;");
		}

		sb.append("}");
		return sb.toString();
	}

	/**
	 * 过滤已经实现的接口，根据已实现的方法，删除接口中对应的需要实现的方法集合
	 * 
	 * @param unimplementMethodSet
	 *            接口中需要实现的方法集合
	 * @param methodListStr
	 *            类中已实现的方法
	 * @return void
	 */
	private static void filterMethod(Set<Method> unimplementMethodSet, Set<String> methodListStr) {
		Set<Method> needToDel = new HashSet<Method>();
		for (Method m : unimplementMethodSet) {
			String methodStr = m.getReturnType().getName().concat(_SPLITER);
			methodStr += m.getName().concat(_SPLITER);
			for (Class c : m.getParameterTypes()) {
				methodStr += c.getName().concat(_SPLITER);
			}
			log.info("BglbCompilerUtil filterMethod(Method)-> methodStr:" + methodStr);
			if (methodListStr.contains(methodStr)) {
				log.info("EXIST!!!");
				needToDel.add(m);
			}
		}
		log.info("BglbCompilerUtil filterMethod()-> 清理前unimplementMethodSet.size():" + unimplementMethodSet.size());
		unimplementMethodSet.removeAll(needToDel);
		log.info("BglbCompilerUtil filterMethod()-> 清理后unimplementMethodSet.size():" + unimplementMethodSet.size());

	}

	/**
	 * 通过CtClass获得当前类中已实现的方法字串
	 * 
	 * @param cc
	 *            传入的class
	 * @throws NotFoundException
	 * @return Set<String> 返回每个方法形成的字串集合 void_funname_arg1_arg2_
	 */
	private static Set<String> getMethodListStr(CtClass cc) throws NotFoundException {
		Set<String> retSet = new HashSet<String>();

		for (CtMethod m : cc.getDeclaredMethods()) {
			String tmpStr = "";
			String retType = m.getReturnType().getName();
			String name = m.getName();
			tmpStr = (retType + _SPLITER + name + _SPLITER);
			for (CtClass paramType : m.getParameterTypes()) {
				tmpStr += (paramType.getName() + _SPLITER);
			}
			log.info("BglbCompilerUtil getMethodListStr()-> tmpStr:" + tmpStr);
			retSet.add(tmpStr);
		}
		return retSet;
	}

	/**
	 * 从文件中获得对象
	 * 
	 * @param sourceFile
	 *            传入的bglb文件
	 * @return void
	 */
	private static BglbFileEntity getEntityFromFile(IFile sourceFile) {
		/*fileEntity = GlobalBixUtil.getGlobalEntityFromIFile(sourceFile);
		if (fileEntity == null) {
			fileEntity = new BglbFileEntity();
		}*/
		return GlobalBixUtil.getGlobalEntityFromIFile(sourceFile);
	}

	/**
	 * 查找当前接口需要实现的方法
	 * 
	 * @param interfaceName
	 *            传入的接口名
	 * @return Set<Method> 返回当前接口需要实现的方法集合
	 */
	private static Set<Method> getUnimplementMethodSet(String interfaceName) {
		Set<Method> retSet = new HashSet<Method>();
		try {
			Class<?> demo = FindClass.fromString(interfaceName, currentProject);
			// Class<?> demo = FindClass.fromString(interfaceName);
			for (Method m : demo.getDeclaredMethods()) {
				retSet.add(m);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return retSet;
	}

	/**
	 * 删除文件时，删除对应的方法
	 * 
	 * @param sourceFile
	 *            传入的bix文件
	 * @param targetFolder
	 *            目标目录
	 * @return
	 */
	public static boolean drop(IFile sourceFile, IFolder targetFolder) {
		boolean succ = false;

		init(sourceFile);

		String targetPath = targetFolder.getLocation().toString();

		print("BglbCompilerUtil.drop()-> targetPath:" + targetPath);
		print("BglbCompilerUtil.drop()-> pkgName:" + pkgName);
		print("BglbCompilerUtil.drop()-> classFullName:" + classFullName);
		print("BglbCompilerUtil.drop()-> className:" + className);
		print("BglbCompilerUtil.drop()-> methodName:" + methodName);

		ClassPool pool = ClassPool.getDefault();
		CtClass cc = null;
		CtMethod fMethod = null;
		// 添加路径
		try {
			pool.appendClassPath(targetPath);
			print("BglbCompilerUtil.drop()-> 添加classpath:" + targetPath);
		} catch (NotFoundException e) {
			log.error("BglbCompilerUtil.drop()-> 添加classpath异常：" + e.toString());
		}

		// 获得类
		try {
			cc = pool.getCtClass(classFullName);
			print("BglbCompilerUtil.drop()-> 获取类fullClassName:" + classFullName);

			// 如果方法已存在，则移除
			fMethod = cc.getDeclaredMethod(methodName);
			cc.removeMethod(fMethod);
			print("BglbCompilerUtil.drop()-> :已移除方法：" + fMethod.getName());

		} catch (NotFoundException e) {
			// 没有找到类或方法，不再继续删除操作
			log.error("BglbCompilerUtil.drop()-> 删除方法操作异常:" + e.toString());
			cc = pool.makeClass(classFullName);
			print("BglbCompilerUtil.drop()-> 创建新类fullClassName:" + classFullName);
		}

		try {
			cc.getClassFile().setVersionToJava5();
			cc.writeFile(targetPath);
			cc.detach();
			print("BglbCompilerUtil drop()-> 已更新文件:" + targetPath + "/" + classFullName);

			succ = true;
		} catch (CannotCompileException e) {
			log.error("BglbCompilerUtil.complier -> " + e.toString());
		} catch (IOException e) {
			log.error("BglbCompilerUtil.complier -> " + e.toString());
		}

		return succ;
	}

	public static void print(String str) {
		log.debug(str);
	}

}
