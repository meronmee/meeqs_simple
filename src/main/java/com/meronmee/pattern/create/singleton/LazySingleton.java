package com.meronmee.pattern.create.singleton;

/**
 * 懒汉式单例
 * 时间换空间
 * @author Meron
 *
 */
public class LazySingleton {
	private static LazySingleton instance = null;
	
	/**
	 * 单例的必要条件：构造函数私有化
	 */
	private LazySingleton(){		
	}
	
	/**
	 * 静态工厂方式
	 * 注意：一般系统中存在多线程的问题，所以必须加上synchronized修饰词，否则多线程情况下可能会出现多个实例
	 * 缺点：因为synchronized，每次都换检查，而实际上只是第一次使用时需要检查，白白的浪费了时间
	 */
	synchronized public  static LazySingleton getInstance(){
		if(instance == null){
			instance = new LazySingleton();
		}
		return instance;
	}
}
