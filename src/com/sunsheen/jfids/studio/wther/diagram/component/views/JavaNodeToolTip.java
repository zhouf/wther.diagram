package com.sunsheen.jfids.studio.wther.diagram.component.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;


public class JavaNodeToolTip {

	JavaComponentTreeNode node = null;
	private final Control control;
	Shell treeItemTip = null;
	Widget widget;
	
	public JavaNodeToolTip(Control control) {
		this.control = control;
		ToolTipOwnerControlListener listener = new ToolTipOwnerControlListener();
		control.addListener(SWT.MouseHover, listener);
		control.addListener(SWT.MouseExit, listener);
	}
	
	private class ToolTipOwnerControlListener implements Listener {
		public void handleEvent(Event event) {
			switch (event.type) {
			case SWT.MouseMove:
				createToolTipContentArea(event);
				break;
			case SWT.MouseHover:
				createToolTipContentArea(event);
				break;
			case SWT.MouseExit:
				close();
				break;
			}
		}

	}
	
	private void close() {
		if (treeItemTip != null) {
			treeItemTip.dispose();
		}

	}

	protected void createToolTipContentArea(Event event) {
		Widget widgetEvent = getTipWidget(event);
		if (widget == widgetEvent) {
			return;
		} else {
			widget = widgetEvent;
		}
		if (treeItemTip != null) {
			treeItemTip.dispose();
		}
		node = getJavaComponentTreeNode(widgetEvent);
		if (node == null) {
			return;
		}
		final Display display = PlatformUI.getWorkbench().getDisplay();
		if (treeItemTip != null && !treeItemTip.isDisposed()) {
			return;
		}
		treeItemTip = new Shell(display, SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
		treeItemTip.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		treeItemTip.setLayout(layout);
		// Label imglabel = new Label(treeItemTip, SWT.NONE);
		// imglabel.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK));
		// imglabel.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		// imglabel.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		Label label = new Label(treeItemTip, SWT.NONE);
		label.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		label.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		label.setText(node.getTip()==null ? "No Tip..." : node.getTip());
		Point size = treeItemTip.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		Point p = control.toDisplay(event.x, event.y);
		treeItemTip.setBounds(p.x-size.x, p.y + 20, size.x, size.y);
		treeItemTip.setVisible(true);
	}
	
	
    private JavaComponentTreeNode getJavaComponentTreeNode(Widget hoverObject) {
        if (hoverObject instanceof Widget) {
            Object data = (hoverObject).getData();
            if (data != null) {
                if (data instanceof JavaComponentTreeNode) {
                    return (JavaComponentTreeNode) data;
                }
            }
        }

        return null;
    }
    
    
    private Widget getTipWidget(Event event) {
        Point widgetPosition = new Point(event.x, event.y);
        Widget widget = event.widget;
        if (widget instanceof Tree) {
        	Tree w = (Tree) widget;
            return w.getItem(widgetPosition);
        }

        return widget;
    }
    
    
    /**
	 * 将字串分段，按字符个数插入换行符
	 * @param str 输入待处理的字串
	 * @param capacity 每行容纳的字符数
	 * @return String 返回添加换行符之后的字串
	 */
	public static String strSpitLine(String str, int capacity) {
    	StringBuilder sb = new StringBuilder(str);
    	int len = str.length();
		if (len > capacity) {
			for (int i = 0; i < len / capacity; i++) {
				sb.insert((i+1)*capacity, "\n");
			}
		}
		return sb.toString();
	}

}
