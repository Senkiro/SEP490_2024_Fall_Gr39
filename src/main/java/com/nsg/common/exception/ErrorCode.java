package com.nsg.common.exception;

public enum ErrorCode {
    //User
    USER_EXISTED(1001, "User already existed!"),
    INVALID_KEY(1002,"Invalid message key!"),
    USER_NOT_EXISTED(1007,"User not existed!"),
    INVALID_LOGINRQ(1008,"Wrong username or password "),
    PASSWORD_NOT_MATCHED(10081,"Old password not matched!"),
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
    TIME_SLOT_NAME_EXISTED(1125, "Time slot name already existed!"),

    //Room
    ROOMNUMBER_IS_NULL(1140, "Room number can not be null"),
    ROOM_NOT_FOUND(1141, "Room not found"),

    //Exam
    EXAM_TYPE_IS_NULL(1160, "Exam type can not be null"),
    EXAM_TYPE_NOT_FOUND(1161, "Exam type not found"),
    EXAM_TYPE_EXISTED(1161, "Exam type already existed!"),
    EXAM_NOT_FOUND(1162, "Exam not found!"),
    EXAM_TITLE_EXISTED(1163, "Exam title already existed!"),
    EXAM_LIST_IS_EMPTY(1164, "Exam list is empty"),
    EXAM_NOT_EXIST(1165, "Exam not exist!"),

    //Class
    CLASS_NAME_IS_NULL(1180, "Class name can not be null!"),
    CLASS_COLOUR_IS_NULL(1181, "Class colour can not be null!"),
    CLASS_NOT_FOUND(1182, "Class not found!"),
    CLASS_NAME_EXISTED(1183, "Class name already existed!"),
    STUDENT_LIST_IS_EMPTY(1184, "Student in class is empty!"),


    //Lesson
    LESSON_NOT_FOUND(1200, "Lesson not found!"),
    NOTNULL_LESSON_TITLE(1201, "Lesson title can not be null"),
    VOCABULARY_IS_NULL(1202, "Vocabulary can not be null!"),
    KANJI_IS_NULL(1203, "Kanji can not be null!"),

    //Event
    EVENT_NOT_EXIST(1220, "Event not existed!"),
    EVENT_EXISTED(1221, "Event existed!"),
    EVENT_NOT_FOUND(1222, "Event not found"),

    //news
    NEWS_NOT_FOUND(2000, "News not found"),

    //Student
    STUDENT_NOT_FOUND(1240, "Student not found!"),

    //session
    SESSION_NOT_FOUND(1260, "Session not found"),
    SESSION_LIST_EMPTY(1261, "There are no session for this class"),

    //schedule
    SCHEDULE_EXISTED(1280, "Schedule for this class already existed!"),

    //attendance
    ATTENDANCE_NOT_FOUND(1300, "Attendance not found!"),
    NO_DATA_ATTENDANCE(1301, "No data attendance found for student"),

    //curriculumn
    CURRICULUMN_NOT_FOUND(1320, "Curriculumn not found!"),
    CURRICULUMN_TITLE_NOT_NULL(1321, "Curriculumn title can not be null"),
    CURRICULUMN_LIST_NOT_FOUND(1340, "Curriculumn list not found!"),
    CURRICULUMN_LIST_TITLE_EXISTED(1341, "Curriculumn list title already existed!"),

    //mark
    MARK_NOT_FOUND(1360, "Mark not found!"),
    MARK_LIST_EMPTY(1361, "There are no mark!"),
    MIN_VALUE_MARK(1362, "Mark must be at least 0.0"),
    MAX_VALUE_MARK(1363, "Mark must not exceed 10.0"),
    MARK_NOT_NULL(1364, "Mark must not be null"),
    MARK_EXISTED(1365, "Mark already exists for this student and exam."),


    //holiday
    HOLIDAY_NOT_FOUND(2100, "Holiday not found"),

    //Excel
    PARSE_ERROR(1230,"Failed to parse Excel file" ),
    NUMBER_SHEET(1231, "Excel file must have at least 2 sheets: Lessons and Exams."),
    LESSON_SHEET_MISSING(1232, "Lesson sheet is missing"),
    EXAM_SHEET_MISSING(1233, "Exam sheet is missing");

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
