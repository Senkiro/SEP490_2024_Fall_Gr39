package com.nsg.controller;

import com.nsg.common.utils.ExcelHelper;
import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.dto.request.event.EventUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.event.EventResponse;
import com.nsg.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class EventController {
    @Autowired
    EventService eventService;

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
}
