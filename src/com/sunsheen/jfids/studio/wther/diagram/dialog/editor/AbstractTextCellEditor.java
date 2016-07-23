package com.sunsheen.jfids.studio.wther.diagram.dialog.editor;


import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.widgets.Text;

import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.editors.TextCellEditor;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;

/**
 * 这是一个用于实现变量弹出提示框的editor,作了基本的提示设置，需要继承以实现部分细节
 * 
 * @author zhouf
 */
public abstract class AbstractTextCellEditor extends TextCellEditor {

	protected SSContentProposalAdapter adapter = null;

	public AbstractTextCellEditor(AgileGrid agileGrid) {
		this(agileGrid, SWT.SINGLE);
	}

	public AbstractTextCellEditor(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
	}

	@Override
	protected void onKeyPressed(KeyEvent keyEvent) {
		// 屏蔽掉按键响应，否则无法响应弹出框的键盘响应
		// super.onKeyPressed(keyEvent);
	}

	@Override
	protected void onTraverse(TraverseEvent e) {
		if (e.keyCode == SWT.ARROW_LEFT) {
			if (text.getCaretPosition() == 0 && text.getSelectionCount() == 0)
				super.onTraverse(e);
		} else if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
			// 屏蔽掉上下键的处理
			return;
		} else if (e.keyCode == SWT.ARROW_RIGHT) {
			if (text.getCaretPosition() == text.getText().length()
					&& text.getSelectionCount() == 0)
				super.onTraverse(e);
			// handle the event within the text widget!
		} else {
			if (e.detail == SWT.TRAVERSE_ESCAPE
					|| e.detail == SWT.TRAVERSE_RETURN) {
				e.doit = false;
			} else {
				super.onTraverse(e);
			}
		}
		return;
	}

	@Override
	protected boolean dependsOnExternalFocusListener() {
		return false;
	}

	@Override
	protected void focusLost() {
		if (!(this.adapter != null && this.adapter.isProposalPopupOpen())) {
			super.focusLost();
		}
		((SDialog) agileGrid.getParams().get("SDIALOG")).validate();
	}
	
	/**
	 * 从输入的字串中提取最近的变量，以显示变量相应的方法提示
	 * 
	 * @param inStr
	 *            文本框字串，如：name1+name2<var3
	 * @return 最右边的变量，如var3
	 */
	protected String praseVar(Text text) {
		int position = text.getCaretPosition();
		String s = "\\d+.\\d+|\\w+";
		Pattern pattern = Pattern.compile(s);
		Matcher ma = pattern.matcher(text.getText().substring(0, position));
		while (ma.find()) {
			if (ma.start() <= position && ma.end() >= position) {
				return ma.group();
			}
		}
		return "";
	}
	
	/**
	 * 返回对话框的参数对象
	 * @return
	 * @return Map<String,Object>
	 */
	public Map<String,Object> getParams(){
		return ((SDialog)agileGrid.getParams().get("SDIALOG")).getParams();
	}

}
