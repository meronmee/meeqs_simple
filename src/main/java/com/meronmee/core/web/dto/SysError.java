package com.meronmee.core.web.dto;

/**
 *  Ajax请求出现程序异常时候时返回数据封装类<p>
 *  {"code":500, "msg":"异常信息", "data":... }
 */
public class SysError extends JsonResult{
	private static final long serialVersionUID = -4947169183770256483L;
	
	/**
	 *  Ajax请求出现程序异常时候时返回数据封装类<p>
	 *  {"code":500, "msg":"异常信息", "data":... }
	 */
	public SysError(){
		this.setCode(JsonResult.Code.EXCEPTION).setMsg("异常信息");
	}
	
    /**
	 * 转换为JSON字符串
	 * @return 格式：{"code":500, "msg":"异常信息", "data":... }
	 */
	public String toJson(){
		return super.toJson();
	}
}
