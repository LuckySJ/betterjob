package com.songle.betterjob.constant;


/**
 * @author songle
 * @create 2022-05-22 16:28
 * @descreption 执行结果上报策略
 */


public class ReportStrategy {

    /*
     从不上报
    */
    public static final int NEVER = 0;

    /*
     总是上报
    */
    public static final int ALWAYS = 1;

    /*
     成功时上报
    */
    public static final int ON_SUCCESS = 2;

    /*
     失败时上报
    */
    public static final int ON_FAIL = 3;
}
