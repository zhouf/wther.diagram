package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.util.Map;

import org.eclipse.ui.PlatformUI;

import com.sunsheen.jfids.studio.wther.diagram.component.views.JavaComponentView;


/**
 * 接收从业务逻辑流逻辑器发来的变量集合varMap,将此对象传递给业务构件库，刷新变量显示
 * 
 * @author zhoufeng Date:2013-6-28
 */
public class RefreshVarTreeAdapter {
	static final org.apache.log4j.Logger log = com.sunsheen.jfids.studio.logging.LogFactory.getLogger(RefreshVarTreeAdapter.class.getName());

	// 记录上次激活这个操作的打开文件，如果是同一文件激活的，则不用刷新变量，解决页面获得焦点时变量树收缩的问题
	private static String lastActionFile = "";
    /**
     * 
     * @param varMap
     * @param actionFile 业务逻辑流文件路径
     * @param forceRefresh 同一页面是否强制刷新
     */
	@SuppressWarnings("rawtypes")
	public static void transferMap(Map<String, Class> varMap, String actionFile, boolean forceRefresh) {
		if (!lastActionFile.equalsIgnoreCase(actionFile) || forceRefresh) {
			JavaComponentView viewPart = (JavaComponentView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.findView(JavaComponentView.ID);
			//					com.sunsheen.jfids.studio.wther.diagram.compiler.views.JavaComponentView
			if(viewPart!=null){
				//viewPart为空时，说明业务构件库窗口没打开
				viewPart.resetVariableTree(varMap);
				lastActionFile = actionFile;
			}
		} else {
			log.debug("RefreshVarTreeAdapter.transferMap -> 同一页面，不重新激活变量列表" + actionFile);
		}
	}

	/**
	 * 重置变量树
	 */
	public static void reset() {
		JavaComponentView viewPart = (JavaComponentView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView("com.sunsheen.jfids.studio.wther.diagram.compiler.views.JavaComponentView");
		viewPart.resetVariableTree(null);
	}
}
