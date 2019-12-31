package com.meronmee.core.web.dto;

/**
 *  Ajax请求成功时返回数据封装类<p>
 *  {"code":200, "msg":"操作成功", "data":... }
 */
public class Success extends JsonResult{
	private static final long serialVersionUID = -454516195068421848L;
	/**
	 *  Ajax请求成功时返回数据封装类<p>
	 *  {"code":200, "msg":"操作成功", "data":... }
	 */
	public Success(){
		this.setCode(JsonResult.Code.SUCCESS).setMsg("Success");
	}
	
	/**
	 * @param msg 提示消息
	 * @param data 主体数据
	 */
    public Success(String msg, Object data) {
    	super(JsonResult.Code.SUCCESS, msg, data);
    }
    
    /**
	 * @param data 主体数据
	 */
    public Success(Object data) {
    	this("Success", data);
    }
    
    /**
	 * 转换为JSON字符串
	 * @return 格式：{"code":200, "msg":"操作成功", "data":... }
	 */
	public String toJson(){
		return super.toJson();
	}
}
