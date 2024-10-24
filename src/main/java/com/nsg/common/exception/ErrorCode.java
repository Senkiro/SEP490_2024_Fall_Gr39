package com.nsg.common.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "User existed!"),
    INVALID_KEY(1002,"Invalid massage key!"),
    USER_NOT_EXISTED(1007,"User not existed!"),
    INVALID_LOGINRQ(1008,"Wrong username or password "),
    INVALID_ACCBAN(1009,"Your account has been locked !"),
    INVALID_BATCHNAME(1009, "Batch name can not be null")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
