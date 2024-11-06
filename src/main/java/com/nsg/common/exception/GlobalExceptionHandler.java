package com.nsg.common.exception;

import com.nsg.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllExceptions(Exception e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(9999);
        apiResponse.setMessage("An unexpected error occurred: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    // Handle runtime exceptions, excluding AppException if it extends RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        if (e instanceof AppException) {
            return handleAppException((AppException) e);
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(9998);
        apiResponse.setMessage("Runtime error: " + e.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Handle custom AppException
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        String validationErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> {
                    String errorCodeMessage = fieldError.getDefaultMessage();
                    String detailedMessage;
                    try {
                        ErrorCode codeEnum = ErrorCode.valueOf(errorCodeMessage);
                        detailedMessage = codeEnum.getMessage();
                    } catch (IllegalArgumentException ex) {
                        detailedMessage = errorCodeMessage;
                    }

                    return detailedMessage;
                })
                .collect(Collectors.joining("; "));

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(validationErrors.isEmpty() ? errorCode.getMessage() : validationErrors);

        return ResponseEntity.badRequest().body(apiResponse);
    }

}
