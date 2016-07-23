package com.sunsheen.jfids.studio.wther.diagram.edit.tip;

import org.eclipse.gef.EditDomain;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.SelectionToolEx;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.sunsheen.jfids.studio.editor.editors.common.SWTResourceManager;
import com.sunsheen.jfids.studio.logic.impl.CallImpl;
import com.sunsheen.jfids.studio.logic.impl.EntityImpl;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
import com.sunsheen.jfids.studio.wther.diagram.preferences.PreferenceConstants;

public class ToolTipForActivityRender {

	static Shell tip = null;
	static EditDomain editDomain = null;
	static Display display = Display.getDefault();// PlatformUI.getWorkbench().getDisplay();

	private IPreferenceStore store = null;
	private boolean preferenceConfig = false;
	
	EntityImpl activity;
	StyledText text = null;

	public ToolTipForActivityRender() {
		store = LogicDiagramEditorPlugin.getInstance().getPreferenceStore();
	}

	public ToolTipForActivityRender(EntityImpl activity, EditDomain editDomain) {
		this();
		this.activity = activity;
		ToolTipForActivityRender.editDomain = editDomain;
	}

	public void renderTipTool(Shell shell) {
		if (editDomain.getActiveTool() instanceof SelectionToolEx) {
			tip = new Shell(display, SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
			tip.setLocation(Display.getDefault().getCursorLocation().x + 20,
					Display.getDefault().getCursorLocation().y + 20);
			// Point size = tip.computeSize (SWT.DEFAULT, SWT.DEFAULT);
			tip.setSize(300, 200);

			tip.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			GridLayout layout = new GridLayout();
			layout.marginWidth = 5;
			tip.setLayout(layout);

			// Button b = new Button(tip,SWT.NONE);
			// b.setText("hello!");
			text = new StyledText(tip, SWT.MULTI | SWT.WRAP);// |SWT.V_SCROLL//滚动条
			text.setLayoutData(new GridData(GridData.FILL_BOTH));
			text.setEditable(false);

			text.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
			text.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			text.setText(getTipText());
			text.addListener(SWT.MouseExit, textListener);
			text.addListener(SWT.MouseEnter, textListener);

			// 横线分栏符
			Label hor = new Label(tip, SWT.SEPARATOR | SWT.HORIZONTAL);
			hor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			// 提示
			Label label = new Label(tip, SWT.NONE);
			GridData labelGridData = new GridData(GridData.FILL_HORIZONTAL);
			label.setLayoutData(labelGridData);

			label.setFont(SWTResourceManager.getFont("", 8, SWT.ITALIC));
			label.setText("按 'F2'以获取焦点");
			label.setAlignment(SWT.RIGHT);
			label.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
			label.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));

			preferenceConfig = store.getBoolean(PreferenceConstants.P_BOOLEAN_AUTO_NODETIP);
			tip.setVisible(preferenceConfig);
		}
	}

	public static Shell getTip() {
		return tip;
	}

	/**
	 * 提供给外部调用，显示tip提示。
	 */
	public static void showTip() {
		if (tip != null && !tip.isDisposed()) {
			tip.setFocus();
		}
	}

	/**
	 * 提供给外部调用，关闭tip提示。
	 */
	public static void stopTip() {
		stopTip(false);
	}

	/**
	 * 提供给内部调用，关闭tip提示。
	 */
	public static void stopTip(Boolean isShowTipAble) {
		if (tip != null && !tip.isDisposed() && !isShowTipAble) {
			tip.dispose();
			// showTipAble = false;
		}
		tip = null;
	}

	private String getTipText() {
		StringBuffer sb = new StringBuffer();
		if (activity != null) {
			
			sb.append("节点名:\n\t");
			sb.append(activity.getName()).append("\n");
			
			// sb.append("ID: \n\t");
			// sb.append(activity.eResource().getURIFragment(activity)).append("\n");
			
			sb.append("内容:\n");
			String nodeContent = NodeContentTipGenerator.getTipContent(activity);
			sb.append(nodeContent);
			
			sb.append("描述:\n\t");
			String comment = activity.getComment();
			if (comment != null && comment.length() > 0) {
				int pos = comment.indexOf(":");
				if (pos >= 0) {
					sb.append(comment.substring(pos + 1));
				} else {
					sb.append(comment);
				}
			}
			if (activity instanceof CallImpl) {
				// 如果是调用节点，则显示TIP
				sb.append("\n");
				sb.append("说明:\n\t");
				String tip2 = ((CallImpl) activity).getTip();
				tip2 = (tip2 == null ? "" : tip2);
				// if(tip2.startsWith("NO tip set"))
				sb.append(tip2);
			}
		}
		return sb.toString();
	}


	/**
	 * 文本框监听器 MouseEnter() MouseExit()
	 */
	final Listener textListener = new Listener() {
		public void handleEvent(Event event) {
			StyledText text = (StyledText) event.widget;
			Shell shell = text.getShell();
			switch (event.type) {
			case SWT.MouseEnter:
				text.setFocus();
				break;

			case SWT.MouseExit:
				shell.dispose();
				break;
			}
		}
	};
}
