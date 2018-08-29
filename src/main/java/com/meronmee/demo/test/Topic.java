package com.meronmee.demo.test;

import com.meronmee.core.annotation.Table;
import com.meronmee.core.model.Model;

/**
 * 
 * @author Meron
 *
 */
@Table("im_topic")
public class Topic extends Model {
	private static final long serialVersionUID = -5855777370683946524L;

	/**
	 * 用户名
	 */
	private String topicName;
	
	/**
	 * 密码
	 */
	private String topicDesc;

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getTopicDesc() {
		return topicDesc;
	}

	public void setTopicDesc(String topicDesc) {
		this.topicDesc = topicDesc;
	}
	
	
}
