package com.meronmee.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meronmee.base.model.User;

public interface UserDao {

	/**
     * 根据手机号查询用户对象
     *
     * @param phone
     * @return
     */
    public User queryByPhone(String phone);    
    
    /**
     * 分页查询用户列表
     *
     * @param offset
     * @param limit
     * @return
     */
    public List<User> queryAll(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 修改昵称
     */
    public void updateNickname(@Param("id") Long id, @Param("nickname") String nickname);
    	

    /**
     * 根据用户名或手机号查询用户对象
     *
     * @param param
     */
    public User findByUsernameOrPhone(String param);    
    
}
