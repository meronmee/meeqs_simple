package com.meronmee.core.service.task.domain;

import com.meronmee.core.api.annotation.Table;
import com.meronmee.core.api.domain.Model;

import java.util.Date;

/**
 * 定时任务
 */
@Table("task")
public class Task extends Model {


    /** 任务名称，用于Quartz,如：clean_overdue_task_1436176814787 */
    private String taskName;

    /** 任务类型代码，如：7,和TaskConstants.TaskType#getTypeCode()一至 */
    private Integer typeCode;

    /** 任务类型标题，如：清理过期定时任务 */
    private String typeTitle;

    /** 任务执行类，如：com.meronmee.core.service.task.job.CommonJob */
    private String jobClass;

    /** 执行周期类型，1:周期执行, 2:执行一次 */
    private Integer execycle;

    /** CRON表达式 */
    private String cronExpression;

    /** 是否启用 */
    private Integer enable;

    /** 任务说明 */
    private String remark;

    /** 一次性定时任务的执行时间 */
    private Date runTime;

    /** 扩展数据，JSON格式字符串 */
    private String taskData;

    /** 执行次数 */
    private Integer executeCount;

    /** 上一次执行时间 */
    private Date lastExecuteTime;

    private String messageId;

    /*------ Getter/Setter ------*/
    
    /** 任务名称，用于Quartz,如：clean_overdue_task_1436176814787 */
    public String getTaskName() {
      return taskName;
    }
    /** 任务名称，用于Quartz,如：clean_overdue_task_1436176814787 */
    public void setTaskName(String taskName) {
      this.taskName = taskName;
    }
    
    /** 任务类型代码，如：7,和TaskConstants.TaskType#getTypeCode()一至 */
    public Integer getTypeCode() {
      return typeCode;
    }
    /** 任务类型代码，如：7,和TaskConstants.TaskType#getTypeCode()一至 */
    public void setTypeCode(Integer typeCode) {
      this.typeCode = typeCode;
    }
    
    /** 任务类型标题，如：清理过期定时任务 */
    public String getTypeTitle() {
      return typeTitle;
    }
    /** 任务类型标题，如：清理过期定时任务 */
    public void setTypeTitle(String typeTitle) {
      this.typeTitle = typeTitle;
    }
    
    /** 任务执行类 */
    public String getJobClass() {
      return jobClass;
    }
    /** 任务执行类 */
    public void setJobClass(String jobClass) {
      this.jobClass = jobClass;
    }
    
    /** 执行周期类型，1:自定义, 2:cron表达式, 3:定时执行一次 */
    public Integer getExecycle() {
      return execycle;
    }
    /** 执行周期类型，1:自定义, 2:cron表达式, 3:定时执行一次 */
    public void setExecycle(Integer execycle) {
      this.execycle = execycle;
    }


    /** CRON表达式 */
    public String getCronExpression() {
        return this.cronExpression;
    }

    /** CRON表达式 */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /** 是否启用 */
    public Integer getEnable() {
        return this.enable;
    }

    /** 是否启用 */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    /** 任务说明 */
    public String getRemark() {
        return this.remark;
    }

    /** 任务说明 */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /** 一次性定时任务的执行时间 */
    public Date getRunTime() {
        return this.runTime;
    }

    /** 一次性定时任务的执行时间 */
    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }

    /** 扩展数据，JSON格式字符串 */
    public String getTaskData() {
        return this.taskData;
    }

    /** 扩展数据，JSON格式字符串 */
    public void setTaskData(String taskData) {
        this.taskData = taskData;
    }

    /** 执行次数 */
    public Integer getExecuteCount() {
        return this.executeCount;
    }

    /** 执行次数 */
    public void setExecuteCount(Integer executeCount) {
        this.executeCount = executeCount;
    }

    /** 上一次执行时间 */
    public Date getLastExecuteTime() {
        return this.lastExecuteTime;
    }

    /** 上一次执行时间 */
    public void setLastExecuteTime(Date lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    /**  */
    public String getMessageId() {
        return this.messageId;
    }

    /**  */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
