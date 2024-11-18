package com.nsg.service.imp;

import com.nsg.Mapper.ClassMapper;
import com.nsg.Mapper.ExamMapper;
import com.nsg.Mapper.LessonMapper;
import com.nsg.Mapper.TimeSlotMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.session.ScheduleCreationRequest;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

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

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    ClassRepository classRepository;


    @Override
    public void createSession(SessionCreattionRequest request) {
        SessionEntity session = new SessionEntity();

        session.setDate(request.getDate());
        session.setStatus(false);
        session.setSessionNumber(request.getSessionNumber());
        session.setSessionWeek(request.getSessionWeek());

        //class
        ClassEntity classEntity = classRepository.findByClassName(request.getClassName());

        if (classEntity == null) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        } else {
            session.setClassEntity(classEntity);
        }

        //lesson
        if (request.getLessionId() != null) {
            LessonEntity lesson = lessonRepository.findById(request.getLessionId()).orElseThrow(
                    () -> new AppException(ErrorCode.LESSON_NOT_FOUND)
            );
            session.setLessonEntity(lesson);
        } else {
            session.setLessonEntity(null);
        }

        if (request.getExamId() != null) {
            //exam
            ExamEntity examEntity = examRepository.findById(request.getExamId()).orElseThrow(
                    () -> new AppException(ErrorCode.EXAM_NOT_FOUND)
            );
            session.setExamEntity(examEntity);
        }else {
            session.setExamEntity(null);
        }

        //time slot
        if (request.getTimeSlotId() != null) {
            TimeSlotEntity timeSlotEntity = timeSlotRepository.findById(request.getTimeSlotId()).orElseThrow(
                    () -> new AppException(ErrorCode.TIME_SLOT_NOT_FOUND)
            );
            session.setTimeSlotEntity(timeSlotEntity);
        } else {
            session.setTimeSlotEntity(null);
        }

        //room
        if (!Objects.equals(request.getRoomNumber(), null)) {
            RoomEntity roomEntity = roomRepository.findByRoomNumber(request.getRoomNumber()).orElseThrow(
                    () -> new AppException(ErrorCode.ROOM_NOT_FOUND)
            );
            session.setRoomEntity(roomEntity);
        } else {
            session.setRoomEntity(null);
        }

        //event
        if (!Objects.equals(request.getEventId(), null)) {
            EventEntity eventEntity = eventRepository.findByEventId(request.getEventId()).orElseThrow(
                    () -> new AppException(ErrorCode.EVENT_NOT_FOUND)
            );
            session.setEventEntity(eventEntity);
        } else {
            session.setEventEntity(null);
        }

        //teacher
        if (!Objects.equals(request.getUserId(), null)) {
            UserEntity teacher = userRepository.findById(request.getUserId()).orElseThrow(
                    () -> new AppException(ErrorCode.USER_NOT_FOUND)
            );
            session.setUser(teacher);
        } else {
            session.setUser(null);
        }
        sessionRepository.save(session);
    }

    @Override
    public void createSchedule(String batchName, String className, ScheduleCreationRequest request) {

        //get batch and count days in batch
        BatchEntity batchEntity = batchRepository.findByBatchName(batchName).orElseThrow(
                () -> new AppException(ErrorCode.BATCH_NOT_EXISTED)
        );

        LocalDate startDate = batchEntity.getStartTime();

        System.out.println("Start date: "+startDate);

        int sessionNo = 1;
        int totalDay = 1;
        int week = 0;


        while (sessionNo < 46) {
            LocalDate dateOfSession = startDate.plusDays((totalDay-1));
            System.out.print("Date of "+(sessionNo)+" session: "+dateOfSession+" ::: ");
            //check date: 20/11

            //tao session voi date
            SessionCreattionRequest sessionCreattionRequest = new SessionCreattionRequest();
            sessionCreattionRequest.setDate(dateOfSession);
            sessionCreattionRequest.setClassName(className);

            sessionCreattionRequest.setSessionNumber(totalDay);

            DayOfWeek dow = dateOfSession.getDayOfWeek();

            if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
                System.out.print(dow+" là ngày cuối tuần!\n");
                //tao session rong
                //session k tang
                if (dow == DayOfWeek.SUNDAY)
                    week += 1;

            } else {
                System.out.print(dow+" không phải là ngày cuối tuần.\n");
                //tao session voi lesson va exam va ngay la: dateOfSession
                sessionCreattionRequest.setTimeSlotId(request.getTimeSlotId());
                sessionCreattionRequest.setRoomNumber(request.getRoomNumber());

                //session tang 1
                sessionNo += 1;
            }

            sessionCreattionRequest.setSessionWeek(week);
            totalDay += 1;

            //save session
            createSession(sessionCreattionRequest);
        }
        System.out.println("Total days: "+totalDay);

    }

    @Override
    public Page<SessionResponse> getAllSession(int page, int size) {
        //get all
        Page<SessionEntity> sessionEntities = sessionRepository.findAll(PageRequest.of(page, size));

        List<SessionResponse> responseList = new ArrayList<>();

        for (SessionEntity session : sessionEntities) {
            SessionResponse tempResponse = new SessionResponse();
            tempResponse.setSessionId(session.getSessionId());
            tempResponse.setSessionNumber(session.getSessionNumber());

            LocalDate localDate = session.getDate();
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();

            tempResponse.setDayOfWeek(dayOfWeek);

            tempResponse.setDate(session.getDate());
            tempResponse.setStatus(session.isStatus());

            tempResponse.setClassResponse(ClassMapper.INSTANCE.toClassResponse(session.getClassEntity()));

            tempResponse.setLessonResponse(LessonMapper.INSTANCE.toLessonResponse(session.getLessonEntity()));

            tempResponse.setTimeSlotResponse(TimeSlotMapper.INSTANCE.toTimeSlotResponse(session.getTimeSlotEntity()));

            if (session.getRoomEntity() != null) {
                tempResponse.setRoomNumber(session.getRoomEntity().getRoomNumber());
            } else {
                tempResponse.setRoomNumber(null);
            }

            tempResponse.setExamResponse(ExamMapper.INSTANCE.toExamResponse(session.getExamEntity()));

            if (session.getEventEntity() != null) {
                tempResponse.setEventName(session.getEventEntity().getEventName());
            }else {
                tempResponse.setEventName(null);
            }

            if (session.getUser() != null) {
                tempResponse.setFullName(session.getUser().getFullName());
                tempResponse.setEmail(session.getUser().getEmail());
            } else {
                tempResponse.setFullName(null);
                tempResponse.setEmail(null);
            }

            responseList.add(tempResponse);
        }
        Collections.sort(responseList, Comparator.comparingInt(SessionResponse::getSessionNumber));
        return new PageImpl<>(responseList, sessionEntities.getPageable(), sessionEntities.getTotalElements());
    }

    @Override
    public List<SessionResponse> getSessionByClassAndWeek(int week) {
        //get all
        List<SessionEntity> sessionEntities = sessionRepository.findBySessionWeek(week);

        List<SessionResponse> responseList = new ArrayList<>();

        for (SessionEntity session : sessionEntities) {
            SessionResponse tempResponse = new SessionResponse();
            tempResponse.setSessionId(session.getSessionId());
            tempResponse.setSessionNumber(session.getSessionNumber());
            tempResponse.setSessionWeek(session.getSessionWeek());

            LocalDate localDate = session.getDate();
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();

            tempResponse.setDayOfWeek(dayOfWeek);

            tempResponse.setDate(session.getDate());
            tempResponse.setStatus(session.isStatus());

            tempResponse.setClassResponse(ClassMapper.INSTANCE.toClassResponse(session.getClassEntity()));

            tempResponse.setLessonResponse(LessonMapper.INSTANCE.toLessonResponse(session.getLessonEntity()));

            tempResponse.setTimeSlotResponse(TimeSlotMapper.INSTANCE.toTimeSlotResponse(session.getTimeSlotEntity()));

            if (session.getRoomEntity() != null) {
                tempResponse.setRoomNumber(session.getRoomEntity().getRoomNumber());
            } else {
                tempResponse.setRoomNumber(null);
            }

            tempResponse.setExamResponse(ExamMapper.INSTANCE.toExamResponse(session.getExamEntity()));

            if (session.getEventEntity() != null) {
                tempResponse.setEventName(session.getEventEntity().getEventName());
            }else {
                tempResponse.setEventName(null);
            }

            if (session.getUser() != null) {
                tempResponse.setFullName(session.getUser().getFullName());
                tempResponse.setEmail(session.getUser().getEmail());
            } else {
                tempResponse.setFullName(null);
                tempResponse.setEmail(null);
            }

            responseList.add(tempResponse);
        }
        Collections.sort(responseList, Comparator.comparingInt(SessionResponse::getSessionNumber));
        return responseList;
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
