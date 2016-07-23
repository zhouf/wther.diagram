package com.sunsheen.jfids.studio.wther.diagram.dialog.popup.listener;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.compoments.listeners.SDialogOnClickListener;

/**
 * 监听“文件名”文本框输入的内容，以及第一次进入对话框时显示初始化列表项
 * @author zhoufeng
 * Date:2013-6-15
 */
public class BeanTypeOnChange implements SDialogOnClickListener {

	@Override
	public void run(Map<String, Widget> coms, Map<String, Object> params, Event event, String currentKey) {
		Text beanNameText = (Text) coms.get("beanName");
		String beanName = beanNameText.getText().trim();
		beanNameText.setSelection(beanName.length());
		List colum = (List) coms.get("show");// 展示选出的内容；
		colum.setItems(new String[0]);// 清空列表；
		Label showText = (Label) coms.get("showText");
		showText.setText("");// 清空显示
		// show0是第一次进来就新建，当然为空，如果第二次则不为空，用来保存文件列表；
		String[] arrProject = (String[]) params.get("show0");
		String[] arrModel = (String[]) params.get("show1");
		if (!"".equals(beanName) && arrProject == null) {
			java.util.List<String> list = getListFiles(params.get("path").toString(), "eix", true);
			//EIXSelectionDialog.selectEIXFile(folder.getProject(), folder, "");
			
			// String[] arr = (String[]) list.toArray(new
			// String[list.size()]);
			params.put("show0", (String[]) list.toArray(new String[list.size()]));
			arrProject = (String[]) params.get("show0");
		}
		if ("".equals(beanName) && arrModel == null) {
//			 Log.debug("BeanTypeOnChange.run()-> params.get().toString():"
//			 + params.get("filePath").toString());
			String modelPath = params.get("filePath").toString().split("/src/")[0];
			java.util.List<String> list = getListFiles(modelPath, "eix", true);
			// String[] arr = (String[]) list.toArray(new
			// String[list.size()]);
			arrModel = (String[]) list.toArray(new String[list.size()]);
			params.put("show1", arrModel);
		}
		if (beanName.length() != 0) {
			// 在正则表达式中，这些特殊符?,*等，是表示重复次数，前面必须有一个字符。但我们的要求是输入*就表示任意多个字符，所以要转化；
			if (beanName.contains("?")) {
				// ？本身在正则表达式是为特殊，所以要转成字符串加\\? 才表示字符串中的？
				// 。因为replaceAll中两个参数为正则表达式。
				beanName = beanName.replaceAll("\\?", ".?");
			}
			if (beanName.contains("*")) {
				beanName = beanName.replaceAll("\\*", ".*");
			}
			for (int i = 0; i < arrProject.length; i++) {
				String str = arrProject[i];
				if (str.toLowerCase().matches(".*" + beanName.toLowerCase() + ".*")) {
					// matches是全额匹配，所以前后加.*，表示任意多个字符
					colum.add(str);
				}
			}
		} else {
			if (arrModel != null) {
				for (int i = 0; i < arrModel.length; i++) {
					colum.add(arrModel[i]);
				}
			}
		}
	}

	public ArrayList<String> getListFiles(String path, String suffix, boolean isdepth) {
//		Log.debug("BeanTypeOnChange.getListFiles()-> path:" + path + " suffix:" + suffix);
		ArrayList<String> lstFileNames = new ArrayList<String>();
		File file = new File(path);
		return listFile(lstFileNames, file, suffix, isdepth);
	}

	private static ArrayList<String> listFile(ArrayList<String> lstFileNames, File f, String suffix, boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
		if (f.isDirectory()) {
			File[] t = f.listFiles();
			for (int i = 0; i < t.length; i++) {
				if (isdepth || t[i].isFile()) {
					listFile(lstFileNames, t[i], suffix, isdepth);
				}
			}
		} else {
			String filePath = f.getAbsolutePath();
			String fileName = f.getName();
			if (!suffix.equals("")) {
				int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";

				if (begIndex != -1) {
					tempsuffix = filePath.substring(begIndex + 1);
					if (tempsuffix.equals(suffix) && filePath.indexOf("\\bin\\") < 0) {
						fileName = fileName.substring(0, fileName.lastIndexOf("."));
						lstFileNames.add(fileName);
						// Log.debug("BeanTypeOnChange.listFile()-> add>>>>>> fileName:"
						// + fileName);
					}
				}
			} else {
				lstFileNames.add(fileName);
			}
		}
		return lstFileNames;
	}

	public static void main(String[] args) {
		String path = "D:/eclipse-SDK-3.7.1_GMF/runtime-EclipseApplication/kkk";
		BeanTypeOnChange change = new BeanTypeOnChange();
		ArrayList<String> list = change.getListFiles(path, "pix", true);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
}