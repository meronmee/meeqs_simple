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
     * @return
     */
    public User queryByPhone(String phone);
}
