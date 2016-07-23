package com.sunsheen.jfids.studio.wther.diagram.compiler.util;



/**
 * 这是生成JAVA事务相关代码的一个类
 * @author zhoufeng
 * Date:2013-12-18
 */
public class TransactionCodeUtil {
	private static String varPrefix = "idsSession";
	private static String sessionVarName = "";

	public static void setSessionVarName(String varName) {
		sessionVarName = varName;
	}

	public static String getSessionVarName() {
		return sessionVarName;
	}

	public static String getTransactionBeginCode(String arg,boolean isTransfer){
		StringBuilder sb = new StringBuilder();
		// session变量名的处理，如果有传入变量名，则使用变量名，如果没有传入，则自动生成一个
		if(arg!=null && !arg.isEmpty()){
			setSessionVarName(arg);
		}else{
			genSessionVarName();
		}
		
		sb.append("\n");
		//sb.append("com.sunsheen.jfids.system.database.IdsSession ").append(getSessionVarName()).append(" = com.sunsheen.jfids.util.IdsDataBaseUtil.");
		sb.append("IdsSession ").append(getSessionVarName()).append(" = IdsDataBaseUtil.");
		if(isTransfer){
			sb.append("getHibernateSession");
		}else{
			sb.append("getStatelessHibernateSession");
		}
		sb.append("();").append("\n");
		
		sb.append(getSessionVarName()).append(".beginTransaction();").append("\n");
		return sb.toString();
	}
	
	public static String getTransactionCommitCode(String arg){
		StringBuilder sb = new StringBuilder();
		sb.append("\t").append(getSessionVarName()).append(".commit();").append("\n");
		sb.append("\tSystem.out.println(\"").append(getSessionVarName()).append("事务提交commit\");").append("\n");
		//sb.append("\tretmsg=\"SUCC\";").append("\n");
		return sb.toString();
	}
	
	public static String getTransactionRollbackCode(String arg){
		StringBuilder sb = new StringBuilder();
		sb.append(getSessionVarName()).append(".rollback();").append("\n");
		sb.append("\tSystem.out.println(\"").append(getSessionVarName()).append("事务回滚rollback\");").append("\n");
		//sb.append("retmsg=\"ERROR\";").append("\n");
		return sb.toString();
	}
	
	//返回SESSION变量名
	private static void genSessionVarName(){
//		sessionVarName = varPrefix + (new Random()).nextInt(1000);
		sessionVarName = varPrefix;
//		return "";
	}

}
