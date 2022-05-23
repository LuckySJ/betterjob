package com.job.betterjob.job;

import com.job.betterjob.annotation.BetterJobCron;
import com.job.betterjob.model.JobResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author songle
 * @create 2022-05-23 9:55
 * @descreption 定时任务类
 */

@Slf4j
@Component
public class TestBetterJob {


    @BetterJobCron(name = "测试betterJob定时任务",cron = "0 0/5 * * * ?")
    public JobResult testJob(){
        log.info("测试betterJob定时任务成功！");
        System.out.println("测试betterJob定时任务成功！");
        return JobResult.success("成功");
    }
}
