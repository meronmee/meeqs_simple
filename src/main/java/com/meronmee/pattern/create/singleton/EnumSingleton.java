package com.meronmee.pattern.create.singleton;

/**
 * 单枚举式的单例
 * 
 * 按照《高效Java 第二版》中的说法：单元素的枚举类型已经成为实现Singleton的最佳方法。
 * 用枚举来实现单例非常简单，只需要编写一个包含单个元素的枚举类型即可。
 * 使用枚举来实现单实例控制会更加简洁，而且无偿地提供了序列化机制，
 * 并由JVM从根本上提供保障，绝对防止多次实例化，是更简洁、高效、安全的实现单例的方式。
 */
public enum EnumSingleton {
	/**
     * 只定义一个枚举的元素，它就代表了EnumSingleton类型的唯一实例
     */
	instance;
	
	/**
	 * 枚举类型无参构造可以不显式的写，如果写，只能为私有的
	 * 与单例的私有特性不磨而合
	 */
    private EnumSingleton() {
    }
    
    //-----------------
    
	/**
	 * 枚举类型可以有自己的成员变量和方法
	 */
	private String filed;
	public String getFiled() {
		return filed;
	}
	public void setFiled(String filed) {
		this.filed = filed;
	}
}
