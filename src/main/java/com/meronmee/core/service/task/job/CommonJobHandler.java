package com.meronmee.core.service.task.job;

import com.meronmee.core.common.util.PropertyHolder;
import com.meronmee.core.common.util.BaseUtils;
import com.meronmee.core.service.task.constant.TaskConstants;
import com.meronmee.core.service.task.domain.Task;
import com.meronmee.core.service.task.impl.TaskService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务处理器
 * @author mizr 2017-06-08 16:47:24
 */
@Component
public class CommonJobHandler {
    private final Logger logger = LoggerFactory.getLogger(CommonJobHandler.class);
    @Autowired
    protected TaskService taskService;


	/**
     *
     * @author Meron
     * @date 2020-09-02 21:31
     * @param params
     * @return
     */
	public boolean execute(String... params) throws Exception {
		String param = null;//最多只有一个参数
		if(params!=null && params.length>0){
			param = params[0];
		}
		logger.info("收到任务执行请求，执行参数：" + param);
		JSONObject jobDataMap = BaseUtils.toJson(param);
		boolean result = false;
		try {
			result = this.executeInternal(jobDataMap);
		}catch (Exception e){
            logger.error("执行定时任务失败，执行参数：" + param, e);
		}

		return result;
	}

	/**
	 * 执行任务
	 */
    public boolean executeInternal(JSONObject jobDataMap) throws Exception {
		try {
			//基本任务参数
			String qmqMessageId = jobDataMap.getString("qmqMessageId");
			Long taskId = BaseUtils.toLong(jobDataMap.get("taskId"));
			String taskName = BaseUtils.toString(jobDataMap.get("taskName"));
			String taskTitle = BaseUtils.toString(jobDataMap.get("taskTitle"));
			String taskTypeName = BaseUtils.toString(jobDataMap.get("taskType"), "other");
			logger.info(BaseUtils.format("开始执行定时任务["+qmqMessageId+"]: taskType:{0}, taskName:{1}, taskTitle:{2}", taskTypeName, taskName, taskTitle));

            //更新本地任务执行状态
            try{
                Task task = null;
                if(BaseUtils.isNotNull0(taskId)){
                    task = this.taskService.retrieve(taskId);
                }
                if(task==null && BaseUtils.isNotBlank(taskName)){
                    task = this.taskService.getByName(taskName);
                }

                if(task != null){
                    task.setLastExecuteTime(new Date());
                    task.setExecuteCount(task.getExecuteCount() + 1);
                    this.taskService.updatePart(task, "lastExecuteTime,executeCount");
                } else {
                    logger.warn("更新本地任务执行状态失败["+qmqMessageId+"]: 没有找到相关的任务-taskName="+taskName+", taskId="+taskId);
                }
            }catch(Exception e){
                logger.warn("更新本地任务执行状态出错["+qmqMessageId+"]-taskName="+taskName+", taskId="+taskId, e);
            }

			//任务类型
			TaskConstants.TaskType taskType = TaskConstants.TaskType.getTypeByName(taskTypeName);
			//特定任务参数
			switch(taskType) {
                case common_api://通用API接口型任务
                {
                    taskService.runCommonApiTask(jobDataMap);
                }
                break;
				default:
					if(!PropertyHolder.getBoolean("test.task.result", true)){
						logger.info("run job failed!");
						throw new Exception("run job failed!");
					} else {
						logger.info("run job success!");
					}
			}
			return true;
		} catch(Exception e) {
			logger.error("执行定时任务出错", e);
			throw e;
		}
	}
}
