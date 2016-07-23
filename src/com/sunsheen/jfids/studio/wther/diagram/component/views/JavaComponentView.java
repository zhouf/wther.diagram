package com.sunsheen.jfids.studio.wther.diagram.component.views;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.sunsheen.jfids.studio.javaassist.ClassPool;
import com.sunsheen.jfids.studio.javaassist.CtClass;
import com.sunsheen.jfids.studio.javaassist.CtMethod;
import com.sunsheen.jfids.studio.javaassist.NotFoundException;
import com.sunsheen.jfids.studio.javaassist.bytecode.AnnotationsAttribute;
import com.sunsheen.jfids.studio.javaassist.bytecode.annotation.Annotation;
import com.sunsheen.jfids.studio.javaassist.bytecode.annotation.AnnotationMemberValue;
import com.sunsheen.jfids.studio.javaassist.bytecode.annotation.ArrayMemberValue;
import com.sunsheen.jfids.studio.javaassist.bytecode.annotation.MemberValue;
import com.sunsheen.jfids.studio.javaassist.bytecode.annotation.StringMemberValue;
import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.run.utils.ProjectUtils;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.Convert;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.CustomConfigFileUtil;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaCompiler;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.Constant;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FilePathManager;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FileScanUtil;
import com.sunsheen.jfids.studio.wther.diagram.parser.CallArgParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.CallArgItem;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicImageUtil;


/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class JavaComponentView extends ViewPart {
	
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(JavaComponentView.class.getName());

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.sunsheen.jfids.studio.wther.diagram.compiler.views.JavaComponentView";
 
	private TreeViewer viewer;
	private Text searchText;
	private Action actionSwitchCompView;
	private Action actionSwitchVarView;
	private Action actionSwitchPrjCompView;
	private Action actionDropCustItem;
	private Action actionReloadTree;
	private Action doubleClickAction;
	private IProject currentProject;
	DirTree libCompTree;
	DirTree varTree;
	DirTree prjCompTree;

	@SuppressWarnings("rawtypes")
	Map<String,Class> variableMap = new HashMap<String,Class>();
	Image dirImg,compImg,varDirImg,varItemImg,prjCompDirImg,prjCompItemImg;
	
	enum DisplayType{STAND_COMPONENT,VAR_TYPE,PRJ_COMPONENT};
	DisplayType displayType = DisplayType.STAND_COMPONENT; 

	int operations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;;
	Transfer[] transfer = new Transfer[] { LocalSelectionTransfer.getTransfer() };

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	public IProject getCurrentProject() {
		return currentProject;
	}
	public void setCurrentProject(IProject currentProject) {
		this.currentProject = currentProject;
		loadProjectComponent();
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Class> getVariableMap() {
		return variableMap;
	}

	@SuppressWarnings("rawtypes")
	public void setVariableMap(Map<String, Class> variableMap) {
		this.variableMap = variableMap;
	}

	class TreeContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ITree) {
				return ((ITree) inputElement).getChildren().toArray();
			}
			return new Object[0];
		}

		@SuppressWarnings("rawtypes")
		public Object[] getChildren(Object parentElement) {
			ITree node = (ITree) parentElement;
			List list = node.getChildren();
			if (list == null) {
				return new Object[0];
			}
			return list.toArray();
		}

		@SuppressWarnings("rawtypes")
		public boolean hasChildren(Object element) {
			ITree node = (ITree) element;
			List list = node.getChildren();
			return !(list == null || list.isEmpty());
		}

		public Object getParent(Object element) {
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}
	}

	//构件的label
	class TreeLabelProvider extends LabelProvider implements ILabelProvider {
        public String getText(Object element) {
        	String retStr = "";
        	if (element instanceof DirTree) {
        		ITree node = (ITree)element;
        		retStr = node.getName();
        	}else{
        		JavaComponentTreeNode obj = (JavaComponentTreeNode)element;
        		String paramStr = obj.getParamStr().replaceAll("　", "");
        		String objName = obj.getName();
        		if(objName.contains(")") && paramStr.length()==0){
        			retStr = obj.getName() + ":" + convertRetType(obj.getRetType());
        		}else{
        			retStr = obj.getName() + "(" + paramStr + "):" + convertRetType(obj.getRetType());
        		}
        	}
        	return retStr;
        }
        
        /**
         * 处理返回，将返回数据类型处理为构件库中的叶子节点显示名
         * @param retStr 节点返回的参数配置，如：String:<NOSET>:<NOSET>:第一个参数说明
         * @return String 如果有返回，则返回Map,若没有，则返回void
         */
        String convertRetType(String retStr) {
        	if(StringUtils.isNotBlank(retStr)){
        		//return "Map";
        		int index = retStr.indexOf(":");
        		if(index>0){
        			return retStr.substring(0,index);
        		}else{
        			return retStr;
        		}
        	}else{
        		return "void";
        	}
        }
        
        public Image getImage(Object element) {
        	if(element instanceof DirTree){
        		return dirImg;
        	}else{
        		return compImg;
        	}
        }
    }

	//变量的label
	class VarTreeLabelProvider extends TreeLabelProvider{
		public String getText(Object element) {
        	String retStr = "";
        	if (element instanceof DirTree) {
        		ITree node = (ITree)element;
        		retStr = node.getName();
        	}else{
        		JavaComponentTreeNode obj = (JavaComponentTreeNode)element;
        		String paramStr = obj.getParamStr().replaceAll("　", "");
        		
        		//从注释里取，里面包含方法名和参数信息，其值如name3.format(String,Object[]):
        		String comment = obj.getComment();
        		String funcName = comment.substring(comment.indexOf(".")+1,comment.indexOf(":"));
        		if(funcName.contains(")") && paramStr.length()==0){
        			retStr = funcName + ":" + convertRetType(obj.getRetType());
        		}else{
        			retStr = funcName + "(" + paramStr + "):" + convertRetType(obj.getRetType());
        		}
        	}
        	return retStr;
		}
		
		public Image getImage(Object element) {
			if(element instanceof DirTree){
				return varDirImg;
			}else{
				return varItemImg;
			}
		}
		
	}
	
	//工程相关构件的label
	class ProjectComponentLabelProvider extends TreeLabelProvider{
		public String getText(Object element) {
        	String retStr = "";
        	if (element instanceof DirTree) {
        		ITree node = (ITree)element;
        		retStr = node.getName();
        	}else{
        		JavaComponentTreeNode obj = (JavaComponentTreeNode)element;
        		retStr = obj.getName();
        	}
        	return retStr;
		}
		
		public Image getImage(Object element) {
			if(element instanceof DirTree){
				return prjCompDirImg;
			}else{
				return prjCompItemImg;
			}
		}
		
	}

	class NameSorter extends ViewerSorter {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			String s1 = ((ITree) e1).getName();
			String s2 = ((ITree) e2).getName();
			s1 = (s1==null? "" : s1);
			s2 = (s2==null? "" : s2);
			return s1.compareTo(s2);
		}
	}

	/**
	 * The constructor.
	 */
	public JavaComponentView() {

		//初始化变量树
		initVariableTree();
		
		libCompTree = new DirTree();
		dirImg = LogicImageUtil.getInstance().getImage("icons/compsView.png");
		compImg = LogicImageUtil.getInstance().getImage("icons/plugin.png");
		varDirImg = LogicImageUtil.getInstance().getImage("icons/varDirImg.png");
		varItemImg = LogicImageUtil.getInstance().getImage("icons/varItemImg.png");
		prjCompDirImg = LogicImageUtil.getInstance().getImage("icons/dir.png");
		prjCompItemImg = LogicImageUtil.getInstance().getImage("icons/prjComponentItem.png");

		loadData();
		
		//加载工程相关构件
		loadProjectComponent();
	}

	private void loadProjectComponent() {

		prjCompTree = new DirTree();
		
		if(currentProject!=null){
			Map<String,List<CtClass>> tmpMap = new HashMap<String,List<CtClass>>();
			
			String resourcePath = currentProject.getFolder("resources").getLocation().toString();
//			log.debug("JavaComponentView.loadProjectComponent()->resourcePath:" + resourcePath);
			List<String> javaFileList = FileScanUtil.getListFiles(resourcePath, "java", true);
			//查找相关库工程
			for(IProject prj2 : ProjectUtils.listRefrenceLibProjects(currentProject)){
				resourcePath =  prj2.getFolder("resources").getLocation().toString();
				javaFileList.addAll(FileScanUtil.getListFiles(resourcePath, "java", true));
			}
			
			List<String> javaClassList = FileScanUtil.convertFilePathToPackagePath(javaFileList);
			ClassPool pool = null;
			try {
				if(javaClassList.size()>0){
					pool = ClassPool.getDefault();
					List<String> poolPaths = JavaCompiler.findPoolPath(currentProject);
					for (String addPath : poolPaths) {
						pool.appendClassPath(addPath);
					}
				}
				
				for(String fullClassName : javaClassList){
					CtClass cc = pool.getCtClass(fullClassName);
					CtClass superClass = cc.getSuperclass();
					
					if(superClass!=null && "com.sunsheen.jfids.system.bizass.core.ABaseComponent".equals(superClass.getName())){
//						log.debug("JavaComponentView.loadProjectComponent()->cc.getName():" + cc.getName());
//						log.debug("JavaComponentView.loadProjectComponent()->superClass.getName():" + superClass.getName());
						String pkgName = cc.getPackageName();
						if(tmpMap.containsKey(pkgName)){
							List<CtClass> classList = tmpMap.get(pkgName);
							classList.add(cc);
						}else{
							List<CtClass> classList = new ArrayList<CtClass>();
							classList.add(cc);
							tmpMap.put(cc.getPackageName(), classList);
						}
					}
				}
				
			} catch (NotFoundException e) {
				log.error("添加classpath异常：" + e.toString());
			}
			
			//将Map中的数据放入到tree中
			for(String key : tmpMap.keySet()){
				DirTree dir = new DirTree();
				dir.setName(key);
//				log.debug("JavaComponentView.loadProjectComponent()->key:" + key);
				for (CtClass cc : tmpMap.get(key)) {
//					log.debug("JavaComponentView.loadProjectComponent()->cc.getName():" + cc.getName());
					JavaComponentTreeNode node = new JavaComponentTreeNode();
					node.setName(cc.getSimpleName());
					node.setDisptip(cc.getName());
					node.setFuncName("LogicComponent.execute");
					node.setExternal(cc.getName());
					node.setRetType("java.lang.Object:ret1:<NOSET>:<NOSET>:<NOSET>:<NOSET>");
					node.setArgs("java.util.Map:param:<NOSET>:<NOSET>:<NOSET>:<NOSET>");
					node.setComment("");
					node.setTip("No Tip set ...");
					
					try {
						CtMethod method = cc.getDeclaredMethod("run");
						if(method!=null){
//							log.debug("JavaComponentView.loadProjectComponent()->method:" + method.getName());
							
							AnnotationsAttribute atrr = (AnnotationsAttribute)method.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
							if(atrr!=null){
								Annotation componentAnnotation = atrr.getAnnotation("com.sunsheen.jfids.system.bizass.annotation.Component");
								if(componentAnnotation!=null){
									node.setName(((StringMemberValue) componentAnnotation.getMemberValue("name")).getValue());
									node.setTip(((StringMemberValue) componentAnnotation.getMemberValue("memo")).getValue());
								}

								//参数的处理
								CallArgParser callArgParser = new CallArgParser();
								Annotation paramsAnnotation = atrr.getAnnotation("com.sunsheen.jfids.system.bizass.annotation.Params");
								if(paramsAnnotation!=null){
									ArrayMemberValue arrayMemberValue = (ArrayMemberValue) paramsAnnotation.getMemberValue("value");
									for(MemberValue v : arrayMemberValue.getValue()){
										if(v instanceof AnnotationMemberValue){
											CallArgItem argItem = new CallArgItem();
											AnnotationMemberValue av = (AnnotationMemberValue)v;
											argItem.setVarType(((StringMemberValue)av.getValue().getMemberValue("type")).getValue());
											argItem.setVarName(((StringMemberValue)av.getValue().getMemberValue("name")).getValue());
											argItem.setComment(((StringMemberValue)av.getValue().getMemberValue("comment")).getValue());
											
											callArgParser.addItem(argItem);
										}
									}
									node.setArgs(callArgParser.toString());
								}


								//返回的处理
								CallArgParser retArgParser = new CallArgParser();
								Annotation returnsAnnotation = atrr.getAnnotation("com.sunsheen.jfids.system.bizass.annotation.Returns");
								if(returnsAnnotation!=null){
									ArrayMemberValue arrayMemberValue2 = (ArrayMemberValue) returnsAnnotation.getMemberValue("retValue");
									for(MemberValue v : arrayMemberValue2.getValue()){
										if(v instanceof AnnotationMemberValue){
											CallArgItem retItem = new CallArgItem();
											AnnotationMemberValue av = (AnnotationMemberValue)v;
											retItem.setVarType(((StringMemberValue)av.getValue().getMemberValue("type")).getValue());
											retItem.setVarName(((StringMemberValue)av.getValue().getMemberValue("name")).getValue());
											retItem.setComment(((StringMemberValue)av.getValue().getMemberValue("comment")).getValue());
											
											retArgParser.addItem(retItem);
										}
									}
									node.setRetType(retArgParser.toString());
								}
								
								//Example中的内容放入说明，FIXME 会存在修改刷新的问题
								StringBuilder sb = new StringBuilder();
								Annotation exampleAnnotation = atrr.getAnnotation("com.sunsheen.jfids.system.bizass.annotation.Example");
								if(exampleAnnotation!=null){
									ArrayMemberValue arrayMemberValue3 = (ArrayMemberValue) exampleAnnotation.getMemberValue("exeampleValue");
									for(MemberValue v : arrayMemberValue3.getValue()){
										if(v instanceof AnnotationMemberValue){
											AnnotationMemberValue av = (AnnotationMemberValue)v;
											sb.append(((StringMemberValue)av.getValue().getMemberValue("exeCom")).getValue());
											sb.append("\n");
										}
									}
									node.setComment(sb.toString());
								}
								
							}
							
						}
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
					
					
					
					dir.addChildren(node);
				}
				prjCompTree.addChildren(dir);
			}
			
		}else{
			log.debug("JavaComponentView.loadProjectComponent()->: currentProject=" + currentProject);
		}
		
	}

	/**
	 * 初始化变量树
	 */
	private void initVariableTree() {
		varTree = new DirTree();
		//根据variableMap里的内容初始化变量树
		Set<String> keys = variableMap.keySet();
		for(String key : keys){
			if(key==null || key.length()==0){
				continue;
			}
			
			DirTree dir = new DirTree();
			
			Class<?> className = variableMap.get(key);
			dir.setName(key + " : " + className.getSimpleName());
			if(Constant.IDATAPORT_STR.equals(className.getName())){
				//IDataPort类型
				for(Method method : className.getDeclaredMethods()){
					if((method.getModifiers()&Modifier.PUBLIC)>0){
						//只加载公有方法
						JavaComponentTreeNode treeNode = new JavaComponentTreeNode();
						String methodName = method.getName();
						treeNode.setName(key + "." + methodName);
						treeNode.setDisptip("DATAPORT." + key.replace("this.", "") );
						treeNode.setFuncName(key + "." + methodName);
						treeNode.setExternal(Constant.IDATAPORT_STR);
						treeNode.setRetType("");
						//ret=type:name:<NOSET>:<NOSET>:comment|more...
						treeNode.setTip(method.toGenericString());
						//设置参数
						StringBuffer dispArg = new StringBuffer();
						for(Class<?> param : method.getParameterTypes()){
							dispArg.append(param.getSimpleName()).append(",");
						}
						if(dispArg.toString().length()>0){
							dispArg.deleteCharAt(dispArg.lastIndexOf(","));
						}
						treeNode.setArgs("");
						//treeNode.setName(methodName + "(" + dispArg + ")");
						treeNode.setComment(key + "." + methodName + "(" + dispArg + "):");
						
						dir.addChildren(treeNode);
					}
				}
			}else{
				
				for(Method method : className.getDeclaredMethods()){
					if((method.getModifiers()&Modifier.PUBLIC)>0){
						//只加载公有方法
						JavaComponentTreeNode treeNode = new JavaComponentTreeNode();
						String methodName = method.getName();
						treeNode.setName(key + "." + methodName);
						treeNode.setDisptip(key + "." + methodName);
						treeNode.setFuncName(key + "." + methodName);
						treeNode.setExternal(methodName + "()");
						String retSimpleName = method.getReturnType().getSimpleName();
						if(!"void".equalsIgnoreCase(retSimpleName)){
							treeNode.setRetType(retSimpleName + ":<NOSET>:<NOSET>:表达式:<NOSET>");
						}
						//ret=type:name:<NOSET>:<NOSET>:comment|more...
						treeNode.setTip(method.toGenericString());
						//设置参数
						StringBuffer argStr = new StringBuffer();
						StringBuffer dispArg = new StringBuffer();
						for(Class<?> param : method.getParameterTypes()){
							argStr.append(param.getName()).append(":<NOSET>:<NOSET>:表达式:<NOSET>:<NOSET>|");
							dispArg.append(param.getSimpleName()).append(",");
						}
						if(argStr.toString().length()>0){
							argStr.deleteCharAt(argStr.lastIndexOf("|"));
						}
						if(dispArg.toString().length()>0){
							dispArg.deleteCharAt(dispArg.lastIndexOf(","));
						}
						treeNode.setArgs(argStr.toString());
						//treeNode.setName(methodName + "(" + dispArg + ")");
						treeNode.setComment(key + "." + methodName + "(" + dispArg + "):");
						
						dir.addChildren(treeNode);
					}
				}
			}
				
			
			varTree.addChildren(dir);
			//actionSwitchCompView.setEnabled(true);
		}
		
	}

	/**
	 * 从配置文件中加载库
	 */
	private void loadData() {
		File baseJsDir = new File(FilePathManager.getJavaComponentPath());
		if(baseJsDir.isDirectory()){
			File files[] = baseJsDir.listFiles();
			for(File f : files){
				Set<DirTree> fileTrees = null;
				if(f.getName().endsWith(".xml")){
					log.debug("JavaComponentView.loadData()->load from file:" + f.getName());
					fileTrees = getTreesFromFile(f);
				}
				if(fileTrees!=null){
					for(DirTree fileTree : fileTrees){
						log.debug("append tree:" + fileTree);
						libCompTree.addChildren(fileTree);
					}
				}
			}
		}
		
		//从jar文件中获取目录结构，从WEB-INF/lib目录中的jar文件里加载目录结构
		List<String> configContents = Scanner.findResourceContent(".*\\.lib.xml");
		for(String content : configContents){
			Set<DirTree> fileTrees = null;
			if(StringUtils.isNotEmpty(content)){
				fileTrees = getTreesFromString(content);
			}
			if(fileTrees!=null){
				for(DirTree fileTree : fileTrees){
					libCompTree.addChildren(fileTree);
				}
			}
		}
		
	}

	private Set<DirTree> getTreesFromFile(File f) {
		Set<DirTree> theTree = new HashSet<DirTree>();
		SAXReader compXmlReader = new SAXReader();
		Element compRoot;
		try {
			compRoot = compXmlReader.read(f).getRootElement();
			@SuppressWarnings("unchecked")
			Iterator<Object> iterator = compRoot.elementIterator();
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				if ("dir".equalsIgnoreCase(element.getName())) {
					theTree.add((DirTree) praseElement(element));
				}
			}
		} catch (DocumentException e) {
			Log.error("JavaComponentView getTreesFromFile()-> File"+f.getName()+"parse exception :" + e.toString());
		} 
		return theTree;
	}
	
	/**
	 * 从文本串中解析DirTree
	 * @param content
	 * @return
	 */
	private Set<DirTree> getTreesFromString(String content) {
		Set<DirTree> theTree = new HashSet<DirTree>();
		SAXReader compXmlReader = new SAXReader();
		Element compRoot;
		try {
			compRoot = compXmlReader.read(new ByteArrayInputStream(content.getBytes("UTF-8"))).getRootElement();
			@SuppressWarnings("unchecked")
			Iterator<Object> iterator = compRoot.elementIterator();
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				if ("dir".equalsIgnoreCase(element.getName())) {
					theTree.add((DirTree) praseElement(element));
				}
			}
		} catch (DocumentException e) {
			Log.error("JavaComponentView getTreesFromString()-> parse exception:" + e.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return theTree;
	}
	
	
	/**
	 * 提供一个添加了自定义库后的一个刷新操作
	 */
	public void refreshAfterAddItem(){
		libCompTree = new DirTree();//重新初始化
		loadData();
		viewer.setInput(libCompTree);
		viewer.refresh(true);
	}
	
	/**
	 * 重新设置变量，供外部调用刷新
	 * @param varMap
	 */
	@SuppressWarnings("rawtypes")
	public void resetVariableTree(Map<String,Class> varMap){
		if(varMap==null){
			varTree = new DirTree();
		}else{
			this.setVariableMap(varMap);
			this.initVariableTree();
		}
		//actionSwitchVarView.setEnabled(true);
		if(this.displayType==DisplayType.VAR_TYPE){
			//如果当前显示状态是变量显示，则刷新
			viewer.setInput(varTree);
			viewer.refresh(true);
		}
	}
	
	@SuppressWarnings("unchecked")
	private ITree praseElement(Element e){
		DirTree tree = new DirTree();
		tree.setName(e.attributeValue("name"));
		Iterator<Object> iterator = e.elementIterator();

		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			if ("dir".equalsIgnoreCase(element.getName())) {
				tree.addChildren(praseElement(element));
			} else {
				// 将XML中的组件配置信息转换为相应对象
				JavaComponentTreeNode compNode = new JavaComponentTreeNode();

				String attName = element.attributeValue("name");
				String attCode = element.attributeValue("code");
				String className = element.attributeValue("classname");
				String retType = element.attributeValue("rettype");
				String ext = element.attributeValue("ext");

				attName = (attName == null ? "<noname>" : attName.trim());
				attCode = (attCode == null ? attName : attCode.trim());
				className = (className == null ? "" : className.trim());
				retType = (retType == null ? "" : retType.trim());
				ext = (ext==null? attCode+"()" : ext.trim());
				
				if(StringUtils.isNotEmpty(className)){
					// 如果有className
					compNode.setName(attName);
					compNode.setFuncName(Constant.LOGIC_COMPONENT_EXECUTE);
					compNode.setExternal(className);
					compNode.setRetType(retType);
				}else{
					compNode.setName(attName);
					compNode.setFuncName(attCode);
					compNode.setExternal(ext);
					compNode.setRetType(retType);
					//compNode.setDisptip(attCode);
				}
				
				//获得父目录的名字，组合为DisplayTip
				compNode.setDisptip(element.getParent().attributeValue("name") + "." + attName);


				// 设置节点TIP
				String tip = element.elementText("memo");
				compNode.setComment(tip);
				if(tip==null){
					compNode.setTip("NO tip set:\n" + attCode);
				}else{
					compNode.setTip(tip);
				}
				
				// 处理参数
				Element paramstr = element.element("paramstr");
				if(paramstr==null){
					Iterator<Object> params = element.elementIterator("param");
					if (params != null) {
						int varIndex = 0;
						while (params.hasNext()) {
							varIndex++;
							Element paramElement = (Element) params.next();
							String pname = paramElement.attributeValue("name");
							String ptype = paramElement.attributeValue("type");
							String defaultVal = paramElement.attributeValue("value");
							String comment = paramElement.attributeValue("comment");
							String tipconf = paramElement.attributeValue("tipconf");
							//处理非空和去掉逗号和冒号
							pname = (pname == null ? "arg" : pname.replaceAll(",", "").replaceAll(":", ""));
							ptype = (ptype == null ? "Object" : ptype.replaceAll(",", "").replaceAll(":", ""));
							defaultVal = (defaultVal == null ? "" : defaultVal.trim());
							//处理注释字串的转义
							comment = (comment == null? "" : Convert.convertSpliter(comment.replaceAll(",", ""),true));
							tipconf = (tipconf == null? "" : tipconf.replaceAll(",", "").replaceAll(":", ""));
							
							if(ptype.endsWith("...")){
								//如果是变长参数
								ptype = ptype.substring(0, ptype.length()-3);
								//String...:str2::<NOSET>:comment...:
								compNode.setVariadic(varIndex + "|" + ptype+":"+pname+"::<NOSET>:"+comment+":");
							}
							compNode.addParam(new Parameter(pname, ptype, defaultVal, comment,tipconf));
							//TODO 对提示的配置处理
						}
					}
					compNode.coverParamToStr();
				}else{
					//直接取paramstr的值赋给Arg
					compNode.setArgs(paramstr.attributeValue("val"));
				}
				
				//处理返回参数
				Element retstr = element.element("retstr");
				if(retstr==null){
					getRetConfig(element, compNode);
				}else{
					compNode.setRetType(retstr.attributeValue("val"));					
				}
				
				tree.addChildren(compNode);
			}
		}
		return tree;
	}


	/**
	 * 处理返回配置，将XML里的配置信息转化为节点的属性，并设置
	 * @param element XML里的节点元素
	 * @param compNode 模型中的节点对象
	 */
	private void getRetConfig(Element element,JavaComponentTreeNode compNode){
		@SuppressWarnings("unchecked")
		Iterator<Object> rets = element.elementIterator("ret");
		
		if (rets != null) {
			
			while (rets.hasNext()) {
				Element retElement = (Element) rets.next();
				String type = retElement.attributeValue("type");
				String name = retElement.attributeValue("name");
				String comment = retElement.attributeValue("comment");
				
				compNode.addReturnStr(type,name,comment);
			}
			compNode.setRetType(compNode.getReturnStr());
		}
	}
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout(1,true);

		parent.setLayout(gridLayout);
		
		searchText = new Text(parent,SWT.SINGLE|SWT.BORDER);
		GridData gdText = new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.GRAB_HORIZONTAL);
		searchText.setLayoutData(gdText);
		searchText.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode==SWT.CR){
					//处理回车事件
					String keyWords = searchText.getText();
					if(keyWords.length()==0){
						if(displayType == DisplayType.STAND_COMPONENT){
							viewer.setInput(varTree);
						}else if(displayType == DisplayType.VAR_TYPE){
							viewer.setInput(libCompTree);
						}else{
							viewer.setInput(prjCompTree);
						}
						
						
					}else{
						if(displayType == DisplayType.STAND_COMPONENT){
							viewer.setInput(findKeyWords((DirTree) varTree, keyWords));
						}else if(displayType == DisplayType.VAR_TYPE){
							viewer.setInput(findKeyWords((DirTree) libCompTree, keyWords));
						}else{
							viewer.setInput(findKeyWords((DirTree) prjCompTree, keyWords));
						}
						
						viewer.expandAll();
					}
				}
			}

		});
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gdTree = new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.GRAB_HORIZONTAL|GridData.VERTICAL_ALIGN_FILL|GridData.GRAB_VERTICAL);
		composite.setLayoutData(gdTree);
		composite.setLayout(new FillLayout());
		
		viewer = new TreeViewer(composite, SWT.BORDER|SWT.H_SCROLL);
		viewer.setContentProvider(new TreeContentProvider());
		viewer.setLabelProvider(new TreeLabelProvider());
		viewer.setSorter(new NameSorter());
		
		viewer.setInput(libCompTree);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),"logic.diagram.compiler.views.viewer");
		
		makeActions();
		hookContextMenu();
//		hookDoubleClickAction();
		contributeToActionBars();

		new JavaNodeToolTip(viewer.getControl());
		
		// 添加拖动处理
		viewer.addDragSupport(operations, transfer, new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {

				event.doit = true;
				// 使用LocalSelectionTransfer的时候必须加上，否则在DropTarget上取不到data。
				LocalSelectionTransfer.getTransfer().setSelection(
						viewer.getSelection());
				LocalSelectionTransfer.getTransfer().setSelectionSetTime(
						event.time & 0xFFFFFFFFL);
			
			}

			public void dragSetData(DragSourceEvent event) {

				if (LocalSelectionTransfer.getTransfer().isSupportedType(
						event.dataType)) {
//					Call object = (Call) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
//					event.data = object;
					Object obj = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
					event.data = obj;
				}
			}

			public void dragFinished(DragSourceEvent event) {
			}
		});
	}

	/**
	 * 按照输入的关键字进行查找
	 * @param tree 输入的树型结构树
	 * @param keywords 关键字
	 * @return
	 */
	private ITree findKeyWords(DirTree tree,String keywords){
		DirTree resultTree = new DirTree();
		resultTree.setName("查找结果");
		filterTreeNode(resultTree,tree,keywords);
		return resultTree;
	}
	
	private void filterTreeNode(DirTree resultTree, DirTree holeTree, String keywords) {
		@SuppressWarnings("unchecked")
		List<DirTree> children = holeTree.getChildren();
		// 存在子节点，则遍历
		for (Object child : children) {
			if (child instanceof DirTree) {
				DirTree dir = (DirTree) child;
				if (dir.hasChildren() == true) {
					DirTree branch = new DirTree();
					branch.setName(dir.getName());
					filterTreeNode(branch,dir,keywords);
					if(branch.hasChildren()){
						resultTree.addChildren(branch);
					}
				}
			} else {
				JavaComponentTreeNode node = (JavaComponentTreeNode) child;
				if (node.getName().toUpperCase().contains(keywords.toUpperCase())) {
					resultTree.addChildren(node);
				}
			}
		}
	}

	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				JavaComponentView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		//fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(actionDropCustItem);
		manager.add(new Separator());
		manager.add(actionSwitchVarView);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(actionDropCustItem);
		//manager.add(actionSwitchVarView);
		manager.add(actionReloadTree);
		// Other plug-ins can contribute there actions here
		//manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		//manager.add(actionAddCustItem);
		manager.add(actionSwitchPrjCompView);
		manager.add(actionSwitchVarView);
		manager.add(actionSwitchCompView);
		manager.add(actionReloadTree);
	}

	private void makeActions() {

		//----------------------------------------------------------------------------------------------
		// 删除自定义结构操作
		actionDropCustItem = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object selectObj = ((IStructuredSelection) selection).getFirstElement();
				if(selectObj instanceof JavaComponentTreeNode){
					JavaComponentTreeNode node = (JavaComponentTreeNode) ((IStructuredSelection) selection).getFirstElement();
					String nodeName = node.getName();
					String funcName = node.getFuncName();
					String ext = node.getExternal();
					ext = (ext==null? "" : ext.trim());
					
					if (funcName.startsWith("ref:cust:") || ext.startsWith("bixref:")) {
						// 是自定义组件
						if(showConfirm("是否删除组件：" + node.getName())){
							boolean doit = CustomConfigFileUtil.dropCustomItem(nodeName, funcName, false);
							if(doit)
								JavaComponentView.this.refreshAfterAddItem();
						}
						
					} else {
						// 不是自定义组件，给出不可删除提示
						showMessage("系统组件不可删除");
					}
				}else if(selectObj instanceof DirTree){
					//如果选择的是不是叶子节点
					showMessage("只能对叶子节点进行操作");
				}
			}
		};
		actionDropCustItem.setText("删除组件");
		actionDropCustItem.setToolTipText("删除选中的自定义组件");
		actionDropCustItem.setImageDescriptor(LogicImageUtil.getInstance().getDescriptor("icons/delete.png"));


		//----------------------------------------------------------------------------------------------
		actionSwitchPrjCompView = new Action() {
			public void run() {
				//显示变量
				JavaComponentView.this.viewer.setInput(prjCompTree);
				JavaComponentView.this.viewer.refresh(true);
				JavaComponentView.this.viewer.setLabelProvider(new ProjectComponentLabelProvider());
				JavaComponentView.this.displayType = DisplayType.PRJ_COMPONENT;
				//JavaComponentView.this.searchText.setEnabled(false);
				JavaComponentView.this.searchText.setText("");
			}
		};
		actionSwitchPrjCompView.setText("切换视图");
		actionSwitchPrjCompView.setToolTipText("切换到工程组件视图");
		actionSwitchPrjCompView.setImageDescriptor(LogicImageUtil.getInstance().getDescriptor("icons/projectCompView.png"));
		//actionSwitchPrjCompView.setEnabled(false);

		
		//----------------------------------------------------------------------------------------------
		actionSwitchVarView = new Action() {
			public void run() {
				//显示变量
				JavaComponentView.this.viewer.setInput(varTree);
				JavaComponentView.this.viewer.refresh(true);
				JavaComponentView.this.viewer.setLabelProvider(new VarTreeLabelProvider());
				JavaComponentView.this.displayType = DisplayType.VAR_TYPE;
				//JavaComponentView.this.searchText.setEnabled(false);
				JavaComponentView.this.searchText.setText("");
			}
		};
		actionSwitchVarView.setText("切换视图");
		actionSwitchVarView.setToolTipText("切换到变量视图");
		actionSwitchVarView.setImageDescriptor(LogicImageUtil.getInstance().getDescriptor("icons/varItemView.png"));
		//actionSwitchVarView.setEnabled(false);
		
		
		//----------------------------------------------------------------------------------------------
		actionSwitchCompView = new Action() {
			public void run() {
				//显示构件
				JavaComponentView.this.viewer.setInput(libCompTree);
				JavaComponentView.this.viewer.refresh(true);
				JavaComponentView.this.viewer.setLabelProvider(new TreeLabelProvider());
				JavaComponentView.this.displayType = DisplayType.STAND_COMPONENT;
				//JavaComponentView.this.searchText.setEnabled(true);
				JavaComponentView.this.searchText.setText("");
			}
		};
		actionSwitchCompView.setText("切换视图");
		actionSwitchCompView.setToolTipText("切换到组件视图");
		actionSwitchCompView.setImageDescriptor(LogicImageUtil.getInstance().getDescriptor("icons/compsView.png"));
		

		//----------------------------------------------------------------------------------------------
		actionReloadTree = new Action(){
			public void run(){
				//刷新构件，从XML中重新加载配置文件
				JavaComponentView.this.refreshAfterAddItem();
				actionSwitchCompView.run();
				//showMessage("数据已重新加载");
			}
		};
		actionReloadTree.setText("重新加载");
		actionReloadTree.setToolTipText("重新加载配置文件到视图");
		actionReloadTree.setImageDescriptor(LogicImageUtil.getInstance().getDescriptor("icons/reload.png"));
		
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),"业务构件库", message);
	}
	
	private boolean showConfirm(String message){
		return MessageDialog.openConfirm(viewer.getControl().getShell(),"业务构件库", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}