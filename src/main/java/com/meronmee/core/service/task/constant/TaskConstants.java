package com.meronmee.core.service.task.constant;

import com.meronmee.core.common.util.BaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 定时任务常量
 * @author Meron
 *
 */
public class TaskConstants {
    /** 加载定时任务在启动后多少分钟后执行 */
    public static final int loadTaskDelayMinutes = 2;
    /** 过期未执行的定时任务在启动后多少分钟后执行 */
    public static final int overdueTaskDelayMinutes = 2;


	//任务执行周期类型
	//--------------------
    /**
     * 任务执行周期类型-cron表达式
     */
    public static final int EXECYCLE_PERIODIC=1;
	/**
	 * 任务执行周期类型-定时执行一次
	 */
	public static final int EXECYCLE_ONCE=2;


	/**
	 * 定时任务类型
	 * @author Meron
	 */
	public static enum TaskType {
		/**通用API接口型任务，触发后会调用相关接口*/
		common_api("通用API接口型任务",1),

		/**其他-0*/
		other("其他", 0)
		;
		
		private String typeTitle;
		private Integer typeCode;
		
		// 构造函数，枚举类型只能为私有
	    private TaskType(String typeTitle, Integer typeCode) {
	        this.typeTitle = typeTitle;
	        this.typeCode = typeCode;
	    }
	    
	    /**
	     * TaskType的描述，如："清理过期定时任务"
	     * @return
	     */
	    public String getTypeTitle(){
	    	return this.typeTitle;
	    }
	    /**
	     * @return 如：7
	     */
	    public Integer getTypeCode(){
	    	return this.typeCode;
	    }
        /**
	     * @return 如：clean_overdue_task
	     */
	    public String getName(){
	    	return this.name();
	    }

	    /** 
	     * 生成TaskType对应的任务名称
	     * @return 如：clean_overdue_task_140394586214_192563
	     */
	    public String getTaskName(){
	    	return BaseUtils.join(this.name(), "_", System.currentTimeMillis(), "_", BaseUtils.random6());
	    }
	    
	    /**
	     * 获取所有的枚举值信息
	     * @return [{"name":"clean_overdue_task", "desc":"清理过期定时任务", "value":7}, ...]
	     */
	    public static List<Map<String, Object>> getAllTypes(){
	    	List<Map<String, Object>> allTypes = new ArrayList<Map<String, Object>>();
	    	for(TaskType taskType : TaskType.values()){
	    		Map<String, Object> map = new HashMap<String, Object>();
	    		map.put("name", taskType.getName());
	    		map.put("desc", taskType.getTypeTitle());
	    		map.put("value", taskType.getTypeCode());
		    	allTypes.add(map);
	    	}
	    	
	    	return allTypes;
	    }
	    /**
	     * 根据 typeCode 获得 TaskType
	     * @param typeCode - 如：7
	     */
	    public static TaskType getTypeByCode(int typeCode){
	    	for(TaskType taskType : TaskType.values()){
	    		if(taskType.getTypeCode() == typeCode){
	    			return taskType;
	    		}
	    	}
	    	
	    	return TaskType.other;
	    }
	    /**
	     * 根据 typeName 获得 TaskType
	     * @param typeName - 如：clean_overdue_task
	     */
	    public static TaskType getTypeByName(String typeName){
	    	if(BaseUtils.isBlank(typeName)){
	    		return TaskType.other;
	    	}
	    	try {
				return Enum.valueOf(TaskConstants.TaskType.class, typeName);
			} catch (Exception e) {
				return TaskType.other;
			}
	    }
	}
}