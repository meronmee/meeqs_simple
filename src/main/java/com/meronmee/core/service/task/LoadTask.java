package com.meronmee.core.service.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meronmee.core.common.util.FileUtil;
import com.meronmee.core.common.util.PropertyHolder;
import com.meronmee.core.common.util.BaseUtils;
import com.meronmee.core.service.task.constant.TaskConstants;
import com.meronmee.core.service.task.domain.Task;
import com.meronmee.core.service.task.impl.TaskService;
import com.meronmee.core.service.task.job.CommonJob;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务加载器
 */
public class LoadTask{
    private final Logger logger = LoggerFactory.getLogger(LoadTask.class);

	@Autowired
	protected TaskService taskService;
	@Autowired
	private Scheduler scheduler;
	
	/**
	 * 系统初始加载任务
	 */
	public void loadTask(){
		if(!checkIfRunTask()){
			return;
		}
        logger.info("延时 "+ TaskConstants.loadTaskDelayMinutes+" 分钟后加载定时任务");
		
		//延时3分钟调度任务，等待系统完全启动
		ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
		timer.schedule(() -> {
			lazyLoad();
		}, TaskConstants.loadTaskDelayMinutes, TimeUnit.MINUTES);
	}
	/**
	 * 延时加载定时任务
	 */
	private void lazyLoad(){
        logger.info("加载定时任务...");
		List<Task> tasks = this.taskService.loadPeriodicTask();
		if(BaseUtils.isEmpty(tasks)) {
            logger.info("当前没有需要调度的定时任务.");
            return;
        }
			int count = 0; 
			int overtimeUnExecutedCount = 0;//已过期但未执行的任务数目
			for (Task task : tasks) {
				String tag = "["+task.getTaskName()+"]["+task.getTypeTitle()+"]";
				//任务开启状态 执行任务调度
				boolean overtimeUnExecuted = false;//是否是已过期但未执行的任务
                Integer execycle = task.getExecycle();//执行周期类型，1:自定义, 2:cron表达式, 3:定时执行一次
                try {
                    logger.info("下发定时任务"+tag+"...");

                    //1.构造任务
                    //任务执行类
                    Class jobClass = getClassByTask(task.getJobClass());//CommonJob.class
                    //任务名称
                    String jobName = "";
                    if(StringUtils.isNotBlank(task.getTaskName())){
                        jobName = task.getTaskName();
                    }else{
                        UUID uuid = UUID.randomUUID();
                        String taskName = uuid.toString() + "_" + System.currentTimeMillis();
                        jobName = taskName;
                        task.setTaskName(taskName);
                        this.taskService.update(task);
                    }
                    //任务数据
                    JobDataMap jobDataMap = getJobDataMap(task);
                    //构造任务
                    JobDetail jobDetail = JobBuilder.newJob(jobClass)
                            .withIdentity(jobName, Scheduler.DEFAULT_GROUP)
                            .usingJobData(jobDataMap)
                            .build();

                    //2.构造触发器
                    //触发器名称
                    String triggerName = getTriggerName(task.getTaskName());
                    //触发规则
                    String cron = this.taskService.getCronExpressionFromDB(task);
                    //构造触发器
                    Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity(triggerName, Scheduler.DEFAULT_GROUP)
                            .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                            .build();

                    //3.调度任务
                    scheduler.scheduleJob(jobDetail, trigger);

                    count ++;
                } catch (SchedulerException e) {
                    String errMsg = e.getMessage();
                    if(errMsg != null && errMsg.indexOf("trigger will never fire") != -1){
                        logger.debug("定时任务已过期，忽略");
                    } else {
                        logger.error("下发定时任务失败，定时任务调度出错", e);
                    }
                } catch (ClassNotFoundException e) {
                    logger.error("下发定时任务失败，没有找到对应的任务执行类"+tag, e);
                } catch (Exception e) {
                    logger.error("下发定时任务失败"+tag, e);
                }
			}//for
			
			String overtimeMsg = "";
			if(overtimeUnExecutedCount > 0){
				overtimeMsg = "，过期未执行任务数：" + overtimeUnExecutedCount;				
			}
			logger.info("加载定时任务完成，有效任务数：" + count + overtimeMsg);

	}

	private JobDataMap getJobDataMap(Task task){
		JobDataMap jdm = new JobDataMap();
		
		String taskData = task.getTaskData();
		if(StringUtils.isNotBlank(taskData)){
			JSONObject json = JSON.parseObject(taskData);
			for(Map.Entry<String, Object> entry: json.entrySet()){
				jdm.put(entry.getKey(), entry.getValue());
			}
		}
		jdm.put("taskId", task.getId());		
		jdm.put("taskName", task.getTaskName());
		jdm.put("taskAddTime", task.getCreateTime().getTime());
		jdm.put("cron", taskService.getCronExpressionFromDB(task));
		jdm.put("taskType", taskService.getTypeNameByCode(task));
		
		return jdm;
	}
	
	/**
	 * 获取 taskName 对应的 triggerName
	 * @param taskName
	 * @return
	 */
	public String getTriggerName(String taskName){
        return "CronTrigger_" + taskName;
	}
	/**
	 * 
	 * @param taskClassName 任务执行类名
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private Class getClassByTask(String taskClassName) throws ClassNotFoundException{
	    if(taskClassName.equals("com.meronmee.core.service.task.job.CommonJob")){
	        return CommonJob.class;
        } else {
            return Class.forName(taskClassName);
        }
	}
	
	/**
	 * 检查是否在当前服务器上运行定时任务
	 * @return true:运行， false:不运行
	 */
	private boolean checkIfRunTask(){
		int runtask = PropertyHolder.getInteger("runtask.on", 1);//不在本机上运行定时任务
		if(runtask == 0){
			return false;
		}
		
		String[] localIps = BaseUtils.getAllLocalHostIP();
		String allowIpStr = PropertyHolder.getString("runtask.ip");
		if(localIps == null || localIps.length==0 || StringUtils.isBlank(allowIpStr)){
			return false;
		}

		List<String> allowIpList = BaseUtils.getStrList(allowIpStr);
		boolean runFlag = false;
		for(String ip : localIps){
			for(String allowIp : allowIpList){
				if(ip.equals(allowIp) || ip.startsWith(allowIp)){
                    runFlag = true;
                    break;
				}
			}
		}

		if(runFlag) {
            String runningFilePath = PropertyHolder.getString("runtask.running.file");
            if (StringUtils.isNotBlank(runningFilePath)) {
                File runningFile = new File(runningFilePath);
                if(runningFile.exists()){
                    logger.warn("定时任务已在本机执行, runningFile:"+runningFilePath);
                    return false;
                } else {
                    try {
                        FileUtil.writeStringToFile(runningFile, "1");
                    }catch (Exception e){
                        logger.warn("保存定时任务控制文件失败, runningFile:"+runningFilePath, e);
                    }
                }
            }

            return true;
        }

		return false;
	}

}
