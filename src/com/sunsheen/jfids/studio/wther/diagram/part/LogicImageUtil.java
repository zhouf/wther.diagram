package com.sunsheen.jfids.studio.wther.diagram.part;

import com.sunsheen.jfids.studio.dialog.image.AImageUtil;
import com.sunsheen.jfids.studio.dialog.image.IImageUtil;

/**
 * Image公共类
 * 
 * @author Administrator
 * 
 */
public class LogicImageUtil extends AImageUtil {
	private final static LogicImageUtil iu = new LogicImageUtil();

	@Override
	public String getPluginId() {
		return LogicDiagramEditorPlugin.ID;
	}

	public static IImageUtil getInstance() {
		return iu;
	}
}
