package com.nsg.service.imp;

import com.nsg.Mapper.*;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.session.ScheduleCreationRequest;
import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.entity.*;
import com.nsg.repository.*;
import com.nsg.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
            LessonEntity lesson = lessonRepository.findById(request.getLessionId()).orElse(null);
            session.setLessonEntity(lesson);
        } else {
            session.setLessonEntity(null);
        }

        if (request.getExamId() != null) {
            //exam
            ExamEntity examEntity = examRepository.findById(request.getExamId()).orElse(null);
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

                //get lesson, exam
                sessionCreattionRequest.setLessionId(String.valueOf(sessionNo));
                sessionCreattionRequest.setExamId(String.valueOf( (sessionNo-1) ));

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
    public List<SessionResponse> getSessionByClassAndWeek(int week, String className) {
        //get all
        List<SessionEntity> sessionEntities = sessionRepository.findBySessionWeekAndClassEntityClassName(week, className);

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
        //sort
        Collections.sort(responseList, Comparator.comparingInt(SessionResponse::getSessionNumber));
        return responseList;
    }

    @Override
    public SessionResponse getSession(String sessionId) {

        //get session
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        //mapping
        SessionResponse response = new SessionResponse();
        response.setSessionId(sessionEntity.getSessionId());
        response.setSessionNumber(sessionEntity.getSessionNumber());
        response.setSessionWeek(sessionEntity.getSessionWeek());

        LocalDate localDate = sessionEntity.getDate();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        response.setDayOfWeek(dayOfWeek);


        response.setDate(sessionEntity.getDate());
        response.setStatus(sessionEntity.isStatus());

        response.setClassResponse(ClassMapper.INSTANCE.toClassResponse(sessionEntity.getClassEntity()));

        response.setLessonResponse(LessonMapper.INSTANCE.toLessonResponse(sessionEntity.getLessonEntity()));

        response.setTimeSlotResponse(TimeSlotMapper.INSTANCE.toTimeSlotResponse(sessionEntity.getTimeSlotEntity()));

        if (sessionEntity.getRoomEntity() != null) {
            response.setRoomNumber(sessionEntity.getRoomEntity().getRoomNumber());
        } else {
            response.setRoomNumber(null);
        }

        response.setExamResponse(ExamMapper.INSTANCE.toExamResponse(sessionEntity.getExamEntity()));

        if (sessionEntity.getEventEntity() != null) {
            response.setEventName(sessionEntity.getEventEntity().getEventName());
        }else {
            response.setEventName(null);
        }

        if (sessionEntity.getUser() != null) {
            response.setFullName(sessionEntity.getUser().getFullName());
            response.setEmail(sessionEntity.getUser().getEmail());
        } else {
            response.setFullName(null);
            response.setEmail(null);
        }

        return response;
    }

    @Override
    public SessionResponse updateSession(String sessionId, SessionCreattionRequest request) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        //mapping
        sessionEntity.setSessionNumber(request.getSessionNumber());
        sessionEntity.setSessionWeek(request.getSessionWeek());

        sessionEntity.setDate(request.getDate());
        sessionEntity.setStatus(request.isStatus());

        //class
        ClassEntity classEntity = classRepository.findByClassName(request.getClassName());
        sessionEntity.setClassEntity(classEntity);

        //lesson
        if (!Objects.equals(request.getLessionId(), "")) {
            sessionEntity.setLessonEntity(lessonRepository.findById(request.getLessionId()).orElseThrow(
                    () -> new AppException(ErrorCode.LESSON_NOT_FOUND)
            ));
        } else {
            sessionEntity.setLessonEntity(null);
        }

        //time slot
        if (!Objects.equals(request.getTimeSlotId(), "")) {
            sessionEntity.setTimeSlotEntity(timeSlotRepository.findById(request.getTimeSlotId()).orElseThrow(
                    () -> new AppException(ErrorCode.TIME_SLOT_NOT_FOUND))
            );
        } else {
            sessionEntity.setTimeSlotEntity(null);
        }

        //room
        if (!Objects.equals(request.getRoomNumber(), "")) {
            sessionEntity.setRoomEntity(roomRepository.findByRoomNumber(request.getRoomNumber()).orElseThrow(
                    () -> new AppException(ErrorCode.ROOM_NOT_FOUND))
            );
        } else {
            sessionEntity.setRoomEntity(null);
        }

        //exam
        if (!Objects.equals(request.getExamId(), "")) {
            sessionEntity.setExamEntity(examRepository.findById(request.getExamId()).orElseThrow(
                    () -> new AppException(ErrorCode.EXAM_NOT_FOUND))
            );
        } else {
            sessionEntity.setEventEntity(null);
        }

        //event
        if (!Objects.equals(request.getEventId(), "")) {
            sessionEntity.setEventEntity(eventRepository.findByEventId(request.getEventId()).orElseThrow(
                    () -> new AppException(ErrorCode.EVENT_NOT_FOUND))
            );
        } else {
            sessionEntity.setEventEntity(null);
        }

        //teacher
        if (!Objects.equals(request.getUserId(), "")) {
            sessionEntity.setUser(userRepository.findById(request.getUserId()).orElseThrow(
                    () -> new AppException(ErrorCode.USER_NOT_FOUND)
            ));
        } else {
            sessionEntity.setUser(null);
        }

        //save
        sessionRepository.save(sessionEntity);

        return getSession(sessionId);
    }

    @Override
    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}
