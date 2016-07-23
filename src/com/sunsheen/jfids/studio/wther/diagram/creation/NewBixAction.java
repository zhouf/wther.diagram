package com.sunsheen.jfids.studio.wther.diagram.creation;

import java.util.Set;

import org.eclipse.ui.PlatformUI;

import com.sunsheen.jfids.studio.run.ui.actions.HKNewAction;
import com.sunsheen.jfids.studio.run.ui.wizards.utils.MyWizardDialog;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicImageUtil;

/**
 * 创建业务逻辑动作
 * 
 * @author litao
 * 
 */
public class NewBixAction extends HKNewAction {

	private final static String text = Messages.Actions_Creation_NewBixAction_text;

	public NewBixAction() {
		super(text);
		this.setImageDescriptor(LogicImageUtil.getInstance().getDescriptor("/icons/obj16/bix.png")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		NewBixWizard wizard = new NewBixWizard();
		wizard.init(PlatformUI.getWorkbench(), selection);
		MyWizardDialog.createAndOpenDialog(shell, wizard);
	}

	@Override
	protected void fillShowNodes(Set<String> showNodes) {
		showNodes.add(TYPE_FOLDER_SRC_CHILD_BIX);
	}
}