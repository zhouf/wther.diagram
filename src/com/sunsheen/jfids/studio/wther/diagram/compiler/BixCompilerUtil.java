package com.sunsheen.jfids.studio.wther.diagram.compiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.sunsheen.jfids.studio.javaassist.CannotCompileException;
import com.sunsheen.jfids.studio.javaassist.ClassPool;
import com.sunsheen.jfids.studio.javaassist.CtClass;
import com.sunsheen.jfids.studio.javaassist.CtMethod;
import com.sunsheen.jfids.studio.javaassist.NotFoundException;
import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.logging.LogFactory;
import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.logic.util.LogicResourceImpl;
import com.sunsheen.jfids.studio.run.utils.io.FileUtil;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ErrorRecordsUtils;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.GenerateJavaCodeUtil;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaCompiler;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.LogicComplierUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.AssociationUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.BixCheckError;
import com.sunsheen.jfids.studio.wther.diagram.parser.FlowArgParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.FlowRetParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.FlowArgItem;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.FlowRetItem;

public class BixCompilerUtil {
	static final Logger log = LogFactory.getLogger(BixCompilerUtil.class.getName());

	private static String pkgName;
	private static String classAndMethodName;
	private static String classFullName;
	private static String className;
	private static String methodName;
	private static String debugFieldName = "";

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
		// 方法名
		methodName = LogicComplierUtil.covertFolderToClassAndMethodName(sourceFile.getLocation().lastSegment(), false);
		// 类全名
		classFullName = pkgName + "." + className;

		debugFieldName = methodName + "_DebugContext_";

		// JavaComplier.init(sourceFile, targetFolder);
	}

	/**
	 * 按目录的方式编译bix成class文件
	 * 
	 * @param sourceFolder
	 *            bix文件目录
	 * @param targetFolder
	 *            生成class的目标目录
	 * @return boolean
	 */
	public static boolean complierFolder(IFolder sourceFolder, IFolder targetFolder) {
		// FIXME 改成目录方式编译后，优化待完善
		boolean retVal = false;
		//删除之前编译的class
		dropOldClass(sourceFolder,targetFolder);
		
		IFile bglbFile = sourceFolder.getFile(".bglb");
		if (bglbFile != null && bglbFile.exists()) {
			BglbCompilerUtil.complier(bglbFile, targetFolder);
		}else{
			BglbCompilerUtil.setFileEntity(null);
		}

		JavaCompiler compiler = new JavaCompiler();
		String packageName = LogicComplierUtil.getPackageName(sourceFolder.getLocation().removeLastSegments(1).toString(), true);
		String clsName = LogicComplierUtil.covertFolderToClassAndMethodName(sourceFolder.getName(), false);

		compiler.setPkgName(packageName);
		compiler.setClassName(clsName);
		compiler.setCurProject(sourceFolder.getProject());

		compiler.prepareClass();

		try {
			List<String> structList = new ArrayList<String>();
			HashMap<String, FlowImpl> flowMap = new HashMap<String, FlowImpl>();
			HashMap<String, IFile> fileMap = new HashMap<String, IFile>();

			for (IResource res : sourceFolder.members()) {
				if ("bix".equalsIgnoreCase(res.getFileExtension())) {
					IFile bixFile = (IFile) res;
					
					//扫描相关的文件
					AssociationUtil.scanBixAssociation(bixFile);

					// 删除之前的记录，重新检查
					log.info("BixCompilerUtil.complierFolder()->删除之前的记录，重新检查:" + res.toString());
					ErrorRecordsUtils.getInstance().removeMapByFile(bixFile.getFullPath().toString());
					boolean hasError = BixCheckError.checkError(bixFile);
					log.debug("BixCompilerUtil.complierFolder()->bixFile:" + bixFile);
					log.debug("BixCompilerUtil.complierFolder()->hasError:" + hasError);
					if (!hasError) {

						String funcStr = genFuncString(bixFile);
						structList.add(funcStr);

						String funcName = LogicComplierUtil.covertFolderToClassAndMethodName(bixFile.getName(), false);
						flowMap.put(funcName, parserFlowModel(bixFile));
						fileMap.put(funcName, bixFile);
					}
				}
			}// end of for

			String uri = "";
			try {
				compiler.genStructure(structList);
				for (String key : flowMap.keySet()) {
					FlowImpl flow = flowMap.get(key);
					IFile file = fileMap.get(key);
					uri = file.getFullPath().toString();
					String funcName = key;
//					CompilationUnit unit = doComplier(flow, packageName, clsName, funcName);// 运用AST开始编译,产生类文件对象。
					FlowCompileUnit flowUnit = doComplier(flow,key);
					log.debug("BixCompilerUtil.complierFolder()->key:" + key);
					if("run".equals(key)){
						// FIXME 需要考虑参数符合要求
						compiler.appendMethodBody(flowUnit,BglbCompilerUtil.getFileEntity());
					}else{
						compiler.appendMethodBody(flowUnit);
					}
					compiler.addSourceDebugLine(file, flow);

					// 如果编译通过，则移除之前的记录
					ErrorRecordsUtils.getInstance().removeMapByFile(uri);
					log.info("BixCompilerUtil.complierFolder()->:编译通过，则移除之前的记录");
				}
			} catch (CannotCompileException ex) {
				log.debug("BixCompilerUtil.complierFolder()->ex.getRowNum():" + ex.getRowNum());
				ex.printStackTrace();
				if (ex.getRowNum() % 1000 == 1) {
					// 第一行出错，按画布出错处理
					ErrorRecordsUtils.getInstance().appendInfo(uri, 0, ex.getMessage());
				} else {
					ErrorRecordsUtils.getInstance().appendInfo(uri, ex.getRowNum(), ex.getMessage());
				}
			} catch (Exception ex) {
				log.debug("BixCompilerUtil.complierFolder()->ex:" + ex);
				ex.printStackTrace();
				// 全局错误，在画布上显示，0作为画布行号
				ErrorRecordsUtils.getInstance().appendInfo(uri, 0, ex.getMessage());
			}

			// 第二次验证
			for (IResource res : sourceFolder.members()) {
				if ("bix".equalsIgnoreCase(res.getFileExtension())) {
					IFile bixFile = (IFile) res;
					BixCheckError.checkError(bixFile);
				}
			}

			compiler.finishAndSave(sourceFolder, targetFolder);

		} catch (CoreException e) {
			log.error(e.getMessage(), e);
		} catch (NullPointerException e) {
			log.error(e.getMessage(), e);
		}
		return retVal;
	}

	/**
	 * 删除之前生成的class文件
	 * @param sourceFolder
	 * @param targetFolder
	 */
	private static void dropOldClass(IFolder sourceFolder, IFolder targetFolder) {
		String str = StringUtils.substringAfterLast(sourceFolder.getProjectRelativePath().toString(), "/src/");
		str = StringUtils.substringBeforeLast(str, ".bix");
		String classFile = targetFolder.getLocation().toString().concat("/").concat(str).concat(".class");
		FileUtil.deleteFile(classFile);
		log.debug("BixCompilerUtil.dropOldClass()->classFile:" + classFile);
	}

	/**
	 * 根据当前的bix文件，扫描同目录下的bix，如果没有错误，将每个文件其转换为：返回_文件名_参数1_参数2...，并以集合方式返回
	 * 
	 * @param sourceFile
	 *            当前的bix文件
	 * @return List<String> 返回的表示当前方法结构的字串集合
	 */
	public static List<String> scanBixFile(IFile sourceFile) {
		List<String> retList = new ArrayList<String>();
		try {
			IResource res[] = sourceFile.getParent().members();
			for (IResource r : res) {
				if ("bix".equalsIgnoreCase(r.getFileExtension()) && !(r.getName().equalsIgnoreCase(sourceFile.getName()))) {
					boolean hasError = BixCheckError.checkError((IFile) r);
					if (!hasError) {
						String funcStr = genFuncString((IFile) r);
						retList.add(funcStr);
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return retList;
	}

	/**
	 * 将方法字串转换为创建方法的java语句
	 * 
	 * @param str
	 *            如void_funcName_java.lang.String_
	 * @return String 生成方法字串，如：public void funcName(java.lang.String
	 *         arg1){return null;}
	 */
	public static String makeCreateStatement(String str) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotEmpty(str)) {
			sb.append("public");
			String items[] = str.split("_");
			String retType = items[0];
			sb.append(" ").append(retType).append(" ").append(items[1]).append("(");
			for (int i = 2; i < items.length; i++) {
				sb.append(items[i]).append(" arg").append(i - 1).append(",");
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
		}

		log.info("BixCompilerUtil makeCreateStatement()-> str:" + str);
		log.info("BixCompilerUtil makeCreateStatement()-> sb.toString():" + sb.toString());

		return sb.toString();
	}

	/**
	 * 从IFile文件中获取方法表示字串，参数和返回类型用完整类名表示，如java.lang.String
	 * 
	 * @param file
	 *            传入的bix文件
	 * @return String 返回的字串如：返回_文件名_参数1_参数2...
	 */
	private static String genFuncString(IFile file) {
		StringBuilder sb = new StringBuilder();
		FlowImpl flow = parserFlowModel(file);
		if (flow != null) {
			String ret = flow.getRet();
			FlowRetParser flowRetParser = new FlowRetParser(ret);

			int retVarNum = flowRetParser.getItemSet().size();
			if (retVarNum > 1) {
				sb.append("java.util.Map_");
			} else if (retVarNum == 1) {
				String retType = flowRetParser.getItemSet().get(0).getType();
				if (JavaTypeUtil.containsKey(retType)) {
					retType = JavaTypeUtil.convertShortToLongType(retType);
				}
				sb.append(retType).append("_");
			} else {
				sb.append("void_");
			}

			String fileName = file.getName();
			fileName = fileName.substring(0, fileName.length() - 4);
			sb.append(fileName).append("_");

			String args = flow.getArgs();
			FlowArgParser flowArgParser = new FlowArgParser(args);
			for (FlowArgItem argItem : flowArgParser.getItemSet()) {
				String argType = argItem.getType();
				if (JavaTypeUtil.containsKey(argType)) {
					argType = JavaTypeUtil.convertShortToLongType(argType);
				}
				sb.append(argType).append("_");
			}
		}
		log.debug("BixCompilerUtil genFuncString()-> file:" + file);
		log.debug("BixCompilerUtil genFuncString()-> sb:" + sb.toString());
		return sb.toString();
	}

	/**
	 * 将文件转换为模型
	 * 
	 * @param sourceFile
	 *            传入的bix文件
	 * @return FlowImpl 如果转换正确，返回Flow模型
	 */
	public static FlowImpl parserFlowModel(IFile sourceFile) {
		FlowImpl flowImpl = null;
		org.eclipse.emf.ecore.resource.Resource res = new LogicResourceImpl(null);
		try {
			res.load(sourceFile.getContents(), null);
			flowImpl = (FlowImpl) res.getContents().get(0);// 第二个为diagram
		} catch (IOException e) {
			log.error("将IFile转换为Flow模型时出错：" + e.toString());
			e.printStackTrace();
		} catch (CoreException e) {
			log.error("将IFile转换为Flow模型时出错：" + e.toString());
			e.printStackTrace();
		}
		return flowImpl;
	}

	/**
	 * 具体执行编译
	 * @param flowImpl
	 * @param funcName
	 */
	public static FlowCompileUnit doComplier(FlowImpl flowImpl, String funcName) {
		FlowCompileUnit compileUnit = new FlowCompileUnit(flowImpl);
		compileUnit.setFuncName(funcName);
		StringBuilder sb = new StringBuilder();
		StringBuffer returnBuffer = new StringBuffer();
		String flowRetType = "void";

		// --------------------生成赋值语句-----------------------------
		String define = flowImpl.getDefine();
		String flowDefineCode = GenerateJavaCodeUtil.genAssignment(define, "");
		log.debug("BixCompilerUtil.doComplier()->flowDefineCode:" + flowDefineCode);
		sb.append(flowDefineCode);

		// --------------------生成赋值语句-----------------------------

		// --------------------对返回变量先定义-----------------------------
		String retStrs = flowImpl.getRet();
		if (StringUtils.isNotBlank(retStrs)) {
			FlowRetParser flowRetParser = new FlowRetParser(retStrs);
			for (FlowRetItem retItem : flowRetParser.getItemSet()) {
				String eachRetExp = genExpress(retItem);
				log.debug("BixCompilerUtil.doComplier()->eachRetExp:" + eachRetExp);
				sb.append(eachRetExp).append("\n");
			}

			// ------------------------------------------返回语句的生成
			if (flowRetParser.getItemSet().size() > 1) {
				// 如果有多个返回值，封装成Map返回
				flowRetType = "java.util.Map";
				Random random = new Random();// 用于生成随机中间变量
				int randInt = random.nextInt();
				randInt = (randInt < 0 ? 0 - randInt : randInt);
				String retMapVar = new String("_retMap") + randInt;

				for (FlowRetItem retItem : flowRetParser.getItemSet()) {
					if (JavaTypeUtil.isPrimitiveType(retItem.getType())) {
						// 如果是普通类型的变量，需要转换为对象放在Map里
						returnBuffer.append(retMapVar).append(".put(\"").append(retItem.getValName()).append("\",new ");
						returnBuffer.append(JavaTypeUtil.convertPrimitiveToClass(retItem.getType())).append("(").append(retItem.getValName())
								.append("));\n");
					} else {
						returnBuffer.append(retMapVar).append(".put(\"").append(retItem.getValName()).append("\",").append(retItem.getValName())
								.append(");\n");
					}
				}
				returnBuffer.insert(0, "java.util.Map " + retMapVar + " = new java.util.HashMap();\n");
				returnBuffer.append("return " + retMapVar + ";");
			} else {
				FlowRetItem flowRetItem = flowRetParser.getItemSet().get(0);
				flowRetType = flowRetItem.getType();
				returnBuffer.append("return ").append(flowRetItem.getValName()).append(";");
			}
			log.debug("BixCompilerUtil.doComplier()->returnBuffer:" + returnBuffer);

		}
		// --------------------定义返回变量结束-----------------------------

		// --------------------------完成方法声明
		StringBuilder declareStr = new StringBuilder("public " + flowRetType + " " + funcName + "(");
		FlowArgParser flowArgParser = new FlowArgParser(flowImpl.getArgs());
		for (FlowArgItem argItem : flowArgParser.getItemSet()) {
			declareStr.append(argItem.getType() + " " + argItem.getValName() + ",");
		}
		if (declareStr.toString().endsWith(",")) {
			declareStr.deleteCharAt(declareStr.lastIndexOf(","));
		}
		declareStr.append("){\n");
		// --------------------------方法声明结束

		String codeStr = GenerateJavaCodeUtil.genMethodBody(flowImpl);
		log.debug("BixCompilerUtil.doComplier()->codeStr:" + codeStr);
		
		codeStr = sb.insert(0, declareStr).append(codeStr).append(returnBuffer).append("\n}").toString();

		compileUnit.setCode(codeStr);

		return compileUnit;

	}
	
	
	
	/*@SuppressWarnings("unchecked")
	public static CompilationUnit doComplier(FlowImpl flowImpl, String packageName, String clsName, String funcName) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource("".toCharArray());

		CompilationUnit unit = (CompilationUnit) parser.createAST(null);
		unit.recordModifications();
		// AST ast = unit.getAST();
		ASTUtil.getInstance().init();
		// 设置包
		ASTUtil.setPackage(unit, packageName);
		// 导入引用包(初始化包，即基础包)
		// ASTUtil.importPkg(unit, new
		// String[]{"java.lang.String","java.util.Date"});
		// 创建类
		TypeDeclaration type = ASTUtil.createClass(unit, clsName, null);
		// 创建方法
		MethodDeclaration methodDeclaration = ASTUtil.getInstance().createMethod(unit, funcName, flowImpl);

		// 向类中添加方法
		type.bodyDeclarations().add(methodDeclaration);
		// 想文件中添加类
		unit.types().add(type);

		return unit;
	}*/

	/**
	 * 生成返回变量的初始化定义
	 * @param retItem
	 * @return
	 */
	private static String genExpress(FlowRetItem retItem) {
		StringBuffer sb = new StringBuffer();
		
		if (JavaType.STRING.equals(retItem.getType())) {
			sb.append("String ").append(retItem.getValName()).append(" = null;");
		} else if (JavaType.DATE.equals(retItem.getType())) {
			sb.append("java.util.Date ").append(retItem.getValName()).append(" = new java.util.Date();");
		} else if (JavaTypeUtil.isPrimitiveType(retItem.getType())) {
			// 基本数据类型，如果为空，赋初值0
			//处理各种数据类型的初值 short int long float double
			if("short".equalsIgnoreCase(retItem.getType())){
				sb.append(retItem.getType()).append(" ").append(retItem.getValName()).append(" = 0s;");
			}else if("long".equalsIgnoreCase(retItem.getType())){
				sb.append(retItem.getType()).append(" ").append(retItem.getValName()).append(" = 0l;");
			}else if("float".equalsIgnoreCase(retItem.getType())){
				sb.append(retItem.getType()).append(" ").append(retItem.getValName()).append(" = 0.0f;");
			}else if("double".equalsIgnoreCase(retItem.getType())){
				sb.append(retItem.getType()).append(" ").append(retItem.getValName()).append(" = 0.0d;");
			}else{
				sb.append(retItem.getType()).append(" ").append(retItem.getValName()).append(" = 0;");
			}
		} else {
			// TODO 其它类型的数据
			sb.append(retItem.getType()).append(" ").append(retItem.getValName()).append(" = null;");
		}

		return sb.toString();
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

		print("BixCompilerUtil.drop()-> targetPath:" + targetPath);
		print("BixCompilerUtil.drop()-> pkgName:" + pkgName);
		print("BixCompilerUtil.drop()-> classFullName:" + classFullName);
		print("BixCompilerUtil.drop()-> className:" + className);
		print("BixCompilerUtil.drop()-> methodName:" + methodName);

		ClassPool pool = ClassPool.getDefault();
		CtClass cc = null;
		CtMethod fMethod = null;
		// 添加路径
		try {
			pool.appendClassPath(targetPath);
			print("BixCompilerUtil.drop()-> 添加classpath:" + targetPath);
		} catch (NotFoundException e) {
			Log.error("BixCompilerUtil.drop()-> 添加classpath异常：" + e.toString());
		}

		// 获得类
		try {
			cc = pool.getCtClass(classFullName);
			print("BixCompilerUtil.drop()-> 获取类fullClassName:" + classFullName);

			// 如果方法已存在，则移除
			fMethod = cc.getDeclaredMethod(methodName);
			cc.removeMethod(fMethod);
			print("BixCompilerUtil.drop()-> :已移除方法：" + fMethod.getName());

		} catch (NotFoundException e) {
			// 没有找到类或方法，不再继续删除操作
			Log.error("BixCompilerUtil.drop()-> 删除方法操作异常:" + e.toString());
			cc = pool.makeClass(classFullName);
			print("BixCompilerUtil.drop()-> 创建新类fullClassName:" + classFullName);
		}

		try {
			cc.getClassFile().setVersionToJava5();
			cc.writeFile(targetPath);
			cc.detach();
			print("BixCompilerUtil drop()-> 已更新文件:" + targetPath + "/" + classFullName);

			succ = true;
		} catch (CannotCompileException e) {
			Log.error("BixCompilerUtil.complier -> " + e.toString());
		} catch (IOException e) {
			Log.error("BixCompilerUtil.complier -> " + e.toString());
		}

		return succ;
	}

	public static void print(String str) {
		Log.debug(str);
	}
}
