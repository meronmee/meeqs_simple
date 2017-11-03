package com.meronmee.pattern.create.singleton;

/**
 * 内部类式单例
 * Lazy initialization holder class模式
 * 
 * 既节省时间，又节省了空间
 * 原理：
 *  在多线程开发中，为了解决并发问题，主要是通过使用synchronized来加互斥锁进行同步控制。但是在某些情况中，JVM已经隐含地为您执行了同步，
 *  这些情况下就不用自己再来进行同步控制了。这些情况包括：
　　1.由静态初始化器（在静态字段上或static{}块中的初始化器）初始化数据时
　　2.访问final字段时
　　3.在创建线程之前创建对象时
　　4.线程可以看见它将要处理的对象时
 *
 */
public class InnerClassSingleton {		 
	/**
	 * 单例的必要条件：构造函数私有化
	 */
	private InnerClassSingleton(){		
	}
	
	/**
	 * 类级别的内部类，即静态成员式的内部类，该内部类的实例与外部类的实例没有绑定关系，而且只有被调用时才会装载，从而实现了延迟加载
	 *
	 */
	private static class SingletonHolder{
		private static InnerClassSingleton instance = new InnerClassSingleton();
	}
	
	/**
	 * 静态工厂方式，通过内部类来获取唯一的实例
	 */
	public  static InnerClassSingleton getInstance(){
		return SingletonHolder.instance;
	}
}
