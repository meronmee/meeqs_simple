package com.meronmee.core.common.batch;

import com.meronmee.core.common.util.FileUtil;
import com.meronmee.core.common.util.BaseUtils;
import com.meronmee.core.service.database.constant.DbConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 批量处理服务类。通过任务文件来下发任务
 * @author Meron
 * @date 2020-08-29 15:27
 */
@Service
public class BatchService {
    private final Logger log = LoggerFactory.getLogger(BatchService.class);

    public void run(BatchBuilder builder){
        long startTime = System.currentTimeMillis();
        int total = 0;//总数据量
        try {
            if(builder == null){
                return;
            }
            log.info(builder.getAction() + "开始...");
            if(builder.getTaskData() == null){
                log.warn(builder.getAction() + "处理结束，没有有效的任务数据");
                return;
            }
            if(builder.getTask() == null){
                log.warn(builder.getAction() + "处理结束，没有有效的任务处理器");
                return;
            }

            //初始化线程池
            ExecutorService threadPool = Executors.newFixedThreadPool(builder.getPoolSize());

            //读取每页数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(builder.getTaskData()));
            int pageNo = 1; //页码
            int lineCountOfPage = 0;//页内行数
            String line = "";
            List<String> dataOfPage = new ArrayList<>();
            while((line = reader.readLine()) != null){
                if(BaseUtils.isBlank(line)){
                    continue;
                }
                line = line.trim();
                if(BaseUtils.isBlank(line)){
                    continue;
                }

                total ++;
                lineCountOfPage ++;
                dataOfPage.add(line);

                //已读取一整页的数据
                if(lineCountOfPage > 0  && (lineCountOfPage % builder.getPageSize() == 0)){
                    executePageTask(threadPool, dataOfPage, builder, pageNo);

                    //重置请求数据
                    dataOfPage = new ArrayList<>();
                    lineCountOfPage = 0;
                    pageNo ++;
                }
            }//读取完所有数据

            //最后还剩余一些不足一页的数据，补充一次
            if(lineCountOfPage > 0){
                log.info(builder.getAction() + "处理最后一页的剩余数据");
                executePageTask(threadPool, dataOfPage, builder, pageNo);
            }

            //关闭任务数据流
            FileUtil.closeStream(builder.getTaskData());

            //关闭线程池
            threadPool.shutdown();

            //等待所有线程处理结束
            while (true) {
                if (threadPool.isTerminated()) {
                    long endTime = System.currentTimeMillis();
                    long cost = endTime - startTime;
                    long minutes = cost / 60000;
                    log.info(builder.getAction() + "所有子线程都处理结束，总数据量 "+total+"， 用时 "+minutes+" 分钟");

                    //回调
                    if(builder.getCallback() != null){
                        builder.getCallback().complete(cost, total);
                    }

                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }catch (Throwable e){
            log.error(builder.getAction() + "处理出错", e);
            //回调
            if(builder.getCallback() != null){
                long endTime = System.currentTimeMillis();
                long cost = endTime - startTime;
                builder.getCallback().fail(cost, total, e);
            }
        }
    }//run

    /**
     * 执行一页数据处理任务
     * @author Meron
     * @date 2020-08-29 16:55
     * @param threadPool
     * @param dataOfPage
     * @param builder
     * @param pageNo
     * @return void
     */
    @Transactional(value= DbConst.MYBATIS_TRANSACTION_MANAGER, propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
    private void executePageTask(ExecutorService threadPool, List<String> dataOfPage, BatchBuilder builder, int pageNo){
        final String logKey = builder.getAction() + "分批执行, pageNo="+pageNo;
        if(BaseUtils.isEmpty(dataOfPage)){
            log.info(logKey + ", 无有效的分页任务数据");
            return;
        }
        threadPool.execute(() -> {
            try {
                log.info(logKey);

                builder.getTask().execute(dataOfPage, pageNo);

                log.info(logKey + ", 处理完成");
            } catch (Exception e) {
                log.error(logKey + ", dataOfPage="+ StringUtils.join(dataOfPage, ",") +", 出错", e);
            }
        });
    }
}
