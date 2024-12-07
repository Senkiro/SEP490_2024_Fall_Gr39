package com.nsg.service;

import com.nsg.dto.request.eventFeedback.EventFeedbackCreattionRequest;
import com.nsg.dto.response.eventFeedback.EventFeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface EventFeedbackService {

    //create new
    void createEventFeedback(EventFeedbackCreattionRequest request);

    //get all
    Page<EventFeedbackResponse> getAllEventFeedback(int page, int size);

    //get one by id
    EventFeedbackResponse getEventFeedback(String eventFeedbackId);

    //get all event feedback of one event
    Page<EventFeedbackResponse> getEventFeedbackOfOneEvent(String eventId, int page, int size);

    //get all event feedback of one student
    Page<EventFeedbackResponse> getEventFeedbackOfOneStudent(String studentId, int page, int size);

    //update by id
    EventFeedbackResponse updateEventFeedback(String eventFeedbackId, EventFeedbackCreattionRequest request);

    //delete
    void deleteEventFeedback(String eventFeedbackId);

    void calculateAvgRateOfOneEvent(String eventId);
}
