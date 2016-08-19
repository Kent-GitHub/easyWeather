package com.easynetwork.common.util.log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.easynetwork.common.util.storage.StorageUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**在调用写文件的时候，必须初始化文件{@link Log#initFile(Context)} 或者 {@link Log#initFile(String)}<br>
 * 打开文件log后，必须关闭写log流{@link Log#close()}<br>
 * 如果日志出现丢失，请将QUEUE_SIZE设置大一点，一般情况下8就足够了。<br>
 * 经过测试，使用线程去写日志到文件，比每次打开file要快0.5倍，主要体现在打开和关闭流。<br>
 * tsb = 13339975656, tinput = 3934150103, tall = 28324100046<br>
 * 其中tsb是对log进行组装的时间, tinput是写数据到文件, tall是开关文件和sb的总共时间，前两个时间和是使用线程。<br>
 * 推荐使用{@link Log#initFile(Context)}写数据。
 * @author WenYF
 *
 */
public final class Log {
	private static Log instance;
	
	public static boolean DEBUG_ENABLE = true;
	public static boolean FILE_ENABLE = true;
	private static String FILE_PATH = StorageUtil.getSDCardPath();
	private static final int QUEUE_SIZE = 9999;

	private File nLogFile;
	
	private BlockingQueue<String> nWaitLogs;
	private WriterThread nLogThread;
	
	private Log(){
		CharSequence time = DateFormat.format("yyyy-MM-dd",
				System.currentTimeMillis());
		File logDir = new File(FILE_PATH);
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		nLogFile = new File(logDir, time + ".txt");
		android.util.Log.w("Log", "log file path = " + nLogFile.getAbsolutePath());
		nWaitLogs = new ArrayBlockingQueue<String>(QUEUE_SIZE, true);
		nLogThread = new WriterThread(nWaitLogs);
		
		nLogThread.start();
	}
	
	private void releaseRes(){
		if(nLogThread != null){
			nLogThread.shutdown();
		}
		if(nWaitLogs != null){
			nWaitLogs.clear();
		}
	}
	
	private void logToFile(String str){
		if(nWaitLogs != null){
			try {
				nWaitLogs.put(str);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**如果有sd卡，日志文件放入sd卡的/siolette/PackageName的最后一个.后的name<br>
	 * 例如com.siolette.calculator的日志文件目录在/sdcard/siolette/calculator/<br>
	 * 日志文件名用天命名<br>
	 * 如果没有sd卡，文件放在app目录下的/siolette/PackageName的最后一个.后的name<br>
	 * init之后用close关系日志输入流<br>
	 * @param context 
	 */
	public static void initFile(Context context){
		if(!FILE_ENABLE || !DEBUG_ENABLE){
			return;
		}
		
		if (StorageUtil.hasSDCard()) {
			FILE_PATH = StorageUtil.getSDCardPath();
		} else {
			FILE_PATH = StorageUtil.getApplicationPath(context);
		}

		FILE_PATH = FILE_PATH.endsWith("/") ? FILE_PATH + "siolette/"
				: FILE_PATH + "/siolette/";
		String[] splitPackName = context.getPackageName().split("\\.");
		if (splitPackName != null && splitPackName.length > 0) {
			FILE_PATH = FILE_PATH + splitPackName[splitPackName.length - 1]
					+ "/";
		}
		
		close();

		android.util.Log.w("Log", "FILE_PATH = " + FILE_PATH);
		instance = new Log();
	}
	/**指定一个目录输出日志文件，自动用天命名文件<br>
	 * init之后用close关系日志输入流<br>
	 * @param context 
	 */
	public static void initFile(String strDir){
		if(!FILE_ENABLE || !DEBUG_ENABLE){
			return;
		}
		FILE_PATH = strDir;
		if(TextUtils.isEmpty(FILE_PATH)){
			if(StorageUtil.hasSDCard()){
				FILE_PATH = StorageUtil.getSDCardPath();
			}else{
				return;
			}
		}
		
		close();
		
		instance = new Log();
	}
	
	public static void close(){
		if(instance != null){
			instance.releaseRes();
		}
	}
	
	public static boolean isInstantial(){
		return instance != null;
	}
	
	public static String getMsg(String msg) {
		return msg;
	}

	public final static void v(String tag, String msg) {
		if (!DEBUG_ENABLE)
			return;
		msg = getMsg(msg);
		android.util.Log.v(tag, "Thread:" + Thread.currentThread().getName()
				+ "  " + msg);
		logToFile("VERBOSE", tag, getMsg(msg));
		
		
		
	}
	
	public final static void i(String tag, String msg) {
		if (!DEBUG_ENABLE)
			return;
		msg = getMsg(msg);
		android.util.Log.i(tag, "Thread:" + Thread.currentThread().getName() + "  " + msg);
		logToFile("INFO", tag, getMsg(msg));
	}

	public final static void i(String tag, String msg, Throwable t) {
		if (!DEBUG_ENABLE)
			return;
		msg = getMsg(msg);
		android.util.Log.i(tag, "Thread:" + Thread.currentThread().getName()
				+ "  " + msg, t);
		logToFile("INFO", tag, msg);
		logToFile("ERROR", tag, traceToString(t));
	}

	public final static void d(String tag, String msg) {
		if (!DEBUG_ENABLE)
			return;
		msg = getMsg(msg);
		android.util.Log.d(tag, "Thread:" + Thread.currentThread().getName()
				+ "  " + msg);
		logToFile("DEBUG", tag, getMsg(msg));
	}

	public final static void d(String tag, String msg, Throwable t) {
		if (!DEBUG_ENABLE)
			return;
		msg = getMsg(msg);
		android.util.Log.d(tag, "Thread:" + Thread.currentThread().getName()
				+ "  " + msg, t);
		logToFile("DEBUG", tag, msg);
		logToFile("ERROR", tag, traceToString(t));
	}

	public final static void w(String tag, String msg, Throwable t) {
		if (!DEBUG_ENABLE)
			return;
		msg = getMsg(msg);
		android.util.Log.w(tag, "Thread:" + Thread.currentThread().getName()
				+ "  " + msg, t);
		logToFile("WARN", tag, getMsg(msg));
		logToFile("ERROR", tag, traceToString(t));
	}

	public final static void w(String tag, String msg) {
		if (!DEBUG_ENABLE)
			return;
		msg = getMsg(msg);
		android.util.Log.w(tag, "Thread:" + Thread.currentThread().getName()
				+ "  " + msg);
		logToFile("WARN", tag, getMsg(msg));
	}

	public final static void e(String tag, String msg) {
		if (!DEBUG_ENABLE)
			return;
		msg = getMsg(msg);
		android.util.Log.e(tag, "Thread:" + Thread.currentThread().getName()
				+ "  " + msg);
		logToFile("ERROR", tag, getMsg(msg));
	}

	public final static void e(String tag, String msg, Throwable t) {
		if (!DEBUG_ENABLE)
			return;
		msg = getMsg(msg);
		android.util.Log.e(tag, "Thread:" + Thread.currentThread().getName()
				+ "  " + msg, t);
		logToFile("ERROR", tag, msg);
		logToFile("ERROR", tag, traceToString(t));
	}

	public final static void wtf(String tag, String msg) {
		if (!DEBUG_ENABLE)
			return;
		msg = getMsg(msg);
		android.util.Log.wtf(tag, "Thread:" + Thread.currentThread().getName()
				+ "  " + msg);
		logToFile("ERROR", tag, msg);
	}

	public final static void wtf(String tag, String msg, Throwable t) {
		if (!DEBUG_ENABLE)
			return;
		msg = getMsg(msg);
		android.util.Log.wtf(tag, "Thread:" + Thread.currentThread().getName()
				+ "  " + msg, t);
		logToFile("ERROR", tag, msg);
		logToFile("ERROR", tag, traceToString(t));
	}

	private final static void logToFile(String level, String tag, String msg) {
		if (!FILE_ENABLE)
			return;
		
		if(instance != null){
			Date date = new Date();
			String d = formatDate(date);
	
			StringBuffer sb = new StringBuffer();
			sb.append(d).append(" ").append(level).append("  ").append(tag)
					.append("  ").append(msg).append("  ").append("\n");
		
			instance.logToFile(sb.toString());
		}
	}

	@SuppressLint("SimpleDateFormat")
	private final static String formatDate(Date d) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		return format.format(d);
	}

	private final static String traceToString(Throwable t) {
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		t.printStackTrace(pw);
		return writer.toString();
	}

	private class WriterThread extends Thread{
		BlockingQueue<String> nnContentQueue;
		boolean nnIsShutdown;
		
		public WriterThread(final BlockingQueue<String> bq) {
			nnContentQueue = bq;
			nnIsShutdown = true;
		}

		@Override
		public void run() {
			nnIsShutdown = false;
			String logContent = null;
			
			while(!nnIsShutdown){
				// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
				FileWriter fileWriter = null;
				BufferedWriter bufferedWriter = null;
				try {
					fileWriter = new FileWriter(nLogFile, true);
					bufferedWriter = new BufferedWriter(fileWriter);
					logContent = nnContentQueue.take() + "\n";
					if(logContent != null && bufferedWriter != null){
						bufferedWriter.write(logContent);
						bufferedWriter.flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					StorageUtil.closeSilently(bufferedWriter);
				}
			}
		}
		
		public void shutdown(){
			nnIsShutdown = true;
		}
	}
	
}
