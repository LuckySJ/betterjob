package com.songle.betterjob.handler;

import com.songle.betterjob.command.JobCommand;
import com.songle.betterjob.thread.RegisterThread;

import java.util.HashMap;

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

    public CommandHandler(ExecutorHandler executorHandler){
        this.executorHandler = executorHandler;
        startRegisterThread();
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
