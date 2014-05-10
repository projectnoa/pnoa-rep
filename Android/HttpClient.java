package com.screwattack.sgcapp.httpclient;

import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient {
	
	//public static final String KEY_CTJSON = "application/json";
	//public static final String KEY_CTXML = "application/xml";
	
	public static final String KEY_POST = "POST";
	public static final String KEY_GET = "GET";
	
	private static final String CONTENT_TYPE = "application/json;charset=utf-8";
	
	protected org.apache.http.client.HttpClient httpclient;
	protected AndroidHttpClient httpAndroidClient;
	//protected HttpPost httpPost;
	protected HttpGet httpGet;
	protected HttpResponse response = null;
	private static final int TIMEOUT = 30000;
	
	//private String logClassTag = "HTTPClientClassException";

	public HttpClient() { }
	
	public String httpClientJsonRequest(String url, JSONObject jsonObject) throws ClientProtocolException, IOException {
		
		BasicHttpParams params = new BasicHttpParams();
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		
		final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
		
		schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
		
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
		
		httpclient = new DefaultHttpClient(cm, params);
		StringEntity stringEntity;
		String convertStreamToString = null;
		
		try {
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpclient.getParams(), TIMEOUT);
			
			//httpPost = new HttpPost(url);
            httpGet = new HttpGet(url);
			
			//httpPost.addHeader("Accept", "application/json;q=1,application/x-msgpack;q=0.9");
			//httpPost.addHeader("Accept-Encoding", "bzip2,gzip,deflate");
			
			stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
			
			httpGet.setHeader("Content-Type", CONTENT_TYPE);//httpPost.setHeader("Content-Type", CONTENT_TYPE);
			//httpPost.setEntity(stringEntity);
			
			response = httpclient.execute(httpGet);//httpPost);
			
			if (response.getEntity() != null)
				convertStreamToString = convertStreamToString(response.getEntity().getContent());
		} catch (Exception e) {
			//Log.e(logClassTag, (e.getMessage() != null ? e.getMessage() + "- 2 - " + url + " - " + jsonObject.toString() : "NULL pointer Exception! - 2 - " + url + " - " + jsonObject.toString()));
			e.printStackTrace();
		} finally {
			try {
				if (httpclient != null) {
					if (httpclient.getConnectionManager() != null)
						httpclient.getConnectionManager().shutdown();
					httpclient = null;
				}
				
				if (response.getEntity() != null)
					response.getEntity().consumeContent();
				
				response = null;
			} catch(Exception e) { e.printStackTrace(); }
		}
		
		return convertStreamToString;
	}
	
	public String httpJsonRequestAndroid(String url, JSONObject jsonObject) throws ClientProtocolException, IOException {
		httpAndroidClient = AndroidHttpClient.newInstance("Android" + android.os.Build.VERSION.RELEASE);
		StringEntity stringEntity;
		String convertStreamToString = null;
		
		//return doRequest(url, jsonObject.toString(), KEY_CTJSON, KEY_POST);
		
		try {
			HttpConnectionParams.setConnectionTimeout(httpAndroidClient.getParams(), TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpAndroidClient.getParams(), TIMEOUT);
			
			//httpPost = new HttpPost(url);
            httpGet = new HttpGet(url);
			
			//httpPost.addHeader("Accept", "application/json;q=1,application/x-msgpack;q=0.9");
			//httpPost.addHeader("Accept-Encoding", "bzip2,gzip,deflate");
			
			stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");

            httpGet.setHeader("Content-Type", CONTENT_TYPE);//httpPost.setHeader("Content-Type", CONTENT_TYPE);
			//httpPost.setEntity(stringEntity);
			
			response = httpAndroidClient.execute(httpGet);//httpPost);
			
			if (response.getEntity() != null)
				convertStreamToString = convertStreamToString(response.getEntity().getContent());
		} catch(Exception e) {
			//Log.e(logClassTag, (e.getMessage() != null ? e.getMessage() + "- 2 - " + url + " - " + jsonObject.toString() : "NULL pointer Exception! - 2 - " + url + " - " + jsonObject.toString()));
			e.printStackTrace();
		} finally {
			try {
				if (httpAndroidClient != null) {
					if (httpAndroidClient.getConnectionManager() != null)
						httpAndroidClient.getConnectionManager().shutdown();
					httpAndroidClient.close();
					httpAndroidClient = null;
				}
				
				if (response.getEntity() != null)
					response.getEntity().consumeContent();
				
				response = null;
			} catch(Exception e) { e.printStackTrace(); }
		}
		
		return convertStreamToString;
	}
	
	private String convertStreamToString(InputStream is) throws IOException {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is), 1024);
		StringBuilder sb = new StringBuilder(is.available());

		String line;
		
		//String logClassTag = "StreamToStringClassException";
		
		try {
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");
		} catch (IOException e) {
			//Log.e(logClassTag, (e.getMessage() != null ? e.getMessage() : Constants.NULLPOINTERERROR));
			e.printStackTrace();
		} finally {
			try {
				is.close();
				reader.close();
			} catch (Exception e) {
				//Log.e(logClassTag, (e.getMessage() != null ? e.getMessage() : Constants.NULLPOINTERERROR));
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}
	
	public String httpURLJsonRequest(String myUrl, JSONObject jsonObject) throws ClientProtocolException, IOException {
		long start = 0;
		
		try {
			Log.wtf("REQUEST", myUrl + " : " + jsonObject.toString(2));
			start = System.nanoTime();
		} catch (Exception e) { e.printStackTrace(); }
		
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO)
			return httpClientJsonRequest(myUrl, jsonObject);
		
		HttpURLConnection httpUrlConnection = null;
		URL url;
		URLConnection urlConnection;
		byte[] params = null;
		String response = null;
		
		try {
			params = jsonObject.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			//Log.e(logClassTag, e1.getMessage());
			e1.printStackTrace();
		}
		
		try {
			if (params != null) {
				url = new URL(myUrl);
				urlConnection = url.openConnection();
				
				//new InterruptThread(Thread.currentThread(), urlConnection).run(); 
	
				urlConnection.setDoOutput(true);
				urlConnection.setRequestProperty("Content-Length", Integer.toString(params.length));
				urlConnection.setRequestProperty("Content-type", CONTENT_TYPE);
				
				httpUrlConnection = (HttpURLConnection) urlConnection;
				
				httpUrlConnection.setReadTimeout(TIMEOUT);
				httpUrlConnection.setConnectTimeout(TIMEOUT);
				httpUrlConnection.setRequestMethod(KEY_GET);//KEY_POST);
				
				OutputStream requestOutput = urlConnection.getOutputStream();
				
	 			requestOutput.write(params);
	 			requestOutput.close();
				
	 			httpUrlConnection.connect();
				
				if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
					response = convertStreamToString(httpUrlConnection.getInputStream());
			}
		} catch (Exception e) {
			//Log.e(logClassTag, (e.getMessage() != null ? e.getMessage() : Constants.NULLPOINTERERROR));
			Log.v("Conecction", e.toString());
			e.printStackTrace();
		} finally {
			if (httpUrlConnection != null) {
				httpUrlConnection.disconnect();
				httpUrlConnection = null;
				
				try {
					Log.wtf(
							"RESPONSE : Elapsed (ms): " + ((System.nanoTime() - start) / 1000000), 
							myUrl.split("/")[myUrl.split("/").length - 1] + " : (CONNECTION CLOSED)");
				} catch (Exception e) { e.printStackTrace(); }
			}
			
			urlConnection = null;
			url = null;
		}
		
		return response;
	}
}