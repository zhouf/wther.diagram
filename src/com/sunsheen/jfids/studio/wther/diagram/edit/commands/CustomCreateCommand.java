package com.sunsheen.jfids.studio.wther.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;

import com.sunsheen.jfids.studio.logic.Custom;
import com.sunsheen.jfids.studio.logic.Flow;
import com.sunsheen.jfids.studio.logic.LogicFactory;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
import com.sunsheen.jfids.studio.wther.diagram.preferences.PreferenceConstants;

/**
 * @generated
 */
public class CustomCreateCommand extends EditElementCommand {

	private EObject newObject;
	private IPreferenceStore store;

	/**
	 * @generated NOT
	 */
	public CustomCreateCommand(CreateElementRequest req) {
		super(req.getLabel(), null, req);
		newObject = req.getNewElement();
		store = LogicDiagramEditorPlugin.getInstance().getPreferenceStore();
	}

	/**
	 * FIXME: replace with setElementToEdit()
	 * @generated
	 */
	protected EObject getElementToEdit() {
		EObject container = ((CreateElementRequest) getRequest()).getContainer();
		if (container instanceof View) {
			container = ((View) container).getElement();
		}
		return container;
	}

	/**
	 * @generated
	 */
	public boolean canExecute() {
		return true;

	}

	/**
	 * @generated NOT
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		Custom newElement;

		if (newObject == null || !(newObject instanceof Custom)) {
			newElement = LogicFactory.eINSTANCE.createCustom();
			//创建时赋初值
			newElement.setName("自定义");
			
			//填入初始代码
			String initCode = store.getString(PreferenceConstants.P_STRING_CUSTOM_NODE_CODE);
			newElement.setExternal(initCode);
			
		} else {
			newElement = (Custom) newObject;
		}

		Flow owner = (Flow) getElementToEdit();
		//owner.getNodes().add(newElement);
		owner.appendNode(newElement);

		doConfigure(newElement, monitor, info);

		((CreateElementRequest) getRequest()).setNewElement(newElement);
		return CommandResult.newOKCommandResult(newElement);
	}

	/**
	 * @generated
	 */
	protected void doConfigure(Custom newElement, IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		IElementType elementType = ((CreateElementRequest) getRequest()).getElementType();
		ConfigureRequest configureRequest = new ConfigureRequest(getEditingDomain(), newElement, elementType);
		configureRequest.setClientContext(((CreateElementRequest) getRequest()).getClientContext());
		configureRequest.addParameters(getRequest().getParameters());
		ICommand configureCommand = elementType.getEditCommand(configureRequest);
		if (configureCommand != null && configureCommand.canExecute()) {
			configureCommand.execute(monitor, info);
		}
	}

}
