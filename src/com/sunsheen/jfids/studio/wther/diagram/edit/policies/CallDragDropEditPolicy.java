package com.sunsheen.jfids.studio.wther.diagram.edit.policies;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.MessageDialog;

import com.sunsheen.jfids.studio.logic.Bixref;
import com.sunsheen.jfids.studio.logic.Call;
import com.sunsheen.jfids.studio.logic.LogicFactory;
import com.sunsheen.jfids.studio.logic.impl.CallImpl;
import com.sunsheen.jfids.studio.wther.diagram.edit.commands.ComponentDropCommand;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;

public class CallDragDropEditPolicy extends DragDropEditPolicy {
	
	@Override
	protected Command getDropElementCommand(EObject element,
			DropObjectsRequest request) {
		CompoundCommand cmd = new CompoundCommand();
		CallImpl obj = (CallImpl)element;
		String funcName = obj.getFuncName();
		String external = obj.getExternal();


		if(funcName.toLowerCase().startsWith("ref:")){
			//引用组件库
			String refFileName = funcName.substring(4);
			
			ComponentDropCommand compCmd = new ComponentDropCommand();
			try{
				compCmd.setContaner(((View) getHost().getModel()).getElement());
				compCmd.setDomain(this.getHost().getViewer().getEditDomain());
				compCmd.setHost(this.getHost());
				compCmd.setFile(refFileName);
				compCmd.setBaseLocation(request.getLocation());
			}catch(Exception e){
				//如果有异常产生，说明文件不存在
				MessageDialog.openInformation(null,"文件打开出错","库所引用的文件不存在，请检查");
			}
			
			cmd.add(compCmd);
		}else if(external.startsWith("bixref:")){
			//创建引用节点
			Bixref bixref = LogicFactory.eINSTANCE.createBixref();
			bixref.setArgs(obj.getArgs());
			bixref.setComment(obj.getComment());
			bixref.setDisptip(obj.getDisptip());
			bixref.setExternal(obj.getExternal());
			bixref.setFuncName(obj.getFuncName());
			bixref.setName(obj.getName());
			bixref.setRetType(obj.getRetType());
			bixref.setTip(obj.getTip());
			//bixref.setHide(obj.getHide());
			
			CreateViewAndElementRequest req = createBixrefRequest(bixref);
			
			//创建普通单节点
			req.setLocation(request.getLocation());
			
			cmd.add(getHost().getCommand(req));
		}else{
			Call call = LogicFactory.eINSTANCE.createCall();
			call.setArgs(obj.getArgs());
			call.setComment(obj.getComment());
			call.setDisptip(obj.getDisptip());
			call.setExternal(obj.getExternal());
			call.setFuncName(obj.getFuncName());
			call.setName(obj.getName());
			call.setRetType(obj.getRetType());
			call.setTip(obj.getTip());
			call.setVariadic(obj.getVariadic());
			
			//创建普通单节点
			CreateViewAndElementRequest req = createCallRequest(call);
			req.setLocation(request.getLocation());
			
			cmd.add(getHost().getCommand(req));
		}
		
		return cmd;
	}
	
	/**
	 * 封装一个新建call节点的请求
	 * @param callObj
	 * @return
	 */
	private CreateViewAndElementRequest createCallRequest(Call callObj){
		IElementType type = LogicElementTypes.Call_2006;
		CreateElementRequest cer = new CreateElementRequest(type);
		cer.setContainer(((View) getHost().getModel()).getElement());
		cer.setNewElement(callObj);
		ViewAndElementDescriptor viewDescriptor = new ViewAndElementDescriptor(new CreateElementRequestAdapter(cer),
				Node.class, ((IHintedType) type).getSemanticHint(), LogicDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);

		return new CreateViewAndElementRequest(viewDescriptor);
	}
	
	private CreateViewAndElementRequest createBixrefRequest(Bixref bixrefObj){
		IElementType type = LogicElementTypes.Bixref_2012;
		CreateElementRequest cer = new CreateElementRequest(type);
		cer.setContainer(((View) getHost().getModel()).getElement());
		cer.setNewElement(bixrefObj);
		ViewAndElementDescriptor viewDescriptor = new ViewAndElementDescriptor(new CreateElementRequestAdapter(cer),
				Node.class, ((IHintedType) type).getSemanticHint(), LogicDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);

		return new CreateViewAndElementRequest(viewDescriptor);
	}
}
