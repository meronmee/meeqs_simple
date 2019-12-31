package com.meronmee.core.api.domain;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.meronmee.core.api.util.ApiUtils;


/**
 * 请求信息
 * @author mizr
 *
 */
public class RequestInfo implements Serializable {
	private static final long serialVersionUID = -3021623847946808119L;
	
	/**
	 * 请求的唯一编号
	 */
	private String id;
	/**
	 * 请求的客户端真实地址
	 */
	private String ip;
	/**
	 * 请求的URL完整地址
	 */
	private String url;

	//------------------------

	/**
	 * 用户accesstoken
	 */
	private String accessToken;
	 
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 当前APP版本号，如ios5.1.0
	 */
	private String appversion;
 
    /**
     * 客户端设备编号
     */
	private String clientId;
    /**
     * 渠道ID
     */
    private String channelId;
    /**
     * 当前位置纬度
     */
    private String lat;
    /**
     * 当前位置经度
     */
    private String lng;

    /**
     * 微信openid
     */
    private String wxOpenId;
    /**
     * 微信unoinId
     */
    private String wxUnoinId;  
    /**
     * isApp
     */
    private Boolean isApp;

    /**
     * isIos
     */
    private Boolean isIOS;
    /**
     * isAndroid
     */
    private Boolean isAndroid;
    /**
     * isWeixin
     */
    private Boolean isWeixin;

    //---------------------------------

	/**
	 * HTTP请求的body和querystring信息
	 */
	public JSONObject body = new JSONObject();

	/**
	 * HTTP请求的header信息
	 */
	public JSONObject header = new JSONObject();


    /**
     * HTTP请求的cookie信息
     */
    public JSONObject cookie = new JSONObject();

	/** HTTP请求的body和querystring信息 */
	public JSONObject getBody() {
		if(body == null){
			body = new JSONObject();
		}
		return this.body;
	}

	/** HTTP请求的body和querystring信息 */
	public RequestInfo setBody(JSONObject body) {
		if(body == null){
			body = new JSONObject();
		}
		this.body = body;
		return this;
	}
	/** 增加HTTP请求的body信息 */
	public JSONObject putBody(String key, String value) {
		this.getBody().put(key, value);
		return this.body;
	}

	/** HTTP请求的header信息 */
	public JSONObject getHeader() {
		if(header == null){
			header = new JSONObject();
		}
		return this.header;
	}

	/** HTTP请求的header信息 */
	public RequestInfo setHeader(JSONObject header) {
		if(header == null){
			header = new JSONObject();
		}
		this.header = header;
        return this;
	}

	/** 增加HTTP请求的header信息 */
	public JSONObject putHeader(String key, String value) {
		this.getHeader().put(key, value);
		return this.header;
	}


    /** HTTP请求的Cookie信息 */
    public JSONObject getCookie() {
        if(cookie == null){
            cookie = new JSONObject();
        }
        return this.cookie;
    }

    /** HTTP请求的cookie信息 */
    public RequestInfo setCookie(JSONObject cookie) {
        if(cookie == null){
            cookie = new JSONObject();
        }
        this.cookie = cookie;
        return this;
    }

    /** 增加HTTP请求的header信息 */
    public JSONObject putCookie(String key, String value) {
        this.getCookie().put(key, value);
        return this.cookie;
    }

    //------------------------

    /**
     * 获取请求中的参数
     * @param name 参数名称，多个用逗号分割
     * @param defaultValue
     * @return
     */
    public String getParam(String name, String defaultValue) {
        if (ApiUtils.isBlank(name)) {
            return defaultValue;
        }

        for(String thisName :  name.split(",")) {
            thisName = thisName.trim();
            if(ApiUtils.isBlank(thisName)){
                continue;
            }
            String value = getBody().getString(thisName);
            if(ApiUtils.isNotBlank(value)){
                return value;
            }
        }

        return defaultValue;
    }


    /**
     * 获取String类型请求参数
     * @param name  参数名称，多个用逗号分割
     * @return
     */
    public String getStringParam(String name) {
        return getStringParam(name, null);
    }
    /**
     * 获取String类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @param defaultValue  默认值
     * @return
     */
    public String getStringParam(String name, String defaultValue){
        return getParam(name, defaultValue);
    }

    /**
     * 获取Integer类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @param defaultValue  默认值
     * @return
     */
    public Integer getIntegerParam(String name, Integer defaultValue){
        String value = getStringParam(name);
        return ApiUtils.toInteger(value, defaultValue);
    }

    /**
     * 获取Integer类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @return
     */
    public Integer getIntegerParam(String name) {
        return getIntegerParam(name, null);
    }

    /**
     * 获取Long类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @return
     */
    public Long getLongParam(String name) {
        return getLongParam(name, null);
    }

    /**
     * 获取Long类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @param defaultValue  默认值
     * @return
     */
    public Long getLongParam(String name, Long defaultValue){
        String value = getStringParam(name);
        return ApiUtils.toLong(value, defaultValue);
    }


    /**
     * 获取Double类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @return
     */
    public Double getDoubleParam(String name) {
        return getDoubleParam(name, null);
    }

    /**
     * 获取 Double 类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @param defaultValue  默认值
     * @return
     */
    public Double getDoubleParam(String name, Double defaultValue){
        String value = getStringParam(name);
        return ApiUtils.toDouble(value, defaultValue);
    }


    /**
     * 获取 Boolean 类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @return
     */
    public Boolean getBooleanParam(String name) {
        return getBooleanParam(name, null);
    }

    /**
     * 获取 Boolean 类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @param defaultValue  默认值
     * @return
     */
    public Boolean getBooleanParam(String name, Boolean defaultValue){
        String value = getStringParam(name);
        return ApiUtils.toBoolean(value, defaultValue);
    }


    /**
     * 获取 Float 类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @return
     */
    public Float getFloatParam(String name) {
        return getFloatParam(name, null);
    }

    /**
     * 获取Float类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @param defaultValue  默认值
     * @return
     */
    public Float getFloatParam(String name, Float defaultValue){
        String value = getStringParam(name);
        return ApiUtils.toFloat(value, defaultValue);
    }


    /**
     * 获取 Date 类型请求参数
     * @param name 参数名称，多个用逗号分割
     * @return
     */
    public Date getDateParam(String name){
        String value = getStringParam(name);
        return ApiUtils.toDate(value);
    }


    //--------------------------------- 


	/** 请求的唯一编号 */
	public String getId() {
		return this.id;
	}

	/** 请求的唯一编号 */
	public RequestInfo setId(String id) {
		this.id = id;
		return this;
	}

	/** 请求的客户端真实地址 */
	public String getIp() {
		return this.ip;
	}

	/** 请求的客户端真实地址 */
	public RequestInfo setIp(String ip) {
		this.ip = ip;
		return this;
	}

	/** 请求的URL完整地址 */
	public String getUrl() {
		return this.url;
	}

	/** 请求的URL完整地址 */
	public RequestInfo setUrl(String url) {
		this.url = url;
		return this;
	}


	/** 用户accesstoken */
	public String getAccessToken() {
		return this.accessToken;
	}

	/** 用户accesstoken */
	public RequestInfo setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}
 
	/** 用户ID */
	public String getUserId() {
		return this.userId;
	}

	/** 用户ID */
	public RequestInfo setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	/** 当前APP版本号，如ios5.1.0 */
	public String getAppversion() {
		return this.appversion;
	}

	/** 当前APP版本号，如ios5.1.0 */
	public RequestInfo setAppversion(String appversion) {
		this.appversion = appversion;
		return this;
	}
 
    /** 客户端设备编号 */
    public String getClientId() {
        return this.clientId;
    }

    /** 客户端设备编号 */
    public RequestInfo setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /** 渠道ID */
    public String getChannelId() {
        return this.channelId;
    }

    /** 渠道ID */
    public RequestInfo setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }


    /** 微信openid */
    public String getWxOpenId() {
        return this.wxOpenId;
    }

    /** 微信openid */
    public RequestInfo setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
        return this;
    }

    /** 微信unoinId */
    public String getWxUnoinId() {
        return this.wxUnoinId;
    }

    /** 微信unoinId */
    public RequestInfo setWxUnoinId(String wxUnoinId) {
        this.wxUnoinId = wxUnoinId;
        return this;
    }
 
    /** isApp */
    public Boolean getIsApp() {
        if(isApp == null){
            isApp = false;
        }
        return this.isApp;
    }

    /** isApp */
    public RequestInfo setIsApp(Boolean isApp) {
        if(isApp == null){
            isApp = false;
        }
        this.isApp = isApp;
        return this;
    }

    /** isIos */
    public Boolean getIsIOS() {
        if(isIOS == null){
            isIOS = false;
        }
        return this.isIOS;
    }

    /** isIos */
    public RequestInfo setIsIOS(Boolean isIOS) {
        if(isIOS == null){
            isIOS = false;
        }
        this.isIOS = isIOS;
        return this;
    }

    /** isAndroid */
    public Boolean getIsAndroid() {
        if(isAndroid == null){
            isAndroid = false;
        }
        return this.isAndroid;
    }

    /** isAndroid */
    public RequestInfo setIsAndroid(Boolean isAndroid) {
        if(isAndroid == null){
            isAndroid = false;
        }
        this.isAndroid = isAndroid;
        return this;
    }

    /** isWeixin */
    public Boolean getIsWeixin() {
        if(isWeixin == null){
            isWeixin = false;
        }
        return this.isWeixin;
    }

    /** isWeixin */
    public RequestInfo setIsWeixin(Boolean isWeixin) {
        if(isWeixin == null){
            isWeixin = false;
        }
        this.isWeixin = isWeixin;
        return this;
    }

    /** 当前位置纬度 */
    public String getLat() {
        return this.lat;
    }

    /** 当前位置纬度 */
    public RequestInfo setLat(String lat) {
        this.lat = lat;
        return this;
    }

    /** 当前位置经度 */
    public String getLng() {
        return this.lng;
    }

    /** 当前位置经度 */
    public RequestInfo setLng(String lng) {
        this.lng = lng;
        return this;
    }
 
}
