package com.sunsheen.jfids.studio.wther.diagram.part;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramDocument;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.part.FileEditorInput;

import com.sunsheen.jfids.studio.wther.diagram.edit.util.FileNameValidator;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicMarkerNavigationProvider;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicValidationProvider;

/**
 * @generated
 */
public class ValidateAction extends Action {

	/**
	 * @generated
	 */
	private IWorkbenchPage page;

	/**
	 * @generated
	 */
	public ValidateAction(IWorkbenchPage page) {
		setText(Messages.ValidateActionMessage);
		this.page = page;
	}

	/**
	 * @generated
	 */
	public void run() {
		IWorkbenchPart workbenchPart = page.getActivePart();
		if (workbenchPart instanceof IDiagramWorkbenchPart) {
			final IDiagramWorkbenchPart part = (IDiagramWorkbenchPart) workbenchPart;
			try {
				new WorkspaceModifyDelegatingOperation(new IRunnableWithProgress() {

					public void run(IProgressMonitor monitor) throws InterruptedException, InvocationTargetException {
						runValidation(part.getDiagramEditPart(), part.getDiagram());
					}
				}).run(new NullProgressMonitor());
			} catch (Exception e) {
				LogicDiagramEditorPlugin.getInstance().logError("Validation action failed", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * 验证文件是否有错
	 * 
	 * @param file
	 */
	public static void runValidation(IFile file) {
		final IFile gmfFile = file;
		// 验证文件名是否合法
		if (!FileNameValidator.validateFileName(file)) {
			return;
		}

		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages();
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					if (page != null) {
						try {
							final IEditorInput newInput = new FileEditorInput(gmfFile);
							LogicDocumentProvider p = new LogicDocumentProvider();
							DiagramDocument document = (DiagramDocument) p.createDocument(newInput);
							IEditorReference[] editorReference = page.getEditorReferences();
							IEditorPart editorPart = null;
							for (int i = 0; i < editorReference.length; i++) {
								IEditorInput editorInput1 = editorReference[i].getEditorInput();
								if (editorInput1 instanceof IFileEditorInput) {
									IFileEditorInput fi = (IFileEditorInput) editorInput1;
									if (fi.getFile().equals(gmfFile)) {
										editorPart = editorReference[i].getEditor(true);
										break;
									}
								}
							}
							if (editorPart != null && editorPart instanceof IDiagramWorkbenchPart) {// 编辑器打开
								DiagramEditPart diagramEditPart = ((IDiagramWorkbenchPart) editorPart)
										.getDiagramEditPart();
								EObject root = document.getDiagram().getElement();
								if (gmfFile != null) {
									LogicMarkerNavigationProvider.deleteMarkers(gmfFile);
								}
								Diagnostic diagnostic = Diagnostic.OK_INSTANCE;
								if (root != null) {
									diagnostic = new Diagnostician() {
										public String getObjectLabel(EObject eObject) {
											return EMFCoreUtil.getQualifiedName(eObject, true);
										}
									}.validate(root);
								}
								createMarkers(gmfFile, diagnostic, diagramEditPart);
								IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance()
										.newValidator(EvaluationMode.BATCH);
								validator.setIncludeLiveConstraints(true);
								if (root != null) {
									IStatus status = validator.validate(root);
									createMarkers(gmfFile, status, diagramEditPart);
								}
							} else {// 编辑器没有打开
								EObject root = document.getDiagram().getElement();
								IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance()
										.newValidator(EvaluationMode.BATCH);
								validator.setIncludeLiveConstraints(true);
								if (root != null) {
									IStatus status = validator.validate(root);
									createMarkersWithNoEditor(gmfFile, status);
								}
							}
						} catch (PartInitException e) {
							e.printStackTrace();
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	private static void createMarkersWithNoEditor(IFile target, IStatus validationStatus) {
		if (validationStatus.isOK()) {
			return;
		}
		final IStatus rootStatus = validationStatus;
		List allStatuses = new ArrayList();
		LogicDiagramEditorUtil.LazyElement2ViewMap element2ViewMap = new LogicDiagramEditorUtil.LazyElement2ViewMap(
				null, collectTargetElements(rootStatus, new HashSet<EObject>(), allStatuses));
		for (Iterator it = allStatuses.iterator(); it.hasNext();) {
			IConstraintStatus nextStatus = (IConstraintStatus) it.next();
			addMarker(null, target, null, EMFCoreUtil.getQualifiedName(nextStatus.getTarget(), true),
					nextStatus.getMessage(), nextStatus.getSeverity());
		}
	}

	/**
	 * @generated
	 */
	public static void runValidation(View view) {
		try {
			if (LogicDiagramEditorUtil.openDiagram(view.eResource())) {
				IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				if (editorPart instanceof IDiagramWorkbenchPart) {
					runValidation(((IDiagramWorkbenchPart) editorPart).getDiagramEditPart(), view);
				} else {
					runNonUIValidation(view);
				}
			}
		} catch (Exception e) {
			LogicDiagramEditorPlugin.getInstance().logError("Validation action failed", e); //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	public static void runNonUIValidation(View view) {
		DiagramEditPart diagramEditPart = OffscreenEditPartFactory.getInstance().createDiagramEditPart(
				view.getDiagram());
		runValidation(diagramEditPart, view);
	}

	/**
	 * @generated
	 */
	public static void runValidation(DiagramEditPart diagramEditPart, View view) {
		final DiagramEditPart fpart = diagramEditPart;
		final View fview = view;
		TransactionalEditingDomain txDomain = TransactionUtil.getEditingDomain(view);
		LogicValidationProvider.runWithConstraints(txDomain, new Runnable() {

			public void run() {
				validate(fpart, fview);
			}
		});
	}

	/**
	 * @generated
	 */
	private static Diagnostic runEMFValidator(View target) {
		if (target.isSetElement() && target.getElement() != null) {
			return new Diagnostician() {

				public String getObjectLabel(EObject eObject) {
					return EMFCoreUtil.getQualifiedName(eObject, true);
				}
			}.validate(target.getElement());
		}
		return Diagnostic.OK_INSTANCE;
	}

	/**
	 * @generated
	 */
	private static void validate(DiagramEditPart diagramEditPart, View view) {
		IFile target = view.eResource() != null ? WorkspaceSynchronizer.getFile(view.eResource()) : null;
		if (target != null) {
			LogicMarkerNavigationProvider.deleteMarkers(target);
		}
		Diagnostic diagnostic = runEMFValidator(view);
		createMarkers(target, diagnostic, diagramEditPart);
		IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(
				EvaluationMode.BATCH);
		validator.setIncludeLiveConstraints(true);
		if (view.isSetElement() && view.getElement() != null) {
			IStatus status = validator.validate(view.getElement());
			createMarkers(target, status, diagramEditPart);
		}
	}

	/**
	 * @generated
	 */
	private static void createMarkers(IFile target, IStatus validationStatus, DiagramEditPart diagramEditPart) {
		if (validationStatus.isOK()) {
			return;
		}
		final IStatus rootStatus = validationStatus;
		List allStatuses = new ArrayList();
		LogicDiagramEditorUtil.LazyElement2ViewMap element2ViewMap = new LogicDiagramEditorUtil.LazyElement2ViewMap(
				diagramEditPart.getDiagramView(),
				collectTargetElements(rootStatus, new HashSet<EObject>(), allStatuses));
		for (Iterator it = allStatuses.iterator(); it.hasNext();) {
			IConstraintStatus nextStatus = (IConstraintStatus) it.next();
			View view = LogicDiagramEditorUtil.findView(diagramEditPart, nextStatus.getTarget(), element2ViewMap);
			addMarker(diagramEditPart.getViewer(), target, view.eResource().getURIFragment(view),
					EMFCoreUtil.getQualifiedName(nextStatus.getTarget(), true), nextStatus.getMessage(),
					nextStatus.getSeverity());
		}
	}

	/**
	 * @generated
	 */
	private static void createMarkers(IFile target, Diagnostic emfValidationStatus, DiagramEditPart diagramEditPart) {
		if (emfValidationStatus.getSeverity() == Diagnostic.OK) {
			return;
		}
		final Diagnostic rootStatus = emfValidationStatus;
		List allDiagnostics = new ArrayList();
		LogicDiagramEditorUtil.LazyElement2ViewMap element2ViewMap = new LogicDiagramEditorUtil.LazyElement2ViewMap(
				diagramEditPart.getDiagramView(), collectTargetElements(rootStatus, new HashSet<EObject>(),
						allDiagnostics));
		for (Iterator it = emfValidationStatus.getChildren().iterator(); it.hasNext();) {
			Diagnostic nextDiagnostic = (Diagnostic) it.next();
			List data = nextDiagnostic.getData();
			if (data != null && !data.isEmpty() && data.get(0) instanceof EObject) {
				EObject element = (EObject) data.get(0);
				View view = LogicDiagramEditorUtil.findView(diagramEditPart, element, element2ViewMap);
				addMarker(diagramEditPart.getViewer(), target, view.eResource().getURIFragment(view),
						EMFCoreUtil.getQualifiedName(element, true), nextDiagnostic.getMessage(),
						diagnosticToStatusSeverity(nextDiagnostic.getSeverity()));
			}
		}
	}

	/**
	 * @generated
	 */
	private static void addMarker(EditPartViewer viewer, IFile target, String elementId, String location,
			String message, int statusSeverity) {
		if (target == null) {
			return;
		}
		LogicMarkerNavigationProvider.addMarker(target, elementId, location, message, statusSeverity);
	}

	/**
	 * @generated
	 */
	private static int diagnosticToStatusSeverity(int diagnosticSeverity) {
		if (diagnosticSeverity == Diagnostic.OK) {
			return IStatus.OK;
		} else if (diagnosticSeverity == Diagnostic.INFO) {
			return IStatus.INFO;
		} else if (diagnosticSeverity == Diagnostic.WARNING) {
			return IStatus.WARNING;
		} else if (diagnosticSeverity == Diagnostic.ERROR || diagnosticSeverity == Diagnostic.CANCEL) {
			return IStatus.ERROR;
		}
		return IStatus.INFO;
	}

	/**
	 * @generated
	 */
	private static Set<EObject> collectTargetElements(IStatus status, Set<EObject> targetElementCollector,
			List allConstraintStatuses) {
		if (status instanceof IConstraintStatus) {
			targetElementCollector.add(((IConstraintStatus) status).getTarget());
			allConstraintStatuses.add(status);
		}
		if (status.isMultiStatus()) {
			IStatus[] children = status.getChildren();
			for (int i = 0; i < children.length; i++) {
				collectTargetElements(children[i], targetElementCollector, allConstraintStatuses);
			}
		}
		return targetElementCollector;
	}

	/**
	 * @generated
	 */
	private static Set<EObject> collectTargetElements(Diagnostic diagnostic, Set<EObject> targetElementCollector,
			List allDiagnostics) {
		List data = diagnostic.getData();
		EObject target = null;
		if (data != null && !data.isEmpty() && data.get(0) instanceof EObject) {
			target = (EObject) data.get(0);
			targetElementCollector.add(target);
			allDiagnostics.add(diagnostic);
		}
		if (diagnostic.getChildren() != null && !diagnostic.getChildren().isEmpty()) {
			for (Iterator it = diagnostic.getChildren().iterator(); it.hasNext();) {
				collectTargetElements((Diagnostic) it.next(), targetElementCollector, allDiagnostics);
			}
		}
		return targetElementCollector;
	}
}
