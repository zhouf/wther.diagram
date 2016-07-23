package com.sunsheen.jfids.studio.wther.diagram.creation;

import java.util.Set;

import org.eclipse.ui.PlatformUI;

import com.sunsheen.jfids.studio.run.ui.actions.HKNewAction;
import com.sunsheen.jfids.studio.run.ui.wizards.utils.MyWizardDialog;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicImageUtil;

/**
 * 创建微应用
 */
public class NewWeatherAction extends HKNewAction {

	private final static String text = Messages.Actions_Creation_NewWeatherAppAction_text;

	public NewWeatherAction() {
		super(text);
		this.setImageDescriptor(LogicImageUtil.getInstance().getDescriptor("/icons/obj16/businesslogicdir.png")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		System.out.println("NewWeatherAction.run()->:");
		NewWeatherWizard wizard = new NewWeatherWizard();
		wizard.init(PlatformUI.getWorkbench(), selection);
		MyWizardDialog.createAndOpenDialog(shell, wizard);
	}

	@Override
	protected void fillShowNodes(Set<String> showNodes) {
		showNodes.add(TYPE_PACKAGE_BUSINESSCOMPONENT);
		showNodes.add(TYPE_BIXPACKAGE);
	}
}