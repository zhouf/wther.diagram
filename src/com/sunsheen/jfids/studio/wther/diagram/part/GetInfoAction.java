package com.sunsheen.jfids.studio.wther.diagram.part;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sunsheen.jfids.studio.logic.Flow;
import com.sunsheen.jfids.studio.wther.diagram.edit.parts.FlowEditPart;
import com.sunsheen.jfids.studio.wther.diagram.parser.FlowArgParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.FlowRetParser;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.FlowArgItem;
import com.sunsheen.jfids.studio.wther.diagram.parser.item.FlowRetItem;

/**
 * 这是一个调试运行的Action
 * 
 * @author zhouf
 */
public class GetInfoAction extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
//		IEditorPart editPart = HandlerUtil.getActiveEditorChecked(event);
		Object obj = HandlerUtil.getCurrentSelection(event);

		Object select = null;
		if (obj != null) {
			select = ((StructuredSelection) obj).getFirstElement();
		}
		if (select instanceof FlowEditPart) {
			// FlowEditPart flowep = (FlowEditPart)select;
			Flow flow = (Flow) ((FlowEditPart) select).resolveSemanticElement();
			String args = flow.getArgs();
			String rets = flow.getRet();
			String comment = flow.getComment();

			StringBuilder sb = new StringBuilder();
			sb.append("【页面基本信息】").append("\n");
			
			sb.append("参数：").append("\n");
			if(StringUtils.isNotBlank(args)){
				FlowArgParser flowArgParser = new FlowArgParser(args);
				for(FlowArgItem item : flowArgParser.getItemSet()){
					sb.append("    ").append(item.getValName()).append("[").append(item.getType()).append("]").append(item.getComment()).append("\n");
				}
			}else{
				sb.append("    ").append("\n");
			}
			
			sb.append("返回：").append("\n");
			if(StringUtils.isNotBlank(rets)){
				FlowRetParser flowArgParser = new FlowRetParser(rets);
				for(FlowRetItem	item : flowArgParser.getItemSet()){
					sb.append("    ").append(item.getValName()).append("[").append(item.getType()).append("]").append(item.getComment()).append("\n");
				}
			}else{
				sb.append("    ").append("\n");
			}
			
			sb.append("描述：").append("\n");
			sb.append("    ").append(comment);
			MessageDialog.openInformation(null, "页面流构件库", "信息已放入剪贴板");
			
			Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable tText = new StringSelection(sb.toString()); 
			sysClip.setContents(tText, null); 
			
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
