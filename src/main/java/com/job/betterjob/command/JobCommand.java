package com.job.betterjob.command;

import com.job.betterjob.model.JobResult;
import com.job.betterjob.model.JobInfo;


/**
 * @author songle
 * @create 2022-05-22 17:52
 * @descreption 任务处理类
 */

public interface JobCommand {

    JobInfo getJobInfo();

    void setJobInfo(JobInfo jobInfo);

    JobResult execute() throws  Exception;

}
