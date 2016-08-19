package com.easynetwork.weather.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.easynetwork.weather.core.Constants;

import android.text.TextUtils;
import android.util.Log;


/**
 * 同步上传多个文件
 * 基于标准的http实现，需要在非UI线程中调用，以免阻塞UI。
 *
 */
public class UploadFileLoader {
	private static final String TAG = "UploadFileLoader";
	
	/**
	 * 同步上传File
	 * 
	 * @param actionUrl
	 * @param fileList
	 * @return 服务器的响应数据的字符串形式
	 */
	public String UploadFileSync(String actionUrl,List<String> fileList, Map<String, String> params) {
		String reulstCode = "";
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "--------boundary";

		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			// 允许Input、Output，不使用Cache
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			// 设置传送的method=POST
			con.setRequestMethod("POST");
			// setRequestProperty
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			// con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
			con.setRequestProperty("token", params.get("token").trim());

			// 设置DataOutputStream
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());

			for (int i = 0; i < fileList.size(); i++) {
				
				String filePath = fileList.get(i);

				int endFileIndex = filePath.lastIndexOf("/");
				String fileName = filePath.substring(endFileIndex + 1);
				Log.i(TAG, "filename= " + fileName);
				// set 头部
				StringBuilder sb = new StringBuilder();

				sb.append(twoHyphens);
				sb.append(boundary);
				sb.append(end);
				sb.append("Content-Disposition: form-data; ");
				sb.append("name=" + "\"" + getFileNameNoEx(fileName) + "\"");
				sb.append(";filename=");
				sb.append("\"" + fileName + "\"");
				sb.append(end);
				
				sb.append("Content-Type: ");
				sb.append("image/jpeg");
				sb.append(end);
				sb.append(end);

				// 1. write sb
				dos.writeBytes(sb.toString());

				// 取得文件的FileInputStream
				FileInputStream fis = new FileInputStream(filePath);
				// 设置每次写入1024bytes
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];

				int length = -1;
				// 从文件读取数据至缓冲区
				while ((length = fis.read(buffer)) != -1) {
					dos.write(buffer, 0, length);
				}
				dos.writeBytes(end);
				fis.close();
				
				dos.writeBytes(end);
				dos.writeBytes(end);
				
				//dos.writeBytes(end);
				//dos.flush();
				// close streams
				//fis.close();
			}

			// set 尾部
			StringBuilder sb2 = new StringBuilder();

			if (params != null && !params.isEmpty()) {
				for (String key : params.keySet()) {
					String value = params.get(key);
					sb2.append(twoHyphens);
					sb2.append(boundary);
					sb2.append(end);
					sb2.append("Content-Disposition: form-data; ");
					sb2.append("name=" + "\"");
					sb2.append(key + "\"");
					sb2.append(end);
					sb2.append(end);
					sb2.append(value);
					sb2.append(end);
				}
			}
			sb2.append(twoHyphens + boundary + end);
			dos.writeBytes(sb2.toString());
			dos.flush();
			Log.i(TAG, "sb2:" + sb2.toString());

			// 取得Response内容
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			// 将Response显示于Dialog
			reulstCode = b.toString().trim();
			// 关闭DataOutputStream
			dos.close();
		} catch (IOException e) {
			Log.i(TAG, "IOException: " + e);
			e.printStackTrace();
		}
		
		if("".equals(reulstCode) || reulstCode == null){
			return reulstCode;
		}
		
		try {
			JSONObject json = new JSONObject(reulstCode);
			reulstCode = json.getString("errNum");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return reulstCode;
	}
	
	/**得到提醒状态
	 * @param Map<String, String> param
	 * @return 该用户的天气数据
	 */
	public String getRemindState(Map<String, String> param) {
		String reulst = "";
		String httpUrl = Constants.WEATHER_REMIND_STATE_URL + "&sid=" + param.get("sid") + "&uid=" +param.get("uid");
		String jsonResult = request(httpUrl,param.get("token"));
		
		Log.d(TAG, "jsonResult=="+jsonResult);
		if(TextUtils.isEmpty(jsonResult)){
			return reulst;
		}
		
		JSONObject json;
		try {
			json = new JSONObject(jsonResult);
			reulst = json.getString("errNum");
		} catch (JSONException e) {
			Log.e(TAG, e.toString(), e);
		}
		return reulst;
	}
	
	private String request(String httpUrl,String token) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			if(!TextUtils.isEmpty(token)){
				connection.setRequestProperty("token", token);
			}
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			Log.w(TAG, connection.getResponseCode() + "   " + connection.getResponseMessage());
			
			if(connection.getResponseCode() == 304){
				return null;
			}
			
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			result = null;
			Log.e(TAG, "request error : " + e.toString(), e);
		}
		return result;
	}
	
	public static String getFileNameNoEx(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length()))) {   
                return filename.substring(0, dot);   
            }   
        }   
        return filename;   
    }   
			
}
