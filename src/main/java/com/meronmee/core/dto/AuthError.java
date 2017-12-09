package com.meronmee.core.dto;

/**
 *  Ajax请求认证失败（未登录）时返回数据封装类<p>
 *  {"code":401, "msg":"请登录后再操作", "data":... }
 */
public class AuthError extends JsonResult{
	private static final long serialVersionUID = -4947169183770256483L;

	public AuthError(){
		this.setCode(JsonResult.Code.AUTH_ERROR).setMsg("请登录后再操作");
	}
	
    /**
	 * 转换为JSON字符串
	 * @return 格式：{"code":401, "msg":"请登录后再操作", "data":... }
	 */
	public String toJson(){
		return super.toJson();
	}
}
