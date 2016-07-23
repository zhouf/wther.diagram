package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.sunsheen.jfids.studio.logging.LogFactory;
import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.wther.diagram.compiler.JavaType;

public class ASTUtil {
	private static final Logger log = LogFactory.getLogger(ASTUtil.class.getName());
	boolean hasReturn = false;// 方法是否有返回值
	boolean moreReturn = false;// 方法是否返回多个值
	private Set<String> importPkgSet = new HashSet<String>();// 存放一个类的包信息
	private static final ASTUtil astUtil = new ASTUtil();

	/**
	 * 当重新切换标签或者文件时，需要清空导入的包。
	 */
	public void init() {
		importPkgSet.clear();
	}

	public static ASTUtil getInstance() {
		return astUtil;
	}

	/**
	 * 设置包
	 * @param unit
	 */
	public static void setPackage(CompilationUnit unit, String pakName) {
		AST ast = unit.getAST();
		PackageDeclaration packageDeclaration = ast.newPackageDeclaration();
		unit.setPackage(packageDeclaration);
		packageDeclaration.setName(ast.newName(getSimpleNames(pakName)));
	}

	/**
	 * 导入包
	 * @param unit
	 * @param IMPORTS
	 */
	@SuppressWarnings("unchecked")
	public void importPkg(CompilationUnit unit, String[] IMPORTS) {
		AST ast = unit.getAST();
		for (int i = 0; i < IMPORTS.length; ++i) {
			if (!importPkgSet.contains(IMPORTS[i])) {
				ImportDeclaration importDeclaration = ast.newImportDeclaration();
				importDeclaration.setName(ast.newName(getSimpleNames(IMPORTS[i])));
				if (IMPORTS[i].indexOf("*") > 0) {
					importDeclaration.setOnDemand(true);
				} else {
					importDeclaration.setOnDemand(false);
				}
				unit.imports().add(importDeclaration);
				importPkgSet.add(IMPORTS[i]);
			}
		}
	}

	/**
	 * 创建类
	 * @param unit
	 * @param className
	 */
	@SuppressWarnings("unchecked")
	public static TypeDeclaration createClass(CompilationUnit unit, String className, String superClassName) {
		AST ast = unit.getAST();

		TypeDeclaration type = ast.newTypeDeclaration();
		type.setInterface(false);
		type.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
		type.setName(ast.newSimpleName(className));
		if (superClassName != null) {
			type.setSuperclassType(ast.newSimpleType(ast.newName(superClassName)));
		}
		return type;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MethodDeclaration createMethod(CompilationUnit unit, String methodName, FlowImpl flowImpl) {
		AST ast = unit.getAST();

		log.debug("ASTUtil.createMethod()->:methodName:" + methodName);

		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		methodDeclaration.setConstructor(false);
		List modifiers = methodDeclaration.modifiers();
		modifiers.add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
		//modifiers.add(ast.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
		methodDeclaration.setName(ast.newSimpleName(methodName));

		//methodDeclaration.setJavadoc(jc);

		// ----------参数配置---------------
		String args = flowImpl.getArgs();
		// 默认参数的处理，设置为两个制表符
		if (args != null && args.length() > 0) {
			for (String eachArg : args.split("\\|")) {
				String argItem[] = eachArg.split(":");
				if (argItem.length >= 3) {
					String type = argItem[0];
					String name = argItem[1];
					String isArray = argItem[2];

					importPkg(unit, type);// 导入包

					// 参数
					SingleVariableDeclaration variableDeclaration = ast.newSingleVariableDeclaration();
					// FIXME 对普通类型的处理，如int,boolean  
					// p1.setType(ast.newPrimitiveType(PrimitiveType.INT));
					Type newSimpleType = parseType(type,ast);
					//SimpleType newSimpleType = ast.newSimpleType(ast.newName(getSimpleNames(type)));
					if ("true".equals(isArray)) {
						variableDeclaration.setType(ast.newArrayType(newSimpleType));
					} else {
						variableDeclaration.setType(newSimpleType);
					}
					variableDeclaration.setName(ast.newSimpleName(name));
					methodDeclaration.parameters().add(variableDeclaration);

				} else {
					log.error("参数格式不正确，无法解析，请检查eachArg:" + eachArg);
				}
			}

		}
		// -------------------------

		// ------------------返回值配置---------------------
		String ret = flowImpl.getRet();
		if (StringUtils.isNotBlank(ret)) {
			hasReturn = true;
			String rets[] = ret.trim().split("\\|");
			if(rets.length>1){
				moreReturn = true;
				// 返回参数为多个是，返回Map对象
				importPkg(unit, "java.util.Map");
				importPkg(unit, "java.util.HashMap");
				methodDeclaration.setReturnType2(ast.newSimpleType(ast.newName("Map")));
			}else{
				moreReturn = false;
				String retType = ret.substring(0,ret.indexOf(":"));
				
				if("ArrayList".equals(retType)){
					importPkg(unit, "java.util.ArrayList");
				}else if("Date".equals(retType)){
					importPkg(unit, "java.util.Date");
				}
				
				Type returnType = parseType(retType,ast);
				methodDeclaration.setReturnType2(returnType);
			}
		} else {
			hasReturn = false;
			// 返回值
			methodDeclaration.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
		}
		// ------------------返回值配置---------------------

		// -------------方法体添加------------------
		createMethodBody(unit, methodDeclaration, flowImpl);
		// --------------------------------------

		return methodDeclaration;
	}

	/**
	 * 将传入的类型转换为AST对应的type
	 * @param type
	 * @return Type
	 */
	private Type parseType(String type,AST ast) {
		Type retType = null;
		if(JavaTypeUtil.isPrimitiveType(type)){
			//对int等普通类型的处理
			// p1.setType(ast.newPrimitiveType(PrimitiveType.INT));
			if("char".equals(type)){
				retType = ast.newPrimitiveType(PrimitiveType.CHAR);
			}else if("boolean".equals(type)){
				retType = ast.newPrimitiveType(PrimitiveType.BOOLEAN);
			}else if("short".equals(type)){
				retType = ast.newPrimitiveType(PrimitiveType.SHORT);
			}else if("int".equals(type)){
				retType = ast.newPrimitiveType(PrimitiveType.INT);
			}else if("long".equals(type)){
				retType = ast.newPrimitiveType(PrimitiveType.LONG);
			}else if("float".equals(type)){
				retType = ast.newPrimitiveType(PrimitiveType.FLOAT);
			}else if("double".equals(type)){
				retType = ast.newPrimitiveType(PrimitiveType.DOUBLE);
			}
			
		}else{
			retType = ast.newSimpleType(ast.newName(getSimpleNames(type)));
		}
		return retType;
	}

	// ------------------------以下为私有的方法----------------------------------
	/**
	 * 创建方法体
	 * @param ast
	 * @param methodDeclaration
	 * @param flowImpl
	 */
	@SuppressWarnings("unchecked")
	private void createMethodBody(CompilationUnit unit, MethodDeclaration methodDeclaration, FlowImpl flowImpl) {
		AST ast = unit.getAST();
		org.eclipse.jdt.core.dom.Block block = ast.newBlock();

		log.debug("ASTUtil.createMethodBody()->methodDeclaration.getName().toString():" + methodDeclaration.getName().toString());

		// --------------------生成赋值语句-----------------------------
		String define = flowImpl.getDefine();
		if (StringUtils.isNotBlank(define)) {
			ASTParser cdefine = ASTParser.newParser(AST.JLS4);
			cdefine.setKind(ASTParser.K_STATEMENTS);

			char[] charArray = "".toCharArray();
//			charArray = GenerateJavaCodeUtil.genAssignment(unit, define, "").toCharArray();
			log.debug("ASTUtil.createMethodBody()->charArray:" + new String(charArray));
			cdefine.setSource(charArray);
			List<Statement> defineAstNodes = ASTNode.copySubtrees(ast, ((Block) cdefine.createAST(null)).statements());

			block.statements().addAll(defineAstNodes);
		}
		// --------------------生成赋值语句-----------------------------


		// --------------------对返回变量先定义-----------------------------
		String retStrs = flowImpl.getRet();
		String rets[] = null;
		if (StringUtils.isNotBlank(retStrs)) {
			rets = retStrs.split("\\|");
			// 有返回值的情况.返回值变量定义
			if (hasReturn) {
				StringBuffer sb = new StringBuffer("");
				for (String ret : rets) {
					String eachExp = genExpress(ret);
					sb.append(eachExp);
				}
				ASTParser c = ASTParser.newParser(AST.JLS4);
				c.setKind(ASTParser.K_STATEMENTS);
				c.setSource(sb.toString().toCharArray());
				// FIXME copySubtrees 是什么意思
				List<Statement> astNodes = ASTNode.copySubtrees(ast, ((Block) c.createAST(null)).statements());
				
				block.statements().addAll(astNodes);
			}
		}
		// --------------------对返回变量的处理-----------------------------
		
		
		// ------------具体方法体------------------------------
		ASTParser c1 = ASTParser.newParser(AST.JLS4);
		c1.setKind(ASTParser.K_STATEMENTS);
		String genMethodBody = GenerateJavaCodeUtil.genMethodBody(flowImpl);
		System.out.println("ASTUtil createMethodBody()-> genMethodBody:" + genMethodBody);
		
		c1.setSource(genMethodBody.toCharArray());
		
		// FIXME 编译的异常不能捕获...
		try{
			ASTNode c1Note = c1.createAST(null);
			List<Statement> astNodes1 = ASTNode.copySubtrees(ast, ((Block) c1Note).statements());

			block.statements().addAll(astNodes1);
		}catch(IllegalArgumentException ex){
			System.err.println("ASTUtil.createMethodBody-> genMethodBody:" + genMethodBody);
			System.err.println("ASTUtil.createMethodBody-> 将方法体放入AST出现异常:" + ex);
		}
		// ------------具体方法体------------------------------

		// 有返回值的情况。返回值赋值
		if (hasReturn && rets != null) {

			ASTParser c = ASTParser.newParser(AST.JLS4);
			c.setKind(ASTParser.K_STATEMENTS);
			if(rets.length>1){
				//如果是多个返回值，则返回Map
				// 为了防止命名重复。这儿采用随机数
				Random random = new Random();// 用于生成随机中间变量
				int randInt = random.nextInt();
				randInt = (randInt < 0 ? 0 - randInt : randInt);
				String retMapVar = new String("_retMap") + randInt;
				StringBuffer sb = new StringBuffer();
				for(String retStr : rets){
					//numbers.put("one", new Integer(1));
					String item[] = ParamUtil.praseStrToArray(retStr, 5);
					if(JavaTypeUtil.isPrimitiveType(item[0])){
						//如果是普通类型的变量，需要转换为对象放在Map里
						sb.append(retMapVar).append(".put(\"").append(item[1]).append("\",new ");
						sb.append(JavaTypeUtil.convertPrimitiveToClass(item[0])).append("(").append(item[1]).append("));");
					}else{
						sb.append(retMapVar).append(".put(\"").append(item[1]).append("\",").append(item[1]).append(");");
					}
				}
				
				c.setSource(("java.util.Map " + retMapVar + " = new java.util.HashMap();" + sb.toString() + "return " + retMapVar + ";").toCharArray());
			}else{
				//single return String:ret1:false:<NOSET>
				String item[] = ParamUtil.praseStrToArray(retStrs, 4);
				c.setSource(("return " + item[1] + ";").toCharArray());
			}
			// ASTNode astNode = ASTNode.copySubtrees(ast, c.createAST(null)));
			List<Statement> astNodes = ASTNode.copySubtrees(ast, ((Block) c.createAST(null)).statements());

			block.statements().addAll(astNodes);

		}

		// 向方法中添加方法体
		methodDeclaration.setBody(block);
	}

	/**
	 * 生成返回变量的初始化定义
	 * @param ret 
	 *            如：String:name1:是否数组:描述A|com.sun.jndi.ldap.Connection:ret1:数组:描述
	 *            |long:ret2:数组:<NOSET>
	 * @return String name1;
	 */
	private static String genExpress(String ret) {
		// TODO 删除不需要的代码
		StringBuffer sb = new StringBuffer("//[node-line]=3\n");
		if (ret != null && ret.length() > 0) {
			String item[] = com.sunsheen.jfids.studio.wther.diagram.compiler.util.ParamUtil.praseStrToArray(ret, 4);
			if (JavaType.STRING.equals(item[0])) {
				sb.append("String ").append(item[1]).append(" = null;");
			} else if (JavaType.DATE.equals(item[0])) {
				sb.append("java.util.Date ").append(item[1]).append(" = new java.util.Date();");
			} else if (JavaTypeUtil.isPrimitiveType(item[0])) {
				// 基本数据类型，如果为空，赋初值0
				//处理各种数据类型的初值 short int long float double
				if("short".equalsIgnoreCase(item[0])){
					sb.append(item[0]).append(" ").append(item[1]).append(" = 0s;");
				}else if("long".equalsIgnoreCase(item[0])){
					sb.append(item[0]).append(" ").append(item[1]).append(" = 0l;");
				}else if("float".equalsIgnoreCase(item[0])){
					sb.append(item[0]).append(" ").append(item[1]).append(" = 0.0f;");
				}else if("double".equalsIgnoreCase(item[0])){
					sb.append(item[0]).append(" ").append(item[1]).append(" = 0.0d;");
				}else{
					sb.append(item[0]).append(" ").append(item[1]).append(" = 0;");
				}
			} else {
				// TODO 其它类型的数据
				sb.append(item[0]).append(" ").append(item[1]).append(" = null;");
			}

		}
		return sb.toString();
	}

	// private static List<String> imports = JavaType.toArray();

	/**
	 * 根据不同的类型导入相应的包。
	 * @param unit
	 * @param type
	 */
	public static void importPkg(CompilationUnit unit, String type) {
		if (!JavaTypeUtil.isPrimitiveType(type)) {
			// 如果不是基本类型
			String fullType = "";
			if (JavaTypeUtil.containsKey(type)) {
				fullType = JavaTypeUtil.convertShortToLongType(type);
			} else {
				fullType = type;
			}

			getInstance().importPkg(unit, new String[] { fullType });
		}

		// Boolean isHad = false;
		// if ("ArrayList".equals(type)) {
		// getInstance().importPkg(unit, new String[] { "java.util.ArrayList"
		// });
		// return;
		// }
		// if ("Date".equals(type)) {
		// getInstance().importPkg(unit, new String[] { "java.util.Date" });
		// return;
		// }
		// if ("HashMap".equals(type)) {
		// getInstance().importPkg(unit, new String[] { "java.util.HashMap" });
		// return;
		// }
		// for (String imporStr : imports) {
		// if (imporStr.equals(type)) {
		// isHad = true;
		// break;
		// }
		//
		// }
		// if (!isHad) {
		// getInstance().importPkg(unit, new String[] { type });
		// // imports.add(type);
		// }
	}

	private static String[] getSimpleNames(String qualifiedName) {
		StringTokenizer st = new StringTokenizer(qualifiedName, ".");
		ArrayList<String> list = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String name = st.nextToken().trim();
			if (!name.equals("*")) {
				list.add(name);
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

}
