package com.meronmee.core.service.task.impl;

import com.alibaba.fastjson.JSONObject;
import com.meronmee.base.api.ConfigApi;
import com.meronmee.core.common.util.*;
import com.meronmee.core.service.database.DataService;
import com.meronmee.core.service.redis.RedisApi;
import com.meronmee.core.service.task.constant.TaskConstants;
import com.meronmee.core.service.task.domain.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务服务类
 * @author Meron
 *
 */
@Service
public class TaskService{
	protected static final Log log = LogFactory.getLog(TaskService.class);
    @Autowired
	protected DataService dataService;
    @Autowired
	private ConfigApi configApi;
	@Autowired
    private RedisApi redisApi;

	/**
     * 加载周期性定时任务
     * @author Meron
     * @date 2020-09-04 09:54
     * @param
     * @return java.util.List<com.meronmee.core.service.task.domain.Task>
     */
    public List<Task> loadPeriodicTask(){
        Map<String, Object> params = new LinkMap<String, Object>()
                .append("deleteStatus", 0)
                .append("execycle", TaskConstants.EXECYCLE_PERIODIC);

        List<Task> list = this.dataService.findByProps(Task.class, params);
        return list;
    }

	public void create(Task task){
		this.dataService.create(task);
	}
	 public Task retrieve(Long taskId){
		return this.dataService.retrieve(Task.class, taskId);
	}
	public Task getByName(String  taskName){
		if(StringUtils.isBlank(taskName)){
			return null;
		}
        Task task = this.dataService.findOneByProperty(Task.class, "taskName", taskName);
		return task;
	}
	
	public void update(Task task){
        this.dataService.update(task);
    }
    public void updatePart(Task task, String propNames){
        this.dataService.updatePart(task, propNames);
    }

	public void update(List<Task> tasks){
		if(tasks == null || tasks.isEmpty()){
			return;
		}
		for(Task task : tasks) {
            this.dataService.update(task);
        }
	}
	
	public void delete(Task task){
		this.dataService.delete(task);
	}
	
	public String getCronExpressionFromDB(Long taskId) {
		Task task = this.retrieve(taskId);
		return this.getCronExpressionFromDB(task);
	}

	public String getCronExpressionFromDB(Task task) {
		if (task.getExecycle().equals(TaskConstants.EXECYCLE_PERIODIC)) {//周期性任务
			return task.getCronExpression();
		} else {//定时执行一次
			return DateUtils.date2Cron(task.getRunTime());
		}
	}

    /**
     * 从 typeCode 解析 TaskConstants.TaskType
     * @return TaskConstants.TaskType.name()
     */
    public String getTypeNameByCode(Task task){
        return TaskConstants.TaskType.getTypeByCode(task.getTypeCode()).name();
    }

    /**
     * 执行通用Api调用任务
     */
    public void runCommonApiTask(JSONObject jobDataMap){
        if(BaseUtils.isEmpty(jobDataMap)){
            return;
        }
        String reqId = BaseUtils.uuidShort();
        String taskName = BaseUtils.toString(jobDataMap.get("taskName"));
        String taskTitle = BaseUtils.toString(jobDataMap.get("taskTitle"));
        log.info("开始执行通用Api调用任务["+reqId+"]: taskName:"+taskName+", taskTitle:"+taskTitle+", jobDataMap:"+jobDataMap);

        try {
            String url = BaseUtils.toString(jobDataMap.get("url"));
            //数据传输模式，form：表单形式提交，body：已请求包体传输
            String mode = BaseUtils.toString(jobDataMap.get("mode"), "form");
            Assert.isNotBlank(url, "API接口路径不能为空");
            String reqParamStr = BaseUtils.toString(jobDataMap.get("params"));

            String response = null;
            if("body".equals(mode)){//以包体的形式传输
                response = HttpUtils.postAsString(url, reqParamStr);
            } else {
                JSONObject reqParams = BaseUtils.toJson(reqParamStr);
                Map<String, String> data = new HashMap<>();
                for(String key : reqParams.keySet()){
                    data.put(key, BaseUtils.toString(reqParams.get(key)));
                }
                response = HttpUtils.post(url, data);
            }
            log.info("执行通用Api调用任务完成["+reqId+"]: taskName:"+taskName+", taskTitle:"+taskTitle
                    +", jobDataMap:"+jobDataMap+", response:"+response);
        } catch (Exception e) {
            log.error("执行通用Api调用任务["+reqId+"]失败", e);
        }
    }

}
