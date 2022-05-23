package com.job.betterjob.handler;

import com.alibaba.fastjson.JSONObject;
import com.job.betterjob.constant.JobRedisKey;
import com.job.betterjob.constant.JobStatus;
import com.job.betterjob.model.JobInfo;
import com.job.betterjob.util.CronExpression;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.text.ParseException;
import java.util.Date;

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
                refreshNextExecuteTime(jobInfo,new Date());
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
            jedisHandler.set(JobRedisKey.JOB_INFO + ":" + jobInfo.getName(),JSONObject.toJSONStringWithDateFormat(jobInfo,"yyyy-MM-dd HH:mm:ss"));
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
    private JobInfo refreshNextExecuteTime(JobInfo jobInfo, Date fromTIme) throws ParseException {
        Date nextValidTimeAfter = new CronExpression(jobInfo.getCron()).getNextValidTimeAfter(fromTIme);
        if (nextValidTimeAfter != null) {
            jobInfo.setNextExcuteTime(nextValidTimeAfter);
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
            String jobInfo = jedisHandler.get(JobRedisKey.JOB_INFO + ":" + name);
            return JSONObject.parseObject(jobInfo,JobInfo.class);
        } catch (Exception e){
            log.error("query job info error,cause:{}",e);
            return null;
        }
    }
}
