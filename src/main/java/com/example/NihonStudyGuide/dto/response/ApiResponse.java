package com.example.NihonStudyGuide.dto.response;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {
    private int code ;
    private String message;

    private T result;

    public ApiResponse(T result, int code, String message) {
        this.result = result;
        this.code = code;
        this.message = message;
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(null, HttpStatus.OK.value(), "success");
    }
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, HttpStatus.OK.value(), "success");
    }
    public static <T> ApiResponse<T> error() {
        return new ApiResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "error");
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(null, code, message);
    }

    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(data, code, message);
    }

    public static <T> ApiResponse<T> notFound() {
        return new ApiResponse<>(null, HttpStatus.NOT_FOUND.value(), "not found");
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(null, HttpStatus.NOT_FOUND.value(), message);
    }

    public static <T> ApiResponse<T> badRequest() {
        return new ApiResponse<>(null, HttpStatus.BAD_REQUEST.value(), "bad request");
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(null, HttpStatus.BAD_REQUEST.value(), message);
    }

    public static <T> ApiResponse<T> unauthorized() {
        return new ApiResponse<>(null, HttpStatus.UNAUTHORIZED.value(), "unauthorized");
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(null, HttpStatus.UNAUTHORIZED.value(), message);
    }

    public static <T> ApiResponse<T> forbidden() {
        return new ApiResponse<>(null, HttpStatus.FORBIDDEN.value(), "forbidden");
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(null, HttpStatus.FORBIDDEN.value(), message);
    }

    public static <T> ApiResponse<T> custom(int code, String message) {
        return new ApiResponse<>(null, code, message);
    }

    public static <T> ApiResponse<T> custom(int code, String message, T data) {
        return new ApiResponse<>(data, code, message);
    }
//error: Xử lý các tình huống lỗi chung với mã lỗi 500 (Internal Server Error).
//notFound: Xử lý tình huống khi tài nguyên không được tìm thấy với mã lỗi 404 (Not Found).
//badRequest: Xử lý các tình huống khi yêu cầu không hợp lệ với mã lỗi 400 (Bad Request).
//unauthorized: Xử lý các tình huống khi người dùng chưa xác thực với mã lỗi 401 (Unauthorized).
//forbidden: Xử lý các tình huống khi người dùng không có quyền truy cập với mã lỗi 403 (Forbidden).
//custom: Cho phép tạo ra các phản hồi tùy chỉnh với mã lỗi và thông điệp cụ thể

}
