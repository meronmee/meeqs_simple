package com.meronmee.base.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meronmee.base.dao.UserDao;
import com.meronmee.base.model.User;
import com.meronmee.base.service.UserService;
import com.meronmee.core.utils.BaseUtils;
import com.meronmee.base.service.BaseService;

@Service
public class UserServiceImpl extends BaseService implements UserService{
	@Autowired
	private UserDao userDao;
	
	
	/**
	 * 分页查询所有用户
	 */
	@Override
	public List<User> getUserList(int offset, int limit) {
		return userDao.queryAll(offset, limit);
	}
	
	/**
	 * 修改昵称
	 */
	@Override
	public void updateNickname(Long id, String nickname) {
		this.userDao.updateNickname(id, nickname);		
	}
	
	/**
     * 根据手机号查询用户对象
     *
     * @param phone
     */
	@Override
    public User findByPhone(String phone) {
		if(StringUtils.isBlank(phone)){
			return null;
		}
		return this.service.findOneModelByProperty(User.class, "mobile", phone);
	}

	/**
     * 根据用户名查询用户对象
     *
     * @param username
     */
	@Override
    public User findByUsername(String username){
		if(StringUtils.isBlank(username)){
			return null;
		}
		return this.service.findOneModelByProperty(User.class, "username", username);
	}
	
	/**
     * 根据用户名或手机号查询用户对象
     *
     * @param param
     */
	@Override
    public User findByUsernameOrPhone(String param){
		if(StringUtils.isBlank(param)){
			return null;
		}
		
		//return this.userDao.findByUsernameOrPhone(param);
		
		User user = null;
		if(BaseUtils.isMobilePhone(param)){
			user = this.findByPhone(param);
		}
		if(user == null){
			user = this.findByUsername(param);
		}
		
		return user;
	}
	
	/**
     * 获取用户的权限数据
     *
     * @param user
     */
    public List<String> getUserPermissions(User user){
    	List<String> permissions = new LinkedList<>();
    	//permissions.add("user:list");
    	return permissions;
    }
    
    /**
     * 获取用户的角色数据
     *
     * @param user
     */
    public List<String> getUserRoles(User user){
    	List<String> roles = new LinkedList<>();
    	return roles;
    }
	
}
