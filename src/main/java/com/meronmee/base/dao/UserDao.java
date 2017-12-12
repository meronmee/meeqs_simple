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
     * 根据偏移量查询用户列表
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
	
}
