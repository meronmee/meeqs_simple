package com.meronmee.pattern.create.singleton;

/**
 * 饿汉式单例
 * 空间换时间
 * @author Meron
 *
 */
public class EagerSingleton {
	private static final EagerSingleton instance = new EagerSingleton();
	
	/**
	 * 单例的必要条件：构造函数私有化
	 */
	private EagerSingleton(){		
	}
	
	/**
	 * 静态工厂方式
	 * @return
	 */
	public static EagerSingleton getInstance(){
		return instance;
	}
}
