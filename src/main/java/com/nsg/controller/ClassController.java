package com.nsg.controller;

import com.nsg.dto.request.classRequest.ClassRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.service.ClassService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class ClassController {
    @Autowired
    ClassService classService;

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
    @DeleteMapping("/delete-class/{class_id}")
    public ApiResponse<?> deleteClass(@PathVariable("class_id") String class_id) {
        classService.deleteClass(class_id);
        return ApiResponse.builder()
                .message("Delete class successfully!")
                .build();
    }

    //change class status
    @GetMapping("/change-class-status")
    public ApiResponse<?> changeClassStatus(@RequestParam String class_id) {
        classService.changeClassStatus(class_id);
        return ApiResponse.builder()
                .message("Change class's status successfully!")
                .build();
    }
}
