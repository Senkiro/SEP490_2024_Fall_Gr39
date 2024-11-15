package com.nsg.service.imp;

import com.nsg.Mapper.EventMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.entity.EventEntity;
import com.nsg.repository.EventRepository;
import com.nsg.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImp implements EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Override
    public List<EventEntity> getAllEvent() {
        return eventRepository.findAll();
    }

    @Override
    public Page<EventEntity> getEvents(int page, int size){
        return eventRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public void createEvent(EventCreateRequest request) {
        EventEntity event = eventMapper.toEventEntity(request);
        if(eventRepository.findByEventName(request.getEventName()).isEmpty()){
            eventRepository.save(event);
        }else {
            throw new AppException(ErrorCode.EVENT_EXISTED);
        }
    }

    @Override
    public EventEntity getEventById(String eventId) {
        return eventRepository.findByEventId(eventId).orElseThrow(
                () -> new AppException(ErrorCode.EVENT_NOT_EXIST)
        );
    }

    @Override
    public List<EventEntity> findEventsByName(String eventName) {
        List<EventEntity> events = eventRepository.findByEventName(eventName);
        if (events.isEmpty()) {
            throw new AppException(ErrorCode.EVENT_NOT_EXIST);
        }
        return events;
    }
}
