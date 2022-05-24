package com.job.betterjob.thread;

import com.alibaba.fastjson.JSONObject;
import com.job.betterjob.command.JobCommand;
import com.job.betterjob.constant.JobRedisKey;
import com.job.betterjob.handler.JedisHandler;
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

    private JedisHandler jedisHandler;

    public ExecuteThread(JobCommand jobCommand,JedisHandler jedisHandler){
         this.jobCommand = jobCommand;
         this.jedisHandler = jedisHandler;
    }


    @Override
    public void run(){
        String jobName = jobCommand.getJobInfo().getName();
        log.info("********【{}】定时任务开始执行*********",jobName);
        JobResult result = null;
        try {
            result= jobCommand.execute();
            log.info("********【{}】定时任务执行完成,执行结果:{}*********",jobName,result.toString());
        } catch (Exception e) {
            log.error("【{}】定时任务执行异常:{}",jobName,e);
            result = JobResult.failure("【{}】定时任务执行异常:" + e.getMessage());
        }
        jedisHandler.hashSet(JobRedisKey.JOB_LOG + jobName,jobCommand.getJobInfo().getCron(), JSONObject.toJSONString(result));
    }

}
