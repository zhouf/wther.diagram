package com.sunsheen.jfids.studio.wther.diagram.part;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewAndElementRequest;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import com.sunsheen.jfids.studio.logic.Flow;
import com.sunsheen.jfids.studio.logic.LogicFactory;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.AssignmentEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.BlankEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.EndEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.FlowEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.StartEditPart;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.LineNumUtil;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;

/**
 * @generated
 */
public class LogicDiagramEditorUtil {

	/**
	 * @generated
	 */
	public static Map<?, ?> getSaveOptions() {
		HashMap<String, Object> saveOptions = new HashMap<String, Object>();
		saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
		saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
		return saveOptions;
	}

	/**
	 * @generated
	 */
	public static boolean openDiagram(Resource diagram) throws PartInitException {
		String path = diagram.getURI().toPlatformString(true);
		IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));
		if (workspaceResource instanceof IFile) {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			return null != page.openEditor(new FileEditorInput((IFile) workspaceResource), LogicDiagramEditor.ID);
		}
		return false;
	}

	/**
	 * @generated
	 */
	public static void setCharset(IFile file) {
		if (file == null) {
			return;
		}
		try {
			file.setCharset("UTF-8", new NullProgressMonitor()); //$NON-NLS-1$
		} catch (CoreException e) {
			LogicDiagramEditorPlugin.getInstance().logError("Unable to set charset for file " + file.getFullPath(), e); //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	public static String getUniqueFileName(IPath containerFullPath, String fileName, String extension) {
		if (containerFullPath == null) {
			containerFullPath = new Path(""); //$NON-NLS-1$
		}
		if (fileName == null || fileName.trim().length() == 0) {
			fileName = "default"; //$NON-NLS-1$
		}
		IPath filePath = containerFullPath.append(fileName);
		if (extension != null && !extension.equals(filePath.getFileExtension())) {
			filePath = filePath.addFileExtension(extension);
		}
		extension = filePath.getFileExtension();
		fileName = filePath.removeFileExtension().lastSegment();
		int i = 1;
		while (ResourcesPlugin.getWorkspace().getRoot().exists(filePath)) {
			i++;
			filePath = containerFullPath.append(fileName + i);
			if (extension != null) {
				filePath = filePath.addFileExtension(extension);
			}
		}
		return filePath.lastSegment();
	}

	/**
	 * Runs the wizard in a dialog.
	 * 
	 * @generated
	 */
	public static void runWizard(Shell shell, Wizard wizard, String settingsKey) {
		IDialogSettings pluginDialogSettings = LogicDiagramEditorPlugin.getInstance().getDialogSettings();
		IDialogSettings wizardDialogSettings = pluginDialogSettings.getSection(settingsKey);
		if (wizardDialogSettings == null) {
			wizardDialogSettings = pluginDialogSettings.addNewSection(settingsKey);
		}
		wizard.setDialogSettings(wizardDialogSettings);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.getShell().setSize(Math.max(500, dialog.getShell().getSize().x), 500);
		dialog.open();
	}

	/**
	 * This method should be called within a workspace modify operation since it creates resources.
	 * @generated NOT
	 */
	public static Resource createDiagram(URI diagramURI, IProgressMonitor progressMonitor) {
		TransactionalEditingDomain editingDomain = GMFEditingDomainFactory.INSTANCE.createEditingDomain();
		progressMonitor.beginTask(Messages.LogicDiagramEditorUtil_CreateDiagramProgressTask, 3);
		final Resource diagramResource = editingDomain.getResourceSet().createResource(diagramURI);
		final String diagramName = diagramURI.lastSegment();
		AbstractTransactionalCommand command = new AbstractTransactionalCommand(editingDomain,
				Messages.LogicDiagramEditorUtil_CreateDiagramCommandLabel, Collections.EMPTY_LIST) {
			protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				Flow model = createInitialModel();
				//行号的处理
				int lineNum = LineNumUtil.findAvailableBaseLineNum(WorkspaceSynchronizer.getFile(diagramResource));
				model.setBaseLineNum(lineNum);

				attachModelToResource(model, diagramResource);

				//添加初始化设置
				com.sunsheen.jfids.studio.logic.Start startNode = LogicFactory.eINSTANCE.createStart();
				startNode.setName("开始");
				startNode.setLineNum(0);
				model.getNodes().add(startNode);
				
				com.sunsheen.jfids.studio.logic.End endNode = LogicFactory.eINSTANCE.createEnd();
				endNode.setName("结束");
				endNode.setLineNum(-1);
				model.getNodes().add(endNode);
				
				
				com.sunsheen.jfids.studio.logic.Blank blankNode = LogicFactory.eINSTANCE.createBlank();
				blankNode.setName("运算");
				blankNode.setLineNum(1);
				model.getNodes().add(blankNode);
				
				com.sunsheen.jfids.studio.logic.Assignment assNode = LogicFactory.eINSTANCE.createAssignment();
				assNode.setName("模型");
				assNode.setLineNum(2);
				model.getNodes().add(assNode);
				
				//link
				startNode.getLink().add(blankNode);
				blankNode.getLink().add(assNode);
				assNode.getLink().add(endNode);

				Diagram diagram = ViewService.createDiagram(model, FlowEditPart.MODEL_ID, LogicDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);

				Node nodeStart = (Node) ViewService.createNode(diagram, startNode, LogicVisualIDRegistry.getType(StartEditPart.VISUAL_ID),
						LogicDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
				Bounds boundsStart = NotationFactory.eINSTANCE.createBounds();
				boundsStart.setX(100);
				boundsStart.setY(100);
				nodeStart.setLayoutConstraint(boundsStart);

				Node nodeEnd = (Node) ViewService.createNode(diagram, endNode, LogicVisualIDRegistry.getType(EndEditPart.VISUAL_ID),
						LogicDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
				Bounds boundsEnd = NotationFactory.eINSTANCE.createBounds();
				boundsEnd.setX(500);
				boundsEnd.setY(100);
				nodeEnd.setLayoutConstraint(boundsEnd);
				
				Node nodeBlank = ViewService.createNode(diagram, blankNode, LogicVisualIDRegistry.getType(BlankEditPart.VISUAL_ID),
						LogicDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
				Bounds boundsBlank = NotationFactory.eINSTANCE.createBounds();
				boundsBlank.setX(300);
				boundsBlank.setY(100);
				nodeBlank.setLayoutConstraint(boundsBlank);
				
				Node nodeAss = ViewService.createNode(diagram, assNode, LogicVisualIDRegistry.getType(AssignmentEditPart.VISUAL_ID),
						LogicDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
				Bounds boundsAssignment = NotationFactory.eINSTANCE.createBounds();
				boundsAssignment.setX(300);
				boundsAssignment.setY(200);
				nodeAss.setLayoutConstraint(boundsAssignment);
				
				//end of modify

				if (diagram != null) {
					diagramResource.getContents().add(diagram);
					diagram.setName(diagramName);
					diagram.setElement(model);
				}

				try {

					diagramResource.save(com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorUtil.getSaveOptions());
				} catch (IOException e) {

					LogicDiagramEditorPlugin.getInstance().logError("Unable to store model and diagram resources", e); //$NON-NLS-1$
				}
				return CommandResult.newOKCommandResult();
			}
		};
		try {
			OperationHistoryFactory.getOperationHistory().execute(command, new SubProgressMonitor(progressMonitor, 1),null);
			
			
		} catch (ExecutionException e) {
			LogicDiagramEditorPlugin.getInstance().logError("Unable to create model and diagram", e); //$NON-NLS-1$
		}

		setCharset(WorkspaceSynchronizer.getFile(diagramResource));
		return diagramResource;
	}

	/**
	 * Create a new instance of domain element associated with canvas.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	private static Flow createInitialModel() {
		//		return LogicFactory.eINSTANCE.createFlow();
		Flow logic = LogicFactory.eINSTANCE.createFlow();
		String timeStr = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
		String author = System.getProperty("user.name");
		logic.setCreatetime(timeStr);
		logic.setAuthor(author);
		return logic;
	}

	/**
	 * Store model element in the resource.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static void attachModelToResource(Flow model, Resource resource) {
		resource.getContents().add(model);
	}

	/**
	 * @generated
	 */
	public static void selectElementsInDiagram(IDiagramWorkbenchPart diagramPart, List<EditPart> editParts) {
		diagramPart.getDiagramGraphicalViewer().deselectAll();

		EditPart firstPrimary = null;
		for (EditPart nextPart : editParts) {
			diagramPart.getDiagramGraphicalViewer().appendSelection(nextPart);
			if (firstPrimary == null && nextPart instanceof IPrimaryEditPart) {
				firstPrimary = nextPart;
			}
		}

		if (!editParts.isEmpty()) {
			diagramPart.getDiagramGraphicalViewer().reveal(
					firstPrimary != null ? firstPrimary : (EditPart) editParts.get(0));
		}
	}

	/**
	 * @generated
	 */
	private static int findElementsInDiagramByID(DiagramEditPart diagramPart, EObject element,
			List<EditPart> editPartCollector) {
		IDiagramGraphicalViewer viewer = (IDiagramGraphicalViewer) diagramPart.getViewer();
		final int intialNumOfEditParts = editPartCollector.size();

		if (element instanceof View) { // support notation element lookup
			EditPart editPart = (EditPart) viewer.getEditPartRegistry().get(element);
			if (editPart != null) {
				editPartCollector.add(editPart);
				return 1;
			}
		}

		String elementID = EMFCoreUtil.getProxyID(element);
		@SuppressWarnings("unchecked")
		List<EditPart> associatedParts = viewer.findEditPartsForElement(elementID, IGraphicalEditPart.class);
		// perform the possible hierarchy disjoint -> take the top-most parts only
		for (EditPart nextPart : associatedParts) {
			EditPart parentPart = nextPart.getParent();
			while (parentPart != null && !associatedParts.contains(parentPart)) {
				parentPart = parentPart.getParent();
			}
			if (parentPart == null) {
				editPartCollector.add(nextPart);
			}
		}

		if (intialNumOfEditParts == editPartCollector.size()) {
			if (!associatedParts.isEmpty()) {
				editPartCollector.add(associatedParts.get(0));
			} else {
				if (element.eContainer() != null) {
					return findElementsInDiagramByID(diagramPart, element.eContainer(), editPartCollector);
				}
			}
		}
		return editPartCollector.size() - intialNumOfEditParts;
	}

	/**
	 * @generated
	 */
	public static View findView(DiagramEditPart diagramEditPart, EObject targetElement,
			LazyElement2ViewMap lazyElement2ViewMap) {
		boolean hasStructuralURI = false;
		if (targetElement.eResource() instanceof XMLResource) {
			hasStructuralURI = ((XMLResource) targetElement.eResource()).getID(targetElement) == null;
		}

		View view = null;
		LinkedList<EditPart> editPartHolder = new LinkedList<EditPart>();
		if (hasStructuralURI && !lazyElement2ViewMap.getElement2ViewMap().isEmpty()) {
			view = lazyElement2ViewMap.getElement2ViewMap().get(targetElement);
		} else if (findElementsInDiagramByID(diagramEditPart, targetElement, editPartHolder) > 0) {
			EditPart editPart = editPartHolder.get(0);
			view = editPart.getModel() instanceof View ? (View) editPart.getModel() : null;
		}

		return (view == null) ? diagramEditPart.getDiagramView() : view;
	}

	/**
	 * XXX This is quite suspicious code (especially editPartTmpHolder) and likely to be removed soon
	 * @generated
	 */
	public static class LazyElement2ViewMap {
		/**
		 * @generated
		 */
		private Map<EObject, View> element2ViewMap;

		/**
		 * @generated
		 */
		private View scope;

		/**
		 * @generated
		 */
		private Set<? extends EObject> elementSet;

		/**
		 * @generated
		 */
		public LazyElement2ViewMap(View scope, Set<? extends EObject> elements) {
			this.scope = scope;
			this.elementSet = elements;
		}

		/**
		 * @generated
		 */
		public final Map<EObject, View> getElement2ViewMap() {
			if (element2ViewMap == null) {
				element2ViewMap = new HashMap<EObject, View>();
				// map possible notation elements to itself as these can't be found by view.getElement()
				for (EObject element : elementSet) {
					if (element instanceof View) {
						View view = (View) element;
						if (view.getDiagram() == scope.getDiagram()) {
							element2ViewMap.put(element, view); // take only those that part of our diagram
						}
					}
				}

				buildElement2ViewMap(scope, element2ViewMap, elementSet);
			}
			return element2ViewMap;
		}

		/**
		 * @generated
		 */
		private static boolean buildElement2ViewMap(View parentView, Map<EObject, View> element2ViewMap,
				Set<? extends EObject> elements) {
			if (elements.size() == element2ViewMap.size()) {
				return true;
			}

			if (parentView.isSetElement() && !element2ViewMap.containsKey(parentView.getElement())
					&& elements.contains(parentView.getElement())) {
				element2ViewMap.put(parentView.getElement(), parentView);
				if (elements.size() == element2ViewMap.size()) {
					return true;
				}
			}
			boolean complete = false;
			for (Iterator<?> it = parentView.getChildren().iterator(); it.hasNext() && !complete;) {
				complete = buildElement2ViewMap((View) it.next(), element2ViewMap, elements);
			}
			for (Iterator<?> it = parentView.getSourceEdges().iterator(); it.hasNext() && !complete;) {
				complete = buildElement2ViewMap((View) it.next(), element2ViewMap, elements);
			}
			for (Iterator<?> it = parentView.getTargetEdges().iterator(); it.hasNext() && !complete;) {
				complete = buildElement2ViewMap((View) it.next(), element2ViewMap, elements);
			}
			return complete;
		}
	} //LazyElement2ViewMap	

}
