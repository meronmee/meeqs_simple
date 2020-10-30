package com.meronmee.core.common.batch;

import java.io.InputStream;

/**
 * 批量任务构造器
 */
public class BatchBuilder{
    /**
     * 线程池大小, 1~50
     */
    private int poolSize = 10;
    /**
     * 分页处理每页数据大小
     */
    private int pageSize = 100;
    /**
     * 操作名称
     */
    private String action;
    /**
     * 任务文件数据流
     */
    private InputStream taskData;
    /**
     * 每页任务的处理器
     */
    private BatchPageTask task;

    /**
     * 批量任务全部处理完成或终止后的回调
     */
    private BatchCallback callback;

    //-----------------------------

    /** 线程池大小, 1~50*/
    public int getPoolSize() {
        if(poolSize <= 0){
            poolSize = 1;
        } else if(poolSize > 50){
            poolSize = 50;
        }
        return this.poolSize;
    }

    /** 线程池大小, 1~50 */
    public BatchBuilder setPoolSize(int poolSize) {
        if(poolSize <= 0){
            poolSize = 1;
        } else if(poolSize > 50){
            poolSize = 50;
        }
        this.poolSize = poolSize;
        return this;
    }

    /** 分页处理每页数据大小 */
    public int getPageSize() {
        if(pageSize <= 0){
            pageSize = 100;
        }
        return this.pageSize;
    }

    /** 分页处理每页数据大小 */
    public BatchBuilder setPageSize(int pageSize) {
        if(pageSize <= 0){
            pageSize = 100;
        }
        this.pageSize = pageSize;
        return this;
    }

    /** 操作名称 */
    public String getAction() {
        return this.action;
    }

    /** 操作名称 */
    public BatchBuilder setAction(String action) {
        this.action = action;
        return this;
    }


    /** 每页任务的处理器 */
    public BatchPageTask getTask() {
        return this.task;
    }

    /** 每页任务的处理器 */
    public BatchBuilder setTask(BatchPageTask task) {
        this.task = task;
        return this;
    }

    /** 任务文件数据流 */
    public InputStream getTaskData() {
        return this.taskData;
    }

    /** 任务文件数据流 */
    public BatchBuilder setTaskData(InputStream taskData) {
        this.taskData = taskData;
        return this;
    }


    /** 批量任务全部处理完成或终止后的回调 */
    public BatchCallback getCallback() {
        return this.callback;
    }

    /** 批量任务全部处理完成或终止后的回调 */
    public BatchBuilder setCallback(BatchCallback callback) {
        this.callback = callback;
        return this;
    }
}
