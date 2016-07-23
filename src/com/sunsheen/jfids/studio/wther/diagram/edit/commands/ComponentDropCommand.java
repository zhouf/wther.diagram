package com.sunsheen.jfids.studio.wther.diagram.edit.commands;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.Node;

import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.logic.Assignment;
import com.sunsheen.jfids.studio.logic.Bixref;
import com.sunsheen.jfids.studio.logic.Blank;
import com.sunsheen.jfids.studio.logic.Call;
import com.sunsheen.jfids.studio.logic.Custom;
import com.sunsheen.jfids.studio.logic.Entity;
import com.sunsheen.jfids.studio.logic.For;
import com.sunsheen.jfids.studio.logic.If;
import com.sunsheen.jfids.studio.logic.LogicFactory;
import com.sunsheen.jfids.studio.logic.Transaction;
import com.sunsheen.jfids.studio.logic.Transcommit;
import com.sunsheen.jfids.studio.logic.Transrollback;
import com.sunsheen.jfids.studio.wther.diagram.compiler.item.Flow;
import com.sunsheen.jfids.studio.wther.diagram.compiler.item.Lines;
import com.sunsheen.jfids.studio.wther.diagram.compiler.item.Nodes;
import com.sunsheen.jfids.studio.wther.diagram.compiler.item.Notation;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.ConvertFile;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.FilePathManager;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;

public class ComponentDropCommand extends Command {
	
	private EObject contaner = null;
	private EditDomain domain = null;
	private EditPart host = null;
	private ConvertFile cfile = null;
	private Point baseLocation = null;
	private CompoundCommand nodeCmds = null;
	private CompoundCommand connCmds = null;
	
	public ComponentDropCommand(){
		nodeCmds = new CompoundCommand();
		connCmds = new CompoundCommand();
	}

	public void setFile(String file) {
		cfile = new ConvertFile(getFileInputStream(file));
	}

	public void setContaner(EObject contaner) {
		this.contaner = contaner;
	}

	public void setBaseLocation(Point baseLocation) {
		this.baseLocation = baseLocation;
	}

	public void setDomain(EditDomain domain) {
		this.domain = domain;
	}

	public void setHost(EditPart host) {
		this.host = host;
	}

	@Override
	public void execute() {
		
		int baseNodeCount = host.getChildren().size();
		//所有添加的节点依次放入此集合，以方便按下标查找节点
		List<String> appendList = new ArrayList<String>();
		
		Flow flow = cfile.getFlow();
		Notation notation = cfile.getNotation();
		//遍历节点，画节点
		Iterator<String> nodeids = flow.getNodesId();
		while(nodeids.hasNext()){
			String nodeid = nodeids.next();
			Nodes node = flow.findNodes(nodeid);
			//去掉开始和结束结点
			if(!("Start".equalsIgnoreCase(node.getType()) || "End".equalsIgnoreCase(node.getType()))){
				Point offset = notation.findNodeOffset(nodeid);
				drawNode(node, offset.translate(baseLocation));
				appendList.add(nodeid);
			}
			
			// 修改连线策略为判断汇聚节点可以是结束节点，将结束节点进行转换zhouf 20160323
			if("If".equalsIgnoreCase(node.getType())){
				String endIfNodeId = flow.findEndIfNodeId(node);
				if(StringUtils.isNotEmpty(endIfNodeId)){
					Nodes endIfNode = flow.findNodes(endIfNodeId);
					if("End".equalsIgnoreCase(endIfNode.getType())){
						//如果判断的汇聚节点刚好是结束，结束节点会被替换成空节点放入
						Point offset = notation.findNodeOffset(endIfNodeId);
						drawNode(endIfNode, offset.translate(baseLocation));
						appendList.add(endIfNodeId);
					}
				}
			}
		}
		
		domain.getCommandStack().execute(nodeCmds);
		
		//遍历连线，画线
		Iterator<String> lineids = notation.getLineId();
		while(lineids.hasNext()){
			String lineid = lineids.next();
			Lines line = notation.getLine(lineid);
			int from = getSourceNodeIndex(line, appendList);
			int to = getTargetNodeIndex(line, appendList);
			if(from>=0 && to>=0){
				createConnection(line,baseNodeCount+from,baseNodeCount+to);
			}
		}

		domain.getCommandStack().execute(connCmds);
	}
	
	/**
	 * 传入两个参数，在编辑器中绘制节点
	 * @param node 传入需要绘制的节点
	 * @param point 绘制节点位置
	 */
	private void drawNode(Nodes node,Point point){
		if("Assignment".equalsIgnoreCase(node.getType())){
			Assignment as = LogicFactory.eINSTANCE.createAssignment();
			as.setName(node.getName());
			as.setExternal(node.getExternal());
			as.setComment(node.getComment());
			createNode(LogicElementTypes.Assignment_2003,as, point);
		}else if("Call".equalsIgnoreCase(node.getType())){
			Call call = LogicFactory.eINSTANCE.createCall();
			call.setArgs(node.getArgs());
			call.setComment(node.getComment());
			call.setDisptip(node.getDisptip());
			call.setExternal(node.getExternal());
			call.setFuncName(node.getFuncName());
			call.setRetType(node.getRetType());
			call.setTip(node.getTip());
			call.setName(node.getName());
			createNode(LogicElementTypes.Call_2006,call, point);
		}else if("Bixref".equalsIgnoreCase(node.getType())){
			Bixref bixref = LogicFactory.eINSTANCE.createBixref();
			bixref.setArgs(node.getArgs());
			bixref.setComment(node.getComment());
			bixref.setDisptip(node.getDisptip());
			bixref.setExternal(node.getExternal());
			bixref.setFuncName(node.getFuncName());
			bixref.setRetType(node.getRetType());
			bixref.setTip(node.getTip());
			bixref.setName(node.getName());
			createNode(LogicElementTypes.Bixref_2012,bixref, point);
		}else if("Blank".equalsIgnoreCase(node.getType()) || "End".equalsIgnoreCase(node.getType())){
			Blank blank = LogicFactory.eINSTANCE.createBlank();
			blank.setName(node.getName());
			blank.setExternal(node.getExternal());
			blank.setComment(node.getComment());
			createNode(LogicElementTypes.Blank_2007,blank, point);
		}else if("For".equalsIgnoreCase(node.getType())){
			For forNode = LogicFactory.eINSTANCE.createFor();
			forNode.setName(node.getName());
			forNode.setExternal(node.getExternal());
			forNode.setComment(node.getComment());
			createNode(LogicElementTypes.For_2004,forNode, point);
		}else if("If".equalsIgnoreCase(node.getType())){
			If ifNode = LogicFactory.eINSTANCE.createIf();
			ifNode.setName(node.getName());
			ifNode.setExternal(node.getExternal());
			ifNode.setComment(node.getComment());
			createNode(LogicElementTypes.If_2005,ifNode, point);
		}else if("Transaction".equalsIgnoreCase(node.getType())){
			Transaction transactionNode = LogicFactory.eINSTANCE.createTransaction();
			transactionNode.setName(node.getName());
			transactionNode.setExternal(node.getExternal());
			transactionNode.setComment(node.getComment());
			createNode(LogicElementTypes.Transaction_2008,transactionNode, point);
		}else if("Transcommit".equalsIgnoreCase(node.getType())){
			Transcommit transcommitNode = LogicFactory.eINSTANCE.createTranscommit();
			transcommitNode.setName(node.getName());
			transcommitNode.setExternal(node.getExternal());
			transcommitNode.setComment(node.getComment());
			createNode(LogicElementTypes.Transcommit_2009,transcommitNode, point);
		}else if("Transrollback".equalsIgnoreCase(node.getType())){
			Transrollback transrollbackNode = LogicFactory.eINSTANCE.createTransrollback();
			transrollbackNode.setName(node.getName());
			transrollbackNode.setExternal(node.getExternal());
			transrollbackNode.setComment(node.getComment());
			createNode(LogicElementTypes.Transrollback_2010,transrollbackNode, point);
		}else if("Custom".equalsIgnoreCase(node.getType())){
			Custom custom = LogicFactory.eINSTANCE.createCustom();
			custom.setName(node.getName());
			custom.setExternal(node.getExternal());
			custom.setComment(node.getComment());
			createNode(LogicElementTypes.Custom_2011,custom, point);
		}
	}

	/**
	 * 传入连线及节点集合，返回连线起始节点对应的集合下标
	 * @param line 连线对象
	 * @param list 节点集合对象
	 * @return 连线起始节点在集合中的下标
	 */
	private int getSourceNodeIndex(Lines line,List<String> list){
		int retVal = -1;
		String source = line.getSource();
		String sourceNodeId = cfile.getNotation().getChild(source).getElement();
		retVal = list.indexOf(sourceNodeId);
		return retVal;
	}
	/**
	 * 传入连线及节点集合，返回连线结束节点对应的集合下标
	 * @param line 连线对象
	 * @param list 节点集合对象
	 * @return 连线结束节点在集合中的下标
	 */
	private int getTargetNodeIndex(Lines line,List<String> list){
		int retVal = -1;
		String target = line.getTarget();
		String targetNodeId = cfile.getNotation().getChild(target).getElement();
		retVal = list.indexOf(targetNodeId);
		return retVal;
	}

	
	/**
	 * 生成创建节点的命令并执行
	 * @param type 节点类型
	 * @param node 传入的节点
	 * @param p 节点的位置信息
	 */
	private void createNode(IElementType type,Entity node,Point p){
		CreateElementRequest createElementRequest = new CreateElementRequest(type);
		createElementRequest.setContainer(contaner);
		createElementRequest.setNewElement(node);
		ViewAndElementDescriptor viewDescriptorAssign = new ViewAndElementDescriptor(new CreateElementRequestAdapter(createElementRequest), Node.class, ((IHintedType) type).getSemanticHint(),
				LogicDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);

		viewDescriptorAssign.getCreateElementRequestAdapter().setNewElement(node);
		CreateViewAndElementRequest cveRequest = new CreateViewAndElementRequest(viewDescriptorAssign);
		cveRequest.setLocation(p);
		nodeCmds.add(host.getCommand(cveRequest));
//		domain.getCommandStack().execute(host.getCommand(cveRequest));
	}
	
	/**
	 * 创建连线操作
	 * @param line 连线对象
	 * @param fromIndex 起始节点的下标
	 * @param toIndex 结束节点的下标
	 */
	private void createConnection(Lines line,int fromIndex,int toIndex){

		EditPart source = (EditPart) host.getChildren().get(fromIndex);
		EditPart target = (EditPart) host.getChildren().get(toIndex);
		if("4001".equalsIgnoreCase(line.getType())){
			createNodeLinkConnection(LogicElementTypes.NodeLink_4001,source, target);
		}else if("4002".equalsIgnoreCase(line.getType())){
			createNodeLinkConnection(LogicElementTypes.IfElselink_4002,source, target);
		}else if("4003".equalsIgnoreCase(line.getType())){
			createNodeLinkConnection(LogicElementTypes.ForEndlink_4003,source, target);
		}else if("4004".equalsIgnoreCase(line.getType())){
			createNodeLinkConnection(LogicElementTypes.IfLink_4004,source, target);
		}else if("4006".equalsIgnoreCase(line.getType())){
			createNodeLinkConnection(LogicElementTypes.EntityBreak_4006,source, target);
		}else if("4007".equalsIgnoreCase(line.getType())){
			createNodeLinkConnection(LogicElementTypes.EntityExceptionlink_4007,source, target);
		}else{
			Log.error("没有类型为["+line.getType()+"]的连线");
		}
	}
	
	private void createNodeLinkConnection(IElementType linktype,EditPart from,EditPart to){
		CreateConnectionViewAndElementRequest createConnectionRequest = new CreateConnectionViewAndElementRequest(linktype, ((IHintedType) linktype).getSemanticHint(),
				PreferencesHint.USE_DEFAULTS);

		org.eclipse.gef.commands.Command createConnection = CreateConnectionViewAndElementRequest.getCreateCommand(createConnectionRequest, from, to);
		connCmds.add(createConnection);
//		domain.getCommandStack().execute(createConnection);

	}
	
	/**
	 * 将库中的文件按流的方式读取出来
	 * @param file 如果是系统组件，则保存相对于插件目录bixlib/的路径，如果是用户自定义组件，则为cust:(绝对路径如：D:/folder/bixfile.bix)
	 * @return
	 */
	private InputStream getFileInputStream(String file){
		//Bundle bundle = Platform.getBundle(LogicDiagramEditorPlugin.ID);
		InputStream in =null;
		try {
			if(file.startsWith("cust:")){
				//自定义组件，file保存的为操作系统路径，取文件路径时去掉cust:标记
				file = file.substring(5);
			}else{
				//将引用的库文件放到eclipse目录中，修改文件获取路径
				/*Path path = new Path("bixlib/" + file);//相对于插件根目录。而不是src跟目录。
				URL fileURL = FileLocator.find(bundle, path, null);
				in = fileURL.openStream();*/
				file = FilePathManager.getBixLibDir() + file;
			}
			in = new FileInputStream(file);
		} catch (IOException e) {
			Log.error("获取文件流时异常，" + e.toString());
		}

		return in;
	}
}
