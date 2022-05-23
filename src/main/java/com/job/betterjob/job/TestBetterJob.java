package com.job.betterjob.job;

import com.job.betterjob.annotation.BetterJobCron;
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


    @BetterJobCron(name = "测试betterJob定时任务",cron = "* */5 * * * ?")
    public void testJob(){
        log.info("测试betterJob定时任务成功！");
        System.out.println("测试betterJob定时任务成功！");
    }
}
