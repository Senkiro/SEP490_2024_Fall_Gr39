package com.nsg.service.imp;

import com.nsg.Mapper.*;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.session.ScheduleCreationRequest;
import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.*;
import com.nsg.repository.*;
import com.nsg.service.CurriculumnService;
import com.nsg.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    CurriculumnRepository curriculumnRepository;

    @Autowired
    CurriculumnService curriculumnService;

    @Autowired
    CurriculumnListRepository curriculumnListRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    HolidayRepository holidayRepository;

    @Autowired
    TeacherRepository teacherRepository;


    @Override
    public void createSession(SessionCreattionRequest request) {
        SessionEntity session = new SessionEntity();

        session.setDate(request.getDate());
        session.setStatus(false);
        session.setSessionAvailable(request.isSessionAvailable());

        session.setSessionNumber(request.getSessionNumber());
        session.setSessionWeek(request.getSessionWeek());

        //class
        ClassEntity classEntity = classRepository.findById(request.getClassId()).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );
        session.setClassEntity(classEntity);

//        //curriculumn
//        if (request.getCurriculumnId() >= 0) {
//            CurriculumnEntity curriculumnEntity = curriculumnRepository.findById(String.valueOf(request.getCurriculumnId())).orElseThrow(
//                    () -> new AppException(ErrorCode.CURRICULUMN_NOT_FOUND)
//            );
//            session.setCurriculumnEntity(curriculumnEntity);
//        } else {
//            session.setCurriculumnEntity(null);
//        }

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
    public void createSchedule(String class_id, ScheduleCreationRequest request) {
        //get session by className and batchName
        //check class: find class by className and batchName
        ClassEntity classEntity = classRepository.findById(class_id).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );

        //check schedule for one class existed by: find class by id
        //if existed -> throw exception
        if (sessionRepository.existsByClassEntityClassId(classEntity.getClassId())) {
            throw new AppException(ErrorCode.SCHEDULE_EXISTED);
        }

        //get batch and count days in batch
        BatchEntity batchEntity = batchRepository.findByBatchName(classEntity.getBatchEntity().getBatchName()).orElseThrow(
                () -> new AppException(ErrorCode.BATCH_NOT_EXISTED)
        );

        LocalDate startDate = batchEntity.getStartTime();

        //get curriculumn list
        CurriculumnListEntity curriculumnListEntity = curriculumnListRepository.findById(String.valueOf(request.getCurriculumnListId())).orElseThrow(
                () -> new AppException(ErrorCode.CURRICULUMN_LIST_NOT_FOUND)
        );

        //get list of curriculumn inside curriculumnList entity
        List<CurriculumnEntity> curriculumnEntityList = curriculumnListEntity.getCurriculumnEntityList();

        //get 2 time_slot entity
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findAll();
        String morning = timeSlotRepository.findByName("Morning").getTimeSlotId();
        String afternoon = timeSlotRepository.findByName("Afternoon").getTimeSlotId();

        //check time_slot
        TimeSlotEntity check = timeSlotRepository.findById(request.getTimeSlotId()).orElseThrow(
                () -> new AppException(ErrorCode.TIME_SLOT_NOT_FOUND)
        );

        //get list of holiday
        List<HolidayEntity> holidays = holidayRepository.findAll();

        //get timeslot would be set to all

        int sessionNo = 1; //count session available by day
        int totalDay = 1; //count days
        int week = 0; //count week
        int count_ts = 1; //count session by time_slot

        int total_session = curriculumnEntityList.size();

        //create session by day and time_slot
        while (sessionNo <= total_session) {
            LocalDate dateOfSession = startDate.plusDays((totalDay-1));
            //create session
            SessionCreattionRequest sessionCreattionRequest = new SessionCreattionRequest();
            sessionCreattionRequest.setDate(dateOfSession);
            sessionCreattionRequest.setClassId(classEntity.getClassId());
            DayOfWeek dow = dateOfSession.getDayOfWeek();

            sessionCreattionRequest.setSessionNumber(count_ts);

            //if %count_ts = 1 -> morning
            if (count_ts % 2 == 1) {
                sessionCreattionRequest.setTimeSlotId(morning);
            }
            //else if % count_ts = 0 -> afternoon
            else if (count_ts % 2 == 0) {
                sessionCreattionRequest.setTimeSlotId(afternoon);
            }


            if (dow == DayOfWeek.SATURDAY
                    || dow == DayOfWeek.SUNDAY) {
                //create blank session
                sessionCreattionRequest.setSessionAvailable(false);


            } else if (checkHoliday(holidays, dateOfSession)) {
                //create blank session
                sessionCreattionRequest.setSessionAvailable(false);
            } else {

                sessionCreattionRequest.setRoomNumber(request.getRoomNumber());

                //if time_slot_id = requestId -> set available is true
                if (request.getTimeSlotId().equals(sessionCreattionRequest.getTimeSlotId())){
                    sessionCreattionRequest.setSessionAvailable(true);
                } else {
                    sessionCreattionRequest.setSessionAvailable(false);
                }

                //week break
                if (dow == DayOfWeek.MONDAY && count_ts % 2 == 0){
                    week += 1;
                }

                //session tang 1
                if (count_ts % 2 == 0) {
                    sessionNo++;
                }
            }

            sessionCreattionRequest.setSessionWeek(week);

            //if %count_ts = 0 then total day increase 1
            if (count_ts % 2 == 0) {
                totalDay++;
            }

            //save session
            createSession(sessionCreattionRequest);
            count_ts++;
        }
        System.out.println("Total days: "+totalDay);

        //update total week and end date for batch
        batchEntity.setTotalWeek(week);
        LocalDate endDate = startDate.plusDays((totalDay-1));
        batchEntity.setEndTime(endDate);

        batchRepository.save(batchEntity);

        //fill session
        fillSession(class_id, curriculumnEntityList);
    }


    //fill session: only fill available session
    public void fillSession(String classId, List<CurriculumnEntity> curriculumnEntityList) {
        //get all session available by classId, then sort it by day and sessionNumber
        List<SessionEntity> availableSessions = sessionRepository.findSessionsByClassIdAndAvailableAndStatus(classId);

        //if available sesssion list null
        if (availableSessions.isEmpty()) {
            throw new AppException(ErrorCode.SESSION_LIST_EMPTY);
        }

        int count_curriculumn = 0;

        for (SessionEntity session : availableSessions) {
            session.setCurriculumnEntity(curriculumnEntityList.get(count_curriculumn));
            count_curriculumn += 1;
        }

        //save all
        sessionRepository.saveAll(availableSessions);

    }

    //add event -> session change to available

    //check one day is holiday or not?
    public boolean checkHoliday(List<HolidayEntity> holidays, LocalDate dateOfSession) {
        for (HolidayEntity holiday: holidays) {
            if (holiday.getHolidayDate().equals(dateOfSession)) {
                return true;
            }
        }
        return false;
    }

    //get session by session id and time_slot_id


    //get available teacher for each session
    @Override
    public List<UserInforResponse> getAvailableTeachers(String sessionId) {

        //get date and timeSlotId
        SessionEntity session = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        List<UserEntity> listTeacher = teacherRepository.findAvailableTeachers(session.getDate(),
                session.getTimeSlotEntity().getTimeSlotId());

        List<UserInforResponse> responseList = new ArrayList<>();

        for (UserEntity user: listTeacher) {
            UserInforResponse temp = UserMapper.INSTANCE.toUserInforResponse(user);
            responseList.add(temp);
        }

        return responseList;
    }

    @Override
    public List<SessionResponse> getSessionUnavailable(String classId) {
        List<SessionEntity> sessionEntities = sessionRepository.findSessionsUnavailableByClassId(classId);

        return toListSessionResponse(sessionEntities);
    }


    @Override
    public void deleteSchedule(String classId) {
        //delete all session have class_id equal to class id receive
        //find session by class id
        List<SessionEntity> sessionEntities = sessionRepository.findByClassEntityClassId(classId);

        //for: delete each session in list
        for (SessionEntity session : sessionEntities) {

            //get session id
            String sessionId = session.getSessionId();

            deleteSession(sessionId);

        }
    }

    @Override
    public Page<SessionResponse> getAllSession(int page, int size) {
        //get all
        Page<SessionEntity> sessionEntities = sessionRepository.findAll(PageRequest.of(page, size));

        List<SessionResponse> responseList = toListSessionResponse(sessionEntities.getContent());
        return new PageImpl<>(responseList, sessionEntities.getPageable(), sessionEntities.getTotalElements());
    }

    @Override
    public List<SessionResponse> getSessionByClassAndWeek(int week, String classId) {
        //get list of session by week and classId
        List<SessionEntity> sessionEntities = sessionRepository.findBySessionWeekAndClassEntityClassId(week, classId);

        return toListSessionResponse(sessionEntities);
    }

    @Override
    public List<SessionResponse> getSessionByClassAndTeacher(String teacherId) {

        if (userRepository.findById(teacherId).isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        //get list of session by week and classId
        List<SessionEntity> sessionEntities = sessionRepository.findByUserId(teacherId);
        if (!sessionEntities.isEmpty()) {
            return toListSessionResponse(sessionEntities);
        } else {
            return null;
        }
    }

    public List<SessionResponse> toListSessionResponse(List<SessionEntity> sessionEntities) {

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
            tempResponse.setSessionAvailable(session.isSessionAvailable());

            tempResponse.setClassResponse(ClassMapper.INSTANCE.toClassResponse(session.getClassEntity()));

            //check null
            if (session.getCurriculumnEntity() != null) {
                tempResponse.setCurriculumnResponse(curriculumnService.toCurriculumnResponse( session.getCurriculumnEntity() ));
            }

            tempResponse.setTimeSlotResponse(TimeSlotMapper.INSTANCE.toTimeSlotResponse(session.getTimeSlotEntity()));

            if (session.getRoomEntity() != null) {
                tempResponse.setRoomNumber(session.getRoomEntity().getRoomNumber());
            } else {
                tempResponse.setRoomNumber(null);
            }

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

        response.setCurriculumnResponse( curriculumnService.toCurriculumnResponse( sessionEntity.getCurriculumnEntity() ) );

        response.setTimeSlotResponse(TimeSlotMapper.INSTANCE.toTimeSlotResponse(sessionEntity.getTimeSlotEntity()));

        if (sessionEntity.getRoomEntity() != null) {
            response.setRoomNumber(sessionEntity.getRoomEntity().getRoomNumber());
        } else {
            response.setRoomNumber(null);
        }

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
    public void updateOnlySessionStatus(String sessionId) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );
        sessionEntity.setStatus(!sessionEntity.isStatus());
        sessionRepository.save(sessionEntity);
    }

    @Override
    public SessionResponse updateSession(String sessionId, SessionCreattionRequest request) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        //convert
        sessionEntity.setSessionNumber(request.getSessionNumber());
        sessionEntity.setSessionWeek(request.getSessionWeek());

        sessionEntity.setDate(request.getDate());
        sessionEntity.setStatus(request.isStatus());

        //class
        ClassEntity classEntity = classRepository.findById(request.getClassId()).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );
        sessionEntity.setClassEntity(classEntity);

        //curriculumn
        if (!Objects.equals(request.getCurriculumnId(), "")) {
            CurriculumnEntity curriculumn = curriculumnRepository.findById(String.valueOf(request.getCurriculumnId())).orElseThrow(
                    () -> new AppException(ErrorCode.CURRICULUMN_NOT_FOUND)
            );
            sessionEntity.setCurriculumnEntity(curriculumn);
        } else {
            sessionEntity.setCurriculumnEntity(null);
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

    @Transactional
    @Override
    public void deleteSession(String sessionId) {
        SessionEntity session = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        // Xóa dữ liệu liên quan (ví dụ: attendanceEntityList)
        attendanceRepository.deleteBySessionEntity(session);

        // Sau đó xóa session
        try {
            sessionRepository.deleteSessionBySessionId(session.getSessionId());
        } catch (Exception e) {
            System.out.println("ERRRRRRROR: "+e.getMessage());
            throw e;
        }
    }
}
