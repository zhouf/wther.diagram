package com.sunsheen.jfids.studio.wther.debug;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sunsheen.jfids.studio.javaassist.bytecode.AttributeInfo;
import com.sunsheen.jfids.studio.javaassist.bytecode.ClassFile;
import com.sunsheen.jfids.studio.logging.Log;

/**
 * 
 * @author litao
 * 
 */
public class SourceDebugExtensionUtils {
	public static String NL = "\n";
	public static String SPACE = " ";
	public static String SMAP = "SMAP";
	public static String _S = "*S ";
	public static String _F = "*F";
	public static String ADD_FILE = "+ ";
	public static String _L = "*L";
	public static String SUPER_3 = "#";
	public static String COLON = ":";
	public static String COMMA = ",";
	public static String _E = "*E";

	/**
	 * 设置class调试属性
	 * 
	 * @param classFilePath
	 *            class文件全路径
	 * @param name
	 *            文件名称
	 * @param extension
	 *            资源后缀
	 * @param sourceDebugFiles
	 *            调试信息记录
	 */
	public static void writeSourceDebugExtension(String classFilePath, String name, String extension, SourceDebugFile[] sourceDebugFiles) {
		try {
			ClassFile classFile = new ClassFile(new DataInputStream(new FileInputStream(classFilePath)));
			addSourceDebugExtensionAttribute(classFile, name, extension, sourceDebugFiles);
			classFile.write(new DataOutputStream(new FileOutputStream(classFilePath)));
		} catch (FileNotFoundException e) {
			Log.error(e.getMessage(), e);
		} catch (IOException e) {
			Log.error(e.getMessage(), e);
		}
	}

	/**
	 * 设置class调试属性
	 * 
	 * @param classFile
	 * @param name
	 *            文件名称
	 * @param extension
	 *            资源后缀
	 * @param sourceDebugFiles
	 *            调试信息记录
	 */
	public static void addSourceDebugExtensionAttribute(ClassFile classFile, String name, String extension, SourceDebugFile[] sourceDebugFiles) {
		AttributeInfo attributeInfo = classFile.getAttribute("SourceDebugExtension");
		byte[] attrBytes = getSourceDebugExtensionAttribute(name, extension, sourceDebugFiles);
		attributeInfo = new AttributeInfo(classFile.getConstPool(), "SourceDebugExtension", attrBytes);
		classFile.addAttribute(attributeInfo);
	}

	public static String readSourceDebugExtension(String classFilePath) {
		try {
			ClassFile classFile = new ClassFile(new DataInputStream(new FileInputStream(classFilePath)));
			AttributeInfo attributeInfo = classFile.getAttribute("SourceDebugExtension");
			if (attributeInfo != null) {
				byte[] bytes = attributeInfo.get();
				String str = new String(bytes);
				return str;
			}
		} catch (FileNotFoundException e) {
			Log.error(e.getMessage(), e);
		} catch (IOException e) {
			Log.error(e.getMessage(), e);
		}
		return null;
	}

	private static byte[] getSourceDebugExtensionAttribute(String name, String extension, SourceDebugFile[] sourceDebugFiles) {
		StringBuffer sb = new StringBuffer(SMAP);
		sb.append(NL).append(name);
		sb.append(NL).append(extension);
		sb.append(NL).append(_S).append(extension);
		// 添加源文件
		sb.append(NL).append(_F);
		StringBuffer lineSb = new StringBuffer();
		for (int i = 0; i < sourceDebugFiles.length; ++i) {
			sourceDebugFiles[i].setLineFileID(String.valueOf(i));
			sb.append(NL).append(ADD_FILE).append(sourceDebugFiles[i].getLineFileID()).append(SPACE).append(sourceDebugFiles[i].getFilename());
			sb.append(NL).append(sourceDebugFiles[i].getFilepath());
			for (SourceDebugLine sdl : sourceDebugFiles[i].getDebugLines()) {
				lineSb.append(NL).append(sdl.getInputStartLine()).append(SUPER_3).append(sourceDebugFiles[i].getLineFileID()).append(COMMA).append(sdl.getRepeatCount())
						.append(COLON).append(sdl.getOutputStartLine()).append(COMMA).append(sdl.getOutputLineIncrement());
			}
		}
		sb.append(NL).append(_L).append(lineSb).append(NL).append(_E);
		return sb.toString().getBytes();
	}
}
