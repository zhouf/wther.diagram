package com.sunsheen.jfids.studio.wther.diagram.part;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sunsheen.jfids.studio.wther.diagram.component.views.JavaComponentView;

/**
 * 这是一个监听编辑器的一个类，
 * 
 * @author zhouf
 * 
 */
public class EditorPartListener implements IPartListener {

	private static EditorPartListener listener;

	// private static JavaComponentView viewPart;

	private EditorPartListener() {
		// viewPart = (JavaComponentView)
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(JavaComponentView.ID);
	}

	public static EditorPartListener getInstance() {
		if (listener == null) {
			listener = new EditorPartListener();
		}
		return listener;
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
	}

	private void activityComponentView() {
		// 获得焦点，检查构件库是否有打开，如果没有打开，则自动打开
		JavaComponentView viewPart = (JavaComponentView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(JavaComponentView.ID);
		try {
			if (viewPart == null) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(JavaComponentView.ID);
			} else {
				//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(viewPart);
			}
		} catch (PartInitException ex) {
			ex.printStackTrace();
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		activityComponentView();
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
	}

}
