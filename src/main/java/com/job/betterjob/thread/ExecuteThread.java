package com.job.betterjob.thread;

import com.job.betterjob.command.JobCommand;
import com.job.betterjob.model.JobResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author songle
 * @create 2022-05-23 15:30
 * @descreption 任务执行线程
 */

@Slf4j
public class ExecuteThread extends Thread{

    private JobCommand jobCommand;

    public ExecuteThread(JobCommand jobCommand){
         this.jobCommand = jobCommand;
    }


    @Override
    public void run(){
        String jobName = jobCommand.getJobInfo().getName();
        log.info("********【{}】定时任务开始执行*********",jobName);
        try {
            JobResult execute = jobCommand.execute();
            log.info("********【{}】定时任务执行完成,执行结果:{}*********",jobName,execute.toString());
        } catch (Exception e) {
            log.error("【{}】定时任务执行异常:{}",jobName,e);
        }
    }

}
