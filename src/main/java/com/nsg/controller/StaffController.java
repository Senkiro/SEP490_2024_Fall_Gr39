package com.nsg.controller;

import com.nsg.common.enums.UserRole;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.common.utils.ExcelHelper;
import com.nsg.dto.request.attendance.AttendanceRequest;
import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.request.classRequest.ClassRequest;
import com.nsg.dto.request.curriculumn.CurriculumnListRequest;
import com.nsg.dto.request.curriculumn.CurriculumnRequest;
import com.nsg.dto.request.event.EventUpdateRequest;
import com.nsg.dto.request.exam.ExamRequest;
import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.dto.request.exam.ExamTypeRequest;
import com.nsg.dto.request.exam.ExamUpdateRequest;
import com.nsg.dto.request.holiday.HolidayRequest;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.request.mark.MarkCreationRequest;
import com.nsg.dto.request.mark.MarkUpdateRequest;
import com.nsg.dto.request.news.NewsRequest;
import com.nsg.dto.request.room.RoomRequest;
import com.nsg.dto.request.session.ScheduleCreationRequest;
import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.timeSlot.TimeSlotCreationRequest;
import com.nsg.dto.request.timeSlot.TimeSlotUpdateRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserInforUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.attendance.AttendanceResponse;
import com.nsg.dto.response.batch.BatchResponse;
import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.dto.response.curriculumn.CurriculumnListResponse;
import com.nsg.dto.response.curriculumn.CurriculumnResponse;
import com.nsg.dto.response.event.EventResponse;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.exam.ExamResponseForMark;
import com.nsg.dto.response.exam.ExamTypeResponse;
import com.nsg.dto.response.holiday.HolidayResponse;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.dto.response.mark.MarkResponse;
import com.nsg.dto.response.news.NewsResponse;
import com.nsg.dto.response.room.RoomResponse;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.dto.response.timeSlot.TimeSlotResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.*;
import com.nsg.repository.BatchRepository;
import com.nsg.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    TimeSlotService timeSlotService;

    @Autowired
    RoomService roomService;

    @Autowired
    ExamTypeService examTypeService;

    @Autowired
    ExamService examService;

    @Autowired
    EventService eventService;

    @Autowired
    ClassService classService;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    SessionService sessionService;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    private ExcelHelper excelHelper;

    @Autowired
    private NewsService newsService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    CurriculumnService curriculumnService;

    @Autowired
    CurriculumnListService curriculumnListService;

    @Autowired
    MarkService markService;


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
     * Manage Lesson
     **********************************/

    //get all
    @GetMapping("/lesson")
    ApiResponse<Page<LessonResponse>> getAllLesson(@RequestParam int page, @RequestParam int size) {
        Page<LessonResponse> lessonEntityList = lessonService.getLessons(page, size);
        return ApiResponse.<Page<LessonResponse>>builder()
                .result(lessonEntityList)
                .build();
    }

    //create new lesson
    @PostMapping("/create-lesson")
    ApiResponse<LessonEntity> createLesson(@RequestBody @Valid LessonCreateRequest request) {
        lessonService.createLesson(request);
        return ApiResponse.<LessonEntity>builder()
                .message("A new lesson have been created!")
                .build();
    }

    //get a lesson by id
    @GetMapping("/get-lesson/{lesson_id}")
    ApiResponse<LessonResponse> getLesson(@PathVariable("lesson_id") String lesson_id) {
        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.getLesson(lesson_id))
                .build();
    }

    //delete lesson
    @DeleteMapping("/lesson/{lessonId}")
    ApiResponse<?> deleteLesson(@PathVariable("lessonId") String lessonId) {
        lessonService.deleteLesson(lessonId);
        return ApiResponse.builder()
                .message("Delete lesson successfully!")
                .build();
    }

    //update lesson
    @PostMapping("/update-lesson/{lessonId}")
    ApiResponse<LessonEntity> updateLesson(@PathVariable("lessonId") String lessonId, @RequestBody LessonCreateRequest request) {
        LessonEntity lesson = lessonService.updateLesson(lessonId, request);
        return ApiResponse.<LessonEntity>builder()
                .result(lesson)
                .build();
    }

//    //upload lesson
//    @PostMapping("/upload-lessons")
//    public ResponseEntity<ApiResponse<String>> uploadFileLesson(@RequestParam("file") MultipartFile file) {
//        if (excelHelper.hasExcelFormat(file)) {
//            try {
//                List<LessonEntity> lessons = excelHelper.excelToLessons(file.getInputStream());
//                lessonService.saveAll(lessons);
//                return ResponseEntity.ok(ApiResponse.<String>builder().
//                        message("File uploaded and lessons added successfully.")
//                        .build());
//            } catch (Exception e) {
//                return ResponseEntity.status(500).body(ApiResponse.<String>builder()
//                        .message("Failed to parse file.")
//                        .build());
//            }
//        }
//        return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
//                .message("Please upload an Excel file.")
//                .build());
//    }

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
     * Manage Time Slot
     **********************************/

    //create time slot
    @PostMapping("/create-time-slot")
    public ApiResponse<TimeSlotResponse> createTimeSlot(@RequestBody @Valid TimeSlotCreationRequest request) {
        return ApiResponse.<TimeSlotResponse>builder()
                .result(timeSlotService.createTimeSlot(request))
                .message("Create time slot successfully!")
                .build();
    }

    //view all time slot
    @GetMapping("/time-slot-list")
    ApiResponse<List<TimeSlotResponse>> getAllTimeSlot() {
        return ApiResponse.<List<TimeSlotResponse>>builder()
                .result(timeSlotService.getAllTimeSlot())
                .build();
    }

    //get one time slot information by id
    @GetMapping("/get-time-slot/{timeSlotId}")
    ApiResponse<TimeSlotResponse> getTimeSlot(@PathVariable("timeSlotId") String timeSlotId) {
        return ApiResponse.<TimeSlotResponse>builder()
                .result(timeSlotService.getTimeSlotById(timeSlotId))
                .build();
    }

    //update time slot
    @PostMapping("/update-time-slot/{timeSlotId}")
    ApiResponse<TimeSlotResponse> updateTimeSlot(@PathVariable("timeSlotId") String timeSlotId, @RequestBody TimeSlotUpdateRequest request) {
        return ApiResponse.<TimeSlotResponse>builder()
                .result(timeSlotService.updateTimeSlotById(timeSlotId, request))
                .message("Update time slot successfully!")
                .build();
    }

    //delete time slot
    @DeleteMapping("/delete-time-slot/{timeSlotId}")
    ApiResponse<?> deleteTimeSlot(@PathVariable("timeSlotId") String timeSlotId) {
        timeSlotService.deleteTimeSlot(timeSlotId);
        return ApiResponse.builder()
                .message("Delete time slot successfully!")
                .build();
    }

    /**********************************
     * Manage Room
     **********************************/
    //create room
    @PostMapping("/create-room")
    public ApiResponse<RoomResponse> createRoom(@RequestBody @Valid RoomRequest request) {
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.createRoom(request))
                .message("Create room successfully!")
                .build();
    }

    //get all room
    @GetMapping("/get-all-room")
    public ApiResponse<List<RoomResponse>> getAllRoom() {
        return ApiResponse.<List<RoomResponse>>builder()
                .result(roomService.getAllRoom())
                .build();
    }

    //get available room for session
    @GetMapping("/get-available-room/{session_id}")
    public ApiResponse<List<RoomResponse>> getAvailableRoom(@PathVariable("session_id") String session_id) {
        return ApiResponse.<List<RoomResponse>>builder()
                .result(roomService.getAvailableRoomForSession(session_id))
                .build();
    }

    //get available room for schedule creatation
    @GetMapping("/get-available-room-for-schedule")
    public ApiResponse<List<RoomResponse>> getAvailableRoomForSchedule(@RequestParam String time_slot_id) {
        return ApiResponse.<List<RoomResponse>>builder()
                .result(roomService.getAvailableRoomForSchedule(time_slot_id))
                .build();
    }

    //get a room by id
    @GetMapping("/get-room/{roomId}")
    public ApiResponse<RoomResponse> getRoom(@PathVariable("roomId") String roomId) {
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.getRoom(roomId))
                .build();
    }

    //update one room
    @PostMapping("/update-room/{roomId}")
    public ApiResponse<RoomResponse> updateRoom(@PathVariable("roomId") String roomId, @RequestBody RoomRequest request) {
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.updateRoom(roomId, request))
                .build();
    }

    //delete room
    @DeleteMapping("/delete-room/{roomId}")
    public ApiResponse<?> deleteRoom(@PathVariable("roomId") String roomId) {
        roomService.deleteRoom(roomId);
        return ApiResponse.builder()
                .message("Delete room successfully!")
                .build();
    }

    /**********************************
     * Manage Exam Type
     **********************************/
    //create exam type
    @PostMapping("/create-exam-type")
    public ApiResponse<ExamTypeResponse> createExamType(@RequestBody ExamTypeRequest request) {
        return ApiResponse.<ExamTypeResponse>builder()
                .result(examTypeService.createExamType(request))
                .message("Create a new exam type rate successfully")
                .build();
    }

    //get all exam type
    @GetMapping("/get-all-exam-type")
    public ApiResponse<List<ExamTypeResponse>> getAllExamType() {
        return ApiResponse.<List<ExamTypeResponse>>builder()
                .result(examTypeService.getAllExamType())
                .build();
    }

    //get exam type by examType
    @GetMapping("/get-exam-type")
    public ApiResponse<ExamTypeResponse> getExamType(@RequestParam int examType) {
        return ApiResponse.<ExamTypeResponse>builder()
                .result(examTypeService.getExamType(examType))
                .build();
    }

    //update exam type
    @PostMapping("/update-exam-type")
    public ApiResponse<ExamTypeResponse> updateExamType(@RequestParam int examType, @RequestBody ExamTypeRequest request) {
        return ApiResponse.<ExamTypeResponse>builder()
                .result(examTypeService.updateExamType(examType, request))
                .message("Update exam type successfully!")
                .build();
    }

    //delete exam type
    @DeleteMapping("/delete-exam-type")
    public ApiResponse<?> deleteExamType(@RequestParam int examType) {
        examTypeService.deleteExamType(examType);
        return ApiResponse.builder()
                .message("Delete exam type successfully!")
                .build();
    }

    /**********************************
     * Manage Exam
     **********************************/

    //create new exam
    @PostMapping("/create-exam")
    public ApiResponse<?> createExam(@RequestBody ExamRequest request) {
        examService.createExam(request);
        return ApiResponse.builder()
                .message("Create new exam successfully!")
                .build();
    }

    //get all exam
    @GetMapping("/get-all-exam")
    public ApiResponse<List<ExamResponse>> getAllExam() {
        return ApiResponse.<List<ExamResponse>>builder()
                .result(examService.getAllExam())
                .build();
    }

    //get exam by id
    @GetMapping("/get-exam/{exam_id}")
    public ApiResponse<ExamResponse> getExam(@PathVariable("exam_id") String exam_id) {
        return ApiResponse.<ExamResponse>builder()
                .result(examService.getExam(exam_id))
                .build();
    }

    //update
    @PostMapping("/update-exam/{exam_id}")
    public ApiResponse<ExamResponse> updateExam(@PathVariable("exam_id") String exam_id, @RequestBody ExamUpdateRequest request) {
        return ApiResponse.<ExamResponse>builder()
                .result(examService.updateExam(exam_id, request))
                .message("Update exam successfully!")
                .build();
    }

    //delete
    @DeleteMapping("/delete-exam/{exam_id}")
    public ApiResponse<?> deleteExam(@PathVariable("exam_id") String exam_id) {
        examService.deleteExam(exam_id);
        return ApiResponse.builder()
                .message("Delete exam successfully!")
                .build();
    }

    //upload exam
//    @PostMapping("/upload-exams")
//    public ResponseEntity<ApiResponse<String>> uploadFileExam(@RequestParam("file") MultipartFile file) {
//        if (excelHelper.hasExcelFormat(file)) {
//            try {
//                List<ExamEntity> examEntityList = excelHelper.excelToExams(file.getInputStream());
//                examService.saveAll(examEntityList);
//                return ResponseEntity.ok(ApiResponse.<String>builder().
//                        message("File uploaded and exams added successfully.")
//                        .build());
//            } catch (Exception e) {
//                return ResponseEntity.status(500).body(ApiResponse.<String>builder()
//                        .message("Failed to parse file.")
//                        .build());
//            }
//        }
//        return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
//                .message("Please upload an Excel file.")
//                .build());
//    }

    /**********************************
     * Manage Event
     **********************************/

    //get all
    @GetMapping("/event")
    public ApiResponse<Page<EventResponse>> getAllEvent(@RequestParam int page, @RequestParam int size) {
        Page<EventResponse> eventEntityList = eventService.getEvents(page, size);
        return ApiResponse.<Page<EventResponse>>builder()
                .result(eventEntityList)
                .build();
    }

    //create new batch
    @PostMapping("/create-event")
    public ApiResponse<EventResponse> createEvnet(@RequestBody @Validated EventCreateRequest request) {
        eventService.createEvent(request);
        return ApiResponse.<EventResponse>builder()
                .message("A new event have been created!")
                .build();
    }

    // getEventById
    @GetMapping("/get-event")
    public ApiResponse<EventResponse> getEventById(@RequestParam String eventId) {
        EventResponse eventEntity = eventService.getEventById(eventId);
        return ApiResponse.<EventResponse>builder()
                .result(eventEntity)
                .build();
    }

    // search and paginate
    @GetMapping("/search-event")
    public ApiResponse<Page<EventResponse>> getEventByName(@RequestParam String name, @RequestParam int page, @RequestParam int size) {
        Page<EventResponse> eventEntityList = eventService.findEventsByName(name, page, size);
        return ApiResponse.<Page<EventResponse>>builder()
                .result(eventEntityList)
                .build();
    }

    //delete
    @DeleteMapping("/delete-event/{event_id}")
    public ApiResponse<?> deleteEvent(@PathVariable("event_id") String event_id) {
        eventService.deleteEvent(event_id);
        return ApiResponse.builder()
                .message("Delete event successfully!")
                .build();
    }

    @PostMapping(value = "/update-event/{event_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<EventResponse> updateEvent(
            @PathVariable("event_id") String eventId,
            @RequestPart("eventDetail") EventUpdateRequest eventRequest, // Nhận JSON payload
            @RequestPart(value = "image", required = false) MultipartFile image) { // Nhận file ảnh

        // Xử lý update
        EventResponse eventResponse = eventService.updateEventById(eventId, eventRequest, image);

        return ApiResponse.<EventResponse>builder()
                .result(eventResponse)
                .message("Update event successfully!")
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
     * Manage Session
     **********************************/
    //create a class
    @PostMapping("/create-session")
    public ApiResponse<?> createSession(@RequestBody SessionCreattionRequest request) {
        sessionService.createSession(request);
        return ApiResponse.builder()
                .message("Create new session successfully!")
                .build();
    }

    //create a schedule
    @PostMapping("/create-schedule/{class_id}")
    public ApiResponse<?> createSchedule(@PathVariable("class_id") String class_id, @RequestBody ScheduleCreationRequest request) {
        sessionService.createSchedule(class_id, request);
        attendanceService.createAttendancesForSession(class_id);
        markService.generateMarkForAllStudentInClass(class_id);
        return ApiResponse.builder()
                .message("Create new schedule successfully!")
                .build();
    }

    //get all session
    @GetMapping("/get-all-session")
    public ApiResponse<Page<SessionResponse>> getAllSession(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<SessionResponse>>builder()
                .result(sessionService.getAllSession(page, size))
                .build();
    }

    @GetMapping("/get-session-week")
    public ApiResponse<List<SessionResponse>> getSessionByWeek(@RequestParam int week, @RequestParam String class_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result(sessionService.getSessionByClassAndWeek(week, class_id))
                .build();
    }

    @GetMapping("/get-session-by-teacher")
    public ApiResponse<List<SessionResponse>> getSessionByTeacher(@RequestParam String teacher_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByClassAndTeacher(teacher_id) )
                .build();
    }

    //get session by student
    @GetMapping("/get-session-by-student")
    public ApiResponse<List<SessionResponse>> getSessionByStudent(@RequestParam String student_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByStudentId(student_id) )
                .build();
    }

    //get session which have exam in a class
    @GetMapping("/get-session-have-exam")
    public ApiResponse<List<SessionResponse>> getSessionHaveExam(@RequestParam String class_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByExamNotNull(class_id) )
                .build();
    }

    //get session which have exam in a class by teacher
    @GetMapping("/get-session-have-exam-and-teacher")
    public ApiResponse<List<SessionResponse>> getSessionHaveExamAndTeacher(@RequestParam String class_id,
                                                                           @RequestParam String teacher_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByExamNotNullAndTeacherId( class_id, teacher_id ) )
                .build();
    }

    //get session by attendance status
    @GetMapping("/get-session-by-attendance-status")
    public ApiResponse<List<SessionResponse>> getSessionByAttendanceStatus(@RequestParam String class_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByAttendanceStatus( class_id ) )
                .build();
    }

    //get session by id
    @GetMapping("/get-session/{session_id}")
    public ApiResponse<SessionResponse> getSessionById(@PathVariable("session_id") String session_id) {
        return ApiResponse.<SessionResponse>builder()
                .result(sessionService.getSession(session_id))
                .build();
    }

    //get available teacher for session
    @GetMapping("/get-available-teacher/{session_id}")
    public ApiResponse<List<UserInforResponse>> getAvailableTeacher(@PathVariable("session_id") String session_id) {
        return ApiResponse.<List<UserInforResponse>>builder()
                .result(sessionService.getAvailableTeachers(session_id))
                .build();
    }

    //get unavailable session
    @GetMapping("/get-unavailable-session")
    public ApiResponse<List<SessionResponse>> getUnavailableSesssion(@RequestParam String class_id,
                                                                     @RequestParam int sessionWeek) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result(sessionService.getSessionUnavailable(class_id, sessionWeek))
                .build();
    }

    //update session status only
    @PostMapping("/update-session-status/{session_id}")
    public ApiResponse<?> updateSessionStatus(@PathVariable("session_id") String session_id) {
        sessionService.updateOnlySessionStatus(session_id);
        return ApiResponse.builder()
                .message("Session status updated!")
                .build();
    }

    @PostMapping("/update-session-attendance-status/{session_id}")
    public ApiResponse<?> updateSessionAttendanceStatus(@PathVariable("session_id") String session_id, @RequestParam String new_status) {
        sessionService.updateSessionAttendanceStatus(session_id, new_status);
        return ApiResponse.builder()
                .message("Session attendance status updated!")
                .build();
    }

    @PostMapping("/update-session-mark-status/{session_id}")
    public ApiResponse<?> updateSessionMarkStatus(@PathVariable("session_id") String session_id, @RequestParam String new_status) {
        sessionService.updateSessionMarkStatus(session_id, new_status);
        return ApiResponse.builder()
                .message("Session mark status updated!")
                .build();
    }

    //update
    @PostMapping("/update-session/{session_id}")
    public ApiResponse<SessionResponse> updateSessionById(@PathVariable("session_id") String session_id, @RequestBody SessionCreattionRequest request) {
        return ApiResponse.<SessionResponse>builder()
                .result(sessionService.updateSession(session_id, request))
                .build();
    }

    //delete schedule
    @DeleteMapping("/delete-schedule/{class_id}")
    public ApiResponse<?> deleteSchedule(@PathVariable("class_id") String class_id) {
        sessionService.deleteSchedule(class_id);
        return ApiResponse.builder()
                .message("Delete schedule successfully!")
                .build();
    }

    //delete
    @DeleteMapping("/delete-session/{session_id}")
    public ApiResponse<SessionResponse> deleteSessionById(@PathVariable("session_id") String session_id) {
        sessionService.deleteSession(session_id);
        return ApiResponse.<SessionResponse>builder()
                .message("Delete session successfully!")
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
     * Manage Holiday
     **********************************/

    @GetMapping("/get-all-holiday")
    public ApiResponse<Page<HolidayResponse>> getAllHoliday(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<HolidayResponse>>builder()
                .result(holidayService.getAllHoliday(page, size))
                .build();
    }

    @PostMapping("/create-holiday")
    public ApiResponse<HolidayResponse> createHoliday(@RequestBody @Valid HolidayRequest request) {
        return ApiResponse.<HolidayResponse>builder()
                .result(holidayService.createHoliday(request))
                .message("Create holiday successfully!")
                .build();
    }

    //update holiday
    @PostMapping("/update-holiday/{holidayId}")
    public ApiResponse<HolidayResponse> updateHoliday(@PathVariable("holidayId") String holidayId, @RequestBody HolidayRequest request) {
        return ApiResponse.<HolidayResponse>builder()
                .result(holidayService.updateHoliday(holidayId, request))
                .build();
    }

    //delete holiday
    @DeleteMapping("/delete-holiday/{holidayId}")
    public ApiResponse<?> deleteHoliday(@PathVariable("holidayId") String holidayId) {
        holidayService.deleteHoliday(holidayId);
        return ApiResponse.builder()
                .message("Delete holiday successfully!")
                .build();
    }

    // get holiday by id
    @GetMapping("/get-holiday")
    public ApiResponse<HolidayEntity> getHolidayById(@RequestParam String holidayId) {
        HolidayEntity holidayEntity = holidayService.getHolidaytById(holidayId);
        return ApiResponse.<HolidayEntity>builder()
                .result(holidayEntity)
                .build();
    }

    /**********************************
     * Manage Curriculumn
     **********************************/
    //upload curriculumn
    @PostMapping("/upload-curriculumn")
    public ResponseEntity<ApiResponse<String>> uploadFileCurriculumn(@RequestParam("file") MultipartFile file) {
        if (!excelHelper.hasExcelFormat(file)) {
            // Trả về lỗi khi định dạng file không phải Excel
            return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                    .message(ErrorCode.PARSE_ERROR.getMessage())
                    .build());
        }
        try {
            Map<String, List<?>> data = excelHelper.excelToLessonsAndExams(file.getInputStream());

            List<LessonEntity> lessons = (List<LessonEntity>) data.get("lessons");
            if (lessons != null && !lessons.isEmpty()) {
                lessonService.saveAll(lessons);
            }

            List<ExamEntity> exams = (List<ExamEntity>) data.get("exams");
            if (exams != null && !exams.isEmpty()) {
                examService.saveAll(exams);
            }

            List<CurriculumnEntity> curriculumns = (List<CurriculumnEntity>) data.get("curriculumns");
            if (curriculumns != null && !curriculumns.isEmpty()) {
                curriculumnService.createCurriculumn(curriculumns);
            }

            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .message("File uploaded and data added successfully.")
                    .build());
        } catch (AppException e) {
            return ResponseEntity.status(400).body(ApiResponse.<String>builder()
                    .message(e.getMessage())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<String>builder()
                    .message("An unexpected error occurred: " + e.getMessage())
                    .build());
        }
    }

    //get all attendance
    @GetMapping("/get-all-curriculumn")
    public ApiResponse<List<CurriculumnResponse>> getAllCurriculumn() {
        return ApiResponse.<List<CurriculumnResponse>>builder()
                .result(curriculumnService.getAllCurriculumn())
                .build();
    }

    //get by curriculumn list id
    @GetMapping("/get-by-curriculumn-list/{curriculumn_list_id}")
    public ApiResponse<Page<CurriculumnResponse>> getByCurriculumnListId(@PathVariable("curriculumn_list_id") String curriculumn_list_id,
                                                                         @RequestParam int page,
                                                                         @RequestParam int size) {
        return ApiResponse.<Page<CurriculumnResponse>>builder()
                .result(curriculumnService.getCurriculumnByCurriculumnListId(curriculumn_list_id, page, size))
                .build();
    }

    //get a curriculumn by id
    @GetMapping("/get-curriculumn/{curriculumn_id}")
    public ApiResponse<CurriculumnResponse> getCurriculumn(@PathVariable("curriculumn_id") String curriculumn_id) {
        return ApiResponse.<CurriculumnResponse>builder()
                .result(curriculumnService.getCurriculumn(curriculumn_id))
                .build();
    }

    //update a curriculumn
    @PostMapping("/update-curriculumn/{curriculumn_id}")
    public ApiResponse<CurriculumnResponse> updateCurriculumn(@PathVariable("curriculumn_id") String curriculumn_id, @RequestBody CurriculumnRequest request) {
        return ApiResponse.<CurriculumnResponse>builder()
                .result(curriculumnService.updateCurriculumn(curriculumn_id, request))
                .build();
    }

    //delete
    @DeleteMapping("/delete-curriculumn/{curriculumn_id}")
    public ApiResponse<?> deleteCurriculumn(@PathVariable("curriculumn_id") String curriculumn_id) {
        curriculumnService.deleteCurriculumn(curriculumn_id);
        return ApiResponse.builder()
                .message("Delete curriculumn successfully!")
                .build();
    }

    /**********************************
     * Manage Curriculumn List
     **********************************/
    //create curriculumn list
    @PostMapping("/create-curriculumn-list")
    public ApiResponse<?> createCurriculumnList(@RequestBody @Valid CurriculumnListRequest request) {
        curriculumnListService.createCurriculumnList(request);
        return ApiResponse.<NewsResponse>builder()
                .message("Create new curriculumn list successfully!")
                .build();
    }

    //get all
    @GetMapping("/get-all-curriculumn-list")
    public ApiResponse<List<CurriculumnListResponse>> getAllCurriculumnList(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<List<CurriculumnListResponse>>builder()
                .result(curriculumnListService.getAllCurriculumnList())
                .build();
    }

    //get a currilumn list
    @GetMapping("/get-curriculumn-list/{id}")
    public ApiResponse<CurriculumnListResponse> getCurriculumnList(@PathVariable("id") int id) {
        return ApiResponse.<CurriculumnListResponse>builder()
                .result(curriculumnListService.getCurriculumnList(id))
                .build();
    }

    //update
    @PostMapping("/update-curriculumn-list/{id}")
    public ApiResponse<?> updateCurriculumnList(@PathVariable("id") int id, @RequestBody @Valid CurriculumnListRequest request) {
        curriculumnListService.updateCurriculumnList(id, request);
        return ApiResponse.<NewsResponse>builder()
                .message("Update curriculumn list successfully!")
                .build();
    }

    //delete
    @DeleteMapping("/delete-curriculumn-list/{id}")
    public ApiResponse<?> deleteCurriculumnList(@PathVariable("id") int id) {
        curriculumnListService.deleteCurriculumnList(id);
        return ApiResponse.builder()
                .message("Delete curriculumn list successfully!")
                .build();
    }

    //get exam by studentId
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<ExamResponseForMark>> getExamsByStudent(
            @PathVariable String studentId,
            @RequestParam int page,
            @RequestParam int size) {
        Page<ExamResponseForMark> exams = examService.getExamsByStudent(studentId, page, size);
        return ResponseEntity.ok(exams);
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
        markService.countAverageMark(student_id);
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

}
