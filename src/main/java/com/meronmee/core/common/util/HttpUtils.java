package com.meronmee.core.common.util;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	/**不超时header标记位*/
	public static final String HEADER_NO_TIMEOUT = "_TIMEOUT_DISABLE";
	/**自定义连接超时header标记位*/
	public static final String HEADER_CONNECT_TIMEOUT_SECONDS = "_TIMEOUT_CONNECT_TIMEOUT_SECONDS";
	/**自定义发送数据超时header标记位*/
	public static final String HEADER_WRITE_TIMEOUT_SECONDS = "_TIMEOUT_WRITE_TIMEOUT_SECONDS";
	/**自定义读取数据超时header标记位*/
	public static final String HEADER_READ_TIMEOUT_SECONDS = "_TIMEOUT_READ_TIMEOUT_SECONDS";
	
	/**默认连接超时，单位：秒*/
	private static final int CONNECT_TIMEOUT = 10;
	/**默认发送数据超时，单位：秒*/
	private static final int WRITE_TIMEOUT = 10;
	/**默认读取数据超时，单位：秒*/
	private static final int READ_TIMEOUT = 15;

	private static final MediaType MEDIATYPE_JSON = MediaType.parse("application/json; charset=utf-8");

	/**
	 *
	 OkHttpClients should be shared
	 OkHttp performs best when you create a single OkHttpClient instance and reuse it for all of your HTTP calls. This is because each client holds its own connection pool and thread pools. Reusing connections and threads reduces latency and saves memory. Conversely, creating a client for each request wastes resources on idle pools.
	*/
	// The singleton HTTP client.
	private static final OkHttpClient sharedClient = new OkHttpClient.Builder()
					.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
					.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
		        	.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
					.build();
	
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
	 * @param headers	请求头部，可以为空或者null。特殊字段：_TIMEOUT_DISABLE,表示该请求是长时耗请求，不设超时时间
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
				if(null == entry.getValue()){
					continue;
				}
				bodyBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//组装header数据
		Headers.Builder headerBuilder = new Headers.Builder();
		if(BaseUtils.isNotEmpty(headers)){
			for(Entry<String, String> entry : headers.entrySet()){
				if(null == entry.getValue()){
					continue;
				}
				headerBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//构建请求
		Request request = new Request.Builder()
				.url(url)
				.headers(headerBuilder.build())
				.post(bodyBuilder.build())
				.build();
		
		Response response = newClient(url, headerBuilder)
				.newCall(request)
				.execute();
		
		String responseBody = getResponseInfo(response);
		log.info("[POST-Form-{}]URL:{}, Response:{}", reqId, url, responseBody);
		return responseBody;
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
	 * @param headers	请求头部，可以为空或者null。特殊字段：_TIMEOUT_DISABLE,表示该请求是长时耗请求，不设超时时间
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
				if(null == entry.getValue()){
					continue;
				}
				bodyBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//组装header数据
		Headers.Builder headerBuilder = new Headers.Builder();
		if(BaseUtils.isNotEmpty(headers)){
			for(Entry<String, String> entry : headers.entrySet()){
				if(null == entry.getValue()){
					continue;
				}
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
		Call call = newClient(url, headerBuilder).newCall(request);
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
					String responseBody = getResponseInfo(response);
					log.info("[POST-Form-Async-{}]URL:{}, Response:{}", reqId, url, responseBody);
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
	 * @param headers	请求头部，可以为空或者null。特殊字段：_TIMEOUT_DISABLE,表示该请求是长时耗请求，不设超时时间
	 * @param contentType	ContentType，默认为:application/json; charset=utf-8
	 * @return
	 */
	public static String postAsString(final String url, final String data, final Map<String, String> headers) throws Exception{
		return postAsString(url, data, headers, null);
	}
	/**
	 * 以字符串的方式发送POST请求
	 * @param url		url地址
	 * @param data		请求数据，可以为空或者null
	 * @param headers	请求头部，可以为空或者null。特殊字段：_TIMEOUT_DISABLE,表示该请求是长时耗请求，不设超时时间
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
				if(null == entry.getValue()){
					continue;
				}
				headerBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//构建请求
		Request request = new Request.Builder()
				.url(url)
				.headers(headerBuilder.build())
				.post(body)
				.build();
		
		Response response = newClient(url, headerBuilder)
				.newCall(request)
				.execute();
			
		String responseBody = getResponseInfo(response);
		log.info("[POST-Str-{}]URL:{}, Response:{}", reqId, url, responseBody);
		return responseBody;
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
	 * @param headers	请求头部，可以为空或者null。特殊字段：_TIMEOUT_DISABLE,表示该请求是长时耗请求，不设超时时间
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
				if(null == entry.getValue()){
					continue;
				}
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
		Call call = newClient(url, headerBuilder).newCall(request);
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
					String responseBody = getResponseInfo(response);
					log.info("[POST-Str-Async-{}]URL:{}, Response:{}", reqId, url, responseBody);
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
	 * @param headers	请求头部，可以为空或者null。特殊字段：_TIMEOUT_DISABLE,表示该请求是长时耗请求，不设超时时间
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
				if(null == entry.getValue()){
					continue;
				}
				headerBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		
		//构建请求
		Request request = new Request.Builder()
				.url(finalUrl)
				.headers(headerBuilder.build())
				.build();
		
		//发起请求
		Response response = newClient(url, headerBuilder)
				.newCall(request)
				.execute();
		
		String responseBody = getResponseInfo(response);
		log.info("[GET-{}]URL:{}, Response:{}", reqId, url, responseBody);
		return responseBody;
	}
	
	/**
	 * 发送GET请求
	 * @param url		url地址
	 * @param data		请求数据，可以为空或者null
	 * @param headers	请求头部，可以为空或者null。特殊字段：_TIMEOUT_DISABLE,表示该请求是长时耗请求，不设超时时间
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
				if(null == entry.getValue()){
					continue;
				}
				headerBuilder.add(entry.getKey(), entry.getValue());				
			}
		}
		//构建请求
		Request request = new Request.Builder()
				.url(finalUrl)
				.headers(headerBuilder.build())
				.build();
		
		
		//发起请求
		Call call = newClient(url, headerBuilder).newCall(request);
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
					String responseBody = getResponseInfo(response);
					log.info("[GET-Async-{}]URL:{}, Response:{}", reqId, url, responseBody);
				}
				
			});			
		}
	}

	//-------------------------

    /**
     * 上传文件
     * @param url url地址
     * @param fileFieldName 文件字段名称
     * @param file 文件数据
     * @param data 普通数据，可以为空或者null
     * @return
     */
    public static String upload(final String url, final String fileFieldName, final File file, final Map<String, String> data) throws Exception{
        return upload(url, fileFieldName, file, data, null);
    }
    /**
     * 上传文件
     * @param url    url地址
     * @param fileFieldName 文件字段名称
     * @param file   文件数据
     * @param data	 普通请求数据，可以为空或者null
     * @param headers	请求头部，可以为空或者null
     * @return
     */
    public static String upload(final String url, final String fileFieldName, final File file, final Map<String, String> data, final Map<String, String> headers) throws Exception{
        Assert.isNotBlank(url, "请求地址不能为空");

        String reqId = genReqId();
        String filePath = file!=null ? file.getAbsolutePath() : "null";
        log.info("[Upload-{}]URL:{}, fileField:{}, File:{}, Data:{}, Headers:{}", reqId, url, fileFieldName, filePath, data, headers);

        //组装表单数据
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);

        //文件数据
        if(file != null && file.exists() && file.isFile()){
            String fileField = BaseUtils.getFirstNotBlank(fileFieldName, "file");
            String fileName = BaseUtils.getFileName(filePath);

            //MediaType.parse("application/octet-stream");
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            bodyBuilder.addFormDataPart(fileField, fileName, fileBody);
        } else {
            log.warn("[Upload-{}]URL:{}, File:{}, invalid file！",  reqId, url, filePath);
        }

        //普通数据
        if(BaseUtils.isNotEmpty(data)){
            for(Entry<String, String> entry : data.entrySet()){
                if(null == entry.getValue()){
                    continue;
                }
                bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        //组装header数据
        Headers.Builder headerBuilder = new Headers.Builder();
        if(BaseUtils.isNotEmpty(headers)){
            for(Entry<String, String> entry : headers.entrySet()){
                if(null == entry.getValue()){
                    continue;
                }
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        //构建请求
        Request request = new Request.Builder()
                .url(url)
                .headers(headerBuilder.build())
                .post(bodyBuilder.build())
                .build();

        Response response = newClient(url, headerBuilder)
                .newCall(request)
                .execute();

        String responseBody = getResponseInfo(response);
        log.info("[Upload-{}]URL:{}, Response:{}", reqId, url, responseBody);
        return responseBody;
    }

    //-------------------------

    /**
     * 异步上传文件
     * @param url url地址
     * @param fileFieldName 文件字段名称
     * @param file 文件数据
     * @param data 普通数据，可以为空或者null
     * @return
     */
    public static void uploadAsync(final String url, final String fileFieldName, final File file, final Map<String, String> data) throws Exception{
        uploadAsync(url, fileFieldName, file, data, null, null);
    }
    /**
     * 异步上传文件
     * @param url url地址
     * @param fileFieldName 文件字段名称
     * @param file 文件数据
     * @param data 普通数据，可以为空或者null
     * @return
     */
    public static void uploadAsync(final String url, final String fileFieldName, final File file, final Map<String, String> data, final Callback callback) throws Exception{
        uploadAsync(url, fileFieldName, file, data, null, callback);
    }
    /**
     * 异步上传文件
     * @param url url地址
     * @param fileFieldName 文件字段名称
     * @param file 文件数据
     * @param data 普通数据，可以为空或者null
     * @param headers	请求头部，可以为空或者null
     * @param callback	回调，不关注返回结果可以为null
     * @return
     */
    public static void uploadAsync(final String url, final String fileFieldName, final File file, final Map<String, String> data, final Map<String, String> headers, final Callback callback) throws Exception{
        Assert.isNotBlank(url, "请求地址不能为空");

        String reqId = genReqId();
        String filePath = file!=null ? file.getAbsolutePath() : "null";
        log.info("[Upload-Async-{}]URL:{}, fileField:{}, File:{}, Data:{}, Headers:{}", reqId, url, fileFieldName, filePath, data, headers);

        //组装表单数据
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);

        //文件数据
        if(file != null && file.exists() && file.isFile()){
            String fileField = BaseUtils.getFirstNotBlank(fileFieldName, "file");
            String fileName = BaseUtils.getFileName(filePath);

            //MediaType.parse("application/octet-stream");
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            bodyBuilder.addFormDataPart(fileField, fileName, fileBody);
        } else {
            log.warn("[Upload-Async-{}]URL:{}, File:{}, invalid file！",  reqId, url, filePath);
        }

        //普通数据
        if(BaseUtils.isNotEmpty(data)){
            for(Entry<String, String> entry : data.entrySet()){
                if(null == entry.getValue()){
                    continue;
                }
                bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        //组装header数据
        Headers.Builder headerBuilder = new Headers.Builder();
        if(BaseUtils.isNotEmpty(headers)){
            for(Entry<String, String> entry : headers.entrySet()){
                if(null == entry.getValue()){
                    continue;
                }
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
        Call call = newClient(url, headerBuilder).newCall(request);
        if(callback != null){
            call.enqueue(callback);
        } else {
            call.enqueue(new Callback(){

                @Override
                public void onFailure(Call call, IOException e) {
                    log.error(BaseUtils.join("[Upload-Async-", reqId, "]URL:", url, ", ResponseError:", e.getMessage()), e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = getResponseInfo(response);
                    log.info("[Upload-Async-{}]URL:{}, Response:{}", reqId, url, responseBody);
                }

            });
        }
    }

    //============================================
	
	/**
	 * 创建一个OkHttpClient客户端
	 * @param url - 请求地址，用于判断是否是HTTPS请求
	 */
	private static OkHttpClient newClient(String url) throws Exception{
		return newClient(url, null);
	}
	/**
	 * 如果没有个性化的设置，将返回共享的OkHttpClient；如果有个性化的设置，则基于共享的客户端sharedClient创建一个轻量的OkHttpClient客户端
	 * @param url - 请求地址，用于判断是否是HTTPS请求
	 * @param headerBuilder - 请求头，用于判断超时
	 */
	private static OkHttpClient newClient(String url, Headers.Builder headerBuilder) throws Exception{
		Assert.isNotBlank(url, "请求地址不能为空");

		/*
		This builds a client that shares the same connection pool, thread pools, and configuration.
		Use the builder methods to configure the derived client for a specific purpose.

		OkHttpClient.Builder builder = sharedClient.newBuilder();
		*/
		OkHttpClient.Builder builder = null;

		//个性化的超时设置
		if(headerBuilder != null){
			boolean customTimeOut = false;//是否自定义了超时

			Integer connectTimeout = CONNECT_TIMEOUT;
			Integer writeTimeout = WRITE_TIMEOUT;
			Integer readTimeout = READ_TIMEOUT;
			if(StringUtils.isNotBlank(headerBuilder.get(HEADER_NO_TIMEOUT))) {//不超时
				customTimeOut = true;
				connectTimeout = 0;
				writeTimeout = 0;
				readTimeout = 0;
			} else {
				connectTimeout = BaseUtils.toInteger(headerBuilder.get(HEADER_CONNECT_TIMEOUT_SECONDS));
				if(connectTimeout == null){
					connectTimeout = CONNECT_TIMEOUT;
				} else {
					customTimeOut = true;
				}
				writeTimeout = BaseUtils.toInteger(headerBuilder.get(HEADER_WRITE_TIMEOUT_SECONDS));
				if(writeTimeout == null){
					writeTimeout = WRITE_TIMEOUT;
				} else {
					customTimeOut = true;
				}
				readTimeout = BaseUtils.toInteger(headerBuilder.get(HEADER_READ_TIMEOUT_SECONDS));
				if(readTimeout == null){
					readTimeout = READ_TIMEOUT;
				} else {
					customTimeOut = true;
				}
			}
			if(customTimeOut) {
				if (builder == null) {
					builder = sharedClient.newBuilder();//newBuilder 新建出来的client将复用sharedClient的线程池
				}
				builder.connectTimeout(connectTimeout, TimeUnit.SECONDS)
						.writeTimeout(writeTimeout, TimeUnit.SECONDS)
						.readTimeout(readTimeout, TimeUnit.SECONDS);
			}
		}

		//个性化的HTTPS设置
		if(url.toLowerCase().startsWith("https:")){//是否是HTTPS请求的客户端
			if(builder == null){
				builder = sharedClient.newBuilder();//newBuilder 新建出来的client将复用sharedClient的线程池
			}
			//信任全部HTTPS证书
			builder.sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
		        .hostnameVerifier(new HostnameVerifier() {  
		            @Override  
		            public boolean verify(String hostname, SSLSession session) {  
		                return true;  
		            }  
		        });
		}

		if(builder == null){//没有个性化的配置
			return sharedClient;
		} else { //有个性化的设置
			return builder.build();
		}
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
	
	/**
	 * 获取Response的信息
	 * @param response
	 * @return
	 */
	private static String getResponseInfo(Response response) throws IOException{
		try{
			if(response == null){
				return "response is null";
			}
			ResponseBody responseBody = response.body();
			if(responseBody != null){
				String bodyString = responseBody.string();
				if(bodyString != null){
					return bodyString;
				}				
			}
			return "response code:" + response.code() + ", response body: null";
		}catch(IOException e){
			throw e;
		}
	}
	
	//-------------------------
	
	/**
	 * 信任全部HTTPS证书的X509TrustManager
	 * @author Meron
	 *
	 */
	public static final class TrustAllCerts implements X509TrustManager {  
	    @Override    
	    public void checkClientTrusted(X509Certificate[] chain, String authType) {}  
	    
	    @Override    
	    public void checkServerTrusted(X509Certificate[] chain, String authType) {}  
	    
	    @Override    
	    public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}    
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
