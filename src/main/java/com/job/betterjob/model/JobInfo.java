package com.job.betterjob.model;

import com.job.betterjob.annotation.BetterJobCron;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author songle
 * @create 2022-05-22 17:02
 * @descreption 定时任务信息实体类
 */

@Data
@NoArgsConstructor
public class JobInfo implements Serializable,Comparable<JobInfo>{

    /*
    * 定时任务名称
    */
    private String name;

    /*
    * 定时任务时间表达式
    */
    private String cron;

    /*
    * 定时任务状态
    */
    private int status;

    /*
    * 定时任务通知策略
    */
    private int noticeStrategy;

    /*
    * 定时任务上报策略
    */
    private int reportStrategy;

    /*
    * 定时任务版本
    */
    private int version;

    /*
    * 定时任务执行超时时间
    */
    private int timeout;

    /*
    * 下次任务的执行时间
    */
    private Date nextExcuteTime;
    /*
    * 最后一次任务的执行时间
    */
    private Date lastExcuteTime;

    /*
    * 服务名称
    */
    private String serviceName;

    /*
    * 任务最后执行状态
    */
    private int lastExcuteStatus;


    public JobInfo (BetterJobCron betterJobCron) {
        this.name = betterJobCron.name();
        this.status = betterJobCron.status();
        this.cron = betterJobCron.cron();
        this.timeout = betterJobCron.timeout();
        this.noticeStrategy = betterJobCron.noticeStrategy();
        this.reportStrategy = betterJobCron.reportStrategy();
        this.version = betterJobCron.version();
    }

    @Override
    public int compareTo(JobInfo jobInfo) {
        return nextExcuteTime.compareTo(jobInfo.nextExcuteTime);
    }
}
