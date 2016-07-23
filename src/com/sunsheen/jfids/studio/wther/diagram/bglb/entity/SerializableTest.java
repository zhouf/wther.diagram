package com.sunsheen.jfids.studio.wther.diagram.bglb.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class SerializableTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String file = "d:/temp/entity.txt";
		file="D:/eclipse-SDK-3.7.1_GMF/runtime-EclipseApplication/kkkk/resources/uiop/dfgh/src/k/i/jjk/first.bix/.bglb";
		
		BglbFileEntity entity = new BglbFileEntity();
	
		entity.setInterfaceTree(getInput());
		entity.setGlobalVarList(getGlobalInput());
		entity.setDataVarList(getDataVarInput());
		
		ObjectOutputStream oo;
		try {
			oo = new ObjectOutputStream(new FileOutputStream(new File(file)));
			oo.writeObject(entity);
			System.out.println("BglbFileEntity对象序列化成功！");
			oo.close();
			
			
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(file)));
			BglbFileEntity entityObj = (BglbFileEntity) ois.readObject();
			System.out.println("BglbFileEntity对象反序列化成功！");
			
			for(InterfaceTree tree : entityObj.getInterfaceTree()){
				System.out.println("SerializableTest=>main=>tree:" + tree);
			}
			
			for(GlobalVarEntity glb : entityObj.getGlobalVarList()){
				System.out.println("SerializableTest=>main=>glb.getVarName():" + glb.getVarName());
			}
			
			for(DataVarEntity data : entityObj.getDataVarList()){
				System.out.println("SerializableTest=>main=>data.getVarName():" + data.getVarName());
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	private static List<InterfaceTree> getInput() {
		InterfaceTree[] tree = new InterfaceTree[3];
		tree[0] = new InterfaceTree();
		tree[0].setLabelStr("com.sunsheen.jfids.system.bizass.port.IDataPort");
		
		tree[1] = new InterfaceTree();
		tree[1].setLabelStr("com.sunsheen.jfids.system.bizass.port.IDataPort22");
		
		tree[2] = new InterfaceTree();
		tree[2].setLabelStr("com.sunsheen.jfids.system.bizass.port.IDataPort32");
		
		tree[0].getChildren().add(tree[1]);
		tree[0].getChildren().add(tree[2]);
		tree[1].setParent(tree[0]);
		tree[2].setParent(tree[0]);
		List<InterfaceTree> treeList = new ArrayList<InterfaceTree>();
		treeList.add(tree[0]);
		treeList.add(tree[1]);
		treeList.add(tree[2]);
		return treeList;
	}
	
	private static List<GlobalVarEntity> getGlobalInput(){
		List<GlobalVarEntity> retList = new ArrayList<GlobalVarEntity>();
		retList.add(new GlobalVarEntity("glbVar1","float"));
		retList.add(new GlobalVarEntity("glbVar2","int"));
//		retList.add(new GlobalVarEntity("kkkkk3","yyyy"));
//		retList.add(new GlobalVarEntity("kkkkk4","yyyy"));
		return retList;
	}
	
	
	private static List<DataVarEntity> getDataVarInput(){
		List<DataVarEntity> retList = new ArrayList<DataVarEntity>();
		retList.add(new DataVarEntity("dtVar10","type1"));
		retList.add(new DataVarEntity("dtVar2","type1"));
		retList.add(new DataVarEntity("dtVar3","type1"));
		retList.add(new DataVarEntity("dtVar4","type1"));
		retList.add(new DataVarEntity("dtVar5","type1"));
		return retList;
	}
	

}
