package com.meronmee.core.common.batch;

import java.util.List;

/**
 * 批量任务每页任务的处理器
 */
public interface BatchPageTask{
    /**
     * 执行每页任务
     * @author Meron
     * @date 2020-08-29 15:47
     * @param pageData 一页的数据
     * @param pageNo 页码
     */
    public void execute(List<String> pageData, int pageNo);
}