package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FileScanUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.PathConvert;
/*
 * 监听“文件名”文本框输入的内容；
 */
public class TypeOnChange implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params,
			Event event, String currentKey) {
		Text fileNameText=(Text)coms.get("fileName");
		String fileName=fileNameText.getText().trim();
		List colum=(List)coms.get("show");//展示选出的内容；
		String blank[]=new String[0];
		colum.setItems(blank);//清空列表；
		Label showText=(Label)coms.get("showText");
		showText.setText("");//清空显示
		//show0是第一次进来就新建，当然为空，如果第二次则不为空，用来保存文件列表；
		if(params.get("show0")==null)
		{
			java.util.List<String> list = getListFiles(params.get("path").toString(), "bix", true);		
			String[] arr = (String[]) list.toArray(new String[list.size()]);
			params.put("show0", arr);
		}
		
		String []arr=(String[]) params.get("show0");
		java.util.List<String> flagPath=new ArrayList<String>();//用于获取 点击列表框后；还能找到所对应的全路径；
		if(fileName.length()!=0)
		{
			//在正则表达式中，这些特殊符?,*等，是表示重复次数，前面必须有一个字符。但我们的要求是输入*就表示任意多个字符，所以要转化；
			if(fileName.contains("?"))
			{
				// ？本身在正则表达式是为特殊，所以要转成字符串加\\? 才表示字符串中的？ 。因为replaceAll中两个参数为正则表达式。
				fileName=fileName.replaceAll("\\?", ".?");
			}
			if(fileName.contains("*"))
			{
				fileName=fileName.replaceAll("\\*", ".*");
			}
			for(int i=0;i<arr.length;i++)
			{
//				Log.debug("TypeOnChange.run()-> arr[i]:" + arr[i]);
				String str=arr[i].substring(arr[i].lastIndexOf("\\")+1,arr[i].length());
//				Log.debug("TypeOnChange.run()-> str:" + str);
				if(str.toLowerCase().matches(".*"+fileName.toLowerCase()+".*"))//matches是全额匹配，所以前后加.*，表示任意多个字符
				{
					//全路径为：f:\\，但js引用是/，所以得转换
					String path=(String) params.get("path");//工程路径；
					String fullPath=arr[i].replaceAll("\\\\", "/");
					String pathInProject=fullPath.substring(path.length()+1,fullPath.length());
					colum.add(str + " - " + PathConvert.convertToDisplayPath(pathInProject));
					flagPath.add(pathInProject);
				}
			}
			params.put("flagPath", flagPath);
		}		
	}
	
	public ArrayList<String> getListFiles(String path, String suffix,boolean isdepth) {
		ArrayList<String> lstFileNames = new ArrayList<String>();
		File file = new File(path);
		return FileScanUtil.listFile(lstFileNames, file, suffix, isdepth);
	}

	public static void main(String[] args) {
		String path="D:/eclipse-SDK-3.7.1_GMF/runtime-EclipseApplication/kkk";
		TypeOnChange change = new TypeOnChange();
		ArrayList<String> list=change.getListFiles(path, "bix", true);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
}