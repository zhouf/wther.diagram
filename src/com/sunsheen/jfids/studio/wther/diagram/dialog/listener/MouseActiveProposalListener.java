package com.sunsheen.jfids.studio.wther.diagram.dialog.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Event;

/**
 * 用鼠标事件激活变量提示
 * @author zhouf
 */
public class MouseActiveProposalListener implements MouseListener {

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		//通过双击模拟键盘变量提示按键Alt+/
		
		
		Event event = new Event();
		event.type = SWT.KeyDown;
		event.keyCode = SWT.ALT;
		e.display.post(event);
		
		event = new Event();
		event.type = SWT.KeyDown;
	    event.character = '/';
	    e.display.post(event);
	    
	    try {
		     Thread.sleep(10);
		} catch (InterruptedException ex) {}
	    
	    event.type = SWT.KeyUp;
	    e.display.post(event);
	    
	    try {
		     Thread.sleep(10);
		} catch (InterruptedException ex) {}
	    
	    event = new Event();
	    event.type = SWT.KeyUp;
	    event.keyCode = SWT.ALT;
	    e.display.post(event); 
	}

	@Override
	public void mouseDown(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
