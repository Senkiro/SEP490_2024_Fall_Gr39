package com.nsg.controller;

import com.nsg.common.enums.UserRole;
import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.dto.request.exam.ExamTypeRequest;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.request.room.RoomRequest;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.timeSlot.TimeSlotCreationRequest;
import com.nsg.dto.request.timeSlot.TimeSlotUpdateRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.exam.ExamTypeResponse;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.dto.response.room.RoomResponse;
import com.nsg.dto.response.staff.StudentResponse;
import com.nsg.dto.response.timeSlot.TimeSlotResponse;
import com.nsg.entity.*;
import com.nsg.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    EventService eventService;


    /**********************************
     * Manage Student
     **********************************/
    //create new student
    @PostMapping("/create-student")
    public ApiResponse<StudentEntity> createStudent(@RequestParam String batch_name, @RequestBody @Valid StudentCreattionRequest request){
        return ApiResponse.<StudentEntity>builder()
                .result(studentService.createStudent(request, batch_name))
                .message("Create student successfully!")
                .build();
    }

    @GetMapping("/student-list")
    public ApiResponse<Page<StudentResponse>> viewStudents(@RequestParam int page, @RequestParam int size){
        Page<StudentResponse> studentList = studentService.getAllStudent(page, size);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    //get student list by batchName
    @GetMapping("/get-student-by-batch")
    public ApiResponse<Page<StudentResponse>> viewStudentByBatchName(@RequestParam int page, @RequestParam int size, @RequestParam String batch_name){
        Page<StudentResponse> studentList = studentService.getStudentByBatchName(page, size, batch_name);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    @DeleteMapping("/delete-student/{student_id}")
    public ApiResponse<?> deleteStudent(@PathVariable("student_id") String student_id){
        userService.deleteUser(student_id);
        return ApiResponse.builder()
                .message("Delete successful")
                .build();
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
     * Manage Evnet
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

}
