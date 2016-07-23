package com.sunsheen.jfids.studio.wther.diagram.edit.parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderedShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.BorderItemSelectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.Color;

import com.sunsheen.jfids.studio.dialog.DialogConfigParser;
import com.sunsheen.jfids.studio.dialog.SDialog;
import com.sunsheen.jfids.studio.logic.For;
import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.logic.impl.ForImpl;
import com.sunsheen.jfids.studio.logic.impl.NodeImpl;
import com.sunsheen.jfids.studio.wther.diagram.dialog.ForDialog;
import com.sunsheen.jfids.studio.wther.diagram.edit.layout.CompomentBorderItemLocator;
import com.sunsheen.jfids.studio.wther.diagram.edit.policies.ForItemSemanticEditPolicy;
import com.sunsheen.jfids.studio.wther.diagram.edit.tip.ToolTipForActivityListener;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.Constant;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarItemEntity;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;
import com.sunsheen.jfids.studio.wther.diagram.figure.ForNodeFigure;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicImageUtil;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;

/**
 * @generated
 */
public class ForEditPart extends AbstractBorderedShapeEditPart {

	private ConnectionAnchor connectionAnchor;

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 2004;

	/**
	 * @generated
	 */
	protected IFigure contentPane;

	/**
	 * @generated
	 */
	protected IFigure primaryShape;

	/**
	 * @generated
	 */
	public ForEditPart(View view) {
		super(view);
	}

	/**
	 * @generated NOT
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ForItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		// XXX need an SCR to runtime to have another abstract superclass that
		// would let children add reasonable editpolicies
		removeEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE);

		ToolTipForActivityListener toolTipForActivityListener = new ToolTipForActivityListener(
				(ForImpl) ((Node) getModel()).getElement(), this.getEditDomain());
		getFigure().addMouseMotionListener(toolTipForActivityListener);
	}

	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {
		org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep = new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy() {

			protected EditPolicy createChildEditPolicy(EditPart child) {
				View childView = (View) child.getModel();
				switch (LogicVisualIDRegistry.getVisualID(childView)) {
				case ForNameEditPart.VISUAL_ID:
					return new BorderItemSelectionEditPolicy() {

						protected List createSelectionHandles() {
							MoveHandle mh = new MoveHandle((GraphicalEditPart) getHost());
							mh.setBorder(null);
							return Collections.singletonList(mh);
						}
					};
				}
				EditPolicy result = child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
				if (result == null) {
					result = new NonResizableEditPolicy();
				}
				return result;
			}

			protected Command getMoveChildrenCommand(Request request) {
				return null;
			}

			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}
		};
		return lep;
	}

	/**
	 * @generated NOT
	 */
	protected IFigure createNodeShape() {
		View view = (View) getModel();
		return primaryShape = new ForNodeFigure((NodeImpl) view.getElement());
	}

	/**
	 * @generated
	 */
	public ForNodeFigure getPrimaryShape() {
		return (ForNodeFigure) primaryShape;
	}

	/**
	 * @generated NOT
	 */
	protected void addBorderItem(IFigure borderItemContainer, IBorderItemEditPart borderItemEditPart) {
		if (borderItemEditPart instanceof ForNameEditPart) {
			BorderItemLocator locator = new CompomentBorderItemLocator(getMainFigure(), PositionConstants.SOUTH);
			// locator.setBorderItemOffset(new Dimension(-20, -20));
			borderItemContainer.add(borderItemEditPart.getFigure(), locator);
		} else {
			super.addBorderItem(borderItemContainer, borderItemEditPart);
		}
	}

	/**
	 * @generated
	 */
	protected NodeFigure createNodePlate() {
		DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(32, 32);
		return result;
	}

	/**
	 * @generated
	 */
	public EditPolicy getPrimaryDragEditPolicy() {
		EditPolicy result = super.getPrimaryDragEditPolicy();
		if (result instanceof ResizableEditPolicy) {
			ResizableEditPolicy ep = (ResizableEditPolicy) result;
			ep.setResizeDirections(PositionConstants.NONE);
		}
		return result;
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model so
	 * you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
	protected NodeFigure createMainFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		return figure;
	}

	/**
	 * Default implementation treats passed figure as content pane. Respects
	 * layout one may have set for generated figure.
	 * 
	 * @param nodeShape
	 *            instance of generated figure class
	 * @generated
	 */
	protected IFigure setupContentPane(IFigure nodeShape) {
		return nodeShape; // use nodeShape itself as contentPane
	}

	/**
	 * @generated
	 */
	public IFigure getContentPane() {
		if (contentPane != null) {
			return contentPane;
		}
		return super.getContentPane();
	}

	/**
	 * @generated
	 */
	protected void setForegroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setForegroundColor(color);
		}
	}

	/**
	 * @generated
	 */
	protected void setBackgroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setBackgroundColor(color);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineWidth(int width) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineWidth(width);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineType(int style) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineStyle(style);
		}
	}

	/**
	 * @generated
	 */
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(LogicVisualIDRegistry.getType(ForNameEditPart.VISUAL_ID));
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMARelTypesOnSource() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(4);
		types.add(LogicElementTypes.NodeLink_4001);
		types.add(LogicElementTypes.EntityExceptionlink_4007);
		types.add(LogicElementTypes.ForEndlink_4003);
		types.add(LogicElementTypes.EntityBreak_4006);
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMARelTypesOnSourceAndTarget(IGraphicalEditPart targetEditPart) {
		LinkedList<IElementType> types = new LinkedList<IElementType>();
		if (targetEditPart instanceof StartEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof EndEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof AssignmentEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof CustomEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof BlankEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof IfEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof CallEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof BixrefEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof TransactionEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof TranscommitEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof TransrollbackEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof AssignmentEditPart) {
			types.add(LogicElementTypes.EntityExceptionlink_4007);
		}
		if (targetEditPart instanceof CustomEditPart) {
			types.add(LogicElementTypes.EntityExceptionlink_4007);
		}
		if (targetEditPart instanceof BlankEditPart) {
			types.add(LogicElementTypes.EntityExceptionlink_4007);
		}
		if (targetEditPart instanceof com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForEditPart) {
			types.add(LogicElementTypes.EntityExceptionlink_4007);
		}
		if (targetEditPart instanceof IfEditPart) {
			types.add(LogicElementTypes.EntityExceptionlink_4007);
		}
		if (targetEditPart instanceof CallEditPart) {
			types.add(LogicElementTypes.EntityExceptionlink_4007);
		}
		if (targetEditPart instanceof BixrefEditPart) {
			types.add(LogicElementTypes.EntityExceptionlink_4007);
		}
		if (targetEditPart instanceof TransactionEditPart) {
			types.add(LogicElementTypes.EntityExceptionlink_4007);
		}
		if (targetEditPart instanceof TranscommitEditPart) {
			types.add(LogicElementTypes.EntityExceptionlink_4007);
		}
		if (targetEditPart instanceof TransrollbackEditPart) {
			types.add(LogicElementTypes.EntityExceptionlink_4007);
		}
		if (targetEditPart instanceof AssignmentEditPart) {
			types.add(LogicElementTypes.ForEndlink_4003);
		}
		if (targetEditPart instanceof CustomEditPart) {
			types.add(LogicElementTypes.ForEndlink_4003);
		}
		if (targetEditPart instanceof BlankEditPart) {
			types.add(LogicElementTypes.ForEndlink_4003);
		}
		if (targetEditPart instanceof com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForEditPart) {
			types.add(LogicElementTypes.ForEndlink_4003);
		}
		if (targetEditPart instanceof IfEditPart) {
			types.add(LogicElementTypes.ForEndlink_4003);
		}
		if (targetEditPart instanceof CallEditPart) {
			types.add(LogicElementTypes.ForEndlink_4003);
		}
		if (targetEditPart instanceof BixrefEditPart) {
			types.add(LogicElementTypes.ForEndlink_4003);
		}
		if (targetEditPart instanceof TransactionEditPart) {
			types.add(LogicElementTypes.ForEndlink_4003);
		}
		if (targetEditPart instanceof TranscommitEditPart) {
			types.add(LogicElementTypes.ForEndlink_4003);
		}
		if (targetEditPart instanceof TransrollbackEditPart) {
			types.add(LogicElementTypes.ForEndlink_4003);
		}
		if (targetEditPart instanceof AssignmentEditPart) {
			types.add(LogicElementTypes.EntityBreak_4006);
		}
		if (targetEditPart instanceof CustomEditPart) {
			types.add(LogicElementTypes.EntityBreak_4006);
		}
		if (targetEditPart instanceof BlankEditPart) {
			types.add(LogicElementTypes.EntityBreak_4006);
		}
		if (targetEditPart instanceof com.sunsheen.jfids.studio.wther.diagram.edit.parts.ForEditPart) {
			types.add(LogicElementTypes.EntityBreak_4006);
		}
		if (targetEditPart instanceof IfEditPart) {
			types.add(LogicElementTypes.EntityBreak_4006);
		}
		if (targetEditPart instanceof CallEditPart) {
			types.add(LogicElementTypes.EntityBreak_4006);
		}
		if (targetEditPart instanceof BixrefEditPart) {
			types.add(LogicElementTypes.EntityBreak_4006);
		}
		if (targetEditPart instanceof TransactionEditPart) {
			types.add(LogicElementTypes.EntityBreak_4006);
		}
		if (targetEditPart instanceof TranscommitEditPart) {
			types.add(LogicElementTypes.EntityBreak_4006);
		}
		if (targetEditPart instanceof TransrollbackEditPart) {
			types.add(LogicElementTypes.EntityBreak_4006);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMATypesForTarget(IElementType relationshipType) {
		LinkedList<IElementType> types = new LinkedList<IElementType>();
		if (relationshipType == LogicElementTypes.NodeLink_4001) {
			types.add(LogicElementTypes.Start_2001);
			types.add(LogicElementTypes.End_2002);
			types.add(LogicElementTypes.Assignment_2003);
			types.add(LogicElementTypes.Custom_2011);
			types.add(LogicElementTypes.Blank_2007);
			types.add(LogicElementTypes.For_2004);
			types.add(LogicElementTypes.If_2005);
			types.add(LogicElementTypes.Call_2006);
			types.add(LogicElementTypes.Bixref_2012);
			types.add(LogicElementTypes.Transaction_2008);
			types.add(LogicElementTypes.Transcommit_2009);
			types.add(LogicElementTypes.Transrollback_2010);
		} else if (relationshipType == LogicElementTypes.EntityExceptionlink_4007) {
			types.add(LogicElementTypes.Assignment_2003);
			types.add(LogicElementTypes.Custom_2011);
			types.add(LogicElementTypes.Blank_2007);
			types.add(LogicElementTypes.For_2004);
			types.add(LogicElementTypes.If_2005);
			types.add(LogicElementTypes.Call_2006);
			types.add(LogicElementTypes.Bixref_2012);
			types.add(LogicElementTypes.Transaction_2008);
			types.add(LogicElementTypes.Transcommit_2009);
			types.add(LogicElementTypes.Transrollback_2010);
		} else if (relationshipType == LogicElementTypes.ForEndlink_4003) {
			types.add(LogicElementTypes.Assignment_2003);
			types.add(LogicElementTypes.Custom_2011);
			types.add(LogicElementTypes.Blank_2007);
			types.add(LogicElementTypes.For_2004);
			types.add(LogicElementTypes.If_2005);
			types.add(LogicElementTypes.Call_2006);
			types.add(LogicElementTypes.Bixref_2012);
			types.add(LogicElementTypes.Transaction_2008);
			types.add(LogicElementTypes.Transcommit_2009);
			types.add(LogicElementTypes.Transrollback_2010);
		} else if (relationshipType == LogicElementTypes.EntityBreak_4006) {
			types.add(LogicElementTypes.Assignment_2003);
			types.add(LogicElementTypes.Custom_2011);
			types.add(LogicElementTypes.Blank_2007);
			types.add(LogicElementTypes.For_2004);
			types.add(LogicElementTypes.If_2005);
			types.add(LogicElementTypes.Call_2006);
			types.add(LogicElementTypes.Bixref_2012);
			types.add(LogicElementTypes.Transaction_2008);
			types.add(LogicElementTypes.Transcommit_2009);
			types.add(LogicElementTypes.Transrollback_2010);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMARelTypesOnTarget() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(6);
		types.add(LogicElementTypes.NodeLink_4001);
		types.add(LogicElementTypes.EntityExceptionlink_4007);
		types.add(LogicElementTypes.IfElselink_4002);
		types.add(LogicElementTypes.ForEndlink_4003);
		types.add(LogicElementTypes.EntityBreak_4006);
		types.add(LogicElementTypes.IfLink_4004);
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMATypesForSource(IElementType relationshipType) {
		LinkedList<IElementType> types = new LinkedList<IElementType>();
		if (relationshipType == LogicElementTypes.NodeLink_4001) {
			types.add(LogicElementTypes.Start_2001);
			types.add(LogicElementTypes.End_2002);
			types.add(LogicElementTypes.Assignment_2003);
			types.add(LogicElementTypes.Custom_2011);
			types.add(LogicElementTypes.Blank_2007);
			types.add(LogicElementTypes.For_2004);
			types.add(LogicElementTypes.If_2005);
			types.add(LogicElementTypes.Call_2006);
			types.add(LogicElementTypes.Bixref_2012);
			types.add(LogicElementTypes.Transaction_2008);
			types.add(LogicElementTypes.Transcommit_2009);
			types.add(LogicElementTypes.Transrollback_2010);
		} else if (relationshipType == LogicElementTypes.EntityExceptionlink_4007) {
			types.add(LogicElementTypes.Assignment_2003);
			types.add(LogicElementTypes.Custom_2011);
			types.add(LogicElementTypes.Blank_2007);
			types.add(LogicElementTypes.For_2004);
			types.add(LogicElementTypes.If_2005);
			types.add(LogicElementTypes.Call_2006);
			types.add(LogicElementTypes.Bixref_2012);
			types.add(LogicElementTypes.Transaction_2008);
			types.add(LogicElementTypes.Transcommit_2009);
			types.add(LogicElementTypes.Transrollback_2010);
		} else if (relationshipType == LogicElementTypes.IfElselink_4002) {
			types.add(LogicElementTypes.If_2005);
		} else if (relationshipType == LogicElementTypes.ForEndlink_4003) {
			types.add(LogicElementTypes.For_2004);
		} else if (relationshipType == LogicElementTypes.EntityBreak_4006) {
			types.add(LogicElementTypes.Assignment_2003);
			types.add(LogicElementTypes.Custom_2011);
			types.add(LogicElementTypes.Blank_2007);
			types.add(LogicElementTypes.For_2004);
			types.add(LogicElementTypes.If_2005);
			types.add(LogicElementTypes.Call_2006);
			types.add(LogicElementTypes.Bixref_2012);
			types.add(LogicElementTypes.Transaction_2008);
			types.add(LogicElementTypes.Transcommit_2009);
			types.add(LogicElementTypes.Transrollback_2010);
		} else if (relationshipType == LogicElementTypes.IfLink_4004) {
			types.add(LogicElementTypes.If_2005);
		}
		return types;
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connEditPart) {
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connEditPart) {
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	private ConnectionAnchor getConnectionAnchor() {
		if (connectionAnchor == null) {
			connectionAnchor = new EllipseAnchor(getNodeFigure());
		}
		return connectionAnchor;
	}

	@Override
	public void performRequest(Request request) {
		if (request.getType().equals(REQ_OPEN)) {
			final For forNode = (For) ((Node) getModel()).getElement();
			// 获得FLOW，并通过 FLOW获取页面的变量定义
			final FlowImpl flow = (FlowImpl) ((Diagram) ((Node) getModel()).eContainer()).getElement();
			final Map<String, Object> params = new HashMap<String, Object>();
			params.put("nodename", forNode.getName());
			params.put("comment", forNode.getComment());
			String ext = forNode.getExternal();
			if (ext != null && (ext.toLowerCase().startsWith(Constant.LOOP_SIMPLE_MARK) || ext.toLowerCase().startsWith("[i]"))) {
				// 与[i]比较是为了兼容之前的版本
				params.put("simpleRadio", "true");
				params.put("ordinaryRadio", "false");

				String iter[] = ext.substring(ext.indexOf("]")+1).split(":");
				if (iter.length >= 2) {
					params.put("simpleVar", iter[0]);
					params.put("simpleTimes", iter[1]);
				}
			} else {
				params.put("simpleRadio", "false");
				params.put("ordinaryRadio", "true");

				params.put("textCode", ext);

			}
			
			params.put("varstore", new VarStore(flow.getVarstore()));
			params.put("definedVarSet", getDefinedVarSet(flow,forNode));

			DialogConfigParser parser = new ForDialog(params);
			final SDialog dialog = parser.getDialog();
			dialog.show(LogicImageUtil.getInstance(), LogicDiagramEditorPlugin.getImage("icons/dialog_logo.gif"),"循环配置");

			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(forNode);
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					if (dialog.getReturnCode() == Dialog.OK) {
						forNode.setName((String) params.get("nodename"));
						forNode.setComment((String) params.get("comment"));
						boolean simpleRadio = (Boolean) params.get("simpleRadio");
						if (simpleRadio) {
							// 如果是选择迭代操作
							forNode.setExternal(Constant.LOOP_SIMPLE_MARK + (String) params.get("simpleVar") + ":"
									+ (String) params.get("simpleTimes"));
						} else {
							forNode.setExternal((String) params.get("textCode"));
						}
					}
				}
			});

			super.performRequest(request);
		}
	}

	/**
	 * 返回当前flow对象中已存在的变量列表，包含flow对象中的变量和其他for节点中的变量
	 * @param flow 当前模型对象，用于获取变量定义和节点集合
	 * @param forNode 当前for节点，用于区分其他for节点
	 * @return
	 */
	private Set<String> getDefinedVarSet(FlowImpl flow, For forNode) {
		Set<String> retSet = new HashSet<String>();
		VarStore varStore = new VarStore(flow.getVarstore());
		for(VarItemEntity varItem : varStore.listAll()){
			retSet.add(varItem.getVarName());
		}
		
		//遍历其他for节点，并解析出变量
		for(com.sunsheen.jfids.studio.logic.Node node : flow.getNodes()){
			if(node instanceof ForImpl && node!=forNode){
				String var = "";
				String ext = ((ForImpl) node).getExternal();
				if(StringUtils.isNotBlank(ext)){
					
					if(ext.startsWith(Constant.LOOP_SIMPLE_MARK)){
						//[-sp-]i:100 枚举类型变量是已定义过的，不解析
						continue;
					}else if(ext.startsWith("for")){
						//for(...
						int firstMark = ext.indexOf(";");
						if(firstMark>0){
							ext = ext.substring(0,firstMark);
						}
						String arr[] = StringUtils.split(ext, " =(");
						if(arr.length>2){
							var = arr[arr.length-2];
						}
					}else if(ext.contains("while")){
						//while,do-while
						int firstMark = ext.indexOf("while");
						if(firstMark>=0){
							ext = ext.substring(firstMark);
						}
						String arr[] = StringUtils.split(ext, " <>=()");
						if(arr.length>2){
							var = arr[1];
						}
					}
				}else{
					//log.warn("ForEditPart.getDefinedVarSet()->:从循环节点解析变量异常：" + ext);
				}
				
				if(StringUtils.isNotBlank(var)){
					retSet.add(var);
				}
			}
		}
		
		return retSet;
	}
}
