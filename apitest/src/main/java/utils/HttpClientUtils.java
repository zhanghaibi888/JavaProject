package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {

	// 控制是否开启代理
	public static boolean openProxy = false;

	static CloseableHttpClient closeableHttpClient;

	/**
	 * 初始化请求池
	 */
	private static void initHttpClient() {
		if (closeableHttpClient != null) {
			return;
		}
		if (openProxy) {
			// 设置代理请求池
			HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
			RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy).build();
			closeableHttpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		} else {
			// 默认请求池
			closeableHttpClient = HttpClients.createDefault();
		}
	}

	/**
	 * 无请求头的Get方法请求
	 * 
	 * @param url
	 *            请求的地址
	 * @return
	 */
	public static String doGet(String url) {
		return doGet(url, null);
	}

	/**
	 * 无请求头的Post()方法请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, Map<String, Object> paramsMps) {
		return doPost(url, paramsMps, null);
	}

	/**
	 * 无请求头的PostJson()方法请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	public static String doPostJson(String url, String json) {
		return doPostJson(url, json, null);
	}

	/**
	 * 有请求头的doGet方法
	 * 
	 * @param url
	 * @param headerMps
	 * @return
	 */
	public static String doGet(String url, Map<String, Object> headerMps) {
		// 初始化
		initHttpClient();
		// 接收response返回的数据
		HttpEntity httpEntity = null;
		// 初始化请求
		HttpGet httpGet = new HttpGet(url);

		// Headers设置
		if (MapUtils.isNotEmpty(headerMps)) {
			Set<String> keyset = headerMps.keySet();
			for (String key : keyset) {
				httpGet.setHeader(key, String.valueOf(headerMps.get(key)));
			}
		}

		try {
			CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				httpEntity = response.getEntity();
				return EntityUtils.toString(httpEntity, "utf-8");
			} else {
				System.out.println(response.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpEntity != null) {
					EntityUtils.consume(httpEntity);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return "error";
	}

	/**
	 * 有请求头的doPost方法
	 * 
	 * @param url
	 * @param paramsMps
	 *            参数
	 * @param headerMps
	 *            请求头
	 * @return
	 */
	public static String doPost(String url, Map<String, Object> paramsMps, Map<String, Object> headerMps) {
		// 初始化
		initHttpClient();
		// 接收response
		HttpEntity httpEntity = null;
		try {
			// 初始化请求
			HttpPost httpPost = new HttpPost(url);
			// 请求头处理
			if (MapUtils.isNotEmpty(headerMps)) {
				Set<String> keySet = headerMps.keySet();
				for (String key : keySet) {
					httpPost.setHeader(key, String.valueOf(headerMps.get(key)));
				}
			}

			// 请求参数处理
			if (MapUtils.isNotEmpty(paramsMps)) {
				List<NameValuePair> formparams = new ArrayList<>();
				Set<String> keySet = paramsMps.keySet();
				for (String key : keySet) {
					formparams.add(new BasicNameValuePair(key, String.valueOf(paramsMps.get(key))));
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams, "utf-8");
				httpPost.setEntity(urlEncodedFormEntity);
			}
			// 执行请求
			CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				httpEntity = response.getEntity();
				return EntityUtils.toString(httpEntity);
			} else {
				System.out.println(response.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpEntity != null) {
					EntityUtils.consume(httpEntity);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return "error";
	}

	public static String doPostJson(String url, String json, Map<String, Object> headerMps) {
		// 初始化
		initHttpClient();
		url = StringUtils.trim(url);
		// 设置请求
		HttpPost httpPost = new HttpPost(url);
		HttpEntity httpEntity = null;

		try {
			// 请求头处理
			if (MapUtils.isNotEmpty(headerMps)) {
				Set<String> keySet = headerMps.keySet();
				for (String key : keySet) {
					httpPost.setHeader(key, String.valueOf(headerMps.get(key)));
				}
			}
			// 请求参数处理
			StringEntity stringEntity = new StringEntity(json, "utf-8");
			httpPost.setEntity(stringEntity);
			CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				httpEntity = response.getEntity();
				return EntityUtils.toString(httpEntity);
			} else {
				System.out.println(response.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpEntity != null) {
					EntityUtils.consume(httpEntity);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return "error";
	}

	public static void main(String[] args) {

		doPostJson("http://118.24.13.38:8080/goods/json2", "{\"count\":3}",
				StringToMapUtils.strToMpConvert2("token=61b3590090982a0185dda9d3bd793b46;userId=123"));
	}
}
