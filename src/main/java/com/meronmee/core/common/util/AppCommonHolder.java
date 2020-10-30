package com.meronmee.core.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author meron
 *
 */
public class AppCommonHolder {   
	
	/**
	 * 可缓存的线程池。
	 * 如果线程池的大小超过了处理任务所需要的线程，那么就会回收部分空闲（60秒不执行任务）的线程，
	 * 当任务数增加时，此线程池又可以智能的添加新线程来处理任务。
	 * 此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说JVM）能够创建的最大线程大小。
	 */
	public static ExecutorService threadPool = Executors.newCachedThreadPool();
}
