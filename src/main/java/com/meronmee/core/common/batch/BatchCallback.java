package com.meronmee.core.common.batch;

import java.util.List;

/**
 * 批量任务全部处理完成或终止后的回调
 */
public interface BatchCallback {
    /**
     * 批量任务全部处理完成后执行
     * @author Meron
     * @date 2020-08-29 15:47
     * @param cost 总耗时，单位:毫秒
     * @param total 处理的总记录数
     */
    public void complete(long cost, int total);

    /**
     * 处理失败后的回调
     * @author Meron
     * @date 2020-08-29 15:47
     * @param cost 至失败时的总耗时，单位:毫秒
     * @param total 至失败时的处理的总记录数
     * @param e 异常
     */
    public void fail(long cost, int total, Throwable e);
}