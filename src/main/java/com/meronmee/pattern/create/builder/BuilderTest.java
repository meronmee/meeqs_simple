package com.meronmee.pattern.create.builder;

public class BuilderTest {
	public static void main(String[] args) throws Exception{
		//创建构造器
		Email.Builder builder = new Email.Builder("from@a.com", "to@b.com", "标题");
		
		//设置相关数据，然后建造目标对象
		Email email = builder
				.setContent("夏至来了")
				//.setCc("cc@c.com")
				.build();
		
		//使用构建好的对象
		email.send();
	}
}
