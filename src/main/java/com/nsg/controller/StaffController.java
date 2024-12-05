package com.nsg.controller;

import com.nsg.common.enums.UserRole;
import com.nsg.common.utils.ExcelHelper;
import com.nsg.dto.request.attendance.AttendanceRequest;
import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.request.classRequest.ClassRequest;
import com.nsg.dto.request.curriculumn.CurriculumnListRequest;
import com.nsg.dto.request.curriculumn.CurriculumnRequest;
import com.nsg.dto.request.event.EventUpdateRequest;
import com.nsg.dto.request.eventFeedback.EventFeedbackCreattionRequest;
import com.nsg.dto.request.exam.ExamRequest;
import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.dto.request.exam.ExamTypeRequest;
import com.nsg.dto.request.exam.ExamUpdateRequest;
import com.nsg.dto.request.holiday.HolidayRequest;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.request.mark.MarkCreationRequest;
import com.nsg.dto.request.mark.MarkUpdateRequest;
import com.nsg.dto.request.news.NewsRequest;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserInforUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.attendance.AttendanceResponse;
import com.nsg.dto.response.batch.BatchResponse;
import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.dto.response.curriculumn.CurriculumnListResponse;
import com.nsg.dto.response.curriculumn.CurriculumnResponse;
import com.nsg.dto.response.event.EventResponse;
import com.nsg.dto.response.eventFeedback.EventFeedbackResponse;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.exam.ExamResponseForMark;
import com.nsg.dto.response.exam.ExamTypeResponse;
import com.nsg.dto.response.holiday.HolidayResponse;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.dto.response.mark.MarkResponse;
import com.nsg.dto.response.news.NewsResponse;
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
    BatchService batchService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    LessonService lessonService;

    @Autowired
    UserService userService;

    @Autowired
    StudentService studentService;

    @Autowired
    ClassService classService;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    private ExcelHelper excelHelper;

    @Autowired
    private NewsService newsService;

    @Autowired
    MarkService markService;

    @Autowired
    EventFeedbackService eventFeedbackService;


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
        markService.calculateAverageMark(class_id);
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
     * Manage Batch
     **********************************/

    //get all batch
    @GetMapping("/batch")
    ApiResponse<Page<BatchResponse>> getAllBatch(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<BatchResponse>>builder()
                .code(1000)
                .result(batchService.getBatches(page, size))
                .build();
    }

    @GetMapping("/search-batch")
    public ApiResponse<Page<BatchResponse>> searchBatchByName(@RequestParam String name, @RequestParam int page, @RequestParam int size) {
        Page<BatchResponse> batchEntityList = batchService.findBatchsByName(name, page, size);
        return ApiResponse.<Page<BatchResponse>>builder()
                .result(batchEntityList)
                .build();
    }

    //get batch by batch name
    @GetMapping("/batch/by-name")
    ApiResponse<BatchEntity> getBatchByName(@RequestParam String batchName) {
        ApiResponse<BatchEntity> apiResponse = new ApiResponse<>();
        BatchEntity batchEntity = batchService.getBatch(batchName);

        if (batchEntity != null) {
            apiResponse.setCode(1000);
            apiResponse.setResult(batchEntity);
        } else {
            apiResponse.setCode(1017);
            apiResponse.setMessage("Batch not found with name: " + batchName);
        }

        return apiResponse;
    }

    //create new batch
    @PostMapping("/save-batch")
    ApiResponse<BatchEntity> saveBatch(@RequestBody @Validated BatchCreationRequest request) {
        batchService.saveBatch(request);
        BatchEntity batchEntity = batchService.getBatch(request.getBatchName());
        return ApiResponse.<BatchEntity>builder()
                .message("A new batch have been created!")
                .result(batchEntity)
                .build();
    }

    //update
    @PostMapping("/update-batch")
    ApiResponse<BatchEntity> updateBatch(@RequestBody BatchCreationRequest request) {
        BatchEntity batch = batchService.updateBatch(request.getBatchName(), request);
        return ApiResponse.<BatchEntity>builder()
                .result(batch)
                .build();
    }

    //delete batch
    @DeleteMapping("/delete-batch/{batchName}")
    ApiResponse<?> deleteBatch(@PathVariable("batchName") String batchName) {
        batchService.deleteBatch(batchName);
        return ApiResponse.builder()
                .message("Delete successfully!")
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


    /**********************************
     * Manage Class
     **********************************/
    //create a class
    @PostMapping("/create-class")
    public ApiResponse<?> createClass(@RequestBody ClassRequest request ) {
        classService.createClass(request);
        return ApiResponse.builder()
                .message("Create new class successfully!")
                .build();
    }

    //get all class
    @GetMapping("/get-all-class")
    public ApiResponse<Page<ClassResponse>> getAllClass(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<ClassResponse>>builder()
                .result(classService.getAllClass(page, size))
                .build();
    }

    //get by batch
    @GetMapping("/get-class-by-batch")
    public ApiResponse<Page<ClassResponse>> getClassByBatch(@RequestParam String batch_name, @RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<ClassResponse>>builder()
                .result(classService.getClassByBatch(batch_name,page, size))
                .build();
    }

    //get a class
    @GetMapping("/get-class/{class_id}")
    public ApiResponse<ClassResponse> getClass(@PathVariable("class_id") String class_id) {
        return ApiResponse.<ClassResponse>builder()
                .result(classService.getClass(class_id))
                .build();
    }

    //update a class
    @PostMapping("/update-class/{class_id}")
    public ApiResponse<ClassResponse> updateClass(@PathVariable("class_id") String class_id, @RequestBody ClassRequest request) {
        return ApiResponse.<ClassResponse>builder()
                .result(classService.updateClass(class_id, request))
                .message("Update class successfully!")
                .build();
    }

    //delete a class
    @DeleteMapping("/delete-clas/{class_id}")
    public ApiResponse<?> deleteClass(@PathVariable("class_id") String class_id) {
        classService.deleteClass(class_id);
        return ApiResponse.builder()
                .message("Delete class successfully!")
                .build();
    }

    /**********************************
     * Manage Attendance
     **********************************/
    //get student by class Id
    @GetMapping("/get-students-class")
    public ApiResponse<Page<StudentResponse>> viewStudentByBatchNameAndClassName(@RequestParam int page,
                                                                                 @RequestParam int size,
                                                                                 @RequestParam String classId) {

        Page<StudentResponse> studentList = studentService.getStudentByClassId(page, size, classId);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    //create new attendance
    @PostMapping("/create-attendance")
    public ApiResponse<?> createAttendance(@RequestBody AttendanceRequest request) {
        attendanceService.createAttendance(request);
        return ApiResponse.builder()
                .message("Create new attendance successfully!")
                .build();
    }

    //get all attendance
    @GetMapping("/get-all-attendance")
    public ApiResponse<Page<AttendanceResponse>> getAllAttendance(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<AttendanceResponse>>builder()
                .result(attendanceService.getAllAttendance(page, size))
                .build();
    }

    //get attendance by id
    @GetMapping("/get-attendance/{attendance_id}")
    public ApiResponse<AttendanceResponse> getAttendanceById(@PathVariable("attendance_id") String attendance_id) {
        return ApiResponse.<AttendanceResponse>builder()
                .result(attendanceService.getAttendance(attendance_id))
                .build();
    }

    //get attendance by session
    @GetMapping("/get-attendance-session/{session_id}")
    public ApiResponse<Page<AttendanceResponse>> getAttendanceBySession(@PathVariable("session_id") String session_id,
                                                                        @RequestParam int page,
                                                                        @RequestParam int size) {
        return ApiResponse.<Page<AttendanceResponse>>builder()
                .result(attendanceService.getAttendanceBySession(session_id, page, size))
                .build();
    }

    //get attendance by student
    @GetMapping("/get-attendance-student/{student_id}")
    public ApiResponse<Page<AttendanceResponse>> getAttendanceByStudent(@PathVariable("student_id") String student_id,
                                                                        @RequestParam int page,
                                                                        @RequestParam int size) {
        return ApiResponse.<Page<AttendanceResponse>>builder()
                .result(attendanceService.getAttendanceByStudent(student_id, page, size))
                .build();
    }

    //update attendance
    @PostMapping("/update-attendance/{attendance_id}")
    public ApiResponse<AttendanceResponse> updateSessionById(@PathVariable("attendance_id") String attendance_id, @RequestBody AttendanceRequest request) {
        return ApiResponse.<AttendanceResponse>builder()
                .result(attendanceService.updateAttendance(attendance_id, request))
                .build();
    }

    //delete attendance
    @DeleteMapping("/delete-attendance/{attendance_id}")
    public ApiResponse<?> deleteAttendanceById(@PathVariable("attendance_id") String attendance_id) {
        attendanceService.deleteAttendance(attendance_id);
        return ApiResponse.builder()
                .message("Delete attendance successfully!")
                .build();
    }

    /**********************************
     * Manage News
     **********************************/
    @PostMapping("/create-news")
    public ApiResponse<NewsResponse> createNews(@RequestBody @Valid NewsRequest request) {
        return ApiResponse.<NewsResponse>builder()
                .result(newsService.createNews(request))
                .message("Create draft news successfully!")
                .build();
    }

    //get news
    @GetMapping("/get-all-news")
    public ApiResponse<Page<NewsResponse>> getNewsByBatch(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<NewsResponse>>builder()
                .result(newsService.getAllNews(page, size))
                .build();
    }

    //get publish news
    @GetMapping("/get-all-publish-news")
    public ApiResponse<Page<NewsResponse>> getAllPublishNews(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<NewsResponse>>builder()
                .result(newsService.getAllPublishNews(page, size))
                .build();
    }

    //update news
    @PostMapping("/update-news/{newsId}")
    public ApiResponse<NewsResponse> updateNews(@PathVariable("newsId") String newsId, @RequestBody NewsRequest request) {
        return ApiResponse.<NewsResponse>builder()
                .result(newsService.updateNews(newsId, request))
                .build();
    }

    //delete news
    @DeleteMapping("/delete-news/{newsId}")
    public ApiResponse<?> deleteNews(@PathVariable("newsId") String newsId) {
        newsService.deleteNews(newsId);
        return ApiResponse.builder()
                .message("Delete news successfully!")
                .build();
    }

    // getNewsById
    @GetMapping("/get-news")
    public ApiResponse<NewEntity> getNewsById(@RequestParam String newsId) {
        NewEntity newEntity = newsService.getNewtById(newsId);
        return ApiResponse.<NewEntity>builder()
                .result(newEntity)
                .build();
    }


    /**********************************
     * Manage Mark
     **********************************/
    //create mark
    @PostMapping("/create-mark")
    public ApiResponse<?> createMark(@RequestBody @Valid MarkCreationRequest request) {
        markService.createMark(request);
        return ApiResponse.builder()
                .message("Create new mark successfully!")
                .build();
    }

    //get all mark
    @GetMapping("/get-all-mark")
    public ApiResponse<Page<MarkResponse>> getAllMark(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<MarkResponse>>builder()
                .result(markService.getAllMark(page, size))
                .build();
    }

    //get a mark by id
    @GetMapping("/get-mark/{mark_id}")
    public ApiResponse<MarkResponse> getMark(@PathVariable("mark_id") String mark_id) {
        return ApiResponse.<MarkResponse>builder()
                .result(markService.getMark(mark_id))
                .build();
    }

    //get mark list of a student
    @GetMapping("/get-student-mark/{student_id}")
    public ApiResponse<List<MarkResponse>> getStudentMark(@PathVariable("student_id") String student_id) {
        markService.calculateAverageMark(student_id);
        return ApiResponse.<List<MarkResponse>>builder()
                .result(markService.getMarkByStudent(student_id))
                .build();
    }

    //update mark
    @PostMapping("/update-mark/{mark_id}")
    public ApiResponse<MarkResponse> updateMark(@PathVariable("mark_id") String mark_id, @RequestBody @Valid MarkUpdateRequest request) {
        return ApiResponse.<MarkResponse>builder()
                .result(markService.updateMark(mark_id, request))
                .message("Mark update successfully!")
                .build();
    }

    //deleteMark
    @DeleteMapping("/delete-mark/{mark_id}")
    public ApiResponse<?> deleteMark(@PathVariable("mark_id") String mark_id) {
        markService.deleteMark(mark_id);
        return ApiResponse.builder()
                .message("Delete mark successfully!")
                .build();
    }

    //get mark by exam and class
    @GetMapping("/get-mark-by-session-exam")
    public ApiResponse<List<MarkResponse>> getMarkByExamAndClass(@RequestParam int exam_id, @RequestParam String class_id) {
        return ApiResponse.<List<MarkResponse>>builder()
                .result(markService.getMarkByExamAndSessionClass(exam_id, class_id) )
                .build();
    }

    /**********************************
     * Manage Event Feedback
     **********************************/
    //create new event feedback
    @PostMapping("/create-event-feedback")
    public ApiResponse<?> createEventFeedback(@RequestBody @Valid EventFeedbackCreattionRequest request) {
        eventFeedbackService.createEventFeedback(request);
        return ApiResponse.builder()
                .message("Create new event feedback successfully!")
                .build();
    }


    //get all
    @GetMapping("/get-all-event-feedback")
    public ApiResponse<Page<EventFeedbackResponse>> getAllEventFeedback(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<EventFeedbackResponse>>builder()
                .result( eventFeedbackService.getAllEventFeedback(page, size) )
                .build();
    }

    //get one
    @GetMapping("/get-event-feedback")
    public ApiResponse<EventFeedbackResponse> getEventFeedback(@RequestParam String event_feedback_id) {
        return ApiResponse.<EventFeedbackResponse>builder()
                .result( eventFeedbackService.getEventFeedback(event_feedback_id) )
                .build();
    }

    //get by event id
    @GetMapping("/get-event-feedback-by-event")
    public ApiResponse<Page<EventFeedbackResponse>> getEventFeedbackByEvent(@RequestParam String event_id,
                                                                            @RequestParam int page,
                                                                            @RequestParam int size) {
        return ApiResponse.<Page<EventFeedbackResponse>>builder()
                .result( eventFeedbackService.getEventFeedbackOfOneEvent(event_id, page, size) )
                .build();
    }

    //get by student id
    @GetMapping("/get-event-feedback-by-student")
    public ApiResponse<Page<EventFeedbackResponse>> getEventFeedbackByStudent(@RequestParam String student_id,
                                                                            @RequestParam int page,
                                                                            @RequestParam int size) {
        return ApiResponse.<Page<EventFeedbackResponse>>builder()
                .result( eventFeedbackService.getEventFeedbackOfOneStudent(student_id, page, size) )
                .build();
    }

    //update
    @PostMapping("/update-event-feedback")
    public ApiResponse<EventFeedbackResponse> updateEventFeedback(@RequestParam String event_feedback_id,
                                                                  @RequestBody @Valid EventFeedbackCreattionRequest request) {
        eventFeedbackService.updateEventFeedback(event_feedback_id, request);
        return ApiResponse.<EventFeedbackResponse>builder()
                .message("Update event feedback successfully!")
                .build();
    }

    //delete
    @DeleteMapping("/delete-event-feedback")
    public ApiResponse<?> deleteEventFeedback(@RequestParam String event_feedback_id) {
        eventFeedbackService.deleteEventFeedback(event_feedback_id);
        return ApiResponse.builder()
                .message("Delete event feedback successfully!")
                .build();
    }

}
