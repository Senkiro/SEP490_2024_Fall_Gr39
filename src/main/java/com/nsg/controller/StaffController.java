package com.nsg.controller;

import com.nsg.common.enums.UserRole;
import com.nsg.common.utils.ExcelHelper;
import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.request.classRequest.ClassRequest;
import com.nsg.dto.request.exam.ExamRequest;
import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.dto.request.exam.ExamTypeRequest;
import com.nsg.dto.request.exam.ExamUpdateRequest;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.request.room.RoomRequest;
import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.timeSlot.TimeSlotCreationRequest;
import com.nsg.dto.request.timeSlot.TimeSlotUpdateRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.exam.ExamTypeResponse;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.dto.response.room.RoomResponse;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.dto.response.timeSlot.TimeSlotResponse;
import com.nsg.entity.*;
import com.nsg.repository.BatchRepository;
import com.nsg.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
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


    /**********************************
     * Manage Student
     **********************************/
    //create new student
    @PostMapping("/create-student")
    public ApiResponse<StudentResponse> createStudent(@RequestBody @Valid StudentCreattionRequest request){
        studentService.createStudent(request);
        return ApiResponse.<StudentResponse>builder()
                .message("Create student successfully!")
                .build();
    }

    @GetMapping("/student-list")
    public ApiResponse<Page<StudentResponse>> viewStudents(@RequestParam int page,
                                                           @RequestParam int size){
        Page<StudentResponse> studentList = studentService.getAllStudent(page, size);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    //get student list by batchName
    @GetMapping("/get-student-by-batch")
    public ApiResponse<Page<StudentResponse>> viewStudentByBatchName(@RequestParam int page,
                                                                     @RequestParam int size,
                                                                     @RequestParam String batch_name){
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
                                                                                 @RequestParam String class_name){
        Page<StudentResponse> studentList = studentService.getStudentByBatchNameAndClassName(page, size, batch_name, class_name);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    //get a student by studentId
    @GetMapping("/get-student/{student_id}")
    public ApiResponse<StudentResponse> getStudent(@PathVariable("student_id") String student_id){
        return ApiResponse.<StudentResponse>builder()
                .result(studentService.getStudent(student_id))
                .build();
    }

    @DeleteMapping("/delete-student/{student_id}")
    public ApiResponse<?> deleteStudent(@PathVariable("student_id") String student_id){
        userService.deleteUser(student_id);
        return ApiResponse.builder()
                .message("Delete successful")
                .build();
    }

    @PostMapping("/upload-students")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                ExcelHelper excelHelper = new ExcelHelper(batchRepository); // Inject batchRepository
                List<StudentEntity> students = ExcelHelper.excelToStudents(file.getInputStream());
                studentService.saveAll(students);
                return ResponseEntity.ok(ApiResponse.<String>builder().message("File uploaded and students added successfully.").build());
            } catch (Exception e) {
                return ResponseEntity.status(500).body(ApiResponse.<String>builder().message("Failed to parse file.").build());
            }
        }
        return ResponseEntity.badRequest().body(ApiResponse.<String>builder().message("Please upload an Excel file.").build());
    }

    /**********************************
     * Manage Batch
     **********************************/

    //get all batch
    @GetMapping("/batch")
    ApiResponse<Page<BatchEntity>> getAllBatch(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<BatchEntity>>builder()
                .code(1000)
                .result(batchService.getBatches(page, size))
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
    ApiResponse<BatchEntity> saveBatch(@RequestBody @Validated BatchCreationRequest request){
        batchService.saveBatch(request);
        BatchEntity batchEntity = batchService.getBatch(request.getBatchName());
        return ApiResponse.<BatchEntity>builder()
                .message("A new batch have been created!")
                .result(batchEntity)
                .build();
    }

    //update
    @PostMapping("/update-batch")
    ApiResponse<BatchEntity> updateBatch(@RequestBody BatchCreationRequest request){
        BatchEntity batch = batchService.updateBatch(request.getBatchName(), request);
        return ApiResponse.<BatchEntity>builder()
                .result(batch)
                .build();
    }

    //delete batch
    @DeleteMapping("/delete-batch/{batchName}")
    ApiResponse<?> deleteBatch(@PathVariable("batchName") String batchName){
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
    ApiResponse<Page<LessonEntity>> getAllLesson(@RequestParam int page, @RequestParam int size){
        Page<LessonEntity> lessonEntityList = lessonService.getLessons(page,size);
        return  ApiResponse.<Page<LessonEntity>>builder()
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
    ApiResponse<LessonResponse> getLesson(@PathVariable("lesson_id") String lesson_id){
        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.getLesson(lesson_id))
                .build();
    }

    //delete lesson
    @DeleteMapping("/lesson/{lessonId}")
    ApiResponse<?> deleteLesson(@PathVariable("lessonId") String lessonId){
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

    /**********************************
     * Manage Teacher
     **********************************/

    //get teacher paginate
    @GetMapping("/teacher")
    ApiResponse<Map<String, Object>> getAllTeacher(@RequestParam int page, @RequestParam int size) {
        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
        Page<UserEntity> teacherPage = teacherService.getTeachers(page, size);
        List<UserEntity> teacherList = teacherPage.getContent();
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
    public ApiResponse<?> createTeacher(@RequestBody @Valid @Validated UserCreationRequest request){
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
    public ApiResponse<TimeSlotResponse> createTimeSlot(@RequestBody @Valid TimeSlotCreationRequest request){
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
    public ApiResponse<RoomResponse> createRoom(@RequestBody @Valid RoomRequest request){
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.createRoom(request))
                .message("Create room successfully!")
                .build();
    }

    //get all room
    @GetMapping("/get-all-room")
    public ApiResponse<List<RoomResponse>> getAllRoom(){
        return ApiResponse.<List<RoomResponse>>builder()
                .result(roomService.getAllRoom())
                .build();
    }

    //get a room by id
    @GetMapping("/get-room/{roomId}")
    public ApiResponse<RoomResponse> getRoom(@PathVariable("roomId") String roomId){
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.getRoom(roomId))
                .build();
    }

    //update one room
    @PostMapping("/update-room/{roomId}")
    public ApiResponse<RoomResponse> updateRoom(@PathVariable("roomId") String roomId, @RequestBody RoomRequest request){
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.updateRoom(roomId, request))
                .build();
    }

    //delete room
    @DeleteMapping("/delete-room/{roomId}")
    public ApiResponse<?> deleteRoom(@PathVariable("roomId") String roomId){
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
    public ApiResponse<?> createExam(@RequestParam int exam_type, @RequestBody ExamRequest request) {
        examService.createExam(request, exam_type);
        return ApiResponse.builder()
                .message("Create new exam successfully!")
                .build();
    }

    //get all exam
    @GetMapping("/get-all-exam")
    public ApiResponse<List<ExamResponse>> getAllExam(){
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
    /**********************************
     * Manage Event
     **********************************/

    //get all
    @GetMapping("/event")
    public ApiResponse<Page<EventEntity>> getAllEvent(@RequestParam int page, @RequestParam int size){
        Page<EventEntity> eventEntityList = eventService.getEvents(page,size);
        return  ApiResponse.<Page<EventEntity>>builder()
                .result(eventEntityList)
                .build();
    }

    //create new batch
    @PostMapping("/create-event")
    public ApiResponse<EventEntity> createEvnet(@RequestBody @Validated EventCreateRequest request){
        eventService.createEvent(request);
        return ApiResponse.<EventEntity>builder()
                .message("A new event have been created!")
                .build();
    }

    // getEventById
    @GetMapping("/get-event/{eventId}")
    public ApiResponse<EventEntity> getEventById(@PathVariable("eventId") String eventId) {
        EventEntity eventEntity = eventService.getEventById(eventId);
        return ApiResponse.<EventEntity>builder()
                .result(eventEntity)
                .build();
    }

    // search
    @GetMapping("/search-event/{name}")
    public ApiResponse<List<EventEntity>> getEventByName(@PathVariable("name") String name) {
        List<EventEntity> eventEntityList = eventService.findEventsByName(name);
        return ApiResponse.<List<EventEntity>>builder()
                .result(eventEntityList)
                .build();
    }

    /**********************************
     * Manage Class
     **********************************/
    //create a class
    @PostMapping("/create-class")
    public ApiResponse<?> createClass(@RequestBody ClassRequest request) {
        classService.createClass(request);
        return ApiResponse.builder()
                .message("Create new class successfully!")
                .build();
    }

    //get all class
    @GetMapping("/get-all-class")
    public ApiResponse<Page<ClassResponse>> getAllClass(@RequestParam int page, @RequestParam int size){
        return ApiResponse.<Page<ClassResponse>>builder()
                .result(classService.getAllClass(page, size))
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

    //get all class
    @GetMapping("/get-all-session")
    public ApiResponse<Page<SessionResponse>> getAllSession(@RequestParam int page, @RequestParam int size){
        return ApiResponse.<Page<SessionResponse>>builder()
                .result(sessionService.getAllSession(page, size))
                .build();
    }

}
