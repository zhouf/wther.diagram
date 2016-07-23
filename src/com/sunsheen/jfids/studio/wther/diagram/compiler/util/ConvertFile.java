package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import com.sunsheen.jfids.studio.wther.diagram.compiler.item.Children;
import com.sunsheen.jfids.studio.wther.diagram.compiler.item.Flow;
import com.sunsheen.jfids.studio.wther.diagram.compiler.item.Notation;

/**
 * 将文件转化为JAVA模型
 * @author zhouf
 */
public class ConvertFile {

	private Flow flow = null;
	private Notation notation;
	private String fileName = null;
	private InputStream inputStream = null;

	public ConvertFile(InputStream inputStream) {
		super();
		this.inputStream = inputStream;
		this.parseXML();
		this.convertLocationToOffset();
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	public Notation getNotation() {
		return notation;
	}

	public void setNotation(Notation notation) {
		this.notation = notation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	
	/**
	 * 将xml文件转换成类对象
	 */
	public void parseXML() {
		InputStream is1=null,is2=null;
		//复制流
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while ((n = inputStream.read(buf)) >= 0)
			    baos.write(buf, 0, n);
			byte[] content = baos.toByteArray();

			is1 = new ByteArrayInputStream(content);
			is2 = new ByteArrayInputStream(content);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Digester digester = new Digester();
		Digester notationDigester = new Digester();
		digester.setValidating(false);
		//完整类名定义，包名改变时需做相应变化
		digester.addObjectCreate("xmi:XMI/Flow", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.Flow");
		digester.addSetProperties("xmi:XMI/Flow");
		digester.addObjectCreate("xmi:XMI/Flow/nodes", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.Nodes");
		digester.addSetProperties("xmi:XMI/Flow/nodes", "xmi:id", "id");
		digester.addSetProperties("xmi:XMI/Flow/nodes", "xmi:type", "type");
		digester.addSetNext("xmi:XMI/Flow/nodes", "addNodes", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.Nodes");

		digester.addObjectCreate("xmi:XMI/Flow/nodes/iflinks", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.IfLinks");
		digester.addSetProperties("xmi:XMI/Flow/nodes/iflinks", "xmi:id", "id");
		digester.addSetNext("xmi:XMI/Flow/nodes/iflinks", "addLinks", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.IfLinks");
		
		digester.addObjectCreate("xmi:XMI/Flow/nodes/callbacklinks", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.CallBackLinks");
		digester.addSetProperties("xmi:XMI/Flow/nodes/callbacklinks", "xmi:id", "id");
		digester.addSetNext("xmi:XMI/Flow/nodes/callbacklinks", "addCallBackLinks", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.CallBackLinks");
		
		notationDigester.addObjectCreate("xmi:XMI/notation:Diagram", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.Notation");
		notationDigester.addObjectCreate("xmi:XMI/notation:Diagram/children", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.Children");
		notationDigester.addSetProperties("xmi:XMI/notation:Diagram/children", "xmi:id", "id");
		notationDigester.addSetProperties("xmi:XMI/notation:Diagram/children", "xmi:type", "xmitype");
		notationDigester.addSetProperties("xmi:XMI/notation:Diagram/children/layoutConstraint", "x", "x");
		notationDigester.addSetProperties("xmi:XMI/notation:Diagram/children/layoutConstraint", "y", "y");
		notationDigester.addObjectCreate("xmi:XMI/notation:Diagram/edges", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.Lines");
		notationDigester.addSetProperties("xmi:XMI/notation:Diagram/edges", "xmi:id", "id");
		notationDigester.addSetProperties("xmi:XMI/notation:Diagram/edges", "xmi:type", "xmitype");
		notationDigester.addSetNext("xmi:XMI/notation:Diagram/children", "addChildren", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.Children");
		notationDigester.addSetNext("xmi:XMI/notation:Diagram/edges", "addLines", "com.sunsheen.jfids.studio.wther.diagram.compiler.item.Lines");
		
		try {
			flow = (Flow)digester.parse(is1);
			notation = (Notation)notationDigester.parse(is2);
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return;
	}
	
	/**
	 * 将节点坐标的位置转换成偏移量
	 */
	private void convertLocationToOffset(){
		Iterator<String> childrenids = notation.getChildrenId();
		int minx=Integer.MAX_VALUE,miny=Integer.MAX_VALUE;
		while(childrenids.hasNext()){
			String childid = childrenids.next();
			Children child = notation.getChild(childid);
			//节点类型判断，连线类型判断，如果以20开始，确定为模型中定义的类型
			if(child.getType().startsWith("20")){
				String nodeType = flow.findNodes(child.getElement()).getType();
				//去掉开始和结束节点的计算
				if(!("Start".equalsIgnoreCase(nodeType) || "End".equalsIgnoreCase(nodeType))){
					int x = child.getX();
					int y = child.getY();
					minx = (minx>x ? x : minx);
					miny = (miny>y ? y : miny);
				}
			}
		}
		//所有坐标减去最小值即得偏移量
		Iterator<String> cids = notation.getChildrenId();
		while(cids.hasNext()){
			String cid = cids.next();
			Children child = notation.getChild(cid);
			if(child.getType().startsWith("20")){
				child.setX(child.getX()-minx);
				child.setY(child.getY()-miny);
				notation.setChild(cid, child);
			}
		}
	}
}
