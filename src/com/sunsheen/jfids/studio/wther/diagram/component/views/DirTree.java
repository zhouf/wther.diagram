package com.sunsheen.jfids.studio.wther.diagram.component.views;

import java.util.ArrayList;
import java.util.List;
/**
 * 用于构造视图树的数据模型
 * @author xh
 *
 */
public class DirTree implements ITree {
	private String name;
	@SuppressWarnings("rawtypes")
	private List children;

	@SuppressWarnings("rawtypes")
	public DirTree() {
		children = new ArrayList();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setChildren(List children) {
		this.children = children;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getChildren() {
		return this.children;
	}
	
	@SuppressWarnings("unchecked")
	public void addChildren(ITree treeNode){
		String treeName = treeNode.getName();
		boolean append = false;
		if(treeNode instanceof DirTree){
			int index = 0;
			for(Object each : this.children){
				if(each instanceof DirTree){
					
					DirTree tmpTree = (DirTree)each;
					if(tmpTree.getName().equals(treeName)){
						
						//节点复制，递归合并多级目录
						for(Object obj : treeNode.getChildren()){
							if(obj instanceof ITree){
								tmpTree.addChildren((ITree)obj);
							}
						}
						//下面只合并第一级目录
						//tmpTree.getChildren().addAll(treeNode.getChildren());
						
						this.children.set(index, tmpTree);
						
						append = true;
						break;
					}
				}
				index++;
			}
		}
		if(!append){
			this.children.add(treeNode);
		}
	}
	
	public boolean hasChildren(){
		return this.children.size()!=0;
	}
}
