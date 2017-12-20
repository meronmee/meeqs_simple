package com.meronmee.core.utils;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * HTTP/HTTPS 请求工具类<p>
 * 
 * 使用OkHttp实现；自动识别http还是https。
 * 
 * @see http://square.github.io/okhttp/
 * @see https://m.2cto.com/net/201605/505364.html
 * @author Meron
 *
 */
public final class HttpUtils {
	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
	
	/**连接超时，单位：秒*/
	private static final int CONNECT_TIMEOUT = 3;
	/**发送数据超时，单位：秒*/
	private static final int WRITE_TIMEOUT = 5;
	/**读取数据超时，单位：秒*/
	private static final int READ_TIMEOUT = 5;

	private static final MediaType MEDIATYPE_JSON = MediaType.parse("application/json; charset=utf-8");
	
	/**
	 * 以表单的方式发送POST请求
	 * @param url url地址
	 * @param data 请求数据，可以为空或者null
	 * @return
	 */
	public static String post(final String url, final Map<String, String> data) throws Exception{
		return post(url, data, null);
	}
	/**
	 * 以表单的方式发送POST请求
	 * @param url		url地址
	 * @param data		请求数据，可以为空或者null
	 * @param headers	请求头部，可以为空或者null
	 * @return
	 */
	public static String post(final String url, final Map<String, String> data, final Map<String, String> headers) throws Exception{
		Assert.isNotBlank(url, "请求地址不能为空");

		String reqId = genReqId();
		log.info("[POST-Form-{}]URL:{}, Data:{}, Headers:{}", reqId, url, data, headers);
		
		//组装表单数据
		FormBody.Builder bodyBuilder = new FormBody.Builder();		
		if(BaseUtils.isNotEmpty(data)){
			for(Entry<String, String> entry : data.entrySet()){
				bodyBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//组装header数据
		Headers.Builder headerBuilder = new Headers.Builder();
		if(BaseUtils.isNotEmpty(headers)){
			for(Entry<String, String> entry : headers.entrySet()){
				headerBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//构建请求
		Request request = new Request.Builder()
				.url(url)
				.headers(headerBuilder.build())
				.post(bodyBuilder.build())
				.build();
		
		Response response = newClient(url)
				.newCall(request)
				.execute();
		
		if (response.isSuccessful()) {//成功的请求，返回包体		
			String responseBody = response.body().string();
			log.info("[POST-Form-{}]URL:{}, Response:{}", reqId, url, responseBody);
			return responseBody;
		} else {
			log.info("[POST-Form-{}]URL:{}, ResponseError:{}", reqId, url, response.code());
			throw new IOException("Unexpected code " + response.code());
		}
	}
	
	//-------------------------
	
	/**
	 * 以表单的方式异步发送POST请求
	 * @param url url地址
	 * @param data 请求数据，可以为空或者null
	 * @return
	 */
	public static void postAsync(final String url, final Map<String, String> data) throws Exception{
		postAsync(url, data, null, null);
	}
	/**
	 * 以表单的方式异步发送POST请求
	 * @param url url地址
	 * @param data 请求数据，可以为空或者null
	 * @param callback	回调，不关注返回结果可以为null
	 * @return
	 */
	public static void postAsync(final String url, final Map<String, String> data, final Callback callback) throws Exception{
		postAsync(url, data, null, callback);
	}
	/**
	 * 以表单的方式异步发送POST请求
	 * @param url		url地址
	 * @param data		请求数据，可以为空或者null
	 * @param headers	请求头部，可以为空或者null
	 * @param callback	回调，不关注返回结果可以为null
	 * @return
	 */
	public static void postAsync(final String url, final Map<String, String> data, final Map<String, String> headers, final Callback callback) throws Exception{
		Assert.isNotBlank(url, "请求地址不能为空");

		String reqId = genReqId();
		log.info("[POST-Form-{}]URL:{}, Data:{}, Headers:{}", reqId, url, data, headers);
		
		//组装表单数据
		FormBody.Builder bodyBuilder = new FormBody.Builder();		
		if(BaseUtils.isNotEmpty(data)){
			for(Entry<String, String> entry : data.entrySet()){
				bodyBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//组装header数据
		Headers.Builder headerBuilder = new Headers.Builder();
		if(BaseUtils.isNotEmpty(headers)){
			for(Entry<String, String> entry : headers.entrySet()){
				headerBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//构建请求
		Request request = new Request.Builder()
				.url(url)
				.headers(headerBuilder.build())
				.post(bodyBuilder.build())
				.build();
		
		
		//发起请求
		Call call = newClient(url).newCall(request);
		if(callback != null){
			call.enqueue(callback);
		} else {
			call.enqueue(new Callback(){

				@Override
				public void onFailure(Call call, IOException e) {
					log.error(BaseUtils.join("[POST-Form-Async-", reqId, "]URL:", url, ", ResponseError:", e.getMessage()), e);	
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					log.info("[POST-Form-Async-{}]URL:{}, Response:{}", reqId, url, response.body().string());
				}
				
			});			
		}
	}
	
	
	//============================================
	
	
	/**
	 * 以字符串的方式发送POST请求<p>
	 *  <li><code>String contentType = "application/json; charset=utf-8";</code></li>
	 * @param url url地址
	 * @param data 请求数据，可以为空或者null
	 * @return
	 */
	public static String postAsString(final String url, final String data) throws Exception{
		return postAsString(url, data, null, null);
	}
	/**
	 * 以字符串的方式发送POST请求
	 * @param url		url地址
	 * @param data		请求数据，可以为空或者null
	 * @param headers	请求头部，可以为空或者null
	 * @param contentType	ContentType，默认为:application/json; charset=utf-8
	 * @return
	 */
	public static String postAsString(final String url, final String data, final Map<String, String> headers, final String contentType) throws Exception{
		Assert.isNotBlank(url, "请求地址不能为空");
		
		String reqId = genReqId();
		log.info("[POST-Str-{}]URL:{}, Data:{}, Headers:{}", reqId, url, data, headers);

		//组装body数据
		String content = "";
		if(StringUtils.isNotBlank(data)){
			content = data;
		}
		
		//contentType
		MediaType mediaType = null;
		if(StringUtils.isNotBlank(contentType)){
			mediaType = MediaType.parse(contentType);
		}
		if(mediaType == null){
			mediaType = MEDIATYPE_JSON;
		}
		
		RequestBody body = RequestBody.create(mediaType, content);
					
		//组装header数据
		Headers.Builder headerBuilder = new Headers.Builder();
		if(BaseUtils.isNotEmpty(headers)){
			for(Entry<String, String> entry : headers.entrySet()){
				headerBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//构建请求
		Request request = new Request.Builder()
				.url(url)
				.headers(headerBuilder.build())
				.post(body)
				.build();
		
		Response response = newClient(url)
				.newCall(request)
				.execute();
				
		if (response.isSuccessful()) {//成功的请求，返回包体		
			String responseBody = response.body().string();
			log.info("[POST-Str-{}]URL:{}, Response:{}", reqId, url, responseBody);
			return responseBody;
		} else {
			log.info("[POST-Str-{}]URL:{}, ResponseError:{}", reqId, url, response.code());
			throw new IOException("Unexpected code " + response.code());
		}
	}

	//-------------------------
	/**
	 * 以字符串的方式异步发送POST请求<p>
	 * @param url url地址
	 * @param data 请求数据，可以为空或者null
	 * @return
	 */
	public static void postAsStringAsync(final String url, final String data) throws Exception{
		postAsStringAsync(url, data, null, null, null);
	}
	/**
	 * 以字符串的方式异步发送POST请求<p>
	 * @param url url地址
	 * @param data 请求数据，可以为空或者null
	 * @param callback	回调，不关注返回结果可以为null
	 * @return
	 */
	public static void postAsStringAsync(final String url, final String data, final Callback callback) throws Exception{
		postAsStringAsync(url, data, null, null, callback);
	}
	/**
	 * 以字符串的方式异步发送POST请求
	 * @param url		url地址
	 * @param data		请求数据，可以为空或者null
	 * @param headers	请求头部，可以为空或者null
	 * @param contentType	ContentType，默认为:application/json; charset=utf-8
	 * @param callback	回调，不关注返回结果可以为null
	 * @return
	 */
	public static void postAsStringAsync(final String url, final String data, final Map<String, String> headers, final String contentType, final Callback callback) throws Exception{
		Assert.isNotBlank(url, "请求地址不能为空");
		
		String reqId = genReqId();
		log.info("[POST-Str-Async-{}]URL:{}, Data:{}, Headers:{}", reqId, url, data, headers);

		//组装body数据
		String content = "";
		if(StringUtils.isNotBlank(data)){
			content = data;
		}
		
		//contentType
		MediaType mediaType = null;
		if(StringUtils.isNotBlank(contentType)){
			mediaType = MediaType.parse(contentType);
		}
		if(mediaType == null){
			mediaType = MEDIATYPE_JSON;
		}
		
		RequestBody body = RequestBody.create(mediaType, content);
					
		//组装header数据
		Headers.Builder headerBuilder = new Headers.Builder();
		if(BaseUtils.isNotEmpty(headers)){
			for(Entry<String, String> entry : headers.entrySet()){
				headerBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//构建请求
		Request request = new Request.Builder()
				.url(url)
				.headers(headerBuilder.build())
				.post(body)
				.build();
						
		//发起请求
		Call call = newClient(url).newCall(request);
		if(callback != null){
			call.enqueue(callback);
		} else {
			call.enqueue(new Callback(){

				@Override
				public void onFailure(Call call, IOException e) {
					log.error(BaseUtils.join("[POST-Str-Async-", reqId, "]URL:", url, ", ResponseError:", e.getMessage()), e);	
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					log.info("[POST-Str-Async-{}]URL:{}, Response:{}", reqId, url, response.body().string());
				}
				
			});			
		}
	}

	
	//============================================
	
		
	/**
	 * 发送GET请求
	 * @param url url地址
	 * @param data 请求数据，可以为空或者null
	 * @return
	 */
	public static String get(final String url, final Map<String, String> data) throws Exception{
		return get(url, data, null);
	}
	
	/**
	 * 发送GET请求
	 * @param url		url地址
	 * @param data		请求数据，可以为空或者null
	 * @param headers	请求头部，可以为空或者null
	 * @return
	 */
	public static String get(final String url, final Map<String, String> data, final Map<String, String> headers) throws Exception{
		Assert.isNotBlank(url, "请求地址不能为空");
		
		String reqId = genReqId();
		log.info("[GET-{}]URL:{}, Data:{}, Headers:{}", reqId, url, data, headers);
		
		//组装URL参数
		String finalUrl = url;
		if(BaseUtils.isNotEmpty(data)){
			String queryString = BaseUtils.map2QueryString(data);
			finalUrl = BaseUtils.addQueryParam(url, queryString);
		}
		
		//组装header数据
		Headers.Builder headerBuilder = new Headers.Builder();
		if(BaseUtils.isNotEmpty(headers)){
			for(Entry<String, String> entry : headers.entrySet()){
				headerBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//构建请求
		Request request = new Request.Builder()
				.url(finalUrl)
				.headers(headerBuilder.build())
				.build();
		
		//发起请求
		Response response = newClient(url)
				.newCall(request)
				.execute();
		
		if (response.isSuccessful()) {//成功的请求，返回包体		
			String responseBody = response.body().string();
			log.info("[GET-{}]URL:{}, Response:{}", reqId, url, responseBody);
			return responseBody;
		} else {
			log.info("[GET-{}]URL:{}, ResponseError:{}", reqId, url, response.code());
			throw new IOException("Unexpected code " + response.code());
		}
	}
	
	/**
	 * 发送GET请求
	 * @param url		url地址
	 * @param data		请求数据，可以为空或者null
	 * @param headers	请求头部，可以为空或者null
	 * @param callback	回调，不关注返回结果可以为null
	 */
	public static void getAsync(final String url, final Map<String, String> data, final Map<String, String> headers, final Callback callback) throws Exception{
		Assert.isNotBlank(url, "请求地址不能为空");
		
		String reqId = genReqId();
		log.info("[GET-Async-{}]URL:{}, Data:{}, Headers:{}", reqId, url, data, headers);
		
		//组装URL参数
		String finalUrl = url;
		if(BaseUtils.isNotEmpty(data)){
			String queryString = BaseUtils.map2QueryString(data);
			finalUrl = BaseUtils.addQueryParam(url, queryString);
		}
		
		//组装header数据
		Headers.Builder headerBuilder = new Headers.Builder();
		if(BaseUtils.isNotEmpty(headers)){
			for(Entry<String, String> entry : headers.entrySet()){
				headerBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		//构建请求
		Request request = new Request.Builder()
				.url(finalUrl)
				.headers(headerBuilder.build())
				.build();
		
		
		//发起请求
		Call call = newClient(url).newCall(request);
		if(callback != null){
			call.enqueue(callback);
		} else {
			call.enqueue(new Callback(){

				@Override
				public void onFailure(Call call, IOException e) {
					log.error(BaseUtils.join("[GET-Async-", reqId, "]URL:", url, ", ResponseError:", e.getMessage()), e);	
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					log.info("[GET-Async-{}]URL:{}, Response:{}", reqId, url, response.body().string());
				}
				
			});			
		}
	}
	
	//-------------------------
	
	/**
	 * 创建一个OkHttpClient客户端
	 * @param url - 请求地址，用于判断是否是HTTPS请求
	 */
	private static OkHttpClient newClient(String url) throws Exception{
		Assert.isNotBlank(url, "请求地址不能为空");
		
		boolean isHttps = url.toLowerCase().startsWith("https:");//是否是HTTPS请求的客户端
		
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
		        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
		        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
		        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
		        
		if(isHttps){
			//信任全部HTTPS证书
			builder.sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
		        .hostnameVerifier(new HostnameVerifier() {  
		            @Override  
		            public boolean verify(String hostname, SSLSession session) {  
		                return true;  
		            }  
		        });
		}       
		        
		OkHttpClient client = builder.build();
		
		return client;				
	}
	
	/**
	 * 生成一个请求编号，主要用于日志
	 */
	private static String genReqId(){
		return BaseUtils.uuidShort();
	}
	
	/**
	 * 创建一个用于HTTPS请求 SSLSocket 工厂
	 */
	private static SSLSocketFactory createSSLSocketFactory() throws Exception{  
		//SSL,TLS。TLS 可看做是 SSL的后续版本，是一种新形式的协议See:https://segmentfault.com/a/1190000002554673
        SSLContext sc = SSLContext.getInstance("TLS"); 
        sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());  
  
        SSLSocketFactory ssfFactory = sc.getSocketFactory(); 
        
        return ssfFactory;    
    }  
	
	//-------------------------
	
	public static void main(String[] args) throws Exception{
		String res = null;
		
		Map<String, String> data = new HashMap<>();
		data.put("msg", "Hello, 树先生！");
		data.put("text", "Hello World!");
		Map<String, String> headers = new HashMap<>();
		headers.put("appversion", "ios2.8.4");
		headers.put("ajax", "true");
		
		//res = get("http://localhost:8081/xxx/login.jsp", null);		
		
		//res = postAsString("http://localhost:8081/xxx/demo/query.json?accessToken=123456", "o.username  from usertable o where o.id= 1");
					
		//res = post("http://localhost:8081/xxx/demo/demo.json", data, headers);
		
		//==================
		
		//getAsync("http://localhost:8081/xxx/login.jsp", null, null, null);
		
		//postAsStringAsync("http://localhost:8081/xxx/demo/query.json?accessToken=123456", "o.username  from usertable o where o.id= 1");
				
		//postAsync("http://localhost:8081/xxx/demo/test.json", data, headers, null);

		//==================
		
		//res = post("https://xxx.com/xxx/demo/test.json", data, headers);
		
		System.out.println("res:"+res);				
	}
}
