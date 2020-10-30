package com.meronmee.demo.controller;

import com.meronmee.base.api.ConfigApi;
import com.meronmee.base.api.UserApi;
import com.meronmee.base.domain.User;
import com.meronmee.core.common.batch.BatchBuilder;
import com.meronmee.core.common.batch.BatchPageTask;
import com.meronmee.core.common.batch.BatchService;
import com.meronmee.core.common.util.*;
import com.meronmee.core.service.redis.RedisApi;
import com.meronmee.core.web.dto.AuthError;
import com.meronmee.core.web.dto.JsonResult;
import com.meronmee.core.web.dto.Success;
import com.meronmee.core.web.util.RequestInfoUtils;
import com.meronmee.core.web.util.RequestUtils;
import com.meronmee.core.web.util.ResponseUtils;
import com.meronmee.demo.api.DemoApi;
import com.meronmee.demo.test.Human;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
 

/**
 * 调试
 * @author Meron
 *
 */
@Controller
public class TestAction {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
		
    @Resource(name="man")  
    private Human human; 

    @Autowired
    private BatchService batchService;
    @Autowired
    private RedisApi redisApi;
    @Autowired
    private UserApi userApi;
    @Autowired
    private ConfigApi configApi;
    @Autowired
    private DemoApi demoApi;
     
    
	/**
	 * 页面请求
	 */
    @RequestMapping(value = "/demo/test/test.htm",  method = RequestMethod.GET)
    public String testPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {   
        String msg = RequestUtils.getStringParam(request, "msg");
        Assert.isNotBlank(msg, "msg不能为空");
      
        if(StringUtils.isBlank(msg)){
            viewData.put("msg", "redirect");
        	return "redirect:/demo/test/error.htm";        	
        }
        
        viewData.put("errMsg", msg);
        return "app/error";
    }

    /**
	 * Ajax请求
	 */ 
    @RequestMapping(value = "/demo/test/test.json")
    public JsonResult testAjax(HttpServletRequest request, HttpServletResponse response) {
        User user = userApi.getCurrentUser(RequestInfoUtils.getRequestInfo(request));
        if(user == null){
            return new AuthError();
        }
    	String oper = RequestUtils.getStringParam(request, "oper", "get");
    	
    	boolean result= "get".equals(oper) && getUser(6L)!=null;

		return new Success(result); 
    }

    /**
     * 操控redis
     */
    @RequestMapping(value = "/sys/config.json")
    public JsonResult config(HttpServletRequest request, HttpServletResponse response) {
        String oper = RequestUtils.getStringParam(request, "oper", "get");
        String key = RequestUtils.getStringParam(request, "key");
        String value = RequestUtils.getStringParam(request, "value");

        if("get".equals(oper)){
            Assert.isNotBlank(key, "key 不能为空");
            return new Success(new LinkMap("value", this.configApi.get(key)));
        } else if("set".equals(oper)){
            Assert.isNotBlank(key, "key 不能为空");
            Assert.isNotBlank(value, "value 不能为空");

            this.configApi.set(key, value);
            return new Success(new LinkMap("value", this.configApi.get(key)));
        } else if("reload".equals(oper)){
            this.configApi.clearCache();
            return new Success();
        }

        return new Success();
    }

    /**
	 * 操控redis
	 */
	@RequestMapping(value = "/sys/redis.json") 
	public JsonResult redis(HttpServletRequest request, HttpServletResponse response) { 
		String oper = RequestUtils.getStringParam(request, "oper", "get");
		String key = RequestUtils.getStringParam(request, "key");
		String value = RequestUtils.getStringParam(request, "value"); 

		if("get".equals(oper)){
			Assert.isNotBlank(key, "key 不能为空"); 
		} else if("set".equals(oper)){
			Assert.isNotBlank(key, "key 不能为空");
			//Assert.isNotBlank(value, "value 不能为空"); 	
			User user = this.userApi.retrieve(6L);
			
			User userInfo = user; 
			//Map<String,Object> userInfo = user.toMap();
			//String userInfo = JSON.toJSONString(user); 
			this.redisApi.setObj(key, userInfo, 8); 			
			//this.redisApi.set(key, userInfo, 8); 
		} else if("delete".equals(oper)){
			Assert.isNotBlank(key, "key 不能为空");
			this.redisApi.delete(key); 
		} 
		
		User user = this.redisApi.getObj(key);
		//Map<String, Object> user = this.redisApi.getObj(key);
		//String user = this.redisApi.getObj(key);
		//String user = this.redisApi.get(key);
		return new Success(new LinkMap("value", user)); 
	}

    /**
     * 测试
     * @return
     */
    @RequestMapping(value = "/demo/lock/redis.json")
    public void testLock0(HttpServletRequest request, HttpServletResponse response) {
        try{
            String oper = RequestUtils.getStringParam(request, "oper");
            String key = RequestUtils.getStringParam(request, "key");
            String reqid = RequestUtils.getStringParam(request, "reqid");
            Assert.isNotBlank(oper, "操作不能为空");
            Assert.isNotBlank(key, "锁key不能为空");
            Assert.isNotBlank(reqid, "请求ID不能为空");
            boolean result = false;

            //快速失败的锁：如果锁被别人占用，则立即返回失败
            if(oper.equals("lock")) {
                int expire = RequestUtils.getIntegerParam(request, "expire", 10);
                result = this.redisApi.getLock(key, reqid, expire);
            } else {
                result = this.redisApi.releaseLock(key, reqid);
            }

            if (result) {
                ResponseUtils.renderJson(response, BaseUtils.success("OK"));
            } else {
                ResponseUtils.renderJson(response, BaseUtils.success("Failed"));
            }
        } catch (IllegalArgumentException e){
            ResponseUtils.renderJson(response, BaseUtils.error(e.getMessage()));
            return;
        } catch(Exception e){
            log.error("操作失败, 原因:"+e.getMessage(), e);
            ResponseUtils.renderJson(response, BaseUtils.error(500));
        }
        return;
    }

    
    /**
	 * 操控redis
	 */
	@RequestMapping(value = "/sys/cache.json") 
	public JsonResult cache(HttpServletRequest request, HttpServletResponse response) { 
		String oper = RequestUtils.getStringParam(request, "oper", "get");
		String key = RequestUtils.getStringParam(request, "key");
		String value = RequestUtils.getStringParam(request, "value"); 

		if("get".equals(oper)){
			Assert.isNotBlank(key, "key 不能为空"); 

			return new Success(new LinkMap("Cache", Cache.get(key)).append("ShortCache", ShortCache.get(key))); 
		} else if("set".equals(oper)){
			Assert.isNotBlank(key, "key 不能为空"); 
			//User user = this.service.retrieveModel(User.class, 6L);
			User user = getUser(6L);
			
			Cache.set(key, user);
			ShortCache.set(key, user);		
			return new Success(new LinkMap("Cache", Cache.get(key)).append("ShortCache", ShortCache.get(key))); 	 
		} else if("delete".equals(oper)){
			Assert.isNotBlank(key, "key 不能为空");
			
			Cache.delete(key);
			ShortCache.delete(key);
			return new Success(new LinkMap("Cache", Cache.get(key)).append("ShortCache", ShortCache.get(key))); 
		} 
		 
		return new Success(); 
	}
	
    private User getUser(final Long id){
    	log.info("getUser......");
        Object cache = Cache.get("User_"+id, key -> {
        	log.info("getCacheUser in function");
            return this.userApi.retrieve(id);
        });
        if(cache != null){
            return (User)cache;
        } else {
            return null;
        }
    }
    
	/**
	 * 操控redis
	 */
	@RequestMapping(value = "/test/zset.json") 
	public JsonResult testRedis(HttpServletRequest request, HttpServletResponse response) {
		String key = RequestUtils.getStringParam(request, "key", "test1");		
		int duration = RequestUtils.getIntegerParam(request, "duration", 300);
		
		String oper = RequestUtils.getStringParam(request, "oper", "get");//set,get,mix
		final int num = RequestUtils.getIntegerParam(request, "num", 1000);

		
		final long now = System.currentTimeMillis();
		final long end = now + duration*1000;
				
		if("set".equals(oper)){
			for(int i=0; i<10; i++){
				new Thread(new Runnable(){					 
				   public void run(){					 
					   for(int j=0; j<num/10; j++){
						   long score = BaseUtils.randomAB(now, end);
						   log.info("zAdd - key={}, value={}, score={}", key, score, score);
						   redisApi.zAdd(key, String.valueOf(score), BaseUtils.toDouble(score));
					   }			 
				   }					 
				}).start();
			}
		} else if("get".equals(oper)){
			for(int i=0; i<10; i++){
				new Thread(new Runnable(){					 
				   public void run(){					 
					   for(int j=0; j<num/10; j++){
						   double min = BaseUtils.toDouble(now);
						   double max = BaseUtils.toDouble(end);
						   log.info("zRange - key={}, min={}, max={}, result:{}", key, min, max, TestAction.toString(redisApi.zRangeByScoreWithScores(key, min, max)));
						   ;
					   }			 
				   }					 
				}).start();
			}
		} else if("mix".equals(oper)){
			for(int i=0; i<10; i++){
				new Thread(new Runnable(){					 
				   public void run(){					 
					   for(int j=0; j<num/10; j++){
						   int value = j%2;
						   if(value==0){
							   long score = BaseUtils.randomAB(now, end);
							   //log.info("zAdd - key={}, value={}, score={}", key, score, score);
							   redisApi.zAdd(key, String.valueOf(score), BaseUtils.toDouble(score));
						   } else {
							   double min = BaseUtils.toDouble(now);
							   double max = BaseUtils.toDouble(end);
							   log.info("zRange - key={}, min={}, max={}, result:{}", key, min, max, TestAction.toString(redisApi.zRangeByScoreWithScores(key, min, max)));
							   ;
						   }
					   }			 
				   }					 
				}).start();
			}
		}  
		return new Success(); 
	}
	private static String toString(Set<TypedTuple<String>> set){
		StringBuilder sb = new StringBuilder();
		if(set == null || set.size()==0){
			return "";
		}
		for(TypedTuple<String> item : set){
			sb.append("[").append(item.getScore()).append("#").append(item.getValue()).append("],");
		}
		return sb.toString();
		
	}
	
	
	/**
	 * 操控redis
	 */
	@RequestMapping(value = "/demo/insert.json") 
	public JsonResult insert(HttpServletRequest request, HttpServletResponse response) { 
		Long userId = RequestUtils.getLongParam(request, "userId");
		String roles = RequestUtils.getStringParam(request, "roles"); 
		
		List<Long> roleIds = BaseUtils.getLongList(roles);

        demoApi.bathBindUserRole(userId, roleIds);
		
		return new Success(); 
	}



    /**
     * 操控redis
     */
    @RequestMapping(value = "/demo/testTruncate.json")
    public JsonResult testTruncate(HttpServletRequest request, HttpServletResponse response) {
        demoApi.truncateTemp();
        return new Success();
    }


    /**
     * 测试通用批量任务，通过任务文件来下发任务
     * @param data 任务文件
     * @author mizr 2020-08-29
     */
    @RequestMapping(value = "/app/test/batch.json")
    public void testBatch(HttpServletRequest request, HttpServletResponse response) {
        try {
            MultipartFile file = RequestUtils.getFileParam(request, "data");
            Assert.isNotNull(file, "请上传任务数据");

            ResponseUtils.renderJson(response, BaseUtils.success());

            this.batchService.run(new BatchBuilder()
                    .setAction("测试批量任务")
                    .setPageSize(10)
                    .setPoolSize(5)
                    .setTaskData(file.getInputStream())
                    .setTask(new BatchPageTask() {//每页的任务处理，只需要new 一个 BatchPageTask 即可，核心逻辑建议在execute中调用一个service方法来处理
                        @Override
                        public void execute(List<String> pageData, int pageNo) {
                            log.info("分页任务数据：" + StringUtils.join(pageData, ","));
                            //核心逻辑最好放到一个service方法中处理，此处只是演示
                            for(String line : pageData){
                                log.info(getUser(BaseUtils.toLong(line)).getNickname());
                            }
                        }
                    })
            );//run

        } catch (IllegalArgumentException e) {
            ResponseUtils.renderJson(response, BaseUtils.error(e.getMessage()));
        } catch (Exception e) {
            log.error("操作失败, 原因:" + e.getMessage(), e);
            ResponseUtils.renderJson(response, BaseUtils.error(JsonResult.Code.EXCEPTION.getCode(), "操作失败"));
        }
    }
}
