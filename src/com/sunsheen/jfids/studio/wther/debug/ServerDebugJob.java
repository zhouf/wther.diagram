package com.sunsheen.jfids.studio.wther.debug;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.debug.internal.ui.views.console.ProcessConsole;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.google.gson.Gson;

/**
 * @author litao
 */
@SuppressWarnings("restriction")
public class ServerDebugJob {
	public static final int SUCCESS = 0;
	public static final int ERROR_NOT_EXECUTE = 1;
	public static final int ERROR_EXECUTED = 2;
	private static Map<Integer, String> typeMessageMap = new HashMap<Integer, String>();
	static {
		typeMessageMap.put(SUCCESS, "调试结束");
		typeMessageMap.put(ERROR_NOT_EXECUTE, "调试失败");
		typeMessageMap.put(ERROR_EXECUTED, "调试错误");
	}
	private String debugURL;
	private IOConsoleOutputStream debugInfo;

	public ServerDebugJob(IFile inputFile, String url) {
		this.debugURL = url;
	}

	public void run() throws ExecutionException {
		Thread thread = new Thread() {
			public void run() {
				StringBuffer returnSb = new StringBuffer();
				BufferedWriter out = null;
				BufferedReader in = null;
				try {
					URL destURL = new URL(debugURL);
					URLConnection urlConn = destURL.openConnection();

					in = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
					String tmpStr = "";
					while ((tmpStr = in.readLine()) != null) {
						returnSb.append(tmpStr);
					}
					printResult(returnSb.toString());
				} catch (Exception e) {
				} finally {
					IOUtils.closeQuietly(out);
					IOUtils.closeQuietly(in);
				}
			}
		};
		thread.start();
	}

	private IOConsoleOutputStream getProcessStandOutSteam() {
		IConsole[] consoles = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
		for (int i = 0; i < consoles.length; ++i) {
			IConsole console = consoles[i];
			if (console instanceof ProcessConsole) {
				IOConsoleOutputStream stream = ((ProcessConsole) console).getStream("org.eclipse.debug.ui.ID_STANDARD_OUTPUT_STREAM");
				return stream;
			}
		}
		return getDebugInfoConsole();
	}

	private IOConsoleOutputStream getDebugInfoConsole() {
		if (this.debugInfo == null) {
			MessageConsole console = new MessageConsole("Debug Info", null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
			this.debugInfo = new MessageConsoleStream(console);
		}
		return this.debugInfo;
	}

	protected void printResult(String jsonResult) {
		DebugResult dr = new Gson().fromJson(jsonResult, DebugResult.class);
		printResult(dr.type, dr.message);
	}

	protected void printResult(int type, String message) {
		try {
			IOConsoleOutputStream stream = getProcessStandOutSteam();
			if (!stream.isClosed()) {
				stream.write(typeMessageMap.get(type));
				if (!StringUtils.isEmpty(message)) {
					stream.write(":\n");
					stream.write(message);
					stream.write("\n");
				}
			}
		} catch (Exception e) {
		}
	}

	public class DebugResult {
		public int type;
		public String message;
	}

}