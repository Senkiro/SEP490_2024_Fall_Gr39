package com.nsg.controller;

import com.nsg.dto.request.holiday.HolidayRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.holiday.HolidayResponse;
import com.nsg.entity.HolidayEntity;
import com.nsg.service.HolidayService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
public class HolidayController {
    @Autowired
    HolidayService holidayService;

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
}
