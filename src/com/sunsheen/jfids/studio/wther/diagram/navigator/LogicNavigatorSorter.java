package com.sunsheen.jfids.studio.wther.diagram.navigator;

import org.eclipse.jface.viewers.ViewerSorter;

import com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry;

/**
 * @generated
 */
public class LogicNavigatorSorter extends ViewerSorter {

	/**
	 * @generated
	 */
	private static final int GROUP_CATEGORY = 4009;

	/**
	 * @generated
	 */
	public int category(Object element) {
		if (element instanceof LogicNavigatorItem) {
			LogicNavigatorItem item = (LogicNavigatorItem) element;
			return LogicVisualIDRegistry.getVisualID(item.getView());
		}
		return GROUP_CATEGORY;
	}

}
