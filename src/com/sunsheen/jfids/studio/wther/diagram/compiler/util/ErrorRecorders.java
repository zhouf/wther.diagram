package com.sunsheen.jfids.studio.wther.diagram.compiler.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 这是记录工程中编译出错的信息
 * @author zhouf
 */
public class ErrorRecorders implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//记录所有文件的编译信息<文件uri,<行号,错误信息>>
	private Map<String, Map<Integer, String>> errorMap = new HashMap<String, Map<Integer,String>>();

	
	protected void appendInfo(String fileUri,Integer lineNum,String msg){
		if(errorMap.containsKey(fileUri)){
			Map<Integer,String> map = errorMap.get(fileUri);
			map.put(lineNum, msg);
		}else{
			Map<Integer,String> map = new HashMap<Integer,String>();
			map.put(lineNum, msg);
			errorMap.put(fileUri, map);
		}
	}

	/**
	 * 返回给定文件的错误信息集合
	 * @param fileUri 指定的文件URI，此处为LogicValidationProvider调用，会有包含platform:/resource前缀
	 * @return 包含错误信息的Map对象
	 */
	protected Map<Integer,String> getMapByFile(String fileUri){
		fileUri = curPrefix(fileUri);
		if(errorMap.containsKey(fileUri)){
			return errorMap.get(fileUri);
		}else{
			return null;
		}
	}
	
	protected void removeMapByFile(String fileUri){
		if(errorMap.containsKey(fileUri)){
			errorMap.remove(fileUri);
		}
	}
	
	/**
	 * 去掉uri前缀
	 * @param uri platform:/resource/HK/resources/Fun/Mood/src/data/test2.bix/getObject.bix
	 * @return /HK/resources/Fun/Mood/src/data/test2.bix/getObject.bix
	 */
	private String curPrefix(String uri){
		String prefix = "platform:/resource";
		if(uri.startsWith(prefix)){
			return uri.substring(prefix.length());
		}
		return uri;
	}

}
