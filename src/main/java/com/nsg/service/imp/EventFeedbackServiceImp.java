package com.nsg.service.imp;

import com.nsg.Mapper.EventMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.eventFeedback.EventFeedbackCreattionRequest;
import com.nsg.dto.response.event.EventResponse;
import com.nsg.dto.response.eventFeedback.EventFeedbackResponse;
import com.nsg.entity.EventEntity;
import com.nsg.entity.EventFeedbackEntity;
import com.nsg.entity.SessionEntity;
import com.nsg.entity.StudentEntity;
import com.nsg.repository.EventFeedbackRepository;
import com.nsg.repository.EventRepository;
import com.nsg.repository.SessionRepository;
import com.nsg.repository.StudentRepository;
import com.nsg.service.EventFeedbackService;
import com.nsg.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventFeedbackServiceImp implements EventFeedbackService {

    @Autowired
    EventFeedbackRepository eventFeedbackRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    StudentService studentService;

    @Override
    public void createEventFeedback(EventFeedbackCreattionRequest request) {

        //check existed
        if (eventFeedbackRepository.existsByStudentEntityStudentIdAndEventEntityEventId( request.getStudentId(), request.getEventId() )) {
            throw new AppException(ErrorCode.EVENT_FEED_BACK_EXISTED);
        }

        EventFeedbackEntity eventFeedbackEntity = new EventFeedbackEntity();

        eventFeedbackEntity.setFeedbackRate( request.getFeedbackRate() );
        eventFeedbackEntity.setFeedbackContent( request.getFeedbackContent() );

        StudentEntity student = studentRepository.findById(request.getStudentId()).orElseThrow(
                () -> new AppException(ErrorCode.STUDENT_NOT_FOUND)
        );
        eventFeedbackEntity.setStudentEntity(student);

        EventEntity event = eventRepository.findById( request.getEventId() ).orElseThrow(
                () -> new AppException(ErrorCode.EVENT_NOT_FOUND)
        );
        eventFeedbackEntity.setEventEntity(event);

        eventFeedbackRepository.save(eventFeedbackEntity);

    }

    @Override
    public Page<EventFeedbackResponse> getAllEventFeedback(int page, int size) {

        Page<EventFeedbackEntity> eventFeedbackEntities = eventFeedbackRepository.findAll(PageRequest.of(page, size));
        List<EventFeedbackResponse> eventFeedbackResponses = toEventFeedbackResponseList(eventFeedbackEntities.getContent());
        return new PageImpl<>(eventFeedbackResponses, eventFeedbackEntities.getPageable(), eventFeedbackEntities.getTotalElements());
    }

    public List<EventFeedbackResponse> toEventFeedbackResponseList(List<EventFeedbackEntity> eventFeedbackEntities) {
        List<EventFeedbackResponse> eventFeedbackResponses = new ArrayList<>();

        for (EventFeedbackEntity eventFeedbackEntity : eventFeedbackEntities) {
            EventFeedbackResponse eventResponse = toEventFeedbackResponse(eventFeedbackEntity);

            eventFeedbackResponses.add(eventResponse);
        }
        return eventFeedbackResponses;
    }

    public EventFeedbackResponse toEventFeedbackResponse(EventFeedbackEntity eventFeedbackEntity) {

        EventFeedbackResponse eventResponse = new EventFeedbackResponse();

        eventResponse.setEventFeedbackId( eventFeedbackEntity.getEventFeedbackId() );
        eventResponse.setFeedbackRate( eventFeedbackEntity.getFeedbackRate() );
        eventResponse.setFeedbackContent(eventFeedbackEntity.getFeedbackContent() );

        //event
        eventResponse.setEventResponse( EventMapper.INSTANCE.toEventResponse( eventFeedbackEntity.getEventEntity() ) );

        //student
        eventResponse.setStudentResponse( studentService.convertToStudentResponse( eventFeedbackEntity.getStudentEntity() ) );

        return eventResponse;

    }

    @Override
    public EventFeedbackResponse getEventFeedback(String eventFeedbackId) {

        EventFeedbackEntity eventFeedbackEntity = eventFeedbackRepository.findById(eventFeedbackId).orElseThrow(
                () -> new AppException(ErrorCode.EVENT_FEED_BACK_NOT_FOUND)
        );

        return toEventFeedbackResponse(eventFeedbackEntity);
    }

    @Override
    public Page<EventFeedbackResponse> getEventFeedbackOfOneEvent(String eventId, int page, int size) {
        Page<EventFeedbackEntity> eventFeedbackEntities = eventFeedbackRepository.findByEventEntityEventId(eventId, PageRequest.of(page, size));
        List<EventFeedbackResponse> eventFeedbackResponses = toEventFeedbackResponseList(eventFeedbackEntities.getContent());

        //calculate avg rate of event
        calculateAvgRateOfOneEvent(eventId);

        return new PageImpl<>(eventFeedbackResponses, eventFeedbackEntities.getPageable(), eventFeedbackEntities.getTotalElements());
    }

    @Override
    public EventFeedbackResponse getEventFeedbackOfOneStudent(String studentId, String eventId) {
        EventFeedbackEntity eventFeedback =
                eventFeedbackRepository.findByStudentEntityStudentIdAndEventEntityEventId(studentId, eventId);

        return toEventFeedbackResponse(eventFeedback);
    }

    @Override
    public EventFeedbackResponse updateEventFeedback(String eventFeedbackId, EventFeedbackCreattionRequest request) {

        EventFeedbackEntity eventFeedbackEntity = eventFeedbackRepository.findById(eventFeedbackId).orElseThrow(
                () -> new AppException(ErrorCode.EVENT_FEED_BACK_NOT_FOUND)
        );

        eventFeedbackEntity.setFeedbackRate(request.getFeedbackRate() );
        if (request.getFeedbackContent() == null || request.getFeedbackContent().isEmpty()) {
            eventFeedbackEntity.setFeedbackContent( null );
        } else {
            eventFeedbackEntity.setFeedbackContent( request.getFeedbackContent() );
        }

        if ( request.getStudentId() == null || request.getStudentId().isEmpty() ) {
            eventFeedbackEntity.setStudentEntity(null);
        } else {
            StudentEntity student = studentRepository.findById(request.getStudentId()).orElseThrow(
                    () -> new AppException(ErrorCode.STUDENT_NOT_FOUND)
            );
            eventFeedbackEntity.setStudentEntity(student);
        }

        if (request.getEventId() == null || request.getStudentId().isEmpty()) {
            eventFeedbackEntity.setEventEntity(null);
        } else {
            EventEntity event = eventRepository.findById( request.getEventId() ).orElseThrow(
                    () -> new AppException(ErrorCode.EVENT_NOT_FOUND)
            );
            eventFeedbackEntity.setEventEntity(event);
        }

        eventFeedbackRepository.save(eventFeedbackEntity);

        return getEventFeedback(eventFeedbackId);
    }

    @Override
    public void deleteEventFeedback(String eventFeedbackId) {
        eventFeedbackRepository.deleteById(eventFeedbackId);
    }

    //calculate avgRate of one event
    @Override
    public void calculateAvgRateOfOneEvent(String eventId) {
        //get event
        EventEntity event = eventRepository.findById(eventId).orElseThrow(
                () -> new AppException(ErrorCode.EVENT_NOT_FOUND)
        );

        //get event feedbacks by eventId
        List<EventFeedbackEntity> eventFeedbackEntities = eventFeedbackRepository.findByEventEntityEventId(eventId);

        float avgRate = 0.0F;

        //calculate avgRate
        for (EventFeedbackEntity eventFeedback : eventFeedbackEntities) {
            avgRate += eventFeedback.getFeedbackRate();
        }

        avgRate = avgRate / eventFeedbackEntities.size();

        //update event's avgRate
        event.setAvgRate(avgRate);

        eventRepository.save(event);

    }

}
