package com.sunsheen.jfids.studio.wther.diagram.dialog.validator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.sunsheen.jfids.studio.dialog.agilegrid.AgileGrid;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateListener;
import com.sunsheen.jfids.studio.dialog.compoments.listeners.ValidateStatus;
import com.sunsheen.jfids.studio.dialog.compoments.table.TableCompoment.InnerTableContentProvider;
/**
 * 这是一个对判断节点的验证
 * @author zhoufeng
 * Date:2013-6-20
 */
public class IfDlgValidator implements ValidateListener {

	@Override
	public ValidateStatus validate(Map<String, Widget> coms, Map<String, Object> param) {
		ValidateStatus status=new ValidateStatus();
		//Log.debug("IfDlgValidator validate()-> start");
		
		Text nodeName=(Text)coms.get("nodename");
		
		//如果ID输入框内容为空，提示出错
		if(nodeName!=null && StringUtils.isEmpty(nodeName.getText())){
			status.setStatus(ValidateStatus.FAILURE);			
			status.setMessages(new String[]{"'节点名'不能为空"});
		}
		//*
		AgileGrid table=(AgileGrid)coms.get("table");	
		InnerTableContentProvider provider=(InnerTableContentProvider) table.getContentProvider();
		if(provider.data.size()>0){
			//有数据，获取表格数据

			//set用于验证多个条件名相同的情况
			Set<String> conditionLinkNameSet = new HashSet<String>();
			
			Map<Integer,Map<Integer,Object>> map=provider.data.get(0);
			List<Integer> keys=Arrays.asList(map.keySet().toArray(new Integer[map.keySet().size()]));
			Iterator<Integer> iterator = keys.iterator();
			while(iterator.hasNext()){
				Map<Integer,Object> row = map.get(iterator.next());
				String lineName = (row.get(0)==null? "" : row.get(0).toString());//连线名
				String condition = (row.get(1)==null? "" : row.get(1).toString());//条件
//				Log.debug("IfDlgValidator validate()-> lineName:" + lineName + " condition:" + condition );
				if(StringUtils.isNotEmpty(lineName)){
					
					//验证条件连线名不重复，条件名重复不影响程序运行，会影响程序图的可阅读性
					if(conditionLinkNameSet.contains(lineName)){
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[]{"连线名【"+lineName+"】重复，请检查"});
						
						break;
					}else{
						conditionLinkNameSet.add(lineName);
					}
					
					//连线不为空，判断条件设置
					if(StringUtils.isEmpty(condition)){
						//条件未设置
						status.setStatus(ValidateStatus.FAILURE);
						status.setMessages(new String[]{"连线【"+lineName+"】条件未设置，请检查"});
	
						break;
					}
				}else{
					//连线名为空，提示错误信息
					status.setStatus(ValidateStatus.FAILURE);
					status.setMessages(new String[]{"连线名称未设定，请检查"});
					break;
				}
			}
			
		}else{
			//没有数据，不进行验证
			status.setStatus(ValidateStatus.SUCCESS);
		}
		//*/
		
		return status;
	}

}
