package com.sunsheen.jfids.studio.wther.diagram.compiler.item;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.draw2d.geometry.Point;

public class Notation {
	private HashMap<String, Children> childMap = new HashMap<String, Children>();
	private HashMap<String, Lines> lineMap = new HashMap<String, Lines>();

	public void addChildren(Children children){
		childMap.put(children.getId(), children);
	}
	
	public void addLines(Lines edges){
		lineMap.put(edges.getId(), edges);
	}
	
	public String findLinkType(String sourceElement,String targetElement){
		String retVal = null;
		String sourceId = elementToId(sourceElement);
		String targetId = elementToId(targetElement);
		retVal = getLineType(sourceId, targetId);
		return retVal;
	}
	private String elementToId(String element){
		String retVal = null;
		Iterator<String> it = childMap.keySet().iterator();
		while(it.hasNext()){
			Children child = childMap.get(it.next());
			if(element.equalsIgnoreCase(child.getElement())){
				retVal = child.getId();
				break;
			}
		}
		return retVal;
	}
	
	private String getLineType(String source,String target){
		String retVal = null;
		Iterator<String> it = lineMap.keySet().iterator();
		while(it.hasNext()){
			Lines line = lineMap.get(it.next());
			if(source.equalsIgnoreCase(line.getSource()) && target.equalsIgnoreCase(line.getTarget())){
				retVal = line.getType();
				break;
			}
		}
		return retVal;
	}
	/**
	 * 返回所有的连线ID
	 * @return
	 */
	public Iterator<String> getLineId(){
		return lineMap.keySet().iterator();
	}
	
	/**
	 * 返回所有的节点ID
	 * @return
	 */
	public Iterator<String> getChildrenId(){
		return childMap.keySet().iterator();
	}
	
	public Lines getLine(String lineid){
		return lineMap.get(lineid);
	}
	
	public Children getChild(String childid){
		return childMap.get(childid);
	}
	
	public void setChildMap(HashMap<String, Children> childMap) {
		this.childMap = childMap;
	}

	public void setChild(String childid,Children child){
		childMap.put(childid, child);
	}
	
	/**
	 * 返回nodeid的坐标
	 * @param nodeid
	 * @return
	 */
	public Point findNodeOffset(String nodeid){
		Point p = new Point();
		Children child = childMap.get(this.elementToId(nodeid));
		p.setX(child.x);
		p.setY(child.y);
		return p;
	}
}
