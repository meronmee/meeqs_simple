package com.meronmee.core.utils;



/**
 * 它山之石可以攻玉<p>
 * 开源包中提供了很多优秀的工具类
 * @author Meron
 *
 */
public class Stones {
	public static void fire(){
		/**-------------apache commons------------**/
		org.apache.commons.lang3.StringUtils.isNotBlank(null);
		//...

		/**-------------Spring------------**/
		//org.springframework.util.*;
		org.springframework.util.xml.DomUtils.getChildElements(null);
		
		/**-------------Shiro------------**/
		org.apache.shiro.util.StringUtils.clean(null);
		org.apache.shiro.util.Assert.isInstanceOf(null, null);
		org.apache.shiro.util.ClassUtils.forName(null);
		new org.apache.shiro.util.AntPathMatcher().match(null, null);
		org.apache.shiro.util.CollectionUtils.asSet(null);		
		org.apache.shiro.web.util.WebUtils.getCleanParam(null, null);

		/**-------------logback------------**/
		ch.qos.logback.core.util.ExecutorServiceUtil.newExecutorService().execute(null);
	}
}
