package com.nsg.service;

import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.dto.request.event.EventUpdateRequest;
import com.nsg.dto.response.event.EventResponse;
import com.nsg.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface EventService {
    //get all lesson
    List<EventResponse> getAllEvent();

    Page<EventResponse> getEvents(int page, int size);

    //create new lesson
    void createEvent(EventCreateRequest request);

    EventResponse getEventById(String eventId);

    Page<EventResponse> findEventsByName(String eventName,int page, int size);

    void deleteEvent(String examId);

    EventResponse updateEventById(String eventId, EventUpdateRequest request, MultipartFile image);

    List<EventResponse> findEventsOfOneClassInSchedule(String classId);

    List<EventResponse> findAvailableEvent(String classId);
}

