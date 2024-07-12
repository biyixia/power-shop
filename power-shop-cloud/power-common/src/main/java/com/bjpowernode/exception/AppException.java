package com.bjpowernode.exception;

public class AppException extends RuntimeException{
    private int code = 500;
    private String msg = "服务器异常";

    public AppException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public AppException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

