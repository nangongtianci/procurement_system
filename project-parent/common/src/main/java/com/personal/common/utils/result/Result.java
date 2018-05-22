package com.personal.common.utils.result;


import com.personal.common.enume.BizExceptionEnum;

public class Result {
    public final static int OK_CODE = 0;
    public final static int EMPTY_CODE = 1;
    public final static int FAIL_CODE = -1;
    public final static int TIMEOUT_CODE = -2;

    private int status;
    private String message;
    private Object data;

    public static Result OK(){
        Result success = new Result();
        success.setStatus(OK_CODE);
        success.setMessage("success");
        return success;
    }

    public static Result OK(Object obj){
        Result success = new Result();
        success.setStatus(OK_CODE);
        if(obj instanceof String){
            success.setMessage((String) obj);
        }else{
            success.setMessage("success");
            success.setData(obj);
        }
        return success;
    }

    public static Result OK(int code,String msg){
        Result success = new Result();
        success.setStatus(code);
        success.setMessage(msg);
        return success;
    }

    public static Result EMPTY() {
        Result success = new Result();
        success.setStatus(EMPTY_CODE);
        success.setMessage("empty");
        return success;
    }

    public static Result EMPTY(String message) {
        Result success = new Result();
        success.setStatus(EMPTY_CODE);
        success.setMessage(message);
        return success;
    }

    public static Result FAIL() {
        Result success = new Result();
        success.setStatus(FAIL_CODE);
        success.setMessage("fail");
        return success;
    }

    public static Result FAIL(String errorMsg){
        Result success = new Result();
        success.setStatus(FAIL_CODE);
        success.setMessage(errorMsg);
        return success;
    }

    public static Result FAIL(BizExceptionEnum exceptionEnum){
        Result success = new Result();
        success.setStatus(exceptionEnum.getCode());
        success.setMessage(exceptionEnum.getMsg());
        return success;
    }

    public static Result FAIL(int code,String errorMsg){
        Result success = new Result();
        success.setStatus(code);
        success.setMessage(errorMsg);
        return success;
    }

    public static Result TIMEOUT() {
        Result success = new Result();
        success.setStatus(FAIL_CODE);
        success.setMessage("fail");
        return success;
    }

    public static Result TIMEOUT(int code, String errorMsg) {
        Result success = new Result();
        success.setStatus(code);
        success.setMessage(errorMsg);
        return success;
    }

    public static Result TIMEOUT(String errorMsg) {
        Result success = new Result();
        success.setStatus(FAIL_CODE);
        success.setMessage(errorMsg);
        return success;
    }

    public int getStatus() {
        return status;
    }

    public Result setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }
}
