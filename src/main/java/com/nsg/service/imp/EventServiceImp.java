package com.nsg.service.imp;

import com.nsg.Mapper.EventMapper;
import com.nsg.Mapper.ExamTypeMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.dto.request.event.EventUpdateRequest;
import com.nsg.dto.response.event.EventResponse;
import com.nsg.entity.EventEntity;
import com.nsg.entity.SessionEntity;
import com.nsg.repository.EventRepository;
import com.nsg.repository.SessionRepository;
import com.nsg.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImp implements EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    SessionRepository sessionRepository;

    @Override
    public List<EventResponse> getAllEvent() {
        List<EventEntity> eventEntities = eventRepository.findAll();
        List<EventResponse> eventResponses = toEventResponseList(eventEntities);
        return eventResponses;
    }

    @Override
    public Page<EventResponse> getEvents(int page, int size){
        Page<EventEntity> eventEntities = eventRepository.findAll(PageRequest.of(page, size));
        List<EventResponse> eventResponses = toEventResponseList(eventEntities.getContent());
        return new PageImpl<>(eventResponses, eventEntities.getPageable(), eventEntities.getTotalElements());
    }

    public List<EventResponse> toEventResponseList(List<EventEntity> eventEntities) {
        List<EventResponse> eventResponses = new ArrayList<>();
        for (EventEntity eventEntity : eventEntities) {
            EventResponse eventResponse = toEventResponse(eventEntity);
            eventResponses.add(eventResponse);
        }
        return eventResponses;
    }

    public EventResponse toEventResponse(EventEntity eventEntity) {
        EventResponse eventResponse = new EventResponse();

        eventResponse.setEventId( eventEntity.getEventId() );
        eventResponse.setEventName( eventEntity.getEventName() );
        eventResponse.setAddress( eventEntity.getAddress() );
        eventResponse.setImagePath( eventEntity.getImagePath() );
        eventResponse.setStatus( eventEntity.isStatus() );
        eventResponse.setDescription( eventEntity.getDescription() );
        eventResponse.setAvgRate( eventEntity.getAvgRate() );

        if (!eventEntity.getSessionEntityList().isEmpty()) {
            eventResponse.setEventDate( eventEntity.getSessionEntityList().get(0).getDate() );

            eventResponse.setSessionId( eventEntity.getSessionEntityList().get(0).getSessionId() );
        }
        return eventResponse;
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
    public EventResponse getEventById(String eventId) {
        EventEntity eventEntity = eventRepository.findByEventId(eventId).orElseThrow(
                () -> new AppException(ErrorCode.EVENT_NOT_EXIST)
        );

        return toEventResponse(eventEntity);
    }

    @Override
    public Page<EventResponse> findEventsByName(String name, int page, int size) {
        Page<EventEntity> eventEntities =  eventRepository.findByEventNameContaining(name, PageRequest.of(page, size));
        List<EventResponse> eventResponses = toEventResponseList(eventEntities.getContent());
        return new PageImpl<>(eventResponses, eventEntities.getPageable(), eventEntities.getTotalElements());
    }

    @Override
    public void deleteEvent(String examId) {
        eventRepository.deleteById(examId);
    }


    @Override
    public EventResponse updateEventById(String eventId, EventUpdateRequest eventRequest, MultipartFile image) {
        // Lấy event từ database
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Cập nhật thông tin từ eventRequest
        event.setEventName(eventRequest.getEventName());
        event.setAddress(eventRequest.getAddress());
        event.setDescription(eventRequest.getDescription());

        // Xử lý ảnh nếu có upload file
        if (image != null && !image.isEmpty()) {
            String savedImagePath = saveImageToAssetsFolder(image);
            event.setImagePath(savedImagePath);
        } else if (eventRequest.getImagePath() != null) {
            event.setImagePath(eventRequest.getImagePath()); // Path ảnh cũ
        }

        return EventMapper.INSTANCE.toEventResponse(eventRepository.save(event));
    }

    private String saveImageToAssetsFolder(MultipartFile image) {
        try {
            // Đường dẫn thư mục lưu trữ ảnh
            String uploadDir = System.getProperty("user.dir") + "/frontend/public/event_image/";
            File dir = new File(uploadDir);

            // Tạo thư mục nếu chưa tồn tại
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Lưu ảnh vào thư mục
            String filePath = uploadDir + image.getOriginalFilename();
            File file = new File(filePath);
            image.transferTo(file);

            // Trả về đường dẫn tương đối để Vue sử dụng
            return "event_image/" + image.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    //get event feedback of one class (in all session of one class)
    @Override
    public List<EventResponse> findEventsOfOneClassInSchedule(String classId) {
        //get list of session have event of one class
        List<SessionEntity> sessionEntityList = sessionRepository.findSessionsHaveEvent(classId);

        //check empty
        if (sessionEntityList.isEmpty()) {
            throw new AppException(ErrorCode.NO_EVENT_IN_CLASS);
        }

        //get list of event
        List<EventEntity> eventEntityList = new ArrayList<>();
        for (SessionEntity session : sessionEntityList) {
            EventEntity event = session.getEventEntity();
            eventEntityList.add(event);
        }
        return toEventResponseList(eventEntityList);
    }

    @Override
    public List<EventResponse> findAvailableEvent(String classId) {
        List<EventEntity> eventEntityList = eventRepository.findEventsWithoutSessionsByClassIdNative(classId);
        return toEventResponseList(eventEntityList);

    }

}
