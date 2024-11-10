package com.nsg.service;

import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    //get all lesson
    List<EventEntity> getAllEvent();

    Page<EventEntity> getEvents(int page, int size);

    //create new lesson
    void createEvent(EventCreateRequest request);

    EventEntity getEventById(String eventId);

    EventEntity getEventByName(String eventName);
}

