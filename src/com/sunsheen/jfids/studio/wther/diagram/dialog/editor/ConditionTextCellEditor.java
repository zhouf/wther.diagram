package com.sunsheen.jfids.studio.wther.diagram.dialog.editor;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Text;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.EditorActivationEvent;
import com.sunsheen.jfids.studio.dialog.proposal.MyContentProposalProvider;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;
import com.sunsheen.jfids.studio.wther.diagram.dialog.proposal.VarLabelProvider;

/**
 * 这是赋值对话框中第一列，变量名的一个编辑器，用于判断输入的变量是否是已定义的变量 以确定复选项是否选中
 * 
 * @author zhouf
 * 
 */
public class ConditionTextCellEditor extends AbstractTextCellEditor {

	IContentProposalProvider contentProvider = null;
	String[] initProposalArray = null;

	public ConditionTextCellEditor(AgileGrid agileGrid) {
		this(agileGrid, SWT.SINGLE);
	}

	public ConditionTextCellEditor(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
		initProposalArray = (String[]) this.getParams().get("proposalData");
		contentProvider = new MyContentProposalProvider(initProposalArray);
	}

	@Override
	public void activate(EditorActivationEvent activationEvent) {
		initAdapter(text);
		super.activate(activationEvent);
	}

	@Override
	protected void onKeyPressed(KeyEvent keyEvent) {
		// 屏蔽掉按键响应，否则无法响应弹出框的键盘响应
		// Log.debug("ConditionTextCellEditor onKeyPressed()-> keyEvent.keyCode:"
		// + keyEvent.keyCode);
		// Log.debug("keyEvent.stateMask:" + keyEvent.stateMask);
		// Log.debug("keyEvent.character:" + keyEvent.character);
		if (keyEvent.character == '.') {
			// 如果是点.则提取出相邻变量
			String getStr = praseVar(text);
			adapter.setContentProposalProvider(new MyContentProposalProvider(TextEditorUtil.getMethods(getStr,initProposalArray)));
		} else if (keyEvent.character == ' ' || keyEvent.character == '/') {
			adapter.setContentProposalProvider(contentProvider);
		}
	}


	public void initAdapter(Text widget) {
		KeyStroke keyStroke = null; // null 表示不接受快捷键
		try {
			keyStroke = KeyStroke.getInstance("Alt+/"); // 在text上按Ctrl+1弹出popup的shell.
		} catch (Exception e) {
			e.printStackTrace();
		}

		adapter = new SSContentProposalAdapter(widget, new TextContentAdapter(), contentProvider, keyStroke, new char[] { '.' });

		adapter.setLabelProvider(new VarLabelProvider());
		adapter.setAutoActivationDelay(200); // 延时200ms
		adapter.setPropagateKeys(true); // 如果用户的输入值在内容列表中[比如输入'o',而内容中有'one'],则弹出popup的shell
		adapter.setFilterStyle(ContentProposalAdapter.FILTER_CUMULATIVE); // 用户同步输入的内容也过滤列表[如:用户输入'o',则弹出popup的shell中的内容列表被过滤,其中都是'o'开头的,
																			// 再输入一个'n',
																			// 则内容列表中被过滤,只有以'on'开头的]
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT); // 回写插入
	}

	public String[] getPopupData() {
		if (initProposalArray != null) {
			return initProposalArray;
		} else {
			return new String[] {};
		}
	}

	
}
