package com.job.betterjob.constant;



/**
 * @author songle
 * @create 2022-05-22 16:25
 * @descreption 任务执行通知策略
 */


public class NoticeStrategy {

    /*
     从不通知
    */
    public static final int NEVER = 0;

    /*
     总是通知
    */
    public static final int ALWAYS = 1;

    /*
     成功时通知
    */
    public static final int ON_SUCCESS = 2;

    /*
     失败时通知
    */
    public static final int ON_FAIL = 3;
}
