package com.nsg.common.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "User already existed!"),
    INVALID_KEY(1002,"Invalid message key!"),
    USER_NOT_EXISTED(1007,"User not existed!"),
    INVALID_LOGINRQ(1008,"Wrong username or password "),
    INVALID_ACCBAN(1009,"Your account has been locked !"),
    INVALID_BATCHNAME(1016, "Batch name can not be null"),
    USER_NOT_FOUND(1010, "User not found"),
    INVALID_EMAIL(1011, "Email is not valid!"),
    INVALID_PASSWORD(1012, "Password must be at least 8 characters!"),
    INVALID_FULLNAME(1013, "Full name must be at least 3 characters and only content letters, spaces!"),
    INVALID_JAPANESENAME(1014, "Japanese name must be at least 3 characters"),
    NOTNULL_EMAIL(1015, "Email cannot be null!")
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
