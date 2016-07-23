package com.sunsheen.jfids.studio.wther.diagram.part;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

import com.sunsheen.jfids.studio.wther.diagram.providers.LogicElementTypes;

/**
 * @generated
 */
public class LogicPaletteFactory {

	/**
	 * @generated
	 */
	public void fillPalette(PaletteRoot paletteRoot) {
		paletteRoot.add(create连线1Group());
		paletteRoot.add(create基本节点2Group());
		paletteRoot.add(create判断3Group());
		paletteRoot.add(create循环4Group());
		paletteRoot.add(create事务5Group());
	}

	/**
	 * Creates "连线" palette tool group
	 * @generated
	 */
	private PaletteContainer create连线1Group() {
		PaletteDrawer paletteContainer = new PaletteDrawer(Messages.连线1Group_title);
		paletteContainer.setId("create连线1Group"); //$NON-NLS-1$
		paletteContainer.add(create连接C1CreationTool());
		paletteContainer.add(create异常连线2CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "基本节点" palette tool group
	 * @generated NOT
	 */
	private PaletteContainer create基本节点2Group() {
		PaletteDrawer paletteContainer = new PaletteDrawer(Messages.基本节点2Group_title);
		paletteContainer.setId("create基本节点2Group"); //$NON-NLS-1$
		paletteContainer.add(create开始1CreationTool());
		paletteContainer.add(create结束2CreationTool());
		paletteContainer.add(create赋值A3CreationTool());
		//paletteContainer.add(create构件E4CreationTool());
		paletteContainer.add(create逻辑流F5CreationTool());
		paletteContainer.add(create自定义D6CreationTool());
		paletteContainer.add(create空节点B7CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "判断" palette tool group
	 * @generated NOT
	 */
	private PaletteContainer create判断3Group() {
		PaletteDrawer paletteContainer = new PaletteDrawer(Messages.判断3Group_title);
		paletteContainer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
		paletteContainer.setId("create判断3Group"); //$NON-NLS-1$
		paletteContainer.setDescription(Messages.判断3Group_desc);
		paletteContainer.add(create判断S1CreationTool());
		paletteContainer.add(create判断连线G2CreationTool());
		paletteContainer.add(create默认连线Q3CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "循环" palette tool group
	 * @generated NOT
	 */
	private PaletteContainer create循环4Group() {
		PaletteDrawer paletteContainer = new PaletteDrawer(Messages.循环4Group_title);
		paletteContainer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
		paletteContainer.setId("create循环4Group"); //$NON-NLS-1$
		paletteContainer.add(create循环R1CreationTool());
		paletteContainer.add(create结束循环T2CreationTool());
		paletteContainer.add(create中止连线3CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "事务" palette tool group
	 * @generated NOT
	 */
	private PaletteContainer create事务5Group() {
		PaletteDrawer paletteContainer = new PaletteDrawer(Messages.事务5Group_title);
		paletteContainer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
		paletteContainer.setId("create事务5Group"); //$NON-NLS-1$
		paletteContainer.add(create事务开始1CreationTool());
		paletteContainer.add(create提交2CreationTool());
		paletteContainer.add(create回滚3CreationTool());
		return paletteContainer;
	}

	/**
	 * @generated
	 */
	private ToolEntry create连接C1CreationTool() {
		LinkToolEntry entry = new LinkToolEntry(Messages.连接C1CreationTool_title, Messages.连接C1CreationTool_desc,
				Collections.singletonList(LogicElementTypes.NodeLink_4001));
		entry.setId("create连接C1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/nodelink_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * 对外公开创建连接的方法，以便在编辑器里通过快捷键调用
	 * @return
	 */
	public static ToolEntry creationTool(int keyCode) {
		LogicPaletteFactory paletteFactory = new LogicPaletteFactory();
		ToolEntry retTool = paletteFactory.create连接C1CreationTool();
		switch (keyCode) {
		//A(97)-G(103)
		case 97:
			retTool = paletteFactory.create赋值A3CreationTool();
			break;
		case 98:
			retTool = paletteFactory.create空节点B7CreationTool();
			break;
		case 99:
			retTool = paletteFactory.create连接C1CreationTool();
			break;
		case 100:
			retTool = paletteFactory.create自定义D6CreationTool();
			break;
		case 101:
			retTool = paletteFactory.create构件E4CreationTool();
			break;
		case 102:
			retTool = paletteFactory.create逻辑流F5CreationTool();
			break;
		case 103:
			retTool = paletteFactory.create判断连线G2CreationTool();
			break;

		// Q(113)-T(116)
		case 113:
			retTool = paletteFactory.create默认连线Q3CreationTool();
			break;
		case 114:
			retTool = paletteFactory.create循环R1CreationTool();
			break;
		case 115:
			retTool = paletteFactory.create判断S1CreationTool();
			break;
		case 116:
			retTool = paletteFactory.create结束循环T2CreationTool();
			break;
		}
		return retTool;
	}

	/**
	 * @generated
	 */
	private ToolEntry create异常连线2CreationTool() {
		LinkToolEntry entry = new LinkToolEntry(Messages.异常连线2CreationTool_title, Messages.异常连线2CreationTool_desc,
				Collections.singletonList(LogicElementTypes.EntityExceptionlink_4007));
		entry.setId("create异常连线2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/exceptionlink_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create开始1CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.开始1CreationTool_title, Messages.开始1CreationTool_desc,
				Collections.singletonList(LogicElementTypes.Start_2001));
		entry.setId("create开始1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/start_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj32/start_32.png")); //$NON-NLS-1$
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create结束2CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.结束2CreationTool_title, Messages.结束2CreationTool_desc,
				Collections.singletonList(LogicElementTypes.End_2002));
		entry.setId("create结束2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/stop_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj32/stop_32.png")); //$NON-NLS-1$
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create赋值A3CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.赋值A3CreationTool_title, Messages.赋值A3CreationTool_desc,
				Collections.singletonList(LogicElementTypes.Assignment_2003));
		entry.setId("create赋值A3CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/assig_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj32/assig_32.png")); //$NON-NLS-1$
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create构件E4CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.构件E4CreationTool_title, Messages.构件E4CreationTool_desc,
				Collections.singletonList(LogicElementTypes.Call_2006));
		entry.setId("create构件E4CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/comp_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj32/comp_32.png")); //$NON-NLS-1$
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create逻辑流F5CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.逻辑流F5CreationTool_title, Messages.逻辑流F5CreationTool_desc,
				Collections.singletonList(LogicElementTypes.Bixref_2012));
		entry.setId("create逻辑流F5CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/bixref_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj32/bixref_32.png")); //$NON-NLS-1$
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create自定义D6CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.自定义D6CreationTool_title, Messages.自定义D6CreationTool_desc,
				Collections.singletonList(LogicElementTypes.Custom_2011));
		entry.setId("create自定义D6CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/custom_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create空节点B7CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.空节点B7CreationTool_title, Messages.空节点B7CreationTool_desc,
				Collections.singletonList(LogicElementTypes.Blank_2007));
		entry.setId("create空节点B7CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/blank_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj32/blank_32.png")); //$NON-NLS-1$
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create判断S1CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.判断S1CreationTool_title, Messages.判断S1CreationTool_desc,
				Collections.singletonList(LogicElementTypes.If_2005));
		entry.setId("create判断S1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/branch_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj32/branch_32.png")); //$NON-NLS-1$
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create判断连线G2CreationTool() {
		LinkToolEntry entry = new LinkToolEntry(Messages.判断连线G2CreationTool_title, Messages.判断连线G2CreationTool_desc,
				Collections.singletonList(LogicElementTypes.IfLink_4004));
		entry.setId("create判断连线G2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/iflink_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create默认连线Q3CreationTool() {
		LinkToolEntry entry = new LinkToolEntry(Messages.默认连线Q3CreationTool_title, Messages.默认连线Q3CreationTool_desc,
				Collections.singletonList(LogicElementTypes.IfElselink_4002));
		entry.setId("create默认连线Q3CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/ifelselink_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create循环R1CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.循环R1CreationTool_title, Messages.循环R1CreationTool_desc,
				Collections.singletonList(LogicElementTypes.For_2004));
		entry.setId("create循环R1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/loop_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj32/loop_32.png")); //$NON-NLS-1$
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create结束循环T2CreationTool() {
		LinkToolEntry entry = new LinkToolEntry(Messages.结束循环T2CreationTool_title, Messages.结束循环T2CreationTool_desc,
				Collections.singletonList(LogicElementTypes.ForEndlink_4003));
		entry.setId("create结束循环T2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/forendlink_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create中止连线3CreationTool() {
		LinkToolEntry entry = new LinkToolEntry(Messages.中止连线3CreationTool_title, Messages.中止连线3CreationTool_desc,
				Collections.singletonList(LogicElementTypes.EntityBreak_4006));
		entry.setId("create中止连线3CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/breaklink_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create事务开始1CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.事务开始1CreationTool_title, Messages.事务开始1CreationTool_desc,
				Collections.singletonList(LogicElementTypes.Transaction_2008));
		entry.setId("create事务开始1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/transstart_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create提交2CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.提交2CreationTool_title, Messages.提交2CreationTool_desc,
				Collections.singletonList(LogicElementTypes.Transcommit_2009));
		entry.setId("create提交2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/transcommit_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry create回滚3CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(Messages.回滚3CreationTool_title, Messages.回滚3CreationTool_desc,
				Collections.singletonList(LogicElementTypes.Transrollback_2010));
		entry.setId("create回滚3CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(LogicDiagramEditorPlugin
				.findImageDescriptor("/com.sunsheen.jfids.studio.wther.diagram/icons/obj16/transrollback_16.png")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private static class NodeToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List<IElementType> elementTypes;

		/**
		 * @generated
		 */
		private NodeToolEntry(String title, String description, List<IElementType> elementTypes) {
			super(title, description, null, null);
			this.elementTypes = elementTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeCreationTool(elementTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}

	/**
	 * @generated
	 */
	private static class LinkToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List<IElementType> relationshipTypes;

		/**
		 * @generated
		 */
		private LinkToolEntry(String title, String description, List<IElementType> relationshipTypes) {
			super(title, description, null, null);
			this.relationshipTypes = relationshipTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeConnectionTool(relationshipTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}
}
