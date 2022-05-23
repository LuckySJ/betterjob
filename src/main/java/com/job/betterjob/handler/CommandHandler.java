package com.job.betterjob.handler;

import com.job.betterjob.thread.RegisterThread;
import com.job.betterjob.command.JobCommand;
import com.job.betterjob.thread.ScheduledThread;

import java.util.HashMap;
import java.util.concurrent.*;

/**
 * @author songle
 * @create 2022-05-22 17:47
 * @descreption 任务信息处理器
 */

public class CommandHandler {

    private ExecutorHandler executorHandler;


    public static final Integer SUCCESS_CODE = 200;

    public static final HashMap<String,JobCommand> COMMAND_REPOSITORY = new HashMap<>();

    private RegisterThread registerThread;
    private ScheduledThread scheduledThread;

    public static final ExecutorService EXECUTOR_POOL = new ThreadPoolExecutor(3,8,0L,
            TimeUnit.SECONDS,new LinkedBlockingDeque<>(),new ThreadPoolExecutor.AbortPolicy());

    public CommandHandler(ExecutorHandler executorHandler){
        this.executorHandler = executorHandler;
        startRegisterThread();
        startScheduledThread();
    }


    /**
     * @description  开启任务注册线程
     *
     * @param
     */
    private void startRegisterThread() {
        this.registerThread = new RegisterThread(executorHandler);
        this.registerThread.setDaemon(true);
        this.registerThread.start();
    }

    /**
     * @description  开启任务调度线程
     *
     * @param
     */
    private void startScheduledThread() {
        this.scheduledThread = new ScheduledThread(executorHandler);
        this.scheduledThread.setDaemon(true);
        this.scheduledThread.start();
    }


    /**
     * @description  任务注册（本地缓存）
     *
     * @param  jobName,jobCommand
     * @author songle
     * @date    2022/5/22 21:19
     */
    public static void registryJobCommand(String jobName,JobCommand jobCommand){
        COMMAND_REPOSITORY.put(jobName,jobCommand);
    }



}
