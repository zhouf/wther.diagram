package com.sunsheen.jfids.studio.wther.diagram.preferences;

import org.eclipse.gmf.runtime.diagram.ui.preferences.DiagramsPreferencePage;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;

import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;

/**
 * @generated
 */
public class DiagramGeneralPreferencePage extends DiagramsPreferencePage {

	/**
	 * @generated
	 */
	public DiagramGeneralPreferencePage() {
		setPreferenceStore(LogicDiagramEditorPlugin.getInstance().getPreferenceStore());
	}
	
	@Override
	protected void createFieldEditors() {
		super.createFieldEditors();
		
		//添加自定义首选项
		addField(new BooleanFieldEditor(PreferenceConstants.P_BOOLEAN_AUTO_NODETIP, "自动弹出节点提示信息", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_BOOLEAN_AUTO_PROPERTY, "放入自定义节点时自动弹出属性", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_STRING_CUSTOM_NODE_CODE, "自定义节点初始代码", getFieldEditorParent()));
		
//		addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH, "&Directory preference:", getFieldEditorParent()));
//		addField(new BooleanFieldEditor(PreferenceConstants.P_BOOLEAN, "&An example of a boolean preference", getFieldEditorParent()));
//		addField(new RadioGroupFieldEditor(PreferenceConstants.P_CHOICE, "An example of a multiple-choice preference", 1, new String[][] {
//				{ "&Choice 1", "choice1" }, { "C&hoice 2", "choice2" } }, getFieldEditorParent()));
//		addField(new StringFieldEditor(PreferenceConstants.P_STRING, "A &text preference:", getFieldEditorParent()));
	}
}
