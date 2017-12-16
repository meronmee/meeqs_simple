package com.meronmee.base.service;

import java.util.List;

import com.meronmee.base.model.User;


public interface UserService {
	List<User> getUserList(int offset, int limit);
	/**
     * 修改昵称
     */
	public void updateNickname(Long id, String nickname);
	
	/**
     * 根据手机号查询用户对象
     *
     * @param phone
     */
    public User findByPhone(String phone);
	/**
     * 根据用户名查询用户对象
     *
     * @param username
     */
    public User findByUsername(String username);
	
    /**
     * 根据用户名或手机号查询用户对象
     *
     * @param param
     */
    public User findByUsernameOrPhone(String param);
    

    /**
     * 获取用户的权限数据
     *
     * @param user
     */
    public List<String> getUserPermissions(User user);
    
    /**
     * 获取用户的角色数据
     *
     * @param user
     */
    public List<String> getUserRoles(User user);
}
