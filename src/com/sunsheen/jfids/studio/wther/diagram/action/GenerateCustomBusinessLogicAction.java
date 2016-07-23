package com.sunsheen.jfids.studio.wther.diagram.action;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.run.Constants;

/**
 * 生成自定义业务逻辑流
 * 
 * @author litao
 * 
 */
public class GenerateCustomBusinessLogicAction implements IObjectActionDelegate {
	private Element componentgroup;
	private String classPathRoot;
	private CustomClassLoader classLoader;

	private Class<? extends Annotation> bixComponentPackageClz;
	private Class<? extends Annotation> componentClz;
	private Class<? extends Annotation> paramsClz;
	private Class<? extends Annotation> returnsClz;
	private Class<? extends Annotation> exampleClz;

	public GenerateCustomBusinessLogicAction() {
	}

	private IStructuredSelection selection;

	@Override
	public void run(IAction action) {
		if (selection.size() != 1)
			return;
		final IProject project = (IProject) selection.getFirstElement();

		if (!MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "确定生成", "将生成自定义业务逻辑流构件库!")) {
			return;
		}

		final Document document = DocumentHelper.createDocument();
		componentgroup = document.addElement("component-group");

		final IFolder classFolder = project.getFolder(Constants.PROJECT_PATH_WEBROOT_WEBINF_CLASSES);
		classPathRoot = classFolder.getLocation().toString();

		List<URL> sourceclasspath = new ArrayList<URL>();
		try {
			IJavaProject jproject = JavaCore.create(project);
			IClasspathEntry[] currEntries = jproject.getRawClasspath();
			IPath resolvedPath = ResourcesPlugin.getWorkspace().getRoot().getFile(jproject.getOutputLocation())
					.getLocation();
			if (resolvedPath != null) {
				sourceclasspath.add(resolvedPath.toFile().toURI().toURL());
			}
			for (IClasspathEntry ce : currEntries) {
				if (ce.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
					resolvedPath = JavaCore.getResolvedVariablePath(ce.getPath());
				} else if (ce.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
					resolvedPath = ce.getPath();
					if (resolvedPath.isRoot())
						resolvedPath = ResourcesPlugin.getWorkspace().getRoot().getFile(resolvedPath).getLocation();
				} else if (ce.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
					IClasspathContainer cc = JavaCore.getClasspathContainer(ce.getPath(), jproject);
					for (IClasspathEntry ice : cc.getClasspathEntries()) {
						sourceclasspath.add(ice.getPath().toFile().toURI().toURL());
					}
				}
				if (resolvedPath != null) {
					sourceclasspath.add(resolvedPath.toFile().toURI().toURL());
					resolvedPath = null;
				}
			}
		} catch (JavaModelException e) {
			Log.error(e.getMessage(), e);
		} catch (MalformedURLException e) {
			Log.error(e.getMessage(), e);
		}
		classLoader = new CustomClassLoader(sourceclasspath.toArray(new URL[0]));

		Job generateJob = new Job("正在生成业务逻辑流构件库配置") {

			@SuppressWarnings("unchecked")
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {

					monitor.beginTask("正在读取业务逻辑流构件库配置信息", calculateClassCount(classFolder));

					// 运行时自定义classloader加载class，不使用插件的classloader
					bixComponentPackageClz = (Class<? extends Annotation>) classLoader
							.loadClass("com.sunsheen.jfids.system.bizass.annotation.BixComponentPackage");
					componentClz = ((Class<? extends Annotation>) classLoader
							.loadClass("com.sunsheen.jfids.system.bizass.annotation.Component"));
					paramsClz = ((Class<? extends Annotation>) classLoader
							.loadClass("com.sunsheen.jfids.system.bizass.annotation.Params"));
					returnsClz = ((Class<? extends Annotation>) classLoader
							.loadClass("com.sunsheen.jfids.system.bizass.annotation.Returns"));
					exampleClz = ((Class<? extends Annotation>) classLoader
							.loadClass("com.sunsheen.jfids.system.bizass.annotation.Example"));

					analysisDir(new File(classPathRoot), classPathRoot.length(), monitor);
					monitor.done();

					XMLWriter writer = null;
					OutputFormat format = OutputFormat.createPrettyPrint();
					format.setEncoding("UTF-8");
					FileOutputStream fout = new FileOutputStream(getTargetXMLFile(project.getName()));
					writer = new XMLWriter(fout, format);
					writer.write(document);
					writer.close();

				} catch (Exception e) {
					Log.error(e.getMessage(), e);
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}
		};
		generateJob.setUser(true);
		generateJob.schedule();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			this.selection = ((IStructuredSelection) selection);
		}
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	public File getTargetXMLFile(String projectName) {
		return new File(Platform.getInstallLocation().getURL().getPath()
				+ "/configuration/com.sunsheen.jfids/component/java/custom_" + projectName.toLowerCase() + ".xml");
	}

	public Element refreshElements(String className, String dirName) {
		Element baseDirEle = findOrAddElement(componentgroup, "name", "自定义构件");
		return findOrAddElement(baseDirEle, "name", dirName);
	}

	public int calculateClassCount(IFolder folder) {
		int fileCount = 0;
		try {
			IResource[] resources = folder.members();
			for (IResource res : resources) {
				if (res.getType() == IResource.FOLDER) {
					fileCount += calculateClassCount((IFolder) res);
				} else if (Constants.FILE_EXT_CLASS.equals(res.getFileExtension())) {
					++fileCount;
				}
			}
		} catch (CoreException e) {
			Log.error(e.getMessage(), e);
		}
		return fileCount;
	}

	public void analysisDir(File dir, int classPathLength, IProgressMonitor monitor) {

		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			if (fs[i].isDirectory()) {
				try {
					analysisDir(fs[i], classPathLength, monitor);
				} catch (Exception e) {
				}
			} else if (fs[i].isFile() && fs[i].getName().endsWith("." + Constants.FILE_EXT_CLASS)) {
				String className = fs[i].getPath().substring(classPathLength + 1, fs[i].getPath().length() - 6)
						.replace(File.separatorChar, '.');
				monitor.setTaskName("正在生成:" + className);
				dealClass(className);
				monitor.worked(1);
			}
		}
	}

	public void dealClass(String className) {
		String dirname;
		String comName;
		String comMemo;
		String comClassName;

		Annotation[] paramValue;
		String paramType;
		String paramName;
		String paramTipconfig;

		Annotation[] retValue;
		String retType;
		String retName;
		String retComment;

		Annotation[] exeValue;
		String execom;

		boolean isBixComponent = false;
		Element dir = null;
		Element component;
		Element memo;
		Element param;
		Element ret;

		try {
			Class<?> myClass = classLoader.loadClass(className);

			if (myClass.isAnnotationPresent(bixComponentPackageClz)) {

				Annotation[] bixComponentPackages = myClass.getAnnotations();
				for (Annotation compos : bixComponentPackages) {
					if (compos.annotationType() == bixComponentPackageClz) {
						dirname = invokeMethod(compos, "dirname");
						dir = refreshElements(className, dirname);
						if (dir == null) {
							Log.warn("生成业务构件配置-无法处理：" + className);
							return;
						}
						isBixComponent = true;
					}
				}
				if (!isBixComponent) {
					return;
				}

				Method m[] = myClass.getDeclaredMethods();
				for (int i = 0; i < m.length; i++) {
					Method me = m[i];
					if (me.isAnnotationPresent(componentClz)) {
						Annotation componentAnno = me.getAnnotation(componentClz);
						comName = invokeMethod(componentAnno, "name");
						comMemo = invokeMethod(componentAnno, "memo");
						comClassName = invokeMethod(componentAnno, "classname");

						component = dir.addElement("component");
						component.addAttribute("name", comName);
						component.addAttribute("classname", "".equals(comClassName) ? className : comClassName);

						if (me.isAnnotationPresent(paramsClz)) {
							Annotation paramsAnno = me.getAnnotation(paramsClz);
							paramValue = (Annotation[]) invokeMethodAsObject(paramsAnno, "value");
							for (Annotation it : paramValue) {
								paramType = invokeMethod(it, "type");
								paramName = invokeMethod(it, "name");
								paramTipconfig = invokeMethod(it, "tipconfig");

								param = component.addElement("param");

								param.addAttribute("type", paramType);
								param.addAttribute("name", paramName);
								param.addAttribute("comment", invokeMethod(it, "comment"));
								param.addAttribute("tipconfig", paramTipconfig);
							}
						}

						if (me.isAnnotationPresent(returnsClz)) {
							Annotation retAnno = me.getAnnotation(returnsClz);
							retValue = (Annotation[]) invokeMethodAsObject(retAnno, "retValue");
							for (Annotation re : retValue) {
								retType = invokeMethod(re, "type");
								retName = invokeMethod(re, "name");
								retComment = invokeMethod(re, "comment");

								ret = component.addElement("ret");
								if (retName.equals("") && retComment.equals("")) {
									ret.addAttribute("type", retType);
								} else if (retName.equals("")) {
									ret.addAttribute("type", retType);
									ret.addAttribute("comment", retComment);
								} else if (retComment.equals("")) {
									ret.addAttribute("type", retType);
									ret.addAttribute("name", retName);
								} else {
									ret.addAttribute("type", retType);
									ret.addAttribute("name", retName);
									ret.addAttribute("comment", retComment);
								}
							}
						}

						if (me.isAnnotationPresent(exampleClz)) {
							Annotation exeAnno = (Annotation) me.getAnnotation(exampleClz);
							exeValue = (Annotation[]) invokeMethodAsObject(exeAnno, "exeampleValue");

							comMemo += "\r\n------------示例-------------\r\n";
							for (Annotation ex : exeValue) {
								execom = invokeMethod(ex, "exeCom");
								comMemo += execom + "\r\n";
							}
							comMemo += "-----------------------------";
						}
						memo = component.addElement("memo");
						memo.addCDATA(comMemo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String invokeMethod(Annotation anno, String methodName) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return (String) invokeMethodAsObject(anno, methodName);
	}

	private Object invokeMethodAsObject(Annotation anno, String methodName) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return anno.getClass().getMethod(methodName).invoke(anno);
	}

	private Element findOrAddElement(Element rootEle, String attrKey, String attrVal) {
		for (Object ele : rootEle.elements()) {
			if (attrVal.equals(((Element) ele).attributeValue(attrKey))) {
				return (Element) ele;
			}
		}
		Element ele = rootEle.addElement("dir");
		ele.addAttribute(attrKey, attrVal);
		return ele;
	}
}
