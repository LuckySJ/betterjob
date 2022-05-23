package com.job.betterjob.annotation;

import com.job.betterjob.constant.NoticeStrategy;
import com.job.betterjob.constant.JobStatus;
import com.job.betterjob.constant.ReportStrategy;

import java.lang.annotation.*;

/**
 * @author songle
 * @create 2022-05-22 16:09
 * @descreption  betterjob 自定义注解
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BetterJobCron {

    String name() default "";

    String cron() default "";

    int timeout() default 1800;

    int status() default JobStatus.ENABLED;

    int noticeStrategy() default NoticeStrategy.ON_FAIL;

    int reportStrategy() default ReportStrategy.ALWAYS;

    int version() default 1;

}
