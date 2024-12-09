package com.nsg.controller;

import com.nsg.dto.request.mark.MarkCreationRequest;
import com.nsg.dto.request.mark.MarkUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.mark.MarkResponse;
import com.nsg.service.MarkService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class MarkController {
    @Autowired
    MarkService markService;

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

    //update many marks at the same request
    @PostMapping("/update-marks")
    public ApiResponse<List<MarkResponse>> updateMarks(@RequestBody @Valid List<MarkUpdateRequest> requests) {
        List<MarkResponse> updatedMarks = markService.updateMarks(requests);
        return ApiResponse.<List<MarkResponse>>builder()
                .result(updatedMarks)
                .message("Marks updated successfully!")
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

    //create mark for one student
    @PostMapping("/create-students-marks")
    public ApiResponse<?> createAllMarkForOneStudent(@RequestParam String studentId) {
        markService.createAllMarkForOneStudent(studentId);
        return ApiResponse.builder()
                .message("Marks for student create successfully!")
                .build();
    }


    //calculate marks of students
    @PostMapping("/calculate-students-marks")
    public ApiResponse<?> calculateMarks(@RequestParam String classId) {
        markService.calculateAllStudentsMarkInClass(classId);
        return ApiResponse.builder()
                .message("Marks for student have been calculated and updated!")
                .build();
    }

    //course summary and calculate all mark include participation mark
    @PostMapping("/course-summary")
    public ApiResponse<?> courseSummary(@RequestParam String classId) {
        markService.courseSummary(classId);
        return ApiResponse.builder()
                .message("Marks for student have been summary!")
                .build();
    }

}
