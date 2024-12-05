package com.nsg.controller;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.common.utils.ExcelHelper;
import com.nsg.dto.request.curriculumn.CurriculumnListRequest;
import com.nsg.dto.request.curriculumn.CurriculumnRequest;
import com.nsg.dto.request.exam.ExamRequest;
import com.nsg.dto.request.exam.ExamTypeRequest;
import com.nsg.dto.request.exam.ExamUpdateRequest;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.curriculumn.CurriculumnListResponse;
import com.nsg.dto.response.curriculumn.CurriculumnResponse;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.exam.ExamResponseForMark;
import com.nsg.dto.response.exam.ExamTypeResponse;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.dto.response.news.NewsResponse;
import com.nsg.entity.*;
import com.nsg.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class CurriculumController {
    @Autowired
    LessonService lessonService;

    @Autowired
    ExamTypeService examTypeService;

    @Autowired
    ExamService examService;

    @Autowired
    private ExcelHelper excelHelper;

    @Autowired
    CurriculumnService curriculumnService;

    @Autowired
    CurriculumnListService curriculumnListService;

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
}
