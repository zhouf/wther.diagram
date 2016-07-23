package com.sunsheen.jfids.studio.wther.diagram.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;

import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;

/**
 * @generated
 */
public class DiagramPreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * @generated NOT
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = getPreferenceStore();
		DiagramGeneralPreferencePage.initDefaults(store);
		DiagramAppearancePreferencePage.initDefaults(store);
		DiagramConnectionsPreferencePage.initDefaults(store);
		DiagramPrintingPreferencePage.initDefaults(store);
		DiagramRulersAndGridPreferencePage.initDefaults(store);

		store.setDefault(IPreferenceConstants.PREF_SNAP_TO_GEOMETRY, true);
		
		store.setDefault(PreferenceConstants.P_BOOLEAN_AUTO_NODETIP, true);
		store.setDefault(PreferenceConstants.P_BOOLEAN_AUTO_PROPERTY, true);
		store.setDefault(PreferenceConstants.P_STRING_CUSTOM_NODE_CODE, "System.out.println(\"\");");
	}

	/**
	 * @generated
	 */
	protected IPreferenceStore getPreferenceStore() {
		return LogicDiagramEditorPlugin.getInstance().getPreferenceStore();
	}
}
