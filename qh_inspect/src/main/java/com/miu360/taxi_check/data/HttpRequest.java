package com.miu360.taxi_check.data;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lubao.lubao.async.DException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.miu360.inspect.R.id.code;

public class HttpRequest {
	public static String post(String strURL, Map<String, String> textMap, Map<String, String> fileMap) {
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(strURL);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setRequestProperty("Accept", "text/html"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Encoding", "gzip"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY); // 设置发送数据的格式
			connection.setConnectTimeout(15 * 1000);
			connection.setReadTimeout(15 * 1000);
			connection.connect();
			OutputStream out = new DataOutputStream(connection.getOutputStream()); // utf-8编码

			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// file
			if (fileMap != null) {
				Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + "multipart/form-data" + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取响应
			int length = (int) connection.getContentLength();// 获取长度
			InputStream is = connection.getInputStream();
			if (length != -1) {
				byte[] data = new byte[length];
				byte[] temp = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, data, destPos, readLen);
					destPos += readLen;
				}
				String result = new String(data, "UTF-8"); // utf-8编码
				System.out.println(result);
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "error"; // 自定义错误信息
	}

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		int code = 0;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(8 * 1000);
			connection.setReadTimeout(8 * 1000);
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 获取返回码
			code = connection.getResponseCode();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！！错误码：" + code + "。\n" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/*
	 * private static String downloadApk(String url, String param) { try { //
	 * 判断SD卡是否存在，并且是否具有读写权限 if
	 * (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
	 * { // 获得存储卡的路径 String sdpath = Environment.getExternalStorageDirectory() +
	 * "/"; mSavePath = sdpath + "download"; URL url = new
	 * URL(mHashMap.get("url")); // 创建连接 HttpURLConnection conn =
	 * (HttpURLConnection) url.openConnection(); conn.connect(); // 获取文件大小 int
	 * length = conn.getContentLength(); // 创建输入流 InputStream is =
	 * conn.getInputStream();
	 *
	 * File file = new File(mSavePath); // 判断文件目录是否存在 if (!file.exists()) {
	 * file.mkdir(); } File apkFile = new File(mSavePath, mHashMap.get("name"));
	 * FileOutputStream fos = new FileOutputStream(apkFile); int count = 0; //
	 * 缓存 byte buf[] = new byte[1024]; // 写入到文件中 do { int numread =
	 * is.read(buf); count += numread; // 计算进度条位置 progress = (int) (((float)
	 * count / length) * 100); // 更新进度 mHandler.sendEmptyMessage(DOWNLOAD); if
	 * (numread <= 0) { // 下载完成 mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
	 * break; } // 写入文件 fos.write(buf, 0, numread); } while (!cancelUpdate);//
	 * 点击取消就停止下载. fos.close(); is.close(); } } catch (MalformedURLException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } //
	 * 取消下载对话框显示 mDownloadDialog.dismiss(); } };
	 */


	public static HeaderResponse sendPost2(String url, String param) throws Exception {
		OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(8, TimeUnit.SECONDS)
				.readTimeout(8, TimeUnit.SECONDS).build();
		FormBody.Builder builder = new FormBody.Builder();
		String[] p1 = param.split("&");
		if(p1 != null) {
			for (String par : p1) {
				if(par.contains("url=")){
					builder.add("url", par.substring(4));
				}else{
					String[] p2 = par.split("=");
					if(p2 !=null && p2.length > 1){
						builder.add(p2[0], p2[1]);
					}
				}
			}
		}
		RequestBody formBody = builder.build();
		Request.Builder requestBuilder = new Request.Builder().url(url).post(formBody);
		Request request = requestBuilder.build();
		Response response = okHttpClient.newCall(request).execute();
		if (response.isSuccessful()) {
			return new HeaderResponse(response.headers(), response.body().bytes());
		} else {
			throw new IOException("网络问题:" + response);
		}
	}

	public static class HeaderResponse {
		private Headers headers;
		private byte[] bytes;

		private HeaderResponse(Headers headers, byte[] bytes) {
			this.headers = headers;
			this.bytes = bytes;
		}

		public Headers getHeaders() {
			return headers;
		}

		public void setHeaders(Headers headers) {
			this.headers = headers;
		}

		public byte[] getBytes() {
			return bytes;
		}

		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws Exception
	 */
	public static String sendPost(String url, String param) throws Exception {
		return new String(sendPost2(url, param).getBytes());
		/*PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		int code = 0;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(8 * 1000);
			conn.setReadTimeout(8 * 1000);
			Log.e("Mozilla:", "Mozilla:"+url);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "**///*");
			/*conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 获取返回码
			code = conn.getResponseCode();
			if(code !=200){
				Log.e("log:", "log:"+code);
			}else{
				Log.e("log:", "log:"+code);
			}

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！错误码：" + code + "。\n" + e);
			e.printStackTrace();
			throw e;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;*/
	}

	// 上传文件到媒体服务器
	// 返回文件下载URL
	public static String uploadFile(String server, File file) throws Exception {
		URL url = new URL(server);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		HttpPost httpPost = new HttpPost(url.toURI());

		MultipartEntity m = new MultipartEntity();
		m.addPart("file", new FileBody(file));
		httpPost.setEntity(m);
		HttpResponse response = httpclient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new DException("网络问题:" + response.getStatusLine().getStatusCode());
		}
		String rst = EntityUtils.toString(response.getEntity(), "UTF-8");
		return rst;
	}

}