package com.meronmee.demo.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Dao<p>
 * 类路径需要和src/main/resources/service/mapper/DemoDao.xml.xml中的namespace一致<p>
 * 方法和参数要和 DemoMapper.xml中的ID参数一致
 * @author Meron
 *
 */
public interface DemoDao {

	/**
     * 根据手机号查询用户对象
     *
     * @param phone
     * @return
     */
    public void bathBindUserRole(@Param("userId") Long userId, @Param("roleIds")  List<Long> roleIds);

    public void truncateTemp();
}
