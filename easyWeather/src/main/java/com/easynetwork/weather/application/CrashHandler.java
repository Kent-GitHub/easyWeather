package com.easynetwork.weather.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.easynetwork.weather.core.Constants;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

/**
 * @Description 
 * 线程未捕获异常控制器是用来处理未捕获异常的。   
 * 如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框  
 * 实现该接口并注册为程序中的默认未捕获异常处理   
 * 这样当未捕获异常发生时，就可以做些异常处理操作  
 * 例如：收集异常信息，发送错误报告 等。 
 * 
 * 当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告
 * @author funben
 * @date 2016-2-17
 * @version 1.0.0
 */
public class CrashHandler implements UncaughtExceptionHandler{
	/** Debug Log Tag */  
	public static final String TAG = "CrashHandler";   
	
	/** 是否开启日志输出, 在Debug状态下开启, 在Release状态下关闭以提升程序性能 */  
	public static final boolean DEBUG = true;   
	
	/** CrashHandler实例 */  
	private static CrashHandler INSTANCE;   
	
	/** 程序的Context对象 */  
	private Context context;   
	
	/** 系统默认的UncaughtException处理类 */  
	private Thread.UncaughtExceptionHandler mDefaultHandler;   

	private String message="程序异常";
	
	/**存储设备信息和异常信息*/
	private Map<String, String> info = new HashMap<String, String>(); 

	/** 保证只有一个CrashHandler实例 */  
	private CrashHandler() {}   

	/** 获取CrashHandler实例 ,单例模式 */  
	public static CrashHandler getInstance() {   
		if (INSTANCE == null)   
			INSTANCE = new CrashHandler();   
		return INSTANCE;   
	}   
	
	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 
	 * 设置该CrashHandler为程序的默认处理器 </b>
	 * init
	 * @param ctx
	 */
	public void init(Context ctx) {  
		context = ctx;   
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();   
		Thread.setDefaultUncaughtExceptionHandler(this);   
		Log.d(TAG, "---CrashHandler init ok---");
	}
	
	/**
	 * Description 当UncaughtException发生时会转入该函数来处理 
	 * @param thread
	 * @param throwable 
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		if (!handleException(throwable) && mDefaultHandler != null) {   
			// 如果用户没有处理则让系统默认的异常处理器来处理   
			mDefaultHandler.uncaughtException(thread, throwable);   
		} else {   
			// Sleep一会后结束程序   
			// 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序   
			try {   
				Thread.sleep(3000);   
			} catch (InterruptedException e) {   
				Log.e(TAG, "Error : ", e);   
			}
			android.os.Process.killProcess(android.os.Process.myPid());   
			System.exit(10);   
		} 
		
	}
	
	/**  
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑  
	 * @param ex  
	 * @return true:如果处理了该异常信息;否则返回false  
	 */  
	private boolean handleException(Throwable throwable) {   
		if (throwable == null) {   
			cancelAllNotification(context);
			return true;   
		} 
		// 使用Toast来显示异常信息   
		new Thread() {   
			@Override  
			public void run() {   
				// Toast 显示需要出现在一个线程的消息队列中   
				Looper.prepare();   
				//Toast.makeText(context, message, Toast.LENGTH_LONG).show();   
				Looper.loop();   
			}   
		}.start();   

		Log.e(TAG, info.toString());
		// 保存错误报告文件   
		saveCrashInfo2File(throwable);
	 	return true;   
	} 
	
	/**
     * 清除通知栏的所有通知，如果能清除
     * @param context
     */
    public static void cancelAllNotification(Context context){
        ((NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE)).cancelAll();
    }
    
    private String saveCrashInfo2File(Throwable ex) {  
        StringBuffer sb = new StringBuffer();  
        for (Map.Entry<String, String> entry : info.entrySet()) {  
            String key = entry.getKey();  
            String value = entry.getValue();  
            sb.append(key + "=" + value + "\r\n");  
        }  
        
 
        Writer writer = new StringWriter();  
        PrintWriter pw = new PrintWriter(writer);  
        ex.printStackTrace(pw);  
        Throwable cause = ex.getCause();  
        // 循环着把所有的异常信息写入writer中  
        while (cause != null) {  
            cause.printStackTrace(pw);  
            cause = cause.getCause();  
        }  
        pw.close();// 记得关闭  
        String result = writer.toString();  
        sb.append(result);  
        
        Log.e(TAG, "---应用崩溃了---原因："+sb.toString());
        // 保存文件  
        String fileName = "E_weather_crash_" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".txt"; 
        if (Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED)) {  
            try {  
            	String PATH_LOGCAT;
            	if (Environment.getExternalStorageState().equals(  
                        Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中  
                    PATH_LOGCAT = Environment.getExternalStorageDirectory().getAbsolutePath() 
                    		+ Constants.SD_FOLDER_PATH+"log"+File.separator;  
                } else {// 如果SD卡不存在，就保存到本应用的目录下  
                    PATH_LOGCAT = context.getFilesDir().getAbsolutePath() + File.separator + "log"+File.separator;
                }  
                File dir = new File(PATH_LOGCAT);  
                Log.i("CrashHandler", dir.toString());  
                if(!dir.exists())  
                    dir.mkdirs();  
                FileOutputStream fos = new FileOutputStream(new File(dir,fileName));  
                fos.write(sb.toString().getBytes());  
                fos.close();  
                return fileName;  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return null;  
    }  
    
    /**
     * 获取应用包
     * @param context 上下文
     * @return
     */
    public static String getPackageName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

}
