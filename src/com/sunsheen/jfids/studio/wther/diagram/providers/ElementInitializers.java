package com.sunsheen.jfids.studio.wther.diagram.providers;

import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;

/**
 * @generated
 */
public class ElementInitializers {

	protected ElementInitializers() {
		// use #getInstance to access cached instance
	}

	/**
	 * @generated
	 */
	public static ElementInitializers getInstance() {
		ElementInitializers cached = LogicDiagramEditorPlugin.getInstance().getElementInitializers();
		if (cached == null) {
			LogicDiagramEditorPlugin.getInstance().setElementInitializers(cached = new ElementInitializers());
		}
		return cached;
	}
}
