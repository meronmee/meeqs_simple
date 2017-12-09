package com.meronmee.core.dto;

/**
 *  Ajax请求授权失败（未授权/权限不够）时返回数据封装类<p>
 *  {"code":402, "msg":"授权失败", "data":... }
 */
public class CertError extends JsonResult{
	private static final long serialVersionUID = -4947169183770256483L;

	public CertError(){
		this.setCode(JsonResult.Code.CERT_ERROR).setMsg("授权失败");
	}
	
    /**
	 * 转换为JSON字符串
	 * @return 格式：{"code":402, "msg":"授权失败", "data":... }
	 */
	public String toJson(){
		return super.toJson();
	}
}
