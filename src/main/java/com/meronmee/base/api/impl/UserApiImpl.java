package com.meronmee.base.api.impl;

import com.meronmee.base.api.UserApi;
import com.meronmee.base.dao.UserDao;
import com.meronmee.base.domain.User;
import com.meronmee.core.api.domain.RequestInfo;
import com.meronmee.core.common.util.BaseUtils;
import com.meronmee.core.common.util.ShortCache;
import com.meronmee.core.service.database.DataService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserApiImpl implements UserApi{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private DataService dataService;

    /**
     * 查询用户
     */
    @Override
    public User retrieve(Long userId){
        if(BaseUtils.isNull0(userId)){
            return null;
        }
        return dataService.retrieve(User.class, userId);
    }

    /**
     * 获取当前请求的用户
     *
     * @param request
     * @return
     */
    @Override
    public User getCurrentUser(RequestInfo request) {
        if (request == null) {
            return null;
        }

        User user = getRequestUser(request);
        if (user != null) {
            checkUser(user);
            return user;
        }
        String username = request.getStringParam("username");
        if (StringUtils.isNotBlank(username)) {
            user = findByUsername(username);
            if (user != null) {
                checkUser(user);
                setRequestUser(request, user);
                return user;
            }
        }
        /*
        //根据AccessToken查询用户
        String accessToken = request.getAccessToken();
        if (StringUtils.isNotBlank(accessToken)) {
            user = findUserByToken(accessToken);
            if (user != null) {
                checkUser(user);
                setRequestUser(request, user);
                return user;
            }
        }

        //根据微信unionid查询用户
        String wxUnionId = request.getWxUnoinId();
        if (StringUtils.isNotBlank(wxUnionId)) {
            user = findUserByWxUnionId(wxUnionId);
            if (user != null) {
                checkUser(user);
                setRequestUser(request, user);
                return user;
            }
        }

        //根据微信openid查询用户
        String wxOpenid = request.getWxOpenId();
        if (StringUtils.isNotBlank(wxOpenid)) {
            user = findUserByWxOpenId(wxOpenid);
            if (user != null) {
                checkUser(user);
                setRequestUser(request, user);
                return user;
            }
        }
        */
        return user;
    }

    /**
     * 缓存当前请求的用户
     *
     * @param request
     * @param user
     */
    private void setRequestUser(RequestInfo request, User user) {
        if (request == null || user == null) {
            return;
        }

        String id = request.getId();
        if (BaseUtils.isBlank(id)) {
            return;
        }

        String key = "RequestInfo_User_" + id;
        Object cache = ShortCache.get(key);
        ShortCache.set(key, user);
    }

    /**
     * 读取当前请求缓存的用户
     *
     * @param request
     */
    private User getRequestUser(RequestInfo request) {
        if (request == null) {
            return null;
        }

        String id = request.getId();
        if (BaseUtils.isBlank(id)) {
            return null;
        }
        String key = "RequestInfo_User_" + id;
        Object cache = ShortCache.get(key);
        if(cache != null){
            return (User)cache;
        } else {
            return null;
        }
    }
    /**
     * 检查用户状态
     *
     * @param user
     */
    private void checkUser(User user) {
        if (user == null) {
            return;
        }
        /*
        if (xxx) {
            Assert.error(yyy);
        }
        */
    }


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
		return this.dataService.findOneByProperty(User.class, "mobile", phone);
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
		return this.dataService.findOneByProperty(User.class, "username", username);
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
