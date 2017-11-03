package com.meronmee.app;

public class Log {
	
	public static void info(Object... objs){
		if(objs == null){
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(Object obj : objs){
			sb.append(String.valueOf(obj));
		}
		System.out.println(sb.toString());
	}
	/**
     * 
     * 替换字符串模板中的变量,变量格式{n}, n为变量序号,从0开始
     * 如：xxxx{0}yyy{1}zzz
     * @param template - 字符串模板
     * @param values - 替换后的值列表
     * @return
     */
    public static String format(String template, Object... values){
    	String result = template;
    	
    	for(int i=0; i<values.length; i++){
    		result = result.replaceAll("\\{"+i+"\\}", String.valueOf(values[i]));
    	}
    	
    	return result;
    }
}
