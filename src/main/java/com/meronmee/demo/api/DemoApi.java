package com.meronmee.demo.api;

import com.meronmee.core.api.domain.RequestInfo;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 
 * @author Meron
 * @date 2019-12-30 16:56
 */ 
public interface DemoApi {
    /**
     * MyBatis+MySQL数据库查询这两提供了三种方式：
     * <ol>1、使用公共接口面向 Model 对象的增删改查<br>
     *			优点:快速便捷<br>
     *			使用条件:只能对单个规范化的实体进行增删改查，无法做复杂的查询<br>
     *			推荐度:☆☆☆☆☆</ol>
     *
     * <ol>2、使用公共接口引用 SQL 语句的增删改查<br>
     *			优点:支持所有SQL语句<br>
     *			使用条件:需要在Mapper中写SQL语句，需要在SqlKey中维护SQL语句ID<br>
     *			推荐度:☆☆☆☆，在“方式1”无法满足的情况下推荐使用该方式</ol>
     *
     * <ol>3、使用 DAO 引用 SQL 语句的增删改查<br>
     *			优点:支持所有SQL语句<br>
     *			使用条件:需要在Mapper中写SQL语句，需要在针对各个业务模块写DAO层接口。分页查询需要自己在SQL中写limit，相比方式二的仅有优势在于SQL调用参数更加直观<br>
     *			推荐度:☆☆☆</ol>
     *
     * 以上三种方式可以任意混合使用<p>
     *
     * MyBatis数据库查询 - 1、使用公共接口面向 Model 对象的增删改查<p>
     */
    public void testDBModel(RequestInfo requestInfo);
    /**
     * MyBatis数据库查询 - 2、使用公共接口引用 SQL 语句的增删改查<p>
     */
    public void testDBSql(RequestInfo requestInfo);
    /**
     * MyBatis数据库查询 - 3、使用 DAO 引用 SQL 语句的增删改查<p>
     */
    public void testDBDao(RequestInfo requestInfo);

    /**
     * 根据手机号查询用户对象
     *
     * @param phone
     * @return
     */
    public void bathBindUserRole(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    public void truncateTemp();
}
