package hello.util.http;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
	private final static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
	private final static int DEFAULT_TIMEOUT = 5000;
	private final static int DEFAULT_RETRY = 1;

	
	public static JSONObject get(String url) {
		return get(url, DEFAULT_TIMEOUT, DEFAULT_RETRY);
	}
	public static JSONObject get(String url, int timeout, int retry) {
		return httpGet(url, timeout, retry,false);
	}
	
	public static JSONObject getWithCert(String url) {
		return getWithCert(url, DEFAULT_TIMEOUT, DEFAULT_RETRY);
	}
	public static JSONObject getWithCert(String url, int timeout, int retry) {
		return httpGet(url, timeout, retry,true);
	}
	
	public static JSONObject httpGet(String url, int timeout, int retry,boolean caflag) {
		JSONObject result = new JSONObject();
		CloseableHttpResponse response = null;
		HttpGet httpGet = null;
		CloseableHttpClient client = null;
		try {
			httpGet = new HttpGet(url);
			httpGet.setConfig(setConfig(timeout));
			client = new HttpConnectionManager(caflag).getHttpClient(retry);
			response = client.execute(httpGet);
			
			result.put("statuscode", response.getStatusLine().getStatusCode());
	        result.put("responsebody", parseResponseBody(response));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("An exception occurred while sending the get request.\n" + e.getStackTrace());
			result.put("statuscode", 500);
			result.put("responsebody", "An exception occurred while sending the get request.");
		} finally {
			HttpConnectionManager.close(httpGet, response, client);
		}
		log.info("get request result: " + result);
		return result;
	} 
	
	public static JSONObject post(String url, String payload) {
		return post(url, payload, DEFAULT_TIMEOUT, DEFAULT_RETRY);
	}
	public static JSONObject post(String url, String payload, int timeout, int retry) {
		return httpPost(url, payload, timeout, retry,false);
	}
	public static JSONObject postWithCert(String url, String payload) {
		return postWithCert(url, payload, DEFAULT_TIMEOUT, DEFAULT_RETRY);
	}
	public static JSONObject postWithCert(String url, String payload, int timeout, int retry) {
		return httpPost(url, payload, timeout, retry,true);
	}
	public static JSONObject httpPost(String url, String payload, int timeout, int retry,boolean caflag) {
		JSONObject result = new JSONObject();
		CloseableHttpResponse response = null;
		HttpPost httpPost = null;
		CloseableHttpClient client = null;
		try {
			httpPost = new HttpPost(url);
			httpPost.setConfig(setConfig(timeout));
			httpPost.setHeader("Accept", "application/json;utf-8");
			httpPost.setHeader("Content-Type", "application/json;charset=utf8");
	        StringEntity entity = new StringEntity(payload,"utf-8");
	        httpPost.setEntity(entity);
	        client = new HttpConnectionManager(caflag).getHttpClient(retry);
			response = client.execute(httpPost);
			result.put("statuscode", response.getStatusLine().getStatusCode());
	        result.put("responsebody", parseResponseBody(response));
	        
		} catch (Exception e) {
			e.printStackTrace();
			log.error("An exception occurred while sending the post request.\n" + e.getStackTrace());
			result.put("statuscode", 500);
			result.put("responsebody", "An exception occurred while sending the post request.");
		} finally {
			HttpConnectionManager.close(httpPost,response,client);
		}
		log.info("post request result: " + result);
		return result;
	} 
	
	public static JSONObject delete(String url) {
		return delete(url, DEFAULT_TIMEOUT, DEFAULT_RETRY);
	}
	public static JSONObject delete(String url, int timeout, int retry) {
		return httpDelete(url, timeout, retry,false);
	}
	public static JSONObject deleteWithCert(String url) {
		return delete(url, DEFAULT_TIMEOUT, DEFAULT_RETRY);
	}
	public static JSONObject deleteWithCert(String url, int timeout, int retry) {
		return httpDelete(url, timeout, retry,true);
	}
	public static JSONObject httpDelete(String url, int timeout, int retry,boolean caflag) {
		JSONObject result = new JSONObject();
		CloseableHttpResponse response = null;
		HttpDelete httpDelete = null;
		CloseableHttpClient client = null;
		try {
			httpDelete = new HttpDelete(url);
			httpDelete.setConfig(setConfig(timeout));
			client = new HttpConnectionManager(caflag).getHttpClient(retry);
			response = client.execute(httpDelete);
			
			result.put("statuscode", response.getStatusLine().getStatusCode());
	        result.put("responsebody", parseResponseBody(response));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("An exception occurred while sending the delete request.\n" + e.getStackTrace());
			result.put("statuscode", 500);
			result.put("responsebody", "An exception occurred while sending the delete request.");
		} finally {
			HttpConnectionManager.close(httpDelete, response, client);
		}
		log.info("delete request result: " + result);
		return result;
	} 
	
	public static RequestConfig setConfig(int timeout) {
		return RequestConfig.custom()
				.setConnectTimeout(DEFAULT_TIMEOUT)
				.setConnectionRequestTimeout(DEFAULT_TIMEOUT)
				.setSocketTimeout(DEFAULT_TIMEOUT)
				.build();
	} 
	
	public static String parseResponseBody(HttpResponse response) {
		StringBuffer sb = new StringBuffer();
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Failed to parse response.\n" + e.getStackTrace());
		}
		return sb.toString();
	}
	
}
