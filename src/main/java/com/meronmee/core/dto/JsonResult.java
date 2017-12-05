package com.meronmee.core.dto;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.meronmee.core.utils.BaseUtils;

/**
 * Ajax请求的返回数据封装类
 * @author Meron
 *
 */
public class JsonResult implements Serializable {
	private static final long serialVersionUID = -4185151304730685014L;
	
	/**
	 * 返回码 200成功, 400:业务报错, 401:未认证(未登录), 402:未授权(权限不够), 500:程序错误
	 */
	private Code code;
	/**
	 * 成功或失败的提示信息
	 */
    private String msg;
    /**
     * 成功或失败返回的主体数据
     */
    private Object data;

    
    public JsonResult(){
    }
    /**
     * @param code 	返回码
     * @param msg 	提示信息
     * @param data	主体数据
     */
    public JsonResult(Code code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
	public Code getCode() {
		return code;
	}
	public int getCodeValue() {
		return code.getCode();
	}
	public JsonResult setCode(Code code) {
		this.code = code;
		return this;
	}
	public String getMsg() {
		return msg;
	}
	public JsonResult setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	public Object getData() {
		return data;
	}
	public JsonResult setData(Object data) {
		this.data = data;
		return this;		
	}	
	
	@Override
	public String toString() {
		return BaseUtils.join("JsonResult [code=", this.getCodeValue(), ", msg=", this.getMsg(), ", data=", this.getData(), "]");
	}
		
	/**
	 * 转换为JSON字符串
	 * @return 格式：{"code":200, "msg":"操作成功", "data":... }
	 */
	public String toJson(final SerializeFilter... filters) {
		JSONObject json = new JSONObject();
		json.put("code", this.getCodeValue());
		json.put("msg", this.getMsg());
		json.put("data", this.getData());
		
		//SerializerFeature.DisableCircularReferenceDetect 避免同一对象的循环引用
		if(filters != null && filters.length>0){
			return JSON.toJSONString(json, filters, SerializerFeature.DisableCircularReferenceDetect);
		} else {
			return JSON.toJSONString(json, SerializerFeature.DisableCircularReferenceDetect);			
		}
	}

	//-----------------------------------------
	
	/**
	 * 返回码字典<p>
	 * 
	 * 200成功, 400:业务报错, 401:未认证(未登录), 402:未授权(权限不够), 500:程序错误
	 */
	public enum Code {
		/** 200 - 成功 */
		SUCCESS(200),
		
		/** 400 - 业务报错 */
		ERROR(400),
		
		/** 401 - 未认证(未登录) */
		CERT_ERROR(401),
		
		/** 402 - 未授权(权限不够) */
		AUTH_ERROR(402),

		/** 500 - 程序错误 */
		EXCEPTION(500);
		
		private int code;
		
		//构造函数，枚举类型只能为私有
	    private Code(int code) {
	        this.code = code;
	    }
	    /**
	     * 返回整型错误码  200成功, 400:业务报错, 401:未认证(未登录), 402:未授权(权限不够), 500:程序错误
	     */
	    public int getCode(){
	    	return this.code;
	    }	
	   
	    public String getName(){
	    	return this.name();
	    }  
	}
	
	
}
