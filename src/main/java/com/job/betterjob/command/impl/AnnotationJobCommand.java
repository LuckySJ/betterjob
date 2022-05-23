package com.job.betterjob.command.impl;


import com.job.betterjob.model.JobResult;
import com.job.betterjob.command.JobCommand;
import com.job.betterjob.model.JobInfo;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

/**
 * @author songle
 * @create 2022-05-22 22:16
 * @descreption 通过反射执行被任务注解标注的方法
 */

@AllArgsConstructor
public class AnnotationJobCommand implements JobCommand{

    private JobInfo jobInfo;
    private final Method method;
    private final Object target;


    @Override
    public JobInfo getJobInfo() {
        return jobInfo;
    }

    @Override
    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    @Override
    public JobResult execute() throws Exception {
        return (JobResult) method.invoke(target);
    }

    @Override
    public String toString() {
        return super.toString() + "[" + target.getClass() + "#" + method.getName() + "]";
    }
}
