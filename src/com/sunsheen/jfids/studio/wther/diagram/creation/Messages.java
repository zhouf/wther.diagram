package com.sunsheen.jfids.studio.wther.diagram.creation;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getName();
	public static String Actions_Creation_NewBixAction_text;
	public static String Actions_Creation_NewWeatherAppAction_text;
	public static String Actions_Creation_NewWeatherPagexAction_text;
	public static String Actions_Creation_NewPixAction_text;
	public static String Actions_Creation_NewPixCompositeAction_text;
	public static String Actions_Creation_NewWpdAction_text;
	public static String Actions_Creation_NewWpdFolderAction_text;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
