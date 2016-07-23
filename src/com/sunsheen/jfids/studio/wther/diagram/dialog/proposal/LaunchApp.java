package com.sunsheen.jfids.studio.wther.diagram.dialog.proposal;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.sunsheen.jfids.studio.dialog.proposal.ISSProPosalPrefixFilter;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalProvider;
import com.sunsheen.jfids.studio.dialog.proposal.SSExpressionProPosalPrefixFilter;


public class LaunchApp {
	protected Shell shell;
	private Text nameT;
	private Combo cityC;
	private Text remarksT;
	private Text remarksT2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LaunchApp window = new LaunchApp();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() {
		final Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	protected void createContents() {
		shell = new Shell();
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shell.setLayout(gridLayout);
		shell.setSize(426, 322);
		shell.setText("Field Assist");
		final Label nameL = new Label(shell, SWT.NONE);
		nameL.setText("简单过滤1：");
		nameT = new Text(shell, SWT.BORDER);
		nameT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final Label cityL = new Label(shell, SWT.NONE);
		cityL.setText("输入过滤2");
		cityC = new Combo(shell, SWT.NONE);
		cityC.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final Label remarksL = new Label(shell, SWT.NONE);
		remarksL.setText("单个变量过滤");
		remarksT = new Text(shell, SWT.BORDER);
		remarksT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		remarksT.setText("one");

		final Label remarksL2 = new Label(shell, SWT.NONE);
		remarksL2.setText("变量表达式过滤");
		remarksT2 = new Text(shell, SWT.BORDER);
		remarksT2
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		remarksT2.setText("one123+oa");
		//
		Dialog.applyDialogFont(this.shell);
		//
		this.addNameTextFieldAssist();
		this.addCityComboFieldAssist();
		this.addRemarksTextFieldAssist();
		this.addRemarksTextFieldAssist2();
	}

	/**
	 * 
	 * 给名称Text添加自动完成功能
	 */
	private void addNameTextFieldAssist() {
		// 让text可以进行代码提示. 提示内容为: "aa", "BB", "无敌".
		// 注意: 不区分大小写. [如: 输入'b', 内容中会出现"BB"]
		new AutoCompleteField(nameT, new TextContentAdapter(), new String[] {
				"aa", "BB", "无敌" });
	}

	/**
	 * 
	 * 给城市Combo添加自动完成功能
	 */
	private void addCityComboFieldAssist() {
		// 让combo可以代码提示. 提示内容为: "BeiJing", "南京", "北京"
		new AutoCompleteField(cityC, new ComboContentAdapter(), new String[] {
				"BeiJing", "南京", "北京" });
	}

	/**
	 * 
	 * 表达式过滤
	 */
	private void addRemarksTextFieldAssist2() {
		KeyStroke keyStroke = null; // null 表示不接受快捷键
		try {
			keyStroke = KeyStroke.getInstance("Ctrl+1"); // 在text上按Ctrl+1弹出popup的shell.
		} catch (Exception e) {
			e.printStackTrace();
		}
		SSContentProposalProvider scpp = new SSContentProposalProvider(
				new String[] { "one123", "on155", "on2234", "oaab3", "oaab4",
						"obab5", "obtt6", "octt7" });
		scpp.setPrefixFilter(new SSExpressionProPosalPrefixFilter());
		SSContentProposalAdapter adapter = new SSContentProposalAdapter(
				remarksT2, new TextContentAdapter(), scpp, keyStroke,
				new char[] { '.', ' ' });
		adapter.setAutoActivationDelay(200); // 延时200ms
		adapter.setPropagateKeys(true); // 如果用户的输入值在内容列表中[比如输入'o',而内容中有'one'],则弹出popup的shell
		adapter.setFilterStyle(ContentProposalAdapter.FILTER_CUMULATIVE); // 用户同步输入的内容也过滤列表[如:用户输入'o',则弹出popup的shell中的内容列表被过滤,其中都是'o'开头的,
																			// 再输入一个'n',
																			// 则内容列表中被过滤,只有以'on'开头的]
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT); // 回写插入
	}

	/**
	 * 
	 * 单个变量过滤
	 */
	private void addRemarksTextFieldAssist() {
		KeyStroke keyStroke = null; // null 表示不接受快捷键
		try {
			keyStroke = KeyStroke.getInstance("Ctrl+1"); // 在text上按Ctrl+1弹出popup的shell.
		} catch (Exception e) {
			e.printStackTrace();
		}
		SSContentProposalProvider scpp = new SSContentProposalProvider(
				new String[] { "one123", "on155", "on2234", "oaab3", "oaab4",
						"obab5", "obtt6", "octt7" });
		scpp.setPrefixFilter(new ISSProPosalPrefixFilter() {
			public String getFilterText(String content, int position) {
				return content;
			}
		});
		SSContentProposalAdapter adapter = new SSContentProposalAdapter(
				remarksT, new TextContentAdapter(), scpp, keyStroke,
				new char[] { '.', ' ' });
		adapter.setAutoActivationDelay(200); // 延时200ms
		adapter.setPropagateKeys(true); // 如果用户的输入值在内容列表中[比如输入'o',而内容中有'one'],则弹出popup的shell
		adapter.setFilterStyle(ContentProposalAdapter.FILTER_CUMULATIVE); // 用户同步输入的内容也过滤列表[如:用户输入'o',则弹出popup的shell中的内容列表被过滤,其中都是'o'开头的,
																			// 再输入一个'n',
																			// 则内容列表中被过滤,只有以'on'开头的]
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT); // 回写插入
	}
}
