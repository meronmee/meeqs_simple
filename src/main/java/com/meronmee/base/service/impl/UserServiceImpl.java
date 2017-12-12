package com.meronmee.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meronmee.base.dao.UserDao;
import com.meronmee.base.model.User;
import com.meronmee.base.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	
	
	@Override
	public List<User> getUserList(int offset, int limit) {
		return userDao.queryAll(offset, limit);
	}
	@Override
	public void updateNickname(Long id, String nickname) {
		this.userDao.updateNickname(id, nickname);
		
	}
	@Override
	public User queryByPhone(String phone) {
		return this.userDao.queryByPhone(phone);
	}
	
}
