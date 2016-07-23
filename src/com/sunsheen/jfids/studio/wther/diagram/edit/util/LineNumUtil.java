package com.sunsheen.jfids.studio.wther.diagram.edit.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.sunsheen.jfids.studio.logging.Log;

public class LineNumUtil {
	
	private static int INIT_LINENUM = 1000;	//初始行号
	private static int LINENUM_STEP = 1000;	//行号增量
	
	/**
	 * main for test
	 * @param args
	 */
	public static void main(String[] args) {
		String a = "<Flow xmi:id=\"_TJHKYXLvEeO8B98jidT6yg\" author=\"Leo\" createtime=\"2014-01-01 22:16:25\" baseLineNum=\"20\">";
		int k = parseLineNum(a);
		System.out.println("LineNumUtil.main()-> k:" + k);
	}

	/**
	 * 传入一个bix文件，找到同目录下的所有bix文件，通过遍历后获取可用的文件行号
	 * @param file 传入的文件
	 * @return 返回的基础行号
	 */
	public static int findAvailableBaseLineNum(IFile file){
//		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
//		IFolder folder = myWorkspaceRoot.getFolder((IPath) file);
		int retVal = INIT_LINENUM;
		IContainer container = file.getParent();
		Map<String,Integer> lineNumMap = new HashMap<String,Integer>();
		try {
			for(IResource res : container.members()){
				if(res!=null && res instanceof IFile && res.getName().endsWith(".bix")){

					int a = fetchLineNumFromIFile((IFile) res);
					lineNumMap.put(res.getName(), new Integer(a));
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		//遍历HashMap, for test
		Iterator<String> iterator = lineNumMap.keySet().iterator();
		while(iterator.hasNext()){
			String name = (String) iterator.next();
			Integer line = lineNumMap.get(name);
			Log.debug("Map name:" + name + "\tline:" + line);
		}

		while(lineNumMap.containsValue(retVal)){
			Log.debug("LineNumUtil.findAvailableBaseLineNum()-> :集合中包含行数据" + retVal);
			retVal += LINENUM_STEP;
			Log.debug("LineNumUtil.findAvailableBaseLineNum()-> :新测试值：" + retVal);
		}
		
		Log.debug("LineNumUtil.findAvailableBaseLineNum()-> retVal:" + retVal);
		return retVal;
	}
	
	/**
	 * 从文件里取行号
	 * @param file 传入的IFile文件
	 * @return 返回该文件设置的行号，如果没有，返回-1
	 */
	public static int fetchLineNumFromIFile(IFile file){
		int retVal = -1;
		//将InputStream转换为String
		try {
			InputStream in = file.getContents();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String eachLine;
			while((eachLine = br.readLine()) != null){
				if(eachLine.contains("baseLineNum=")){
					retVal = parseLineNum(eachLine);
					break;
				}
			}
			br.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
			
		return retVal;
	}
	
	/**
	 * 从含有lineNumber信息的文本行里解析出行设置数据
	 * @param line 传入的文本串行
	 * @return 返回其中的行号设置，如果异常，返回-1
	 */
	public static int parseLineNum(String line){
		// FIXME......  baseLineNum="20"
		int retInt = -1;
		
		int indexFirst = line.indexOf("baseLineNum=");
		int left = line.indexOf("\"", indexFirst);
		int right = line.indexOf("\"",left+1);
		String numStr = line.substring(left+1, right);
		retInt = Integer.parseInt(numStr);
		
		return retInt;
	}

}
