package com.nsg.common.exception;

public enum ErrorCode {
    //User
    USER_EXISTED(1001, "User already existed!"),
    INVALID_KEY(1002,"Invalid message key!"),
    USER_NOT_EXISTED(1007,"User not existed!"),
    INVALID_LOGINRQ(1008,"Wrong username or password "),
    INVALID_ACCBAN(1009,"Your account has been locked !"),
    USER_NOT_FOUND(1010, "User not found"),
    INVALID_PASSWORD(1012, "Password must be at least 8 characters!"),
    INVALID_FULLNAME(1013, "Full name must be at least 2 characters and only content letters, spaces!"),
    INVALID_JAPANESENAME(1014, "Japanese name must be at least 2 characters"),
    DOB_NOT_IN_PAST(1015, "Date of birth must be in past!"),
    GENDER_NOTNULL(1016, "Gender cannot be null!"),

    //Email
    NOTNULL_EMAIL(1015, "Email cannot be null!"),
    INVALID_EMAIL(1016, "Email is not valid!"),
    EMAIL_EXISTED(1018, "Email already existed!"),
    EMAIL_NOT_EXISTED(1011, "Email not existed!"),

    //Batch
    INVALID_BATCHNAME(1100, "Batch name can not be null"),
    BATCH_EXISTED(1101, "Batch name existed!"),
    BATCH_NOT_EXISTED(1102, "Batch not existed!"),
    BATCHNAME_SIZE_INVALID(1103, "Batch name must be between 4 and 10 character"),
    START_TIME_NOTIN_FUTURE(1104, "Start time must be in the present or future"),
    END_TIME_NOTIN_FUTURE(1104, "End time must be in the future"),
    YEAR_NOT_AFTER2000(1105, "Year must be after 2000"),
    YEAR_IS_NULL(1106, "Year can not be null!"),

    //Time slot
    TIME_SLOT_NAME_IS_NULL(1120, "Time slot name can not be null!"),
    START_TIME_IS_NULL(1121, "Start time can not be null"),
    END_TIME_IS_NULL(1122, "End time can not be null"),
    INVALID_TIME_FORMAT(1123, "Time must be in the format HH:mm"),
    TIME_SLOT_NOT_FOUND(1124, "Time slot not found!s"),

    //Lesson
    LESSON_NOT_FOUND(1200, "Lesson not found!"),
    NOTNULL_LESSON_TITLE(1201, "Lesson title can not be null")
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
