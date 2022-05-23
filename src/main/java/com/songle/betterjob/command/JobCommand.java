package com.songle.betterjob.command;

import com.songle.betterjob.model.JobInfo;
import com.songle.betterjob.model.JobResult;


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
