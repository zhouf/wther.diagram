package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.JavaElementInfo;
import org.eclipse.jdt.internal.core.SourceMethod;
import org.eclipse.jdt.internal.core.SourceMethodElementInfo;

/**
 * 这是一个将JAVA方法转换为调用节点参数的类
 * 构造参数为一个SourceMethod对象
 * 解析出调用节点所需要的属性字串
 * @author zhouf
 */
public class ParseSourceMethod {

	private String methodName = "";
	private String className = "";
	private String argStr = "";
	private String retStr = "";
	private boolean isStatic = false;
	private String pkgStr = "";

	@SuppressWarnings("restriction")
	public ParseSourceMethod(SourceMethod method) {
		
		methodName = method.getElementName();
		className = method.getParent().getElementName();
//		Log.debug("method.getParent().getParent().toString()->" + method.getParent().getParent().toString());
//		Log.debug(method.getParent().getParent().toString());

		try {
			
			//获取包名
			JavaElement element = (JavaElement) method.getParent().getParent();
			Object info = element.getElementInfo();
			if (info == null || !(info instanceof JavaElementInfo)) return;
			for (IJavaElement children : ((JavaElementInfo)info).getChildren()) {
				if(children.getElementType()==JavaElement.PACKAGE_DECLARATION){
					pkgStr = children.getElementName();
				}
			}
			
			//解析参数信息
			StringBuffer sb = new StringBuffer();
			for (ILocalVariable lv : method.getParameters()) {
				String varType = Signature.toString(lv.getTypeSignature());
				String varName = lv.getElementName();
				sb.append(varType).append(":").append(varName);
				sb.append(":<NOSET>:变量:<NOSET>:<NOSET>|");
			}
			argStr = sb.toString();
			
			//返回数据
			retStr = Signature.toString(method.getReturnType());
			
			//是否是静态方法
			SourceMethodElementInfo methodInfo = (SourceMethodElementInfo) (method.getElementInfo());
			int flags = methodInfo.getModifiers();
			if (Flags.isStatic(flags)) {
				isStatic = true;
			}
			
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
//		Log.debug("methodName:" + methodName);
//		Log.debug("className:" + className);
//		Log.debug("argStr:" + argStr);
//		Log.debug("retStr:" + retStr);
	}

	public String getMethodName() {
		return methodName;
	}

	public String getClassName() {
		return className;
	}

	public String getArgStr() {
		return argStr;
	}

	public String getRetStr() {
		return retStr;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public String getPkgStr() {
		return pkgStr;
	}
}
