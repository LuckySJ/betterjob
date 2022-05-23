package com.songle.betterjob.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author songle
 * @create 2022-05-22 21:33
 * @descreption  任务执行结果实体类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResult {

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 1000;

    private static final long serialVersionUID = -3334301472648327797L;

    private Integer code;

    private String msg;


    public static JobResult success(String msg) {
        return new JobResult(SUCCESS_CODE, msg);
    }

    public static JobResult failure(String msg) {
        return new JobResult(FAIL_CODE, msg);
    }
}
