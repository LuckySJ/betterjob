package com.job.betterjob.thread;


import com.job.betterjob.constant.JobStatus;
import com.job.betterjob.handler.CommandHandler;
import com.job.betterjob.handler.ExecutorHandler;
import com.job.betterjob.model.JobInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author songle
 * @create 2022-05-23 14:40
 * @descreption  任务调度线程
 */


@Slf4j
public class ScheduledThread extends Thread {

    private  ExecutorHandler executorHandler;

    private volatile boolean toStop = false;

    public ScheduledThread(ExecutorHandler executorHandler) {
        this.executorHandler = executorHandler;
    }


    @Override
    public void run(){
        Thread.currentThread().setName("better-job-scheduled-thread");
        while(!toStop) {
            try {
                Date now = new Date();
                List<JobInfo> allJobInfo = executorHandler.getAllJobInfo();
                if (allJobInfo != null && allJobInfo.size() > 0) {
                    allJobInfo.forEach(t ->{
                        if (t.getStatus() == JobStatus.ENABLED && now.getTime() > t.getNextExcuteTime().getTime()) {
                            // 调用任务执行处理器执行任务
                            executorHandler.executeJob(t);
                        }
                    });
                }
            }catch (Exception e){
                log.error("定时任务调度异常:{}",e);
            }
        }
    }
}
