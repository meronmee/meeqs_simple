package com.meronmee.base.api;

import com.meronmee.base.domain.User;
import com.meronmee.core.api.domain.RequestInfo;

import java.util.List;

/**
 * 用户相关服务接口
 * @author Meron
 * @date 2019-12-30 16:37
 */
public interface UserApi {
    /**
     * 查询用户
     */
    public User retrieve(Long userId);
    /**
     * 获取当前请求的用户
     *
     * @param request
     * @return
     */
    public User getCurrentUser(RequestInfo request);

	/**
	 * 分页查询所有用户
	 */
	public List<User> getUserList(int offset, int limit);
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
