package com.meronmee.demo.service.impl;

import com.meronmee.base.api.UserApi;
import com.meronmee.base.domain.User;
import com.meronmee.core.api.domain.Pager;
import com.meronmee.core.api.domain.RequestInfo;
import com.meronmee.core.common.util.BaseUtils;
import com.meronmee.core.service.database.DateService;
import com.meronmee.demo.api.DemoApi;
import com.meronmee.demo.constant.DemoSqlKeyConst;
import com.meronmee.demo.dao.DemoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 示例服务接口实现<p>
 * Service层禁止使用Controller层的特有资源，如HttpServletRequest request, HttpServletResponse response
 * </p>
 * @author Meron
 * @date 2019-12-30 16:57
 */
@Service
public class DemoApiImpl implements DemoApi {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DemoDao demoDao;
    @Autowired
    private DateService dataService;
    @Autowired
    private UserApi userApi;

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
    public void testDBModel(RequestInfo requestInfo) {
        int oper = requestInfo.getIntegerParam("oper", 1);

        log.info("testDBModel-oper"+ oper);
        switch (oper){
            case  1: {
                int rad = BaseUtils.random4();
                User user = new User();
                user.setUsername("meron"+rad);
                user.setPassword(""+rad);
                user.setMobile("1345566"+rad);
                user.setEmail("abc"+rad+"@d.com");
                user.setNickname("小米乐天"+rad);
                user.setRealname("米饭宝宝"+rad);
                this.dataService.createModel(user);
                log.info("userId"+ user.getId());
                break;
            }
            case  2: {
                User user = this.dataService.retrieveModel(User.class, 20L);
                user.setNickname("小米粒");
                user.setPassword("123456");
                this.dataService.updateModel(user);
                user = this.dataService.retrieveModel(User.class, 20L);
                log.info("user："+user);
                break;
            }
            case  3: {
                List<User> users = this.dataService.findModelByProperty(User.class, "id", Arrays.asList(1,2,3,15));
                log.info("users："+users);
                break;
            }
            case  4: {
                User user = this.dataService.findOneModelByProperty(User.class, "password", "11111");
                log.info("user："+user);
                this.dataService.deleteModel(user);
                break;
            }
            case  5: {
                User user = this.dataService.findOneModelByProperty(User.class, "password", "11111");
                log.info("user："+user);

                int ret = this.dataService.deleteModelPhysically(user);
                log.info("deleted:{}", ret);
                break;
            }
            case  6: {
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("password", "111111");
                params.put("sex", 3);
                List<User> list = this.dataService.findModelByProps(User.class, params);
                log.info("list："+list);
                break;
            }

            default:;
        }

    }

    /**
     * MyBatis数据库查询 - 2、使用公共接口引用 SQL 语句的增删改查<p>
     */
    public void testDBSql(RequestInfo requestInfo) {
        int oper = requestInfo.getIntegerParam("oper", 1);

        log.info("testDBSql-oper"+ oper);
        switch (oper){
            case  1: {
                List<User> list = new ArrayList<>();
                for(int i=1; i<5; i++){
                    int rad = BaseUtils.random4();

                    User user = new User();
                    user.setUsername("meron"+rad);
                    user.setPassword(""+rad);
                    user.setMobile("1345566"+rad);
                    user.setEmail("abc"+rad+"@d.com");
                    user.setNickname("小米乐天"+rad);
                    user.setRealname("米饭宝宝"+rad);

                    list.add(user);
                }
                this.dataService.create(DemoSqlKeyConst.createUserBatch, list);
                break;
            }
            case 2:{
                Map<String, Object> params = new HashMap<>();
                params.put("nickname", "小米");
                Pager<Map<String, Object>> pager = this.dataService.query(DemoSqlKeyConst.demoQueryCount, DemoSqlKeyConst.demoQueryList, 2, 3, params);
                log.info("pager:"+pager.getRecords());
                break;
            }
            case 3:{
                int  i = BaseUtils.random4();;
                User user = new User();
                user.setUsername("meron"+i);
                user.setPassword("11111"+i);
                user.setMobile("1345566779"+i);
                user.setEmail("abc"+i+"@d.com");
                user.setNickname("小小米"+i);
                user.setRealname("米小饭"+i);
                this.dataService.create(DemoSqlKeyConst.createUser, user);
                break;
            }
            case 4:{
                User user = this.dataService.retrieveModel(User.class, 17L);
                user.setRealname("笑傲米饭");
                this.dataService.update(DemoSqlKeyConst.updateUser, user);
                break;
            }
        }

    }

    /**
     * MyBatis数据库查询 - 2、使用公共接口引用 SQL 语句的增删改查<p>
     * 特例：使用存储过程
     * 【存储过程禁止使用，无法实现读写分离等控制】
     */
    @Deprecated
    public void testProc(RequestInfo requestInfo) {

        //参数校验
        Map<String, Object> map = new HashMap<>();
        map.put("in_str", "1, aa ,  bb, ccc ,  4");
        map.put("in_delimiter", ",");
        map.put("in_trim", 1);
        map.put("io_var", 2);
        //map.put("out_total", 0);
        //map.put("out_result", "");

        //this.dataService.runProc(DemoSqlKeyConst.testProc, map);

    }

    /**
     * MyBatis数据库查询 - 3、使用 DAO 引用 SQL 语句的增删改查<p>
     */
    public void testDBDao(RequestInfo requestInfo) {
        List<User> list = this.userApi.getUserList(5, 3);

    	this.userApi.updateNickname(17L, "小米007");

    }

    @Override
    public void bathBindUserRole(Long userId, List<Long> roleIds) {
        if(BaseUtils.isNull0(userId) || BaseUtils.isEmpty(roleIds)){
            return;
        }

        demoDao.bathBindUserRole(userId, roleIds);
    }

}
