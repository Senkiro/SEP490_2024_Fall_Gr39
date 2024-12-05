package com.nsg.controller;

import com.nsg.common.enums.UserRole;
import com.nsg.common.utils.ExcelHelper;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserInforUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.*;
import com.nsg.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class StaffController {
    @Autowired
    TeacherService teacherService;

    @Autowired
    UserService userService;

    @Autowired
    StudentService studentService;

    @Autowired
    private ExcelHelper excelHelper;

    /**********************************
     * Manage Student
     **********************************/
    //create new student
    @PostMapping("/create-student")
    public ApiResponse<StudentResponse> createStudent(@RequestBody @Valid StudentCreattionRequest request) {
        studentService.createStudent(request);
        return ApiResponse.<StudentResponse>builder()
                .message("Create student successfully!")
                .build();
    }

    @GetMapping("/student-list")
    public ApiResponse<Page<StudentResponse>> viewStudents(@RequestParam int page,
                                                           @RequestParam int size) {
        Page<StudentResponse> studentList = studentService.getAllStudent(page, size);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    //get student list by batchName
    @GetMapping("/get-student-by-batch")
    public ApiResponse<Page<StudentResponse>> viewStudentByBatchName(@RequestParam int page,
                                                                     @RequestParam int size,
                                                                     @RequestParam String batch_name) {
        Page<StudentResponse> studentList = studentService.getStudentByBatchName(page, size, batch_name);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    //get student by batchName and className
    @GetMapping("/get-student-by-batch-class")
    public ApiResponse<Page<StudentResponse>> viewStudentByBatchNameAndClassName(@RequestParam int page,
                                                                                 @RequestParam int size,
                                                                                 @RequestParam String batch_name,
                                                                                 @RequestParam String class_id) {
        Page<StudentResponse> studentList = studentService.getStudentByBatchNameAndClassName(page, size, batch_name, class_id);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    //get a student by studentId
    @GetMapping("/get-student/{student_id}")
    public ApiResponse<StudentResponse> getStudent(@PathVariable("student_id") String student_id) {
        return ApiResponse.<StudentResponse>builder()
                .result(studentService.getStudent(student_id))
                .build();
    }

    @DeleteMapping("/delete-student/{student_id}")
    public ApiResponse<?> deleteStudent(@PathVariable("student_id") String student_id) {
        userService.deleteUser(student_id);
        return ApiResponse.builder()
                .message("Delete successful")
                .build();
    }

    @PostMapping("/upload-students")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (excelHelper.hasExcelFormat(file)) {
            try {
                List<StudentEntity> students = excelHelper.excelToStudents(file.getInputStream());
                studentService.saveAll(students);
                return ResponseEntity.ok(ApiResponse.<String>builder().message("File uploaded and students added successfully.").build());
            } catch (Exception e) {
                return ResponseEntity.status(500).body(ApiResponse.<String>builder()
                        .message("Failed to parse file.")
                        .build());
            }
        }
        return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                .message("Please upload an Excel file.")
                .build());
    }

    @GetMapping("/search-student")
    public ApiResponse<Page<StudentResponse>> searchStudentByName(@RequestParam String name,
                                                                  @RequestParam String class_id,
                                                                  @RequestParam int page,
                                                                  @RequestParam int size) {
        Page<StudentResponse> studentEntityList = studentService.findStudentsByName(name, class_id, page, size);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentEntityList)
                .build();
    }

    @PutMapping(value = "/update-student/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> updateUser(
            @PathVariable("userId") String userId,
            @RequestPart("userDetail") @Valid UserInforUpdateRequest request,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {

        // Gọi service để xử lý logic cập nhật thông tin người dùng
        var updatedUser = userService.updateUserInfor(userId, request, avatar);

        // Trả về kết quả
        return ApiResponse.builder()
                .result(updatedUser)
                .message("User updated successfully!")
                .build();
    }

    /**********************************
     * Manage Teacher
     **********************************/

    //get teacher paginate
    @GetMapping("/get-all-teacher")
    ApiResponse<Map<String, Object>> getAllTeacher(@RequestParam int page, @RequestParam int size) {
        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
        Page<UserInforResponse> teacherPage = teacherService.getTeachers(page, size);
        List<UserInforResponse> teacherList = teacherPage.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("teachers", teacherList); // Danh sách giáo viên
        response.put("totalElements", teacherPage.getTotalElements()); // Tổng số giáo viên
        response.put("totalPages", teacherPage.getTotalPages()); // Tổng số trang
        response.put("currentPage", teacherPage.getNumber()); // Trang hiện tại (bắt đầu từ 0)
        response.put("pageSize", teacherPage.getSize()); // Số lượng phần tử trên mỗi trang
        apiResponse.setCode(1000);
        apiResponse.setResult(response);
        return apiResponse;
    }

    //create teacher
    @PostMapping("/create-teacher")
    public ApiResponse<?> createTeacher(@RequestBody @Valid @Validated UserCreationRequest request) {
        UserRole role = UserRole.TEACHER;
        return ApiResponse.builder()
                .result(userService.createUser(request, role))
                .build();
    }



}
