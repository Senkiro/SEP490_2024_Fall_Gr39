package com.nsg.service.imp;

import com.nsg.Mapper.ExamMapper;
import com.nsg.Mapper.LessonMapper;
import com.nsg.Mapper.TimeSlotMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.entity.*;
import com.nsg.repository.*;
import com.nsg.service.EventService;
import com.nsg.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SessionServiceImp implements SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public void createSession(SessionCreattionRequest request) {
        SessionEntity session = new SessionEntity();

        session.setDate(request.getDate());
        session.setStatus(false);

        //lesson
        LessonEntity lesson = lessonRepository.findById(request.getLessionId()).orElseThrow(
                () -> new AppException(ErrorCode.LESSON_NOT_FOUND)
        );
        session.setLessonEntity(lesson);

        //time slot
        TimeSlotEntity timeSlotEntity = timeSlotRepository.findById(request.getTimeSlotId()).orElseThrow(
                () -> new AppException(ErrorCode.TIME_SLOT_NOT_FOUND)
        );
        session.setTimeSlotEntity(timeSlotEntity);

        //room
        RoomEntity roomEntity = roomRepository.findByRoomNumber(request.getRoomNumber()).orElseThrow(
                () -> new AppException(ErrorCode.ROOM_NOT_FOUND)
        );
        session.setRoomEntity(roomEntity);

        //exam
        ExamEntity examEntity = examRepository.findById(request.getExamId()).orElseThrow(
                () -> new AppException(ErrorCode.EXAM_NOT_FOUND)
        );
        session.setExamEntity(examEntity);

        //event
        if (!Objects.equals(request.getEventId(), "")) {
            EventEntity eventEntity = eventRepository.findByEventId(request.getEventId()).orElseThrow(
                    () -> new AppException(ErrorCode.EVENT_NOT_FOUND)
            );
            session.setEventEntity(eventEntity);
        } else {
            session.setEventEntity(null);
        }

        //teacher
        UserEntity teacher = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        session.setUser(teacher);

        sessionRepository.save(session);
    }

    @Override
    public Page<SessionResponse> getAllSession(int page, int size) {
        //get all
        Page<SessionEntity> sessionEntities = sessionRepository.findAll(PageRequest.of(page, size));

        List<SessionResponse> responseList = new ArrayList<>();

        for (SessionEntity session : sessionEntities) {
            SessionResponse tempResponse = new SessionResponse();
            tempResponse.setSessionId(session.getSessionId());
            tempResponse.setDate(session.getDate());
            tempResponse.setStatus(session.isStatus());

            tempResponse.setLessonResponse(LessonMapper.INSTANCE.toLessonResponse(session.getLessonEntity()));

            tempResponse.setTimeSlotResponse(TimeSlotMapper.INSTANCE.toTimeSlotResponse(session.getTimeSlotEntity()));

            tempResponse.setRoomNumber(session.getRoomEntity().getRoomNumber());

            tempResponse.setExamResponse(ExamMapper.INSTANCE.toExamResponse(session.getExamEntity()));

            if (session.getEventEntity() != null) {
                tempResponse.setEventName(session.getEventEntity().getEventName());
            }else {
                tempResponse.setEventName(null);
            }

            tempResponse.setFullName(session.getUser().getFullName());
            tempResponse.setEmail(session.getUser().getEmail());

            responseList.add(tempResponse);
        }

        return new PageImpl<>(responseList, sessionEntities.getPageable(), sessionEntities.getTotalElements());
    }

    @Override
    public SessionResponse getSession(String sessionId) {
        return null;
    }

    @Override
    public SessionResponse updateSession(String sessionId, SessionCreattionRequest request) {
        return null;
    }

    @Override
    public void deleteSession(String sessionId) {

    }
}
