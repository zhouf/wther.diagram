package com.sunsheen.jfids.studio.wther.diagram.dialog.editor;


import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.Cell;
import com.sunsheen.jfids.studio.dialog.agilegrid.EditorActivationEvent;
import com.sunsheen.jfids.studio.dialog.proposal.IProposalRender;
import com.sunsheen.jfids.studio.dialog.proposal.MyContentProposalProvider;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;
import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.run.Constants;
import com.sunsheen.jfids.studio.run.core.extensionpoint.ResourcePromptManager;
import com.sunsheen.jfids.studio.run.core.resources.prompt.IResourcePrompt;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.TableHiddenType;
import com.sunsheen.jfids.studio.wther.diagram.dialog.listener.MouseActiveProposalListener;
import com.sunsheen.jfids.studio.wther.diagram.dialog.popup.ItemSelectDialog;
import com.sunsheen.jfids.studio.wther.diagram.dialog.popup.SqlidSelectDialog;
import com.sunsheen.jfids.studio.wther.diagram.dialog.proposal.ComponentInterfaceProposalRender;
import com.sunsheen.jfids.studio.wther.diagram.dialog.proposal.DefaultProposalRender;
import com.sunsheen.jfids.studio.wther.diagram.dialog.proposal.ReturnProposalRender;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.InputDataValidate;

/**
 * 这是一个对对话框中设置变量的单元格编辑器，包含变量自动提示
 * 
 * @author zhoufeng
 */

public class VarTextCellEditor extends AbstractTextCellEditor {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(VarTextCellEditor.class.getName());

	SSContentProposalAdapter adapter = null;
	String rowType = "";
	String dataName = "";
	String tipRender = "";

	private enum DialogType {
		NONE, DEFAULT_DLG, SERVICE_COMPONENT, SQLID
	}

	DialogType dialogType = DialogType.NONE;

	public VarTextCellEditor(AgileGrid agileGrid) {
		this(agileGrid, SWT.SINGLE);
	}

	public VarTextCellEditor(AgileGrid agileGrid, int style) {
		super(agileGrid, style);
		this.text.addMouseListener(new MouseActiveProposalListener());
	}

	@Override
	protected boolean dependsOnExternalFocusListener() {
		// 不启用外部监听
		return false;
	}


	@Override
	protected void onKeyPressed(KeyEvent keyEvent) {
		// 屏蔽掉key的响应，如果开启，则弹出提示框无法响应回车选中
		// super.onKeyPressed(keyEvent);

		// 对bean的对话框热键处理 Alt stateMask=65536
		if (SWT.ALT == keyEvent.stateMask && 47 == keyEvent.keyCode) {
			log.debug("VarTextCellEditor onKeyPressed()-> :Alt + /");
			if (dialogType == DialogType.DEFAULT_DLG) {
				// 如果是Alt+/ 激活对话框
				// Log.debug("VarTextCellEditor.onKeyPressed()-> keyEvent.keyLocation:"
				// + keyEvent.keyLocation);
				keyEvent.doit = false;
				ItemSelectDialog itemSelectDialog = new ItemSelectDialog(tipRender, text.getText());
				this.text.setText(itemSelectDialog.getRetVal());
			} else if (dialogType == DialogType.SERVICE_COMPONENT) {
				keyEvent.doit = false;
				IResourcePrompt prompt = ResourcePromptManager.getResourcePrompt(Constants.FILE_EXT_SIX);
				if (prompt != null) {
					String getComName = prompt.getComName();
					if (getComName != null) {
						this.text.setText(getComName);
					}
				}
			} else if (dialogType == DialogType.SQLID) {
				log.debug("VarTextCellEditor onKeyPressed()-> :DialogType.SQLID");
				keyEvent.doit = false;
				SqlidSelectDialog sqlDialog = new SqlidSelectDialog(text.getText());
				String getVal = sqlDialog.getRetVal();
				log.debug("VarTextCellEditor onKeyPressed()-> getVal:" + getVal);
				if (getVal != null) {
					this.text.setText(getVal);
					// 设置值类型为常量
					Cell thisCell = this.getAgileGrid().getFocusCell();
					this.getAgileGrid().setContentAt(thisCell.row, thisCell.column + 1, "常量");
				}
			}
			dialogType = DialogType.NONE;
			// 该变量当作焦点移出后验证数据的一个标志位
		}else if (keyEvent.keyCode == 46) {
			// . 被按下
			String getStr = praseVar(text);
			String[] varArray =  (String[]) this.getParams().get("proposalData");
			adapter.setContentProposalProvider(new MyContentProposalProvider(TextEditorUtil.getMethods(getStr, varArray)));
			adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT);
		}
	}


	@Override
	protected void focusLost() {

		if (this.adapter != null && !this.adapter.isProposalPopupOpen()) {
			// 验证数据
			agileGrid.applyEditorValue();

			if ("Date".equalsIgnoreCase(rowType) || "".equalsIgnoreCase(rowType)) {
				boolean dateFormatOk = InputDataValidate.checkDateStrValidate(text.getText());
				if (dateFormatOk) {
					// Log.debug("VarTextCellEditor.focusLost()-> 日期验证通过：" +
					// text.getText());
				} else {
					Log.error("VarTextCellEditor.focusLost()-> 日期格式不正确：" + text.getText());
				}
			}
			super.focusLost();
			//((SDialog) agileGrid.getParams().get("SDIALOG")).validate();
		}
	}

	@Override
	public void activate(EditorActivationEvent activationEvent) {
		super.activate(activationEvent);
		String varCatagory = (String) this.agileGrid.getContentAt(cell.row, 0);// 变量的分类，参数or返回
		if (cell != null) {
			// 获取该行数据类型
			rowType = (String) this.agileGrid.getContentAt(cell.row, 1);
			dataName = (String) this.agileGrid.getContentAt(cell.row, 2);
			tipRender = (String) this.agileGrid.getContentAt(cell.row, TableHiddenType.TIP_RENDER);
		}
		rowType = (rowType == null ? "" : rowType.trim());
		tipRender = (tipRender == null ? "" : tipRender.trim());

		if (tipRender.toLowerCase().endsWith(".xml")) {
			dialogType = DialogType.DEFAULT_DLG;
		} else if ("component".equalsIgnoreCase(tipRender) || ("String".equalsIgnoreCase(rowType) && "serviceName".equalsIgnoreCase(dataName))) {
			dialogType = DialogType.SERVICE_COMPONENT;
		} else if ("comp_interface".equalsIgnoreCase(tipRender) || ("String".equalsIgnoreCase(rowType) && "interfaceName".equalsIgnoreCase(dataName))) {
			ComponentInterfaceProposalRender render = new ComponentInterfaceProposalRender();
			adapter = render.initAdapter(text);
			return;
		} else if ("sqlid".equalsIgnoreCase(tipRender)) {
			dialogType = DialogType.SQLID;
		} else {
			dialogType = DialogType.NONE;

			try {
				if (tipRender == null || tipRender.length() == 0) {
					// TODO 在未配置tipRender时，通过对rowType和dataName的判断，确定提示类型

					if ("返回".equalsIgnoreCase(varCatagory)) {
						// 对返回参数的提示处理,因返回数据类型提示和参数类型数据提示不同
						// tipRender =
						// "logic.diagram.dialog.proposal.ReturnProposalRender";
						ReturnProposalRender render = new ReturnProposalRender();
						render.setFilterStr(rowType);

						adapter = render.initAdapter(text,null,this.getParams());

					} else {
						// 参数提示
						if ("String".equalsIgnoreCase(rowType) && "sqlid".equalsIgnoreCase(dataName)) {
							dialogType = DialogType.SQLID;
						} else {

							// tipRender =
							// "logic.diagram.dialog.proposal.DefaultProposalRender";
							DefaultProposalRender render = new DefaultProposalRender();
							render.setFilterStr(rowType);
							adapter = render.initAdapter(text,null,this.getParams());
						}
					}

				} else {

					IProposalRender render = (IProposalRender) Class.forName(tipRender).newInstance();
					adapter = render.initAdapter(text);
				}

			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
