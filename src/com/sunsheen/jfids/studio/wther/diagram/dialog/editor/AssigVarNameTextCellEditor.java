package com.sunsheen.jfids.studio.wther.diagram.dialog.editor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Text;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.agilegrid.Cell;
import com.sunsheen.jfids.studio.dialog.agilegrid.EditorActivationEvent;
import com.sunsheen.jfids.studio.dialog.proposal.MyContentProposalProvider;
import com.sunsheen.jfids.studio.dialog.proposal.SSContentProposalAdapter;
import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.JavaTypeUtil;
import com.sunsheen.jfids.studio.wther.diagram.dialog.listener.MouseActiveProposalListener;
import com.sunsheen.jfids.studio.wther.diagram.dialog.proposal.VarLabelProvider;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FindClass;

/**
 * 这是赋值对话框中第一列，变量名的一个编辑器，用于判断输入的变量是否是已定义的变量 以确定复选项是否选中
 * 
 * @author zhouf
 * 
 */
public class AssigVarNameTextCellEditor extends AbstractTextCellEditor {

	IContentProposalProvider contentProvider = null;
	String[] initProposalArray = null;
	boolean lastAltKey = false;
	
	public AssigVarNameTextCellEditor(AgileGrid agileGrid) {
		this(agileGrid, SWT.SINGLE);
	}

	public AssigVarNameTextCellEditor(AgileGrid agileGrid, int style) {
		super(agileGrid, style);

		initProposalArray = (String[]) this.getParams().get("proposalData");
		contentProvider = new MyContentProposalProvider(initProposalArray);
		
		this.text.addMouseListener(new MouseActiveProposalListener());
	}
	
	

	@Override
	protected void onKeyPressed(KeyEvent keyEvent) {
		
		if (keyEvent.character == '/' && lastAltKey) {
			adapter.setContentProposalProvider(contentProvider);
			
		} else if (keyEvent.character == '/') {
			// 如果是/则提取出相邻变量
			String getStr = praseVar(text);
			adapter.setContentProposalProvider(new MyContentProposalProvider(getMethods(getStr)));
		}
		lastAltKey = (keyEvent.keyCode == SWT.ALT);
	}

	@Override
	public void activate(EditorActivationEvent activationEvent) {
		// IProposalRender render =
		// (IProposalRender)Class.forName("logic.diagram.dialog.proposal.AssignmentProposalRender").newInstance();
//		AssignmentProposalRender render = new AssignmentProposalRender();
//		adapter = render.initAdapter(text);
		initAdapter(text);
		super.activate(activationEvent);
	}

	@Override
	protected void focusLost() {
		if (this.adapter != null && !this.adapter.isProposalPopupOpen()) {
			
			Cell focusCell = agileGrid.getFocusCell();
			agileGrid.setContentAt(focusCell.row, focusCell.column, text.getText());
			agileGrid.applyEditorValue();
			super.focusLost();
		}
	}
	
	public void initAdapter(Text widget) {
		KeyStroke keyStroke = null; // null 表示不接受快捷键
		try {
			keyStroke = KeyStroke.getInstance("Alt+/"); // 在text上按Ctrl+1弹出popup的shell.
		} catch (Exception e) {
			e.printStackTrace();
		}

		adapter = new SSContentProposalAdapter(widget, new TextContentAdapter(), contentProvider, keyStroke, new char[] { '/' });

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
	

	/**
	 * 通过反射获取变量的成员方法，从定义的变量里查找变量及类型 支持
	 * obj/attr方式赋值，不整合到TextEditorUtil.getMethods()
	 * @param varName 输入变量名
	 * @return 返回可执行的方法字串
	 */
	private String[] getMethods(String varName) {
		// TODO 变量方法的多级提示，如obj.toString().之后的方法提示
		String retArray[] = new String[] {};
		ArrayList<String> methodList = new ArrayList<String>();
		
		for (String eachVar : initProposalArray) {
			
			String items[] = eachVar.split(" "); // 变量名和类型是按空格分开的
			if (items.length == 2) {
				if (varName.equals(items[0])) {
					// 找到了变量，判断变量类型
					String type = items[1];
					if(type.contains("&")){
						type = type.substring(0,type.indexOf("&"));
					}
					Log.debug("AssigVarNameTextCellEditor getMethods()-> 判断类型:" + type);
					if (JavaTypeUtil.isPrimitiveType(type)) {
						Log.debug("AssigVarNameTextCellEditor.getMethods() 简单类型，无可用方法");
						break;
					} else {
						if (JavaTypeUtil.containsKey(type)) {
							type = JavaTypeUtil.convertShortToLongType(type);
						}
						try {
							//Class<?> className = Class.forName(type);
							Class<?> className = FindClass.fromString(type);
							for (Method method : className.getDeclaredMethods()) {
								if ((method.getModifiers() & Modifier.PUBLIC) > 0) {
									// 只加载公有方法
									String methodName = method.getName();
									//过滤set方法
									if(methodName!=null && methodName.startsWith("set")){
										String attrName = convertFuncToAttrName(methodName);
										methodList.add(attrName);
									}
								}
							}
							if (methodList.size() > 0) {
								retArray = methodList.toArray(retArray);
							}
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else {
				Log.error("ConditionTextCellEditor.getMethods-> :分解变量名及类型时出错：" + eachVar);
			}
		}
		return retArray;
	}

	/**
	 * 将方法名转换为属性名
	 * @param methodName 方法名如：setName
	 * @return String 返回属性名如:name
	 */
	private String convertFuncToAttrName(String methodName) {
		String retStr = "";
		if(methodName.length()>4){
			String endStr = methodName.substring(4);
			String firstLetter = methodName.substring(3, 4);
			retStr = firstLetter.toLowerCase().concat(endStr);
		}
		
		return retStr;
	}

}
