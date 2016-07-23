package com.sunsheen.jfids.studio.wther.diagram.edit.parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.common.util.EList;
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
import com.sunsheen.jfids.studio.logging.Log;
import com.sunsheen.jfids.studio.logic.If;
import com.sunsheen.jfids.studio.logic.IfLink;
import com.sunsheen.jfids.studio.logic.impl.FlowImpl;
import com.sunsheen.jfids.studio.logic.impl.IfImpl;
import com.sunsheen.jfids.studio.logic.impl.NodeImpl;
import com.sunsheen.jfids.studio.wther.diagram.compiler.util.TableHiddenType;
import com.sunsheen.jfids.studio.wther.diagram.dialog.IfDialog;
import com.sunsheen.jfids.studio.wther.diagram.edit.layout.CompomentBorderItemLocator;
import com.sunsheen.jfids.studio.wther.diagram.edit.policies.IfItemSemanticEditPolicy;
import com.sunsheen.jfids.studio.wther.diagram.edit.tip.ToolTipForActivityListener;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.GlobalBixUtil;
import com.sunsheen.jfids.studio.wther.diagram.edit.util.vartip.VarStore;
import com.sunsheen.jfids.studio.wther.diagram.figure.IfNodeFigure;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicDiagramEditorPlugin;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicImageUtil;
import com.sunsheen.jfids.studio.wther.diagram.part.LogicVisualIDRegistry;
import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;

/**
 * @generated
 */
public class IfEditPart extends AbstractBorderedShapeEditPart {

	private ConnectionAnchor connectionAnchor;

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 2005;

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
	public IfEditPart(View view) {
		super(view);
	}

	/**
	 * @generated NOT
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new IfItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		// XXX need an SCR to runtime to have another abstract superclass that
		// would let children add reasonable editpolicies
		removeEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE);

		ToolTipForActivityListener toolTipForActivityListener = new ToolTipForActivityListener(
				(IfImpl) ((Node) getModel()).getElement(), this.getEditDomain());
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
				case IfNameEditPart.VISUAL_ID:
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
		return primaryShape = new IfNodeFigure((NodeImpl) view.getElement());
	}

	/**
	 * @generated
	 */
	public IfNodeFigure getPrimaryShape() {
		return (IfNodeFigure) primaryShape;
	}

	/**
	 * @generated NOT
	 */
	protected void addBorderItem(IFigure borderItemContainer, IBorderItemEditPart borderItemEditPart) {
		if (borderItemEditPart instanceof IfNameEditPart) {
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
		return getChildBySemanticHint(LogicVisualIDRegistry.getType(IfNameEditPart.VISUAL_ID));
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMARelTypesOnSource() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(5);
		types.add(LogicElementTypes.NodeLink_4001);
		types.add(LogicElementTypes.EntityExceptionlink_4007);
		types.add(LogicElementTypes.IfElselink_4002);
		types.add(LogicElementTypes.EntityBreak_4006);
		types.add(LogicElementTypes.IfLink_4004);
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
		if (targetEditPart instanceof ForEditPart) {
			types.add(LogicElementTypes.NodeLink_4001);
		}
		if (targetEditPart instanceof com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfEditPart) {
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
		if (targetEditPart instanceof ForEditPart) {
			types.add(LogicElementTypes.EntityExceptionlink_4007);
		}
		if (targetEditPart instanceof com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfEditPart) {
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
		if (targetEditPart instanceof StartEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
		}
		if (targetEditPart instanceof EndEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
		}
		if (targetEditPart instanceof AssignmentEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
		}
		if (targetEditPart instanceof CustomEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
		}
		if (targetEditPart instanceof BlankEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
		}
		if (targetEditPart instanceof ForEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
		}
		if (targetEditPart instanceof com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
		}
		if (targetEditPart instanceof CallEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
		}
		if (targetEditPart instanceof BixrefEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
		}
		if (targetEditPart instanceof TransactionEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
		}
		if (targetEditPart instanceof TranscommitEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
		}
		if (targetEditPart instanceof TransrollbackEditPart) {
			types.add(LogicElementTypes.IfElselink_4002);
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
		if (targetEditPart instanceof ForEditPart) {
			types.add(LogicElementTypes.EntityBreak_4006);
		}
		if (targetEditPart instanceof com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfEditPart) {
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
		if (targetEditPart instanceof StartEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
		}
		if (targetEditPart instanceof EndEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
		}
		if (targetEditPart instanceof AssignmentEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
		}
		if (targetEditPart instanceof CustomEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
		}
		if (targetEditPart instanceof BlankEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
		}
		if (targetEditPart instanceof ForEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
		}
		if (targetEditPart instanceof com.sunsheen.jfids.studio.wther.diagram.edit.parts.IfEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
		}
		if (targetEditPart instanceof CallEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
		}
		if (targetEditPart instanceof BixrefEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
		}
		if (targetEditPart instanceof TransactionEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
		}
		if (targetEditPart instanceof TranscommitEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
		}
		if (targetEditPart instanceof TransrollbackEditPart) {
			types.add(LogicElementTypes.IfLink_4004);
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
		} else if (relationshipType == LogicElementTypes.IfElselink_4002) {
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
			final If ifNode = (If) ((Node) getModel()).getElement();
			FlowImpl flow = (FlowImpl) ((Diagram) ((Node) getModel()).eContainer()).getElement();
			final Map<String, Object> params = paramBuilder(ifNode);
			VarStore varStore = new VarStore(flow.getVarstore());
			varStore.appendGlobalVarEntityList(GlobalBixUtil.getGlobalVarEntityList());
			params.put("varstore", varStore);
			params.put("proposalData", varStore.toProposalArray());

			DialogConfigParser parser = new IfDialog(params);
			final SDialog dialog = parser.getDialog();
			dialog.show(LogicImageUtil.getInstance(), LogicDiagramEditorPlugin.getImage("icons/dialog_logo.gif"),"判断节点配置");

			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(ifNode);
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					if (dialog.getReturnCode() == Dialog.OK) {
						ifNode.setName((String) params.get("nodename"));
						ifNode.setComment((String) params.get("comment"));

						// 获取返回的数据
						@SuppressWarnings("unchecked")
						Map<String, String> dlgValue = coverDlgData((List<Map<Integer, Map<Integer, Object>>>) params.get("table"));

						// 获得与当前节点的所有连线
						EList<IfLink> ifLinks = ifNode.getIflinks();
						for (IfLink link : ifLinks) {
							if (link.getSource().equals(ifNode)) {
								// 如果是当前if节点连接的IfLinks
								String uuid = link.getUuid();
								String dlgValueString = dlgValue.get(uuid);
								// dlgValueString格式如：优先级:名称:条件
								String[] setVal = dlgValueString.split(":");
								if (setVal.length == 3) {
									// 格式正确，分解字串
									link.setPriority(Integer.valueOf(setVal[0]));
									link.setTip(setVal[1]);
									link.setCondition(setVal[2]);
								} else {
									Log.error("当前条件格式设置不正确:" + dlgValueString);
								}
							}
						}

					}
				}
			});

			super.performRequest(request);
		}
	}

	/**
	 * 这是一个处理对话框返回数据的方法
	 * 
	 * @param data
	 *            从对话框返回的表格数据
	 * @return 以条件优先级为键值的条件集合
	 */
	private Map<String, String> coverDlgData(List<Map<Integer, Map<Integer, Object>>> data) {
		Map<String, String> conditions = new HashMap<String, String>();

		Map<Integer, Map<Integer, Object>> inputData = data.get(0);// 取第一个表格数据
		int index = 1;
		for (Entry<Integer, Map<Integer, Object>> eachRow : inputData.entrySet()) {
			// 循环取每行数据，生成需要的格式如：ID=>优先级:名称:条件
			Map<Integer, Object> map = eachRow.getValue();
			// int priority = (Integer) map.get(0);// 获取第1列数据
			String name = (String) map.get(0);
			String condition = (String) map.get(1);
			String ID = (String) map.get(TableHiddenType.CONDITION_UUID);

			conditions.put(ID, index + ":" + name + ":" + condition);
			index++;
		}
		return conditions;
	}

	/**
	 * 为调用对话框准备参数
	 * 
	 * @param ifNode
	 *            传入的节点
	 * @return 返回的参数对象
	 */
	private Map<String, Object> paramBuilder(If ifNode) {
		Map<String, Object> params = new HashMap<String, Object>();

		EList<IfLink> ifLinks = ifNode.getIflinks();

		List<String> tipList = new ArrayList<String>();
		for (IfLink link : ifLinks) {
			tipList.add(link.getTip());
		}

		Map<Integer, Map<Integer, Object>>[] data = new HashMap[1];
		Map<Integer, Map<Integer, Object>> tableInput;

		if (ifLinks.size() == 0) {
			// 没有参数
			tableInput = new HashMap<Integer, Map<Integer, Object>>();
			Map<Integer, Object> row = new HashMap<Integer, Object>();
			row.put(0, "<无>");
			row.put(1, "<未设置>");

			tableInput.put(0, row);
		} else {
			// 将ifLinks中的数据放到table中
			tableInput = new HashMap<Integer, Map<Integer, Object>>();

			for (IfLink link : ifLinks) {
				Map<Integer, Object> row = new HashMap<Integer, Object>();
				int priority = link.getPriority();

				// row.put(0, priority);
				row.put(0, link.getTip());
				row.put(1, link.getCondition());
				row.put(TableHiddenType.CONDITION_UUID, link.getUuid());

				tableInput.put(priority, row);
			}
		}
		data[0] = tableInput;

		params.put("table", data);
		params.put("nodename", ifNode.getName());
		params.put("comment", ifNode.getComment());
		return params;
	}
}
