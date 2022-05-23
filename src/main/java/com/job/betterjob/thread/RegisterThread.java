package com.job.betterjob.thread;


import com.job.betterjob.handler.CommandHandler;
import com.job.betterjob.handler.ExecutorHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * @author songle
 * @create 2022-05-22 21:21
 * @descreption  任务注册线程
 */


@Slf4j
public class RegisterThread  extends Thread {

    private  ExecutorHandler executorHandler;

    private volatile boolean toStop = false;

    public RegisterThread(ExecutorHandler executorHandler) {
        this.executorHandler = executorHandler;
    }


    @Override
    public void run(){
        Thread.currentThread().setName("better-job-register-thread");
        Set<String> registeredJobs = new HashSet<>();
        while(!toStop) {
            try {
                CommandHandler.COMMAND_REPOSITORY.forEach((k,v) ->{
                    if (registeredJobs.contains(k)) {
                        return;
                    }
                    if (executorHandler.registerJob(v.getJobInfo())) {
                        log.info("定时任务{}注册成功",k);
                        registeredJobs.add(k);
                    }else{
                        log.error("定时任务{}注册异常",k);
                    }
                });
//                TimeUnit.SECONDS.sleep(10);
            }catch (Exception e){
                log.error("定时任务注册异常:{}",e);
            }
        }
    }
}
