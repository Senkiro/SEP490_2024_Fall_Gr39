package com.nsg.controller;

import com.nsg.dto.request.event.EventUpdateRequest;
import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.dto.request.holiday.HolidayRequest;
import com.nsg.dto.request.room.RoomRequest;
import com.nsg.dto.request.session.ScheduleCreationRequest;
import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.request.session.SessionUpdateRequest;
import com.nsg.dto.request.timeSlot.TimeSlotCreationRequest;
import com.nsg.dto.request.timeSlot.TimeSlotUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.event.EventResponse;
import com.nsg.dto.response.holiday.HolidayResponse;
import com.nsg.dto.response.room.RoomResponse;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.timeSlot.TimeSlotResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.*;
import com.nsg.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class ScheduleController {
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
     * Manage Session
     **********************************/
    //create a class
    @PostMapping("/create-session")
    public ApiResponse<?> createSession(@RequestBody SessionCreattionRequest request) {
        sessionService.createSession(request);
        return ApiResponse.builder()
                .message("Create new session successfully!")
                .build();
    }

    //create a schedule
    @PostMapping("/create-schedule/{class_id}")
    public ApiResponse<?> createSchedule(@PathVariable("class_id") String class_id, @RequestBody ScheduleCreationRequest request) {
        sessionService.createSchedule(class_id, request);
        attendanceService.createAttendancesForSession(class_id);
        markService.generateMarkForAllStudentInClass(class_id);
        return ApiResponse.builder()
                .message("Create new schedule successfully!")
                .build();
    }

    //get all session
    @GetMapping("/get-all-session")
    public ApiResponse<Page<SessionResponse>> getAllSession(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<SessionResponse>>builder()
                .result(sessionService.getAllSession(page, size))
                .build();
    }

    @GetMapping("/get-session-week")
    public ApiResponse<List<SessionResponse>> getSessionByWeek(@RequestParam int week, @RequestParam String class_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result(sessionService.getSessionByClassAndWeek(week, class_id))
                .build();
    }

    @GetMapping("/get-session-by-teacher")
    public ApiResponse<List<SessionResponse>> getSessionByTeacher(@RequestParam String teacher_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByClassAndTeacher(teacher_id) )
                .build();
    }

    //get session by student
    @GetMapping("/get-session-by-student")
    public ApiResponse<List<SessionResponse>> getSessionByStudent(@RequestParam String student_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByStudentId(student_id) )
                .build();
    }

    //get session which have exam in a class
    @GetMapping("/get-session-have-exam")
    public ApiResponse<List<SessionResponse>> getSessionHaveExam(@RequestParam String class_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByExamNotNull(class_id) )
                .build();
    }

    //get session which have exam in a class by teacher
    @GetMapping("/get-session-have-exam-and-teacher")
    public ApiResponse<List<SessionResponse>> getSessionHaveExamAndTeacher(@RequestParam String class_id,
                                                                           @RequestParam String teacher_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByExamNotNullAndTeacherId( class_id, teacher_id ) )
                .build();
    }

    //get session by attendance status
    @GetMapping("/get-session-by-attendance-status")
    public ApiResponse<List<SessionResponse>> getSessionByAttendanceStatus(@RequestParam String class_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByAttendanceStatus( class_id ) )
                .build();
    }

    //get session by id
    @GetMapping("/get-session/{session_id}")
    public ApiResponse<SessionResponse> getSessionById(@PathVariable("session_id") String session_id) {
        return ApiResponse.<SessionResponse>builder()
                .result(sessionService.getSession(session_id))
                .build();
    }

    //get available teacher for session
    @GetMapping("/get-available-teacher/{session_id}")
    public ApiResponse<List<UserInforResponse>> getAvailableTeacher(@PathVariable("session_id") String session_id) {
        return ApiResponse.<List<UserInforResponse>>builder()
                .result(sessionService.getAvailableTeachers(session_id))
                .build();
    }

    //get unavailable session
    @GetMapping("/get-unavailable-session")
    public ApiResponse<List<SessionResponse>> getUnavailableSesssion(@RequestParam String class_id,
                                                                     @RequestParam int sessionWeek) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result(sessionService.getSessionUnavailable(class_id, sessionWeek))
                .build();
    }

    //update session status only
    @PostMapping("/update-session-status/{session_id}")
    public ApiResponse<?> updateSessionStatus(@PathVariable("session_id") String session_id) {
        sessionService.updateOnlySessionStatus(session_id);
        return ApiResponse.builder()
                .message("Session status updated!")
                .build();
    }

    @PostMapping("/update-session-attendance-status/{session_id}")
    public ApiResponse<?> updateSessionAttendanceStatus(@PathVariable("session_id") String session_id, @RequestParam String new_status) {
        sessionService.updateSessionAttendanceStatus(session_id, new_status);
        return ApiResponse.builder()
                .message("Session attendance status updated!")
                .build();
    }

    @PostMapping("/update-session-mark-status/{session_id}")
    public ApiResponse<?> updateSessionMarkStatus(@PathVariable("session_id") String session_id, @RequestParam String new_status) {
        sessionService.updateSessionMarkStatus(session_id, new_status);
        return ApiResponse.builder()
                .message("Session mark status updated!")
                .build();
    }

    //update
    @PostMapping("/update-session/{session_id}")
    public ApiResponse<SessionResponse> updateSessionById(@PathVariable("session_id") String session_id, @RequestBody SessionUpdateRequest request) {
        return ApiResponse.<SessionResponse>builder()
                .result(sessionService.updateSession(session_id, request))
                .build();
    }

    //delete schedule
    @DeleteMapping("/delete-schedule/{class_id}")
    public ApiResponse<?> deleteSchedule(@PathVariable("class_id") String class_id) {
        sessionService.deleteSchedule(class_id);
        return ApiResponse.builder()
                .message("Delete schedule successfully!")
                .build();
    }

    //delete
    @DeleteMapping("/delete-session/{session_id}")
    public ApiResponse<SessionResponse> deleteSessionById(@PathVariable("session_id") String session_id) {
        sessionService.deleteSession(session_id);
        return ApiResponse.<SessionResponse>builder()
                .message("Delete session successfully!")
                .build();
    }

    //swap session
    @PostMapping("/swap-to-unavailable-session")
    public ApiResponse<?> swapToUnavailableSesssion(@RequestParam String currentSessionId,
                                                    @RequestParam String toSessionId) {
        sessionService.swapToUnavailableSession(currentSessionId, toSessionId);
        return ApiResponse.builder()
                .message("Change session successfully!")
                .build();
    }

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

    //get available room for session
    @GetMapping("/get-available-room/{session_id}")
    public ApiResponse<List<RoomResponse>> getAvailableRoom(@PathVariable("session_id") String session_id) {
        return ApiResponse.<List<RoomResponse>>builder()
                .result(roomService.getAvailableRoomForSession(session_id))
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
