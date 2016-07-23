package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.runtime.Platform;

import com.sunsheen.jfids.studio.wther.diagram.compiler.item.Flow;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FilePathManager;


/**
 * 这是一个处理自定义配置文件的一个类，如XML配置文件内容的写入和删除
 * @author zhouf
 * Date: 2013-6-23
 */
public class CustomConfigFileUtil {
	static String configPath;
	static{
		configPath = Platform.getConfigurationLocation().getURL().getPath();
	}
	
	/**
	 * 保存自定义配置文件
	 * @param custName 节点名
	 * @param fileFullPath 引用文件完整路径
	 * @param filePrjPath 引用文件工作区的相对路径
	 * @param pixMode 标明是否是PIX文件
	 */
	private static void saveConfig(String custName,String fileFullPath,String filePrjPath,boolean pixMode) {
		String xmlFile;
		if(pixMode){
			xmlFile = FilePathManager.getCustomPixCompomentFile();
		}else{
			xmlFile = FilePathManager.getCustomBixCompomentFile();
		}
		SAXReader reader = new SAXReader();
		XMLWriter writer = null;// 声明写XML的对象
		
		OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");// 设置XML文件的编码格式
        Document doc = null;
        Element root = null;
		try {
			File file = new File(xmlFile);
			if(file.exists()){
				doc = reader.read(file);
				root = doc.getRootElement().element("dir");
			}else{
				//创建文件及结构，并写入数据
				file.createNewFile();
                doc = DocumentHelper.createDocument();
                root = doc.addElement("component-group").addElement("dir");
                root.addAttribute("name", "自定义");
			}
			if(filePrjPath.length()==0){
				//添加模板没有这个参数
				genTemplateElement(custName, fileFullPath, root);
			}else{
				//添加自定义库
				genLibraryElement(custName, fileFullPath, filePrjPath, pixMode, root);
			}
			
			FileOutputStream fos = new FileOutputStream(file);
			writer = new XMLWriter(fos, format);
			writer.write(doc);
			writer.close();
			
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveTemplateConfig(String custName,String fileFullPath,boolean pixMode) {
		saveConfig(custName, fileFullPath, "", pixMode);
	}

	//生成模板的节点信息
	private static void genTemplateElement(String custName, String fileFullPath, Element root) {
		Element component = root.addElement("component");
		component.addAttribute("name", custName);
		component.addAttribute("code", "ref:cust:"+fileFullPath);	//自定义模板还添加一个cust:前缀，以区别
	}

	
	public static void saveLibraryConfig(String custName, String fileFullPath, String filePrjPath, boolean pixMode) {
		saveConfig(custName, fileFullPath, filePrjPath, pixMode);
	}

	//生成库的节点信息
	private static void genLibraryElement(String custName, String fileFullPath, String filePrjPath, boolean pixMode,
			Element root) {
		Flow flow = ParseFlow.fromFile(fileFullPath);
		String args = flow.getArgs();
		String rets = flow.getRet();
		args = (args==null? "" : args);
		rets = (rets==null? "" : rets);
		
		Element component = root.addElement("component");
		component.addAttribute("name", custName);
		if(pixMode){
			component.addAttribute("code", PixBixCallUtil.getPixPrefix(filePrjPath));
			component.addAttribute("ext", "pixref:"+filePrjPath);
		}else{
			component.addAttribute("code", PixBixCallUtil.getBixPrefix(filePrjPath));
			component.addAttribute("ext", "bixref:"+filePrjPath);
		}
		
		Element paramstr = component.addElement("paramstr");
		paramstr.addAttribute("val", args);
		
		Element retstr = component.addElement("retstr");
		retstr.addAttribute("val", rets);
		
		Element comment = component.addElement("memo");
		comment.setText(PathConvert.convertToDisplayPath(filePrjPath));
	}
	

	/**
	 * 删除配置文件中的项目
	 * @param nodeName 节点名
	 * @param funcName 节点中的配置文件路径
	 * @param pixMode 标明是否是PIX文件
	 * @return 如果找到删除项，执行了删除操作，返回TRUE,否则返回FALSE
	 */
	public static boolean dropCustomItem(String nodeName, String funcName, boolean pixMode) {
		String xmlFile;
		if(pixMode){
			xmlFile = FilePathManager.getCustomPixCompomentFile();
		}else{
			xmlFile = FilePathManager.getCustomBixCompomentFile();
		}

		SAXReader reader = new SAXReader();
		Document doc;
		boolean doDelete = false;
		try {
			doc = reader.read(new File(xmlFile));

			@SuppressWarnings("unchecked")
			Iterator<Element> iter = doc.getRootElement().element("dir").elementIterator();
			while (iter.hasNext()) {
				Element component = (Element) iter.next();
				String eName = component.attribute("name").getText();
				String eCode = component.attribute("code").getText();

				if(eName.equalsIgnoreCase(nodeName) && eCode.equalsIgnoreCase(funcName)){
					// 获得其父节点然后删除节点
					component.getParent().remove(component);
					doDelete = true;
				}
			}
			if(doDelete){
				FileOutputStream fos = new FileOutputStream(xmlFile);
				OutputFormat format = OutputFormat.createPrettyPrint();
		        format.setEncoding("UTF-8");// 设置XML文件的编码格式
				XMLWriter writer = new XMLWriter(fos, format);
				writer.write(doc);
				writer.close();
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doDelete;
	}

}
