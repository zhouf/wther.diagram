package com.sunsheen.jfids.studio.wther.diagram.providers;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.common.ui.util.CustomData;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.AWTClipboardHelper;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.commands.CopyImageCommand;
import org.eclipse.gmf.runtime.diagram.ui.requests.PasteViewRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IWorkbenchPart;

import com.sunsheen.jfids.studio.logic.Node;
import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditor;

public class BixDiagramGlobalActionHandler extends DiagramGlobalActionHandler {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(BixDiagramGlobalActionHandler.class.getName());
	
	//用于存放节点粘贴时已有的行号
	Set<Integer> lineSet;

	public BixDiagramGlobalActionHandler() {
		super();
		lineSet = new HashSet<Integer>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler
	 * #canPaste(org.eclipse.gmf.runtime.common.ui.services.action.global.
	 * IGlobalActionContext)
	 */
	protected boolean canPaste(IGlobalActionContext cntxt) {
		IWorkbenchPart activePart = cntxt.getActivePart();
		try{
			if(activePart instanceof LogicDiagramEditor){
				FlowImpl flow = (FlowImpl) ((Diagram) (((LogicDiagramEditor)activePart).getDiagramEditPart().getModel())).getElement();
				//获取当前编辑器中的节点
				List<Node> nodes = flow.getNodes();
				lineSet.removeAll(lineSet);
				for(Node n : nodes){
					lineSet.add(n.getLineNum());
				}
			}
		}catch(NullPointerException ex){
			System.err.println("BixDiagramGlobalActionHandler.canPaste-> " + ex.toString());
		}
		/*
		 * if (!AWTClipboardHelper.getInstance().isImageCopySupported()) {
		 * return super.canPaste(cntxt); }
		 */

		/* Check if the clipboard has data for the drawing surface */
		// return AWTClipboardHelper.getInstance().hasCustomData();
		return true;
	}

	@Override
	protected boolean canCopy(IGlobalActionContext cntxt) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler
	 * #getCopyCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.
	 * IGlobalActionContext,
	 * org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart, boolean)
	 */
	protected ICommand getCopyCommand(IGlobalActionContext cntxt,
			IDiagramWorkbenchPart diagramPart, final boolean isUndoable) {
		if (!AWTClipboardHelper.getInstance().isImageCopySupported()) {
			return super.getCopyCommand(cntxt, diagramPart, isUndoable);
		}
		return new CopyImageCommand(cntxt.getLabel(), diagramPart.getDiagram(),
				getSelectedViews(cntxt.getSelection()),
				diagramPart.getDiagramEditPart()) {

			public boolean canUndo() {
				return isUndoable;
			}

			public boolean canRedo() {
				return isUndoable;
			}

			protected CommandResult doUndoWithResult(
					IProgressMonitor progressMonitor, IAdaptable info)
					throws ExecutionException {

				return isUndoable ? CommandResult.newOKCommandResult() : super
						.doUndoWithResult(progressMonitor, info);
			}

			protected CommandResult doRedoWithResult(
					IProgressMonitor progressMonitor, IAdaptable info)
					throws ExecutionException {

				return isUndoable ? CommandResult.newOKCommandResult() : super
						.doRedoWithResult(progressMonitor, info);
			}

			public void addContext(IUndoContext context) {
				if (isUndoable) {
					super.addContext(context);
				}
			}

			public void removeContext(IUndoContext context) {
				if (isUndoable) {
					super.removeContext(context);
				}
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler
	 * #createPasteViewRequest()
	 */
	@SuppressWarnings("unchecked")
	protected PasteViewRequest createPasteViewRequest() {
		if (!AWTClipboardHelper.getInstance().isImageCopySupported()) {
			return super.createPasteViewRequest();
		}
		CustomData data = AWTClipboardHelper.getInstance().getCustomData();
		String str = new String(data.getData());
		
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			Map<String, String> map = new HashMap<String, String>();
			map.put("xmlns:xmi", "http://www.omg.org/XMI");
			map.put("xmlns", "http://logic");
			map.put("xmlns:ecore", "http://www.eclipse.org/emf/2002/Ecore");
			map.put("xmlns:notation",
					"http://www.eclipse.org/gmf/runtime/1.0.2/notation");
			map.put("xmi:version", "2.0");
			saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
			document = saxReader.read(new StringReader(str));
			
			//去掉粘贴时的一些属性，找到悬空的Link结点（连线），并全部删除
			String[] ignoreElements = new String[]{"//xmlns:link","//xmlns:elselink"};
			for(String nameStr : ignoreElements){
				List<Element> linkNodes = document.selectNodes(nameStr);
				for (Element element : linkNodes) {
					element.getParent().remove(element);
				}
			}
			
			//处理iflinks
			List<Element> iflinksNodes = document.selectNodes("//xmlns:iflinks");
			for (Element element : iflinksNodes) {
				String target = element.attributeValue("target");
				if(!StringUtils.contains(str, "xmi:id=\"" + target)){
					//如果拷贝内容中不包含目标结点，则删除连接信息
					element.getParent().remove(element);
				}
			}
			
			// 更改复制结点的行号，对每种类型分别判断，可考虑代码优化todo...
			List<Element> nodes = document.selectNodes("//xmlns:Custom");
			for (Element element : nodes) {
				int line = getAvailableLineNum();
				element.attribute("lineNum").setText(String.valueOf(line));
			}

			nodes = document.selectNodes("//xmlns:Assignment");
			for (Element element : nodes) {
				int line = getAvailableLineNum();
				element.attribute("lineNum").setText(String.valueOf(line));
			}
			nodes = document.selectNodes("//xmlns:Bixref");
			for (Element element : nodes) {
				int line = getAvailableLineNum();
				element.attribute("lineNum").setText(String.valueOf(line));
			}
			nodes = document.selectNodes("//xmlns:Blank");
			for (Element element : nodes) {
				int line = getAvailableLineNum();
				element.attribute("lineNum").setText(String.valueOf(line));
			}
			nodes = document.selectNodes("//xmlns:If");
			for (Element element : nodes) {
				int line = getAvailableLineNum();
				element.attribute("lineNum").setText(String.valueOf(line));
			}
			nodes = document.selectNodes("//xmlns:For");
			for (Element element : nodes) {
				int line = getAvailableLineNum();
				element.attribute("lineNum").setText(String.valueOf(line));
			}
			nodes = document.selectNodes("//xmlns:Call");
			for (Element element : nodes) {
				int line = getAvailableLineNum();
				element.attribute("lineNum").setText(String.valueOf(line));
			}
			nodes = document.selectNodes("//xmlns:Transaction");
			for (Element element : nodes) {
				int line = getAvailableLineNum();
				element.attribute("lineNum").setText(String.valueOf(line));
			}
			nodes = document.selectNodes("//xmlns:Transcommit");
			for (Element element : nodes) {
				int line = getAvailableLineNum();
				element.attribute("lineNum").setText(String.valueOf(line));
			}
			nodes = document.selectNodes("//xmlns:Transrollback");
			for (Element element : nodes) {
				int line = getAvailableLineNum();
				element.attribute("lineNum").setText(String.valueOf(line));
			}
			
			
			
			str = formatXML(document, "UTF-8");
			// <!-- OFFSET|3598 -->
			str = str.replaceAll("OFFSET\\|\\d+","OFFSET\\|" + str.indexOf("<!-- TYPE|EMF+VERSION|1.0 -->"));
			
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		// 返回编译后的字符串格式
		CustomData d = new CustomData(data.getFormatType(), str.getBytes());
		PasteViewRequest request = new PasteViewRequest(new ICustomData[] { d });
		return request;
	}

	private static String formatXML(Document document, String charset) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(charset);
		StringWriter sw = new StringWriter();
		XMLWriter xw = new XMLWriter(sw, format);
		try {
			xw.write(document);
			xw.flush();
			xw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	/**
	 * 找到一个可用的行号，用于分配给新粘贴的节点，从2开始
	 * @return int
	 */
	private int getAvailableLineNum(){
		int retVal = 2;
		while(lineSet.contains(retVal)){
			retVal++;
		}
		lineSet.add(retVal);
		return retVal;
	}

}
