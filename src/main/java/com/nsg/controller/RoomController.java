package com.nsg.controller;

import com.nsg.dto.request.room.RoomRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.room.RoomResponse;
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
public class RoomController {
    @Autowired
    RoomService roomService;

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
}
