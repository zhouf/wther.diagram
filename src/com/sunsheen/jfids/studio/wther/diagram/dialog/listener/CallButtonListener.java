package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;
import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;
import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.wther.diagram.compiler.item.Flow;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ParamUtil;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ParseFlow;
import com.sunsheen.jfids.studio.wther.diagram.dialog.CallDialog;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.PathConvert;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicImageUtil;

public class CallButtonListener implements SDialogOnClickListener {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(CallButtonListener.class.getName());

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params, Event e, String currControlKey) {
		if ("bixBrowser".equals(currControlKey)) {

			IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

			String path = "";
			IProject activeProject = null;
			IContainer folder = null;
			if (editorPart != null) {
				IFileEditorInput input = (IFileEditorInput) editorPart.getEditorInput();
				IFile file = input.getFile();
				folder = file.getParent();
				activeProject = file.getProject();
				// path = activeProject.getLocation().toString();
				path = activeProject.getFolder("resources").getLocation().toString();
			}

			Log.debug("CallButtonListener.run() path:" + path);
			params.put("path", path);
			params.put("showText", "");
			DialogConfigParser parser = new DialogConfigParser(CallDialog.class.getResourceAsStream("selectbix.xml"), new Shell(), params);
			SDialog dialog = parser.getDialog();
			params.put("object", dialog);
			
			//IFile file = com.sunsheen.jfids.studio.ued.dialog.dataSelect.SearchResourceUI.selectLogicFile(activeProject.getParent(), folder, "");

			dialog.show(LogicImageUtil.getInstance(), LogicDiagramEditorPlugin.getImage("icons/dialog_logo.gif"), "业务逻辑流选择");

			// 处理获得的文件
			String chooseFile = (String) params.get("showText");
			log.debug("CallButtonListener run()-> chooseFile:" + chooseFile);
			params.put("chooseFile", chooseFile);

			IResource res = activeProject.findMember("resources/" + chooseFile);
			String fname = res.getLocation().toString();

			if (fname != null && fname.endsWith(".bix")) {
				putFileInfoIntoTable(coms, params, fname);
				((Text) coms.get("bixfile")).setText(PathConvert.convertToDisplayPath(chooseFile));
			}

		} else {
			Log.error("CallButtonListener.run() 传入参数不合法，请检查currControlKey:" + currControlKey);
		}
	}

	/**
	 * 将文件中的信息放入到表格中
	 * @param coms 对话框组件
	 * @param params 传递的参数
	 * @param fname 文件名，工程格式路径，如：src/abc.bix/aad.bix
	 */
	public static void putFileInfoIntoTable(Map<String, Widget> coms, Map<String, Object> params, String fname) {
		Flow flow = ParseFlow.fromFile(fname);
		String args = flow.getArgs();
		String rets = flow.getRet();
		if (args == null || "null".equalsIgnoreCase(args))
			args = "";
		if (rets == null || "null".equalsIgnoreCase(rets))
			rets = "";

		
		//获取原始填写值
		AgileGrid agileGrid = (AgileGrid) coms.get("table");
		InnerTableContentProvider provider=(InnerTableContentProvider)agileGrid.getContentProvider();
		int allRows = 0;
		for (int i = 0; i < provider.data.size(); i++) {
			allRows += provider.data.get(i).size();
		}
		HashMap<String,String> oldSetValues = new HashMap<String,String>();
		HashMap<String,String> oldSetTypes = new HashMap<String,String>();
		for (int i = 0; i < allRows; i++) {
			String name = (String) agileGrid.getContentAt(i, 2);
			String setVal = (String) agileGrid.getContentAt(i, 3);
			String setType = (String) agileGrid.getContentAt(i, 4);
			oldSetValues.put(name,setVal);
			oldSetTypes.put(name,setType);
		}
		//获取原始填写值end------------------------------------------------------------
				
		//先删除之前的数据
		emptyTable(coms, params);
		
		if (args.length() > 0) {
			fillArgsIntoTable(args, coms, params);
		} else {
			Log.error("CallButtonListener.putFileInfoIntoTable -> 参数为空，不生成参数行数据");
		}
		
		if (rets.length() > 0) {
			fillRetsIntoTable(rets, coms, params);
		} else {
			Log.error("CallButtonListener.putFileInfoIntoTable -> 返回为空，不生成返回行数据");
		}
		
		//将原来已填写的信息重新写到表格中
		provider=(InnerTableContentProvider)agileGrid.getContentProvider();
		allRows = 0;
		for (int i = 0; i < provider.data.size(); i++) {
			allRows += provider.data.get(i).size();
		}
		for (int i = 0; i < allRows; i++) {
			String name = (String) agileGrid.getContentAt(i, 2);
			if(oldSetValues.containsKey(name)){
				String setVal = oldSetValues.get(name);
				String setType = oldSetTypes.get(name);
				agileGrid.setContentAt(i, 3, setVal);
				agileGrid.setContentAt(i, 4, setType);
			}
		}
	}

	/**
	 * 将从文件中获取的参数信息填入表格中
	 * 
	 * @param args 参数字串，如=>String:arg1:false:表达式:<NOSET>|String:arg2:是否数组:表达式:描述
	 * @param coms
	 * @param params
	 */
	public static void fillArgsIntoTable(String args, Map<String, Widget> coms, Map<String, Object> params) {
		AgileGrid agileGrid = (AgileGrid) coms.get("table");
		InnerTableContentProvider provider = (InnerTableContentProvider) agileGrid.getContentProvider();
		int dataSize = provider.data.size();
		if (dataSize == 1) {
			provider.data.remove(0);
		} else if (dataSize == 2) {
			provider.data.remove(1);
			provider.data.remove(0);
		}
		provider.data.add(new HashMap<Integer, Map<Integer, Object>>());
		Map<Integer, Map<Integer, Object>> map = provider.data.get(0);

		int rowIndex = 0;

		log.debug("CallButtonListener.fillArgsIntoTable() args:" + args);
		String argArray[] = args.split("\\|");
		for (String eachArg : argArray) {
			log.debug("CallButtonListener.fillArgsIntoTable()->eachArg:" + eachArg);
			String p[] = ParamUtil.praseStrToArray(eachArg, 5);
			Map<Integer, Object> data = new HashMap<Integer, Object>();
			if("true".equals(p[2])){
				//如果是数组，则添加标记
				p[1] = p[1]+"[]";
			}
			// Log.debug("放入表格数据 eachArg:" + eachArg);
			data.put(0, "参数");
			data.put(1, p[0]);// 数据类型
			data.put(2, p[1]);// 数据名
			// data.put(3, p[1]);
			data.put(4, "变量");// 参数类型
			data.put(5, p[4]);// 参数描述

			map.put(rowIndex++, data);// 放入一行的数据
		}
		provider.adjustData();
		agileGrid.redraw();
		params.put("table", provider.data.toArray(new Map[provider.data.size()]));
	}

	/**
	 * 将从文件中获取的返回信息填入表格中
	 * @param rets 返回数据串，如=>String:ret1:false:表达式:<NOSET>|String:ret2:是否数组:表达式:描述
	 * @param coms
	 * @param params
	 */
	public static void fillRetsIntoTable(String rets, Map<String, Widget> coms, Map<String, Object> params) {
		AgileGrid agileGrid = (AgileGrid) coms.get("table");
		InnerTableContentProvider provider = (InnerTableContentProvider) agileGrid.getContentProvider();
		int providerDataSize = provider.data.size();
		if(providerDataSize==0){
			//如果没有参数和返回，则都创建
			provider.data.add(new HashMap<Integer, Map<Integer, Object>>());
			provider.data.add(new HashMap<Integer, Map<Integer, Object>>());
		}else if(providerDataSize==1){
			//如果只有参数，则只需要创建一个返回
			provider.data.add(new HashMap<Integer, Map<Integer, Object>>());
		}else{
			//如果已经有参数和返回，则删除之前的数据，再重新创建
			provider.data.remove(1);
			provider.data.add(new HashMap<Integer, Map<Integer, Object>>());
		}
		Map<Integer, Map<Integer, Object>> map = provider.data.get(1);

		int rowIndex = 0;

		String argArray[] = rets.split("\\|");
		for (String eachArg : argArray) {
			String p[] = ParamUtil.praseStrToArray(eachArg, 5);
			Map<Integer, Object> data = new HashMap<Integer, Object>();
			if("true".equals(p[2])){
				//如果是数组，则添加标记
				p[1] = p[1]+"[]";
			}

//			Log.debug("放入表格数据 eachArg:" + eachArg);
			data.put(0, "返回");
			data.put(1, p[0]);// 数据类型
			data.put(2, p[1]);// 数据名
			// data.put(3, p[1]);
			data.put(4, p[3]);// 参数类型
			data.put(5, p[4]);// 参数描述

			map.put(rowIndex++, data);// 放入一行的数据
		}
		provider.adjustData();
		agileGrid.redraw();
		params.put("table", provider.data.toArray(new Map[provider.data.size()]));
	}

	/**
	 * 清空表格数据
	 * 
	 * @param coms
	 * @param params
	 */
	public static void emptyTable(Map<String, Widget> coms, Map<String, Object> params) {
		AgileGrid agileGrid = (AgileGrid) coms.get("table");
		InnerTableContentProvider provider = (InnerTableContentProvider) agileGrid.getContentProvider();
		int dataSize = provider.data.size();
		if (dataSize == 1) {
			provider.data.remove(0);
		} else if (dataSize == 2) {
			provider.data.remove(1);
			provider.data.remove(0);
		}
		provider.adjustData();
		agileGrid.redraw();
		params.put("table", provider.data.toArray(new Map[provider.data.size()]));
	}
}
