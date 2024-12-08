package com.nsg.controller;

import com.nsg.common.utils.ExcelHelper;
import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.dto.request.event.EventUpdateRequest;
import com.nsg.dto.request.eventFeedback.EventFeedbackCreattionRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.event.EventResponse;
import com.nsg.dto.response.eventFeedback.EventFeedbackResponse;
import com.nsg.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class EventController {
    @Autowired
    EventService eventService;

    @Autowired
    EventFeedbackService eventFeedbackService;

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

    //get all event of one class in schedule
    @GetMapping("/get-events-of-one-class")
    public ApiResponse<List<EventResponse>> getEventOfOneClass(@RequestParam String class_id) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.findEventsOfOneClassInSchedule(class_id))
                .build();
    }

    /**********************************
     * Manage Event Feedback
     **********************************/
    //create new event feedback
    @PostMapping("/create-event-feedback")
    public ApiResponse<?> createEventFeedback(@RequestBody @Valid EventFeedbackCreattionRequest request) {
        eventFeedbackService.createEventFeedback(request);
        return ApiResponse.builder()
                .message("Create new event feedback successfully!")
                .build();
    }


    //get all
    @GetMapping("/get-all-event-feedback")
    public ApiResponse<Page<EventFeedbackResponse>> getAllEventFeedback(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<EventFeedbackResponse>>builder()
                .result( eventFeedbackService.getAllEventFeedback(page, size) )
                .build();
    }

    //get one
    @GetMapping("/get-event-feedback")
    public ApiResponse<EventFeedbackResponse> getEventFeedback(@RequestParam String event_feedback_id) {
        return ApiResponse.<EventFeedbackResponse>builder()
                .result( eventFeedbackService.getEventFeedback(event_feedback_id) )
                .build();
    }

    //get by event id
    @GetMapping("/get-event-feedback-by-event")
    public ApiResponse<Page<EventFeedbackResponse>> getEventFeedbackByEvent(@RequestParam String event_id,
                                                                            @RequestParam int page,
                                                                            @RequestParam int size) {
        return ApiResponse.<Page<EventFeedbackResponse>>builder()
                .result( eventFeedbackService.getEventFeedbackOfOneEvent(event_id, page, size) )
                .build();
    }

    //get by student id and event id
    @GetMapping("/get-event-feedback-by-student")
    public ApiResponse<EventFeedbackResponse> getEventFeedbackByStudent(@RequestParam String student_id,
                                                                        @RequestParam String event_id) {
        return ApiResponse.<EventFeedbackResponse>builder()
                .result( eventFeedbackService.getEventFeedbackOfOneStudent( student_id, event_id) )
                .build();
    }

    //update
    @PostMapping("/update-event-feedback")
    public ApiResponse<EventFeedbackResponse> updateEventFeedback(@RequestParam String event_feedback_id,
                                                                  @RequestBody @Valid EventFeedbackCreattionRequest request) {
        eventFeedbackService.updateEventFeedback(event_feedback_id, request);
        return ApiResponse.<EventFeedbackResponse>builder()
                .message("Update event feedback successfully!")
                .build();
    }

    //delete
    @DeleteMapping("/delete-event-feedback")
    public ApiResponse<?> deleteEventFeedback(@RequestParam String event_feedback_id) {
        eventFeedbackService.deleteEventFeedback(event_feedback_id);
        return ApiResponse.builder()
                .message("Delete event feedback successfully!")
                .build();
    }

    //calculate avgRate of one event
    @GetMapping("/calculate-avg-rate-event")
    public ApiResponse<?> calculateEventFeedbackAvgRate(@RequestParam String event_id) {
        eventFeedbackService.calculateAvgRateOfOneEvent(event_id);
        return ApiResponse.builder()
                .message("Average event feedback rate have been calculated!")
                .build();
    }

}
