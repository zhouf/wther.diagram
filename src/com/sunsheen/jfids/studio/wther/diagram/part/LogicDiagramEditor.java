package com.sunsheen.jfids.studio.wther.diagram.part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.ui.sourcelookup.IGraphicEditor;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gmf.runtime.common.ui.services.marker.MarkerNavigationService;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramDropTargetListener;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.ShapeImpl;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.SourceMethod;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IShowInTargetList;
import org.eclipse.ui.part.ShowInContext;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;
import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.logic.Call;
import com.sunsheen.jfids.studio.logic.LogicFactory;
import com.sunsheen.jfids.studio.logic.Node;
import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.run.ui.dialog.MessageNotifier;
import com.sunsheen.jfids.studio.run.utils.ProjectUtils;
import com.sunsheen.jfids.studio.wther.diagram.bglb.entity.BglbFileEntity;
import com.sunsheen.jfids.studio.wther.diagram.compiler.item.Flow;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.Convert;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ParseFlow;
import com.sunsheen.jfids.studio.wther.diagram.dialog.FlowDialog;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BixCallEditPartUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.FlowEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.GlobalBixUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.ParamConvert;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.ParseSourceMethod;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.PathConvert;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.RefreshVarTreeUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;
import com.sunsheen.jfids.studio.wther.diagram.navigator.LogicNavigatorItem;
import com.sunsheen.jfids.studio.wther.diagram.parser.FlowArgParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.FlowDefineParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.FlowRetParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.FlowArgItem;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.FlowDefineItem;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.FlowRetItem;

/**
 * @generated NOT
 */
public class LogicDiagramEditor extends DiagramDocumentEditor implements IGotoMarker, IGraphicEditor {


	/**
	 * @generated
	 */
	public static final String ID = "com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorID"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final String CONTEXT_ID = "com.sunsheen.jfids.studio.wther.diagram.ui.diagramContext"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public LogicDiagramEditor() {
		super(true);
	}

	/**
	 * @generated
	 */
	protected String getContextID() {
		return CONTEXT_ID;
	}

	/**
	 * @generated
	 */
	protected PaletteRoot createPaletteRoot(PaletteRoot existingPaletteRoot) {
		PaletteRoot root = super.createPaletteRoot(existingPaletteRoot);
		new LogicPaletteFactory().fillPalette(root);
		return root;
	}

	/**
	 * @generated
	 */
	protected PreferencesHint getPreferencesHint() {
		return LogicDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
	}

	/**
	 * @generated
	 */
	public String getContributorId() {
		return LogicDiagramEditorPlugin.ID;
	}

	/**
	 * @generated
	 */
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class type) {
		if (type == IShowInTargetList.class) {
			return new IShowInTargetList() {
				public String[] getShowInTargetIds() {
					return new String[] { ProjectExplorer.VIEW_ID };
				}
			};
		}
		return super.getAdapter(type);
	}

	/**
	 * @generated
	 */
	protected IDocumentProvider getDocumentProvider(IEditorInput input) {
		if (input instanceof IFileEditorInput || input instanceof URIEditorInput) {
			return LogicDiagramEditorPlugin.getInstance().getDocumentProvider();
		}
		return super.getDocumentProvider(input);
	}

	/**
	 * @generated
	 */
	public TransactionalEditingDomain getEditingDomain() {
		IDocument document = getEditorInput() != null ? getDocumentProvider().getDocument(getEditorInput()) : null;
		if (document instanceof IDiagramDocument) {
			return ((IDiagramDocument) document).getEditingDomain();
		}
		return super.getEditingDomain();
	}

	/**
	 * @generated
	 */
	protected void setDocumentProvider(IEditorInput input) {
		if (input instanceof IFileEditorInput || input instanceof URIEditorInput) {
			setDocumentProvider(LogicDiagramEditorPlugin.getInstance().getDocumentProvider());
		} else {
			super.setDocumentProvider(input);
		}
		// TODO 为解决在svn环境下Ctrl+Z无法回退的问题
		// super.setDocumentProvider(input);
	}

	/**
	 * @generated
	 */
	public void gotoMarker(IMarker marker) {
		MarkerNavigationService.getInstance().gotoMarker(this, marker);
	}

	/**
	 * @generated
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * @generated
	 */
	public void doSaveAs() {
		performSaveAs(new NullProgressMonitor());
	}

	/**
	 * @generated
	 */
	protected void performSaveAs(IProgressMonitor progressMonitor) {
		Shell shell = getSite().getShell();
		IEditorInput input = getEditorInput();
		SaveAsDialog dialog = new SaveAsDialog(shell);
		IFile original = input instanceof IFileEditorInput ? ((IFileEditorInput) input).getFile() : null;
		if (original != null) {
			dialog.setOriginalFile(original);
		}
		dialog.create();
		IDocumentProvider provider = getDocumentProvider();
		if (provider == null) {
			// editor has been programmatically closed while the dialog was open
			return;
		}
		if (provider.isDeleted(input) && original != null) {
			String message = NLS.bind(Messages.LogicDiagramEditor_SavingDeletedFile, original.getName());
			dialog.setErrorMessage(null);
			dialog.setMessage(message, IMessageProvider.WARNING);
		}
		if (dialog.open() == Window.CANCEL) {
			if (progressMonitor != null) {
				progressMonitor.setCanceled(true);
			}
			return;
		}
		IPath filePath = dialog.getResult();
		if (filePath == null) {
			if (progressMonitor != null) {
				progressMonitor.setCanceled(true);
			}
			return;
		}
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IFile file = workspaceRoot.getFile(filePath);
		final IEditorInput newInput = new FileEditorInput(file);
		// Check if the editor is already open
		IEditorMatchingStrategy matchingStrategy = getEditorDescriptor().getEditorMatchingStrategy();
		IEditorReference[] editorRefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (int i = 0; i < editorRefs.length; i++) {
			if (matchingStrategy.matches(editorRefs[i], newInput)) {
				MessageDialog.openWarning(shell, Messages.LogicDiagramEditor_SaveAsErrorTitle,
						Messages.LogicDiagramEditor_SaveAsErrorMessage);
				return;
			}
		}
		boolean success = false;
		try {
			provider.aboutToChange(newInput);
			getDocumentProvider(newInput).saveDocument(progressMonitor, newInput,
					getDocumentProvider().getDocument(getEditorInput()), true);
			success = true;
		} catch (CoreException x) {
			IStatus status = x.getStatus();
			if (status == null || status.getSeverity() != IStatus.CANCEL) {
				ErrorDialog.openError(shell, Messages.LogicDiagramEditor_SaveErrorTitle,
						Messages.LogicDiagramEditor_SaveErrorMessage, x.getStatus());
			}
		} finally {
			provider.changed(newInput);
			if (success) {
				setInput(newInput);
			}
		}
		if (progressMonitor != null) {
			progressMonitor.setCanceled(!success);
		}
	}

	/**
	 * @generated
	 */
	public ShowInContext getShowInContext() {
		return new ShowInContext(getEditorInput(), getNavigatorSelection());
	}

	/**
	 * @generated
	 */
	private ISelection getNavigatorSelection() {
		IDiagramDocument document = getDiagramDocument();
		if (document == null) {
			return StructuredSelection.EMPTY;
		}
		Diagram diagram = document.getDiagram();
		if (diagram == null || diagram.eResource() == null) {
			return StructuredSelection.EMPTY;
		}
		IFile file = WorkspaceSynchronizer.getFile(diagram.eResource());
		if (file != null) {
			LogicNavigatorItem item = new LogicNavigatorItem(diagram, file, false);
			return new StructuredSelection(item);
		}
		return StructuredSelection.EMPTY;
	}

	PaletteViewer paletteViewer = null;
	DefaultEditDomain editDomain = null;
	DiagramGraphicalViewer graphicalViewer = null;

	/**
	 * 这里是自定义的一个初始化方法，完成快捷键激活工具栏操作
	 */
	private void MyInit() {
		//this.getEditorSite().getPage().addPartListener(EditorPartListener.getInstance());
		if (paletteViewer == null)
			paletteViewer = this.getEditDomain().getPaletteViewer();
		if (editDomain == null)
			editDomain = this.getEditDomain();
		if (graphicalViewer == null)
			graphicalViewer = (DiagramGraphicalViewer) this.getDiagramGraphicalViewer();
		

		// 处理按键的工具栏激活事件
//		this.getDiagramGraphicalViewer().getControl().addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				// Log.debug("这里是按下键的处理 character[" +
//				// e.character + "] keycode[" + e.keyCode + "]");
//				ToolTipForActivityListener.stopTip();
//				// A(97)-G(103) Q(113)-T(116)
//				int kcode = e.keyCode;
//				if ((kcode >= 97 && kcode <= 103) || (kcode >= 113 && kcode <= 116)) {
//					editDomain.setActiveTool(LogicPaletteFactory.creationTool(kcode).createTool());
//					paletteViewer.getActiveTool().createTool();
//				}
//				super.keyPressed(e);
//			}
//		});
		// 处理双击事件
		this.getDiagramGraphicalViewer().getControl().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Object selectObject = ((IStructuredSelection) graphicalViewer.getSelection()).getFirstElement();
				if (selectObject instanceof FlowEditPart) {
					performRequest();
				}
			}
		});

		ResourcesPlugin.getWorkspace().addResourceChangeListener(this.resourceTracker);
	}

	/**
	 * 获取当前模型的参数信息
	 * @return List<Object>
	 */
	public List<FlowArgItem> getArgList() {
		FlowImpl flow = (FlowImpl) ((Diagram) (getDiagramEditPart().getModel())).getElement();
		String arg = flow.getArgs();
		FlowArgParser argParser = new FlowArgParser(arg);
		return argParser.getItemSet();
	}

	/**
	 * 这是一个自己定义响应鼠标双击事件的一个方法
	 */
	private void performRequest() {
		final FlowImpl flow = (FlowImpl) ((Diagram) (getDiagramEditPart().getModel())).getElement();
		final Map<String, Object> params = paramBuilder(flow);
		// params.put("flowname", flow.getName());
		params.put("author", flow.getAuthor());
		params.put("createtime", flow.getCreatetime());
		params.put("retstr", flow.getRet());
		params.put("canbeinvoked", (flow.isCanInvoked() ? "true" : "false"));
		params.put("comment", flow.getComment());

		DialogConfigParser parser = new FlowDialog(params);
		final SDialog dialog = parser.getDialog();
		dialog.show(LogicImageUtil.getInstance(), LogicDiagramEditorPlugin.getImage("icons/dialog_logo.gif"), "业务逻辑流配置");

		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(flow);
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@SuppressWarnings("unchecked")
			@Override
			protected void doExecute() {
				if (dialog.getReturnCode() == Dialog.OK) {
					VarStore varStore = new VarStore(flow.getVarstore());
					// flow.setName((String) params.get("flowname"));
					flow.setAuthor((String) params.get("author"));
					flow.setCreatetime((String) params.get("createtime"));
					flow.setCanInvoked((Boolean) params.get("canbeinvoked"));
					flow.setComment((String) params.get("comment"));

					String defString = coverDlgDefineData(
							(List<Map<Integer, Map<Integer, Object>>>) params.get("defTable"), varStore);
					flow.setDefine(defString);
					String argString = coverDlgData((List<Map<Integer, Map<Integer, Object>>>) params.get("argTable"),
							true, varStore);
					flow.setArgs(argString);
					String retString = coverDlgData((List<Map<Integer, Map<Integer, Object>>>) params.get("argTable"),
							false, varStore);
					flow.setRet(retString);
					flow.setVarstore(varStore.toString());
					// 获取全局变量设置的情况
					// String glbString = coverDlgDefineData((List<Map<Integer,
					// Map<Integer, Object>>>) params.get("glbTable"));
					// String oldGlbString = (String) params.get("glbParam");
					//
					// if (!glbString.equalsIgnoreCase(oldGlbString)) {
					// // 如果不相等，则说明全局变量有被修改过，重新写入全局变量的数据到文件
					// GlobalBixUtil.updateCurrentGlobalString(glbString);
					// }
					// 刷新变量树窗口

					RefreshVarTreeUtil.refresh(flow, true);
					
					LogicDiagramEditor.this.setDirty();
				}
			}
		});
	}

	public IFile getFile() {
		IFileEditorInput input = (IFileEditorInput) getEditorInput();
		return input.getFile();
	}

	/**
	 * 返回当前文件对应的全局文件
	 * 
	 * @return IFile
	 */
	private IFile findGlobalFile() {
		IFile retFile = null;
		IFile file = ((IFileEditorInput) this.getEditorInput()).getFile();
		retFile = GlobalBixUtil.getGlobalFile(file);
		return retFile;
	}

	/**
	 * 获得一个全局定义的实体对象
	 * 
	 * @return BglbFileEntity
	 */
	private BglbFileEntity getGlobalEntity() {
		BglbFileEntity retObj = null;
		IFile globalFile = findGlobalFile();
		if (globalFile != null && globalFile.exists()) {
			retObj = GlobalBixUtil.getGlobalEntityFromIFile(globalFile);
		}

		return retObj;
	}

	/**
	 * 处理返回的数据，把从对话框设置的数据处理成符合模型格式的字串，点击确定后保存时调用
	 * 
	 * @param data
	 *            表格中的数据
	 * @param isArg
	 *            标记是否是参数还是返回值
	 * @param varStore
	 *            变量集，传入用于更新
	 * @return 对应的拼接好的字串
	 */
	private String coverDlgData(List<Map<Integer, Map<Integer, Object>>> data, boolean isArg, VarStore varStore) {
		FlowArgParser argParser = new FlowArgParser();

		if (data.size() == 0) {
			// 如果没有数据，直接返回
			return "";
		}
		Map<Integer, Map<Integer, Object>> inputData;
		if (isArg) {
			inputData = data.get(0);// 取第一个表格数据
			varStore.removeByFrom(VarStore.ARG_TYPE);
		} else {
			if (data.size() > 1) {
				inputData = data.get(1);// 取第二个表格数据
			} else {
				return "";
			}
			varStore.removeByFrom(VarStore.RET_TYPE);
		}
		for (Entry<Integer, Map<Integer, Object>> eachRow : inputData.entrySet()) {
			// 循环取每行数据
			Map<Integer, Object> map = eachRow.getValue();
			FlowArgItem argItem = new FlowArgItem();

			String argName, argType, comment;
			boolean isArray = false;
			// 参数设置
			argName = Convert.convertSpliter((String) map.get(1), true);// 参数名
			argType = (String) map.get(2);// 参数类型
			isArray = (Boolean) map.get(3);// 是否数组
			// type = (String) map.get(4);// 类型
			comment = Convert.convertSpliter((String) map.get(4), true);// 描述
			// 对默认值和描述为空的处理
			argType = argType.length() == 0 ? "String" : argType;
			// type = type.length() == 0 ? "字符串" : type;
			comment = comment.length() == 0 ? "<NOSET>" : comment;

			argItem.setValName(argName);
			argItem.setType(argType);
			argItem.setArrayType(isArray);
			argItem.setComment(comment);

			argParser.addItem(argItem);
			String arrayMarker = (isArray ? "[]" : ""); // 标记参数类型是否是数组
			if (isArg) {
				varStore.appendVar(argName + ":" + argType + arrayMarker + ":" + VarStore.ARG_TYPE);
			} else {
				varStore.appendVar(argName + ":" + argType + arrayMarker + ":" + VarStore.RET_TYPE);
			}
		}
		return argParser.toString();
	}

	/**
	 * 将变量定义表格中的数据转换为模型中的属性字串，点击对话框确定时执行
	 * 
	 * @param data
	 *            表格中的定义数据
	 * @param varStore
	 *            变量集，传入用于更新
	 * @return 拼接后的属性字串
	 */
	private String coverDlgDefineData(List<Map<Integer, Map<Integer, Object>>> data, VarStore varStore) {
		FlowDefineParser defineParser = new FlowDefineParser();
		varStore.removeByFrom(VarStore.DEFINE_TYPE);

		if (data.size() == 0) {
			// 如果没有数据，直接返回
			return "";
		}
		Map<Integer, Map<Integer, Object>> inputData = data.get(0);// 取第一个表格数据
		for (Entry<Integer, Map<Integer, Object>> eachRow : inputData.entrySet()) {
			// 循环取每行数据，列分别是：变量名，变量初值，变量类型，数组，是否定义
			Map<Integer, Object> map = eachRow.getValue();
			FlowDefineItem defItem = new FlowDefineItem();

			// 变量定义
			defItem.setVarName(Convert.convertSpliter((String) map.get(0), true));
			defItem.setInitVal(Convert.convertSpliter((String) map.get(1), true));
			defItem.setType((String) map.get(2));
			defItem.setArrayType((Boolean) map.get(3));
			defItem.setDefined(true);

			defineParser.addItem(defItem);

			if (defItem.isArrayType()) {
				varStore.appendVar(defItem.getVarName() + ":" + defItem.getType() + "[]:" + VarStore.DEFINE_TYPE);
			} else {
				varStore.appendVar(defItem.getVarName() + ":" + defItem.getType() + ":" + VarStore.DEFINE_TYPE);
			}
		}
		return defineParser.toString();
	}

	/**
	 * 根据模型属性构造表格中所需要的数据
	 * 
	 * @param flow
	 *            转入的模型
	 * @return
	 */
	public Map<String, Object> paramBuilder(FlowImpl flow) {
		Map<String, Object> params = new HashMap<String, Object>();
		String args = flow.getArgs();
		String retStr = flow.getRet();
		String defineStr = flow.getDefine();
		args = (args == null ? "" : args.trim());
		retStr = (retStr == null ? "" : retStr.trim());
		defineStr = (defineStr == null ? "" : defineStr.trim());
		// String glbParam = findGlobalParam();

		Map<Integer, Map<Integer, Object>>[] data = new HashMap[2];
		Map<Integer, Map<Integer, Object>> tableInput;
		Map<Integer, Map<Integer, Object>> tableOutput;
		Map<Integer, Map<Integer, Object>>[] tableDefine;
		Map<Integer, Map<Integer, Object>>[] tableGlobal;

		if (defineStr.length() > 0) {
			tableDefine = convertDefineToTable(defineStr);
			params.put("defTable", tableDefine);
		}

		if (args.length() > 0) {
			tableInput = convertArgToTable(args);
			data[0] = tableInput;
		} else {
			// Log.debug("LogicDiagramEditor.paramBuilder()-> args为空，不生成参数数据");
		}

		if (retStr.length() > 0) {
			// 如果有返回
			tableOutput = convertRetToTable(retStr);
			data[1] = tableOutput;
		} else {
			// Log.debug("LogicDiagramEditor.paramBuilder()-> retStr为空，不生成返回数据");
		}
		params.put("argTable", data);

		// if (glbParam.length() > 0) {
		// // 如果有返回
		// tableGlobal = convertDefineToTable(glbParam);
		// params.put("glbTable", tableGlobal);
		// // 将原始的字串值放入，用于是否修改的比较
		// params.put("glbParam", glbParam);
		// }

		return params;
	}

	/**
	 * 将定义字串转换为表格数据
	 * 
	 * @param defString
	 *            输入的定义字串
	 * @return
	 */
	public static Map<Integer, Map<Integer, Object>>[] convertDefineToTable(String defineStr) {
		Map<Integer, Map<Integer, Object>>[] data = new HashMap[1];
		Map<Integer, Map<Integer, Object>> tableDefine = new HashMap<Integer, Map<Integer, Object>>();
		FlowDefineParser defineParser = new FlowDefineParser(defineStr);
		int rowIndex = 0;
		for (FlowDefineItem item : defineParser.getItemSet()) {
			Map<Integer, Object> row = new HashMap<Integer, Object>();
			row.put(0, item.getVarName());
			row.put(1, item.getInitVal());
			row.put(2, item.getType());
			row.put(3, item.isArrayType());

			tableDefine.put(rowIndex++, row);
		}

		data[0] = tableDefine;
		return data;

	}

	/**
	 * 将返回参数转换为对话框中的表格数据
	 * 
	 * @param retType
	 *            其格式为=> 参数/返回：变量名：变量类型：数组：描述|数据类型：参数值：参数类型：参数描述
	 * @return
	 */
	public static Map<Integer, Map<Integer, Object>> convertArgToTable(String args) {
		Map<Integer, Map<Integer, Object>> tableOutput;
		tableOutput = new HashMap<Integer, Map<Integer, Object>>();
		FlowArgParser argParser = new FlowArgParser(args);
		int rowIndex = 0;
		for (FlowArgItem item : argParser.getItemSet()) {
			Map<Integer, Object> row = new HashMap<Integer, Object>();
			row.put(0, "参数");
			row.put(1, item.getValName());
			row.put(2, item.getType());
			row.put(3, item.isArrayType());
			row.put(4, item.getComment());
			tableOutput.put(rowIndex++, row);
		}

		return tableOutput;
	}

	public static Map<Integer, Map<Integer, Object>> convertRetToTable(String args) {
		Map<Integer, Map<Integer, Object>> tableOutput;
		tableOutput = new HashMap<Integer, Map<Integer, Object>>();
		FlowRetParser argParser = new FlowRetParser(args);
		int rowIndex = 0;
		for (FlowRetItem item : argParser.getItemSet()) {
			Map<Integer, Object> row = new HashMap<Integer, Object>();
			row.put(0, "返回");
			row.put(1, item.getValName());
			row.put(2, item.getType());
			row.put(3, item.isArrayType());
			row.put(4, item.getComment());
			tableOutput.put(rowIndex++, row);
		}

		return tableOutput;
	}

	/**
	 * @generated NOT
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		DiagramEditorContextMenuProvider provider = new DiagramEditorContextMenuProvider(this,
				getDiagramGraphicalViewer());
		getDiagramGraphicalViewer().setContextMenu(provider);
		getSite().registerContextMenu(ActionIds.DIAGRAM_EDITOR_CONTEXT_MENU, provider, getDiagramGraphicalViewer());

		getDiagramGraphicalViewer().addDropTargetListener(new DropComponentTargetListener(getGraphicalViewer()));
		getDiagramGraphicalViewer().addDropTargetListener(new DropBixfileTargetListener(getGraphicalViewer()));
		MyInit();

	}

	/**
	 * 内容加载后的方法
	 */
	protected void createGraphicalViewer(Composite parent) {
		super.createGraphicalViewer(parent);
		ValidateAction.runValidation(this.getDiagramEditPart(), ((View) getDiagramDocument().getContent()));

	}

	/**
	 * 处理从工程树中拖放操作的一个监听器
	 * 
	 * @author zhouf
	 */
	public class DropBixfileTargetListener extends DiagramDropTargetListener {
		public DropBixfileTargetListener(EditPartViewer viewer) {
			super(viewer, LocalSelectionTransfer.getTransfer());
			// viewer.addDropTargetListener(new DropTargetListener(viewer){});
		}

		@Override
		public boolean isEnabled(DropTargetEvent event) {
			return true;
		}

		@SuppressWarnings("rawtypes")
		@Override
		protected List getObjectsBeingDropped() {
			List<EObject> eObjects = new ArrayList<EObject>();
			if (LocalSelectionTransfer.getTransfer().isSupportedType(this.getCurrentEvent().currentDataType)) {
				TreeSelection ts = (TreeSelection) this.getCurrentEvent().data;
				// Object dt = this.getCurrentEvent().data;
				this.getCurrentEvent().detail = DND.DROP_DEFAULT;
				try {
					if (ts != null) {
						Object firstElement = ts.getFirstElement();
						System.out
								.println("LogicDiagramEditor.DropBixfileTargetListener.getObjectsBeingDropped()->firstElement.getClass().getName():"
										+ firstElement.getClass().getName());
						if(firstElement instanceof SourceType){
							SourceType javaType = (SourceType)firstElement;
							try {
								System.out.println("LogicDiagramEditor.DropBixfileTargetListener.getObjectsBeingDropped()->javaType.getSuperclassName():"
										+ javaType.getSuperclassName());
							} catch (JavaModelException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}else if (firstElement instanceof SourceMethod) {
							SourceMethod method = (SourceMethod) firstElement;
							ParseSourceMethod parseSourceMethod = new ParseSourceMethod(method);
							if (parseSourceMethod.isStatic()) {
								Call call = LogicFactory.eINSTANCE.createCall();
								call.setArgs(parseSourceMethod.getArgStr());
								call.setRetType(parseSourceMethod.getRetStr());
								call.setName(parseSourceMethod.getMethodName());

								StringBuilder funcNameBuilder = new StringBuilder();
								funcNameBuilder.append(parseSourceMethod.getPkgStr());
								funcNameBuilder.append(".").append(parseSourceMethod.getClassName());
								funcNameBuilder.append(".").append(parseSourceMethod.getMethodName());

								call.setComment(funcNameBuilder + ":");
								call.setFuncName(funcNameBuilder.toString());
								call.setExternal("javamethod");

								eObjects.add(call);
							} else {
								// 非静态方法，给出提示后返回
								//MessageDialog.openWarning(null, "提示", "不支持非静态方法拖入");
								MessageNotifier.notify("不支持非静态方法拖入",MessageNotifier.WARING);
								return null;
							}
						} else {
							Log.debug("data is NOT instanceof SourceMethod:" + firstElement);
							IFile file = (IFile) firstElement;
							if ("bix".equalsIgnoreCase(file.getFileExtension())) {
								// 拖入时只接收Call节点，此处便构造了Call节点对象
								Call call = LogicFactory.eINSTANCE.createCall();
								Flow flow = ParseFlow.fromFile(file.getLocation().toOSString());
								String args = flow.getArgs();
								String rets = flow.getRet();
								if (args == null || "null".equalsIgnoreCase(args)) {
									args = "";
								} else {
									args = ParamConvert.convert4to6(args);
								}
								if (rets == null || "null".equalsIgnoreCase(rets)) {
									rets = "";
								} else {
									rets = ParamConvert.convert4to6(rets);
								}
								String filePath = file.getFullPath().toString();
								String dispPath = PathConvert.convertToDisplayPath(filePath);

								call.setArgs(args);
								call.setRetType(rets);
								call.setName(file.getName());
								call.setDisptip(dispPath);

								// FIXME 改造bixref，将Ext信息保存为调用字串，如this.fun,(new XX()).fun
								call.setFuncName(BixCallEditPartUtil.getPrefix(filePath));
								// call.setExternal("bixref:" +
								// file.getFullPath().toString());
								call.setExternal("bixref:" + ProjectUtils.getProjectFileKeyPath(file.getFullPath()));
								eObjects.add(call);
							}
						}
					}
				} catch (ClassCastException e) {
					Log.error("拖入时类型转换异常：" + e.toString());
				}
			}

			return eObjects;
		}
	}

	/**
	 * 处理拖放操作
	 * 
	 * @author zhoufeng Date:2013-5-29
	 */
	public class DropComponentTargetListener extends DiagramDropTargetListener {
		public DropComponentTargetListener(EditPartViewer viewer) {
			super(viewer, LocalSelectionTransfer.getTransfer());
		}

		@Override
		protected Request createTargetRequest() {
			return super.createTargetRequest();
		}

		@SuppressWarnings("rawtypes")
		@Override
		protected List getObjectsBeingDropped() {
			LocalSelectionTransfer transfer = (LocalSelectionTransfer) this.getTransfer();

			TransferData[] data = getCurrentEvent().dataTypes;
			List<EObject> eObjects = new ArrayList<EObject>();

			for (int i = 0; i < data.length; i++) {
				if (transfer.isSupportedType(data[i])) {
					IStructuredSelection selection = (IStructuredSelection) transfer.nativeToJava(data[i]);
					for (Object o : selection.toList()) {
						if (o instanceof Call) {
							eObjects.add((EObject) o);
						}
					}
				}
			}
			return eObjects;
		}
	}

	/**
	 * 在editor中控制断点，而不将断点放入模型中 <li>打开editor时，将断点显示在图形化中</li> <li>
	 * 在debug视图中删除断点时，将editor图形化中的断点去掉</li>
	 * 
	 * @author Len
	 * @since 2014-1-14
	 */
	private IResourceChangeListener resourceTracker = new ResourceTracker();

	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
//		doBreakPoints();
	}
	

	/**
	 * 处理editor中断点
	 */
//	public void doBreakPoints() {
//		IResource resource = ((IFileEditorInput) getEditorInput()).getFile();
//		if (resource != null) {
//			Diagram diagram = this.getDiagram();
//			EList list = diagram.getVisibleChildren();
//			for (int i = 0; i < list.size(); i++) {
//				ShapeImpl shapeimpl = (ShapeImpl) list.get(i);
//				Node element = (Node) shapeimpl.getElement();
//				if (element != null) {// 对于备注Note节点等，进行排除判断。
//					drawBreakPoint(element.getAbsLineNum(), false);
//				}
//			}
//
//			try {
//				for (IJavaLineBreakpoint breakpoint : BreakPointUtils.listLineBreakpoints(resource)) {
//					drawBreakPoint(breakpoint.getLineNumber(), true);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//	}

	

	public void dispose() {
		super.dispose();
		if (this.resourceTracker != null) {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(this.resourceTracker);
		}
		this.resourceTracker = null;
	}

	/**
	 * <li>Eclipse插件实现资源监听的主要方式是实现资源监听器，通过资源监听器，插件就能够保证Workspace中的资源和模型同步。另外，
	 * 资源监听器还能知道当前什么资源被改变，以及怎样被改变了。<br>
	 * 　　 资源监听器要实现IResourceChangeListener接口，并能通过IWorkspace的addResourceChange
	 * Listener方法注册
	 * 。当不再需要资源监听器时，一定要调用removeResourceChangeListener方法把监听器从IWorkspace中删除。<br>
	 * </li> <li>在Workspace中，资源是以树型结构组织的，用户可以通过树型结构访问被改变的资源树。通常，
	 * 用户应该实现IResourceDeltaVisitor接口，以实现改变资源的访问</li> <li>
	 * ResourceDelta和IResourceDeltaVisitor的实现类结合起来，实现了Visitor设计模式</li>
	 * 
	 * @author len
	 * 
	 * @since 2014-1-11
	 */
	class ResourceTracker implements IResourceChangeListener, IResourceDeltaVisitor {
		ResourceTracker() {
		}

		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			try {
				if (delta != null)
					delta.accept(this);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		public boolean visit(IResourceDelta delta) {
			if (!(LogicDiagramEditor.this.getEditorInput() instanceof IFileEditorInput)) {
				return false;
			}

			if ((delta == null)
					|| (!delta.getResource().equals(
							((FileEditorInput) LogicDiagramEditor.this.getEditorInput()).getFile()))) {
				return true;
			}

			if ((delta.getKind() == IResourceDelta.CHANGED || delta.getKind() == IResourceDelta.ADDED)
					&& ((IResourceDelta.MARKERS & delta.getFlags()) > 0)) {
				Display display = null;
				IWorkbenchPartSite partSite = LogicDiagramEditor.this.getSite();
				if (partSite != null) {
					Shell shell = partSite.getShell();
					if (shell != null)
						display = shell.getDisplay();
				}
				if (display == null)
					display = Display.getDefault();
//				display.asyncExec(new Runnable() {
//					public void run() {
//						if (LogicDiagramEditor.this != null) {
//							LogicDiagramEditor.this.doBreakPoints();
//							// LogicDiagramEditor.this.undrawRunDebuging();
//						}
//					}
//				});
			}
			return true;
		}
	}

	void undrawRunDebuging() {
		Diagram diagram = this.getDiagram();
		EList list = diagram.getVisibleChildren();
		for (int i = 0; i < list.size(); i++) {
			ShapeImpl shapeimpl = (ShapeImpl) list.get(i);
			Node element = (Node) shapeimpl.getElement();
		}
	}

	// ----------------------------------------------------------------------------------------------------
	LogicDiagramEditor logicDiagramEditor = this;

	/**
	 * 该方法为自定义覆盖的方法。 实现面板的tab切换.
	 * 
	 * @SS 2014-03-26
	 */
	@Override
	public void createPartControl(Composite parent) {
		// 定义ctabFolder
//		parent.setLayout(new FillLayout());
//		final CTabFolder ctabFolder = new CTabFolder(parent, SWT.BOTTOM | SWT.FLAT);
//
//		// 每一个tabItem内包含一个Composite的子类，因此我们可以将其分离出来，
//		// 这里继承composite就好了。
//		final CTabItem viewTabItem = new CTabItem(ctabFolder, SWT.NONE);
//		viewTabItem.setText(VIEW_TAB_TEXT);
//		// 实例化一个我们定制的composite
//		final Composite viewComposite = new Composite(ctabFolder, SWT.NONE);
//		viewComposite.setLayout(new FillLayout());
//		// 将我们定制的composite附加到ctabItem上。
//		viewTabItem.setControl(viewComposite);

		super.createPartControl(parent);// 再将view先放置到前面；
	}

	boolean isDirty = false;

	public void setDirty() {
		this.isDirty = true;
		this.firePropertyChange(PROP_DIRTY);
	}

	public void setNotDirty() {
		this.isDirty = false;
		this.firePropertyChange(PROP_DIRTY);
	}

	@Override
	public boolean isDirty() {
		return super.isDirty() || this.isDirty;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		final FlowImpl flowImpl = (FlowImpl) getDiagram().getElement();
		// 可能在流程发布或保存的时候，修改了相应的值，所以需要持久化起来。
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(flowImpl);
//		domain.getCommandStack().execute(new RecordingCommand(domain) {
//			@Override
//			protected void doExecute() {
//				flowImpl.setExtra(logicTestCasePage.getSerialData());
//			}
//
//		});

		this.isDirty = false;
		// 触发设置编辑器的*号
		// this.firePropertyChange(PROP_DIRTY);
		super.doSave(monitor);
	}

	private static IDebugEventSetListener debugEventSetListener = new IDebugEventSetListener() {
		@Override
		public void handleDebugEvents(DebugEvent[] events) {
			if (events.length != 1)
				return;
			DebugEvent event = events[0];
			//			System.out.println("Event:" + event);
			switch (event.getKind()) { // 本地调试结束判断
			case DebugEvent.TERMINATE:
				endDebuging(DebugEvent.TERMINATE);
				return;
			}
			switch (event.getDetail()) {
			case DebugEvent.STEP_OVER:
				endDebuging(DebugEvent.STEP_OVER);
				return;
			case DebugEvent.CLIENT_REQUEST:
				endDebuging(DebugEvent.CLIENT_REQUEST);
				return;
			case DebugEvent.STEP_RETURN:
				endDebuging(DebugEvent.STEP_RETURN);
				return;
			}
		}
	};

	private static LogicEditorDebugInfo logicEditorDebugInfo = null;

	public static void beginDebuging(LogicDiagramEditor currentEditor, int lastLineNumber) {
		if (logicEditorDebugInfo == null) {
			DebugPlugin.getDefault().addDebugEventListener(debugEventSetListener);
			logicEditorDebugInfo = new LogicEditorDebugInfo(currentEditor, lastLineNumber);
		}
	}

	@SuppressWarnings({ "restriction", "unchecked" })
	public static void endDebuging(int eventType) {
		if (logicEditorDebugInfo != null) {
			if (eventType == DebugEvent.STEP_OVER && !logicEditorDebugInfo.canFinish())
				return;
			JDIDebugTarget dt = (JDIDebugTarget) logicEditorDebugInfo.debugTarget;
			try {
				if (eventType == DebugEvent.CLIENT_REQUEST) { // 服务器Resume事件调试结束判断
					IFile inputFile = ((FileEditorInput) logicEditorDebugInfo.currentEditor.getEditorInput()).getFile();
//					for (IJavaLineBreakpoint breakpoint : BreakPointUtils.filterResourceLineBreakpoints(
//							dt.getBreakpoints(), inputFile)) {
//						if (breakpoint.getLineNumber() > logicEditorDebugInfo.currentLineNumber) // 后面还存在断点则不结束
//							return;
//					}
				}
				if (dt != null) {
					Thread.sleep(200);
					dt.resume();
				}
			} catch (DebugException e) {
				Log.error(e.getMessage(), e);
			} catch (InterruptedException e) {
				Log.error(e.getMessage(), e);
			} catch (CoreException e) {
				Log.error(e.getMessage(), e);
			}
			logicEditorDebugInfo.currentEditor.undrawRunDebuging();
			logicEditorDebugInfo = null;
			DebugPlugin.getDefault().removeDebugEventListener(debugEventSetListener);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void positionEditor(IStackFrame frame) {
		try {
			int lineNumber = frame.getLineNumber();
			Log.debug("Logic Debug#Current Line:" + lineNumber);
			undrawRunDebuging();
			Diagram diagram = this.getDiagram();
			EList<Object> list = diagram.getVisibleChildren();
			int lastLineNumber = 0;
			for (int i = 0; i < list.size(); i++) {
				ShapeImpl shapeimpl = (ShapeImpl) list.get(i);
				Node element = (Node) shapeimpl.getElement();
				if (lastLineNumber < element.getAbsLineNum())
					lastLineNumber = element.getAbsLineNum();
				if (element.getAbsLineNum() == lineNumber) {
//					drawRunDebuging(lineNumber, true);
				}
			}
			beginDebuging(this, lastLineNumber);
			if (logicEditorDebugInfo != null)
				logicEditorDebugInfo.updateLineNumber(frame.getDebugTarget(), lineNumber);
		} catch (DebugException e) {
			Log.error(e.getMessage(), e);
		}
	}

	/**
	 * 编辑器当前调试信息
	 * 
	 * @author litao
	 */
	private static class LogicEditorDebugInfo {
		public int lastLineNumber;
		public IDebugTarget debugTarget;
		private int currentLineNumber;
		public LogicDiagramEditor currentEditor;

		public LogicEditorDebugInfo(LogicDiagramEditor currentEditor, int lastLineNumber) {
			this.lastLineNumber = lastLineNumber;
			this.currentEditor = currentEditor;
		}

		public void updateLineNumber(IDebugTarget debugTarget, int lineNumber) {
			this.currentLineNumber = lineNumber;
			this.debugTarget = debugTarget;
		}

		public boolean canFinish() {
			return currentLineNumber >= lastLineNumber;
		}
	}
}
