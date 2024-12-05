package com.nsg.controller;

import com.nsg.dto.request.timeSlot.TimeSlotCreationRequest;
import com.nsg.dto.request.timeSlot.TimeSlotUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.timeSlot.TimeSlotResponse;
import com.nsg.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class TimeSlotController {
    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    RoomService roomService;

    @Autowired
    EventService eventService;

    @Autowired
    SessionService sessionService;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    MarkService markService;

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
}
