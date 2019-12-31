package com.meronmee.base.domain;

import com.meronmee.core.api.annotation.Table;
import com.meronmee.core.api.domain.Model;

/**
 * 
 * @author Meron
 *
 */
@Table("user")
public class User extends Model {
	private static final long serialVersionUID = -5855777370683946524L;

	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 真实姓名
	 */
	private String realname;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 性别 - 1:男, 2:女, 3:保密
	 */
	private Integer sex;
	
	/**
	 * 用户名
	 */
	public String getUsername() {
		return username;
	}/**
	 * 用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * 密码
	 */
	public String getPassword() {
		return password;
	}/**
	 * 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 手机号
	 */
	public String getMobile() {
		return mobile;
	}/**
	 * 手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	/**
	 * 昵称
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * 昵称
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	/**
	 * 真实姓名
	 */
	public String getRealname() {
		return realname;
	}
	/**
	 * 真实姓名
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	/**
	 * 邮箱
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 性别 - 1:男, 2:女, 3:保密
	 */
	public Integer getSex() {
		return sex;
	}
	/**
	 * 性别 - 1:男, 2:女, 3:保密
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}
}
