package com.job.betterjob.handler;

import com.alibaba.fastjson.JSONObject;
import com.job.betterjob.constant.JobRedisKey;
import com.job.betterjob.constant.JobStatus;
import com.job.betterjob.model.JobInfo;
import com.job.betterjob.thread.ExecuteThread;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.net.InetAddress;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author songle
 * @create 2022-05-22 17:44
 * @descreption 任务执行处理器
 */

@Slf4j
public class ExecutorHandler {


    private JedisHandler jedisHandler;
    private String serverName;
    @Getter
    private final String executorId;

    public static final Map<String, ExecuteThread> CURRENT_EXECUTING_THREADS_REPOSITORY = new HashMap<>();

    @SneakyThrows
    public ExecutorHandler(String serverName, JedisHandler jedisHandler){
        this.serverName = serverName;
        this.jedisHandler = jedisHandler;
        InetAddress address = InetAddress.getLocalHost();
        this.executorId = "executor$" + this.serverName + "@" + address.getHostAddress() + "#" + System.currentTimeMillis();
        jedisHandler.init(15);
    }


    /**
     * @description 执行任务注册逻辑
     *
     * @param  jobInfo
     * @return boolean
     * @date    2022/5/22 21:35
     */
    public boolean registerJob(JobInfo jobInfo){
        try {
            // 从redis中获取任务信息
            JobInfo job = loadJobInfo(jobInfo.getName());
            if (job == null || job.getVersion() < jobInfo.getVersion()) {
                // 刷新下次任务的执行时间
                refreshNextExecuteTime(jobInfo,LocalDateTime.now());
                // 注册任务到redis
                return updateJobInfo(jobInfo);
        }else {
                log.info("定时任务 [{}] 本地版本号 [{}] 远端版本号 [{}] 不予更新！", jobInfo.getName(), jobInfo.getVersion(), job.getVersion());
            }
             return true;
    } catch (Exception e){
            log.error("任务注册异常:{}",e);
        }
        return false;
    }



    /**
     * @description 注册任务到redis
     *
     * @param  jobInfo
     * @return boolean
     * @date    2022/5/22 21:35
     */
    private boolean updateJobInfo(JobInfo jobInfo) {
        try {
            jedisHandler.hashSet(JobRedisKey.JOB_INFO,jobInfo.getName(),JSONObject.toJSONStringWithDateFormat(jobInfo,"yyyy-MM-dd HH:mm:ss"));
            return true;
        } catch (Exception e){
            log.error("注册任务到redis异常:{}",e);
            return false;
        }
    }


    /**
     * @description 刷新下次任务的执行时间
     *
     * @param  jobInfo
     * @return jobInfo
     * @date    2022/5/22 21:50
     */
    private JobInfo refreshNextExecuteTime(JobInfo jobInfo, LocalDateTime fromTIme) throws ParseException {
        CronExpression cronExpression = CronExpression.parse(jobInfo.getCron());
        LocalDateTime nextValidTimeAfter = cronExpression.next(fromTIme);
        if (nextValidTimeAfter != null) {
            jobInfo.setNextExcuteTime(Date.from(nextValidTimeAfter.atZone( ZoneId.systemDefault()).toInstant()));
        }else{
            jobInfo.setStatus(JobStatus.DISABLED);
            jobInfo.setNextExcuteTime(null);
        }
        return jobInfo;
    }


    /**
     * @description 根据任务名称查询任务信息
     *
     * @param  name
     * @return void
     * @date    2022/5/22 21:35
     */
    private JobInfo loadJobInfo(String name) {
        try{
            return jedisHandler.hashGet(JobRedisKey.JOB_INFO,name,JobInfo.class);
        } catch (Exception e){
            log.error("query job info error,cause:{}",e);
            return null;
        }
    }


    /**
     * @description  查询所有的任务信息
     *
     */
    public List<JobInfo> getAllJobInfo() {
        try{
            List<JobInfo> result = new ArrayList<>();
            Map<String, JobInfo> jobInfoMap = jedisHandler.hashGetAll(JobRedisKey.JOB_INFO, JobInfo.class);
            if (jobInfoMap != null) {
                result = new ArrayList<>(jobInfoMap.values());
            }
            return result;
        } catch (Exception e){
            log.error("query job list info error,cause:{}",e);
            return null;
        }
    }


    /**
     * @description  执行任务
     *
     */
    public boolean executeJob(JobInfo jobInfo) {
        String key = JobRedisKey.JOB_INFO_LOCK + jobInfo.getName();
        try{
            // 分布式锁
            jedisHandler.set(key,executorId,"nx","ex",jobInfo.getTimeout());
            if (executorId.equals(jedisHandler.get(JobRedisKey.JOB_INFO_LOCK + jobInfo.getName()))) {
                ExecuteThread executeThread = new ExecuteThread(CommandHandler.COMMAND_REPOSITORY.get(jobInfo.getName()));
                // 将当前执行线程任务交给线程池执行
                CommandHandler.EXECUTOR_POOL.execute(executeThread);
                // 刷新下次任务的执行时间
                refreshNextExecuteTime(jobInfo,LocalDateTime.now());
                return updateJobInfo(jobInfo);
            }
            return false;
        }catch (Exception e) {
            log.error("【{}】任务执行异常：{}",jobInfo.getName(),e);
            jedisHandler.del(key);
            return false;
        }

    }
}
