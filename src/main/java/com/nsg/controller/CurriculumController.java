package com.nsg.controller;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.common.utils.ExcelHelper;
import com.nsg.dto.request.curriculumn.CurriculumnListRequest;
import com.nsg.dto.request.curriculumn.CurriculumnRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.curriculumn.CurriculumnListResponse;
import com.nsg.dto.response.curriculumn.CurriculumnResponse;
import com.nsg.dto.response.exam.ExamResponseForMark;
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
}
