package com.sunsheen.jfids.studio.wther.diagram.part;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionDelegate;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderedShapeEditPart;
import org.eclipse.ui.IObjectActionDelegate;

import com.sunsheen.jfids.studio.logic.Node;
import com.sunsheen.jfids.studio.wther.diagram.figure.NodeFigure;

/**
 * 这是一个设计断点的Action
 * 
 * @author zhouf
 */
public class SetTryAction extends AbstractActionDelegate implements IObjectActionDelegate  {

	@Override
	protected void doRun(IProgressMonitor progressMonitor) {
		
		Object select = getStructuredSelection().getFirstElement();
		
		if (select instanceof AbstractBorderedShapeEditPart) {
			final AbstractBorderedShapeEditPart aep = (AbstractBorderedShapeEditPart) select;
			final Node model = (Node) aep.resolveSemanticElement();
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(model);
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					if (model instanceof Node) {
						model.setStarttry(!model.isStarttry());
					}
				}
			});
			NodeFigure figure = (NodeFigure)aep.getContentPane();
			figure.setTry(model.isStarttry());
		}
	}
}
