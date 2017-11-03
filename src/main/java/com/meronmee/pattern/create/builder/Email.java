package com.meronmee.pattern.create.builder;

import com.meronmee.app.Assert;
import com.meronmee.app.Log;

public class Email {
	private String from;
	private String to;
	private String title;
	private String cc;
	private String content;
	
	/**
	 * 私有构造方法，避免在外部直接实例化
	 */
	private Email(Builder builder){
		this.from = builder.from;
		this.to = builder.to;
		this.title = builder.title;
		this.cc = builder.cc;
		this.content = builder.content;
	}
	
	
	public void send(){
		Log.info("Email["
			+ Log.format("from:{0}, to:{1}, title:{2}, cc:{3}, content:{4}", this.from, this.to, this.title, this.cc, this.content)
			+ "] is sending...");
	}
	
	
	/**
	 * 建造者一般使用工友的静态内部类
	 */
	public static class Builder{
		private String from;
		private String to;
		private String title;
		private String cc;
		private String content;
		
		/**
		 * 关键的参数可以在builder的构造器中传入，当然，也可以使用空参构造器，然后再后面设置
		 */
		public Builder(String from, String to, String title){
			this.from = from;
			this.to = to;
			this.title = title;
		}
		
		/**
		 * 设置完相关属性后，通过build方法返回目标对象Email
		 */
		public Email build(){
			Assert.isNotBlank(from, "发件人不能为空 ");
			Assert.isNotBlank(to, "收件人不能为空 ");
			Assert.isNotBlank(title, "标题不能为空 ");
			
			return new Email(this);
		}
		
		/**
		 * 为了可以链式操作，builder的所有set方法返回实例本身
		 */
		public Builder setFrom(String from) {
			this.from = from;
			return this;
		}

		public Builder setTo(String to) {
			this.to = to;
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setCc(String cc) {
			this.cc = cc;
			return this;
		}

		public Builder setContent(String content) {
			this.content = content;
			return this;
		}
		
		
		
	}
}
