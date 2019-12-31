package com.meronmee.core.web.dto;

/**
 *  Ajax请求失败时返回数据封装类<p>
 *  {"code":400, "msg":"操作失败", "data":... }
 */
public class BizError extends JsonResult{
	private static final long serialVersionUID = -4947169183770256483L;
	/**
	 *  Ajax请求失败时返回数据封装类<p>
	 *  {"code":400, "msg":"操作失败", "data":... }
	 */
	public BizError(){
		this.setCode(JsonResult.Code.ERROR).setMsg("Failure");
	}
	
	/**
	 * @param msg 提示消息
	 * @param data 主体数据
	 */
    public BizError(String msg, Object data) {
    	super(JsonResult.Code.ERROR, msg, data);
    }
    
    /**
	 * @param data 提示消息
	 */
    public BizError(String msg) {
    	this(msg, null);
    }

    /**
	 * 转换为JSON字符串
	 * @return 格式：{"code":400, "msg":"操作失败", "data":... }
	 */
	public String toJson(){
		return super.toJson();
	}
}
