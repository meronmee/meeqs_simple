package com.meronmee.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meronmee.base.domain.User;
 

/**
 * 用户Dao<p>
 * 类路径需要和src/main/resources/mapper/UserMapper.xml中的namespace一致<p>
 * 方法和参数要和UserMapper.xml中的ID参数一致
 * @author Meron
 *
 */
public interface UserDao {

	/**
     * 根据手机号查询用户对象
     *
     * @param phone
     * @return
     */
    public User queryByPhone(@Param("phone") String phone);    
    
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
    public User findByUsernameOrPhone(@Param("param") String param);    
    
}
