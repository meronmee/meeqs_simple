package com.meronmee.core.service.task.job;

import com.alibaba.fastjson.JSONObject;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 通用任务类
 * @author Meron
 */
public class CommonJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(CommonJob.class);

    /**
	 * 执行任务
	 */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			SchedulerContext schCtx = context.getScheduler().getContext();
			//获取Spring中的上下文
			ApplicationContext appCtx = (ApplicationContext)schCtx.get("applicationContext");

			//任务参数
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
            JSONObject jobData = new JSONObject();
            jobData.putAll(jobDataMap.getWrappedMap());

            CommonJobHandler commonJobHandler = (CommonJobHandler)appCtx.getBean("commonJobHandler");
            if(commonJobHandler != null){
                commonJobHandler.executeInternal(jobData);
            } else {
                logger.error("执行定时任务失败，未能加载到 CommonJobHandler Bean实例， 任务参数：" + jobData);
            }
        } catch (Throwable e) {
			logger.error("执行定时任务出错，" + e.getMessage(), e);
		}
	}

}
