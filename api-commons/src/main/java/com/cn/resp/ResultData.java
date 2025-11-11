package com.cn.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultData<T>{
    private String code;
    private String message;
    private T data;
    // 时间戳，那个时间调用的接口
    private Long timestamp;

    public ResultData() {
        this.timestamp = System.currentTimeMillis();
    }


    public static <T> ResultData<T> success(){
        return new ResultData<T>()
                .setCode(ReturnCodeEnum.RC200.getCode())
                .setMessage(ReturnCodeEnum.RC200.getMessage())
                .setData(null);
    }

    public static <T> ResultData<T> success(T data){
        return new ResultData<T>()
                .setCode(ReturnCodeEnum.RC200.getCode())
                .setMessage(ReturnCodeEnum.RC200.getMessage())
                .setData(data);
    }

    public static <T> ResultData<T> success(String code,T data){
        ReturnCodeEnum returnCodeEnumV1 = ReturnCodeEnum.getReturnCodeEnumV1(code);
        return new ResultData<T>()
                .setCode(code)
                .setMessage(returnCodeEnumV1.getMessage())
                .setData(data);
    }

    public static <T> ResultData<T> fail(String code,String message){
        return new ResultData<T>()
                .setCode(code)
                .setMessage(message)
                .setData(null);
    }

    public static <T> ResultData<T> fail(String code){
        ReturnCodeEnum returnCodeEnumV2 = ReturnCodeEnum.getReturnCodeEnumV2(code);
        return new ResultData<T>()
                .setCode(code)
                .setMessage(returnCodeEnumV2.getMessage())
                .setData(null);
    }

}